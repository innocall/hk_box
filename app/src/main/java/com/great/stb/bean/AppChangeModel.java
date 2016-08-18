package com.great.stb.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用变更接口返回实体
 *
 * @author wu
 *
 */
public class AppChangeModel {
	private DeviceInfoMode deviceInfo;
	private List<String> removeAppPackageName = new ArrayList<String>();
	private List<AddAppModel> addAppList = new ArrayList<AddAppModel>();

	public DeviceInfoMode getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfoMode deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public List<String> getRemoveAppPackageName() {
		return removeAppPackageName;
	}

	public void setRemoveAppPackageName(List<String> removeAppPackageName) {
		this.removeAppPackageName = removeAppPackageName;
	}

	public List<AddAppModel> getAddAppList() {
		return addAppList;
	}

	public void setAddAppList(List<AddAppModel> addAppList) {
		this.addAppList = addAppList;
	}

}
