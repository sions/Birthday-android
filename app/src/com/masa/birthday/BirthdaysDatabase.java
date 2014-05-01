package com.masa.birthday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BirthdaysDatabase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String BIRTHDAYS_TABLE_NAME = "birthdays";
	private static final String NAME_COL = "name";
	private static final String BIRTHDAY_COL = "birthday";
	private static final String PHOTO_COL = "name";

	private static final String BIRTHDAYS_TABLE_CREATE = "CREATE TABLE "
			+ BIRTHDAYS_TABLE_NAME + " (" + NAME_COL + " TEXT, " + BIRTHDAY_COL
			+ " DATE, " + PHOTO_COL + " TEXT);";

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
}
