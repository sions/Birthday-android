package com.masa.notifier;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

public class Notify {
	
	private static final int INTENT_ID = 13;

	public static void notify(Context ctx, List<Email> emails) {
		Resources res = ctx.getResources();
		
		Notification.Builder builder = new Notification.Builder(ctx)
				.setSmallIcon(R.drawable.cake)
				.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.cake));
		if (emails.isEmpty()) {
			throw new IllegalArgumentException("Must suppply at least one email");
		}
		
		Intent intent;
		if (emails.size() == 1) {
			Email email = emails.get(0);
			builder.setContentTitle(email.getFirstName() + " has a birthday today!")
				.setContentText("Click to send happy birthday");
			intent = email.createIntent();
		} else {
			intent = new Intent();
			builder
				.setContentTitle(String.format("%d friends have a birthday today!", emails.size()))
				.setContentText("Click to send them happy birthday");
		}
		

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		builder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = 
				(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(INTENT_ID, builder.build());
	}
}
