package com.great.stb.activity;

import com.great.stb.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;

public class AccountInfoActivity extends Activity {

	private TextView tv_account_01;
	private TextView tv_account_02;
	private View ll_account_info;
	private TextView tv_account_info_username;
	private TextView tv_account_info_telphone;
	private TextView tv_account_info_regDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_info);
		initData();
		initListener();
	}

	private void initData() {

		ll_account_info = findViewById(R.id.ll_account_info);
		tv_account_01 = (TextView) findViewById(R.id.tv_account_01);
		tv_account_02 = (TextView) findViewById(R.id.tv_account_02);
		tv_account_info_username = (TextView) findViewById(R.id.tv_account_info_username);
		tv_account_info_telphone = (TextView) findViewById(R.id.tv_account_info_telphone);
		tv_account_info_regDate = (TextView) findViewById(R.id.tv_account_info_regDate);
		
		
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		String userNameText = sp.getString("userNameText", "");
		String telphone = sp.getString("serviceTel", "");
		String activeDate = sp.getString("activeDate", "");
		tv_account_info_username.setText(userNameText);
		tv_account_info_telphone.setText(telphone);
		tv_account_info_regDate.setText(activeDate);
	}

	private void initListener() {
		ll_account_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ll_account_info.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					tv_account_01.setTextColor(Color.parseColor("#ff0000"));
					tv_account_02.setTextColor(Color.parseColor("#ff0000"));
				} else {
					tv_account_01.setTextColor(Color.parseColor("#ffffff"));
					tv_account_02.setTextColor(Color.parseColor("#ffffff"));
				}

			}
		});
	}

}
