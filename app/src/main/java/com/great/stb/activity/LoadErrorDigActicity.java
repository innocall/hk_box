package com.great.stb.activity;

import com.great.stb.R;

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
	
}
