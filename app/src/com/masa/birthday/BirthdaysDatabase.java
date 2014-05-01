package com.masa.birthday;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("SimpleDateFormat")
public class BirthdaysDatabase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String BIRTHDAYS_TABLE_NAME = "birthdays";
	private static final String NAME_COL = "name";
	private static final String BIRTHDAY_COL = "birthday";
	private static final String PHOTO_COL = "photo";
	private static final String EMAIL_COL = "email";

	private static final String[] COLUMNS = { NAME_COL, BIRTHDAY_COL, PHOTO_COL, EMAIL_COL };

	private static final String BIRTHDAYS_TABLE_CREATE = "CREATE TABLE " + BIRTHDAYS_TABLE_NAME
			+ " (" + NAME_COL + " TEXT, " + BIRTHDAY_COL + " DATE, " + PHOTO_COL + " TEXT,"
			+ EMAIL_COL + " TEXT);";

	private static final SimpleDateFormat iso8601Format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	BirthdaysDatabase(Context context) {
		super(context, BIRTHDAYS_TABLE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BIRTHDAYS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public static void add(SQLiteDatabase db, BirthdayRecord record) {
		db.insert(BIRTHDAYS_TABLE_NAME, null, build(record));
	}

	public static void update(SQLiteDatabase db, BirthdayRecord oldRecord, BirthdayRecord newRecord) {
		remove(db, oldRecord);
		add(db, oldRecord);
	}

	public static void remove(SQLiteDatabase db, BirthdayRecord record) {
		db.delete(BIRTHDAYS_TABLE_NAME, WHERE, whereClauseArgs(record));
	}

	public static List<BirthdayRecord> readAll(SQLiteDatabase db) {
		List<BirthdayRecord> records = new ArrayList<BirthdayRecord>();
		Cursor cursor = db.query(BIRTHDAYS_TABLE_NAME, COLUMNS, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			BirthdayRecord comment = cursorToRecord(cursor);
			records.add(comment);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return records;
	}

	private static ContentValues build(BirthdayRecord record) {
		ContentValues values = new ContentValues();
		values.put(NAME_COL, record.getName());
		values.put(BIRTHDAY_COL, iso8601Format.format(record.getBirthday()));
		values.put(PHOTO_COL, record.getPhoto());
		values.put(EMAIL_COL, record.getEmail());
		return values;
	}

	private static final String WHERE = String.format("%s=? AND %s=? AND %s=? AND %s=?",
			COLUMNS[0], COLUMNS[1], COLUMNS[2], COLUMNS[3]);

	private static String[] whereClauseArgs(BirthdayRecord record) {
		return new String[] { record.getName(), iso8601Format.format(record.getBirthday()),
				record.getPhoto(), record.getEmail() };
	}
	
	private static BirthdayRecord cursorToRecord(Cursor cursor) {
		try {
			return new BirthdayRecord(cursor.getString(1), 
					iso8601Format.parse(cursor.getString(2)), 
					cursor.getString(3), 
					cursor.getString(4));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Bad value in db " + cursor.getString(2));
		}
	}
}
