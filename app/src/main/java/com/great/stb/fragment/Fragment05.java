package com.great.stb.fragment;


import com.great.stb.R;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Fragment02
 * @author kevin
 *
 */
public class Fragment05 extends Fragment {
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment05, null);
		
		initData();
		
		return view;
		
	}

	private void initData() {
		
		ImageButton ib_setting = (ImageButton) view.findViewById(R.id.ib_setting);
		ib_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(Settings.ACTION_SETTINGS);
				startActivity(intent);*/
				Intent mIntent = new Intent();
				ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.VIEW");
				startActivity(mIntent);
			}
		});
		
	}
}
