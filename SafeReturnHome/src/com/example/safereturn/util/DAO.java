package com.example.safereturn.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DAO {

	// //
	public static final String DATABASE_NAME = "iknow.db";
	private static final int DATABASE_VERSION = 1;
	private static final String LOG_ERROR = "DAO";

	// USER TABLE
	// ==========================================================================================
	private static final String USER_TABLE = "user";
	public static final String USER_ID = "_id";
	public static final String USER_NAME = "name";
	public static final String USER_PHONENUM = "num";
	public static final String USER_ALLOW = "allow";

	private static final String USER_CREATE = "create table if not exists "
							  + USER_TABLE + "(" 
							  + USER_ID + " integer primary key autoincrement,"
							  + USER_NAME  + " text not null, "
							  + USER_PHONENUM + " text not null,"
							  + USER_ALLOW + " integer not null);";

	// if insert failed, return -1
	public long insertUser(String name, String number,
			boolean allowPositionSharing) {
		if (name == null || number == null) {
			new NullPointerException(LOG_ERROR);
		}

		int allowInt = 0;

		if (allowPositionSharing)
			allowInt = 1;
		else
			allowInt = 0;

		ContentValues cv = new ContentValues();
		cv.put(USER_NAME, name);
		cv.put(USER_PHONENUM, number);
		cv.put(USER_ALLOW, allowInt);
		return mDB.insert(USER_TABLE, null, cv);
	}

	public long updateUser(int id, String name, String number,
			boolean allowPositionSharing) {
		if (id == 0 || name == null || number == null) {
			new NullPointerException(LOG_ERROR);
		}

		int allowInt = 0;

		if (allowPositionSharing)
			allowInt = 1;
		else
			allowInt = 0;

		ContentValues cv = new ContentValues();
		cv.put(USER_NAME, name);
		cv.put(USER_PHONENUM, number);
		cv.put(USER_ALLOW, allowInt);
		return mDB.update(USER_TABLE, cv, USER_ID + " = ?", new String[] { ""+ id });
	}

	public int deleteUser(int id) {

		if (id == 0) {
			new NullPointerException(LOG_ERROR);
		}

		return mDB.delete(USER_TABLE, USER_ID + " = ?", new String[] { "" + id });
	}

	public Cursor selectAllUser() {
		Cursor cs = mDB.query(true, USER_TABLE, new String[] { 
															   USER_ID,
															   USER_NAME,
															   USER_PHONENUM, 
															   USER_ALLOW },
															   null, null, null, null,null, null);
		if (cs != null)
			cs.moveToFirst();
		return cs;
	}

	// SAFETY ZONE TABLE
	// ==========================================================================================
	private static final String ZONE_TABLE = "zone";
	public static final String ZONE_ID = "_id";
	public static final String ZONE_LATI = "latitude";
	public static final String ZONE_LONG = "longitude";

	private static final String ZONE_CREATE = "create table if not exists "
							  + ZONE_TABLE + "(" + ZONE_ID + " integer primary key autoincrement,"
							  + ZONE_LATI  + " DEC(19,6) not null, "
							  + ZONE_LONG + " DEC(19,6) not null);";

	// if insert failed, return -1
	public long insertZone(float lati, float longi) {
		
		if (lati == 0.0f || longi == 0.0) {
			new NullPointerException(LOG_ERROR);
		}
		
		ContentValues cv = new ContentValues();
		cv.put(ZONE_LATI, lati);
		cv.put(ZONE_LONG, longi);

		return mDB.insert(ZONE_TABLE, null, cv);
	}

	public long updateZone(int id, float lati, float longi) {
		
		if (id == 0 || lati == 0.0f || longi == 0.0f) {
			new NullPointerException(LOG_ERROR);
		}
		
		ContentValues cv = new ContentValues();
		cv.put(ZONE_LATI, lati);
		cv.put(ZONE_LONG, longi);

		return mDB.update(ZONE_TABLE, cv, ZONE_ID + " = ?", new String[] { ""+ id });
	}

	public Cursor selectAllZone() {
		Cursor cs = mDB.query(true, ZONE_TABLE, new String[] { 
																ZONE_ID,
																ZONE_LATI, 
																ZONE_LONG },
																null, null, null, null, null, null);
		if (cs != null)
			cs.moveToFirst();
		return cs;
	}

	public int deleteZone(int id) {

		if (id == 0) {
			new NullPointerException(LOG_ERROR);
		}

		return mDB.delete(ZONE_TABLE, ZONE_ID + " = ?",
				new String[] { "" + id });
	}

	// SAFETY TIME TABLE
	// ==========================================================================================
	private static final String TIME_TABLE = "time";
	public static final String TIME_ID = "_id";
	public static final String TIME_START_HOUR = "shour";
	public static final String TIME_START_MINUTE = "sminute";
	public static final String TIME_END_HOUR = "ehour";
	public static final String TIME_END_MINUTE = "eminute";

	private static final String TIME_CREATE = "create table if not exists "
							  + TIME_TABLE
							  + "("
							  + TIME_ID+ " integer primary key autoincrement,"
							  + TIME_START_HOUR  + " integer not null, "
							  + TIME_START_MINUTE + " integer not null,"
							  + TIME_END_HOUR + " integer not null, "
							  + TIME_END_MINUTE + " integer not null);";

	// if insert failed, return -1
	public long insertTime(int sHour, int sMinute, int eHour, int eMinute) {
		
		ContentValues cv = new ContentValues();
		cv.put(TIME_START_HOUR, sHour);
		cv.put(TIME_START_MINUTE, sMinute);
		cv.put(TIME_END_HOUR, eHour);
		cv.put(TIME_END_MINUTE, eMinute);

		return mDB.insert(TIME_TABLE, null, cv);
	}

	public long updateTime(int id, int sHour, int sMinute, int eHour, int eMinute) {
		
		if (id == 0) {
			new NullPointerException(LOG_ERROR);
		}
		
		ContentValues cv = new ContentValues();
		cv.put(TIME_START_HOUR, sHour);
		cv.put(TIME_START_MINUTE, sMinute);
		cv.put(TIME_END_HOUR, eHour);
		cv.put(TIME_END_MINUTE, eMinute);

		return mDB.update(TIME_TABLE, cv, TIME_ID + " = ?", new String[] { ""
				+ id });
	}
	
	public Cursor selectAllTime() {
		Cursor cs = mDB.query(true, TIME_TABLE, new String[] {
																TIME_ID,
																TIME_START_HOUR, 
																TIME_START_MINUTE, 
																TIME_END_HOUR, 
																TIME_END_MINUTE },
																null, null, null, null, null, null);
		if (cs != null)
			cs.moveToFirst();
		return cs;
	}
	
	public int deleteTime(int id) {

		if (id == 0) {
			new NullPointerException(LOG_ERROR);
		}

		return mDB.delete(TIME_TABLE, TIME_ID + " = ?",
				new String[] { "" + id });
	}
	
	
	// MESSAGE TABLE
	// ==========================================================================================
	private static final String MSG_TABLE = "msg";
	public static final String MSG_ID = "_id";
	public static final String MSG_USER_ID = "userid";
	public static final String MSG_CONTENT = "context";
	public static final String MSG_CREATED_HOUR = "hour";
	public static final String MSG_CREATED_MINUTE = "minute";

	private static final String MSG_CREATE = "create table if not exists "
							  + MSG_TABLE + "(" 
							  + MSG_ID + " integer primary key autoincrement,"
							  + MSG_USER_ID + " integer not null,"
							  + MSG_CONTENT + " text not null, "
							  + MSG_CREATED_HOUR + " integer not null,"
							  + MSG_CREATED_MINUTE + " integer not null);";

	// if insert failed, return -1
	public long insertMsg(int user_id, String content, int createH, int createM) {
		
		if (user_id == 0 || content == null) {
			new NullPointerException(LOG_ERROR);
		}
		
		ContentValues cv = new ContentValues();
		cv.put(MSG_USER_ID, user_id);
		cv.put(MSG_CONTENT, content);
		cv.put(MSG_CREATED_HOUR, createH);
		cv.put(MSG_CREATED_MINUTE, createM);

		return mDB.insert(MSG_TABLE, null, cv);
	}

public long updateMsg(int id, int user_id, String content, int createH, int createM) {
		
		if (id == 0 || user_id == 0 || content == null) {
			new NullPointerException(LOG_ERROR);
		}
		
		ContentValues cv = new ContentValues();
		cv.put(MSG_USER_ID, user_id);
		cv.put(MSG_CONTENT, content);
		cv.put(MSG_CREATED_HOUR, createH);
		cv.put(MSG_CREATED_MINUTE, createM);

		return mDB.update(MSG_TABLE, cv, MSG_ID + " = ?", new String[] { ""+ id });
	}

	
	public Cursor selectAllMsg() {
		Cursor cs = mDB.query(true, MSG_TABLE, new String[] { 
															  MSG_ID, 
															  MSG_USER_ID,
															  MSG_CONTENT, 
															  MSG_CREATED_HOUR, 
															  MSG_CREATED_MINUTE }, 
															  null, null, null, null, null, null);
		if (cs != null)
			cs.moveToFirst();
		return cs;
	}

	public int deleteMsg(int id) {
		if (id == 0) {
			new NullPointerException(LOG_ERROR);
		}

		return mDB.delete(MSG_TABLE, MSG_ID + " = ?", new String[] { "" + id });
	}

	//
	// ==========================================================================================

	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;
	private final Context context;

	public DAO(Context context) {
		this.context = context;
	}

	/*
	 * public DAO delete() throws SQLException{ mDBHelper = new
	 * DBHelper(context); }
	 */
	public DAO open() throws SQLException {
		mDBHelper = new DBHelper(context);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDBHelper.close();
	}

	// ///////////////////////////////////////////////////////inner class
	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(USER_CREATE);
			db.execSQL(ZONE_CREATE);
			db.execSQL(TIME_CREATE);
			db.execSQL(MSG_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(LOG_ERROR, "Upgrading db from version" + oldVersion + " to"
					+ newVersion + ", which will destroy all old data");

			db.execSQL("drop table if exists " + USER_TABLE);
			db.execSQL("drop table if exists " + ZONE_TABLE);
			db.execSQL("drop table if exists " + TIME_TABLE);
			db.execSQL("drop table if exists " + MSG_TABLE);

			onCreate(db);
		}
	}
	// ///////////////////////////////////////////////////////inner class
}
