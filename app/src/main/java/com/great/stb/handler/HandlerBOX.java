package com.great.stb.handler;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.bean.UpdateResponse;
import com.great.stb.bean.VersionInfoMode;
import com.great.stb.common.UpdateRequest;
import com.great.stb.dao.ElementDAO;

public class HandlerBOX {

	private static Context context;
	private static final int BOX_ACTIVE = 0;

	protected static final int BOX_DESKTOP_APP = 1;

	protected static final int BOX_ENTERPRISE_APP = 2;

	protected static final int BOX_DESKTOP_CONTENT = 3;

	protected static final int BOX_ENTERPRISE_CONTENT = 4;



	/**
	 * 消息处理器
	 */
	private static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

				case BOX_ACTIVE:
					UpdateResponse req_active = (UpdateResponse) msg.obj;


					ElementDAO dao = new ElementDAO(context);
					dao.insertActive(req_active.getDeviceInfo(),req_active.getReslut().getBox_id());

					break;

				case BOX_DESKTOP_APP:
					UpdateResponse req_desktop_app = (UpdateResponse) msg.obj;
					DeviceInfoMode deviceInfo = req_desktop_app.getDeviceInfo();
					VersionInfoMode versionInfo = req_desktop_app.getVersionInfo();
					List<ElementMode> element = versionInfo.getElement();

					break;

				case BOX_DESKTOP_CONTENT:
					UpdateResponse req_desktop_content = (UpdateResponse) msg.obj;

					break;

			}
		};
	};

/*	public static void activeBox(Context context_main) {
		
		context = context_main;

		new Thread() {
			public void run() {

				// 认证
				UpdateRequest ure = new UpdateRequest();
				// 激活
				String url1 = "http://61.233.25.22/WebService.asmx/box_active";
				// String xml1 =
				// "<request><DeviceInfo><mac>B8-88-E3-38-3E-6C</mac></DeviceInfo><VersionInfo><element><code>B10</code><version>1</version></element></VersionInfo></request>";
				DeviceInfoMode deviceInfo = new DeviceInfoMode();
				deviceInfo.setMac("B8-88-E3-38-3E-6C");
				deviceInfo.setSn("12345678901234567890");
				// deviceInfo.setPhone_no("13912345678");
				UpdateResponse req = ure.getUpdate(deviceInfo, null, url1);

				Message msg = new Message();
				msg.what = BOX_ACTIVE;
				msg.obj = req;
				handler.sendMessage(msg);

			};

		}.start();

	}*/

	public static void desktopAppBox() {

		new Thread() {
			public void run() {

				String url1 = "http://61.233.25.22/WebService.asmx/box_desktop_app";
				UpdateRequest ure = new UpdateRequest();

				DeviceInfoMode deviceInfo = new DeviceInfoMode();

				deviceInfo.setMac("B8-88-E3-38-3E-6C");

				List<ElementMode> list = new ArrayList<ElementMode>();

				ElementMode element1 = new ElementMode();
				element1.setCode("B10");
				element1.setVersion("1");
				list.add(element1);

				UpdateResponse req = ure.getUpdate(deviceInfo, list, url1);

				Message msg = new Message();
				msg.what = BOX_DESKTOP_APP;
				msg.obj = req;
				handler.sendMessage(msg);

			};

		}.start();

	}

	public static void enterpriseAppBox() {

		new Thread() {
			public void run() {

				String url1 = "http://61.233.25.22/WebService.asmx/box_enterprise_app";
				UpdateRequest ure = new UpdateRequest();

				DeviceInfoMode deviceInfo = new DeviceInfoMode();

				deviceInfo.setMac("B8-88-E3-38-3E-6C");

				List<ElementMode> list = new ArrayList<ElementMode>();

				ElementMode element1 = new ElementMode();
				element1.setCode("B20");
				element1.setVersion("1");
				list.add(element1);

				UpdateResponse req = ure.getUpdate(deviceInfo, list, url1);

				Message msg = new Message();
				msg.what = BOX_ENTERPRISE_APP;
				msg.obj = req;
				handler.sendMessage(msg);

			};

		}.start();

	}

	public static void desktopContentBox() {

		new Thread() {
			public void run() {

				//桌面内容更新	
				String url2 =  "http://61.233.25.22/WebService.asmx/box_desktop_content";
				//	String xml1 = "<request><DeviceInfo><mac>B8-88-E3-38-3E-6C</mac></DeviceInfo><VersionInfo><element><code>B10</code><version>1</version></element></VersionInfo></request>";
				UpdateRequest ure = new UpdateRequest();

				DeviceInfoMode deviceInfo2 = new DeviceInfoMode();

				deviceInfo2.setMac("B8-88-E3-38-3E-6C");


				List<ElementMode> list2 = new ArrayList<ElementMode>();

				ElementMode element12 = new ElementMode();
				element12.setCode("C10");
				element12.setVersion("1");
				list2.add(element12);



				ElementMode element121 = new ElementMode();
				element121.setCode("C40");
				element121.setVersion("1");
				list2.add(element121);

				ElementMode element13 = new ElementMode();
				element13.setCode("D10");
				element13.setVersion("1");
				list2.add(element13);

				ElementMode element14 = new ElementMode();
				element14.setCode("D20");
				element14.setVersion("1");
				list2.add(element14);

				ElementMode element15 = new ElementMode();
				element15.setCode("D30");
				element15.setVersion("1");
				list2.add(element15);

				ElementMode element16 = new ElementMode();
				element16.setCode("D40");
				element16.setVersion("1");
				list2.add(element16);

				ElementMode element17 = new ElementMode();
				element17.setCode("D50");
				element17.setVersion("1");
				list2.add(element17);


				ElementMode element171 = new ElementMode();
				element171.setCode("D60");
				element171.setVersion("1");
				list2.add(element171);


				ElementMode element172 = new ElementMode();
				element172.setCode("D70");
				element172.setVersion("1");
				list2.add(element172);

				ElementMode element173 = new ElementMode();
				element173.setCode("D80");
				element173.setVersion("1");
				list2.add(element173);

				ElementMode element18 = new ElementMode();
				element18.setCode("E10");
				element18.setVersion("1");
				list2.add(element18);

				ElementMode element19 = new ElementMode();
				element19.setCode("E20");
				element19.setVersion("1");
				list2.add(element19);

				ElementMode element181 = new ElementMode();
				element181.setCode("E30");
				element181.setVersion("1");
				list2.add(element181);

				ElementMode element182 = new ElementMode();
				element182.setCode("E40");
				element182.setVersion("1");
				list2.add(element182);

				ElementMode element183 = new ElementMode();
				element183.setCode("E50");
				element183.setVersion("1");
				list2.add(element183);


				UpdateResponse req = ure.getUpdate(deviceInfo2, list2, url2);


				Message msg = new Message();
				msg.what = BOX_DESKTOP_CONTENT;
				msg.obj = req;
				handler.sendMessage(msg);

			};

		}.start();

	}

	public static void enterpriseContentBox() {

		new Thread() {
			public void run() {



//				Message msg = new Message();
//				msg.what = BOX_ENTERPRISE_CONTENT;
//				msg.obj = req;
//				handler.sendMessage(msg);

			};

		}.start();

	}

	public static void getStateBox() {

		new Thread() {
			public void run() {



//				Message msg = new Message();
//				msg.what = BOX_GET_STATE;
//				msg.obj = req;
//				handler.sendMessage(msg);

			};

		}.start();

	}

	public static void updateStateBox() {

		new Thread() {
			public void run() {



//				Message msg = new Message();
//				msg.what = BOX_UPDATE_STATE;
//				msg.obj = req;
//				handler.sendMessage(msg);

			};

		}.start();

	}

	public static void updateOrderBox() {

		new Thread() {
			public void run() {



//				Message msg = new Message();
//				msg.what = BOX_UPDATE_ORDER;
//				msg.obj = req;
//				handler.sendMessage(msg);

			};

		}.start();

	}

	public static void uploadLogBox() {

		new Thread() {
			public void run() {



//				Message msg = new Message();
//				msg.what = BOX_UPDATE_LOG;
//				msg.obj = req;
//				handler.sendMessage(msg);

			};

		}.start();

	}

	public static void preorderAppBox() {

		new Thread() {
			public void run() {



//				Message msg = new Message();
//				msg.what = BOX_PREORDER_APP;
//				msg.obj = req;
//				handler.sendMessage(msg);

			};

		}.start();

	}

}
