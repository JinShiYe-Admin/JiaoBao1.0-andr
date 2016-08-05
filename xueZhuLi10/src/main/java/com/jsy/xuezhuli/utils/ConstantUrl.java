package com.jsy.xuezhuli.utils;

/**
 * 接口常量
 */
public interface ConstantUrl {
	// url
	public static String url = "http://192.168.2.100:8080/";

	public static String jsyoa = "http://www.jiaobao.net/jbclient";
	// public static String jsyoa = "http://www.jiaobao.net/jbclient";
	// ACache.get(mContext.getApplicationContext()).getAsString("KaoQUrl")
	// public static String signurl = "http://58.56.66.215:8084/";
	// ACache.get(mContext.getApplicationContext()).getAsString("RiCUrl")
	// public static String dailyworkurl = "http://58.56.66.215:8082/";

	// 学生档案http://218.98.32.200:8083/HOME/DataGet?CMD=BaseInfo&DATA=5150001|2|991
	public static String StuRecordUrl = "http://218.98.32.200:8083/";
	public static String StuRecordDataGet = StuRecordUrl + "HOME/DataGet";
	/**
	 * 服务器时间
	 */
	public static String getcurTime = jsyoa + "/Account/getcurTime";
	/**
	 * 头像
	 */
	public static String photoURL = "/ClientSrv/getfaceimg";
	/**
	 * 位头像单
	 */
	public static String getUnitlogo = "/ClientSrv/getUnitlogo";
	/**
	 * 主题头像
	 */
	public static String getlogoimg = "/Unit/getlogoimg";
	/** 上传 **/
	public static String updatefaceimg = "/ClientSrv/updatefaceimg";
	/**
	 * 获取主接口地址
	 */
	public static String sys_SRVUrl = jsyoa + "/Account/getSRVUrl";
	/** 登录互联应用 **/
	public static String checkToken = "/Account/checkToken";// http://www.jb.edu8800.com/jbapp/http://www.jb.edu8800.com/JBApp
	/**
	 * 获取融云token
	 */
	public static String getRongYunToken = jsyoa + "/RongYun/getToken";// http://www.jb.edu8800.com/JBApp
	/**
	 * 注册客户端
	 */
	public static String sys_regClient = jsyoa + "/Account/Reg";

	/**
	 * 通讯握手
	 */
	public static String sys_hello = jsyoa + "/Account/hello";

	/**
	 * 用户登录
	 */
	public static String user_login = jsyoa + "/Account/login";

	/**
	 * 获取令牌
	 */
	public static String getToken = jsyoa + "/Account/getToken";

	/**
	 * 获取用户身份信息
	 */
	public static String user_getRoleIdentity = jsyoa
			+ "/Account/getRoleIdentity";
	/**
	 * 获取用户信息
	 */
	public static String user_getUserInfo = jsyoa + "/Account/getUserInfo";
	/**
	 * 获取家长信息
	 */
	public static String getGenInfo = jsyoa + "/Account/getGenInfo";
	/**
	 * 获取学生信息
	 */
	public static String getStuInfo = jsyoa + "/Account/getStuInfo";
	/**
	 * 用户切换单位
	 */
	public static String changeCurUnit = jsyoa + "/Account/changeCurUnit";
	/**
	 * 用户注销
	 */
	public static String user_logout = jsyoa + "/Account/logout";
	/**
	 * 最新版本
	 */
	public static String AndroidCurrVersionInfo = "http://www.jiaobao.net/DL/AndroidCurrVersionInfo.txt";
	/**
	 * 取关联的班级
	 */
	public static String getmyUserClass = jsyoa + "/Account/getmyUserClass";
	/**
	 * 获取指定班级的所有学生数据列表
	 */
	public static String getClassStdInfo = jsyoa + "/Basic/getClassStdInfo";
	/**
	 * 获取指定班级的所有家长数据列表
	 */
	public static String getClassGenInfo = jsyoa + "/Basic/getClassGenInfo";
	/**
	 * 检查手机是否重复 功能：检查手机号码是否重复（已注册），true没有注册，false有注册
	 */
	public static String checkmobileAcc = jsyoa + "/AccountReg/checkmobileAcc";
	/** 服务器生成一个4位数字的验证码，并返回包含此数字的图片url。每获取一次都生成不同的数字。 */
	public static String GetValidateCode = jsyoa
			+ "/AccountReg/GetValidateCode";
	/** 用户输入手机号码，并输入图片验证码，app客户端把图片验证码和手机发回服务器，服务器验证后向手机发送验证码（6位数字）。 */
	public static String SendCheckCode = jsyoa + "/AccountReg/SendCheckCode";
	/** 重置密码时 验证账号是否存在 */
	public static String ReSendCheckCode = jsyoa
			+ "/AccountReg/ReSendCheckCode";
	/** 用户输入收到的验证码，并输入图片验证码，app客户端把图片验证码和手机验证码发回服务器，检查用户输入是否正确 */
	public static String RegCheckMobileVcode = jsyoa
			+ "/AccountReg/RegCheckMobileVcode";
	/** 用于在重置密码时验证用户手机（与注册时验证手机的接口不一样）。 */
	public static String CheckMobileVcode = jsyoa
			+ "/AccountReg/CheckMobileVcode";
	/** 验证手机号码正确后，用户输入输入密码，app把用户注册手机和密码发给服务器完成注册手续。 */
	public static String RegAccId = jsyoa + "/AccountReg/RegAccId";
	/** 直接重置帐户密码（不是修改密码，无须验证旧密码），参数须加密 */
	public static String ResetAccPw = jsyoa + "/AccountReg/ResetAccPw";
	/** 在修改昵称时检查昵称是否重复 */
	public static String checkAccN = jsyoa + "/AccountReg/checkAccN";
	/** 修改帐户信息的昵称和姓名 */
	public static String UpateRecAcc = jsyoa + "/AccountReg/UpateRecAcc";
	/** 验证旧密码后修改帐户密码（不是重置密码），参数须加密 */
	public static String ChangePW = jsyoa + "/AccountReg/ChangePW";
	/** 根据手机号码自动配置的用户信息，获取用户所在单位及身份信息，每一条用户信息匹配一条记录。如果在一个单位有相同的身份，则会显示两条相同的记录。 */
	public static String GetMyMobileUnitList = jsyoa
			+ "/AccountReg/GetMyMobileUnitList";
	/** 加入单位操作 */
	public static String JoinUnitOP = jsyoa + "/AccountReg/JoinUnitOP";
	/********************* 移动互联应用 ****************************/
	// //////////////////////////////交流
	/** 取单位所有组 */
	public static String getUnitGroups = jsyoa + "/Basic/getUnitGroups";
	/** 取单位内所有人员 **/
	public static String getUserInfoByUnitID = jsyoa
			+ "/Basic/getUserInfoByUnitID";
	/** 取组内成员 **/
	public static String getUserInfoByGroupID = jsyoa
			+ "/Basic/getUserInfoByGroupID";
	/** 取好友组 **/
	public static String GetMyGroups = "/LGQIFriends/GetMyGroups";
	/** 取所有好友 **/
	public static String GetMyFriends = "/LGQIFriends/GetMyFriends";
	/** 取关注组 **/
	public static String GetMyAttGroups = "/LGQIFriends/GetMyAttGroups";
	/** 取所有关注人 **/
	public static String GetMyAttFriends = "/LGQIFriends/GetMyAttFriends";
	// //////////////////////////////事务
	/** 交流信息 发给我 功能：取发给我的交流信息 **/
	public static String CommListToMe = jsyoa + "/CommMsg/CommListToMe";
	/** 交流信息 回复我的 功能：取回复我的交流信息。 **/
	public static String FeebackToMe = jsyoa + "/CommMsg/FeebackToMe";
	/** 交流信息 我发的信息 功能：取我发的交流信息。 **/
	public static String MySend = jsyoa + "/CommMsg/MySend";
	/** 获取我发送的消息列表，列表中包括回复数量，未读回复数量经。比原接口简化了返回的字段且不加密 **/
	public static String GetMySendMsgList = jsyoa + "/CommMsg/GetMySendMsgList";
	/** 取发给我消息的用户列表。包括最新一条消息内容，以及该用户发给我的未读消息数量，未回复数量 **/
	public static String SendToMeUserList = jsyoa + "/CommMsg/SendToMeUserList";
	/** 取发给我消息的用户列表。包括最新一条消息内容，以及该用户发给我的未读消息数量，未回复数量 **/
	public static String SendToMeMsgList = jsyoa + "/CommMsg/SendToMeMsgList";
	/**
	 * 交流信息 显示交流信息明细 功能：显示交流信息明细，可以从发给我信息中查看明细，从我发送的信息列表中查看明细，也可以从我回复的列表中查看明细，
	 * 可以从回复我的信息列表查看明细。不同场景调用接口的参数有所不同。
	 **/
	public static String ShowDetail = jsyoa + "/CommMsg/ShowDetail";
	/**
	 * 交流信息 显示交流信息明细 功能：显示交流信息明细，可以从发给我信息中查看明细，从我发送的信息列表中查看明细，也可以从我回复的列表中查看明细，
	 * 可以从回复我的信息列表查看明细。不同场景调用接口的参数有所不同。
	 **/
	public static String ShowDetail2 = jsyoa + "/CommMsg/ShowDetail2";
	/** 交流信息 回复交流信息标记为已读 **/
	public static String MarkRead = jsyoa + "/CommMsg/MarkRead";
	/** 交流信息 回复交流信息 功能：对发给我的交流信息添加回复 **/
	public static String addfeeback = jsyoa + "/CommMsg/addfeeback";
	/** 交流信息 发表交流信息 功能：发表交流信息。 **/
	public static String CreateCommMsg = jsyoa + "/CommMsg/CreateCommMsg";
	/** 交流信息 获取交流信息接收人列表 功能： 根据用户当前所在单位，获取交流信息接收人列表。用户可以切换单位，然后重新获取接收人列表。 **/
	public static String CommMsgRevicerList = jsyoa
			+ "/CommMsg/CommMsgRevicerList";
	public static String CommMsgRevicerUnitList = jsyoa
			+ "/CommMsg/CommMsgRevicerUnitList";
	/**
	 * 取发送事务权限
	 * 功能：当用户的身份为单位人员或老师时，需要判定用户是否有发送上级单位事务，本单位事务，下级单位事务的权限，如果用户不具备相应的权限，
	 * 在app界面不要提供相应功能
	 * 。这里指的新建事务的权限（权限对象为单位人员和老师），阅读和回复的权限人人都有。班级事务（家校沟通的权限）只要有关联的班级就可以使用，不受限制。
	 */
	public static String GetCommPerm = jsyoa + "/CommMsg/GetCommPerm";
	/** 功能：获取单位接收人 */
	public static String GetUnitRevicer = jsyoa + "/CommMsg/GetUnitRevicer";
	/** 获取班级接收人 */
	public static String GetUnitClassRevice = jsyoa
			+ "/CommMsg/GetUnitClassRevicer";
	/** 功能：群发给下属单位的接收者，返回所有下属单位的管理员列表 */
	public static String GetMsgAllRevicer_toSubUnit = jsyoa
			+ "/CommMsg/GetMsgAllRevicer_toSubUnit";
	/** 获取群发家长的接收对象 功能：群发给下属单位的接收者，返回所有下属单位的管理员列表 */
	public static String GetMsgAllRevicer_toSchoolGen = jsyoa
			+ "/CommMsg/GetMsgAllRevicer_toSchoolGen";
	/**
	 * 获取群发权限
	 * 功能：当前用户是否有群发给下属单位单位人员事务信息（下属学校的家长）或群发信息给本校家长的权限，该事务信息分两类接收人：群发给下属单位
	 * （单位人员），群发给家长
	 */
	public static String GetMsgAllReviceUnitList = jsyoa
			+ "/CommMsg/GetMsgAllReviceUnitList";

	/** 交流信息 获得当前未读交流信息数量 功能： 获得当前未读交流信息数量 **/
	public static String getNoReadeCount = jsyoa + "/CommMsg/getNoReadeCount";
	/** 交流信息 获得当前未读回复数量 功能：获得当前未读回复数量 **/
	public static String getfbtomecount = jsyoa + "/CommMsg/getfbtomecount";
	/**
	 * 交流信息获取短信直通车接收单位树数据源<br>
	 * 功能：根据用户当前所在单位，获取短信直通车接收单位树数据源（包括本单位及所有下级单位，跨级下级单位）。用户可以切换单位，
	 * 然后重新获取短信直通车接收单位树数据源
	 **/
	public static String SMSCommIndex = jsyoa + "/CommMsg/SMSCommIndex";

	// ////////////////////////////////////文章
	/** 取个人分享最新和推荐文章 **/
	public static String ShareingArthList = jsyoa
			+ "/Sections/ShareingArthList";
	/** 功能：对文章进行点赞操作。一篇文章一个用户 （以教宝号为准）只能赞一次。 **/
	public static String LikeIt = jsyoa + "/Sections/LikeIt";
	/** 功能：发表评论。发表评论可以在别人评论的基础上再次评论，称为引用评论。 **/
	public static String addComment = jsyoa + "/Sections/addComment";
	/** 功能：获取文章评论列表。 **/
	public static String CommentsList = jsyoa + "/Sections/CommentsList";
	/** 功能：对评论进行顶或踩的操作。 **/
	public static String AddScore = jsyoa + "/Sections/AddScore";
	/** 取栏目最新和推荐文章 **/
	public static String TopArthListIndex = jsyoa
			+ "/Sections/TopArthListIndex";
	/** 取我相关单位的列表及栏目未读文章数量，功能：单位列表包括上级单位和我所在单位，去重后的单位列表 **/
	public static String getUnitSectionMessages = jsyoa
			+ "/Sections/getUnitSectionMessages";
	/** 取栏目最新和推荐文章 未读消息数量 **/
	public static String getSectionMessage = jsyoa
			+ "/Sections/getSectionMessage";
	/** 取本单位基础信息 **/
	public static String getMyUnitInfo = jsyoa + "/Basic/getMyUnitInfo";
	/** 取本单位的所有上级单位基础信息 **/
	public static String getMyParentUnitInfo = jsyoa
			+ "/Basic/getMyParentUnitInfo";
	/** 取本单位的所有下级单位基础信息 **/
	public static String getMySubUnitInfo = jsyoa + "/Basic/getMySubUnitInfo";
	/** 获取指定学校的所有班级基础数据 **/
	public static String getSchoolClassInfo = jsyoa
			+ "/Basic/getSchoolClassInfo";
	/** 取本单位栏目文章 **/
	public static String ArthListIndex = jsyoa + "/Sections/ArthListIndex";
	/** 取本单位栏目文章 **/
	public static String UnitArthListIndex = jsyoa
			+ "/Sections/UnitArthListIndex";
	/** 取本单位栏目文章 详情 **/
	public static String ShowArthDetail = jsyoa + "/Sections/ShowArthDetail";
	/** 功能：获取文章附加信息，同时执行积分，已读等处理操作 **/
	public static String GetArthInfo = jsyoa + "/Sections/GetArthInfo";
	/** 发布文章 **/
	public static String savepublishArticle = jsyoa
			+ "/Sections/savepublishArticle";
	/** 上传图片 **/
	public static String uploadSectionImg = jsyoa
			+ "/ClientUpLoadFile/uploadSectionImg";
	/** 获取单位通知 **/
	public static String getUnitNotics = jsyoa + "/Sections/getUnitNotics";
	/** 获取单位通知详细信息 **/
	public static String ShowNoticDetail = jsyoa + "/Sections/ShowNoticDetail";
	/**
	 * 功能：取最新更新文章单位信息,（包含单位分享和单位展示），本地指的当前登录用户的默认单位所在地，默认单位不是切换的所在单位。
	 * 是指在系统中设置的默认单位，
	 **/
	public static String UpdateUnitList = jsyoa + "/Sections/UpdateUnitList";
	/** 取最新或推荐单位栏目文章 **/
	public static String ShowingUnitArthList = jsyoa
			+ "/Sections/ShowingUnitArthList";
	/** 取我关注的单位栏目文章 功能：取我关注单位的文章列表,（包含官方文章和非官方文章,即单位展示和单位分享） **/
	public static String MyAttUnitArthListIndex = jsyoa
			+ "/Sections/MyAttUnitArthListIndex";
	/** 我的班级文章列表 功能：取我所在班级（所有的班级）的所有文章列表，注意取时是按当前身份来获取的。所以要注意切换单位。 **/
	public static String AllMyClassArthList = jsyoa
			+ "/Sections/AllMyClassArthList";
	/** 取单位简介 **/
	public static String getintroduce = "/ClientSrv/getintroduce";
	/** 取同事，关注人，好友的分享文章 功能：同事，关注人，好友的分享文章，按时间先后顺序返回记录。 **/
	public static String myShareingArth = jsyoa + "/Sections/myShareingArth";
	/** 功能：取最新更新文章的主题列表 **/
	public static String UpdatedInterestList = jsyoa
			+ "/Sections/UpdatedInterestList";
	/** 功能：取我关注的和我所参与的主题 **/
	public static String EnjoyInterestList = jsyoa
			+ "/Sections/EnjoyInterestList";
	/** 功能：获取当前用户可以发布动态的单位列表(含班级），如果返回结果为空，则不能在任何单位发布动态。 **/
	public static String GetReleaseNewsUnits = jsyoa
			+ "/Sections/GetReleaseNewsUnits";
	// 功能：主题的关注操作
	/** 功能：是否关注。 */
	public static String ExistAtt = jsyoa + "/Interest/ExistAtt";
	/** 功能：取消关注 */
	public static String RemoveAtt = jsyoa + "/Interest/RemoveAtt";
	/** 功能：加关注 */
	public static String AddAtt = jsyoa + "/Interest/AddAtt";

	/********************* 签到start *****************************/
	/** 签到 **/
	public static String user_sign = "InterAppMobileInterface/MobileCreateSignIn";
	/** 获取签到方式 **/
	public static String getSignWay = "InterAppMobileInterface/GetSignInGroupByUnitID";
	/** 查询签到信息 **/
	public static String user_select = "InterAppMobileInterface/GetSignInListForMobile";
	/** 查询单位围栏 **/
	public static String fance_select = "InterAppSignInAddress/GetSignInAddJsonData";

	/********************** 日程start ************************/
	/** 获取可延迟时间 **/
	public static String getDiffence = "WorkPlanMobileInterface/WorkPlanGetReportedDelayDateByUnitID";
	/** 提交日程 **/
	public static String commitWorkPlan = "WorkPlanMobileInterface/WorkPlanAddContent";
	public static String selectWorkPlanMonth = "WorkPlanMobileInterface/WorkPlanSelectContentByMonth";
	public static String selectWorkPlanDay = "WorkPlanMobileInterface/GetWorkPlanInfoByUnitIDUserIDDate";
	/** 获取组 **/
	public static String getUserGroup = jsyoa + "/Basic/getGroupInfoByUserID";
	/************************ 相册 **************************/
	/** 功能：获取某单位中的相册 **/
	public static String GetUnitPGroup = "/LGQIPhotoInfo/GetUnitPGroup";
	/** 功能：获相册列表 **/
	public static String GetPhotoList = "/LGQIPhoto/GetPhotoList";
	/** 功能：获取单位相册的照片 **/
	public static String GetUnitPhotoByGroupID = "/LGQIPhotoInfo/GetUnitPhotoByGroupID";
	/** 功能：获取danwei相册的最新照片(int UnitID,int count) **/
	public static String GetUnitNewPhoto = "/LGQIPhotoInfo/GetUnitNewPhoto";
	/** 功能：获取相册的最新照片 **/
	public static String GetNewPhoto = "/LGQIPhoto/GetNewPhoto";
	/** 功能：获取相册的所有照片 **/
	public static String GetPhotoByGroup = "/LGQIPhoto/GetPhotoByGroup";
	/** 获取我的关注的单位 **/
	public static String GetMyAttUnit = "/LGQIFriends/GetMyAttUnit";
	/** 获取我的相册封面int JiaoBaoHao, string GroupInfo) **/
	public static String GetFristPhotoByGroup = "/LGQIPhoto/GetFristPhotoByGroup";
	/** 获取单位相册封面(int UnitID, int GroupID) **/
	public static String GetUnitFristPhotoByGroupID = "/LGQIPhotoInfo/GetUnitFristPhotoByGroupID";
	/** 创建单位相册 **/
	public static String CreateUnitPhotoGroup = "/LGQIPhotoInfo/CreateUnitPhotoGroup";
	/** 个人空间上传图片 **/
	public static String UpLoadPhotoFromAPP = "/LGQIPhoto/UpLoadPhotoFromAPP";
	/** 单位相册上传照片 **/
	public static String UpLoadPhotoUnit = "/LGQIPhotoInfo/UpLoadPhotoUnit";
	/** 个人空间添加相册 **/
	public static String AddPhotoGroup = "/LGQIPhoto/AddPhotoGroup";

	/************** 求知 *********************/
	/** 功能：取用户信息 */
	public static String GetUserInfo = jsyoa + "/Knl/GetUserInfo";
	/** 功能：取所有话题 */
	public static String GetAllCategory = jsyoa + "/Knl/GetAllCategory";
	/** 功能：获取问题列表（首页） */
	public static String QuestionIndex = jsyoa + "/Knl/QuestionIndex";
	/** 功能：获取推荐列表（首页） */
	public static String RecommentIndex = jsyoa + "/Knl/RecommentIndex";
	/** 功能：获取精选列表（首页通过指定ID获取一个精选问题集或最新的一个精选问题集） */
	public static String GetPickedById = jsyoa + "/Knl/GetPickedById";
	/** 功能：获取各期精选列表 */
	public static String PickedIndex = jsyoa + "/Knl/PickedIndex";
	/** 功能：通过指定ID获取一个精选问题内容的明细 */
	public static String ShowPicked = jsyoa + "/Knl/ShowPicked";
	/** 功能：获取指定话题ID的置顶问题。每一个话题只有一个置顶问题，如果该问题状态不可用（删除或屏蔽)或没有置顶问题，则返回的数组为空 */
	public static String GetCategoryTopQ = jsyoa + "/Knl/GetCategoryTopQ";
	/** 功能：获取单个推荐详情 */
	public static String ShowRecomment = jsyoa + "/Knl/ShowRecomment";
	/** 功能：关注某一问题 */
	public static String AddMyAttQ = jsyoa + "/Knl/AddMyAttQ";
	/** 功能：取消关注某一问题 */
	public static String RemoveMyAttQ = jsyoa + "/Knl/RemoveMyAttQ";
	/** 功能：邀请指定的用户回答问题 */
	public static String AtMeForAnswer = jsyoa + "/KnUser/AtMeForAnswer";
	/** 功能：.获区首页问题列表，按要求排序和包含一个回答。 */
	public static String UserIndexQuestion = jsyoa + "/Knl/UserIndexQuestion";
	/** 功能：获取指定话题ID的问题列表，按提问时间排序和包含一个回答。返回结果包含本话题及其子级话题的问题 */
	public static String CategoryIndexQuestion = jsyoa
			+ "/Knl/CategoryIndexQuestion";
	/** 功能：取系统话题列表。话题分为多级，一级话题的父Id为0。 */
	public static String GetCategory = jsyoa + "/Knl/GetCategory";
	/** 功能：获取回答xiang'qi。 */
	public static String QuestionDetail = jsyoa + "/Knl/QuestionDetail";
	/** 功能：修改答案。 */
	public static String UpdateAnswer = jsyoa + "/Knl/UpdateAnswer";
	/** 功能：取系统话题列表。话题分为多级，一级话题的父Id为0。 */
	public static String GetAnswerById = jsyoa + "/Knl/GetAnswerById";
	/** 功能：回答问题。 */
	public static String AddAnswer = jsyoa + "/Knl/AddAnswer";
	/** 功能：获取一个答案明细信息，包括问题内容。 */
	public static String AnswerDetail = jsyoa + "/Knl/AnswerDetail";
	/** 功能：获取答案的评论 列表。 */
	public static String KnlCommentsList = jsyoa + "/Knl/CommentsList";
	/** 功能：举报回答含有非法内容，如果超过一定人数举报答案，则系统自动屏蔽该答案; */
	public static String ReportAns = jsyoa + "/Knl/ReportAns";
	/** 功能：评价答案，支持或反对; */
	public static String SetYesNo = jsyoa + "/Knl/SetYesNo";
	/** 功能：对答案添加评论; */
	public static String AddComment = jsyoa + "/Knl/AddComment";
	/** 功能：对评论进行顶或踩的操作 */
	public static String AddScoreKnl = jsyoa + "/Knl/AddScore";
	/** 功能：发布问题 */
	public static String NewQuestion = jsyoa + "/Knl/NewQuestion";
	/** 功能：通过昵称获取教宝号，昵称是一个数组参数。可以一次查询多个昵称 */
	public static String GetAccIdbyNickname = jsyoa + "/Knl/GetAccIdbyNickname";
	/** 功能：去系统中省份信息 */
	public static String GetProvice = jsyoa + "/Basic/GetProvice";
	/** 功能：取指定省份的地市数据或取指定地市的区县数据 */
	public static String GetCity = jsyoa + "/Basic/GetCity";
	/** 功能：获取我关注的问题列表 */
	public static String MyAttQIndex = jsyoa + "/KnUser/MyAttQIndex";
	/** 功能：获取邀请我回答的问题列表 */
	public static String AtMeQIndex = jsyoa + "/KnUser/AtMeQIndex";
	/** 功能：获取我提出的问题列表 */
	public static String MyQuestionIndex = jsyoa + "/KnUser/MyQuestionIndex";
	/** 功能：获取我回答的问题列表 */
	public static String MyAnswerIndex = jsyoa + "/KnUser/MyAnswerIndex";
	/** 功能：取我关注的话题ID数组 */
	public static String GetMyattCate = jsyoa + "/KnUser/GetMyattCate";
	/** 功能：更新我关注的话题 */
	public static String AddMyattCate = jsyoa + "/KnUser/AddMyattCate";
	/** 功能：邀请人回答时，获取回答该话题问题最多的用户列表（4个） */
	public static String GetAtMeUsers = jsyoa + "/KnUser/GetAtMeUsers";
	/** 功能：获取我的评论列表 */
	public static String GetMyComms = jsyoa + "/Knl/GetMyComms";
	/** 功能：获取我的日积分 */
	public static String GetMyPointsDay = jsyoa + "/Knl/GetMyPointsDay";
	/** 功能：获取我的月积分 */
	public static String GetMyPointsMonth = jsyoa + "/Knl/GetMyPointsMonth";
	/*
	 * 请假
	 */
	/** 请假系统主入口 */
	public static String leave = jsyoa + "/Leave";
	/** 功能：单位请假设置 */
	public static String GetLeaveSetting = leave + "/GetLeaveSetting";
	/** 功能：保存一条新增请假记录 */
	public static String NewLeavelModel = leave + "/NewLeaveModel";
	/** 功能：更新一条请假记录 */
	public static String UpdateLeaveModel = leave + "/UpdateLeaveModel";
	/** 给一个假条新增一个时间段 */
	public static String AddLeaveTime = leave + "/AddLeaveTime";
	/** 更新假条的一个时间段 */
	public static String UpdateLeaveTime = leave + "/UpdateLeaveTime";
	/** 删除假条的一个时间段 */
	public static String DeleteLeaveTime = leave + "/DeleteLeaveTime";
	/** 删除假条 */
	public static String DeleteLeaveModel = leave + "/DeleteLeaveModel";
	/** 获取我提出的请假记录 */
	public static String GetMyLeaves = leave + "/GetMyLeaves";
	/** 获取一个假条的详细信息 */
	public static String GetLeaveModel = leave + "/GetLeaveModel";
	/** 班主任获取本班学生请假记录 */
	public static String GetClassLeaves = leave + "/GetClassLeaves";
	/** 审核人员取本单位的请假记录 */
	public static String GetUnitLeaves = leave + "/GetUnitLeaves";
	/** 门卫取请假记录 */
	public static String GetGateLeaves = leave + "/GetGateLeaves";
	/** 审批假条 */
	public static String CheckLeaveModel = leave + "/CheckLeaveModel";
	/** 门卫登记离校返校时间 */
	public static String UpdateGateInfo = leave + "/UpdateGateInfo";
	/** 家长获取关联学生列表 */
	public static String GetMyStdInfo = jsyoa + "/Account/GetMyStdInfo";
	/** 班主任获取管理班级列表 */
	public static String GetMyAdminClass = jsyoa + "/Account/GetMyAdminClass";
	/** 获取所有年级班级 */
	public static String getunitclass = jsyoa + "/basic/getunitclass";
	/** 获取教职工查询统计 */
	public static String GetManSumLeaves = leave + "/GetManSumLeaves";
	/** 获取学生查询统计 */
	public static String GetStudentSumLeaves = leave + "/GetStudentSumLeaves";
	/** 获取班级统计 */
	public static String GetClassSumLeaves = leave + "/GetClassSumLeaves";
}
