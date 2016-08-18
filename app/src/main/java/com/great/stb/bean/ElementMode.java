package com.great.stb.bean;

import java.util.List;

public class ElementMode {
	private String code;
	private String version;
	private String image;
	private String smallImage;
	private String title;
	private String actType;
	private String mediaId;
	private String filePath;
	private String pic_large;
	private String txt_intro;
	private String appName;
	private String count;
	private String logo1;
	private String logo2;
	private String logo3;
	private String argument;
	private String popupImage;
	private String popupTime;

	public String getLogo1() {
		return logo1;
	}

	public void setLogo1(String logo1) {
		this.logo1 = logo1;
	}

	public String getLogo2() {
		return logo2;
	}

	public void setLogo2(String logo2) {
		this.logo2 = logo2;
	}

	public String getPic_large() {
		return pic_large;
	}

	public void setPic_large(String pic_large) {
		this.pic_large = pic_large;
	}

	public String getTxt_intro() {
		return txt_intro;
	}

	public void setTxt_intro(String txt_intro) {
		this.txt_intro = txt_intro;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	private List<ActDataMode> actData;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public List<ActDataMode> getActData() {
		return actData;
	}

	public void setActData(List<ActDataMode> actData) {
		this.actData = actData;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLogo3() {
		return logo3;
	}

	public void setLogo3(String logo3) {
		this.logo3 = logo3;
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

	public String getPopupImage() {
		return popupImage;
	}

	public void setPopupImage(String popupImage) {
		this.popupImage = popupImage;
	}

	public String getPopupTime() {
		return popupTime;
	}

	public void setPopupTime(String popupTime) {
		this.popupTime = popupTime;
	}

}
