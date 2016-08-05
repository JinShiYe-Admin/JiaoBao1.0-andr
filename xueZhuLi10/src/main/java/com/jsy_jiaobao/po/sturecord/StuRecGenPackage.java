package com.jsy_jiaobao.po.sturecord;//

public class StuRecGenPackage {
	private int AUTO_ID;// 0,
	private int GEN_ID;// 0,
	private String REC_NAME;// null,
	private String GEN_NAME;// null,
	private String CHI_NAME;// null,
	private String CHI_BIRTH;// null,
	private String CHI_SEX;// null,
	private String CHI_BROOD;// null,
	private String CHI_VOLK;// null,
	private String PHOTO_PATH;// null,
	private String ADDR;// null,
	private String ORIGIN;// null,
	private String REC_DATE;// 0001-01-01T00:00:00"

	public int getAUTO_ID() {
		return AUTO_ID;
	}

	public void setAUTO_ID(int aUTO_ID) {
		AUTO_ID = aUTO_ID;
	}

	public int getGEN_ID() {
		return GEN_ID;
	}

	public void setGEN_ID(int gEN_ID) {
		GEN_ID = gEN_ID;
	}

	public String getREC_NAME() {
		return REC_NAME;
	}

	public void setREC_NAME(String rEC_NAME) {
		REC_NAME = rEC_NAME;
	}

	public String getGEN_NAME() {
		return GEN_NAME;
	}

	public void setGEN_NAME(String gEN_NAME) {
		GEN_NAME = gEN_NAME;
	}

	public String getCHI_NAME() {
		return CHI_NAME;
	}

	public void setCHI_NAME(String cHI_NAME) {
		CHI_NAME = cHI_NAME;
	}

	public String getCHI_BIRTH() {
		return CHI_BIRTH;
	}

	public void setCHI_BIRTH(String cHI_BIRTH) {
		CHI_BIRTH = cHI_BIRTH;
	}

	public String getCHI_SEX() {
		return CHI_SEX;
	}

	public void setCHI_SEX(String cHI_SEX) {
		CHI_SEX = cHI_SEX;
	}

	public String getCHI_BROOD() {
		return CHI_BROOD;
	}

	public void setCHI_BROOD(String cHI_BROOD) {
		CHI_BROOD = cHI_BROOD;
	}

	public String getCHI_VOLK() {
		return CHI_VOLK;
	}

	public void setCHI_VOLK(String cHI_VOLK) {
		CHI_VOLK = cHI_VOLK;
	}

	public String getPHOTO_PATH() {
		return PHOTO_PATH;
	}

	public void setPHOTO_PATH(String pHOTO_PATH) {
		PHOTO_PATH = pHOTO_PATH;
	}

	public String getADDR() {
		return ADDR;
	}

	public void setADDR(String aDDR) {
		ADDR = aDDR;
	}

	public String getORIGIN() {
		return ORIGIN;
	}

	public void setORIGIN(String oRIGIN) {
		ORIGIN = oRIGIN;
	}

	public String getREC_DATE() {
//		REC_DATE = REC_DATE.replace("T", " ");
		return REC_DATE.replace("T", " ");
	}

	public void setREC_DATE(String rEC_DATE) {
		REC_DATE = rEC_DATE;
	}

}
