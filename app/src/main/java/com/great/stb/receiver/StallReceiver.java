package com.great.stb.receiver;




import java.io.File;
import java.io.IOException;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.great.stb.dao.ElementDAO;
import com.great.stb.service.AppService;
import com.great.stb.util.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.format.DateFormat;

public class StallReceiver extends BroadcastReceiver {

	private static final Logger logger = LoggerFactory.getLogger(StallReceiver.class);
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		PropertyConfigurator.getConfigurator(context).configure();

		//安装
		//接收广播：设备上新安装了一个应用程序包后自动启动新安装应用程序。
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			//String packName = intent.getDataString();
//    		Intent servie = new Intent(context, AppService.class);
//    		context.startService(servie);

			long timeMillis = System.currentTimeMillis();
			String installStrTime = "0";
			installStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
			installStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();


			String packageNameInstall = intent.getDataString().substring(8);

			PackageManager packageManager = null;
			ApplicationInfo applicationInfo = null;
			try {
				packageManager = context.getPackageManager();
				applicationInfo = packageManager.getApplicationInfo(packageNameInstall, 0);
			} catch (NameNotFoundException e) {
				applicationInfo = null;
			}

			String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);


			logger.debug("#"+installStrTime+","+"10"+","+"app"+","+applicationName);

		}

		//删除
		//接收广播：设备上删除了一个应用程序包。
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			long timeMillis = System.currentTimeMillis();
			String unstallStrTime = "0";
			unstallStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
			unstallStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();
			String packageNameUnStall = intent.getDataString().substring(8);

			PackageManager packageManager = null;
			ApplicationInfo applicationInfo = null;
			String applicationName = null;
			try {
				packageManager = context.getPackageManager();
				applicationInfo = packageManager.getApplicationInfo(packageNameUnStall, 0);
			} catch (NameNotFoundException e) {
				applicationInfo = null;
			}
			if(applicationInfo==null){
				applicationName =intent.getDataString().substring(8);
			}else{
				applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
			}

			logger.debug("#"+unstallStrTime+","+"40"+","+"app"+","+applicationName);
		}



	}




}
