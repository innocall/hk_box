package com.great.stb.activity;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.great.stb.R;
import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.UpdateResponse;
import com.great.stb.common.UpdateRequest;
import com.great.stb.dao.ElementDAO;
import com.great.stb.util.*;

public class ActiveActivity extends Activity {

	private Context context;
	private boolean flag = true;
	private EditText et_active_phone_number;

	private static final int BOX_ACTIVE = 0;
	private static final int NUMBER_EMPTY = 1;
	private static final int NUMBER_ERROR = 2;
	private LayoutInflater inflater;
	private View view;
	private TextView textView;
	private Toast toast;
	private SharedPreferences sp;
	private TextView tv_localinfo_400;

	/**
	 * 消息处理器
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

				case BOX_ACTIVE:
					UpdateResponse req_active = (UpdateResponse) msg.obj;
					ElementDAO dao = new ElementDAO(ActiveActivity.this);
					// if
					// (req_active.getReslut()!=null&&req_active.getReslut().equals("0"))
					// {
					if (req_active.getReslut() != null) {
						dao.insertActive(req_active.getDeviceInfo(), req_active.getReslut().getBox_id());
					} else {
						// 网络错误
						textView = (TextView) view.findViewById(R.id.tt_toast);
						textView.setText("用户您好，您的终端状态异常，无法激活，请联系经销商解决。");
						Toast toast = new Toast(ActiveActivity.this);
						toast.setView(view);
						toast.setDuration(Toast.LENGTH_LONG);
						toast.show();
						finish();
						Intent intent = new Intent(getApplicationContext(),	ActiveActivity.class);
						startActivity(intent);
						flag = true;
						return;
					}

					if (req_active.getReslut().getBox_id() != null) {
						Intent intent = new Intent(getApplicationContext(),	SplashActivity.class);
						startActivity(intent);
						finish();
					}

					break;
				case NUMBER_EMPTY:
					textView = (TextView) view.findViewById(R.id.tt_toast);
					textView.setText("认证手机号不能为空");
					Toast toast = new Toast(ActiveActivity.this);
					toast.setView(view);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.show();
					break;
				case NUMBER_ERROR:
					textView = (TextView) view.findViewById(R.id.tt_toast);
					textView.setText("请输入正确的手机号");
					toast = new Toast(ActiveActivity.this);
					toast.setView(view);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.show();
					break;

			}
		};
	};
	private String boxurl;
	private String macid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active);
		inflater = LayoutInflater.from(getApplicationContext());
		view = inflater.inflate(R.layout.toast_style, null);
		tv_localinfo_400 = (TextView)findViewById(R.id.tv_localinfo_400);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		String text = sp.getString("serviceTel", "");
		if(!text.equals("")) {
			tv_localinfo_400.setText("客服电话：" + text);
		}
		initData();

	}

	private void initData() {

		boxurl = getString(R.string.boxurl);
		//macid = getString(R.string.macid);
		//macid = Util.getLocalMacAddress(getApplicationContext());
		macid = getWifiMacAddress();
		Button bt_ok = (Button) findViewById(R.id.bt_ok);
		et_active_phone_number = (EditText) findViewById(R.id.et_active_phone_number);
		TextView tv_localinfo_serial = (TextView) findViewById(R.id.tv_localinfo_serial);
		TextView tv_localinfo_local_mac = (TextView) findViewById(R.id.tv_localinfo_local_mac);
		TextView tv_localinfo_wifi_mac = (TextView) findViewById(R.id.tv_localinfo_wifi_mac);
		et_active_phone_number.setFocusable(true);
		et_active_phone_number.requestFocus();
		et_active_phone_number.setFocusableInTouchMode(true);
		// 序列号
		String box_serial = android.os.Build.SERIAL;
		if (box_serial.equalsIgnoreCase("unknown")) {
			box_serial = "";
		}
		// 有线MAC地址
		String localMacAddress = getLocalMacAddress();
		// 无线MAC地址
		String wifiMacAddress = getWifiMacAddress();

		tv_localinfo_serial.setText("序列号：" + box_serial);
		tv_localinfo_local_mac.setText("有线mac：" + localMacAddress);
		tv_localinfo_wifi_mac.setText("无线mac：" + wifiMacAddress);

		// 激活按钮
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				/*if (flag) {
					activeBox(getApplicationContext());
					flag = false;
				} else {
					return;
				}*/
				activeBox(getApplicationContext());
			}
		});

	}

	// 手机号码的正则判断
	private boolean isLegalPhoneNumber(String mobiles) {

		/*Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");*/
		Pattern p = Pattern
				.compile("^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public void activeBox(Context context_main) {

		context = context_main;

		new Thread() {
			public void run() {

				String phone_number = et_active_phone_number.getText().toString();

				if (TextUtils.isEmpty(phone_number)) {
					Message msg = new Message();
					msg.what = NUMBER_EMPTY;
					handler.sendMessage(msg);
					return;
				}

				if (isLegalPhoneNumber(phone_number)) {
					// 认证
					UpdateRequest ure = new UpdateRequest();
					// 激活
					String url1 = boxurl + "/WebService.asmx/box_active";
					DeviceInfoMode deviceInfo = new DeviceInfoMode();
					deviceInfo.setMac(macid);
					deviceInfo.setSn("12345678901234567890");
					deviceInfo.setPhone_no(phone_number);
					UpdateResponse req = ure.getUpdate(deviceInfo, null, url1);

					Message msg = new Message();
					msg.what = BOX_ACTIVE;
					msg.obj = req;
					handler.sendMessage(msg);
				}else {
					Message msg = new Message();
					msg.what = NUMBER_ERROR;
					handler.sendMessage(msg);
				}

			};

		}.start();

	}

	// 有线获取本机MAC地址方法
	public String getLocalMacAddress() {

		String mac = null;
		try {
			Enumeration localEnumeration = NetworkInterface.getNetworkInterfaces();

			while (localEnumeration.hasMoreElements()) {
				NetworkInterface localNetworkInterface = (NetworkInterface) localEnumeration.nextElement();
				String interfaceName = localNetworkInterface.getDisplayName();

				if (interfaceName == null) {
					continue;
				}

				if (interfaceName.equals("eth0")) {
					// MACAddr = convertMac(localNetworkInterface
					// .getHardwareAddress());
					mac = convertToMac(localNetworkInterface.getHardwareAddress());
					if (mac != null && mac.startsWith("0:")) {
						mac = "0" + mac;
					}
					break;
				}

			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return mac;
	}

	private String convertToMac(byte[] mac) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			byte b = mac[i];
			int value = 0;
			if (b >= 0 && b <= 16) {
				value = b;
				sb.append("0" + Integer.toHexString(value));
			} else if (b > 16) {
				value = b;
				sb.append(Integer.toHexString(value));
			} else {
				value = 256 + b;
				sb.append(Integer.toHexString(value));
			}
			if (i != mac.length - 1) {
				sb.append(":");
			}
		}
		return sb.toString();
	}

	// 无线获取本机MAC地址方法
	public String getWifiMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

}
