package com.jsy.xuezhuli.utils;

/**
 * 接口常量
 */
public interface ConstantUrl {
    // url
    String url = "http://192.168.2.100:8080/";
    String jsyoa = "http://www.jiaobao.net/jbclient";
    String QuickSignInUrl = "https://www.jiaobaowang.net/JBClient";
    String StuRecordUrl = "http://218.98.32.200:8083/";
    String StuRecordDataGet = StuRecordUrl + "HOME/DataGet";
    /**
     * 服务器时间
     */
    String getcurTime = jsyoa + "/Account/getcurTime";
    /**
     * 头像
     */
    String photoURL = "/ClientSrv/getfaceimg";
    /**
     * 位头像单
     */
    String getUnitlogo = "/ClientSrv/getUnitlogo";
    /**
     * 上传
     **/
    String updatefaceimg = "/ClientSrv/updatefaceimg";
    /**
     * 获取主接口地址
     */
    String sys_SRVUrl = jsyoa + "/Account/getSRVUrl";
    /**
     * 登录互联应用
     **/
    String checkToken = "/Account/checkToken";
    /**
     * 注册客户端
     */
    String sys_regClient = jsyoa + "/Account/Reg";
    /**
     * 通讯握手
     */
    String sys_hello = jsyoa + "/Account/hello";
    /**
     * 用户登录
     */
    String user_login = jsyoa + "/Account/login";
    /**
     * 获取令牌
     */
    String getToken = jsyoa + "/Account/getToken";
    /**
     * 获取用户身份信息
     */
    String user_getRoleIdentity = jsyoa + "/Account/getRoleIdentity";
    /**
     * 获取用户信息
     */
    String user_getUserInfo = jsyoa + "/Account/getUserInfo";
    /**
     * 获取家长信息
     */
    String getGenInfo = jsyoa + "/Account/getGenInfo";
    /**
     * 获取学生信息
     */
    String getStuInfo = jsyoa + "/Account/getStuInfo";
    /**
     * 用户切换单位
     */
    String changeCurUnit = jsyoa + "/Account/changeCurUnit";
    /**
     * 用户注销
     */
    String user_logout = jsyoa + "/Account/logout";
    /**
     * 最新版本
     */
    String AndroidCurrVersionInfo = "http://www.jiaobao.net/DL/AndroidCurrVersionInfo.txt";
    /**
     * 取关联的班级
     */
    String getmyUserClass = jsyoa + "/Account/getmyUserClass";
    /**
     * 获取指定班级的所有学生数据列表
     */
    String getClassStdInfo = jsyoa + "/Basic/getClassStdInfo";
    /**
     * 检查手机是否重复 功能：检查手机号码是否重复（已注册），true没有注册，false有注册
     */
    String checkmobileAcc = jsyoa + "/AccountReg/checkmobileAcc";
    /**
     * 服务器生成一个4位数字的验证码，并返回包含此数字的图片url。每获取一次都生成不同的数字。
     */
    String GetValidateCode = jsyoa + "/AccountReg/GetValidateCode";
    /**
     * 用户输入手机号码，并输入图片验证码，app客户端把图片验证码和手机发回服务器，服务器验证后向手机发送验证码（6位数字）。
     */
    String SendCheckCode = jsyoa + "/AccountReg/SendCheckCode";
    /**
     * 重置密码时 验证账号是否存在
     */
    String ReSendCheckCode = jsyoa + "/AccountReg/ReSendCheckCode";
    /**
     * 用户输入收到的验证码，并输入图片验证码，app客户端把图片验证码和手机验证码发回服务器，检查用户输入是否正确
     */
    String RegCheckMobileVcode = jsyoa + "/AccountReg/RegCheckMobileVcode";
    /**
     * 用于在重置密码时验证用户手机（与注册时验证手机的接口不一样）。
     */
    String CheckMobileVcode = jsyoa + "/AccountReg/CheckMobileVcode";
    /**
     * 验证手机号码正确后，用户输入输入密码，app把用户注册手机和密码发给服务器完成注册手续。
     */
    String RegAccId = jsyoa + "/AccountReg/RegAccId";
    /**
     * 直接重置帐户密码（不是修改密码，无须验证旧密码），参数须加密
     */
    String ResetAccPw = jsyoa + "/AccountReg/ResetAccPw";
    /**
     * 在修改昵称时检查昵称是否重复
     */
    String checkAccN = jsyoa + "/AccountReg/checkAccN";
    /**
     * 修改帐户信息的昵称和姓名
     */
    String UpateRecAcc = jsyoa + "/AccountReg/UpateRecAcc";
    /**
     * 验证旧密码后修改帐户密码（不是重置密码），参数须加密
     */
    String ChangePW = jsyoa + "/AccountReg/ChangePW";
    /**
     * 根据手机号码自动配置的用户信息，获取用户所在单位及身份信息，每一条用户信息匹配一条记录。如果在一个单位有相同的身份，则会显示两条相同的记录。
     */
    String GetMyMobileUnitList = jsyoa + "/AccountReg/GetMyMobileUnitList";
    /**
     * 加入单位操作
     */
    String JoinUnitOP = jsyoa + "/AccountReg/JoinUnitOP";
    /********************* 移动互联应用 ****************************/
    /***************交流****************/
    /**
     * 取单位所有组
     */
    String getUnitGroups = jsyoa + "/Basic/getUnitGroups";
    /**
     * 取单位内所有人员
     **/
    String getUserInfoByUnitID = jsyoa + "/Basic/getUserInfoByUnitID";
    /**
     * 取所有好友
     **/
    String GetMyFriends = "/LGQIFriends/GetMyFriends";
    //**************事务************
    /**
     * 交流信息 发给我 功能：取发给我的交流信息
     **/
    String CommListToMe = jsyoa + "/CommMsg/CommListToMe";
    /**
     * 交流信息 我发的信息 功能：取我发的交流信息。
     **/
    String MySend = jsyoa + "/CommMsg/MySend";
    /**
     * 获取我发送的消息列表，列表中包括回复数量，未读回复数量经。比原接口简化了返回的字段且不加密
     **/
    String GetMySendMsgList = jsyoa + "/CommMsg/GetMySendMsgList";
    /**
     * 取发给我消息的用户列表。包括最新一条消息内容，以及该用户发给我的未读消息数量，未回复数量
     **/
    String SendToMeUserList = jsyoa + "/CommMsg/SendToMeUserList";
    /**
     * 取发给我消息的用户列表。包括最新一条消息内容，以及该用户发给我的未读消息数量，未回复数量
     **/
    String SendToMeMsgList = jsyoa + "/CommMsg/SendToMeMsgList";
    /**
     * 交流信息 显示交流信息明细 功能：显示交流信息明细，可以从发给我信息中查看明细，从我发送的信息列表中查看明细，也可以从我回复的列表中查看明细，
     * 可以从回复我的信息列表查看明细。不同场景调用接口的参数有所不同。
     **/
    String ShowDetail = jsyoa + "/CommMsg/ShowDetail";
    /**
     * 交流信息 显示交流信息明细 功能：显示交流信息明细，可以从发给我信息中查看明细，从我发送的信息列表中查看明细，也可以从我回复的列表中查看明细，
     * 可以从回复我的信息列表查看明细。不同场景调用接口的参数有所不同。
     **/
    String ShowDetail2 = jsyoa + "/CommMsg/ShowDetail2";
    /**
     * 交流信息 回复交流信息标记为已读
     **/
    String MarkRead = jsyoa + "/CommMsg/MarkRead";
    /**
     * 交流信息 回复交流信息 功能：对发给我的交流信息添加回复
     **/
    String addfeeback = jsyoa + "/CommMsg/addfeeback";
    /**
     * 交流信息 发表交流信息 功能：发表交流信息。
     **/
    String CreateCommMsg = jsyoa + "/CommMsg/CreateCommMsg";
    /**
     * 交流信息 获取交流信息接收人列表 功能： 根据用户当前所在单位，获取交流信息接收人列表。用户可以切换单位，然后重新获取接收人列表。
     **/
    String CommMsgRevicerList = jsyoa + "/CommMsg/CommMsgRevicerList";
    String CommMsgRevicerUnitList = jsyoa + "/CommMsg/CommMsgRevicerUnitList";
    /**
     * 取发送事务权限
     * 功能：当用户的身份为单位人员或老师时，需要判定用户是否有发送上级单位事务，本单位事务，下级单位事务的权限，如果用户不具备相应的权限，
     * 在app界面不要提供相应功能
     * 。这里指的新建事务的权限（权限对象为单位人员和老师），阅读和回复的权限人人都有。班级事务（家校沟通的权限）只要有关联的班级就可以使用，不受限制。
     */
    String GetCommPerm = jsyoa + "/CommMsg/GetCommPerm";
    /**
     * 功能：获取单位接收人
     */
    String GetUnitRevicer = jsyoa + "/CommMsg/GetUnitRevicer";
    /**
     * 获取班级接收人
     */
    String GetUnitClassRevice = jsyoa + "/CommMsg/GetUnitClassRevicer";
    /**
     * 功能：群发给下属单位的接收者，返回所有下属单位的管理员列表
     */
    String GetMsgAllRevicer_toSubUnit = jsyoa + "/CommMsg/GetMsgAllRevicer_toSubUnit";
    /**
     * 获取群发家长的接收对象 功能：群发给下属单位的接收者，返回所有下属单位的管理员列表
     */
    String GetMsgAllRevicer_toSchoolGen = jsyoa + "/CommMsg/GetMsgAllRevicer_toSchoolGen";
    /**
     * 获取群发权限
     * 功能：当前用户是否有群发给下属单位单位人员事务信息（下属学校的家长）或群发信息给本校家长的权限，该事务信息分两类接收人：群发给下属单位
     * （单位人员），群发给家长
     */
    String GetMsgAllReviceUnitList = jsyoa + "/CommMsg/GetMsgAllReviceUnitList";
    /**
     * 交流信息 获得当前未读交流信息数量 功能： 获得当前未读交流信息数量
     **/
    String getNoReadeCount = jsyoa + "/CommMsg/getNoReadeCount";
    /**
     * 交流信息 获得当前未读回复数量 功能：获得当前未读回复数量
     **/
    String getfbtomecount = jsyoa + "/CommMsg/getfbtomecount";
    /**
     * 交流信息获取短信直通车接收单位树数据源<br>
     * 功能：根据用户当前所在单位，获取短信直通车接收单位树数据源（包括本单位及所有下级单位，跨级下级单位）。用户可以切换单位，
     * 然后重新获取短信直通车接收单位树数据源
     **/
    String SMSCommIndex = jsyoa + "/CommMsg/SMSCommIndex";
    /*******************文章****************/
    /**
     * 功能：对文章进行点赞操作。一篇文章一个用户 （以教宝号为准）只能赞一次。
     **/
    String LikeIt = jsyoa + "/Sections/LikeIt";
    /**
     * 功能：发表评论。发表评论可以在别人评论的基础上再次评论，称为引用评论。
     **/
    String addComment = jsyoa + "/Sections/addComment";
    /**
     * 功能：获取文章评论列表。
     **/
    String CommentsList = jsyoa + "/Sections/CommentsList";
    /**
     * 功能：对评论进行顶或踩的操作。
     **/
    String AddScore = jsyoa + "/Sections/AddScore";
    /**
     * 取栏目最新和推荐文章
     **/
    String TopArthListIndex = jsyoa + "/Sections/TopArthListIndex";
    /**
     * 取我相关单位的列表及栏目未读文章数量，功能：单位列表包括上级单位和我所在单位，去重后的单位列表
     **/
    String getUnitSectionMessages = jsyoa + "/Sections/getUnitSectionMessages";
    /**
     * 取栏目最新和推荐文章 未读消息数量
     **/
    String getSectionMessage = jsyoa + "/Sections/getSectionMessage";
    /**
     * 取本单位基础信息
     **/
    String getMyUnitInfo = jsyoa + "/Basic/getMyUnitInfo";
    /**
     * 取本单位的所有上级单位基础信息
     **/
    String getMyParentUnitInfo = jsyoa + "/Basic/getMyParentUnitInfo";
    /**
     * 取本单位的所有下级单位基础信息
     **/
    String getMySubUnitInfo = jsyoa + "/Basic/getMySubUnitInfo";
    /**
     * 获取指定学校的所有班级基础数据
     **/
    String getSchoolClassInfo = jsyoa + "/Basic/getSchoolClassInfo";
    /**
     * 取本单位栏目文章
     **/
    String ArthListIndex = jsyoa + "/Sections/ArthListIndex";
    /**
     * 取本单位栏目文章
     **/
    String UnitArthListIndex = jsyoa + "/Sections/UnitArthListIndex";
    /**
     * 取本单位栏目文章 详情
     **/
    String ShowArthDetail = jsyoa + "/Sections/ShowArthDetail";
    /**
     * 功能：获取文章附加信息，同时执行积分，已读等处理操作
     **/
    String GetArthInfo = jsyoa + "/Sections/GetArthInfo";
    /**
     * 发布文章
     **/
    String savepublishArticle = jsyoa + "/Sections/savepublishArticle";
    /**
     * 上传图片
     **/
    String uploadSectionImg = jsyoa + "/ClientUpLoadFile/uploadSectionImg";
    /**
     * 获取单位通知
     **/
    String getUnitNotics = jsyoa + "/Sections/getUnitNotics";
    /**
     * 取最新或推荐单位栏目文章
     **/
    String ShowingUnitArthList = jsyoa + "/Sections/ShowingUnitArthList";
    /**
     * 取我关注的单位栏目文章 功能：取我关注单位的文章列表,（包含官方文章和非官方文章,即单位展示和单位分享）
     **/
    String MyAttUnitArthListIndex = jsyoa + "/Sections/MyAttUnitArthListIndex";
    /**
     * 我的班级文章列表 功能：取我所在班级（所有的班级）的所有文章列表，注意取时是按当前身份来获取的。所以要注意切换单位。
     **/
    String AllMyClassArthList = jsyoa + "/Sections/AllMyClassArthList";
    /**
     * 取单位简介
     **/
    String getintroduce = "/ClientSrv/getintroduce";
    /**
     * 功能：获取当前用户可以发布动态的单位列表(含班级），如果返回结果为空，则不能在任何单位发布动态。
     **/
    String GetReleaseNewsUnits = jsyoa + "/Sections/GetReleaseNewsUnits";
    /********************* 签到start *****************************/
    /**
     * 签到
     **/
    String user_sign = "InterAppMobileInterface/MobileCreateSignIn";
    /**
     * 获取签到方式
     **/
    String getSignWay = "InterAppMobileInterface/GetSignInGroupByUnitID";
    /**
     * 查询签到信息
     **/
    String user_select = "InterAppMobileInterface/GetSignInListForMobile";
    /**
     * 查询单位围栏
     **/
    String fance_select = "InterAppSignInAddress/GetSignInAddJsonData";

    /********************** 日程start ************************/
    /**
     * 获取可延迟时间
     **/
    String getDiffence = "WorkPlanMobileInterface/WorkPlanGetReportedDelayDateByUnitID";
    /**
     * 提交日程
     **/
    String commitWorkPlan = "WorkPlanMobileInterface/WorkPlanAddContent";
    String selectWorkPlanMonth = "WorkPlanMobileInterface/WorkPlanSelectContentByMonth";
    String selectWorkPlanDay = "WorkPlanMobileInterface/GetWorkPlanInfoByUnitIDUserIDDate";
    /************************ 相册 **************************/
    /**
     * 功能：获取某单位中的相册
     **/
    String GetUnitPGroup = "/LGQIPhotoInfo/GetUnitPGroup";
    /**
     * 功能：获相册列表
     **/
    String GetPhotoList = "/LGQIPhoto/GetPhotoList";
    /**
     * 功能：获取单位相册的照片
     **/
    String GetUnitPhotoByGroupID = "/LGQIPhotoInfo/GetUnitPhotoByGroupID";
    /**
     * 功能：获取danwei相册的最新照片(int UnitID,int count)
     **/
    String GetUnitNewPhoto = "/LGQIPhotoInfo/GetUnitNewPhoto";
    /**
     * 功能：获取相册的最新照片
     **/
    String GetNewPhoto = "/LGQIPhoto/GetNewPhoto";
    /**
     * 功能：获取相册的所有照片
     **/
    String GetPhotoByGroup = "/LGQIPhoto/GetPhotoByGroup";
    /**
     * 获取我的关注的单位
     **/
    String GetMyAttUnit = "/LGQIFriends/GetMyAttUnit";
    /**
     * 获取我的相册封面int JiaoBaoHao, string GroupInfo)
     **/
    String GetFristPhotoByGroup = "/LGQIPhoto/GetFristPhotoByGroup";
    /**
     * 获取单位相册封面(int UnitID, int GroupID)
     **/
    String GetUnitFristPhotoByGroupID = "/LGQIPhotoInfo/GetUnitFristPhotoByGroupID";
    /**
     * 创建单位相册
     **/
    String CreateUnitPhotoGroup = "/LGQIPhotoInfo/CreateUnitPhotoGroup";
    /**
     * 个人空间上传图片
     **/
    String UpLoadPhotoFromAPP = "/LGQIPhoto/UpLoadPhotoFromAPP";
    /**
     * 单位相册上传照片
     **/
    String UpLoadPhotoUnit = "/LGQIPhotoInfo/UpLoadPhotoUnit";
    /**
     * 个人空间添加相册
     **/
    String AddPhotoGroup = "/LGQIPhoto/AddPhotoGroup";

    /************** 求知 *********************/
    /**
     * 功能：取用户信息
     */
    String GetUserInfo = jsyoa + "/Knl/GetUserInfo";
    /**
     * 功能：取所有话题
     */
    String GetAllCategory = jsyoa + "/Knl/GetAllCategory";
    /**
     * 功能：获取推荐列表（首页）
     */
    String RecommentIndex = jsyoa + "/Knl/RecommentIndex";
    /**
     * 功能：获取精选列表（首页通过指定ID获取一个精选问题集或最新的一个精选问题集）
     */
    String GetPickedById = jsyoa + "/Knl/GetPickedById";
    /**
     * 功能：获取各期精选列表
     */
    String PickedIndex = jsyoa + "/Knl/PickedIndex";
    /**
     * 功能：通过指定ID获取一个精选问题内容的明细
     */
    String ShowPicked = jsyoa + "/Knl/ShowPicked";
    /**
     * 功能：获取指定话题ID的置顶问题。每一个话题只有一个置顶问题，如果该问题状态不可用（删除或屏蔽)或没有置顶问题，则返回的数组为空
     */
    String GetCategoryTopQ = jsyoa + "/Knl/GetCategoryTopQ";
    /**
     * 功能：获取单个推荐详情
     */
    String ShowRecomment = jsyoa + "/Knl/ShowRecomment";
    /**
     * 功能：关注某一问题
     */
    String AddMyAttQ = jsyoa + "/Knl/AddMyAttQ";
    /**
     * 功能：取消关注某一问题
     */
    String RemoveMyAttQ = jsyoa + "/Knl/RemoveMyAttQ";
    /**
     * 功能：邀请指定的用户回答问题
     */
    String AtMeForAnswer = jsyoa + "/KnUser/AtMeForAnswer";
    /**
     * 功能：.获区首页问题列表，按要求排序和包含一个回答。
     */
    String UserIndexQuestion = jsyoa + "/Knl/UserIndexQuestion";
    /**
     * 功能：获取指定话题ID的问题列表，按提问时间排序和包含一个回答。返回结果包含本话题及其子级话题的问题
     */
    String CategoryIndexQuestion = jsyoa
            + "/Knl/CategoryIndexQuestion";
    /**
     * 功能：取系统话题列表。话题分为多级，一级话题的父Id为0。
     */
    String GetCategory = jsyoa + "/Knl/GetCategory";
    /**
     * 功能：获取回答xiang'qi。
     */
    String QuestionDetail = jsyoa + "/Knl/QuestionDetail";
    /**
     * 功能：修改答案。
     */
    String UpdateAnswer = jsyoa + "/Knl/UpdateAnswer";
    /**
     * 功能：取系统话题列表。话题分为多级，一级话题的父Id为0。
     */
    String GetAnswerById = jsyoa + "/Knl/GetAnswerById";
    /**
     * 功能：回答问题。
     */
    String AddAnswer = jsyoa + "/Knl/AddAnswer";
    /**
     * 功能：获取一个答案明细信息，包括问题内容。
     */
    String AnswerDetail = jsyoa + "/Knl/AnswerDetail";
    /**
     * 功能：获取答案的评论 列表。
     */
    String KnlCommentsList = jsyoa + "/Knl/CommentsList";
    /**
     * 功能：举报回答含有非法内容，如果超过一定人数举报答案，则系统自动屏蔽该答案;
     */
    String ReportAns = jsyoa + "/Knl/ReportAns";
    /**
     * 功能：评价答案，支持或反对;
     */
    String SetYesNo = jsyoa + "/Knl/SetYesNo";
    /**
     * 功能：对答案添加评论;
     */
    String AddComment = jsyoa + "/Knl/AddComment";
    /**
     * 功能：对评论进行顶或踩的操作
     */
    String AddScoreKnl = jsyoa + "/Knl/AddScore";
    /**
     * 功能：发布问题
     */
    String NewQuestion = jsyoa + "/Knl/NewQuestion";
    /**
     * 功能：通过昵称获取教宝号，昵称是一个数组参数。可以一次查询多个昵称
     */
    String GetAccIdbyNickname = jsyoa + "/Knl/GetAccIdbyNickname";
    /**
     * 功能：去系统中省份信息
     */
    String GetProvice = jsyoa + "/Basic/GetProvice";
    /**
     * 功能：取指定省份的地市数据或取指定地市的区县数据
     */
    String GetCity = jsyoa + "/Basic/GetCity";
    /**
     * 功能：获取我关注的问题列表
     */
    String MyAttQIndex = jsyoa + "/KnUser/MyAttQIndex";
    /**
     * 功能：获取邀请我回答的问题列表
     */
    String AtMeQIndex = jsyoa + "/KnUser/AtMeQIndex";
    /**
     * 功能：获取我提出的问题列表
     */
    String MyQuestionIndex = jsyoa + "/KnUser/MyQuestionIndex";
    /**
     * 功能：获取我回答的问题列表
     */
    String MyAnswerIndex = jsyoa + "/KnUser/MyAnswerIndex";
    /**
     * 功能：取我关注的话题ID数组
     */
    String GetMyattCate = jsyoa + "/KnUser/GetMyattCate";
    /**
     * 功能：更新我关注的话题
     */
    String AddMyattCate = jsyoa + "/KnUser/AddMyattCate";
    /**
     * 功能：邀请人回答时，获取回答该话题问题最多的用户列表（4个）
     */
    String GetAtMeUsers = jsyoa + "/KnUser/GetAtMeUsers";
    /**
     * 功能：获取我的评论列表
     */
    String GetMyComms = jsyoa + "/Knl/GetMyComms";
    /**
     * 功能：获取我的日积分
     */
    String GetMyPointsDay = jsyoa + "/Knl/GetMyPointsDay";
    /**
     * 功能：获取我的月积分
     */
    String GetMyPointsMonth = jsyoa + "/Knl/GetMyPointsMonth";
    /**
     * 请假系统主入口
     */
    String leave = jsyoa + "/Leave";
    /**
     * 功能：单位请假设置
     */
    String GetLeaveSetting = leave + "/GetLeaveSetting";
    /**
     * 功能：保存一条新增请假记录
     */
    String NewLeavelModel = leave + "/NewLeaveModel";
    /**
     * 功能：更新一条请假记录
     */
    String UpdateLeaveModel = leave + "/UpdateLeaveModel";
    /**
     * 给一个假条新增一个时间段
     */
    String AddLeaveTime = leave + "/AddLeaveTime";
    /**
     * 更新假条的一个时间段
     */
    String UpdateLeaveTime = leave + "/UpdateLeaveTime";
    /**
     * 删除假条的一个时间段
     */
    String DeleteLeaveTime = leave + "/DeleteLeaveTime";
    /**
     * 删除假条
     */
    String DeleteLeaveModel = leave + "/DeleteLeaveModel";
    /**
     * 获取我提出的请假记录
     */
    String GetMyLeaves = leave + "/GetMyLeaves";
    /**
     * 获取一个假条的详细信息
     */
    String GetLeaveModel = leave + "/GetLeaveModel";
    /**
     * 班主任获取本班学生请假记录
     */
    String GetClassLeaves = leave + "/GetClassLeaves";
    /**
     * 审核人员取本单位的请假记录
     */
    String GetUnitLeaves = leave + "/GetUnitLeaves";
    /**
     * 门卫取请假记录
     */
    String GetGateLeaves = leave + "/GetGateLeaves";
    /**
     * 审批假条
     */
    String CheckLeaveModel = leave + "/CheckLeaveModel";
    /**
     * 门卫登记离校返校时间
     */
    String UpdateGateInfo = leave + "/UpdateGateInfo";
    /**
     * 家长获取关联学生列表
     */
    String GetMyStdInfo = jsyoa + "/Account/GetMyStdInfo";
    /**
     * 班主任获取管理班级列表
     */
    String GetMyAdminClass = jsyoa + "/Account/GetMyAdminClass";
    /**
     * 获取所有年级班级
     */
    String getunitclass = jsyoa + "/basic/getunitclass";
    /**
     * 获取教职工查询统计
     */
    String GetManSumLeaves = leave + "/GetManSumLeaves";
    /**
     * 获取学生查询统计
     */
    String GetStudentSumLeaves = leave + "/GetStudentSumLeaves";
    /**
     * 获取班级统计
     */
    String GetClassSumLeaves = leave + "/GetClassSumLeaves";

    /**
     * 快速签到
     */
    String QuickSignIn = QuickSignInUrl + "/SignIn/SignIn";
    /**
     * 查询快速签到的记录
     */
    String GetMySignInfo = QuickSignInUrl + "/SignIn/getMySingInfo";
}