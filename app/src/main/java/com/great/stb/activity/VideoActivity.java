package com.great.stb.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.great.stb.R;
import com.great.stb.bean.ActDataMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.dao.ElementDAO;
import com.great.stb.fragment.Fragment01;
//import com.great.stb.fragment.Fragment01.adverSmalldynamic;
import com.great.stb.util.Util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoActivity extends Activity {

	private Button bt_video_play;
	private TextView tv_video_title_01;
	private TextView tv_video_title02;
	private TextView tv_video_info;
	private ImageView iv_video_image;
	private ArrayList<ActDataMode> list;
	private Bitmap bitmap;
	protected static final int BOX_DESKTOP_CONTENT = 1;
	protected static final int BOX_ORDER_IMAGES = 2;
	protected static final int BOX_VIDEO_IMAGES = 3;
	protected static final int BOX_ADVER_SMALL = 4;
	public static String pic_large_path ="";
	private int dw,dh;
	/**
	 * 消息处理器
	 */
	private Handler handler = new Handler() {

		private int i;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

				case BOX_VIDEO_IMAGES:
//				pd.dismiss();
					iv_video_image.setImageBitmap(bitmap);
					break;
			}
		}

	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		dw = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		dh = getWindow().getWindowManager().getDefaultDisplay().getHeight();
		Intent intent = getIntent();
		pic_large_path = intent.getStringExtra("pic_large_path");
		//Toast.makeText(getApplicationContext(), pic_large_path, 3000).show();
		videoImagesLoads(pic_large_path);
		initData();
		initListener();
	}

	private void initData() {

		iv_video_image = (ImageView) findViewById(R.id.iv_video_image);
		bt_video_play = (Button) findViewById(R.id.bt_video_play);

		bt_video_play.setNextFocusDownId(View.NO_ID);
		bt_video_play.setNextFocusLeftId(View.NO_ID);
		bt_video_play.setNextFocusUpId(View.NO_ID);
		bt_video_play.setNextFocusRightId(View.NO_ID);

		tv_video_title_01 = (TextView) findViewById(R.id.tv_video_title_01);
		tv_video_title02 = (TextView) findViewById(R.id.tv_video_title02);
		tv_video_info = (TextView) findViewById(R.id.tv_video_info);

		ElementDAO dao = new ElementDAO(this);
		ElementMode elementMode = dao.byCodeElementMode(AdvActivity.codeVideo);
		String txt_intro = elementMode.getTxt_intro();
		String title = elementMode.getTitle();

		list = dao.getAllByWhereActDataMode(AdvActivity.codeVideo);


		tv_video_title_01.setText(title);
		tv_video_title02.setText(title);
		tv_video_info.setText(txt_intro);


		// by shenhb
//		String SDCarePath=Environment.getExternalStorageDirectory().toString();
//		String filePath=SDCarePath+"/myImage/"+"videoImage" +".png"; 
//		Bitmap bitmap=BitmapFactory.decodeFile(filePath); 
//		iv_video_image.setImageBitmap(bitmap);

	}

	private void initListener() {

		bt_video_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list.size()<2) {
					String filePath = list.get(0).getFilePath();
					Util.getVideoFromURL(VideoActivity.this, filePath);

				} else {
					Intent intent = new Intent(getApplicationContext(), VideoPlayActivity.class);
					startActivity(intent);
				}



			}
		});
		
/*		ll_video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ll_video.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					tv_video_01.setTextColor(Color.parseColor("#ff0000"));
					tv_video_02.setTextColor(Color.parseColor("#ff0000"));
				} else {
					tv_video_01.setTextColor(Color.parseColor("#ffffff"));
					tv_video_02.setTextColor(Color.parseColor("#ffffff"));
				}

			}
		});*/
	}

	// 获取视频首页图片
	public void videoImagesLoads(final String pic_large_path) {
		new Thread() {
			public void run() {
				bitmap = Util.getImageFromURL2(pic_large_path,dw,dh);

				Message msg = new Message();
				msg.what = BOX_VIDEO_IMAGES;
				handler.sendMessage(msg);
			};
		}.start();
	}

}
