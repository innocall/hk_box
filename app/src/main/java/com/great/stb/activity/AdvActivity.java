package com.great.stb.activity;

import java.io.File;
import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.great.stb.R;
import com.great.stb.bean.ActDataMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.dao.ElementDAO;
import com.great.stb.preorder.PreorderActivity;
import com.great.stb.util.ImageUtils;
import com.great.stb.util.Util;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.ImageView;
/**
 * 广告图片
 * @author wu
 * @version 1.0
 * @category 2014.08.28
 */
public class AdvActivity extends Activity {
	//日志
	private static final Logger logger = LoggerFactory.getLogger(AdvActivity.class);
	private int crry = 3;    //广告显示时间，默认3s
	private final static int PARAM = 1;
	protected static final int BOX_ORDER_IMAGES = 2;
	private ImageView imageview;
	private String pathName;
	private int dw,dh;  //屏幕大小
	private String code = "";
	private String actType = "";
	private SharedPreferences sp;
	private ArrayList<String> urlString = null;
	public static String codeVideo ="";
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what) {
				case BOX_ORDER_IMAGES:
					Intent intent_model01 = new Intent(AdvActivity.this,PreorderActivity.class);
					Bundle bu = new Bundle();
					bu.putStringArrayList("url", urlString);
					intent_model01.putExtras(bu);
					startActivity(intent_model01);
					finish();
					break;
				case PARAM:
					startMain(code,actType);
					break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adv);
		Bundle b = getIntent().getBundleExtra("param");
		actType = b.getString("actType");
		code = b.getString("code");
		init();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AdvActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AdvActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

	private void init() {
		// TODO Auto-generated method stub
		ElementDAO dao = new ElementDAO(AdvActivity.this);
		String filePath = dao.byCodeElementMode(code).getPopupImage();  //贴片路径
		String time = dao.byCodeElementMode(code).getPopupTime();   //显示时间
		if(filePath == null || "".equals(filePath) || time == null || "".equals(time) || "0".equals(time)) {
			startMain(code,actType);
		} else {
			String SDCarePath = Environment.getExternalStorageDirectory().toString();
			pathName = SDCarePath + "/myImage/" + "popupImage" + code + ".png";
			Display d = getWindowManager().getDefaultDisplay();
			dw = d.getWidth();
			dh = d.getHeight();
			imageview = (ImageView) findViewById(R.id.adv_fl_back_id);
			Bitmap bm = ImageUtils.decodeFileToCompress(pathName, dw, dh);
			if(bm == null) {
				//不显示贴片  
				downLoadImag(filePath);
				startMain(code,actType);
			} else {
				imageview.setImageBitmap(bm);
				Message message = new Message();
				message.what = PARAM;
				crry = Integer.parseInt(time);
				handler.sendMessageDelayed(message, crry * 1000);
			}
		}
	}

	private void downLoadImag(final String filePath) {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				Bitmap bitmap = Util.getImageFromURL(filePath);
				if(bitmap != null) {
					Util.saveImage2SD(bitmap, "popupImage" + code);
				}
			};
		}.start();
	}

	private void startMain(String code,String actType) {
		if("10".equals(actType)){
			String prictureStrTime = "0";
			long timeMillis = System.currentTimeMillis();
			prictureStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
			prictureStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();
			logger.debug("#"+prictureStrTime+","+code+","+"menu");
			//图片
			Intent intent = new Intent();
			intent.setClass(this, WatchPicActivity.class);
			intent.putExtra("imageCode", code);
			startActivity(intent);
			this.overridePendingTransition(R.anim.hyperspace_in,R.anim.hyperspace_out);
			finish();
		}else if("20".equals(actType)){
			//视频
			videoImagesLoads(code);
		}else if("30".equals(actType)){
			String prictureStrTime = "0";
			long timeMillis = System.currentTimeMillis();
			prictureStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
			prictureStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();
			logger.debug("#"+prictureStrTime+","+code+","+"menu");
			//网页
			ElementDAO dao = new ElementDAO(this);
			String filePath = dao.byCodeElementMode(code).getFilePath();
			Uri uri = Uri.parse(filePath);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			finish();
		}else if("40".equals(actType)){
			String prictureStrTime = "0";
			long timeMillis = System.currentTimeMillis();
			prictureStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
			prictureStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();
			logger.debug("#"+prictureStrTime+","+code+","+"menu");
			//应用
			ElementDAO dao = new ElementDAO(this);
			ElementMode elementMode = dao.byCodeElementMode(code);
			String appName = elementMode.getAppName();
			String[] strings = appName.split(",");
			if ("cn.cibntv.ott".equals(strings[0])) {
				try{
					Intent intent_model05 = new Intent();
					intent_model05.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					if(elementMode.getArgument() != null && !"".equals(elementMode.getArgument().trim())) {
						intent_model05.putExtra("param", elementMode.getArgument().trim());
					}
					intent_model05.setAction(strings[0]);
					startActivity(intent_model05);
				} catch(Exception e) {
					Intent intent_model05 = new Intent();
					ComponentName component05 = new ComponentName(strings[0],strings[1]);
					intent_model05.setComponent(component05);
					startActivity(intent_model05);
				}
			} else if("com.great.stb.enterprise".equals(strings[0])) {
				Intent intent_model05 = new Intent();
				ComponentName component05 = new ComponentName(strings[0],strings[1]);
				if(elementMode.getArgument() != null && !"".equals(elementMode.getArgument().trim())) {
					intent_model05.putExtra("param", elementMode.getArgument().trim());
				}
				intent_model05.setComponent(component05);
				startActivity(intent_model05);
			}else {
				Intent intent_model05 = new Intent();
				ComponentName component05 = new ComponentName(strings[0],strings[1]);
				intent_model05.setComponent(component05);
				startActivity(intent_model05);
			}
			finish();
		}else if("50".equals(actType)){
			String prictureStrTime = "0";
			long timeMillis = System.currentTimeMillis();
			prictureStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
			prictureStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();
			logger.debug("#"+prictureStrTime+","+code+","+"menu");
			//预受理工单标题
			ElementDAO dao = new ElementDAO(this);
			ElementMode elementMode = dao.byCodeElementMode(code);
			String title = elementMode.getTitle();
			String mediaId = elementMode.getMediaId();
			sp = this.getSharedPreferences("config", this.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putString("orderTitle", title);
			edit.putString("mediaId", mediaId);
			edit.commit();
			orderImagesLoads(code);
			finish();
		}
	}

	//视频播放首页图片
	public void videoImagesLoads(final String code) {
		String prictureStrTime = "0";
		long timeMillis = System.currentTimeMillis();
		prictureStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
		prictureStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();
		logger.debug("#"+prictureStrTime+","+code+","+"menu");
		codeVideo = code;
		ElementDAO dao = new ElementDAO(AdvActivity.this);
		ElementMode elementMode = dao.byCodeElementMode(code);
		String pic_large_path = elementMode.getPic_large();
		Intent intent = new Intent(this, VideoActivity.class);
		intent.putExtra("pic_large_path", pic_large_path);
		startActivity(intent);
		finish();
	}

	//预受理工单图片下载
	public void orderImagesLoads(final String code) {

		new Thread() {
			public void run() {
				urlString = new ArrayList<String>();
				ElementDAO dao = new ElementDAO(AdvActivity.this);
				ArrayList<ActDataMode> list = dao.getAllByWhereActDataMode(code);
				for(int i=0;i<list.size();i++){
					urlString.add(list.get(i).getFilePath());
				}
				Message msg = new Message();
				msg.what = BOX_ORDER_IMAGES;
				handler.sendMessage(msg);
			};
		}.start();
	}
}
