package com.great.stb.activity;

import com.great.stb.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoadErrorDigActicity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dig_error);
		Intent intent = getIntent();
		String result = intent.getStringExtra("result");
		
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("LoadErrorDigActicity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("LoadErrorDigActicity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}
}
