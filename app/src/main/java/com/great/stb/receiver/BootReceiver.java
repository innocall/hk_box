package com.great.stb.receiver;



import java.util.List;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.great.stb.MainActivity;
import com.great.stb.R;
import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.LogBean;
import com.great.stb.common.UpdateRequest;
import com.great.stb.service.AppService;
import com.great.stb.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	private static final Logger logger = LoggerFactory.getLogger(BootReceiver.class);
	protected static final int APP_TOP = 2;
	private String boxurl;
	private String macid;
	private SharedPreferences sp;



	@Override
	public void onReceive(Context context, Intent intent) {
		PropertyConfigurator.getConfigurator(context).configure();
		boxurl =context.getString(R.string.boxurl);
//		macid =context.getString(R.string.macid);
		macid =Util.getLocalMacAddress(context);

//		Intent servie = new Intent(context, AppService.class);
//		context.startService(servie);

		//开机
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
			String onStrTime = "0";
			long timeMillis = System.currentTimeMillis();
			onStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
			onStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();
			logger.debug("#"+onStrTime+","+"1"+","+"onoff");
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			Editor ed = sp.edit();
			ed.putBoolean("isParam", true);
			ed.commit();
			//initLogText();

		}


		//关机
		if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")){
			String offStrTime = "0";
			long timeMillis = System.currentTimeMillis();
			offStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
			offStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();
			logger.debug("#"+offStrTime+","+"0"+","+"onoff");
		}
	}

	private void initLogText() {

		new Thread() {
			public void run() {

				String SDCarePath=Environment.getExternalStorageDirectory().toString();
				String filePath=SDCarePath+"/greatstb.txt";
				String url1 = boxurl+"/WebService.asmx/box_upload_log";

				DeviceInfoMode deviceInfo = new DeviceInfoMode();
				deviceInfo.setMac(macid);

				List<LogBean> logs = UpdateRequest.ReadTxtFile(filePath);
				boolean logUpLoadResponse = UpdateRequest.logUpLoadResponse(deviceInfo, logs, url1);

				if(logUpLoadResponse){
					UpdateRequest.removeContext(filePath);
				}

			};

		}.start();

	}
}
