package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 事务里的附件Model
 */
public class Attlist implements Serializable {
	private static final long serialVersionUID = -595988062353930880L;
	private String dlurl;// \":
							// \"http://www.jb.edu8800.com/jbapp/AppFiles/dlfile/NDEzRjk0MjY4MDVDOTNBOQ\",
	private String OrgFilename;// \": \"开发部测试题目.doc\",
	private String FileSize;// \": \"274KB\"

	public String getOrgFilename() {
		return OrgFilename;
	}

	public void setOrgFilename(String orgFilename) {
		OrgFilename = orgFilename;
	}

	public String getFileSize() {
		return FileSize;
	}

	public void setFileSize(String fileSize) {
		FileSize = fileSize;
	}

	public String getDlurl() {
		return dlurl;
	}

	public void setDlurl(String dlurl) {
		this.dlurl = dlurl;
	}
}