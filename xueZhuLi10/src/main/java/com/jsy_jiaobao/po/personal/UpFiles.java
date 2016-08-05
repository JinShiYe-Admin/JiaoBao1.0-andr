package com.jsy_jiaobao.po.personal;

/**
 * 文件Model
 * 
 * @author admin
 * 
 */
public class UpFiles {
	private String originalName; // 原文件名
	private String url; // 下载url
	private String size; // 文件大小
	private String type; // 文件类型

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
