package com.jsy.xuezhuli.utils;

import com.jsy_jiaobao.po.sign.ChildSignWay;
import com.jsy_jiaobao.po.sign.ParentSignWay;
import com.jsy_jiaobao.po.sys.UserIdentity;

import java.security.PublicKey;
import java.util.List;

/**
 * @author cai
 * 常量类
 */
public class Constant {
	public static int ScreenWith = 0;
	public static int ScreenHeight = 0;

	public static String SP_TB_USER = "sp_tb_user";
	public static String SP_TB_SYS = "sp_tb_sys";
	static String SPKXmlStr = "<RSAKeyValue><Modulus>xc0EHqiwAGNZn4s+Nr6U7NOSq6H62WBHOPZHwMFrwRMRm55S7R2oIL7vaaDLTg+x10bztPgKrhxor1hQ1uv2NKAx5uqJwsRd4VckhKEWVms6ysQDTy01Jhz9N3SIe2iMibkIuVW/mVSmgLEWHAAKUv4j0pCPSqkmozX6Ur0BAKE=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
	public static PublicKey publicKey = RsaHelper.decodePublicKeyFromXml(SPKXmlStr);

	public static final String YINSI_URL="http://www.jiaobao.net/dl/jiaobaoxiaoyuan/jbxyPrivacy.htm";
	public static final String KNOWN_URL="http://www.jiaobao.net/dl/jiaobaoxiaoyuan/jbxyPrivacy.htm";
	public static final String EMAIL = "kf@jsy8800.com";
	public static final String FANKUI = "如果您有任何建议或问题欢迎给我们发送邮件:"+EMAIL;
	public static final String KNOWN = "依据最新法律要求，我们更新了<a target='_blank' href="+YINSI_URL+">《用户协议与隐私政策》</a> 请您务必审慎阅读,充分理解相关条款内容，特别是字体加粗标识的重要条款。\n"+
			"<br>点击同意即代表您已阅读并同意<a target='_blank' href="+YINSI_URL+">《用户协议与隐私政策》</a>,如果您不同意用户协议与隐私政策的内容，我们暂时将无法为您提供服务\n" +
			"<br>我们会尽力保护您的个人信息安全。";
	public static final String NO_PASS_TEXT = "由于您不同意《用户协议与隐私政策》的相关内容，我们暂时无法为您提供服务。请谅解！";
	public static final String CANCELLATION = "注销账户后，相关信息将无法找回，确定要注销此账户吗？";

	//用户所有身份
	public static List<UserIdentity> listUserIdentity;
	//考勤方式
	public static List<ParentSignWay> listParentSignWay;
	public static ParentSignWay SIGNWAY_P =new ParentSignWay("1","普通考勤");//分组父类
	public static ChildSignWay SIGNWAY_C=new ChildSignWay("5150001","默认");//分组孩子
	//用户所在单位群组

	final public static int msgcenter_checkversion = -1;
	final public static int msgcenter_updataversion = -2;

	final public static int system_login_again = 99;

	final public static int msgcenter_select_position_1 = 101;

	final public static int user_regist_ReSendCheckCode = 212;
	final public static int user_regist_SendCheckCode = 205;
	final public static int user_regist_checkmobileAcc = 204;
	final public static int user_regist_RegCheckMobileVcode = 206;
	final public static int user_regist_CheckMobileVcode = 210;
	final public static int user_regist_RegAccId = 207;
	final public static int user_regist_ResetAccPw = 211;
	final public static int user_regist_hello = 208;
	final public static int user_regist_getTime = 209;
	final public static int user_regist_checkAccN = 213;
	final public static int user_regist_UpateRecAcc = 214;
	final public static int user_regist_ChangePW = 215;
	final public static int user_regist_GetMyMobileUnitList = 216;

	final public static int msgcenter_work_notice = 1;
	final public static int msgcenter_show_notice = 3;
	final public static int msgcenter_getMyUnitInfo = 5;
	final public static int msgcenter_getMyParentUnitInfo = 6;
	final public static int msgcenter_GetMyFriends = -2;

	final public static int msgcenter_select_position = 8;
	final public static int msgcenter_publish_permission = 80;
	final public static int msgcenter_publish_getmyUserClass = 81;
	
	final public static int msgcenter_work_change = 10;
	final public static int msgcenter_work_CommMsgRevicerList = 11;
	final public static int msgcenter_work_CreateCommMsg = 12;
	final public static int msgcenter_work_SMSCommIndex = 13;
	final public static int msgcenter_work_geterror = 14;
	final public static int msgcenter_work_CommMsgRevicerUnitList = 15;
	final public static int msgcenter_work_GetCommPerm = 20;
	final public static int msgcenter_work_GetUnitRevicer = 16;
	final public static int msgcenter_work_GetMsgAllRevicer_toSubUnit = 17;
	final public static int msgcenter_work_GetMsgAllRevicer_toSchoolGen = 19;
	final public static int msgcenter_work_GetMsgAllReviceUnitList = 18;
	
	final public static int msgcenter_work2_FirstWorkDetails = 13;
	final public static int msgcenter_work2_addfeeback = 151;
	final public static int msgcenter_work2_GetMySendMsgList = 16;
	final public static int msgcenter_work2_SendToMeUserList = 17;
	final public static int msgcenter_work2_SendToMeMsgList = 18;
	final public static int msgcenter_work2_CommListToMeAll = 110;
	final public static int msgcenter_work2_CommListToMeUnRead = 111;
	final public static int msgcenter_work2_CommListToMeUnComment = 112;
	final public static int msgcenter_work2_CommListToMeCommented = 113;
	final public static int msgcenter_work2_CommListFromMe = 114;
	
	final public static int msgcenter_worksend_getUnitGroups = 115;
	final public static int msgcenter_worksend_GetUnitClassRevicer = 117;
	final public static int msgcenter_worksend_GetUnitRevicer_otherunit = 118;
	final public static int msgcenter_worksend_SendBtnClicked = 119;

	final public static int msgcenter_notice_getMySubUnitInfo = 23;
	final public static int msgcenter_notice_getSchoolClassInfo = 24;
	final public static int msgcenter_notice_ArthListIndex = 25;
	final public static int msgcenter_notice_ShowArthDetail = 26;
	final public static int msgcenter_notice_GetArthInfo = 36;
	final public static int msgcenter_notice_uploadSectionImg = 28;
	final public static int msgcenter_notice_uploadSectionAudio = 36;
	final public static int msgcenter_notice_uploadSectionVideo = 37;
	final public static int msgcenter_notice_savepublishArticle = 29;
	final public static int msgcenter_notice_changeUnit = 30;
	final public static int msgcenter_notice_getSectionMessageNew = 32;
	final public static int msgcenter_notice_getSectionMessageSuggest = 33;

	final public static int msgcenter_article_LikeIt = 1;
	final public static int msgcenter_article_addComment = 2;
	final public static int msgcenter_articlelist_addComment = 200;
	final public static int msgcenter_article_CommentsList = 3;
	final public static int msgcenter_article_AddScore = 4;
	final public static int msgcenter_article_AddScore_like = 5;
	final public static int msgcenter_article_AddScore_cai = 6;
	final public static int msgcenter_article_AddScore_like_ref = 8;
	final public static int msgcenter_article_AddScore_cai_ref = 9;
	final public static int msgcenter_article_AddScore_callback = 7;
	final public static int msgcenter_article_click_reply = 10;

	final public static int msgcenter_show_TopArthListIndex = 40;
	final public static int msgcenter_show_ArthListIndex = 41;
	final public static int msgcenter_show_UnitArthListIndex = 141;
	final public static int msgcenter_show_UnitArthListIndex_more = 140;
	final public static int msgcenter_show_PersonArthListIndex = 142;
	final public static int msgcenter_show_ShowingUnitArthListLocal = 143;
	final public static int msgcenter_show_ShowingUnitArthListAll = 144;
	final public static int msgcenter_show_MyAttUnitArthListIndex = 145;
	final public static int msgcenter_show_AllMyClassArthList_1 = 146;
	final public static int msgcenter_show_AllMyClassArthList_2 = 147;
	final public static int msgcenter_show_AllMyClassArthList_more = 148;
	final public static int msgcenter_show_AllMyClassArthList_more_select = 150;
	final public static int msgcenter_show_SchoolArthListIndex = 42;
	final public static int msgcenter_show_getmyUserClass = 43;
	final public static int msgcenter_show_getSectionMessageSuggest = 44;
	final public static int msgcenter_show_getSectionMessageNew = 45;
	final public static int msgcenter_show_getSchoolClassInfo = 46;
	final public static int msgcenter_show_getUnitSectionMessages = 47;
	final public static int msgcenter_show_getintroduce = 51;
	final public static int msgcenter_show_GetMyAttUnit = 52;
	
	final public static int msgcenter_train_getUnitNotics = 56;
	final public static int msgcenter_train_changeUnit = 58;
	final public static int msgcenter_train_getUserInfo = 59;
	final public static int msgcenter_train_getmyUserClass = 60;
	
	final public static int msgcenter_chat_getUnitGroups = 70;
	final public static int msgcenter_chat_getClassStdInfo = 73;
	final public static int msgcenter_chat_getUserInfoByUnitID = 75;
	
	final public static int msgcenter_unitspace_GetUnitPGroup = 1;
	final public static int msgcenter_unitspace_GetUnitPhotoByGroupID = 2;
	final public static int msgcenter_personalspace_GetPhotoList = 3;
	final public static int msgcenter_personalspace_GetNewPhoto = 4;
	final public static int msgcenter_unitspace_GetPhotoByGroup = 5;
	final public static int msgcenter_unitspace_GetUnitNewPhoto = 6;
	final public static int msgcenter_unitspace_AddPhotoGroup = 7;
	final public static int msgcenter_unitspace_CreateUnitPhotoGroup = 8;
	
	final public static int appcenter_location_success = 1;
	final public static int appcenter_gallery_GetPhotoList = 1;
	final public static int appcenter_gallery_UpLoadPhoto = 2;
	final public static int appcenter_gallery_UpLoadPhotofailed = 3;

	final public static int sturecord_home_BaseInfo = 1;
	final public static int sturecord_home_PackMsgSch_sch = 20;
	final public static int sturecord_home_PackMsg_sch = 22;
	final public static int sturecord_home_StuMsgSch_sch = 21;
	final public static int sturecord_home_StuMsg_sch = 23;
	
	final public static int sturecord_home_PackMsgSch_clas = 30;
	final public static int sturecord_home_StuMsgSch_clas = 31;
	final public static int sturecord_home_PackMsg_clas = 32;
	final public static int sturecord_home_StuMsg_clas = 33;
	
	final public static int sturecord_home_PackMsgSch_daily = 40;
	final public static int sturecord_home_StuMsgSch_daily = 41;
	final public static int sturecord_home_PackMsg_daily = 42;
	final public static int sturecord_home_StuMsg_daily = 43;
	
	final public static int sturecord_home_StuQpSch = 50;
	final public static int sturecord_home_PackQpSch = 51;
	final public static int sturecord_home_StuQp = 52;
	final public static int sturecord_home_PackQp = 53;
	
	final public static int sturecord_home_StuTecWSch = 60;
	final public static int sturecord_home_PackTecWSch = 61;
	final public static int sturecord_home_StuTecW = 62;
	final public static int sturecord_home_PackTecW = 63;
	
	final public static int sturecord_home_PackGenW = 70;
	
	final public static int msgcenter_qiuzhi_UserIndexQuestion = 301;
	final public static int msgcenter_qiuzhi_GetAllCategory = 302;
	final public static int msgcenter_qiuzhi_QuestionIndex = 303;
	final public static int msgcenter_qiuzhi_GetCategory = 304;
	final public static int msgcenter_qiuzhi_QuestionDetail = 305;
	final public static int msgcenter_qiuzhi_GetAnswerById = 306;
	final public static int msgcenter_qiuzhi_AddAnswer = 307;
	final public static int msgcenter_qiuzhi_uploadSectionImg = 308;
	final public static int msgcenter_qiuzhi_CategoryIndexQuestion = 309;
	final public static int msgcenter_qiuzhi_AnswerDetail = 310;
	final public static int msgcenter_qiuzhi_CommentsList = 311;
	final public static int msgcenter_qiuzhi_ReportAns = 312;
	final public static int msgcenter_qiuzhi_SetYes = 313;
	final public static int msgcenter_qiuzhi_SetNo = 314;
	final public static int msgcenter_qiuzhi_AddComment = 315;
	final public static int msgcenter_qiuzhi_RecommentIndex = 316;
	final public static int msgcenter_qiuzhi_ShowRecomment = 317;
	final public static int msgcenter_qiuzhi_NewQuestion = 318;
	final public static int msgcenter_qiuzhi_GetAccIdbyNickname = 319;
	final public static int msgcenter_qiuzhi_GetProvice = 320;
	final public static int msgcenter_qiuzhi_GetCity = 321;
	final public static int msgcenter_qiuzhi_GetCounty = 322;
	final public static int msgcenter_qiuzhi_GetCategoryTopQ = 323;
	final public static int msgcenter_qiuzhi_GetPickedById = 324;
	final public static int msgcenter_qiuzhi_PickedIndex = 325;
	final public static int msgcenter_qiuzhi_ShowPicked = 326;
	final public static int msgcenter_qiuzhi_GetPickedById_pd = 327;
	final public static int del_user = 88888;
	final public static int msgcenter_qiuzhi_AddMyAttQ = 328;
	final public static int msgcenter_qiuzhi_AtMeForAnswer = 329;
	final public static int msgcenter_qiuzhi_MyAttQIndex = 330;
	final public static int msgcenter_qiuzhi_AtMeQIndex = 331;
	final public static int msgcenter_qiuzhi_MyQuestionIndex = 332;
	final public static int msgcenter_qiuzhi_MyAnswerIndex = 333;
	final public static int msgcenter_qiuzhi_GetMyattCate = 334;
	final public static int msgcenter_qiuzhi_UpdateAnswer = 335;
	final public static int msgcenter_qiuzhi_RemoveMyAttQ = 336;
	final public static int msgcenter_qiuzhi_AddMyattCate = 337;
	final public static int msgcenter_qiuzhi_GetAtMeUsers = 338;
	final public static int msgcenter_qiuzhi_GetMyComms = 339;
	final public static int msgcenter_qiuzhi_AddScoreKnl = 340;	
}
