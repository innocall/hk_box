package com.great.stb.fragment;

import com.great.stb.R;
import com.great.stb.activity.AccountInfoActivity;
import com.great.stb.activity.LocalInfoActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Fragment02
 *
 * @author kevin
 *
 */
public class Fragment04 extends Fragment implements OnClickListener {

	private View view;
	private ImageButton ib_account_bg;
	private ImageButton ib_local_bg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment04, null);

		initData();
		initListener();
		return view;

	}



	private void initData() {

		ib_account_bg = (ImageButton) view.findViewById(R.id.ib_account_bg);
		ib_local_bg = (ImageButton) view.findViewById(R.id.ib_local_bg);

	}

	private void initListener() {
		ib_account_bg.setOnClickListener(this);
		ib_local_bg.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ib_account_bg:
				Intent intent_account = new Intent(getActivity(), AccountInfoActivity.class);
				startActivity(intent_account);
				break;
			case R.id.ib_local_bg:
				Intent intent_local = new Intent(getActivity(), LocalInfoActivity.class);
				startActivity(intent_local);
				break;

		}
	}
}
