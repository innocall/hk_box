package com.great.stb.activity;


import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.great.stb.R;
import com.great.stb.util.Util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;

public class LocalInfoActivity extends Activity {
	private static final String TAG = "LocalInfoActivity";
	private View ll_local_info;
	private TextView tv_local_01;
	private TextView tv_local_02;
	private String tmDevice;
	private String tmSerial;
	private String androidId;
	private TextView tv_local_row01_01;
	private TextView tv_local_row01_02;
	private TextView tv_local_row01_03;
	private TextView tv_local_row01_04;
	private TextView tv_local_row02_01;
	private TextView tv_local_row02_02;
	private TextView tv_local_row02_03;
	private TextView tv_local_row02_04;
	//private TextView tv_local_row03_01;
	//private TextView tv_local_row03_02;
	private TextView tv_local_row03_03;
	private TextView tv_local_row03_04;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_local_info);

		initData();
		initInformation();
		initListener();
	}

	private void initInformation() {

		//设备信息
		TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(TELEPHONY_SERVICE);
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		String handSetInfo=
				"手机型号:" + android.os.Build.MODEL +      //SCH-I939
						",SDK版本:" + android.os.Build.VERSION.SDK +   //16
						",系统版本:" + android.os.Build.VERSION.RELEASE+  //4.1.2
						",CODENAME:" + android.os.Build.VERSION.CODENAME+
						",INCREMENTAL:" + android.os.Build.VERSION.INCREMENTAL+
						",BOARD:" + android.os.Build.BOARD+
						",BOOTLOADER:" + android.os.Build.BOOTLOADER+
						",BRAND:" + android.os.Build.BRAND+
						",CPU_ABI:" + android.os.Build.CPU_ABI+
						",CPU_ABI2:" + android.os.Build.CPU_ABI2+
						",DEVICE:" + android.os.Build.DEVICE+
						",DISPLAY:" + android.os.Build.DISPLAY+
						",FINGERPRINT:" + android.os.Build.FINGERPRINT+
						",HARDWARE:" + android.os.Build.HARDWARE+
						",HOST:" + android.os.Build.HOST+
						",ID:" + android.os.Build.ID+
						",MANUFACTURER:" + android.os.Build.MANUFACTURER+
						",MODEL:" + android.os.Build.MODEL+
						",PRODUCT:" + android.os.Build.PRODUCT+
						",RADIO:" + android.os.Build.RADIO+
						",SERIAL:" + android.os.Build.SERIAL+
						",TAGS:" + android.os.Build.TAGS+
						",TIME:" + android.os.Build.TIME+
						",TYPE:" + android.os.Build.TYPE+
						",UNKNOWN:" + android.os.Build.UNKNOWN+
						",USER:" + android.os.Build.USER;


		//序列号
		String box_serial = android.os.Build.SERIAL;
		//有线MAC地址
		String localMacAddress = getLocalMacAddress();
		//无线MAC地址
		String wifiMacAddress = getWifiMacAddress();
		//IP地址
		String localIpAddress = getLocalIpAddress();
		//android版本号
		String android_version = android.os.Build.VERSION.RELEASE;
		//固件版本
		String model = android.os.Build.MODEL;
		//内核版本
		String core_version = android.os.Build.CPU_ABI;
		//版本号
//    String android_version_number = android.os.Build.VERSION.SDK;
		String android_version_number = Util.getVersion(getApplicationContext());

		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("localMacAddress", localMacAddress);
		edit.commit();

		String activeDate = sp.getString("activeDate", "");
		String activeTel = sp.getString("activeTel", "");
		String ServiceName = sp.getString("ServiceName", "");
		String ServiceCycle = sp.getString("ServiceCycle", "");



		tv_local_row01_01.setText(box_serial);
		tv_local_row01_02.setText(localMacAddress);
		tv_local_row01_03.setText(wifiMacAddress);
		tv_local_row01_04.setText(localIpAddress);
		tv_local_row02_01.setText(android_version);
		tv_local_row02_02.setText(model);
		tv_local_row02_03.setText(core_version);
		tv_local_row02_04.setText(android_version_number);
		// tv_local_row03_01.setText(activeDate);
		//  tv_local_row03_02.setText(activeTel);
//    tv_local_row03_03.setText(ServiceName);
//    tv_local_row03_04.setText(ServiceCycle);
		//tmDevice=A0000040338E7C--------  tmSerial=89860312700104469514-------androidId=3bb534e0313b494

		Log.i(TAG, "tmDevice="+tmDevice+"--------  tmSerial="+tmSerial+"-------androidId="+androidId);
		Log.i(TAG, handSetInfo);
	}



	private void initData() {

		//箭头返回部分
		ll_local_info = findViewById(R.id.ll_local_info);
		tv_local_01 = (TextView) findViewById(R.id.tv_local_01);
		tv_local_02 = (TextView) findViewById(R.id.tv_local_02);

		tv_local_row01_01 = (TextView) findViewById(R.id.tv_local_row01_01);
		tv_local_row01_02 = (TextView) findViewById(R.id.tv_local_row01_02);
		tv_local_row01_03 = (TextView) findViewById(R.id.tv_local_row01_03);
		tv_local_row01_04 = (TextView) findViewById(R.id.tv_local_row01_04);

		tv_local_row02_01 = (TextView) findViewById(R.id.tv_local_row02_01);
		tv_local_row02_02 = (TextView) findViewById(R.id.tv_local_row02_02);
		tv_local_row02_03 = (TextView) findViewById(R.id.tv_local_row02_03);
		tv_local_row02_04 = (TextView) findViewById(R.id.tv_local_row02_04);

		//tv_local_row03_01 = (TextView) findViewById(R.id.tv_local_row03_01);
		//tv_local_row03_02 = (TextView) findViewById(R.id.tv_local_row03_02);
//		tv_local_row03_03 = (TextView) findViewById(R.id.tv_local_row03_03);
//		tv_local_row03_04 = (TextView) findViewById(R.id.tv_local_row03_04);

	}

	private void initListener() {

		//箭头返回
		ll_local_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		//箭头获取焦点状态改变
		ll_local_info.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					tv_local_01.setTextColor(Color.parseColor("#ff0000"));
					tv_local_02.setTextColor(Color.parseColor("#ff0000"));
				} else {
					tv_local_01.setTextColor(Color.parseColor("#ffffff"));
					tv_local_02.setTextColor(Color.parseColor("#ffffff"));
				}

			}
		});
	}

	//无线获取本机MAC地址方法
	public String getWifiMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	//有线获取本机MAC地址方法
	public String getLocalMacAddress(){

		String mac=null;
		try {
			Enumeration localEnumeration=NetworkInterface.getNetworkInterfaces();

			while (localEnumeration.hasMoreElements()) {
				NetworkInterface localNetworkInterface=(NetworkInterface) localEnumeration.nextElement();
				String interfaceName=localNetworkInterface.getDisplayName();

				if (interfaceName==null) {
					continue;
				}

				if (interfaceName.equals("eth0")) {
					// MACAddr = convertMac(localNetworkInterface
					// .getHardwareAddress());
					mac=convertToMac(localNetworkInterface.getHardwareAddress());
					if (mac!=null&&mac.startsWith("0:")) {
						mac="0"+mac;
					}
					break;
				}

			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return mac;
	}

	private  String convertToMac(byte[] mac) {
		StringBuilder sb=new StringBuilder();
		for (int i=0; i<mac.length; i++) {
			byte b=mac[i];
			int value=0;
			if (b>=0&&b<=16) {
				value=b;
				sb.append("0"+Integer.toHexString(value));
			} else if (b>16) {
				value=b;
				sb.append(Integer.toHexString(value));
			} else {
				value=256+b;
				sb.append(Integer.toHexString(value));
			}
			if (i!=mac.length-1) {
				sb.append(":");
			}
		}
		return sb.toString();
	}

	protected InetAddress getLocalInetAddress() {
		InetAddress ip = null;
		try {
			Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
			while (en_netInterface.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();
				Enumeration<InetAddress> en_ip = ni.getInetAddresses();
				while (en_ip.hasMoreElements()) {
					ip = en_ip.nextElement();
					if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
						break;
					else
						ip = null;
				}

				if (ip != null) {
					break;
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;
	}



	//获取本机IP方法
	private String getLocalIpAddress(){
		WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		if(ipAddress==0)return null;
		return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."
				+(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
	}


}
