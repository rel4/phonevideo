package com.limaoso.phonevideo.global;

import com.limaoso.phonevideo.utils.SDUtils;
import com.limaoso.phonevideo.utils.UIUtils;

public interface GlobalConstant {
	/* 包名 */
	String APP_CHANNEL = "main";
	// String APP_CHANNEL = "1005";
	/**
	 * 根文件夹名称
	 */
	String ROOT_DIR_NAME = "limao";
	/**
	 * 头像的名字
	 */
	String HELAD_ICON_NAME = "_HELAD_ICON_NAME";

	// intnet id
	String INTENT_ID = "intet_id";
	// intnet名字
	String INTENT_NAME = "intent_name";
	String INTENT_BUNDLE = "intent_bundle";
	String INTENT_DATA = "intent_data";
	// 数值开始
	int ZERO = 0;
	int ONE = 1;
	int TWO = 2;
	int THREE = 3;
	int FOUR = 4;
	// 数值结束
	/**
	 * 视频ID
	 */
	String TV_ID = "1";
	/**
	 * 影片评论id
	 */
	String FILM_COMMENT_ID = "fci";
	/**
	 * 搜索关键词
	 */
	String APP_SEARCH_KEY = "2";
	/**
	 * 总剧集数
	 */
	String TV_ALL_NUM = "tvAllNum";
	/**
	 * 切割标志
	 */
	String FLAG_APP_SPLIT = "#_`_#";
	/**
	 * sp保存搜索词
	 */
	String SERACH_CACHE_KEY = "serach_cache_key";
	/**
	 * sp保存是否跳过片头片尾
	 */
	String MS_SETTING_SKIP = "MS_SETTING_SKIP";
	/**
	 * sp保存是否连续
	 */
	String MS_SETTING_CONTINUE = "MS_SETTING_CONTINUE";
	/**
	 * sp保存是否用2G/3G/4G
	 */
	String MS_SETTING_USEWEB = "MS_SETTING_USEWEB";
	/**
	 * sp保存是否用在非wifi时提醒
	 */
	String MS_SETTING_NOTICEWEB = "MS_SETTING_NOTICEWEB";
	/**
	 * sp保存是否消息通知
	 */
	String MS_PUSH_MESSAGE_NOTICE = "MS_PUSH_MESSAGE_NOTICE";
	/**
	 * sp保存是否系统通知
	 */
	String MS_PUSH_SYSTEM_NOTICE = "MS_PUSH_SYSTEM_NOTICE";
	/**
	 * sp保存通知接受时段
	 */
	String MTV_SHOW_TIME = "MTV_SHOW_TIME";

	/**
	 * sp保存图片路径名
	 */
	String HEAD_ICON = "HEAD_ICON";
	/**
	 * sp保存图片路径具体图片/DCIM/Camera/limao/
	 */
	String HEAD_ICON_PATH = SDUtils.getRootFile(UIUtils.getContext())
			+ "/icon/" + ROOT_DIR_NAME + ".jpg";
	/**
	 * sp保存图片路径
	 */
	String HEAD_ICON_SAVEPATH = SDUtils.getRootFile(UIUtils.getContext())
			+ "/icon/";
	/**
	 * 首页图片保存sp
	 */
	String BG_HOME_PAGE = "bg_home_page";
	/**
	 * 首页图片保存地址
	 */
	// String HOMEPAGE_HEADICON = HEAD_ICON_SAVEPATH + "limaohomeicon.jpg";
	/**
	 * 首页图片保存地址2
	 */
	String HOMEPAGE_HEADICON2 = HEAD_ICON_SAVEPATH + "limaohomeicon2.jpg";

	/**
	 * 视频ID和总集数
	 */
	String TV_ID_AND_TVNUM = "tv_id_and_num";
	/**
	 * 判断当前状态是否为登陆状态
	 */
	String IS_LOGINED = "is_logined";
	/**
	 * 关闭播放来源的广播
	 */
	String CLOSE_PLAY_SOURCE_ACTIVITY = "close_play_source_activity";

	/**
	 * 存储消息通知的消息总数量
	 */
	String INFORM_NUM = "inform_num";

	// /**
	// *从登陆界面进入首页的状态码
	// * ·
	// */
	// int GO_HOME= 101;
	// /**
	// *从登陆界面进入观影社区的状态码
	// * ·
	// */
	// int GO_MOVIE_PAGE= 102;
	// /**
	// *从登陆界面进入必看推荐的状态码
	// *
	// */
	// int GO_RECOMMEND_PAGE= 103;
	// /**
	// *从登陆界面进入应用发现的状态码
	// *
	// */
	// int GO_APP_PAGE= 104;

	/**
	 * 判断是否点击退出登录
	 */
	String QUIT = "quit";
	/**
	 * 首页图片更新时间
	 */
	String HOMEPAGE_TIME = "homepage_time";
	/**
	 * 为你推荐详情ID
	 */
	String RECOMMEND_DETAIL_RECID = "recommend_detail_recid";
	/**
	 * 保存的关键词
	 */
	String SKEY = "skey";
	/**
	 * 保存忽略版本号
	 */
	String SAVE_IGNORE_VERSIONCODE = "save_ignore_versioncode";
	/**
	 * 保存更新apk路径
	 */
	String SAVE_APK_PATH = SDUtils.getRootFile(UIUtils.getContext()) + "/apks/"
			+ UIUtils.getContext().getPackageName() + ".apk";
	/**
	 * 保存apk路径
	 */
	String DL_APK_PATH = SDUtils.getRootFile(UIUtils.getContext()) + "/apks/";
	// 用户数据
	String USER_UCODE = "user_ucode";
	String USER_NICKNAME = "user_nickname";
	String USER_ID = "user_id";
	String USER_PASSWORD = "user_password";
	String USER_ISEDITPWD = "user_iseditpwd";
	String USER_BIRTHDAY = "user_birthday";
	String USER_SCORE = "user_score";
	String USER_TEL = "user_tel";
	String USER_EMAIL = "user_email";
	String USER_EMAIL_VERIFY = "user_email_verify";
	String USER_TEL_VERIFY = "user_tel_verify";

	String USER_TOP_DATA = "user_top_data";// 小视频顶部分类 判断服务器是否有修改
	// 用户数据结束
	// 下载appid
	String DL_APP_ID = "dl_app_id";
	/**
	 * 缓存页面去播放页面标志
	 */
	String FLAG_PALY_CACHE_HASH = "FLAG_PALY_CACHE_HASH";

	/**
	 * 下载的升级包软件版本号
	 */
	String UPLOADVERSIONCODE = "UPLOADVERSIONCODE";
	/**
	 * 升级包的主要信息
	 */
	String UPLOAD_DESC = "UPLOAD_DESC";
	/**
	 * 升级包的名字
	 */
	String UPLOAD_VNAME = "UPLOAD_VNAME";
	/**
	 * 升级包的大小
	 */
	String UPLOAD_FSIZE = "UPLOAD_FSIZE";
	/**
	 * 升级包的时间
	 */
	String UPLOAD_PDATE = "UPLOAD_PDATE";
	/**
	 * 播放视频的id标志
	 */
	String PLAY_VIDEO_ID = "PLAY_VIDEO_ID";
	/**
	 * 获取该条评论的id
	 */
	String COMMENTDATAURL = "COMMENTDATAURL";
}
