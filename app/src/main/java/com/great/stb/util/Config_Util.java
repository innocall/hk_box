package com.great.stb.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.great.stb.bean.ActDataMode;
import com.great.stb.bean.AddAppModel;
import com.great.stb.bean.AppChangeModel;
import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.DeviceStateMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.bean.ItemDataMode;
import com.great.stb.bean.ReslutMode;
import com.great.stb.bean.UpdateResponse;
import com.great.stb.bean.VersionInfoMode;
import com.great.stb.bean.WorkOrdersStateResponse;

public class Config_Util {

	public static String readProperty(String key) {
		String result = "";
		try {
			Properties properties = new Properties();
			InputStream inputStream = Config_Util.class
					.getResourceAsStream("/assets/setting.properties");
			properties.load(inputStream);
			boolean flag = properties.containsKey(key);
			if (flag) {
				result = properties.getProperty(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		Log.d("debug", "----->UUID" + uuid);
		return uniqueId;
	}

	public static UpdateResponse parseXmlToUpdateResponse(String xml) {
		UpdateResponse response = new UpdateResponse();
		DeviceInfoMode deviceInfo = null;
		VersionInfoMode versionInfo = null;
		List<ElementMode> elements = new ArrayList<ElementMode>();
		ElementMode element = null;
		List<ActDataMode> actDatas = null;
		ActDataMode actData = null;
		ReslutMode result = null;
		ItemDataMode itemdate = null;

		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			if (xmlParser == null || xml == null) {
				return response;
			}
			xmlParser.setInput(new ByteArrayInputStream(xml.getBytes("UTF-8")),
					"UTF-8");
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
					case XmlPullParser.START_TAG:
						String tag = xmlParser.getName();
						if (tag.equalsIgnoreCase("DeviceInfo")) {
							deviceInfo = new DeviceInfoMode();
						}

						else if (tag.equalsIgnoreCase("mac")) {
							deviceInfo.setMac(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("boxID")) {
							deviceInfo.setBoxID(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("userName")) {
							deviceInfo.setUserName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("agencyName")) {
							deviceInfo.setAgencyName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("desktopText")) {
							deviceInfo.setDesktopText(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("positionText")) {
							deviceInfo.setPositionText(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("weatherText")) {
							deviceInfo.setWeatherText(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("enterpriseText")) {
							deviceInfo.setEnterpriseText(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("telphone")) {
							deviceInfo.setTelphone(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("regDate")) {
							deviceInfo.setRegDate(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("activeDate")) {
							deviceInfo.setActiveDate(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("activeTel")) {
							deviceInfo.setActiveTel(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("ServiceName")) {
							deviceInfo.setServiceName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("ServiceCycle")) {
							deviceInfo.setServiceCycle(xmlParser.nextText());
						}

						else if (tag.equalsIgnoreCase("VersionInfo")) {
							versionInfo = new VersionInfoMode();
						} else if (tag.equalsIgnoreCase("element")) {
							element = new ElementMode();
							actDatas = new ArrayList<ActDataMode>();
						} else if (tag.equalsIgnoreCase("code")) {
							element.setCode(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("version")) {
							element.setVersion(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("image")) {
							element.setImage(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("logo1")) {
							element.setLogo1(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("logo2")) {
							element.setLogo2(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("logo3")) {
							element.setLogo3(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("smallImage")) {
							element.setSmallImage(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("popupTime")) {
							element.setPopupTime(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("popupImage")){
							element.setPopupImage(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("title")) {
							element.setTitle(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("actType")) {
							element.setActType(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("mediaId")) {
							element.setMediaId(xmlParser.nextText());
						}

						else if (tag.equalsIgnoreCase("pic_large")) {
							element.setPic_large(xmlParser.nextText());
						}

						else if (tag.equalsIgnoreCase("txt_intro")) {
							element.setTxt_intro(xmlParser.nextText());
						}

						else if (tag.equalsIgnoreCase("count")) {
							element.setCount(xmlParser.nextText());
						}

						else if (tag.equalsIgnoreCase("appName")) {
							element.setAppName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("argument")) {
							element.setArgument(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("actData")) {
							actData = new ActDataMode();
						} else if (tag.equalsIgnoreCase("picData")) {
							actData = new ActDataMode();
						} else if (tag.equalsIgnoreCase("order")) {
							actData.setOrder(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("filePath")) {
							if (actData != null) {
								actData.setFilePath(xmlParser.nextText());
							} else {
								element.setFilePath(xmlParser.nextText());
							}

						} else if (tag.equalsIgnoreCase("result")) {
							result = new ReslutMode();
						} else if (tag.equalsIgnoreCase("active_result")) {
							result.setActive_result(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("box_id")) {
							result.setBox_id(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("serviceTel")) {
							result.setServiceTel(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("result_desc")) {
							result.setResult_desc(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("user_id")) {
							result.setUser_id(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("license_id")) {
							result.setLicense_id(xmlParser.nextText());
						}

						else if (tag.equalsIgnoreCase("itemData")) {
							itemdate = new ItemDataMode();
						} else if (tag.equalsIgnoreCase("itemId")) {
							itemdate.setItemId(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("itemTitle")) {
							itemdate.setItemTitle(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("itemLength")) {
							itemdate.setItemLength(xmlParser.nextText());
						}

						break;
					case XmlPullParser.END_TAG:
						tag = xmlParser.getName();

						if (tag.equalsIgnoreCase("element")) {
							if (element != null) {
								elements.add(element);
								element.setActData(actDatas);
								actDatas = null;

							}
						} else if (tag.equalsIgnoreCase("actData")) {
							actDatas.add(actData);
							actData = null;

						} else if (tag.equalsIgnoreCase("picData")) {
							actDatas.add(actData);
							actData = null;

						}
						break;
					default:
						break;
				}
				evtType = xmlParser.next();
			}
			response.setDeviceInfo(deviceInfo);
			if (versionInfo != null) {
				versionInfo.setElement(elements);
				response.setVersionInfo(versionInfo);
			}
			response.setReslut(result);
			response.setItemDate(itemdate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static WorkOrdersStateResponse parseXmlToWorkOrderResponse(String xml) {
		WorkOrdersStateResponse response = new WorkOrdersStateResponse();
		DeviceInfoMode deviceInfo = null;
		DeviceStateMode deviceState = null;
		String result = "";

		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(new ByteArrayInputStream(xml.getBytes("UTF-8")),
					"UTF-8");
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
					case XmlPullParser.START_TAG:
						String tag = xmlParser.getName();
						if (tag.equalsIgnoreCase("DeviceInfo")) {
							deviceInfo = new DeviceInfoMode();
						} else if (tag.equalsIgnoreCase("mac")) {
							deviceInfo.setMac(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("boxID")) {
							deviceInfo.setBoxID(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("userName")) {
							deviceInfo.setUserName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("agencyName")) {
							deviceInfo.setAgencyName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("desktopText")) {
							deviceInfo.setDesktopText(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("positionText")) {
							deviceInfo.setPositionText(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("weatherText")) {
							deviceInfo.setWeatherText(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("enterpriseText")) {
							deviceInfo.setEnterpriseText(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("DeviceState")) {
							deviceState = new DeviceStateMode();
						} else if (tag.equalsIgnoreCase("state")) {
							if (deviceState != null) {
								deviceState.setState(xmlParser.nextText());
							}
						} else if (tag.equalsIgnoreCase("result")) {
							if (deviceState != null) {
								deviceState.setResult(xmlParser.nextText());
							} else {
								result = xmlParser.nextText();
							}
						}
					/*
					 * else if (tag.equalsIgnoreCase("result")) { if
					 * (deviceState == null) { result = xmlParser.nextText(); }
					 * }
					 */
						break;
				}
				evtType = xmlParser.next();
			}
			response.setDeviceInfo(deviceInfo);
			if (deviceState != null) {
				response.setDeviceState(deviceState);
			}

			if (null != result && !"".equals(result)) {
				response.setResult(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static AppChangeModel parseXmlToUpdateChange(String xml) {
		AppChangeModel change = new AppChangeModel();
		DeviceInfoMode deviceInfo = null;
		List<String> removeAppPackageName = new ArrayList<String>();
		List<AddAppModel> addAppList = new ArrayList<AddAppModel>();
		AddAppModel addAppModel = null;
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			if (xmlParser == null || xml == null) {
				return change;
			}
			xmlParser.setInput(new ByteArrayInputStream(xml.getBytes("UTF-8")),
					"UTF-8");
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
					case XmlPullParser.START_TAG:
						String tag = xmlParser.getName();
						if (tag.equalsIgnoreCase("DeviceInfo")) {
							deviceInfo = new DeviceInfoMode();
						} else if (tag.equalsIgnoreCase("mac")) {
							deviceInfo.setMac(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("boxID")) {
							deviceInfo.setBoxID(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("item")) {
							removeAppPackageName.add(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("add_item")) {
							addAppModel = new AddAppModel();
						} else if(tag.equalsIgnoreCase("pkg_name")) {
							addAppModel.setPackName(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("intro")) {
							addAppModel.setDes(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("url")) {
							addAppModel.setUrl(xmlParser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						tag = xmlParser.getName();
						if (tag.equalsIgnoreCase("add_item")) {
							if (addAppList != null) {
								addAppList.add(addAppModel);
								addAppModel = null;
							}
						}
						break;
					default:
						break;
				}
				evtType = xmlParser.next();
			}
			change.setDeviceInfo(deviceInfo);
			change.setAddAppList(addAppList);
			change.setRemoveAppPackageName(removeAppPackageName);;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return change;
	}

}
