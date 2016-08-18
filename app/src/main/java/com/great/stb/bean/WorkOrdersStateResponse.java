package com.great.stb.bean;

public class WorkOrdersStateResponse {
   private DeviceInfoMode deviceInfo;
   private DeviceStateMode deviceState;
   private String result;
public DeviceInfoMode getDeviceInfo() {
	return deviceInfo;
}
public void setDeviceInfo(DeviceInfoMode deviceInfo) {
	this.deviceInfo = deviceInfo;
}
public DeviceStateMode getDeviceState() {
	return deviceState;
}
public void setDeviceState(DeviceStateMode deviceState) {
	this.deviceState = deviceState;
}
public String getResult() {
	return result;
}
public void setResult(String result) {
	this.result = result;
}
   
}
