package com.great.stb.preorder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.great.stb.R;
import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.ItemDataMode;
import com.great.stb.bean.OrderInfoMode;
import com.great.stb.bean.UpdateResponse;
import com.great.stb.bean.WorkOrdersStateResponse;
import com.great.stb.common.UpdateRequest;
import com.great.stb.util.AsyncImageLoader;

public class PreorderActivity extends Activity implements OnClickListener {

	private static final int BOX_DESKTOP_CONTENT = 1;
	private Button bt_last;
	private Button bt_next;
	//private ArrayList<Bitmap> bitmaps; // 用来显示的图片数组
	private int flag = 0;
	private String mediaId = "";
	private ImageView iv_order_image;
	private View ll_order_phone_number;
	private View ll_order_success;
	private View ll_order_image;
	private EditText et_order_phone_number;
	private String result;
	private UpdateResponse req;
	private String[] url = new String[3];
	private SharedPreferences sp;
	private AsyncImageLoader asy;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// super.handleMessage(msg);
			// 切换viewPager当前页面
			switch (msg.what) {
				case 0:
					//iv_order_image.setImageBitmap(bitmaps.get(0));
					asy.DisplayImage(url[0], iv_order_image);
					break;
				case BOX_DESKTOP_CONTENT:
					if (result.equals("0") ) {
						ll_order_phone_number.setVisibility(View.GONE);
						ll_order_success.setVisibility(View.VISIBLE);
						bt_last.setVisibility(View.GONE);
						bt_next.setText("确定");
						flag++;
					} else {
						finish();
						Toast.makeText(PreorderActivity.this, "预订失败，请重新预订",Toast.LENGTH_LONG).show();
					}
					break;
			}
		}
	};
	private String boxurl;
	private String macid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preorder_main);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		asy = new AsyncImageLoader(getApplicationContext(), 480, 320);
		initData();
	}

	private void initData() {

		boxurl = getString(R.string.boxurl);
//		macid = Util.getLocalMacAddress(getApplicationContext());
		macid = getWifiMacAddress();

		TextView tv_order_title = (TextView) findViewById(R.id.tv_order_title);
		String orderTitle = sp.getString("orderTitle", "");
		tv_order_title.setText(orderTitle);
		mediaId = sp.getString("mediaId", "");
		Bundle bu = getIntent().getExtras();
		try {
			//String orderTitle = (String) getIntent().getExtras().get("orderTitle");
			if(bu != null) {
				//String orderTitle = bu.getString("orderTitle");
				//mediaId = bu.getString("mediaId");
				//tv_order_title.setText(orderTitle);
				ArrayList<String> alist = bu.getStringArrayList("url");
				for(int i=0; i<alist.size();i++) {
					url[i] = alist.get(i);
					//Log.i("tag",url[i] + "------------------------------");
				}
			}
		} catch (Exception e) {
		}
		//String[] urlStr = getIntent().getStringArrayExtra("url");
		//url = getIntent().getExtras().getStringArray("url");
		iv_order_image = (ImageView) findViewById(R.id.iv_order_image);
		ll_order_image = findViewById(R.id.ll_order_image);
		ll_order_phone_number = findViewById(R.id.ll_order_phone_number);
		ll_order_success = findViewById(R.id.ll_order_success);
		et_order_phone_number = (EditText) findViewById(R.id.et_order_phone_number);

		bt_last = (Button) findViewById(R.id.bt_last);
		bt_last.setVisibility(View.INVISIBLE);
		bt_next = (Button) findViewById(R.id.bt_next);

		bt_last.setOnClickListener(this);
		bt_next.setOnClickListener(this);

		//bitmaps = new ArrayList<Bitmap>();
		//final String[] urlStr = new String[3];
		//String SDCarePath=Environment.getExternalStorageDirectory().toString();
		//String filePath=SDCarePath+"/myImage/"+"orderImage" + "0"+".png";
		/*urlStr[0] = SDCarePath+"/myImage/"+"orderImage" + "0"+".png";
		urlStr[1] = SDCarePath+"/myImage/"+"orderImage" + "1"+".png";
		urlStr[2] = SDCarePath+"/myImage/"+"orderImage" + "2"+".png";*/
		
	/*	for (int i = 0; i < 3; i++) {
			//Bitmap bitmap=BitmapFactory.decodeFile(urlStr[i]); 				
			//bitmaps.add(bitmap);
			url[i] = urlStr[i];
		}*/


		new Thread() {
			public void run() {
//				for (int i = 0; i < 3; i++) {
//					Bitmap bitmap=BitmapFactory.decodeFile(urlStr[i]); 				
//					bitmaps.add(bitmap);
//				}
				Message msg = new Message();
				msg.obj = url;
				msg.what = 0;
				handler.sendMessage(msg);
			};
		}.start();

	}

	// 手机号码的正则判断
	private boolean isLegalPhoneNumber(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		//System.out.println(m.matches() + "---");
		return m.matches();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_last:

				if (flag == 3) {
					ll_order_image.setVisibility(View.VISIBLE);
					ll_order_phone_number.setVisibility(View.GONE);
					//iv_order_image.setImageBitmap(bitmaps.get(2));
					asy.DisplayImage(url[2], iv_order_image);
					bt_next.setText("预定");
					flag--;
				} else if (flag == 2) {
					//iv_order_image.setImageBitmap(bitmaps.get(1));
					asy.DisplayImage(url[1], iv_order_image);
					bt_next.setText("下一步");
					flag--;
				} else if (flag == 1) {
					//iv_order_image.setImageBitmap(bitmaps.get(0));
					asy.DisplayImage(url[0], iv_order_image);
					flag--;
					bt_last.setVisibility(View.INVISIBLE);
				}
				break;
			case R.id.bt_next:
				if (flag == 0) {
					//iv_order_image.setImageBitmap(bitmaps.get(1));
					asy.DisplayImage(url[1], iv_order_image);
					bt_last.setVisibility(View.VISIBLE);
					flag++;
				} else if (flag == 1) {
					//iv_order_image.setImageBitmap(bitmaps.get(2));
					asy.DisplayImage(url[2], iv_order_image);
					bt_next.setText("预定");
					flag++;
				} else if (flag == 2) {
					bt_next.clearFocus();
					et_order_phone_number.requestFocus();
					ll_order_image.setVisibility(View.GONE);
					ll_order_phone_number.setVisibility(View.VISIBLE);
					bt_next.setText("确认");
					flag++;
				} else if (flag == 3) {
					et_order_phone_number.requestFocus();
					String pone_nume = et_order_phone_number.getText().toString().trim();
					if (pone_nume==null||pone_nume.equals("")||pone_nume.contains(" ")) {
						Toast.makeText(getApplicationContext(), "手机号码不能为空！", Toast.LENGTH_LONG).show();

					}else if(!isLegalPhoneNumber(pone_nume)){
						Toast.makeText(getApplicationContext(), "手机号码必须合法！", Toast.LENGTH_LONG).show();
					}else{
						updateOrde();
					}

				} else if (flag == 4) {
					finish();
				}

				break;

		}

	}

	private void updateOrde() {
		new Thread() {


			public void run() {

				String pone_nume = et_order_phone_number.getText().toString();
				// 状态更改http://112.124.109.234:8080/WebService.asmx/WebService.asmx/box_commit_pre_order
				String url3 = boxurl+"/WebService.asmx/box_commit_pre_order";
				// String xml1 =
				// "<request><DeviceInfo><mac>B8-88-E3-38-3E-6C</mac></DeviceInfo><VersionInfo><element><code>B10</code><version>1<ersion></element></VersionInfo></request>";

				UpdateRequest ure = new UpdateRequest();
				DeviceInfoMode deviceInfo3 = new DeviceInfoMode();

				deviceInfo3.setMac(macid);

				OrderInfoMode orderInfo = new OrderInfoMode();
				if(mediaId != null && !mediaId.equals("")) {
					orderInfo.setOrderTypeId(mediaId);
				} else {
					orderInfo.setOrderTypeId("1");
				}
				orderInfo.setItemCount("1");
				ItemDataMode itemData = new ItemDataMode();
				itemData.setItemId("2");
				itemData.setItemTitle("手机");
				itemData.setItemData(pone_nume);

				List<ItemDataMode> list = new ArrayList<ItemDataMode>();
				list.add(itemData);
				orderInfo.setItemData(list);
				WorkOrdersStateResponse req = ure.getWorkOrderState(deviceInfo3, null, orderInfo, url3);

				result = req.getResult();
				Message msg = new Message();
				msg.what = BOX_DESKTOP_CONTENT;
				msg.obj = req;
				handler.sendMessage(msg);

			};

		}.start();

	}

	// 无线获取本机MAC地址方法
	public String getWifiMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

}
