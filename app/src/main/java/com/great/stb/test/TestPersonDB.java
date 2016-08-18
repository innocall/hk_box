package com.great.stb.test;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.great.stb.bean.ActDataMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.dao.ElementDAO;
import com.great.stb.database.SmartSTBSQLiteOpenHelper;
import com.great.stb.util.FileUtil;


public class TestPersonDB extends AndroidTestCase {

	private static final String TAG = "TestPersonDB";

	public void testCreateDB() throws Exception {

		SmartSTBSQLiteOpenHelper helper = new SmartSTBSQLiteOpenHelper(getContext());
		helper.getWritableDatabase();
	}

	public void AddTest() throws Exception {

		ElementDAO dao = new ElementDAO(getContext());

		ElementMode elementMode = new ElementMode();
		elementMode.setCode("F30");
		elementMode.setVersion("version");
		elementMode.setImage("imag");
		elementMode.setSmallImage("smallimage");
		elementMode.setTitle("title");
		elementMode.setActType("actType");

		List<ActDataMode> list = new ArrayList<ActDataMode>();

		ActDataMode actDataMode01 = new ActDataMode();
		actDataMode01.setFilePath("filePath01");
		actDataMode01.setOrder("order01");

		ActDataMode actDataMode02 = new ActDataMode();
		actDataMode02.setFilePath("filePath02");
		actDataMode02.setOrder("order02");

		ActDataMode actDataMode03 = new ActDataMode();
		actDataMode03.setFilePath("filePath03");
		actDataMode03.setOrder("order03");

		list.add(actDataMode01);
		list.add(actDataMode02);
		list.add(actDataMode03);


		elementMode.setActData(list);


		dao.insert(elementMode);

	}

	public void updateElementTest()throws Exception{

		ElementDAO dao = new ElementDAO(getContext());

		ElementMode elementMode = new ElementMode();
		elementMode.setCode("code");
		elementMode.setVersion("version_updata");
		elementMode.setImage("imag_updata");
		elementMode.setSmallImage("smallimage_updata");
		elementMode.setTitle("title_updata");
		elementMode.setActType("actType_updata");

		List<ActDataMode> list = new ArrayList<ActDataMode>();

		ActDataMode actDataMode01 = new ActDataMode();
		actDataMode01.setFilePath("filePath01_updata");
		actDataMode01.setOrder("order01_updata");

		ActDataMode actDataMode02 = new ActDataMode();
		actDataMode02.setFilePath("filePath02_updata");
		actDataMode02.setOrder("order02_updata");

		ActDataMode actDataMode03 = new ActDataMode();
		actDataMode03.setFilePath("filePath03_updata");
		actDataMode03.setOrder("order03_updata");

		list.add(actDataMode01);
		list.add(actDataMode02);
		list.add(actDataMode03);


		elementMode.setActData(list);


		dao.update(elementMode);
	}

	public void getDeskElementTest(){

		ElementDAO dao = new ElementDAO(getContext());


		ElementMode mode = dao.getDeskElementMode();
		Log.i(TAG, mode.toString());

	}

	public void getCompanyElementTest(){

		ElementDAO dao = new ElementDAO(getContext());


		ElementMode mode = dao.getCompanyElementMode();

		Log.i(TAG, mode.toString());
	}

	public void getDeskContentElementTest(){

		ElementDAO dao = new ElementDAO(getContext());


		List<ElementMode> list = dao.getDeskContentElementMode();

		Log.i(TAG, list.toString());
	}

	public void getComanyContentElementTest(){

		ElementDAO dao = new ElementDAO(getContext());


		List<ElementMode> list = dao.getComanyContentElementMode();

		Log.i(TAG, list.toString());

	}


}
