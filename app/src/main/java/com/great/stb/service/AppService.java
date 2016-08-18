package com.great.stb.service;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.great.stb.receiver.BootReceiver;
import com.great.stb.util.Util;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class AppService extends Service {

	private static final Logger logger = LoggerFactory.getLogger(BootReceiver.class);
	protected static final int APP_TOP = 2;
	private String strapp =null;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {


				case APP_TOP:

					String pagename = (String) msg.obj;
					logger.debug("xxxxxxxxx"+pagename);

					break;
			}
		};
	};

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initAppTop(this);
		PropertyConfigurator.getConfigurator(this).configure();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	private void initAppTop(final Context context) {
		new Thread() {
			public void run() {

				do {

					String currentPk = Util.getCurrentPk(context);
					if(strapp.equals(currentPk)){

					}else{
						strapp = currentPk;
						Message msg = new Message();
						msg.what = APP_TOP;
						msg.obj =currentPk;
						handler.sendMessage(msg);
					}

				} while(true);

			};

		}.start();

	}

}
