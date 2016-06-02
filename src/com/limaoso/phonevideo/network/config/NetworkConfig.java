package com.limaoso.phonevideo.network.config;

import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.utils.PhoneInfoUtils;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;

/**
 * 网络配置文件
 * 
 * @author jjm
 * 
 */
public class NetworkConfig {
	/**
	 * 域名
	 */
	private static final String BASEDOMAIN = "http://mmmapi.limaoso.com/";
	private static final String SEARCHURL = BASEDOMAIN + "msearch.php/";
	public static final String FMT = "&fmt=sx";

	// private static final String DOMAIN =
	// "http://limaoso.cntttt.com/mapi.php/";

	// private static final String DOMAIN = "http://www.limaoso.com/mapi.php/";
	private static final String DOMAIN = "http://mmmapi.limaoso.com/mapi.php/";

	// private static final String DOMAIN = "http://101.200.89.78:9191/";

	// 获取ucode
	public static String getUcode() {
		return PrefUtils.getString(GlobalConstant.USER_UCODE, "");
	}

	/**
	 * 消息通知页-----消息数
	 * 
	 * @return
	 */

	public static String getInformNum() {
		return DOMAIN + "User/getInfoNums?ucode=" + getUcode();

	}

	/**
	 * 消息通知页-----评论
	 * 
	 * @return
	 */

	public static String getInformPage_c() {
		return DOMAIN + "User/getMyComments?ucode=" + getUcode();

	}

	public static String getInformPage() {
		return DOMAIN + "User/getMyReply?ucode=" + getUcode();

	}

	/**
	 * 消息通知页-回复
	 * 
	 * @return
	 */
	public static String getInformPage_r() {
		return DOMAIN + "User/getMyReply?ucode=" + getUcode();
	}

	/**
	 * 消息通知页--系统回复
	 * 
	 * @return
	 */
	public static String getInformPage_s() {
		return DOMAIN + "User/getMySmsg?ucode=" + getUcode();
	}

	/**
	 * 记录页面
	 * 
	 * @return
	 */
	public static String getRecordPage() {
		return DOMAIN + "User/getMyHistory?ucode=" + getUcode();
	}

	/**
	 * 记录页面 删除操作
	 * 
	 * @return
	 */
	public static String getRecordDelPage() {
		return DOMAIN + "User/delMyHistory?ucode=" + getUcode();
	}

	/**
	 * 收藏
	 * 
	 * @return
	 */
	public static String getColection(int page) {
		if (page == 1) {
			return DOMAIN + "User/getMyFav?ucode=" + getUcode();

		} else {
			return DOMAIN + "User/getMyFav?ucode=" + getUcode() + "&page="
					+ page;

		}
	}

	/**
	 * 删除收藏
	 * 
	 * @return
	 */
	// http://limaoso.cntttt.com/mapi/User/getMyFav/
	public static String getColectionDel(String action, String fav_ids) {

		return DOMAIN + "User/delMyFav?ucode=" + getUcode() + "&action="
				+ action + "&ids=" + fav_ids;

	}

	/**
	 * 回复
	 * 
	 * @param id
	 * @param i
	 * @return
	 */
	public static String getReplyComment() {

		return DOMAIN + "User/addComments?ucode=" + getUcode();
	}

	/**
	 * 上网必备页面
	 * 
	 * @return
	 */
	public static String getLeftNetworkpage() {

		return DOMAIN + "Index/bibei?ucode=" + getUcode();
	}

	/**
	 * 首页
	 * 
	 * @return
	 */
	public static String getIndex() {

		return DOMAIN + "Index/getIndexAll/channel/"
				+ GlobalConstant.APP_CHANNEL;
	}

	/**
	 * 发现
	 * 
	 * @return
	 */
	public static String getFind(int page) {

		if (page == 1) {
			return DOMAIN + "Index/faxian/";
		} else {
			return DOMAIN + "Index/faxian?page=" + page;
		}
	}

	/**
	 * 发现详情
	 * 
	 * @return
	 */
	public static String getFindDetail(String id) {

		return DOMAIN + "Index/findDetail?id=" + id;
	}

	/**
	 * 我的资料
	 * 
	 * @return
	 */
	public static String getMyData(String where) {
		return DOMAIN + "User/getUserInfo?ucode=" + getUcode();
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public static String setPassword(String oldpwd, String newpwd) {
		return DOMAIN + "User/editPwd?ucode=" + getUcode() + "&oldpwd="
				+ oldpwd + "&newpwd=" + newpwd;
	}

	/**
	 * 修改性别
	 * 
	 * @return
	 */
	public static String setSex(int sex) {
		return DOMAIN + "User/editSex/?ucode=" + getUcode() + "&sex=" + sex;
	}

	/**
	 * 修改昵称
	 * 
	 * @return
	 */
	public static String setNickName(String nickName) {

		return DOMAIN + "User/editNickname?ucode=" + getUcode() + "&nickname="
				+ nickName;
	}

	/**
	 * 修改生日
	 * 
	 * @return
	 */
	public static String setBirth(String birth) {

		return DOMAIN + "User/editBirth/?ucode=" + getUcode() + "&birth="
				+ birth;
	}

	/**
	 * 注册
	 * 
	 * @return
	 */
	public static String getUser() {

		return DOMAIN + "Public/autoRegisterUser?tel="
				+ PhoneInfoUtils.getPhoneNumber(UIUtils.getContext())
				+ "&imei=" + PhoneInfoUtils.getPhoneImei(UIUtils.getContext())
				+ "&mac=" + PhoneInfoUtils.getPhoneMac(UIUtils.getContext())
				+ "&channel=" + GlobalConstant.APP_CHANNEL;
	}

	/**
	 * 为你推荐界面
	 * 
	 * @return
	 */
	public static String getRecommendIndex() {
		http: // www.limaoso.com/mapi.php/Index/getSpecials/
		return DOMAIN + "Index/getSpecials?ucode=" + getUcode();
	}

	/**
	 * 频道界面
	 * 
	 * @return
	 */
	public static String getChannelIndex() {
		return DOMAIN + "Index/category?ucode=" + getUcode();
	}

	/**
	 * 播放页面
	 * 
	 * @return
	 */
	public static String getPlayActivityUrl(String cfilmid) {

		return DOMAIN + "Index/getDetailCfilm?cfilmid=" + cfilmid + "&ucode="
				+ getUcode() + "&channel=" + GlobalConstant.APP_CHANNEL;

	}

	/**
	 * 用户推荐界面 --->登陆状态下
	 * 
	 * @return
	 */
	public static String getUserCenter() {

		return DOMAIN + "Index/getUserHomeIndex?ucode=" + getUcode();

	}

	/**
	 * 用户推荐界面 --->未登陆状态下
	 * 
	 * @return
	 */
	public static String getUserCenter_un() {

		return DOMAIN + "Index/getUserHomeIndex/";

	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	public static String getLogin(String usrid, String pwd) {
		return DOMAIN + "Public/userLoginByUidPwd?uid=" + usrid + "&pwd=" + pwd;
	}

	/**
	 * 上传头像
	 * 
	 * @return
	 */
	// http://limaoso.cntttt.com/mapi/User/getMyFav/
	public static String setHeadIcon() {

		return DOMAIN + "User/uploadFace?ucode=" + getUcode() + "&cc=bb";

	}

	/**
	 * 邮箱发验证码
	 * 
	 * @return
	 */
	// http://limaoso.cntttt.com/mapi/User/getMyFav/
	public static String getMailCode(String email) {

		return DOMAIN + "User/email_post?ucode=" + getUcode() + "&email="
				+ email;

	}

	/**
	 * 邮箱验证
	 * 
	 * @return
	 */
	// http://limaoso.cntttt.com/mapi/User/getMyFav/
	public static String getMailCheck(String email, String code) {

		return DOMAIN + "User/email_verify?ucode=" + getUcode() + "&email="
				+ email + "&code=" + code;

	}

	/**
	 * 手机发验证码
	 * 
	 * @return
	 */
	// http://limaoso.cntttt.com/mapi/User/getMyFav/
	public static String getPhoneCode(String phone) {

		return DOMAIN + "User/tel_msg_post?ucode=" + getUcode() + "&tel="
				+ phone;

	}

	/**
	 * 手机验证
	 * 
	 * @return
	 */
	// http://limaoso.cntttt.com/mapi/User/getMyFav/
	public static String getPhoneCheck(String phone, String code) {

		return DOMAIN + "User/tel_verify?ucode=" + getUcode() + "&tel=" + phone
				+ "&code=" + code;
	}

	/**
	 * 搜索页
	 * 
	 * @return
	 */
	public static String getSearchAll() {
		return DOMAIN + "Index/getSearchDefaultAll/";
	}

	/**
	 * 搜索结果页
	 * 
	 * @param refreshPage
	 * @return
	 */
	public static String getSearchResult(String key, int refreshPage) {
		return DOMAIN + "Index/search?page=" + refreshPage + "&key=" + key;
	}

	/**
	 * 视频资源来源页
	 * 
	 * @param id
	 * @param i
	 * @return
	 * 
	 */
	public static String getPlaySourceAll(String id, int jindex) {

		return DOMAIN + "Index/getDetailCku/?id=" + id + "&jindex=" + jindex
				+ "&ucode=" + getUcode() + "&channel="
				+ GlobalConstant.APP_CHANNEL;
	}

	/**
	 * 回复
	 * 
	 * @param id
	 * @param i
	 * @return
	 */
	public static String getComment(String comid) {

		return DOMAIN + "Index/getMycommentsOneJson?ucode=" + getUcode()
				+ "&comid=" + comid;
	}

	/**
	 * 确认能否播放
	 * 
	 * @param id
	 *            播放ID
	 * @param num
	 *            播放集数
	 * @return
	 */
	public static String getReqPlay(String id, int num) {

		return DOMAIN + "Index/ishasCfilmByJindex?jindex=" + num + "&kuid="
				+ id;
	}

	/**
	 * 获取找回密码url
	 */
	public static String getRetrievePassword() {
		return DOMAIN + "Public/userFindPassword/";
	}

	/**
	 * 获取htmld页面url
	 */
	public static String getHtmlUrl() {

		return DOMAIN + "Html/index/";

	}

	/**
	 * 为你推荐详情页面
	 * 
	 * @param recid
	 * @return
	 */
	public static String getRecommendDetail(String recid) {
		return DOMAIN + "Index/getSpecialData?id=" + recid;
	}

	/**
	 * 获取版本信息
	 * 
	 * @param versionflag
	 *            标志
	 * @param i
	 * @return
	 */
	public static String getgetVersionInfo(String versionflag, int vcode) {
		return DOMAIN + "Version/getVersionInfo?flag=" + versionflag
				+ "&vcode=" + vcode;
	}

	public static String ucode = "1468ed7813537eb4163682c601bda77d";

	/**
	 * 获取动态影视集合
	 * 
	 * @return
	 */
	public static String getDTVListAll(int channelid, int page) {
		return SEARCHURL + "video/getRecommendVideo2?page=" + page
				+ "&channelid=" + channelid + "&ucode=" + getUcode() + FMT;
	}

	/**
	 * 设置用户兴趣选项数据
	 * 
	 * @return
	 */
	public static String getsetIntrest() {
		return SEARCHURL + "Userintrest/setIntrest?ucode=" + getUcode() + FMT;
	}

	/**
	 * 获取首页栏目
	 * 
	 * @return
	 */
	public static String getUserIntrest() {
		return SEARCHURL + "Userintrest/getUserIntrestSSS?ucode=" + getUcode()
				+ FMT;
	}

	/**
	 * 提交评论接口
	 * 
	 * @return
	 */
	public static String commentTextUrl() {
		return SEARCHURL + "Videodetail/addComments?ucode=" + getUcode() + FMT;
	}

	/**
	 * 视频界面顶踩操作 1 顶，2踩
	 * 
	 * @param type
	 * @return
	 */
	public static String getUpOrDownUrl(String id, String type) {
		return SEARCHURL + "Videodetail/goDingCai?ucode=" + getUcode()
				+ "&ckuid=" + id + "&type=" + type + FMT;
	}

	/**
	 * 获取详细评论内容
	 * 
	 * @param mId
	 *            该条评论的id
	 * @param current_tag
	 *            1:获取主评论，2，获取分页子评论
	 * @param mPage
	 *            当前分页的子评论
	 * @return
	 */
	public static String getCommentURL(String mId, String type, int mPage) {
		return SEARCHURL + "Videodetail/getVideoCommentsDetail?ucode="
				+ getUcode() + "&type=" + type + "&commentid=" + mId + "&page="
				+ mPage + FMT;
	}

	/**
	 * 播放视频接口
	 * 
	 * @param tv_id
	 * @return
	 */
	public static String getPlayActivityUrl_shejiao(String tv_id) {
		return SEARCHURL + "Videodetail/getDetailVideos?ckuid=" + tv_id
				+ "&ucode=" + getUcode();
	}

	/**
	 * 播放视频接口
	 * 
	 * @param tv_id
	 * @return
	 */
	public static String getRecIndex(int page) {
		return SEARCHURL + "Video/getRecIndex?page=" + page + "&ucode="
				+ getUcode();
	}
}
