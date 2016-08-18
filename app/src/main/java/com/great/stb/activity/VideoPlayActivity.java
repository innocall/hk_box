package com.great.stb.activity;

import java.util.ArrayList;
import java.util.List;

import com.great.stb.R;
import com.great.stb.bean.ActDataMode;
import com.great.stb.dao.ElementDAO;
import com.great.stb.fragment.Fragment01;
import com.great.stb.fragment.FragmentVideoPaly01;
import com.great.stb.fragment.FragmentVideoPaly02;
import com.great.stb.fragment.FragmentVideoPaly03;
import com.great.stb.fragment.FragmentVideoPaly04;
import com.great.stb.fragment.FragmentVideoPaly05;
import com.great.stb.util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoPlayActivity extends Activity implements OnClickListener,
		OnFocusChangeListener, FragmentVideoPaly01.Callbacks,
		FragmentVideoPaly02.Callbacks, FragmentVideoPaly03.Callbacks,
		FragmentVideoPaly04.Callbacks, FragmentVideoPaly05.Callbacks{

	private TextView tv_video_play01;
	private TextView tv_video_play02;
	private TextView tv_video_play03;
	private TextView tv_video_play04;
	private TextView tv_video_play05;
	private int position_one;
	private int position_two;
	private int position_three;
	private int position_four;
	private ImageView ivBottomLine;
	private int currIndex = 1;
	private View fragment_video_play01;
	private View fragment_video_play02;
	private View fragment_video_play03;
	private View fragment_video_play04;
	private View fragment_video_play05;
	private List<Integer> videoSetList;
	private GridView gv_video_list01;
	private GridView gv_video_list02;
	private GridView gv_video_list03;
	private GridView gv_video_list04;
	private GridView gv_video_list05;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_paly);
		initData();
		initTitle();
		initKeyTitle(R.id.gv_video_list01);
		initFragment();
	}

	private void initTitle() {
		List<Integer> list = getVideoSet();
		if(list.size()<=18){
			tv_video_play02.setVisibility(View.INVISIBLE);
			tv_video_play03.setVisibility(View.INVISIBLE);
			tv_video_play04.setVisibility(View.INVISIBLE);
			tv_video_play05.setVisibility(View.INVISIBLE);
		}else if(list.size()<=36){
			tv_video_play03.setVisibility(View.INVISIBLE);
			tv_video_play04.setVisibility(View.INVISIBLE);
			tv_video_play05.setVisibility(View.INVISIBLE);

		}else if(list.size()<=54){
			tv_video_play04.setVisibility(View.INVISIBLE);
			tv_video_play05.setVisibility(View.INVISIBLE);

		}else if(list.size()<=72){
			tv_video_play05.setVisibility(View.INVISIBLE);
		}else{

		}

	}

	private void initData() {
		tv_video_play01 = (TextView) findViewById(R.id.tv_video_play01);
		tv_video_play02 = (TextView) findViewById(R.id.tv_video_play02);
		tv_video_play03 = (TextView) findViewById(R.id.tv_video_play03);
		tv_video_play04 = (TextView) findViewById(R.id.tv_video_play04);
		tv_video_play05 = (TextView) findViewById(R.id.tv_video_play05);
		gv_video_list01 = (GridView) findViewById(R.id.gv_video_list01);
		gv_video_list02 = (GridView) findViewById(R.id.gv_video_list02);
		gv_video_list03 = (GridView) findViewById(R.id.gv_video_list03);
		gv_video_list04 = (GridView) findViewById(R.id.gv_video_list04);
		gv_video_list05 = (GridView) findViewById(R.id.gv_video_list05);

		tv_video_play01.setOnClickListener(this);
		tv_video_play02.setOnClickListener(this);
		tv_video_play03.setOnClickListener(this);
		tv_video_play04.setOnClickListener(this);
		tv_video_play05.setOnClickListener(this);

		tv_video_play01.setOnFocusChangeListener(this);
		tv_video_play02.setOnFocusChangeListener(this);
		tv_video_play03.setOnFocusChangeListener(this);
		tv_video_play04.setOnFocusChangeListener(this);
		tv_video_play05.setOnFocusChangeListener(this);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int dip2px = Util.dip2px(getApplicationContext(), 146);
		int screenW = dm.widthPixels - dip2px;
		position_one = (int) (screenW / 9.0);
		position_two = position_one * 2;
		position_three = position_one * 3;
		position_four = position_one * 4;

		ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
		LayoutParams params = ivBottomLine.getLayoutParams();
		params.width = position_one;
		ivBottomLine.setLayoutParams(params);

	}

	private void initKeyTitle(int id) {

		if (id == R.id.gv_video_list01) {
//			gv_video_list01.setFocusable(true);
//			gv_video_list01.requestFocus();
			id = R.id.ib_video_set_bg01;
		} else if (id == R.id.gv_video_list02) {
			id = R.id.ib_video_set_bg02;
		} else if (id == R.id.gv_video_list03) {
			id = R.id.ib_video_set_bg03;
		}else if (id == R.id.gv_video_list04) {
			id = R.id.ib_video_set_bg04;
		}else if (id == R.id.gv_video_list05) {
			id = R.id.ib_video_set_bg05;
		}

		tv_video_play01.setNextFocusUpId(View.NO_ID);
		tv_video_play01.setNextFocusDownId(id);
		tv_video_play01.setNextFocusLeftId(R.id.tv_video_play05);
		tv_video_play01.setNextFocusRightId(R.id.tv_video_play02);

		tv_video_play02.setNextFocusUpId(View.NO_ID);
		tv_video_play02.setNextFocusDownId(id);
		tv_video_play02.setNextFocusLeftId(R.id.tv_video_play01);
		tv_video_play02.setNextFocusRightId(R.id.tv_video_play03);

		tv_video_play03.setNextFocusUpId(View.NO_ID);
		tv_video_play03.setNextFocusDownId(id);
		tv_video_play03.setNextFocusLeftId(R.id.tv_video_play02);
		tv_video_play03.setNextFocusRightId(R.id.tv_video_play04);

		tv_video_play04.setNextFocusUpId(View.NO_ID);
		tv_video_play04.setNextFocusDownId(id);
		tv_video_play04.setNextFocusLeftId(R.id.tv_video_play03);
		tv_video_play04.setNextFocusRightId(R.id.tv_video_play05);

		tv_video_play05.setNextFocusUpId(View.NO_ID);
		tv_video_play05.setNextFocusDownId(id);
		tv_video_play05.setNextFocusLeftId(R.id.tv_video_play04);
		tv_video_play05.setNextFocusRightId(R.id.tv_video_play01);

	}

	private void initFragment() {

		fragment_video_play01 = findViewById(R.id.fragment_video_play01);
		fragment_video_play02 = findViewById(R.id.fragment_video_play02);
		fragment_video_play03 = findViewById(R.id.fragment_video_play03);
		fragment_video_play04 = findViewById(R.id.fragment_video_play04);
		fragment_video_play05 = findViewById(R.id.fragment_video_play05);
		fragment_video_play01.setVisibility(View.VISIBLE);
		fragment_video_play02.setVisibility(View.GONE);
		fragment_video_play03.setVisibility(View.GONE);
		fragment_video_play04.setVisibility(View.GONE);
		fragment_video_play05.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.tv_video_play01:
				initKeyTitle(R.id.gv_video_list01);
				fragment_video_play01.setVisibility(View.VISIBLE);
				fragment_video_play02.setVisibility(View.GONE);
				fragment_video_play03.setVisibility(View.GONE);
				fragment_video_play04.setVisibility(View.GONE);
				fragment_video_play05.setVisibility(View.GONE);
				break;
			case R.id.tv_video_play02:
				initKeyTitle(R.id.gv_video_list02);
				fragment_video_play01.setVisibility(View.GONE);
				fragment_video_play02.setVisibility(View.VISIBLE);
				fragment_video_play03.setVisibility(View.GONE);
				fragment_video_play04.setVisibility(View.GONE);
				fragment_video_play05.setVisibility(View.GONE);
				break;
			case R.id.tv_video_play03:
				initKeyTitle(R.id.gv_video_list03);
				fragment_video_play01.setVisibility(View.GONE);
				fragment_video_play02.setVisibility(View.GONE);
				fragment_video_play03.setVisibility(View.VISIBLE);
				fragment_video_play04.setVisibility(View.GONE);
				fragment_video_play05.setVisibility(View.GONE);
				break;
			case R.id.tv_video_play04:
				initKeyTitle(R.id.gv_video_list04);
				fragment_video_play01.setVisibility(View.GONE);
				fragment_video_play02.setVisibility(View.GONE);
				fragment_video_play03.setVisibility(View.GONE);
				fragment_video_play04.setVisibility(View.VISIBLE);
				fragment_video_play05.setVisibility(View.GONE);
				break;
			case R.id.tv_video_play05:
				initKeyTitle(R.id.gv_video_list05);
				fragment_video_play01.setVisibility(View.GONE);
				fragment_video_play02.setVisibility(View.GONE);
				fragment_video_play03.setVisibility(View.GONE);
				fragment_video_play04.setVisibility(View.GONE);
				fragment_video_play05.setVisibility(View.VISIBLE);
				break;

		}

	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {

		Animation animation = null;

		switch (view.getId()) {

			case R.id.tv_video_play01:

				if (hasFocus) {
					tv_video_play01.setTextColor(Color.parseColor("#FFFFFF"));
				} else {
					tv_video_play01.setTextColor(Color.parseColor("#B9B9B9"));
				}

				if (currIndex == 5) {
					animation = new TranslateAnimation(position_four, 0, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 1;
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 1;
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_two, 0, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 1;
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(position_three, 0, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 1;
				}

				initKeyTitle(R.id.gv_video_list01);
				fragment_video_play01.setVisibility(View.VISIBLE);
				fragment_video_play02.setVisibility(View.GONE);
				fragment_video_play03.setVisibility(View.GONE);
				fragment_video_play04.setVisibility(View.GONE);
				fragment_video_play05.setVisibility(View.GONE);

				break;
			case R.id.tv_video_play02:

				if (hasFocus) {
					tv_video_play02.setTextColor(Color.parseColor("#FFFFFF"));

				} else {
					tv_video_play02.setTextColor(Color.parseColor("#B9B9B9"));
				}

				if (currIndex == 1) {
					animation = new TranslateAnimation(0, position_one, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 2;
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_two, position_one,
							0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 2;
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(position_three,
							position_one, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 2;
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(position_four, position_one,
							0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 2;
				}

				initKeyTitle(R.id.gv_video_list02);
				fragment_video_play01.setVisibility(View.GONE);
				fragment_video_play02.setVisibility(View.VISIBLE);
				fragment_video_play03.setVisibility(View.GONE);
				fragment_video_play04.setVisibility(View.GONE);
				fragment_video_play05.setVisibility(View.GONE);

				break;
			case R.id.tv_video_play03:

				if (hasFocus) {
					tv_video_play03.setTextColor(Color.parseColor("#FFFFFF"));
				} else {
					tv_video_play03.setTextColor(Color.parseColor("#B9B9B9"));
				}

				if (currIndex == 2) {
					animation = new TranslateAnimation(position_one, position_two,
							0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 3;
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(position_three,
							position_two, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 3;
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(0, position_two, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 3;
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(position_four, position_two,
							0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 3;
				}

				initKeyTitle(R.id.gv_video_list03);
				fragment_video_play01.setVisibility(View.GONE);
				fragment_video_play02.setVisibility(View.GONE);
				fragment_video_play03.setVisibility(View.VISIBLE);
				fragment_video_play04.setVisibility(View.GONE);
				fragment_video_play05.setVisibility(View.GONE);

				break;
			case R.id.tv_video_play04:

				if (hasFocus) {
					tv_video_play04.setTextColor(Color.parseColor("#FFFFFF"));
				} else {
					tv_video_play04.setTextColor(Color.parseColor("#B9B9B9"));
				}

				if (currIndex == 3) {
					animation = new TranslateAnimation(position_two,
							position_three, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 4;
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(position_four,
							position_three, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 4;
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(0, position_three, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 4;
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_one,
							position_three, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 4;
				}

				initKeyTitle(R.id.gv_video_list04);
				fragment_video_play01.setVisibility(View.GONE);
				fragment_video_play02.setVisibility(View.GONE);
				fragment_video_play03.setVisibility(View.GONE);
				fragment_video_play04.setVisibility(View.VISIBLE);
				fragment_video_play05.setVisibility(View.GONE);

				break;

			case R.id.tv_video_play05:

				if (hasFocus) {
					tv_video_play05.setTextColor(Color.parseColor("#FFFFFF"));
				} else {
					tv_video_play05.setTextColor(Color.parseColor("#B9B9B9"));
				}

				if (currIndex == 1) {
					animation = new TranslateAnimation(0, position_four, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 5;
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(position_three,
							position_four, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(300);
					ivBottomLine.startAnimation(animation);
					currIndex = 5;
				}

				initKeyTitle(R.id.gv_video_list05);
				fragment_video_play01.setVisibility(View.GONE);
				fragment_video_play02.setVisibility(View.GONE);
				fragment_video_play03.setVisibility(View.GONE);
				fragment_video_play04.setVisibility(View.GONE);
				fragment_video_play05.setVisibility(View.VISIBLE);

				break;
		}
	}

	@Override
	public List<Integer> getVideoSet() {
		videoSetList = new ArrayList<Integer>();

		Intent intent = getIntent();
		String code = AdvActivity.codeVideo;
		ElementDAO dao = new ElementDAO(this);
		ArrayList<ActDataMode> list = dao.getAllByWhereActDataMode(code);
		for(int i=0;i<list.size();i++){
			videoSetList.add(i);
		}

		return videoSetList;
	}

}
