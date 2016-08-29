package com.great.stb.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import com.great.stb.MainActivity;
import com.great.stb.R;
import com.great.stb.bean.AddAppModel;
import com.great.stb.bean.AppChangeModel;
import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.DeviceStateMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.bean.UpdateResponse;
import com.great.stb.bean.WorkOrdersStateResponse;
import com.great.stb.common.UpdateRequest;
import com.great.stb.dao.BlackListDao;
import com.great.stb.dao.ElementDAO;
import com.great.stb.util.FileUtil;
import com.great.stb.util.SystemAppUtils;
import com.great.stb.util.Util;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class SplashActivity extends Activity {

	/**
	 * 服务器更新信息
	 */
	private static final int LOAD_MAIN_UI = 0;
	private static final int LOAD_ACTIVE_UI = 1;
	private static final int BOX_DESKTOP_APP = 2;
	private static final int SHOW_UPDATE_INFO = 3;
	private static final int XML_PARSE_ERROR = 4;
	private static final int BOX_DESKTOP_CONTENT = 5;
	protected static final String TAG = "SplashActivity";
	private static final int BOX_NO_WIFI = 6;
	protected static final int BOX_GET_STATE = 7;
	private static final int WIFI_MSG = 8;
	private static final int INTERFACE_ERROR = 9;
	private static final int LOAD_ERROR_TIP = 10;
	private static final int WITHISOK = 11;
	private static final int DOWNCHANGEAPP = 12;
	private static final int STARTAPP = 13;

	private UpdateResponse req;

	private List<String> version2D = new ArrayList<String>();
	private List<String> version2E = new ArrayList<String>();
	private SharedPreferences sp;
	private String active_result;
	private String serviceTel; // 客服号码
	/* 定义广播接受者 */
	private InstallBroadCast installBroadCase = new InstallBroadCast();
	private boolean param = false;// 记录是否为第一次开机
	private LayoutInflater inflater;
	private RelativeLayout rl_splash;
	private TextView tv_sp_loadmsg;
	private boolean isLicense = false;
	// private InstallBroadCast mInstallBroadCast = new InstallBroadCast();

	/**
	 * 消息处理器
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case LOAD_ACTIVE_UI:
					loadActiveUI();
					break;
				case LOAD_MAIN_UI:
					if (isLicense) {
						// 显示国广界面
						tv_sp_loadmsg.setText("数据初始化成功...");
						Animation in = AnimationUtils.loadAnimation(
								SplashActivity.this, R.anim.left_in);
						Animation out = AnimationUtils.loadAnimation(
								SplashActivity.this, R.anim.left_out);
						viewFlipper.setInAnimation(in);
						viewFlipper.setOutAnimation(out);
						viewFlipper.showNext();
					}
					appChange();
					break;
				case BOX_DESKTOP_APP:
					break;
				case SHOW_UPDATE_INFO:
					tv_sp_loadmsg.setText("正在升级...");
					downLoadApk();
					break;
				case XML_PARSE_ERROR:
					Toast.makeText(getApplicationContext(), "解析更新信息失败", 0).show();
					break;
				case BOX_DESKTOP_CONTENT:
					// 调用状态接口
					tv_sp_loadmsg.setText("桌面内容初始化成功...");
					getState();
					// loadMainUI();
					break;
				case BOX_NO_WIFI:
					loadNOWIFI();
					break;
				case INTERFACE_ERROR:
					// 接口数据错误
					tv_sp_loadmsg.setText("数据获取错误...");
					loadInterfaceMsg();
					break;
				case WIFI_MSG:
					Toast.makeText(getApplicationContext(), "网络异常", 3000).show();
					finish();
					break;
				case BOX_GET_STATE:
					WorkOrdersStateResponse wosr = (WorkOrdersStateResponse) msg.obj;
					String result = wosr.getDeviceState().getState();
					String reason = "成功";
					if ("10".equals(result)) {
						// 关停--黑屏不能动
						Intent intent = new Intent(getApplicationContext(),
								OFFBOXActivity.class);
						startActivity(intent);
						commitState(result, "关停");
						finish();
					} else if ("20".equals(result)) {
						// 强制本机--应用，首页不能用
						commitState(result, "强制本机");
						loadNOWIFI();
					} else if ("30".equals(result)) {
						// 正常
						commitState(result, reason);
						loadMainUI();
					}
					break;
				case LOAD_ERROR_TIP:
					String res = (String) msg.obj;
				/*
				 * Intent intent = new
				 * Intent(getApplicationContext(),LoadErrorDigActicity.class);
				 * intent.putExtra("result", res); startActivity(intent);
				 */
					if (isLicense) {
						// 显示国广界面
						tv_sp_loadmsg.setText("数据初始化失败...");
						Animation in = AnimationUtils.loadAnimation(
								SplashActivity.this, R.anim.left_in);
						Animation out = AnimationUtils.loadAnimation(
								SplashActivity.this, R.anim.left_out);
						viewFlipper.setInAnimation(in);
						viewFlipper.setOutAnimation(out);
						viewFlipper.showNext();
					}
					onlinErrorDialog(res);
					break;
				case WITHISOK:
					withIsOk();
					break;
				case DOWNCHANGEAPP:
					Bundle b = msg.getData();
					final String urlPath = b.getString("urlPath");
					final String name = b.getString("name");
					//String des = b.getString("des");
					SystemAppUtils.downLoadApk(getApplicationContext(),urlPath, "/sdcard/" + name,
							SplashActivity.this);
					desktopContentBox();
					break;
				case STARTAPP:
					desktopContentBox();
					break;
			}
		}
	};
	private String boxurl;
	private String macid;
	private String mac_wifi;
	private String mac_eth;
	private String cpu_id;
	private ViewFlipper viewFlipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
		// Toast.makeText(SplashActivity.this, getAppPackageName(), 3).show();
		// 图片切换动画
		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
		// 监听开机状态
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
		getApplicationContext()
				.registerReceiver(installBroadCase, intentFilter);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		// boolean detect = Util.detect(getApplicationContext());
		TextView tv = (TextView) findViewById(R.id.tv_sp_version);
		tv_sp_loadmsg = (TextView) findViewById(R.id.tv_sp_loadmsg);
		tv.setText("柠檬OSS:" + getViersion());
		inflater = LayoutInflater.from(getApplicationContext());
		// 获取wifi mac
		macid = getWifiMacAddress();
		mac_wifi = getWifiMacAddress();
		mac_eth = Util.getLocalMacAddress(getApplicationContext());
		File path = Environment.getExternalStorageDirectory();
		File file = new File("/mnt/sdcard/wifi.txt");
		if (!file.exists()) {
			if (macid != null && !"".equals(macid)) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					FileUtil.saveFile(macid, path, "wifi.txt");
				}
			} 
			/*else {
				macid = FileUtil.readFile("/mnt/sdcard/wifi.txt");
				mac_wifi = macid;
			}*/
		} else {
			if (macid != null && !"".equals(macid)) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					FileUtil.saveFile(macid, path, "wifi.txt");
				}
			} else {
				macid = FileUtil.readFile("/mnt/sdcard/wifi.txt");
				mac_wifi = macid;
			}
		}
		boolean detect = Util.detect(getApplicationContext());
		param = sp.getBoolean("isParam", false);
		if (param) {
			Editor ed = sp.edit();
			ed.putBoolean("isParam", false);
			ed.commit();
		}
		if (detect) {
			withIsOk();
		} else {
			// if(param) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					boolean detect1 = false;
					int n = 15;
					for (int i = 0; i < n; i++) {
						try {
							Thread.sleep(2000);
							detect1 = Util.detect(getApplicationContext());
							// Log.i(TAG, i + "");
							if (detect1) {
								n = i;
								break;
							}
						} catch (InterruptedException e) {
							Toast.makeText(getApplicationContext(), "网络连接错误",
									Toast.LENGTH_LONG).show();
						}
					}
					Message msg = new Message();
					if (detect1) {
						msg.what = WITHISOK;
					} else {
						msg.what = BOX_NO_WIFI;
					}
					handler.sendMessage(msg);
				}
			}).start();
			/*
			 * } else { loadNOWIFI(); }
			 */
		}
	}

	private void withIsOk() {
		tv_sp_loadmsg.setText("正在初始化数据...");
		boxurl = getString(R.string.boxurl);
		// Toast.makeText(this, macid, Toast.LENGTH_SHORT).show();
		cpu_id = getCPUSerial();
		new Thread(new CheckVersionTask()).start();
		// 主界面动画效果，由透明变亮
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(5000);
		rl_splash.startAnimation(aa);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

	/**
	 * 检查更新的子线程
	 *
	 * @author Administrator
	 *
	 */
	private class CheckVersionTask implements Runnable {
		@Override
		public void run() {
			Message msg = Message.obtain();
			long startTime = System.currentTimeMillis();
			try {
				// 1、判断是否需要升级
				boolean upload = isVersionUp();
				if (upload) {
					msg.what = SHOW_UPDATE_INFO; // 控制app升级w
				} else {
					// isQueryActive(msg); //2、激活
					isOnlineVerify(msg); // 2、自动激活、拍照认证接口
				}
			} catch (Exception e) {
			} finally {
				long endTime = System.currentTimeMillis();
				long dTime = endTime - startTime;
				if (dTime < 4000) {
					try {
						Thread.sleep(4000 - dTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendMessage(msg);
			}

		}

		/**
		 * 自动激活，牌照认证
		 */
		public void isOnlineVerify(Message msg) {
			String url_active = boxurl + "/WebService.asmx/box_online_verify"; // 上线认证接口
			UpdateRequest ure = new UpdateRequest();
			DeviceInfoMode deviceInfo_active = new DeviceInfoMode();
			deviceInfo_active.setMac(macid);
			deviceInfo_active.setSn("12345678901234567890");
			deviceInfo_active.setPhone_no("13912345678");
			UpdateResponse req = ure.getUpdate(deviceInfo_active, null,
					url_active);
			if (req != null) {
				active_result = req.getReslut().getActive_result();
				serviceTel = req.getReslut().getServiceTel();
				String license_id = req.getReslut().getLicense_id();
				Editor ed = sp.edit();
				ed.putString("serviceTel", serviceTel);
				ed.putString("userNameText", req.getReslut().getUser_id());
				ed.putString("license_id", license_id);
				ed.commit();
				//license_id = "1";
				// active_result = "4";
				//active_result = "1";
				if ("1".equals(license_id) || "9".equals(license_id)) {
					// 1：华创国广 ； 9：柠檬国广 显示国广启动界面
					isLicense = true;
				}
				if (active_result.equals("1") || active_result.equals("0")) {
					msg.what = LOAD_MAIN_UI; // 0: 正常 1: 已开通未激活(自动激活)
				} else {
					msg.what = LOAD_ERROR_TIP;
					msg.obj = active_result;
				}
			} else {
				msg.what = WIFI_MSG;
				handler.sendMessage(msg);
			}

		}

		/**
		 * 判断是否激活
		 *
		 * @param msg
		 */
		/*
		 * @Deprecated private void isQueryActive(Message msg) { String
		 * url_active = boxurl + "/WebService.asmx/box_query_active";
		 * UpdateRequest ure_active = new UpdateRequest(); DeviceInfoMode
		 * deviceInfo_active = new DeviceInfoMode();
		 * deviceInfo_active.setMac(macid);
		 * deviceInfo_active.setMacWifi(mac_wifi);
		 * deviceInfo_active.setMacEth(mac_eth);
		 * deviceInfo_active.setCPUID(cpu_id); UpdateResponse req_active =
		 * ure_active.getUpdate(deviceInfo_active, null, url_active); if
		 * (req_active.getReslut() != null) { active_result =
		 * req_active.getReslut().getActive_result(); serviceTel =
		 * req_active.getReslut().getServiceTel(); Editor ed = sp.edit();
		 * ed.putString("serviceTel", serviceTel); ed.commit(); if
		 * (active_result.equals("1")) { msg.what = LOAD_MAIN_UI; } else {
		 * msg.what = LOAD_ACTIVE_UI; //未激活进入激活页面 } } else { msg.what =
		 * WIFI_MSG; handler.sendMessage(msg); } }
		 */
	}

	protected void loadActiveUI() {
		Intent intent = new Intent(SplashActivity.this, ActiveActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 是否需要升级
	 *
	 * @return true需要，false版本相同不需要
	 */
	public boolean isVersionUp() {
		String version = getViersion();
		String url1 = boxurl + "/WebService.asmx/box_desktop_app";// app升级接口
		UpdateRequest ure = new UpdateRequest();
		DeviceInfoMode deviceInfo = new DeviceInfoMode();
		deviceInfo.setMac(macid);
		List<ElementMode> list = new ArrayList<ElementMode>();
		ElementMode element1 = new ElementMode();
		element1.setCode("B10");
		element1.setVersion(version);
		list.add(element1);
		req = ure.getUpdate(deviceInfo, list, url1);
		if (req != null && req.getVersionInfo() != null) {
			return true;
		}
		return false;
	}

	private void loadMainUI() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		intent.putExtra("WIFI", true);
		if (isLicense) {
			intent.putExtra("isLicense", true);
		} else {
			intent.putExtra("isLicense", false);
		}
		startActivity(intent);
		finish();
	}

	private void loadNOWIFI() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		intent.putExtra("WIFI", false);
		if (isLicense) {
			intent.putExtra("isLicense", true);
		} else {
			intent.putExtra("isLicense", false);
		}
		startActivity(intent);
		finish();
	}

	private void loadInterfaceMsg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SplashActivity.this);
		builder.setTitle(getString(R.string.title));
		View view = inflater.inflate(R.layout.activity_dialog, null);
		String userNameText = sp.getString("userNameText", "");
		TextView textView = (TextView) view.findViewById(R.id.msg_tip);
		if (serviceTel != null && !"".equals(serviceTel)) {
			textView.setText("2、如果网络连接正常，您还可以拨打客服电话:" + serviceTel
					+ "通过告知盒子编号:" + userNameText + "寻求解决。");
		}
		builder.setCancelable(false);
		builder.setView(view);
		builder.setPositiveButton("检查网络", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				startActivity(intent);
			}
		});
		builder.create().show();
	}

	/**
	 * 显示自动升级提醒对话框
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setTitle("升级提醒:");
		// builder.setMessage(updateInfo.getDescription());
		builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				downLoadApk();

			}
		});

		builder.setNegativeButton("下次", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ElementDAO dao = new ElementDAO(getApplicationContext());
				String box_id = dao.getActive();
				if (active_result.equals("1")) {
					loadMainUI();
				} else {
					// 是第一次登陆，进入登陆界面
					loadActiveUI();
				}
			}
		});

		builder.show();
	}

	/**
	 * 下载apk文件
	 */
	protected void downLoadApk() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setTitle("正在下载");
		pd.setCancelable(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		FinalHttp fh = new FinalHttp();
		// 调用download方法开始下载
		// fh.download(updateInfo.getApkurl(), "/sdcard/temp.apk",
		fh.download(req.getVersionInfo().getElement().get(0).getFilePath(),
				"/sdcard/hk_box.apk", new AjaxCallBack<File>() {
					// boolean isInstall = true;
					@Override
					public void onSuccess(File t) {
						pd.dismiss();
						/*
						 * //if("com.great.stb".equals(getAppPackageName())) {
						 * File soure = new File("/sdcard/hk_box.apk"); File
						 * target = new File("/system/app/hk_box.apk"); //File
						 * target = new File("/sdcard/box/hk_box.apk"); try {
						 * if(soure.exists() && target.exists()) {
						 * Runtime.getRuntime
						 * ().exec("chmod 777 /system/app/hk_box.apk");
						 * FileUtil.copyFile(soure, target); isInstall = false;
						 * } } catch (IOException e) { } //} if(isInstall) {
						 */
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setDataAndType(Uri.fromFile(t),
								"application/vnd.android.package-archive");
						startActivity(intent);
						finish();
						// }
						super.onSuccess(t);
					}

					@Override
					public void onLoading(long count, long current) {
						pd.setMax((int) count);
						pd.setProgress((int) (current));
						super.onLoading(count, current);
					}

				});

	}

	public void appChange() {
		new Thread(){
			public void run(){
				// 获取黑名单
				boolean param = true;
				String url = boxurl + "/WebService.asmx/box_app_change";
				UpdateRequest ur = new UpdateRequest();
				DeviceInfoMode deviceInfo3 = new DeviceInfoMode();
				deviceInfo3.setMac(macid);
				AppChangeModel appChange = ur.getUpdate(deviceInfo3, url);
				// 1.黑名单数据插入数据库
				BlackListDao blackDao = new BlackListDao(
						getApplicationContext());
				List<String> blackList = appChange.getRemoveAppPackageName();
				if (blackList.size() > 0) {
					blackDao.updateBlackList(blackList);
				}
				// 2.判断是否有app推送安装
				List<AddAppModel> listAddApp = appChange.getAddAppList();
				for (AddAppModel addApp : listAddApp) {
					// 判断该app是否已经安装
					if (!SystemAppUtils.checkPackage(getApplicationContext(),
							addApp.getPackName())) {
						// 没安装则下载安装
						String urlPath = addApp.getUrl();
						String name = urlPath.substring(urlPath.lastIndexOf("/") + 1, urlPath.length());
						Message msg = new Message();
						msg.what = DOWNCHANGEAPP;
						Bundle data = new Bundle();
						data.putString("urlPath", urlPath);
						data.putString("name", name);
						data.putString("des", addApp.getDes());
						msg.setData(data);
						param = false;
						handler.sendMessage(msg);
					}
				}
				if(param) {
					Message msg = new Message();
					msg.what = STARTAPP;
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	/**
	 * 桌面内容更新
	 */
	public void desktopContentBox() {
		// tv_sp_loadmsg.setText("正在初始化桌面内容...");
		new Thread() {
			public void run() {
				ElementDAO dao = new ElementDAO(getApplicationContext());
				List<ElementMode> elementsDB = dao.getDeskContentElementMode();
				// 更新桌面内容
				if (elementsDB.size() == 0) {
					for (int i = 0; i < 8; i++) {
						version2D.add("0");
					}
					for (int i = 0; i < 5; i++) {
						version2E.add("0");
					}
				} else {
					for (int i = 0; i < elementsDB.size(); i++) {
						if (elementsDB.get(i).getCode().contains("E")) {
							version2E.add(elementsDB.get(i).getVersion());
						}
						if (elementsDB.get(i).getCode().contains("D")) {
							version2D.add(elementsDB.get(i).getVersion());
						}
					}
				}

				// 桌面内容更新
				String url2 = boxurl + "/WebService.asmx/box_desktop_content";
				UpdateRequest ure = new UpdateRequest();
				DeviceInfoMode deviceInfo2 = new DeviceInfoMode();
				deviceInfo2.setMac(macid);
				List<ElementMode> list2 = new ArrayList<ElementMode>();
				ElementMode element12 = new ElementMode();
				element12.setCode("C10");
				element12.setVersion("0");
				list2.add(element12);

				ElementMode element121 = new ElementMode();
				element121.setCode("C40");
				element121.setVersion("91");
				list2.add(element121);

				if (elementsDB.size() == 0) {
					for (int i = 0; i < 8; i++) {
						ElementMode element13 = new ElementMode();
						element13.setCode("D" + (i + 1) + "0");
						element13.setVersion(version2D.get(i));
						list2.add(element13);
					}
					for (int j = 0; j < 5; j++) {
						ElementMode element18 = new ElementMode();
						element18.setCode("E" + (j + 1) + "0");
						element18.setVersion(version2E.get(j));
						list2.add(element18);
					}
				} else {
					for (int i = 0; i < version2D.size(); i++) {
						ElementMode element13 = new ElementMode();
						element13.setCode("D" + (i + 1) + "0");
						element13.setVersion(version2D.get(i));
						list2.add(element13);
					}
					for (int j = 0; j < version2E.size(); j++) {
						ElementMode element18 = new ElementMode();
						element18.setCode("E" + (j + 1) + "0");
						element18.setVersion(version2E.get(j));
						list2.add(element18);
					}
				}

				UpdateResponse req = ure.getUpdate(deviceInfo2, list2, url2);
				if (req == null || req.getDeviceInfo() == null
						|| req.getVersionInfo() == null) {
					// 接口返回数据错误
					Message msg = new Message();
					msg.what = INTERFACE_ERROR;
					msg.obj = req;
					handler.sendMessage(msg);
				} else {
					// 保存推送数据
					DeviceInfoMode deviceInfo = req.getDeviceInfo();
					String desktopText = deviceInfo.getDesktopText();
					String positionText = deviceInfo.getPositionText();
					String weatherText = deviceInfo.getWeatherText();
					// String userNameText = deviceInfo.getUserName();

					String telphone = deviceInfo.getTelphone();
					String regDate = deviceInfo.getRegDate();
					String activeDate = deviceInfo.getActiveDate();
					String activeTel = deviceInfo.getActiveTel();
					String ServiceName = deviceInfo.getServiceName();
					String ServiceCycle = deviceInfo.getServiceCycle();

					Editor edit = sp.edit();
					edit.putString("desktopText", desktopText);
					edit.putString("positionText", positionText);
					edit.putString("weatherText", weatherText);
					// edit.putString("userNameText", userNameText);
					edit.putString("telphone", telphone);
					edit.putString("regDate", regDate);
					edit.putString("activeDate", activeDate);
					edit.putString("activeTel", activeTel);
					edit.putString("ServiceName", ServiceName);
					edit.putString("ServiceCycle", ServiceCycle);
					edit.commit();

					List<ElementMode> elements = req.getVersionInfo().getElement();

					for (int i = 0; i < elements.size(); i++) {
						if (elementsDB.size() == 0) {
							// 存图片到SD卡
							if (elements.get(i).getCode().contains("E")) {
								// 图片
								Bitmap bitmap = Util.getImageFromURL(elements.get(i).getImage());
								//贴片
								Bitmap bitmapPopup = Util.getImageFromURL(elements.get(i).getPopupImage());
								if(bitmapPopup != null) {
									Util.saveImage2SD(bitmapPopup, "popupImage" + elements.get(i).getCode());
								}
								if (bitmap != null) {
									Util.saveImage2SD(bitmap, "menuBigImage" + elements.get(i).getCode());
								}
							} else if (elements.get(i).getCode().contains("D")) {
								Bitmap bitmap = Util.getImageFromURL(elements
										.get(i).getImage());
								Bitmap smallBitmap = Util
										.getImageFromURL(elements.get(i)
												.getSmallImage());
								if (bitmap != null) {
									Util.saveImage2SD(bitmap, "adverBigImage"
											+ elements.get(i).getCode());
								}
								if (smallBitmap != null) {
									Util.saveImage2SD(smallBitmap,
											"menuSmallImage"
													+ elements.get(i).getCode());
								}
								//贴片
								Bitmap bitmapPopup = Util.getImageFromURL(elements.get(i).getPopupImage());
								if(bitmapPopup != null) {
									Util.saveImage2SD(bitmapPopup, "popupImage" + elements.get(i).getCode());
								}
							} else if (elements.get(i).getCode().contains("C")) {
								Bitmap bitmapLogo1 = Util
										.getImageFromURL(elements.get(i)
												.getLogo1());
								Bitmap bitmapLogo2 = Util
										.getImageFromURL(elements.get(i)
												.getLogo2());
								Bitmap bitmapLogo3 = Util
										.getImageFromURL(elements.get(i)
												.getLogo3());
								if (bitmapLogo1 != null) {
									Util.saveImage2SD(bitmapLogo1, "Logo1");
								}
								if (bitmapLogo2 != null) {
									Util.saveImage2SD(bitmapLogo2, "Logo2");
								}
								if (bitmapLogo3 != null) {
									Util.saveImage2SD(bitmapLogo3, "Logo3");
								}
							}
							dao.insert(elements.get(i));
						} else {
							if (elements.get(i) != null) {
								if (elements.get(i).getCode().contains("E")) {
									// 图片
									Bitmap bitmap = Util.getImageFromURL(elements.get(i).getImage());
									if (bitmap != null) {
										Util.saveImage2SD(bitmap,"menuBigImage" + elements.get(i).getCode());
									}
									//贴片
									Bitmap bitmapPopup = Util.getImageFromURL(elements.get(i).getPopupImage());
									if(bitmapPopup != null) {
										Util.saveImage2SD(bitmapPopup, "popupImage" + elements.get(i).getCode());
									}
								} else if (elements.get(i).getCode()
										.contains("D")) {
									Bitmap bitmap = Util
											.getImageFromURL(elements.get(i)
													.getImage());
									Bitmap smallBitmap = Util
											.getImageFromURL(elements.get(i)
													.getSmallImage());
									if (bitmap != null) {
										Util.saveImage2SD(
												bitmap,
												"adverBigImage"
														+ elements.get(i)
														.getCode());
									}
									if (smallBitmap != null) {
										Util.saveImage2SD(smallBitmap,
												"menuSmallImage"
														+ elements.get(i)
														.getCode());
									}
									//贴片
									Bitmap bitmapPopup = Util.getImageFromURL(elements.get(i).getPopupImage());
									if(bitmapPopup != null) {
										Util.saveImage2SD(bitmapPopup, "popupImage" + elements.get(i).getCode());
									}
								} else if (elements.get(i).getCode()
										.contains("C")) {
									Bitmap bitmapLogo1 = Util
											.getImageFromURL(elements.get(i)
													.getLogo1());
									Bitmap bitmapLogo2 = Util
											.getImageFromURL(elements.get(i)
													.getLogo2());
									Bitmap bitmapLogo3 = Util
											.getImageFromURL(elements.get(i)
													.getLogo3());
									if (bitmapLogo1 != null) {
										Util.saveImage2SD(bitmapLogo1, "Logo1");
									}
									if (bitmapLogo2 != null) {
										Util.saveImage2SD(bitmapLogo2, "Logo2");
									}
									if (bitmapLogo3 != null) {
										Util.saveImage2SD(bitmapLogo3, "Logo3");
									}
								}
								dao.update(elements.get(i));
							}
						}
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
					Message msg = new Message();
					msg.what = BOX_DESKTOP_CONTENT;
					msg.obj = req;
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	public void getState() {
		new Thread() {
			public void run() {
				// 获取状态
				UpdateRequest ure = new UpdateRequest();
				// String status = "30";
				// 激活
				String url1 = boxurl + "/WebService.asmx/box_get_state";
				DeviceInfoMode deviceInfo = new DeviceInfoMode();
				deviceInfo.setMac(macid);
				DeviceStateMode deviceState = new DeviceStateMode();
				deviceState.setState("30");
				WorkOrdersStateResponse req = ure.getWorkOrderState(deviceInfo,
						deviceState, null, url1);
				Message msg = new Message();
				msg.what = BOX_GET_STATE;
				msg.obj = req;
				handler.sendMessage(msg);
			};

		}.start();

	}

	private void commitState(final String state, final String reason) {
		new Thread() {
			public void run() {
				// 获取状态
				UpdateRequest ure = new UpdateRequest();
				// 提交状态
				String url1 = boxurl + "/WebService.asmx/box_update_state";
				DeviceInfoMode deviceInfo = new DeviceInfoMode();
				deviceInfo.setMac(macid);
				DeviceStateMode deviceState = new DeviceStateMode();
				deviceState.setState(state);
				deviceState.setReason(reason);
				WorkOrdersStateResponse req = ure.getWorkOrderState(deviceInfo,
						deviceState, null, url1);
			};
		}.start();

	}

	public static String getCPUSerial() {
		String str = "", strCPU = "", cpuAddress = "0000000000000000";
		try {
			// 读取CPU信息
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			// 查找CPU序列号
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					// 查找到序列号所在行
					if (str.indexOf("Serial") > -1) {
						// 提取序列号
						strCPU = str.substring(str.indexOf(":") + 1,
								str.length());
						// 去空格
						cpuAddress = strCPU.trim();
						break;
					}
				} else {
					// 文件结尾
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}

		return cpuAddress;
	}

	// 启动屏蔽返回键
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 无线获取本机MAC地址方法
	public String getWifiMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 打开Wifi网卡
	 */
	/*
	 * public void openNetCard() { WifiManager wifi = (WifiManager)
	 * getSystemService(WIFI_SERVICE); if (!wifi.isWifiEnabled()) {
	 * wifi.setWifiEnabled(true); } }
	 */

	/*
	 * public String getWifiMacAddress() { String macAddress = null, ip = null;
	 * WifiManager wifiMgr = (WifiManager)
	 * getSystemService(Context.WIFI_SERVICE); WifiInfo info = (null == wifiMgr
	 * ? null : wifiMgr.getConnectionInfo()); if (null != info) { macAddress =
	 * info.getMacAddress(); //ip = int2ip(info.getIpAddress()); } return
	 * macAddress; }
	 */

	private String getViersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "获取版本错误";
		}
	}

	/**
	 * 获取当前应用包名
	 *
	 * @return 包名
	 */
	private String getAppPackageName() {
		PackageManager pm = getPackageManager();
		PackageInfo info;
		try {
			info = pm.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return "";
		}
		String packName = info.packageName;
		return packName;
	}

	class InstallBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null
					&& intent.getAction().equals(
					"android.intent.action.BOOT_COMPLETED")) {
				param = true;
				Log.i(TAG, "开机启动");
			}
		}

	}

	private void onlinErrorDialog(String res) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				SplashActivity.this);
		dialog.setCancelable(false);
		dialog.setTitle(getString(R.string.title));
		View view = inflater.inflate(R.layout.activity_dig_error, null);
		TextView errorMsg = (TextView) view.findViewById(R.id.errorMsg);
		TextView phone = (TextView) view.findViewById(R.id.phone);
		TextView wifimac = (TextView) view.findViewById(R.id.wifimac);
		TextView echmac = (TextView) view.findViewById(R.id.echmac);
		TextView errorCode = (TextView) view.findViewById(R.id.errorCode);
		TextView username = (TextView) view.findViewById(R.id.username);
		String serviceTel = sp.getString("serviceTel", "");
		String userNameText = sp.getString("userNameText", "");
		errorMsg.setText("         用户您好，您使用的柠檬OSS电视盒出现问题，请记住您的柠檬账号，并联系客服。");
		if (serviceTel != null && !"".equals(serviceTel)) {
			phone.setText("客服号码：" + serviceTel);
			phone.setVisibility(View.VISIBLE);
		}
		if (macid != null && !"".equals(macid)) {
			wifimac.setText("无线物理地址：" + macid);
			wifimac.setVisibility(View.VISIBLE);
		}
		if (mac_eth != null && !"".equals(mac_eth)) {
			echmac.setText("有线物理地址：" + mac_eth);
			echmac.setVisibility(View.VISIBLE);
		}
		if (userNameText != null && !"".equals(userNameText)) {
			username.setText("柠檬账号：" + userNameText);
			username.setVisibility(View.VISIBLE);
		}
		if ("2".equals(res)) {
			errorCode.setText("错误码：2");
		} else if ("3".equals(res)) {
			errorCode.setText("错误码：3");
		} else if ("4".equals(res)) {
			errorCode.setText("错误码：4");
		} else {
			errorCode.setText("错误码：5");
		}
		dialog.setView(view);
		dialog.setPositiveButton("重启", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getApplicationContext(),
						SplashActivity.class);
				startActivity(intent);
			}
		});
		dialog.create().show();
	}

}
