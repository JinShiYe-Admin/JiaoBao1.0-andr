package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 照片Model
 * @author admin
 *
 */
public class Photo implements Serializable {

	
	private static final long serialVersionUID = 5057772015315261804L;
	private String TabID;// Int 照片ID
	private String CreateByjiaobaohao;// string 照片创建人
	private String SMPhotoPath;// String 照片对应的小图片url
	private String BIGPhotoPath;// String 照片对应的原始照片的URL
	private String PhotoDescribe;// String 照片描述
	
	public String getCreateByjiaobaohao() {
		return CreateByjiaobaohao;
	}

	public void setCreateByjiaobaohao(String createByjiaobaohao) {
		CreateByjiaobaohao = createByjiaobaohao;
	}

	public String getSMPhotoPath() {
		return SMPhotoPath;
	}

	public void setSMPhotoPath(String sMPhotoPath) {
		SMPhotoPath = sMPhotoPath;
	}

	public String getBIGPhotoPath() {
		return BIGPhotoPath;
	}

	public void setBIGPhotoPath(String bIGPhotoPath) {
		BIGPhotoPath = bIGPhotoPath;
	}

	public String getPhotoDescribe() {
		return PhotoDescribe;
	}

	public void setPhotoDescribe(String photoDescribe) {
		PhotoDescribe = photoDescribe;
	}

	public String getTabID() {
		return TabID;
	}

	public void setTabID(String tabID) {
		TabID = tabID;
	}

}
