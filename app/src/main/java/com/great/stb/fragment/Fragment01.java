package com.great.stb.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.great.stb.R;
import com.great.stb.activity.AdvActivity;
import com.great.stb.bean.ElementMode;
import com.great.stb.dao.ElementDAO;
import com.great.stb.util.SystemAppUtils;
import com.great.stb.util.Util;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment02
 *
 * @author kevin
 *
 */
public class Fragment01 extends Fragment implements OnClickListener,
		OnFocusChangeListener {

	private static final String TAG = "Fragment01";
	private View view;
	private ImageButton ib_adver_big;
	private TextView tv_adver_big;
	private ImageButton ib_adver_small_01;
	private ImageButton ib_adver_small_02;
	private ImageButton ib_adver_small_03;
	private ImageButton ib_adver_small_04;
	private ImageButton ib_adver_small_05;
	private ImageButton ib_adver_small_06;
	private ImageButton ib_adver_small_07;
	private ImageButton ib_adver_small_08;
	private List<ImageButton> images_adver_small;
	private List<ImageButton> images_adver_big;
	private List<Integer> images_adver_small_id;
	private List<ImageButton> images_menu;
	private List<TextView> textviews_menu;
	private ImageButton ib_model_01;
	private ImageButton ib_model_02;
	private ImageButton ib_model_03;
	private ImageButton ib_model_04;
	private ImageButton ib_model_05;
	private TextView tv_menu01;
	private TextView tv_menu02;
	private TextView tv_menu03;
	private TextView tv_menu04;
	private TextView tv_menu05;
	private ProgressDialog pd;

	//日志
	private static final Logger logger = LoggerFactory.getLogger(Fragment.class);

	//数据库获取的
	private List<Bitmap> bitmapsAdver = new ArrayList<Bitmap>();
	private List<Bitmap> bitmapsSmallAdver = new ArrayList<Bitmap>();
	private List<Bitmap> bitmapsMenu = new ArrayList<Bitmap>();
	private List<String> titleAdver = new ArrayList<String>();
	private List<String> titleMenu = new ArrayList<String>();
	private List<String> actionAdver = new ArrayList<String>();
	private List<String> actionMenu = new ArrayList<String>();

	protected static final int BOX_DESKTOP_CONTENT = 1;

	protected static final int BOX_ADVER_SMALL = 4;

	private int location =1;
	private int location_lunbo =1;
	private boolean suspendFlag = false;
	private int adver_samll_arrays[];

	private SharedPreferences sp;
	private Thread adverSmallThread;
	public static String pic_large_path ="";
	/*保存预受理工单图片*/
	private ArrayList<String> urlString = null;
	/**
	 * 消息处理器
	 */
	private Handler handler = new Handler() {


		private int i;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

				case BOX_DESKTOP_CONTENT:

					ib_adver_big.setImageBitmap(bitmapsAdver.get(0));
					tv_adver_big.setText(titleAdver.get(0));
//				adverGrayBG(1);
					location=1;
					for (int i = 0; i < images_adver_small.size(); i++) {
						images_adver_small.get(i).setImageBitmap(bitmapsSmallAdver.get(i));
					}

					for(int i=0; i<5;i++){
						images_menu.get(i).setImageBitmap(bitmapsMenu.get(i));
						textviews_menu.get(i).setText(titleMenu.get(i));

					}
					adverGrayBG(-1);
					adverSmallThread = new Thread(new adverSmalldynamic());
					adverSmallThread.start();

					break;


				case BOX_ADVER_SMALL:
					//TODO
					if(!suspendFlag){

						location_lunbo = location_lunbo%8;
						location = location_lunbo;
						if(location_lunbo==0){
							location_lunbo=8;
							ib_adver_big.setImageBitmap(bitmapsAdver.get(7));
							tv_adver_big.setText(titleAdver.get(7));
							adverGrayBG(8);
							ib_adver_big.setNextFocusDownId(adver_samll_arrays[7]);
							location=1;
						}else{
							ib_adver_big.setImageBitmap(bitmapsAdver.get(location_lunbo-1));
							tv_adver_big.setText(titleAdver.get(location_lunbo-1));
							adverGrayBG(location_lunbo);
							ib_adver_big.setNextFocusDownId(adver_samll_arrays[location_lunbo-1]);
						}
						i = location_lunbo-1;
						location_lunbo++;
					}
//            	Toast.makeText(getActivity(), "&&:"+suspendFlag+"&&:"+i+"&&:"+location, Toast.LENGTH_LONG).show();

					break;

			}
		}

	};



	private class adverSmalldynamic implements Runnable {
		@Override
		public void run() {
			try {
				while(true){
					if (!suspendFlag) {
						Thread.sleep(2000);
						Message msg = new Message();
						msg.what = BOX_ADVER_SMALL;
						handler.sendMessage(msg);
					}

				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment01, null);


		boolean wifiFlage = getActivity().getIntent().getBooleanExtra("WIFI", false);
		if(wifiFlage){
			initData();
			desktopContentBoxFromDB();
			//防止页面显示默认图片
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			initNOWIFIData();
		}
		return view;

	}

	private void initNOWIFIData() {
		ib_adver_big = (ImageButton) view.findViewById(R.id.ib_adver_big);
		tv_adver_big = (TextView) view.findViewById(R.id.tv_adver_big);
		ib_adver_small_01 = (ImageButton) view.findViewById(R.id.ib_adver_small_01);
		ib_adver_small_02 = (ImageButton) view.findViewById(R.id.ib_adver_small_02);
		ib_adver_small_03 = (ImageButton) view.findViewById(R.id.ib_adver_small_03);
		ib_adver_small_04 = (ImageButton) view.findViewById(R.id.ib_adver_small_04);
		ib_adver_small_05 = (ImageButton) view.findViewById(R.id.ib_adver_small_05);
		ib_adver_small_06 = (ImageButton) view.findViewById(R.id.ib_adver_small_06);
		ib_adver_small_07 = (ImageButton) view.findViewById(R.id.ib_adver_small_07);
		ib_adver_small_08 = (ImageButton) view.findViewById(R.id.ib_adver_small_08);

		ib_model_01 = (ImageButton) view.findViewById(R.id.ib_model_01);
		ib_model_02 = (ImageButton) view.findViewById(R.id.ib_model_02);
		ib_model_03 = (ImageButton) view.findViewById(R.id.ib_model_03);
		ib_model_04 = (ImageButton) view.findViewById(R.id.ib_model_04);
		ib_model_05 = (ImageButton) view.findViewById(R.id.ib_model_05);


		tv_menu01 = (TextView) view.findViewById(R.id.tv_menu01);
		tv_menu02 = (TextView) view.findViewById(R.id.tv_menu02);
		tv_menu03 = (TextView) view.findViewById(R.id.tv_menu03);
		tv_menu04 = (TextView) view.findViewById(R.id.tv_menu04);
		tv_menu05 = (TextView) view.findViewById(R.id.tv_menu05);

		ib_adver_big.setImageResource(R.drawable.nowifi_image_main);
		ib_adver_small_01.setImageResource(R.drawable.nowifi_image_small);
		ib_adver_small_02.setImageResource(R.drawable.nowifi_image_small);
		ib_adver_small_03.setImageResource(R.drawable.nowifi_image_small);
		ib_adver_small_04.setImageResource(R.drawable.nowifi_image_small);
		ib_adver_small_05.setImageResource(R.drawable.nowifi_image_small);
		ib_adver_small_06.setImageResource(R.drawable.nowifi_image_small);
		ib_adver_small_07.setImageResource(R.drawable.nowifi_image_small);
		ib_adver_small_08.setImageResource(R.drawable.nowifi_image_small);

		ib_model_01.setImageResource(R.drawable.nowifi_image_menu1);
		ib_model_02.setImageResource(R.drawable.nowifi_image_menu1);
		ib_model_03.setImageResource(R.drawable.nowifi_image_menu1);
		ib_model_04.setImageResource(R.drawable.nowifi_image_menu1);
		ib_model_05.setImageResource(R.drawable.nowifi_image_menu2);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	private void initData() {
		images_adver_small = new ArrayList<ImageButton>(); // 小广告图片ImageButton
		images_adver_big=new ArrayList<ImageButton>(); 		//大广告图片
		images_adver_small_id = new ArrayList<Integer>(); // 小广告id
		images_menu = new ArrayList<ImageButton>(); // 菜单图片
		textviews_menu = new ArrayList<TextView>(); // 菜单图片


		//TODO
//		images_adver_small_id.add(R.drawable.adver_small_01);
//		images_adver_small_id.add(R.drawable.adver_small_02);
//		images_adver_small_id.add(R.drawable.adver_small_03);
//		images_adver_small_id.add(R.drawable.adver_small_04);
//		images_adver_small_id.add(R.drawable.adver_small_05);
//		images_adver_small_id.add(R.drawable.adver_small_06);
//		images_adver_small_id.add(R.drawable.adver_small_07);
//		images_adver_small_id.add(R.drawable.adver_small_08);

		ib_adver_big = (ImageButton) view.findViewById(R.id.ib_adver_big);
		tv_adver_big = (TextView) view.findViewById(R.id.tv_adver_big);
		adver_samll_arrays = new int[] {R.id.ib_adver_small_01,R.id.ib_adver_small_02,R.id.ib_adver_small_03,R.id.ib_adver_small_04,R.id.ib_adver_small_05,R.id.ib_adver_small_06,R.id.ib_adver_small_07,R.id.ib_adver_small_08};
		ib_adver_small_01 = (ImageButton) view.findViewById(R.id.ib_adver_small_01);
		ib_adver_small_02 = (ImageButton) view.findViewById(R.id.ib_adver_small_02);
		ib_adver_small_03 = (ImageButton) view.findViewById(R.id.ib_adver_small_03);
		ib_adver_small_04 = (ImageButton) view.findViewById(R.id.ib_adver_small_04);
		ib_adver_small_05 = (ImageButton) view.findViewById(R.id.ib_adver_small_05);
		ib_adver_small_06 = (ImageButton) view.findViewById(R.id.ib_adver_small_06);
		ib_adver_small_07 = (ImageButton) view.findViewById(R.id.ib_adver_small_07);
		ib_adver_small_08 = (ImageButton) view.findViewById(R.id.ib_adver_small_08);

		images_adver_small.add(ib_adver_small_01);
		images_adver_small.add(ib_adver_small_02);
		images_adver_small.add(ib_adver_small_03);
		images_adver_small.add(ib_adver_small_04);
		images_adver_small.add(ib_adver_small_05);
		images_adver_small.add(ib_adver_small_06);
		images_adver_small.add(ib_adver_small_07);
		images_adver_small.add(ib_adver_small_08);

		ib_model_01 = (ImageButton) view.findViewById(R.id.ib_model_01);
		ib_model_02 = (ImageButton) view.findViewById(R.id.ib_model_02);
		ib_model_03 = (ImageButton) view.findViewById(R.id.ib_model_03);
		ib_model_04 = (ImageButton) view.findViewById(R.id.ib_model_04);
		ib_model_05 = (ImageButton) view.findViewById(R.id.ib_model_05);

		tv_menu01 = (TextView) view.findViewById(R.id.tv_menu01);
		tv_menu02 = (TextView) view.findViewById(R.id.tv_menu02);
		tv_menu03 = (TextView) view.findViewById(R.id.tv_menu03);
		tv_menu04 = (TextView) view.findViewById(R.id.tv_menu04);
		tv_menu05 = (TextView) view.findViewById(R.id.tv_menu05);

		images_menu.add(ib_model_01);
		images_menu.add(ib_model_02);
		images_menu.add(ib_model_03);
		images_menu.add(ib_model_04);
		images_menu.add(ib_model_05);

		textviews_menu.add(tv_menu01);
		textviews_menu.add(tv_menu02);
		textviews_menu.add(tv_menu03);
		textviews_menu.add(tv_menu04);
		textviews_menu.add(tv_menu05);

		// 点击事件监听
		ib_adver_big.setOnClickListener(this);
		ib_adver_small_01.setOnClickListener(this);
		ib_adver_small_02.setOnClickListener(this);
		ib_adver_small_03.setOnClickListener(this);
		ib_adver_small_04.setOnClickListener(this);
		ib_adver_small_05.setOnClickListener(this);
		ib_adver_small_06.setOnClickListener(this);
		ib_adver_small_07.setOnClickListener(this);
		ib_adver_small_08.setOnClickListener(this);

		// model 点击事件监听
		ib_model_01.setOnClickListener(this);
		ib_model_02.setOnClickListener(this);
		ib_model_03.setOnClickListener(this);
		ib_model_04.setOnClickListener(this);
		ib_model_05.setOnClickListener(this);

		// 焦点事件监听
		ib_adver_small_01.setOnFocusChangeListener(this);
		ib_adver_small_02.setOnFocusChangeListener(this);
		ib_adver_small_03.setOnFocusChangeListener(this);
		ib_adver_small_04.setOnFocusChangeListener(this);
		ib_adver_small_05.setOnFocusChangeListener(this);
		ib_adver_small_06.setOnFocusChangeListener(this);
		ib_adver_small_07.setOnFocusChangeListener(this);
		ib_adver_small_08.setOnFocusChangeListener(this);
		ib_adver_big.setOnFocusChangeListener(this);
		ib_model_01.setOnFocusChangeListener(this);
		ib_model_02.setOnFocusChangeListener(this);
		ib_model_03.setOnFocusChangeListener(this);
		ib_model_04.setOnFocusChangeListener(this);
		ib_model_05.setOnFocusChangeListener(this);
	}

	/**
	 * 广告位置变灰
	 *
	 * @param position
	 */
	private void adverGrayBG(int position) {
		for (int i = 0; i < images_adver_small.size(); i++) {
			Bitmap bitmap = bitmapsSmallAdver.get(i);
			Drawable drawable = new BitmapDrawable(bitmap);
			//Drawable drawable = getResources().getDrawable(images_adver_small_id.get(i));
			drawable.setColorFilter(Color.DKGRAY, Mode.MULTIPLY);
			images_adver_small.get(i).setImageDrawable(drawable);
		}

		if (position > 0) {
			Bitmap bitmap = bitmapsSmallAdver.get(position-1);
			Drawable drawable = new BitmapDrawable(bitmap);
			//Drawable drawable = getResources().getDrawable(images_adver_small_id.get(position - 1));
			drawable.clearColorFilter();
			images_adver_small.get(position-1).setImageDrawable(drawable);
		}
	}

	/**
	 * 点击事件处理
	 */
	@Override
	public void onClick(View v) {
		if(!Util.detect(getActivity())){
			Toast.makeText(getActivity(), "无网络，请检查网络设备！", Toast.LENGTH_LONG).show();
			return;
		}
		switch (v.getId()) {
			case R.id.ib_adver_big:
				if(location==1){
					String actType = actionAdver.get(0);
					actDataAction(actType, "D10");
				}else if(location==2){
					String actType = actionAdver.get(1);
					actDataAction(actType, "D20");
				}else if(location==3){
					String actType = actionAdver.get(2);
					actDataAction(actType, "D30");
				}else if(location==4){
					String actType = actionAdver.get(3);
					actDataAction(actType, "D40");
				}else if(location==5){
					String actType = actionAdver.get(4);
					actDataAction(actType, "D50");
				}else if(location==6){
					String actType = actionAdver.get(5);
					actDataAction(actType, "D60");
				}else if(location==7){
					String actType = actionAdver.get(6);
					actDataAction(actType, "D70");
				}else if(location==8){
					String actType = actionAdver.get(7);
					actDataAction(actType, "D80");
				}

//			Intent intent = new Intent(getActivity(), VideoActivity.class);
//			startActivity(intent);
//			ib_adver_big.setImageResource(R.drawable.adver_big);
				break;

			case R.id.ib_adver_small_01:
				actDataAction(actionAdver.get(0), "D10");
				break;
			case R.id.ib_adver_small_02:
				actDataAction(actionAdver.get(1), "D20");
				break;
			case R.id.ib_adver_small_03:
				actDataAction(actionAdver.get(2), "D30");
				break;
			case R.id.ib_adver_small_04:
				actDataAction(actionAdver.get(3), "D40");
				break;
			case R.id.ib_adver_small_05:
				actDataAction(actionAdver.get(4), "D50");
				break;
			case R.id.ib_adver_small_06:
				actDataAction(actionAdver.get(5), "D60");
				break;
			case R.id.ib_adver_small_07:
				actDataAction(actionAdver.get(6), "D70");
				break;
			case R.id.ib_adver_small_08:
				actDataAction(actionAdver.get(7), "D80");
				break;
			case R.id.ib_model_01:

				String actType01 = actionMenu.get(0);
				actDataAction(actType01, "E10");

//			Intent intent_model01 = new Intent();
//			ComponentName component01 = new ComponentName("com.great.stb.order", "com.great.stb.order.MainActivity");
//			intent_model01.setComponent(component01);
//			startActivity(intent_model01);
				break;
			case R.id.ib_model_02:
				String actType02 = actionMenu.get(1);
				actDataAction(actType02, "E20");
//			Intent intent_3Dgallery = new Intent(getActivity(),Grallery3DActivity.class);
//			startActivity(intent_3Dgallery);

				break;
			case R.id.ib_model_03:

				String actType03 = actionMenu.get(2);
				actDataAction(actType03, "E30");

//			Intent intent00 = new Intent(getActivity(), ActiveActivity.class);
//			startActivity(intent00);

				break;
			case R.id.ib_model_04:

				String actType04 = actionMenu.get(3);
				actDataAction(actType04, "E40");

//				String actType = actionAdver.get(2);
//				actDataAction("20", "D20");

				break;
			case R.id.ib_model_05:
//			String prictureStrTime = "0";
//			long timeMillis = System.currentTimeMillis();
//			prictureStrTime = DateFormat.format("yyyy-MM-dd", timeMillis).toString()+" ";
//			prictureStrTime += DateFormat.format("kk:mm:ss", timeMillis).toString();
//			logger.debug("#"+prictureStrTime+","+"E50"+","+"menu");
//			Intent intent_model05 = new Intent();
//			ComponentName component05 = new ComponentName("com.great.stb.enterprise","com.great.stb.enterprise.SplashActivity");
//			intent_model05.setComponent(component05);
//			startActivity(intent_model05);

				String actType05 = actionMenu.get(4);
				actDataAction(actType05, "E50");

				break;

		}
	}

	/**
	 * 获取焦点事件处理
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		//TODO
		switch (v.getId()) {
			case R.id.ib_adver_big:
				if(hasFocus){
					suspendFlag =true;
				}else{
					suspendFlag =false;
				}
				break;

			case R.id.ib_adver_small_01:
				if (hasFocus) {
					suspendFlag =true;
					ib_adver_big.setImageBitmap(bitmapsAdver.get(0));
					ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_01);
					tv_adver_big.setText(titleAdver.get(0));
					adverGrayBG(1);
					location=1;
				}

				break;
			case R.id.ib_adver_small_02:
				if (hasFocus) {
					try {
						suspendFlag =true;
						ib_adver_big.setImageBitmap(bitmapsAdver.get(1));
						ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_02);
						tv_adver_big.setText(titleAdver.get(1));
						adverGrayBG(2);
						location=2;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case R.id.ib_adver_small_03:
				if (hasFocus) {
					suspendFlag =true;
					ib_adver_big.setImageBitmap(bitmapsAdver.get(2));
					ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_03);
					tv_adver_big.setText(titleAdver.get(2));
					adverGrayBG(3);
					location=3;
				}
				break;
			case R.id.ib_adver_small_04:
				if (hasFocus) {
					suspendFlag =true;
					ib_adver_big.setImageBitmap(bitmapsAdver.get(3));
					ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_04);
					tv_adver_big.setText(titleAdver.get(3));
					adverGrayBG(4);
					location=4;
				}
				break;
			case R.id.ib_adver_small_05:
				if (hasFocus) {
					suspendFlag =true;
					ib_adver_big.setImageBitmap(bitmapsAdver.get(4));
					ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_05);
					tv_adver_big.setText(titleAdver.get(4));
					adverGrayBG(5);
					location=5;
				}
				break;
			case R.id.ib_adver_small_06:
				if (hasFocus) {
					suspendFlag =true;
					ib_adver_big.setImageBitmap(bitmapsAdver.get(5));
					ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_06);
					tv_adver_big.setText(titleAdver.get(5));
					adverGrayBG(6);
					location=6;
				}
				break;
			case R.id.ib_adver_small_07:
				if (hasFocus) {
					suspendFlag =true;
					ib_adver_big.setImageBitmap(bitmapsAdver.get(6));
					ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_07);
					tv_adver_big.setText(titleAdver.get(6));
					adverGrayBG(7);
					location=7;
				}
				break;
			case R.id.ib_adver_small_08:
				if (hasFocus) {
					suspendFlag =true;
					ib_adver_big.setImageBitmap(bitmapsAdver.get(7));
					ib_adver_big.setNextFocusDownId(R.id.ib_adver_small_08);
					tv_adver_big.setText(titleAdver.get(7));
					adverGrayBG(8);
					location=8;
				} else {
					//adverGrayBG(-1);
					suspendFlag =false;
				}
				break;
		}
	}

	public void desktopContentBoxFromDB() {
		new Thread() {
			public void run() {
				ElementDAO dao = new ElementDAO(getActivity());
				List<ElementMode> elements = dao.getDeskContentElementMode();

				String SDCarePath=Environment.getExternalStorageDirectory().toString();
				for (int i = 0; i < elements.size(); i++) {
					if (elements.get(i).getCode().contains("E")) {
						// 图片
						String filePath=SDCarePath+"/myImage/"+"menuBigImage" + elements.get(i).getCode()+".png";
						Bitmap bitmap=BitmapFactory.decodeFile(filePath);
						if(bitmap == null) {
							bitmap = Util.getImageFromURL(elements.get(i).getImage());
							if (bitmap != null) {
								Util.saveImage2SD(bitmap, "menuBigImage" + elements.get(i).getCode());
							}
						}
						bitmapsMenu.add(bitmap);
						// 标题
						String title = 	elements.get(i).getTitle();
						titleMenu.add(title);
						// 打开方式 
						String actType = elements.get(i).getActType();
						actionMenu.add(actType);
					} else if (elements.get(i).getCode().contains("D")) {

						String filePath=SDCarePath+"/myImage/"+"adverBigImage" + elements.get(i).getCode()+".png";
						String filePathSmall=SDCarePath+"/myImage/"+"menuSmallImage" + elements.get(i).getCode()+".png";
						Bitmap bitmap=BitmapFactory.decodeFile(filePath);
						Bitmap bitmapSmall=BitmapFactory.decodeFile(filePathSmall);
						if(bitmap == null) {
							bitmap = Util.getImageFromURL(elements.get(i).getImage());
							if (bitmap != null) {
								Util.saveImage2SD(bitmap, "adverBigImage" + elements.get(i).getCode());
							}
						}
						if(bitmapSmall == null) {
							bitmapSmall = Util.getImageFromURL(elements.get(i)
									.getSmallImage());
							if (bitmapSmall != null) {
								Util.saveImage2SD(bitmapSmall,"menuSmallImage" + elements.get(i).getCode());
							}
						}
						bitmapsAdver.add(bitmap);
						String title = elements.get(i).getTitle();
						titleAdver.add(title);
						String actType = elements.get(i).getActType();
						actionAdver.add(actType);

						bitmapsSmallAdver.add(bitmapSmall);

					}
				}
				int sizeAdver = 8 - bitmapsAdver.size();
				int sizeSmallAdver = 8 - bitmapsSmallAdver.size();
				int sizeTitleAdver = 8 - titleAdver.size();
				int sizeMenu = 5-bitmapsMenu.size();
				int sizeTitleMenu = 5-titleMenu.size();

				for (int i = 0; i < sizeAdver; i++) {
					bitmapsAdver.add(null);
				}

				for (int i = 0; i < sizeSmallAdver; i++) {
					bitmapsSmallAdver.add(null);
				}

				for (int i = 0; i < sizeTitleAdver; i++) {
					titleAdver.add(null);
				}

				for (int i = 0; i < sizeMenu; i++) {
					bitmapsMenu.add(null);
				}

				for(int i=0;i<sizeTitleMenu;i++){
					titleMenu.add(null);
				}

				Message msg = new Message();
				msg.what = BOX_DESKTOP_CONTENT;
				handler.sendMessage(msg);

			};

		}.start();

	}

	public void actDataAction(String actType,String code){
		if("40".equals(actType)){
			//是应用，判断是否有该应用
			ElementDAO dao = new ElementDAO(getActivity());
			ElementMode elementMode = dao.byCodeElementMode(code);
			String appName = elementMode.getAppName();
			String filePath = dao.byCodeElementMode(code).getFilePath();
			String[] strings = appName.split(",");
			if (!SystemAppUtils.checkPackage(getActivity(),strings[0])) {
				downLoadApk(filePath);
				return;
			}
		}
		Intent intent = new Intent(getActivity(),AdvActivity.class);
		Bundle b = new Bundle();
		b.putString("actType", actType);
		b.putString("code", code);
		intent.putExtra("param", b);
		startActivity(intent);
	}

	/**
	 * 下载apk文件
	 */
	protected void downLoadApk(String urlAPK) {
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setTitle("正在下载");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		FinalHttp fh = new FinalHttp();
		// 调用download方法开始下载
		//fh.download(updateInfo.getApkurl(), "/sdcard/temp.apk",
		fh.download(urlAPK, "/sdcard/temp.apk",
				new AjaxCallBack<File>() {

					@Override
					public void onSuccess(File t) {
						pd.dismiss();
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setDataAndType(Uri.fromFile(t),"application/vnd.android.package-archive");
						startActivity(intent);
						//getActivity().finish();						
						super.onSuccess(t);
					}

					@Override
					public void onLoading(long count, long current) {
						pd.setMax((int) count);
						pd.setProgress((int) (current));
						super.onLoading(count, current);
					}

				});

	}
	
	/*public boolean checkPackVersion() throws RuntimeException{
		boolean param = false;
		try {
			List<PackageInfo> info =  getActivity().getPackageManager().getInstalledPackages(0);
			for(int i=0;i<info.size();i++) {
				if(info.get(i).packageName.equals("cn.cibntv.ott")) {
					if(!info.get(i).versionName.equals("EPG-GXTT(8850)-VER3.5.101-20140715-F")) {
						param = true;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return param;
	}*/

}
