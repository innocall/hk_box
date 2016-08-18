package com.great.stb.bean;

public class UpdateResponse {
	private DeviceInfoMode deviceInfo;
	private VersionInfoMode versionInfo;
	private ReslutMode reslut;
	private ItemDataMode itemDate;

	public DeviceInfoMode getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfoMode deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public VersionInfoMode getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(VersionInfoMode versionInfo) {
		this.versionInfo = versionInfo;
	}

	public ReslutMode getReslut() {
		return reslut;
	}

	public void setReslut(ReslutMode reslut) {
		this.reslut = reslut;
	}

	public ItemDataMode getItemDate() {
		return itemDate;
	}

	public void setItemDate(ItemDataMode itemDate) {
		this.itemDate = itemDate;
	}

}
