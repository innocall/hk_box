package com.great.stb.activity;


import java.util.List;

import com.great.stb.R;
import com.great.stb.adapter.LocalImageAdapter;
import com.great.stb.adapter.MusicAdapter;
import com.great.stb.adapter.VideoAdapter;
import com.great.stb.bean.ImagesSD;
import com.great.stb.bean.Music;
import com.great.stb.util.Util;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LocalVideoActivity extends Activity {
	private ListView listView;
	private TextView tv_local_video_no;
	private ProgressDialog pd;
	private List<ImagesSD> listMusic;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pd.dismiss();
			if (listMusic==null||listMusic.size()==0) {
				listView.setVisibility(View.GONE);
				tv_local_video_no.setVisibility(View.VISIBLE);
			}
			LocalImageAdapter adapter=new LocalImageAdapter(LocalVideoActivity.this, listMusic);
			//VideoAdapter adapter=new VideoAdapter(LocalVideoActivity.this, listMusic);
			listView.setAdapter(adapter);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("LocalVideoActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("LocalVideoActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_video);
		pd = new ProgressDialog(this);
		initData();
	}

	private void initData() {

		listView= (ListView) this.findViewById(R.id.listAllMusic);
		tv_local_video_no = (TextView) this.findViewById(R.id.tv_local_video_no);
		pd.show();
		new Thread() {
			public void run() {
				Looper.prepare();
				listMusic = Util.getUShostVideoData(getApplicationContext());
				handler.sendEmptyMessage(0);
				Looper.loop();
			};
		}.start();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				//视频播放器播放
				String url = listMusic.get(position).getUrl();
//				Toast.makeText(getApplicationContext(),url , Toast.LENGTH_LONG).show();
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW,uri);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setDataAndType(uri , "video/*");
				startActivity(intent);


			}
		});



	}




}
