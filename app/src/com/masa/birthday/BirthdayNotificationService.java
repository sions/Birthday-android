package com.masa.birthday;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.masa.notifier.Email;
import com.masa.notifier.Notify;

public class BirthdayNotificationService extends IntentService {
	public BirthdayNotificationService() {
		super("BirthdayNotificationService");
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    //Toast.makeText(this, "service starting...", Toast.LENGTH_SHORT).show();
	    return super.onStartCommand(intent,flags,startId);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		BirthdaysDatabase db = new BirthdaysDatabase(this);
		List<BirthdayRecord> records = BirthdaysDatabase.readAll(db.getReadableDatabase());
		db.close();
		
		List<Email> emails = new ArrayList<Email>();
		for (BirthdayRecord record : records) {
			if (record.shouldFireNotification()) {
				emails.add(record.getEmail(getResources()));
			}
		}
		if (!emails.isEmpty()) {
			Notify.notify(this, emails);
		}
	}
}
