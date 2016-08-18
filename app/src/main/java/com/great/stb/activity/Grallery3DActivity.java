package com.great.stb.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.great.stb.R;
import com.great.stb.MyView.MyGalleryView;
import com.great.stb.adapter.ImageAdapter;
import com.great.stb.bean.ActDataMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.dao.ElementDAO;
import com.great.stb.util.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Grallery3DActivity extends Activity {

	private TextView tvTitle;
	private MyGalleryView gallery;
	private ImageAdapter adapter;
	private ProgressDialog pd;
	public List<Map<String, Object>> list;
	private ImageView[] mImages;
	private ArrayList<Bitmap> images; // 用来显示的图片数组
	private int dw;
	private int dh;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case 0:
					list = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < images.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("image", images.get(i));
						list.add(map);
					}
					mImages = new ImageView[list.size()];

					initRes();
					pd.dismiss();

					break;

			}

		}
	};
	private ArrayList<ActDataMode> dataModeList;

	private String[] urlStr;
	private List<String> listMusic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_3dgrallery_layout);
		//1. 首先得到屏幕的宽高
		Display currentDisplay = getWindowManager().getDefaultDisplay();
		dw = currentDisplay.getWidth();
		dh = currentDisplay.getHeight();
		final String imageCode = getIntent().getStringExtra("imageCode");

		// ===================
		// 用来显示的图片
		images = new ArrayList<Bitmap>();

		if (imageCode.equals("00")) {
			// listMusic = Util.getImageData(getApplicationContext());
			urlStr = new String[listMusic.size()];
			for (int i = 0; i < listMusic.size(); i++) {
				urlStr[i] = listMusic.get(i);
			}
		} else {
			ElementDAO dao = new ElementDAO(this);
			dataModeList = dao.getAllByWhereActDataMode(imageCode);
			urlStr = new String[dataModeList.size()];
			for (int i = 0; i < dataModeList.size(); i++) {
				urlStr[i] = dataModeList.get(i).getFilePath();
			}
		}

		// ElementDAO dao = new ElementDAO(this);
		// dataModeList = dao.getAllByWhereActDataMode(imageCode);
		// final String[] urlStr = new String[dataModeList.size()];
		//
		// for(int i=0;i<dataModeList.size();i++){
		// urlStr[i]=dataModeList.get(i).getFilePath();
		// }

		// urlStr[0] =
		// "http://pic.itrends.com.cn/media/pic/567_c5602_8d4cf_size640x1136.jpg";
		// urlStr[1] =
		// "http://pic.itrends.com.cn/media/pic/56c_b5804_c8f7b_size270x270.jpg";
		// urlStr[2] =
		// "http://pic.itrends.com.cn/media/pic/56c_b5751_7d85b_size270x270.jpg";
		// urlStr[3] =
		// "http://pic.itrends.com.cn/media/pic/56c_b5751_7d85b_size270x270.jpg";
		// urlStr[4] =
		// "http://pic.itrends.com.cn/media/pic/56c_b5751_7d85b_size270x270.jpg";
		// urlStr[5] =
		// "http://pic.itrends.com.cn/media/pic/56c_b5694_aabdf_size270x270.jpg";
		// urlStr[6] =
		// "http://pic.itrends.com.cn/media/pic/56c_a0830_dd7d5_size270x270.jpg";
		// urlStr[7] =
		// "http://pic.itrends.com.cn/media/pic/56c_9e27a_b787a_size270x270.jpg";

		pd = ProgressDialog.show(Grallery3DActivity.this, "正在加载中", "请稍后...");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.show();

		new Thread() {
			public void run() {

				if (imageCode.equals("00")) {
					for (int i = 0; i < 5; i++) {
						Bitmap bmp = getBmp(i);
						//Bitmap bitmap = BitmapFactory.decodeFile(urlStr[i]);
						images.add(bmp);
					}
				} else {
					for (int i = 0; i < dataModeList.size(); i++) {
						Bitmap bitmap = Util.getImageFromURL2(urlStr[i],dw,dh);
						images.add(bitmap);
					}

					// for (int i = 0; i < dataModeList.size(); i++) {
					// Bitmap bitmap = Util.getImageFromURL(urlStr[i]);
					// images.add(bitmap);
				}

				Message msg = new Message();
				msg.obj = images;
				msg.what = 0;
				handler.sendMessage(msg);
			}
			private Bitmap getBmp(int i) {
				// 2. 获取图片的宽度和高度 
				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				// 设置inJustDecodeBounds = true ;
				bmpFactoryOptions.inJustDecodeBounds = true;
				// 不会真正的解析这个位图 
				Bitmap bmp = BitmapFactory.decodeFile(urlStr[i], bmpFactoryOptions);
				//得到图片的宽度和高度 
				int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)dh);
				int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)dw);
				//真正的解析位图 
				bmpFactoryOptions.inJustDecodeBounds = false;
				if(heightRatio>widthRatio){
					bmpFactoryOptions.inSampleSize = heightRatio;
				}else{
					bmpFactoryOptions.inSampleSize = widthRatio;
				}
				bmp = BitmapFactory.decodeFile(urlStr[i], bmpFactoryOptions);
				return bmp;
			};
		}.start();
	}

	private void initRes() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		gallery = (MyGalleryView) findViewById(R.id.mygallery);

		adapter = new ImageAdapter(getApplicationContext(), list, mImages);
		adapter.createReflectedImages();
		gallery.setAdapter(adapter);

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				// tvTitle.setText(adapter.titles[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		gallery.setOnItemClickListener(new OnItemClickListener() { // 设置点击事件监听
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// Toast.makeText(Grallery3DActivity.this, "img " + (position+1)
				// + " selected", Toast.LENGTH_SHORT).show();
			}
		});
	}
}