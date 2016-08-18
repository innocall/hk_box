package com.great.stb;

import java.util.List;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.great.stb.MyView.MyTextView;
import com.great.stb.activity.SplashActivity;
import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.bean.LogBean;
import com.great.stb.common.UpdateRequest;
import com.great.stb.dao.ElementDAO;
import com.great.stb.database.SmartSTBSQLiteOpenHelper;
import com.great.stb.util.ImageUtils;
import com.great.stb.util.Util;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	private FragmentManager fm;
	private FragmentTransaction ft;
	private TextView tv_title_01;
	private TextView tv_title_02;
	private TextView tv_title_03;
	private TextView tv_title_04;
	private int position_one;
	private int position_two;
	private int position_tree;
	private int position_four;
	private ImageView ivBottomLine;
	private int currIndex = 1;
	private View fragment01;
	private View fragment02;
	private View fragment03;
	private View fragment04;
	private View fragment05;
	private TextView tv_title_05;
	private MyTextView tv_push;
	private boolean hasMeasured = false;
	private ImageButton ib_adver_big;
	private ImageButton ib_setting;
	private ImageButton ib_model_05;
	private ImageButton ib_model_01;
	private ImageButton ib_model_02;
	private ImageButton ib_account_bg;
	private ImageButton ib_native_video_bg;
	private ImageButton ib_native_music_bg;
	private ImageButton ib_native_picture_bg;
	private ImageButton ib_local_bg;
	//private TextView tv_title_position;
	//private TextView tv_title_weather;
	private TextView tv_title_time;
	private ImageView tv_title_wifi;
	private String boxurl;
	private String macid;

	private Animation ani;
	private int width;
	private int width_font;

	protected static final int BOX_TIME = 1;
	protected static final int APP_TOP = 2;
	private ImageView iv_logo01;
	private ImageView iv_logo02;
	private ImageView iv_logo03;
	private TextView tv_username;

	private int i = 1;
	private boolean flag_time = false;
	private int flag_back = 1;

	private String strapp = null;
	// 判断是否有网络
	private boolean detect = true;
	private boolean detect_flage = true;
	private boolean detect_flage_frist = true;
	private int dw,dh;
	// 日志
	private static final Logger logger = LoggerFactory
			.getLogger(MainActivity.class);

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

				case BOX_TIME:
					if (detect != detect_flage) {
						detect = detect_flage;
						if (detect) {
							tv_title_wifi.setImageResource(R.drawable.logo_wifi);
							if (!detect_flage_frist) {
								finish();
								Intent intent = new Intent(getApplicationContext(),
										SplashActivity.class);
								startActivity(intent);
								detect_flage_frist = true;
							}
						} else {
							tv_title_wifi.setImageResource(R.drawable.logo_no_wifi);
						}

						// MainActivity.this.finish();
						// Intent intent = new Intent(getApplicationContext(),
						// SplashActivity.class);
						// startActivity(intent);
					}
					long sysTime = System.currentTimeMillis();
					CharSequence sysTimeStr = null;
				/*if (i % 40 == 0) {
					flag_time = !flag_time;
				}*/

			/*	if (flag_time) {
					sysTimeStr = DateFormat.format("yyyy-MM-dd", sysTime);
				} else {*/
					sysTimeStr = DateFormat.format("kk:mm:ss", sysTime);
					//}
					i++;

					tv_title_time.setText(sysTimeStr);
					break;

				case APP_TOP:

					String pagename = (String) msg.obj;
					logger.debug("xxxxxxxxx" + pagename);

					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		boolean detect2 = Util.detect(getApplicationContext());
		//detect = detect2;
		detect_flage = detect2;
		detect_flage_frist = getIntent().getBooleanExtra("WIFI", false);
		detect = detect_flage_frist;
		Display display = getWindowManager().getDefaultDisplay();
		dw = display.getWidth();
		dh = display.getHeight();
		boxurl = getString(R.string.boxurl);
		// macid =getString(R.string.macid);
		// macid =Util.getLocalMacAddress(getApplicationContext());
		macid = getWifiMacAddress();
		// 日志
		PropertyConfigurator.getConfigurator(this).configure();

		initDatabase();
		initData();
		initKey();
		initFragment();
		initLogText();
		// 获取顶部应用日志
		// initAppTop();

	}

	private void initLogText() {
		new Thread() {
			public void run() {
				String SDCarePath = Environment.getExternalStorageDirectory()
						.toString();
				String filePath = SDCarePath + "/greatstb.txt";
				String url1 = boxurl + "/WebService.asmx/box_upload_log";

				DeviceInfoMode deviceInfo = new DeviceInfoMode();
				deviceInfo.setMac(macid);
				List<LogBean> logs = UpdateRequest.ReadTxtFile(filePath);
				boolean logUpLoadResponse = UpdateRequest.logUpLoadResponse(
						deviceInfo, logs, url1);

				if (logUpLoadResponse) {
					UpdateRequest.removeContext(filePath);
				}
			};
		}.start();
	}

	private void initAppTop() {
		new Thread() {
			public void run() {
				do {
					String currentPk = Util
							.getCurrentPk(getApplicationContext());
					if (strapp.equals(currentPk)) {

					} else {
						strapp = currentPk;
						Message msg = new Message();
						msg.what = APP_TOP;
						msg.obj = currentPk;
						handler.sendMessage(msg);
					}

				} while (true);

			};

		}.start();

	}

	private void initDatabase() {
		SmartSTBSQLiteOpenHelper helper = new SmartSTBSQLiteOpenHelper(this);
		helper.getWritableDatabase();
	}

	private void initData() {
		iv_logo01 = (ImageView) findViewById(R.id.iv_logo01);
		iv_logo02 = (ImageView) findViewById(R.id.iv_logo02);
		iv_logo03 = (ImageView) findViewById(R.id.logo3);

		tv_title_01 = (TextView) findViewById(R.id.tv_title_01);
		tv_title_02 = (TextView) findViewById(R.id.tv_title_02);
		tv_title_03 = (TextView) findViewById(R.id.tv_title_03);
		tv_title_04 = (TextView) findViewById(R.id.tv_title_04);
		tv_title_05 = (TextView) findViewById(R.id.tv_title_05);
		tv_username = (TextView) findViewById(R.id.tv_username);

		//tv_title_position = (TextView) findViewById(R.id.tv_title_position);
		//tv_title_weather = (TextView) findViewById(R.id.tv_title_weather);
		tv_title_time = (TextView) findViewById(R.id.tv_title_time);
		tv_title_wifi = (ImageView) findViewById(R.id.tv_title_wifi);

		boolean wifiFlage = getIntent().getBooleanExtra("WIFI", false);
		if (!wifiFlage) {
			tv_title_wifi.setImageResource(R.drawable.logo_no_wifi);
		} else {
			tv_title_wifi.setImageResource(R.drawable.logo_wifi);
		}

		ib_adver_big = (ImageButton) findViewById(R.id.ib_adver_big);
		ib_model_05 = (ImageButton) findViewById(R.id.ib_model_05);
		ib_model_01 = (ImageButton) findViewById(R.id.ib_model_01);
		ib_model_02 = (ImageButton) findViewById(R.id.ib_model_02);
		ib_setting = (ImageButton) findViewById(R.id.ib_setting);
		ib_account_bg = (ImageButton) findViewById(R.id.ib_account_bg);
		ib_local_bg = (ImageButton) findViewById(R.id.ib_local_bg);
		ib_native_video_bg = (ImageButton) findViewById(R.id.ib_native_video_bg);
		ib_native_music_bg = (ImageButton) findViewById(R.id.ib_native_music_bg);
		ib_native_picture_bg = (ImageButton) findViewById(R.id.ib_native_picture_bg);
		// 去除大图的焦点
		ib_adver_big.setFocusable(false);
		ib_adver_big.setFocusableInTouchMode(false);
		// 推送字体滚动
		tv_push = (MyTextView) findViewById(R.id.tv_push);

		tv_title_01.setOnClickListener(this);
		tv_title_02.setOnClickListener(this);
		tv_title_03.setOnClickListener(this);
		tv_title_04.setOnClickListener(this);
		tv_title_05.setOnClickListener(this);

		tv_title_01.setOnFocusChangeListener(this);
		tv_title_02.setOnFocusChangeListener(this);
		tv_title_03.setOnFocusChangeListener(this);
		tv_title_04.setOnFocusChangeListener(this);
		tv_title_05.setOnFocusChangeListener(this);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int dip2px = Util.dip2px(getApplicationContext(), 120);
		int screenW = dm.widthPixels - dip2px;
		position_one = (int) (screenW / 9.0);
		position_two = position_one * 2;
		position_tree = position_one * 3;
		position_four = position_one * 4;

		ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
		LayoutParams params = ivBottomLine.getLayoutParams();
		params.width = position_one;
		ivBottomLine.setLayoutParams(params);

		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		String desktopText = sp.getString("desktopText", " ");
		String positionText = sp.getString("positionText", " ");
		String weatherText = sp.getString("weatherText", " ");
		String userNameText = sp.getString("userNameText", " ");
		//tv_title_position.setText(positionText);
		//tv_title_weather.setText(weatherText);
		//desktopText = "6月16日福特大促销...";
		String str = desktopText.replaceAll("\\|", "             ");
		tv_push.setText(str);
		tv_push.setCircleTimes(100);
		tv_push.setSpeed(20);
		tv_username.setText("柠檬ID：" + userNameText);

		String SDCarePath = Environment.getExternalStorageDirectory()
				.toString();
		String filePath1 = SDCarePath + "/myImage/" + "Logo1" + ".png";
		//String filePath2 = SDCarePath + "/myImage/" + "Logo2" + ".png";
		String filePath3 = SDCarePath + "/myImage/" + "Logo3" + ".png";
		Bitmap logo1 = ImageUtils.decodeFileToCompress(filePath1, dw, dh);
		//Bitmap logo2 = BitmapFactory.decodeFile(filePath2);
		iv_logo01.setImageBitmap(logo1);
		//iv_logo02.setImageBitmap(logo2);
		boolean isLicense = getIntent().getBooleanExtra("isLicense", false);
		if (isLicense) {
			Bitmap logo3 = ImageUtils.decodeFileToCompress(filePath3,dw,dh);
			if(logo3 != null) {
				iv_logo03.setVisibility(View.VISIBLE);
				iv_logo03.setImageBitmap(logo3);
			}
		} 
		/*iv_logo03.setVisibility(View.VISIBLE);
		iv_logo03.setImageResource(R.drawable.cibn_logo2);*/
		//iv_logo03.setImageResource(R.drawable.cibn_logo2);
		// 标题时间更新
		getTime();

	}

	private void initKeyTitle(int id) {

		if (id == R.id.ib_adver_big) {

			// ib_adver_big.setNextFocusUpId(R.id.tv_title_01);
			// ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_01);
			// ib_adver_big.setNextFocusLeftId(View.NO_ID);
			// ib_adver_big.setNextFocusRightId(R.id.ib_model_01);

			ib_model_05.setNextFocusUpId(R.id.ib_model_03);
			ib_model_05.setNextFocusDownId(View.NO_ID);
			// ib_model_05.setNextFocusLeftId(R.id.ib_adver_big);
			ib_model_05.setNextFocusLeftId(R.id.ib_adver_small_08);
			ib_model_05.setNextFocusRightId(View.NO_ID);

			ib_model_01.setNextFocusUpId(R.id.tv_title_01);
			// ib_model_01.setNextFocusLeftId(R.id.ib_adver_big);
			ib_model_01.setNextFocusLeftId(R.id.ib_adver_small_08);

			ib_model_02.setNextFocusUpId(R.id.tv_title_01);

		} else if (id == R.id.ib_setting) {
			ib_setting.setNextFocusUpId(R.id.tv_title_05);
			ib_setting.setNextFocusDownId(View.NO_ID);
			ib_setting.setNextFocusLeftId(View.NO_ID);
			ib_setting.setNextFocusRightId(R.id.tv_title_05);

		} else if (id == R.id.gv_apps_list) {

		} else if (id == R.id.ib_account_bg) {

			ib_account_bg.setNextFocusUpId(R.id.tv_title_04);
			ib_account_bg.setNextFocusDownId(View.NO_ID);
			ib_account_bg.setNextFocusLeftId(View.NO_ID);
			ib_account_bg.setNextFocusRightId(R.id.ib_local_bg);

			ib_local_bg.setNextFocusUpId(R.id.tv_title_04);
			ib_local_bg.setNextFocusDownId(View.NO_ID);
			ib_local_bg.setNextFocusLeftId(R.id.ib_account_bg);
			ib_local_bg.setNextFocusRightId(View.NO_ID);

		} else if (id == R.id.ib_native_video_bg) {

			ib_native_video_bg.setNextFocusUpId(R.id.tv_title_03);
			ib_native_video_bg.setNextFocusDownId(View.NO_ID);
			ib_native_video_bg.setNextFocusLeftId(View.NO_ID);
			ib_native_video_bg.setNextFocusRightId(R.id.ib_native_music_bg);

			ib_native_music_bg.setNextFocusUpId(R.id.tv_title_03);
			ib_native_music_bg.setNextFocusDownId(View.NO_ID);
			ib_native_music_bg.setNextFocusLeftId(R.id.ib_native_video_bg);
			ib_native_music_bg.setNextFocusRightId(R.id.ib_native_picture_bg);

			ib_native_picture_bg.setNextFocusUpId(R.id.tv_title_03);
			ib_native_picture_bg.setNextFocusDownId(View.NO_ID);
			ib_native_picture_bg.setNextFocusLeftId(R.id.ib_native_music_bg);
			ib_native_picture_bg.setNextFocusRightId(R.id.tv_title_03);
		}

		tv_title_01.setNextFocusUpId(View.NO_ID);
		tv_title_01.setNextFocusDownId(R.id.ib_adver_small_01);
		tv_title_01.setNextFocusLeftId(R.id.tv_title_05);
		tv_title_01.setNextFocusRightId(R.id.tv_title_02);

		tv_title_02.setNextFocusUpId(View.NO_ID);
		tv_title_02.setNextFocusDownId(id);
		tv_title_02.setNextFocusLeftId(R.id.tv_title_01);
		tv_title_02.setNextFocusRightId(R.id.tv_title_03);

		tv_title_03.setNextFocusUpId(View.NO_ID);
		tv_title_03.setNextFocusDownId(id);
		tv_title_03.setNextFocusLeftId(R.id.tv_title_02);
		tv_title_03.setNextFocusRightId(R.id.tv_title_04);

		tv_title_04.setNextFocusUpId(View.NO_ID);
		tv_title_04.setNextFocusDownId(id);
		tv_title_04.setNextFocusLeftId(R.id.tv_title_03);
		tv_title_04.setNextFocusRightId(R.id.tv_title_05);

		tv_title_05.setNextFocusUpId(View.NO_ID);
		tv_title_05.setNextFocusDownId(id);
		tv_title_05.setNextFocusLeftId(R.id.tv_title_04);
		tv_title_05.setNextFocusRightId(R.id.tv_title_01);

	}

	private void initKey() {

		tv_title_01.setNextFocusUpId(View.NO_ID);
		tv_title_01.setNextFocusDownId(R.id.ib_adver_small_01);
		tv_title_01.setNextFocusLeftId(R.id.tv_title_05);
		tv_title_01.setNextFocusRightId(R.id.tv_title_02);

		tv_title_02.setNextFocusUpId(View.NO_ID);
		tv_title_02.setNextFocusDownId(R.id.ib_adver_small_01);
		tv_title_02.setNextFocusLeftId(R.id.tv_title_01);
		tv_title_02.setNextFocusRightId(R.id.tv_title_03);

		tv_title_03.setNextFocusUpId(View.NO_ID);
		tv_title_03.setNextFocusDownId(R.id.ib_adver_small_01);
		tv_title_03.setNextFocusLeftId(R.id.tv_title_02);
		tv_title_03.setNextFocusRightId(R.id.tv_title_04);

		tv_title_04.setNextFocusUpId(View.NO_ID);
		tv_title_04.setNextFocusDownId(R.id.ib_adver_small_01);
		tv_title_04.setNextFocusLeftId(R.id.tv_title_03);
		tv_title_04.setNextFocusRightId(R.id.tv_title_05);

		tv_title_05.setNextFocusUpId(View.NO_ID);
		tv_title_05.setNextFocusDownId(R.id.ib_adver_small_01);
		tv_title_05.setNextFocusLeftId(R.id.tv_title_04);
		tv_title_05.setNextFocusRightId(R.id.tv_title_01);

		// ib_adver_big.setNextFocusUpId(R.id.tv_title_01);
		// ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_01);
		// ib_adver_big.setNextFocusLeftId(View.NO_ID);
		// ib_adver_big.setNextFocusRightId(R.id.ib_model_01);

		ib_model_01.setNextFocusUpId(R.id.tv_title_01);
		ib_model_01.setNextFocusLeftId(R.id.ib_adver_small_08);

		ib_model_02.setNextFocusUpId(R.id.tv_title_01);

		ib_model_05.setNextFocusUpId(R.id.ib_model_03);
		ib_model_05.setNextFocusDownId(View.NO_ID);
		ib_model_05.setNextFocusLeftId(R.id.ib_adver_small_08);
		// ib_model_05.setNextFocusLeftId(R.id.ib_adver_big);
		ib_model_05.setNextFocusRightId(View.NO_ID);

	}

	/**
	 * 使头像变灰
	 *
	 * @param drawable
	 */
	public static void porBecomeGrey(ImageView imageView, Drawable drawable) {
		drawable.mutate();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
		drawable.setColorFilter(cf);
		imageView.setImageDrawable(drawable);
	}

	private void initFragment() {
		fragment01 = findViewById(R.id.fragment01);
		fragment02 = findViewById(R.id.fragment02);
		fragment03 = findViewById(R.id.fragment03);
		fragment04 = findViewById(R.id.fragment04);
		fragment05 = findViewById(R.id.fragment05);
		fragment01.setVisibility(View.VISIBLE);
		fragment02.setVisibility(View.GONE);
		fragment03.setVisibility(View.GONE);
		fragment04.setVisibility(View.GONE);
		fragment05.setVisibility(View.GONE);
		/*
		 * fm = getFragmentManager(); ft = fm.beginTransaction();
		 * ft.replace(R.id.content, new Fragment01()); ft.commit();
		 */
	}

	@Override
	public void onClick(View view) {
		// ft = fm.beginTransaction();

		switch (view.getId()) {
			case R.id.tv_title_01:
				tv_title_01.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_02.setTextColor(Color.parseColor("#596175"));
				tv_title_03.setTextColor(Color.parseColor("#596175"));
				tv_title_04.setTextColor(Color.parseColor("#596175"));
				tv_title_05.setTextColor(Color.parseColor("#596175"));
				initKeyTitle(R.id.ib_adver_big);
				fragment01.setVisibility(View.VISIBLE);
				fragment02.setVisibility(View.GONE);
				fragment03.setVisibility(View.GONE);
				fragment04.setVisibility(View.GONE);
				fragment05.setVisibility(View.GONE);
				// ft.replace(R.id.content, new Fragment01());
				break;
			case R.id.tv_title_02:
				tv_title_02.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_01.setTextColor(Color.parseColor("#596175"));
				tv_title_03.setTextColor(Color.parseColor("#596175"));
				tv_title_04.setTextColor(Color.parseColor("#596175"));
				tv_title_05.setTextColor(Color.parseColor("#596175"));
				initKeyTitle(R.id.gv_apps_list);
				fragment01.setVisibility(View.GONE);
				fragment02.setVisibility(View.VISIBLE);
				fragment03.setVisibility(View.GONE);
				fragment04.setVisibility(View.GONE);
				fragment05.setVisibility(View.GONE);
				// ft.replace(R.id.content, new Fragment02());
				break;
			case R.id.tv_title_03:
				tv_title_03.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_02.setTextColor(Color.parseColor("#596175"));
				tv_title_01.setTextColor(Color.parseColor("#596175"));
				tv_title_04.setTextColor(Color.parseColor("#596175"));
				tv_title_05.setTextColor(Color.parseColor("#596175"));
				initKeyTitle(R.id.ib_native_video_bg);
				fragment01.setVisibility(View.GONE);
				fragment02.setVisibility(View.GONE);
				fragment03.setVisibility(View.VISIBLE);
				fragment04.setVisibility(View.GONE);
				fragment05.setVisibility(View.GONE);
				// ft.replace(R.id.content, new Fragment03());
				break;
			case R.id.tv_title_04:
				tv_title_04.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_02.setTextColor(Color.parseColor("#596175"));
				tv_title_03.setTextColor(Color.parseColor("#596175"));
				tv_title_01.setTextColor(Color.parseColor("#596175"));
				tv_title_05.setTextColor(Color.parseColor("#596175"));
				initKeyTitle(R.id.ib_account_bg);
				fragment01.setVisibility(View.GONE);
				fragment02.setVisibility(View.GONE);
				fragment03.setVisibility(View.GONE);
				fragment04.setVisibility(View.VISIBLE);
				fragment05.setVisibility(View.GONE);
				// ft.replace(R.id.content, new Fragment04());
				// tv_title_04.setTextColor(getResources().getColor(R.color.text_color_white));
				break;
			case R.id.tv_title_05:
				tv_title_05.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_02.setTextColor(Color.parseColor("#596175"));
				tv_title_03.setTextColor(Color.parseColor("#596175"));
				tv_title_04.setTextColor(Color.parseColor("#596175"));
				tv_title_01.setTextColor(Color.parseColor("#596175"));
				initKeyTitle(R.id.ib_setting);
				fragment01.setVisibility(View.GONE);
				fragment02.setVisibility(View.GONE);
				fragment03.setVisibility(View.GONE);
				fragment04.setVisibility(View.GONE);
				// fragment05.requestFocus();
				fragment05.setVisibility(View.VISIBLE);
				// ft.replace(R.id.content, new Fragment03());
				break;

		}

		// ft.commit();

	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {

		Animation animation = null;

		switch (view.getId()) {

			case R.id.tv_title_01:

				flag_back = 1;

				// FIXME:bug1 的更改
				tv_title_01.setFocusable(true);
				// if (hasFocus) {
				tv_title_01.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_02.setTextColor(Color.parseColor("#596175"));
				tv_title_03.setTextColor(Color.parseColor("#596175"));
				tv_title_04.setTextColor(Color.parseColor("#596175"));
				tv_title_05.setTextColor(Color.parseColor("#596175"));
			/*
			 * } else { tv_title_01.setTextColor(Color.parseColor("#596175")); }
			 */

			/*
			 * if (currIndex == 5) { animation = new
			 * TranslateAnimation(position_four, 0, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 1; } else if
			 * (currIndex == 2) { animation = new
			 * TranslateAnimation(position_one, 0, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 1; } else if
			 * (currIndex == 3) { animation = new
			 * TranslateAnimation(position_two, 0, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 1; } else if
			 * (currIndex == 4) { animation = new
			 * TranslateAnimation(position_tree, 0, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 1; }
			 */

				initKeyTitle(R.id.ib_adver_big);
				fragment01.setVisibility(View.VISIBLE);
				fragment02.setVisibility(View.GONE);
				fragment03.setVisibility(View.GONE);
				fragment04.setVisibility(View.GONE);
				fragment05.setVisibility(View.GONE);

				break;
			case R.id.tv_title_02:
				tv_title_02.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_01.setTextColor(Color.parseColor("#596175"));
				tv_title_03.setTextColor(Color.parseColor("#596175"));
				tv_title_04.setTextColor(Color.parseColor("#596175"));
				tv_title_05.setTextColor(Color.parseColor("#596175"));
				flag_back = 2;
			/*
			 * if (hasFocus) {
			 * tv_title_02.setTextColor(Color.parseColor("#FFFFFF"));
			 * 
			 * } else { tv_title_02.setTextColor(Color.parseColor("#596175")); }
			 */
			/*
			 * if (currIndex == 1) { animation = new TranslateAnimation(0,
			 * position_one, 0, 0); animation.setFillAfter(true);
			 * animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 2; } else if
			 * (currIndex == 3) { animation = new
			 * TranslateAnimation(position_two, position_one, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 2; } else if
			 * (currIndex == 4) { animation = new
			 * TranslateAnimation(position_tree, position_one, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 2; } else if
			 * (currIndex == 5) { animation = new
			 * TranslateAnimation(position_four, position_one, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 2; }
			 */

				initKeyTitle(R.id.gv_apps_list);
				fragment01.setVisibility(View.GONE);
				fragment02.setVisibility(View.VISIBLE);
				fragment03.setVisibility(View.GONE);
				fragment04.setVisibility(View.GONE);
				fragment05.setVisibility(View.GONE);

				break;
			case R.id.tv_title_03:

				flag_back = 3;
			/*
			 * if (hasFocus) {
			 * tv_title_03.setTextColor(Color.parseColor("#FFFFFF")); } else {
			 * tv_title_03.setTextColor(Color.parseColor("#596175")); }
			 */
				tv_title_03.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_02.setTextColor(Color.parseColor("#596175"));
				tv_title_01.setTextColor(Color.parseColor("#596175"));
				tv_title_04.setTextColor(Color.parseColor("#596175"));
				tv_title_05.setTextColor(Color.parseColor("#596175"));
			/*
			 * if (currIndex == 2) { animation = new
			 * TranslateAnimation(position_one, position_two, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 3; } else if
			 * (currIndex == 4) { animation = new
			 * TranslateAnimation(position_tree, position_two, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 3; } else if
			 * (currIndex == 1) { animation = new TranslateAnimation(0,
			 * position_two, 0, 0); animation.setFillAfter(true);
			 * animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 3; } else if
			 * (currIndex == 5) { animation = new
			 * TranslateAnimation(position_four, position_two, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 3; }
			 */

				initKeyTitle(R.id.ib_native_video_bg);
				fragment01.setVisibility(View.GONE);
				fragment02.setVisibility(View.GONE);
				fragment03.setVisibility(View.VISIBLE);
				fragment04.setVisibility(View.GONE);
				fragment05.setVisibility(View.GONE);
				break;
			case R.id.tv_title_04:
				flag_back = 4;
				tv_title_04.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_02.setTextColor(Color.parseColor("#596175"));
				tv_title_03.setTextColor(Color.parseColor("#596175"));
				tv_title_01.setTextColor(Color.parseColor("#596175"));
				tv_title_05.setTextColor(Color.parseColor("#596175"));
			/*
			 * if (hasFocus) {
			 * tv_title_04.setTextColor(Color.parseColor("#FFFFFF")); } else {
			 * tv_title_04.setTextColor(Color.parseColor("#596175")); }
			 */

			/*
			 * if (currIndex == 3) { animation = new
			 * TranslateAnimation(position_two, position_tree, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 4; } else if
			 * (currIndex == 5) { animation = new
			 * TranslateAnimation(position_four, position_tree, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 4; } else if
			 * (currIndex == 1) { animation = new TranslateAnimation(0,
			 * position_tree, 0, 0); animation.setFillAfter(true);
			 * animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 4; } else if
			 * (currIndex == 2) { animation = new
			 * TranslateAnimation(position_one, position_tree, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 4; }
			 */

				initKeyTitle(R.id.ib_account_bg);
				fragment01.setVisibility(View.GONE);
				fragment02.setVisibility(View.GONE);
				fragment03.setVisibility(View.GONE);
				fragment04.setVisibility(View.VISIBLE);
				fragment05.setVisibility(View.GONE);

				break;

			case R.id.tv_title_05:

				flag_back = 5;
			/*
			 * if (hasFocus) {
			 * tv_title_05.setTextColor(Color.parseColor("#FFFFFF")); } else {
			 * tv_title_05.setTextColor(Color.parseColor("#596175")); }
			 */
				tv_title_05.setTextColor(Color.parseColor("#FFFFFF"));
				tv_title_02.setTextColor(Color.parseColor("#596175"));
				tv_title_03.setTextColor(Color.parseColor("#596175"));
				tv_title_04.setTextColor(Color.parseColor("#596175"));
				tv_title_01.setTextColor(Color.parseColor("#596175"));

			/*
			 * if (currIndex == 1) { animation = new TranslateAnimation(0,
			 * position_four, 0, 0); animation.setFillAfter(true);
			 * animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 5; } else if
			 * (currIndex == 4) { animation = new
			 * TranslateAnimation(position_tree, position_four, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 5; }
			 */

				initKeyTitle(R.id.ib_setting);
				fragment01.setVisibility(View.GONE);
				fragment02.setVisibility(View.GONE);
				fragment03.setVisibility(View.GONE);
				fragment04.setVisibility(View.GONE);
				// fragment05.requestFocus();
				fragment05.setVisibility(View.VISIBLE);

				break;
		}
	}

	// 时间显示
	public void getTime() {

		new Thread() {

			public void run() {

				do {
					try {
						detect_flage = Util.detect(getApplicationContext());
						Thread.sleep(1000);
						Message msg = new Message();
						msg.what = BOX_TIME;
						handler.sendMessage(msg);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (true);

			};

		}.start();

	}

	// back 键处理
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//	Animation animation = null;
			tv_title_01.requestFocus();
			//tv_title_01.setFocusable(true);
			//tv_title_01.setFocusableInTouchMode(true);
			/*
			 * if (currIndex == 5) {
			 * tv_title_05.setTextColor(Color.parseColor("#596175")); animation
			 * = new TranslateAnimation(position_four, 0, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 1; } else if
			 * (currIndex == 2) {
			 * tv_title_02.setTextColor(Color.parseColor("#596175")); animation
			 * = new TranslateAnimation(position_one, 0, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 1; } else if
			 * (currIndex == 3) {
			 * tv_title_03.setTextColor(Color.parseColor("#596175")); animation
			 * = new TranslateAnimation(position_two, 0, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 1; } else if
			 * (currIndex == 4) {
			 * tv_title_04.setTextColor(Color.parseColor("#596175")); animation
			 * = new TranslateAnimation(position_tree, 0, 0, 0);
			 * animation.setFillAfter(true); animation.setDuration(300);
			 * ivBottomLine.startAnimation(animation); currIndex = 1; }
			 */
			// tv_title_01.setTextColor(Color.parseColor("#FFFFFF"));
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

}
