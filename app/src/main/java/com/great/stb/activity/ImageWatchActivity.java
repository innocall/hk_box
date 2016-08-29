package com.great.stb.activity;

import java.io.BufferedInputStream;
import java.net.URL;

import com.great.stb.R;
import com.great.stb.util.Util;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageWatchActivity extends Activity {
	private ImageView imageView;
	private String url;
	private Bitmap mBitmap;
	private int dw,dh;
	private static final int IMAGEUPDATE = 1;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
				case IMAGEUPDATE:
					imageView.setImageBitmap(mBitmap);
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_image);
		imageView = (ImageView) findViewById(R.id.image);
		//imageView.setImageResource(R.drawable.nowifi_image_menu1);//不要默认
		url = getIntent().getStringExtra("url");
		// 1. 首先得到屏幕的宽高
		Display currentDisplay = getWindowManager().getDefaultDisplay();
		dw = currentDisplay.getWidth() - 150;
		dh = currentDisplay.getHeight() - 150;
		downLoadImag(url);
		imageView.setFocusable(true);
		imageView.setFocusableInTouchMode(true);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				close();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ImageWatchActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ImageWatchActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

	private void downLoadImag(final String filePath) {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				mBitmap =  Util.getImageFromURL2(url,dw,dh);
				handler.sendEmptyMessage(IMAGEUPDATE);
			};
		}.start();
	}

	public void close() {
		finish();
		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}

	public Bitmap getBitmapFromUrl() throws Exception {
		URL mUrl = new URL(url);
		BufferedInputStream bis = new BufferedInputStream(mUrl.openConnection()
				.getInputStream());
		Bitmap bitmap = BitmapFactory.decodeStream(bis);
		bis.close();
		return bitmap;
	}
}
