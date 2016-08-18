package com.great.stb.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.great.stb.bean.AppChangeModel;
import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.DeviceStateMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.bean.ItemDataMode;
import com.great.stb.bean.LogBean;
import com.great.stb.bean.OrderInfoMode;
import com.great.stb.bean.UpdateResponse;
import com.great.stb.bean.WorkOrdersStateResponse;
import com.great.stb.util.Config_Util;

public class UpdateRequest {

	private static String prefixURL = Common_ATP.BOX_URL;

	public static UpdateResponse getUpdate(DeviceInfoMode deviceInfo,
										   List<ElementMode> list, String methodUrl) {
		UpdateResponse response = new UpdateResponse();
		try {
			StringBuffer xml = new StringBuffer("<request>");
			xml.append("<DeviceInfo>");

			if (deviceInfo.getMac() != null && !deviceInfo.getMac().equals("")) {
				xml.append("<mac>");
				xml.append(deviceInfo.getMac());
				xml.append("</mac>");
			}

			// by shenhb
			if (deviceInfo.getMacWifi() != null
					&& !deviceInfo.getMacWifi().equals("")) {
				xml.append("<mac_wifi>");
				xml.append(deviceInfo.getMacWifi());
				xml.append("</mac_wifi>");
			}

			if (deviceInfo.getMacEth() != null
					&& !deviceInfo.getMacEth().equals("")) {
				xml.append("<mac_eth>");
				xml.append(deviceInfo.getMacEth());
				xml.append("</mac_eth>");
			}

			if (deviceInfo.getCPUID() != null
					&& !deviceInfo.getCPUID().equals("")) {
				xml.append("<cpu_id>");
				xml.append(deviceInfo.getCPUID());
				xml.append("</cpu_id>");
			}
			// by shenhb end

			if (deviceInfo.getUserName() != null
					&& !deviceInfo.getUserName().equals("")) {
				xml.append("<userName>");
				xml.append(deviceInfo.getUserName());
				xml.append("</userName>");
			}

			if (deviceInfo.getAgencyName() != null
					&& !deviceInfo.getAgencyName().equals("")) {
				xml.append("<agencyName>");
				xml.append(deviceInfo.getAgencyName());
				xml.append("</agencyName>");
			}

			if (deviceInfo.getSn() != null && !deviceInfo.getSn().equals("")) {
				xml.append("<sn>");
				xml.append(deviceInfo.getSn());
				xml.append("</sn>");
			}

			if (deviceInfo.getPhone_no() != null
					&& !deviceInfo.getPhone_no().equals("")) {
				xml.append("<phone_no>");
				xml.append(deviceInfo.getPhone_no());
				xml.append("</phone_no>");
			}

			if (deviceInfo.getActive_phone() != null
					&& !deviceInfo.getActive_phone().equals("")) {
				xml.append("<active_phone>");
				xml.append(deviceInfo.getActive_phone());
				xml.append("</active_phone>");
			}

			xml.append("</DeviceInfo>");
			if (list != null) {

				xml.append("<VersionInfo>");

				for (ElementMode element : list) {
					xml.append("<element>");
					xml.append("<code>" + element.getCode() + "</code>");
					xml.append("<version>" + element.getVersion()
							+ "</version>");
					xml.append("</element>");
				}
				xml.append("</VersionInfo>");
			}
			xml.append("</request>");
			String result = methodCalling(methodUrl, xml.toString());
			response = Config_Util.parseXmlToUpdateResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private static String methodCalling(String methodUrl, String bodyXml) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("xmlparams", bodyXml);

		String str = HttpPostRequestAd.postRequest(methodUrl, map);

		return str;
	}

	public static WorkOrdersStateResponse getWorkOrderState(
			DeviceInfoMode deviceInfo, DeviceStateMode deviceState,
			OrderInfoMode orderInfo, String methodUrl) {
		WorkOrdersStateResponse response = new WorkOrdersStateResponse();
		try {
			StringBuffer xml = new StringBuffer("<request>");
			xml.append("<DeviceInfo>");
			if (deviceInfo.getMac() != null && !deviceInfo.getMac().equals("")) {
				xml.append("<mac>");
				xml.append(deviceInfo.getMac());
				xml.append("</mac>");
			}
			if (deviceInfo.getUserName() != null
					&& !deviceInfo.getUserName().equals("")) {
				xml.append("<userName>");
				xml.append(deviceInfo.getUserName());
				xml.append("</userName>");
			}
			if (deviceInfo.getAgencyName() != null
					&& !deviceInfo.getAgencyName().equals("")) {
				xml.append("<agencyName>");
				xml.append(deviceInfo.getAgencyName());
				xml.append("</agencyName>");
			}
			xml.append("</DeviceInfo>");

			if (deviceState != null) {
				xml.append("<DeviceState>");
				if (null != deviceState.getState()
						&& !deviceState.getState().equals("")) {
					xml.append("<state>");
					xml.append(deviceState.getState());
					xml.append("</state>");
				}

				if (null != deviceState.getReason()
						&& !deviceState.getReason().equals("")) {
					xml.append("<fail_reason>");
					xml.append(deviceState.getReason());
					xml.append("</fail_reason>");
				}
				if (null != deviceState.getResult()
						&& !deviceState.getResult().equals("")) {
					xml.append("<result>");
					xml.append(deviceState.getResult());
					xml.append("</result>");
				}
				xml.append("</DeviceState>");
			}

			if (orderInfo != null) {

				xml.append("<orderInfo>");
				if (null != orderInfo.getOrderTypeId()
						&& !orderInfo.getOrderTypeId().equals("")) {
					xml.append("<orderTypeId>");
					xml.append(orderInfo.getOrderTypeId());
					xml.append("</orderTypeId>");
				}
				if (null != orderInfo.getItemCount()
						&& !orderInfo.getItemCount().equals("")) {
					xml.append("<ItemCount>");
					xml.append(orderInfo.getItemCount());
					xml.append("</ItemCount>");
				}

				List<ItemDataMode> list = orderInfo.getItemData();

				for (ItemDataMode itemData : list) {
					xml.append("<itemData>");
					xml.append("<itemId>" + itemData.getItemId() + "</itemId>");
					xml.append("<itemTitle>" + itemData.getItemTitle()
							+ "</itemTitle>");
					xml.append("<itemData>" + itemData.getItemData()
							+ "</itemData>");
					xml.append("</itemData>");
				}

				xml.append("</orderInfo>");

			}

			xml.append("</request>");

			String result = methodCalling(methodUrl, xml.toString());
			response = Config_Util.parseXmlToWorkOrderResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static boolean logUpLoadResponse(DeviceInfoMode deviceInfo,
											List<LogBean> logs, String methodUrl) {
		try {
			StringBuffer xml = new StringBuffer("<request>");
			xml.append("<DeviceInfo>");
			if (deviceInfo.getMac() != null && !deviceInfo.getMac().equals("")) {
				xml.append("<mac>");
				xml.append(deviceInfo.getMac());
				xml.append("</mac>");
			}
			xml.append("</DeviceInfo>");
			xml.append("<DeviceLog>");
			List<LogBean> onOffLogs = new ArrayList<LogBean>();
			List<LogBean> AppUseLog = new ArrayList<LogBean>();
			List<LogBean> AccessLog = new ArrayList<LogBean>();
			List<LogBean> MenuUseLog = new ArrayList<LogBean>();

			for (LogBean bean : logs) {
				LogBean bean1 = bean;
				if (bean.getType().equals("onoff")) {
					onOffLogs.add(bean1);
				} else if (bean.getType().equals("app")) {
					AppUseLog.add(bean1);
				} else if (bean.getType().equals("access")) {
					AccessLog.add(bean1);
				} else if (bean.getType().equals("menu")) {
					MenuUseLog.add(bean1);
				}
			}
			if (onOffLogs.size() > 0) {
				xml.append("<OnOffLog>");
				for (LogBean bean : onOffLogs) {
					xml.append("<OnOffItem>");
					xml.append("<OnOffTime>");
					xml.append(bean.getDatetime());
					xml.append("</OnOffTime>");

					xml.append("<OnOff>");
					xml.append(bean.getValue());
					xml.append("</OnOff>");
					xml.append("</OnOffItem>");
				}
				xml.append("</OnOffLog>");
			}

			if (AppUseLog.size() > 0) {
				xml.append("<AppUseLog>");
				for (LogBean bean : AppUseLog) {
					xml.append("<AppUseItem>");
					xml.append("<RunTime>");
					xml.append(bean.getDatetime());
					xml.append("</RunTime>");

					xml.append("<AppName>");
					xml.append(bean.getName());
					xml.append("</AppName>");

					xml.append("<Action>");
					xml.append(bean.getValue());
					xml.append("</Action>");
					xml.append("</AppUseItem>");
				}
				xml.append("</AppUseLog>");
			}

			if (AccessLog.size() > 0) {
				xml.append("<AccessLog>");
				for (LogBean bean : AccessLog) {
					xml.append("<AccessItem>");
					xml.append("<AccessTime>");
					xml.append(bean.getDatetime());
					xml.append("</AccessTime>");
					xml.append("<AccessURL>");
					xml.append(bean.getValue());
					xml.append("</AccessURL>");
					xml.append("</AccessItem>");
				}
				xml.append("</AccessLog>");
			}

			if (MenuUseLog.size() > 0) {
				xml.append("<MenuUseLog>");
				for (LogBean bean : MenuUseLog) {
					xml.append("<MenuUseItem>");
					xml.append("<AccessTime>");
					xml.append(bean.getDatetime());
					xml.append("</AccessTime>");

					xml.append("<MenuCode>");
					xml.append(bean.getValue());
					xml.append("</MenuCode>");
					xml.append("</MenuUseItem>");
				}
				xml.append("</MenuUseLog>");
			}

			xml.append("</DeviceLog>");
			xml.append("</request>");

			String result = methodCalling(methodUrl, xml.toString());

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static List<LogBean> ReadTxtFile(String strFilePath) {
		List<LogBean> list = new ArrayList<LogBean>();
		LogBean bean = null;
		String path = strFilePath;
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(path);
		// 如果path是传递过来的参数，可以做一个非目录的判断
		if (file.isDirectory()) {
			Log.d("File", "The File doesn't not exist.");
		} else {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(
							instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					// 分行读取
					while ((line = buffreader.readLine()) != null) {
						bean = new LogBean();
						String logLine[] = line.split("#");
						String logs[] = logLine[1].split(",");
						bean.setDatetime(logs[0].trim());
						bean.setValue(logs[1].trim());
						bean.setType(logs[2].trim());
						if (logs.length > 3) {
							bean.setName(logs[3].trim());
						}
						list.add(bean);

					}
					instream.close();
				}
			}

			catch (Exception e) {
				Log.d("TestFile", e.getMessage());
			}
		}
		return list;
	}

	public static void removeContext(String path) {
		FileOutputStream testfile;
		try {
			testfile = new FileOutputStream(path);
			testfile.write(new String("").getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public AppChangeModel getUpdate(DeviceInfoMode deviceInfo, String url) {
		// TODO Auto-generated method stub
		AppChangeModel change = new AppChangeModel();
		try {
			StringBuffer xml = new StringBuffer("<request>");
			xml.append("<DeviceInfo>");

			if (deviceInfo.getMac() != null && !deviceInfo.getMac().equals("")) {
				xml.append("<mac>");
				xml.append(deviceInfo.getMac());
				xml.append("</mac>");
			}
			xml.append("</DeviceInfo></request>");
			String result = methodCalling(url, xml.toString());
			change = Config_Util.parseXmlToUpdateChange(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return change;
	}

}
