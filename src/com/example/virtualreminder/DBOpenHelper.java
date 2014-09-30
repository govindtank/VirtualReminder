package com.example.virtualreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	//Declare some useful variables
	private final static String DATABASE_NAME = "itcast.db";
	private final static int DATABASE_VERSION = 5;
	private final static String TABLE_NAME = "Reminder_table";
	public final static String FIELD_id = "event_id";
	public final static String FIELD_TEXT = "event_text";
	
	
	public DBOpenHelper(Context context) {
		//When created, will be saved under database/
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Applied when the database first created
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + FIELD_id
				+ " INTEGER primary key autoincrement, " + " " + FIELD_TEXT
				+ " varchar(500), date varchar(100), time varchar(100),"
				+ " video varchar(100), recorder varchar(100), time_int INTEGER )";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int OldVersion, int NewVersion) {
		// Applied when the version number changes
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

}
