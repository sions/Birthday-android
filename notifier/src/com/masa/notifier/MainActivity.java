package com.masa.notifier;

import java.util.ArrayList;
import java.util.List;

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

	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		final Button notifyButton = (Button) findViewById(R.id.notify_button);
		notifyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<Email> emails = new ArrayList<Email>();
				emails.add(new Email("bla@gmail.com", "Test" + count, "Testing", "Miri"));
				if (count % 2 == 1) {
					emails.add(new Email("foo@gmail.com", "2nd Test" + count, "Testing", "Alina"));
				}
				
				Notify.notify(v.getContext(), emails);
				++count;
			}
		});

	}

}
