package com.great.stb.fragment;

import com.great.stb.R;
import com.great.stb.activity.Grallery3DActivity;
import com.great.stb.activity.LocalAudioActivity;
import com.great.stb.activity.LocalImageActivity;
import com.great.stb.activity.LocalVideoActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * Fragment03
 *
 * @author kevin
 *
 */
public class Fragment03 extends Fragment implements OnClickListener {

	private View view;
	private ImageButton ib_native_video_bg;
	private ImageButton ib_native_music_bg;
	private ImageButton ib_native_picture_bg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment03, null);

		initData();
		initListener();

		return view;

	}

	private void initData() {

		ib_native_video_bg = (ImageButton) view
				.findViewById(R.id.ib_native_video_bg);
		ib_native_music_bg = (ImageButton) view
				.findViewById(R.id.ib_native_music_bg);
		ib_native_picture_bg = (ImageButton) view
				.findViewById(R.id.ib_native_picture_bg);

	}

	private void initListener() {
		ib_native_video_bg.setOnClickListener(this);
		ib_native_music_bg.setOnClickListener(this);
		ib_native_picture_bg.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ib_native_video_bg:
				Intent intent_video = new Intent(getActivity(), LocalVideoActivity.class);
				startActivity(intent_video);
//			Intent intent_video = new Intent(Intent.ACTION_GET_CONTENT);
//			intent_video.setType("video/*");
//			startActivity(intent_video);

				break;
			case R.id.ib_native_music_bg:
				Intent intent_music = new Intent(getActivity(), LocalAudioActivity.class);
				startActivity(intent_music);
//			Intent intent_music = new Intent(Intent.ACTION_GET_CONTENT);
//			intent_music.setType("audio/*");
//			startActivity(intent_music);

//			 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//			 intent.setType("*/*");
//			 intent.addCategory(Intent.CATEGORY_OPENABLE);
//			 try {
//			 startActivityForResult(Intent.createChooser(intent,"请选择打开方式"),1);
//			 } catch (android.content.ActivityNotFoundException ex) {
//			 // Potentially direct the user to the Market with a Dialog
//			 Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT).show();
//			 }

				break;
			case R.id.ib_native_picture_bg:

				//图片
//			Intent intent_3Dgallery = new Intent(getActivity(),Grallery3DActivity.class);
//			intent_3Dgallery.putExtra("imageCode", "00");
//			startActivity(intent_3Dgallery);

				Intent intent_image = new Intent(getActivity(), LocalImageActivity.class);
				startActivity(intent_image);

//			Intent intent_picture = new Intent(Intent.ACTION_VIEW);
//			intent_picture.setType("vnd.android.cursor.dir/image");
//			startActivity(intent_picture);

//			Intent intent_picture = new Intent(Intent.ACTION_GET_CONTENT);
//			intent_picture.setType("image/*");
//			startActivity(intent_picture);

				break;

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Fragement03"); //统计页面，"MainScreen"为页面名称，可自定义
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Fragement03");
	}
}
