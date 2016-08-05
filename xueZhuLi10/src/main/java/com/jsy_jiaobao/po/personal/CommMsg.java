package com.jsy_jiaobao.po.personal;

/**
 * 这个model的注释原来写得很混乱，我在原来的基础上进行了修改<br>
 * 但是依旧无法保证完全正确，请根据自己得到的信息进行判断<br>
 * 给别人的代码加注释好烦啊 orz--ShangLin Mo
 */
/**
 * 交流信息model
 */
public class CommMsg {
	// 交流消息表 不允许空 数据类型int
	private int TabID;// 58459,;//标识字段
	private String TabIDStr;// 58459,;//加密后的标识字段
	private int UnitID;// 0,;// 单位ID 不允许空 数据类型int
	private String UnitShortName;// 教育局测试4,;// 单位简称 长度60 不允许空 数据类型nvarchar	
	private int UserID;// 0,;// 发送者用户ID 不允许空 数据类型int	
	private String UserName;// 李国信,;// 发送者姓名 长度40 不允许空 数据类型nvarchar	
	private String MsgContent;// test,// 内容 长度2000 不允许空 数据类型nvarchar
	private String RecDate;// 2014-08-19T11:28:18,// 发送时间 不允许空 数据类型smalldatetime
	private int UserType;// 0,// 发送者类型 不允许空 数据类型tinyint	
	private int JiaoBaoHao;// 0,// 发送者AccID 不允许空 数据类型int	
	private int SMSFlag;// 0,// 是否发短信 不允许空 数据类型tinyint		
	private String JiaobaoID;// 5150001,// 接收者AccID串 长度2147483647 数据类型text	
	private String ReaderList;// 接收者对象json串， 长度2147483647 数据类型text// [{\UserID\:701,\UserIDType\:\701_1\,\JiaoBaoHao\:5150001,\TrueName\:\李国信\,\SrvState\:2,\MCState\:0,\SMSState\:0,\PCState\:1,\ClassID\:0,\UnitID\:990}],
	private String ReadFlagList;// 5150001,// 已读过的AccID串 长度2147483647 数据类型text
	private String TrunToList;// [],// 转发对象json串 长度2147483647 数据类型text
	private int State;// 1,// 状态1正常0在审核中 不允许空 数据类型int
	private String Checker;// null,// 审核者 长度40 数据类型nvarchar
	private String CheckDate;// null,// 审核日期 数据类型smalldatetime	
	private String AttList;// null,// 附件ID串 长度500 数据类型varchar
	private int IsAdmin;// 0,// 发送者是否管理员
	private String FeebackList;// null,// 回复者教宝号ID串 长度2147483646 数据类型ntext
	private int MsgType;// 0// 消息类型，0交流信息，1短信直通车信息 不允许空 数据类型int
	
	/*以下变量的说明本来有两个人知道，一个是作者另一个是上帝，现在只有上帝知道了。嗯很好，这样很基督*/	
	private int ClassID;// 0,//班级ID
	private String CityCode;// null,
	private String destID;// null,
	private String RecGUID;// null,
	private int PointActionCode;// 0,
	private int TrunToFlag;// 0,
	private int GenReadCount;// 0,
	private int GenSMSCount;// 0,
	
	

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
	}

	public int getUnitID() {
		return UnitID;
	}

	public void setUnitID(int unitID) {
		UnitID = unitID;
	}

	public String getUnitShortName() {
		return UnitShortName;
	}

	public void setUnitShortName(String unitShortName) {
		UnitShortName = unitShortName;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getMsgContent() {
		return MsgContent;
	}

	public void setMsgContent(String msgContent) {
		MsgContent = msgContent;
	}

	public String getRecDate() {
		return RecDate.replace("T", " ");
	}

	public void setRecDate(String recDate) {
		RecDate = recDate;
	}

	public int getClassID() {
		return ClassID;
	}

	public void setClassID(int classID) {
		ClassID = classID;
	}

	public int getUserType() {
		return UserType;
	}

	public void setUserType(int userType) {
		UserType = userType;
	}

	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}

	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}

	public int getSMSFlag() {
		return SMSFlag;
	}

	public void setSMSFlag(int sMSFlag) {
		SMSFlag = sMSFlag;
	}

	public String getDestID() {
		return destID;
	}

	public void setDestID(String destID) {
		this.destID = destID;
	}

	public String getJiaobaoID() {
		return JiaobaoID;
	}

	public void setJiaobaoID(String jiaobaoID) {
		JiaobaoID = jiaobaoID;
	}

	public String getReaderList() {
		return ReaderList;
	}

	public void setReaderList(String readerList) {
		ReaderList = readerList;
	}

	public String getReadFlagList() {
		return ReadFlagList;
	}

	public void setReadFlagList(String readFlagList) {
		ReadFlagList = readFlagList;
	}

	public String getTrunToList() {
		return TrunToList;
	}

	public void setTrunToList(String trunToList) {
		TrunToList = trunToList;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public String getChecker() {
		return Checker;
	}

	public void setChecker(String checker) {
		Checker = checker;
	}

	public String getCheckDate() {
		return CheckDate;
	}

	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}

	public String getRecGUID() {
		return RecGUID;
	}

	public void setRecGUID(String recGUID) {
		RecGUID = recGUID;
	}

	public String getAttList() {
		return AttList;
	}

	public void setAttList(String attList) {
		AttList = attList;
	}

	public int getIsAdmin() {
		return IsAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		IsAdmin = isAdmin;
	}

	public String getCityCode() {
		return CityCode;
	}

	public void setCityCode(String cityCode) {
		CityCode = cityCode;
	}

	public String getFeebackList() {
		return FeebackList;
	}

	public void setFeebackList(String feebackList) {
		FeebackList = feebackList;
	}

	public int getPointActionCode() {
		return PointActionCode;
	}

	public void setPointActionCode(int pointActionCode) {
		PointActionCode = pointActionCode;
	}

	public int getTrunToFlag() {
		return TrunToFlag;
	}

	public void setTrunToFlag(int trunToFlag) {
		TrunToFlag = trunToFlag;
	}

	public int getGenReadCount() {
		return GenReadCount;
	}

	public void setGenReadCount(int genReadCount) {
		GenReadCount = genReadCount;
	}

	public int getGenSMSCount() {
		return GenSMSCount;
	}

	public void setGenSMSCount(int genSMSCount) {
		GenSMSCount = genSMSCount;
	}

	public int getMsgType() {
		return MsgType;
	}

	public void setMsgType(int msgType) {
		MsgType = msgType;
	}

	public String getTabIDStr() {
		return TabIDStr;
	}

	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}

}
