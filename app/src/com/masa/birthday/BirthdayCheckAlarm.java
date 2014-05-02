package com.masa.birthday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class BirthdayCheckAlarm  extends BroadcastReceiver {

	private static final int CHECK_INTERVAL_SECS = 60;

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Actually start the service that checks and fires notifications.
        context.startService(new Intent(context, BirthdayNotificationService.class));

        wl.release();
    }

	public void setAlarm(Context context) {
        AlarmManager am= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BirthdayCheckAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * CHECK_INTERVAL_SECS, pendingIntent);
    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, BirthdayCheckAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
