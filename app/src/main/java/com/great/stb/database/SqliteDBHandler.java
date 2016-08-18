package com.great.stb.database;

import java.util.ArrayList;

import com.great.stb.bean.ActDataMode;
import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.ElementMode;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqliteDBHandler {

	public static SmartSTBSQLiteOpenHelper helper;

	public static void addElementinfo(ElementMode element) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			String code = element.getCode();
			String version = element.getVersion();
			String image = element.getImage();
			String smallImage = element.getSmallImage();
			String title = element.getTitle();
			String actType = element.getActType();
			String filePath = element.getFilePath();
			String txt_intro = element.getTxt_intro();
			String appName = element.getAppName();
			String argument = element.getArgument();
			String pic_large = element.getPic_large();
			String mediaId = element.getMediaId();
			String popupImage = element.getPopupImage();
			String popupTime = element.getPopupTime();
			ContentValues values = new ContentValues();
			values.put("code", code);
			values.put("version", version);
			values.put("image", image);
			values.put("smallImage", smallImage);
			values.put("title", title);
			values.put("actType", actType);
			values.put("filePath", filePath);
			values.put("pic_large", pic_large);
			values.put("txt_intro", txt_intro);
			values.put("appName", appName);
			values.put("argument", argument);
			values.put("mediaId", mediaId);
			values.put("popupTime",popupTime);
			values.put("popupImage", popupImage);
			db.insert("elementinfo", null, values);
			db.close();
		}

	}

	public static void addActiveInfo(DeviceInfoMode deviceInfo, String box_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			String mac = deviceInfo.getMac();
			String activeState = deviceInfo.getActive_state();
			ContentValues values = new ContentValues();
			values.put("mac", mac);
			values.put("activeState", activeState);
			values.put("box_id", box_id);
			db.insert("active", null, values);
			db.close();
		}

	}

	public static void addActdata(ActDataMode actDate, String code) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			String order = actDate.getOrder();
			String filePath = actDate.getFilePath();

			ContentValues values = new ContentValues();
			values.put("code", code);
			values.put("ordered", order);
			values.put("filePath", filePath);

			db.insert("actdata", null, values);
			db.close();
		}

	}

	public static String getActive() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String box_id = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("Select * from active", null);
			while (cursor.moveToNext()) {
				box_id = cursor.getString(cursor.getColumnIndex("box_id"));
			}
			//关闭cursor
			cursor.close();
			db.close();
		}
		return box_id;

	}

	public static void updateElementMode(ElementMode element) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			String image = element.getImage();
			String smallImage = element.getSmallImage();
			String title = element.getTitle();
			String actType = element.getActType();
			String version = element.getVersion();
			String appName = element.getAppName();
			String txt_intro = element.getTxt_intro();
			String filePath = element.getFilePath();
			String pic_large = element.getPic_large();
			String mediaId = element.getMediaId();
			String argument = element.getArgument();
			String popupImage = element.getPopupImage();
			String popupTime = element.getPopupTime();
			ContentValues values = new ContentValues();
			values.put("version", version);
			values.put("image", image);
			values.put("smallImage", smallImage);
			values.put("title", title);
			values.put("actType", actType);
			values.put("filePath", filePath);
			values.put("pic_large", pic_large);
			values.put("appName", appName);
			values.put("argument", argument);
			values.put("mediaId", mediaId);
			values.put("txt_intro", txt_intro);
			values.put("popupTime",popupTime);
			values.put("popupImage", popupImage);
			db.update("elementinfo", values, "code = ?",
					new String[] { element.getCode() });
			db.close();
		}
	}

	public static void delete(String code){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("actdata", "code = ?", new String[]{code});
		db.close();

	}

	public static void updateActdata(ActDataMode actDate, String code) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			String order = actDate.getOrder();
			String filePath = actDate.getFilePath();

			ContentValues values = new ContentValues();
			values.put("code", code);
			values.put("ordered", order);
			values.put("filePath", filePath);
			db.update("actdata", values, "code = ?", new String[] { code });
			db.close();
		}

	}

	public static ArrayList<ElementMode> getAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<ElementMode> instances = new ArrayList<ElementMode>();
		ElementMode instance = null;
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"Select * from elementinfo where code like 'C%' or code like 'D%' or code like 'E%'",
							null);
			instances = new ArrayList<ElementMode>();
			while (cursor.moveToNext()) {
				instance = new ElementMode();
				instance.setCode(cursor.getString(cursor.getColumnIndex("code")));
				instance.setVersion(cursor.getString(cursor
						.getColumnIndex("version")));
				instance.setImage(cursor.getString(cursor
						.getColumnIndex("image")));
				instance.setSmallImage(cursor.getString(cursor
						.getColumnIndex("smallImage")));
				instance.setTitle(cursor.getString(cursor
						.getColumnIndex("title")));
				instance.setActType(cursor.getString(cursor
						.getColumnIndex("actType")));
				// instance.setAPDU(cursor.getString(cursor.getColumnIndex("APDU")));
				instances.add(instance);
			}
			cursor.close();
			db.close();
		}
		return instances;
	}



	public static ArrayList<ElementMode> getAllCompany() {
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<ElementMode> instances = new ArrayList<ElementMode>();
		ElementMode instance = null;
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"Select * from elementinfo where code like 'F%' or code like 'G%'",
							null);
			instances = new ArrayList<ElementMode>();
			while (cursor.moveToNext()) {
				instance = new ElementMode();
				instance.setCode(cursor.getString(cursor.getColumnIndex("code")));
				instance.setVersion(cursor.getString(cursor
						.getColumnIndex("version")));
				instance.setImage(cursor.getString(cursor
						.getColumnIndex("image")));
				instance.setSmallImage(cursor.getString(cursor
						.getColumnIndex("smallImage")));
				instance.setTitle(cursor.getString(cursor
						.getColumnIndex("title")));
				instance.setActType(cursor.getString(cursor
						.getColumnIndex("actType")));
				// instance.setAPDU(cursor.getString(cursor.getColumnIndex("APDU")));
				instances.add(instance);
			}
			cursor.close();
			db.close();
		}
		return instances;
	}

	public static ArrayList<ElementMode> getByCode(String code) {
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<ElementMode> instances = new ArrayList<ElementMode>();
		ElementMode instance = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"Select * from elementinfo where code=?",
					new String[] { code });
			instances = new ArrayList<ElementMode>();
			while (cursor.moveToNext()) {
				instance = new ElementMode();
				instance.setCode(cursor.getString(cursor.getColumnIndex("code")));
				instance.setVersion(cursor.getString(cursor.getColumnIndex("version")));
				instance.setImage(cursor.getString(cursor.getColumnIndex("image")));
				instance.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
				instance.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				instance.setActType(cursor.getString(cursor.getColumnIndex("actType")));
				instance.setFilePath(cursor.getString(cursor.getColumnIndex("filePath")));
				instance.setTxt_intro(cursor.getString(cursor.getColumnIndex("txt_intro")));
				instance.setPic_large(cursor.getString(cursor.getColumnIndex("pic_large")));
				instance.setAppName(cursor.getString(cursor.getColumnIndex("appName")));
				instance.setArgument(cursor.getString(cursor.getColumnIndex("argument")));
				instance.setMediaId(cursor.getString(cursor.getColumnIndex("mediaId")));
				instance.setPopupImage(cursor.getString(cursor.getColumnIndex("popupImage")));
				instance.setPopupTime(cursor.getString(cursor.getColumnIndex("popupTime")));
				// instance.setAPDU(cursor.getString(cursor.getColumnIndex("APDU")));
				instances.add(instance);
			}
			cursor.close();
			db.close();
		}
		return instances;
	}

	public static ArrayList<ActDataMode> getAllByWhere(String code) {
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<ActDataMode> instances = new ArrayList<ActDataMode>();
		ActDataMode instance = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("Select * from actdata where code = ?",
					new String[] { code });
			instances = new ArrayList<ActDataMode>();
			while (cursor.moveToNext()) {
				instance = new ActDataMode();
				instance.setOrder(cursor.getString(cursor
						.getColumnIndex("ordered")));
				instance.setFilePath(cursor.getString(cursor
						.getColumnIndex("filePath")));
				// instance.setAPDU(cursor.getString(cursor.getColumnIndex("APDU")));
				instances.add(instance);
			}
			cursor.close();
			db.close();
		}
		return instances;
	}

	public static void updateVersionByCode(String version, String code) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if(db.isOpen()) {
			db.execSQL("update elementinfo set version = ? where code = ?",new Object[]{version,code});
		}
		db.close();
	}



}
