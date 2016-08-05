package com.jsy_jiaobao.po.sys;

public class VersionInfo {
	private int versionCode;// ": "1",
	private String versionName;// ": "1.0",
	private String introduce;// ": "",
	private String url;// ": "",
	private String updata_1;// ": "",
	private String updata_2;// ": ""
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 0=不强制，1=强制更新
	 */
	public String getUpdata_1() {
		return updata_1;
	}
	public void setUpdata_1(String updata_1) {
		this.updata_1 = updata_1;
	}
	public String getUpdata_2() {
		return updata_2;
	}
	public void setUpdata_2(String updata_2) {
		this.updata_2 = updata_2;
	}
	
}
