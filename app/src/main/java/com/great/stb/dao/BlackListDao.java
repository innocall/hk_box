package com.great.stb.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.great.stb.database.SmartSTBSQLiteOpenHelper;

/**
 * 黑名单DAO层
 * @author wu
 */
public class BlackListDao {
	private SmartSTBSQLiteOpenHelper sqlListOpenHelper;

	public BlackListDao(Context context) {
		sqlListOpenHelper = new SmartSTBSQLiteOpenHelper(context);
	}

	public void addBlack(String packName) {
		SQLiteDatabase db = sqlListOpenHelper.getWritableDatabase();
		if(db.isOpen()) {
			db.execSQL("insert into blacklist(packname) values(?)", new Object[]{packName});
			db.close();
		}
	}

	public void addBlackList(List<String> list) {
		SQLiteDatabase db = sqlListOpenHelper.getWritableDatabase();
		if(db.isOpen()) {
			for(String black:list) {
				db.execSQL("insert into blacklist(packname) values(?)", new Object[]{black});
			}
			db.close();
		}
	}

	public boolean findByPackName(String packName) {
		boolean param = false;
		SQLiteDatabase db = sqlListOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor = db.query("blacklist", null, "packname=?",
					new String[] { packName }, null, null, null);
			if(cursor.moveToFirst()) {
				param = true;
			}
			cursor.close();
			//db.close();
		}
		return param;
	}

	public void updateBlackList(List<String> list) {
		SQLiteDatabase db = sqlListOpenHelper.getWritableDatabase();
		if(db.isOpen()) {
			for(String black:list) {
				if(findByPackName(black)) {
					db.execSQL("update blacklist set packname=? where packname = ?", new Object[]{black,black});
				} else {
					addBlack(black);
				}
			}
			db.close();
		}
	}

	public List<String> getAllRemoveAppPackageName() {
		List<String> persons = null;
		SQLiteDatabase db = sqlListOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("blacklist", null, null, null, null, null,
					null);
			persons = new ArrayList<String>();
			while (cursor.moveToNext()) {
				String packName = cursor.getString(cursor.getColumnIndex("packname"));
				persons.add(packName);
			}
			cursor.close();
			db.close();
		}
		return persons;
	}

}
