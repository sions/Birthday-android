package com.masa.birthday;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.masa.notifier.Email;

public class MainActivity extends Activity {

	static final int GET_BIRTHDAY_RECORD_REQUEST = 1; // The request code.
	static final int ADD_BIRTHDAY_RECORD_REQUEST = 2; // The request code.


	ListView listView;
	List<BirthdayRecord> birthdays;
	BirthdayRecordAdapter adapter;
	BirthdayRecord currentRecord;

	public static class IntentBuilder {
		private Intent intent;

		public IntentBuilder(Context context, Class<?> activityClass) {
			intent = new Intent(context, activityClass);
		}

		public void setName(String value) {
			intent.putExtra("name", value);
		}

		public void setBirthday(Date value) {
			intent.putExtra("birthday", value);
		}

		public void setPhoto(String value) {
			intent.putExtra("photo", value);
		}

		public void setEmail(String value) {
			intent.putExtra("email", value);
		}

		public Intent build() {
			return intent;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize alarm that will execute service periodically. 
		BirthdayCheckAlarm alarm = new BirthdayCheckAlarm();
		alarm.setAlarm(this);

		TextView currentDate = (TextView) findViewById(R.id.today);
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(
				new Date());
		currentDate.setText(currentDateTimeString);
		
		

		listView = (ListView) findViewById(R.id.birthdays);

		BirthdaysDatabase db = new BirthdaysDatabase(this);
		birthdays = BirthdaysDatabase.readAll(db.getReadableDatabase());
		db.close();

		adapter = new BirthdayRecordAdapter(this, birthdays);
		listView.setAdapter(adapter);

		// To register the button with context menu.
		registerForContextMenu(listView);
	}

	private void openBirthdayEditor(BirthdayRecord record) {
		IntentBuilder builder = new IntentBuilder(this,
				EditBirthdayActivity.class);
		if (record != null) {
			builder.setName(record.getName());
			//builder.setBirthday(record.getBirthday());
			//builder.setPhoto(record.getPhoto());
			builder.setEmail(record.getEmail());
		}
		Intent intent = builder.build();
		if (record == null) {
			startActivityForResult(intent, ADD_BIRTHDAY_RECORD_REQUEST);

		} else {
			currentRecord = record;
			startActivityForResult(intent, GET_BIRTHDAY_RECORD_REQUEST);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			Log.e("error", "resultCode: " + resultCode);
			return;
		}
		
		BirthdaysDatabase db = new BirthdaysDatabase(this);
		SQLiteDatabase sql = db.getWritableDatabase();
		try {
			switch (requestCode) {
			// Check which request we're responding to
			case GET_BIRTHDAY_RECORD_REQUEST:
				birthdays.remove(currentRecord);
				BirthdaysDatabase.remove(sql, currentRecord);
				// No break because after remove we re-add.
			case ADD_BIRTHDAY_RECORD_REQUEST:
				// Make sure the request was successful
				Bundle bundle = data.getExtras();
				BirthdayRecord record = new BirthdayRecord(bundle);
				birthdays.add(record);
				BirthdaysDatabase.add(sql, record);
				this.adapter.notifyDataSetChanged();
				break;
			}
		} finally {
			db.close();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater m = getMenuInflater();
		m.inflate(R.menu.birthday_list_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = (int) info.position;

		switch (item.getItemId()) {
		case R.id.send: {
			BirthdayRecord record = birthdays.get(position);
			String subject = getResources().getString(
					R.string.happy_birthday_email_title, record.getName());
			String text = getResources().getString(
					R.string.happy_birthday_email_text);
			Email email = new Email(record.getEmail(), subject, text, record.getName());
			startActivity(email.createIntent());
		}
			break;
		case R.id.edit: {
			// Edit Action
			openBirthdayEditor(birthdays.get(position));
		}
			break;
		case R.id.delete: {
			BirthdaysDatabase db = new BirthdaysDatabase(this);
			try {
				SQLiteDatabase sql = db.getWritableDatabase();
				BirthdaysDatabase.remove(sql, birthdays.get(position));
				birthdays.remove(position);
				this.adapter.notifyDataSetChanged();
			} finally {
				db.close();
			}
		}
			break;
		}

		return super.onContextItemSelected(item);
	}

	public static final int MENU_ADD = Menu.FIRST;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_ADD, Menu.NONE, "Add");

		/*
		 * // Inflate the menu; this adds items to the action bar if it is
		 * present. getMenuInflater().inflate(R.menu.main, menu);
		 */
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD:
			openBirthdayEditor(null /* No current record to edit. */);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
