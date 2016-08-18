package com.great.stb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SmartSTBSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "SmartSTBSQLiteOpenHelper";

	/**
	 * 数据库的构造方法 用来定义数据库的名称 数据库查询的结果集 数据库的版本
	 *
	 * @param context
	 */
	public SmartSTBSQLiteOpenHelper(Context context) {
		super(context, "smartSTB.db", null, 5);
	}

	/**
	 * 数据库第一次被创建的时候 调用的方法.
	 *
	 * @param db
	 *            被创建的数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 初始化数据库的表结构

		db.execSQL("CREATE TABLE IF NOT EXISTS elementinfo "
				+ "(id integer primary key autoincrement,"
				+ "code varchar(50)," + "version varchar(50),"
				+ "image varchar(200)," + "smallImage varchar(200),"
				+ "popupImage varchar(200)," + "popupTime varchar(4),"
				+ "filePath varchar(200)," + "appName varchar(200),"
				+ "title varchar(100)," + "pic_large varchar(200),"
				+ "mediaId varchar(20)," + "argument varchar(255) DEFAULT '',"
				+ "txt_intro varchar(200)," + "actType varchar(50))");

		db.execSQL("CREATE TABLE IF NOT EXISTS actdata "
				+ "(id integer primary key autoincrement,"
				+ "code varchar(50)," + "ordered varchar(50),"
				+ "filePath varchar(200))");

		db.execSQL("CREATE TABLE IF NOT EXISTS active "
				+ "(id integer primary key autoincrement,"
				+ "activeState varchar(10)," + "box_id varchar(50),"
				+ "mac varchar(50))");

		/* 黑名单,数据库版本3升4时创建 */
		db.execSQL("CREATE TABLE IF NOT EXISTS blacklist"
				+ "(id integer primary key autoincrement,"
				+ "packname varchar(200))");
	}

	/**
	 * 当数据库的版本号发生变化的时候(增加的时候) 调用.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "数据库的版本变化了...oldVersion=" + oldVersion);
		if (oldVersion == 1) {
			db.execSQL("ALTER TABLE elementinfo ADD mediaId varchar(20)");
			db.execSQL("ALTER TABLE elementinfo ADD argument varchar(255) DEFAULT ''");
			db.execSQL("CREATE TABLE IF NOT EXISTS blacklist"
					+ "(id integer primary key autoincrement,"
					+ "packname varchar(200))");
			db.execSQL("ALTER TABLE elementinfo ADD popupImage varchar(200)");
			db.execSQL("ALTER TABLE elementinfo ADD popupTime varchar(4)");
		} else if (oldVersion == 2) {
			db.execSQL("ALTER TABLE elementinfo ADD argument varchar(255) DEFAULT ''");
			db.execSQL("CREATE TABLE IF NOT EXISTS blacklist"
					+ "(id integer primary key autoincrement,"
					+ "packname varchar(200))");
			db.execSQL("ALTER TABLE elementinfo ADD popupImage varchar(200)");
			db.execSQL("ALTER TABLE elementinfo ADD popupTime varchar(4)");
		} else if (oldVersion == 3) {
			db.execSQL("CREATE TABLE IF NOT EXISTS blacklist"
					+ "(id integer primary key autoincrement,"
					+ "packname varchar(200))");
			db.execSQL("ALTER TABLE elementinfo ADD popupImage varchar(200)");
			db.execSQL("ALTER TABLE elementinfo ADD popupTime varchar(4)");
		} else if(oldVersion == 4) {
			db.execSQL("ALTER TABLE elementinfo ADD popupImage varchar(200)");
			db.execSQL("ALTER TABLE elementinfo ADD popupTime varchar(4)");
		}
	}

}
