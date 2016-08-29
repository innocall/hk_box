package com.great.stb.activity;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.great.stb.R;
import com.great.stb.bean.ActDataMode;
import com.great.stb.dao.ElementDAO;
import com.great.stb.util.AsyncImageLoader;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery.LayoutParams;

public class WatchPicActivity extends Activity implements
		AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {

	// private ImageSwitcher mSwitcher;
	private ArrayList<Bitmap> images; // 用来显示的图片数组
	private ArrayList<ActDataMode> dataModeList;
	int dw, dh;
	private String[] urlStr;
	private AnimationSet mAnimationSet;
	private LayoutInflater inlater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_watch);
		inlater = LayoutInflater.from(getApplicationContext());
		// 1. 首先得到屏幕的宽高
		Display currentDisplay = getWindowManager().getDefaultDisplay();
		dw = currentDisplay.getWidth();
		dh = currentDisplay.getHeight();
		final String imageCode = getIntent().getStringExtra("imageCode");
		// ===================
		// 用来显示的图片
		ElementDAO dao = new ElementDAO(this);
		dataModeList = dao.getAllByWhereActDataMode(imageCode);
		urlStr = new String[dataModeList.size()];
		for (int i = 0; i < dataModeList.size(); i++) {
			urlStr[i] = dataModeList.get(i).getFilePath();
		}
		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(this);
		g.setOnItemClickListener(new OnItemClickListener() { // 设置点击事件监听
			@Override
			public void onItemClick(AdapterView<?> parent, final View v,
									final int position, long id) {
				// 增加点击放大效果
				loadWatchByUrl(urlStr[position]);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WatchPicActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WatchPicActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

	private void loadWatchByUrl(String string) {
		Intent intent = new Intent(WatchPicActivity.this,ImageWatchActivity.class);
		intent.putExtra("url", string);
		startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
							   long arg3) {
		// mSwitcher.setImageResource(mImageIds[arg2]);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	private AsyncImageLoader asyncImageLoader;

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
			asyncImageLoader = new AsyncImageLoader(mContext, dw, dh);
		}

		public int getCount() {
			return urlStr.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			String url = urlStr[position];
			/*LinearLayout llayout = new LinearLayout(mContext);
		    llayout.setOrientation(LinearLayout.VERTICAL); // 设置线性布局的排列方式
		    llayout.setGravity(Gravity.CENTER_HORIZONTAL);
		    llayout.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			TextView tv = new TextView(mContext);
			tv.setPadding(0, 30, 0, 30);
			tv.setText((position + 1) + "/" + urlStr.length);
			tv.setTextColor(Color.WHITE);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			//tv.setTextSize(R.dimen.dim_30_dp);
			tv.setTextSize(16);
			llayout.addView(tv);*/

			ImageView i = new ImageView(mContext);
			int height = (int) (0.8 * dh);
			int width = (int)(0.8 * dw);
			i.setMaxHeight(height);
			i.setMaxWidth(width);
			i.setAdjustViewBounds(true);
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			asyncImageLoader.DisplayImage(url, i);
			//llayout.addView(i);
			return i;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果按下的是返回键，并且没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
			return false;
		}
		return false;
	}
}
