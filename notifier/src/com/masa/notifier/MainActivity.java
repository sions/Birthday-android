package com.masa.notifier;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends Activity {

	private static final int INTENT_ID = 13;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		final Button notifyButton = (Button) findViewById(R.id.notify_button);
		notifyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Resources res = v.getResources();
				
				Notification.Builder builder = new Notification.Builder(v.getContext())
						.setContentTitle("Miri has a birthday today.")
						.setContentText("Click to send happy birthday")
						.setSmallIcon(R.drawable.cake)
						.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.cake));
				
				// Creates an explicit intent for an Activity in your app
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"sion.schori@gmail.com"});
				i.putExtra(Intent.EXTRA_SUBJECT, "Happy Birthday!");
				i.putExtra(Intent.EXTRA_TEXT   , "Miri has a birthday!");

				TaskStackBuilder stackBuilder = TaskStackBuilder.create(v.getContext());
				// Adds the Intent that starts the Activity to the top of the stack
				stackBuilder.addNextIntent(i);
				PendingIntent resultPendingIntent =
				        stackBuilder.getPendingIntent(
				            0,
				            PendingIntent.FLAG_UPDATE_CURRENT
				        );
				builder.setContentIntent(resultPendingIntent);

				
				NotificationManager mNotificationManager = 
						(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(INTENT_ID, builder.build());
			}
		});

	}

}
