package com.limaoso.phonevideo.base.impl.hometitlepager.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.adapter.MyChannelPageAdapter;
import com.limaoso.phonevideo.bean.HomeBean.Cont.TV;
import com.limaoso.phonevideo.bean.TVbean;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.RefreshListView;
import com.limaoso.phonevideo.view.RefreshListView.OnRefreshListener;

/**
 * 此类提供频道页面公共的方法。
 * 
 * @author liang
 * 
 */
public abstract class ShowPage implements OnRefreshListener,
		OnCheckedChangeListener {

	// 总分类
	public static final String MOVIE = "&dtype=1"; // 电影
	public static final String TV = "&dtype=2"; // 电视
	public static final String ANIM = "&dtype=3"; // 动漫
	public static final String VIDEO = "&dtype=4"; // 视频
	// 各个分类下的子分类
	public static final int FEATURED = 1; // 频道页，精选子项
	public static final int DALU = 2; // 频道页，大陆子项
	public static final int GANGTAI = 3; // 频道页，港台子项
	public static final int RIHAN = 4; // 频道页，日韩子项
	public static final int OUMEI = 5; // 频道页，欧美子项
	public static final int MORE = 6; // 频道页，更多子项

	public int loadMoviePage = 2; // 加载更多电影的下一页
	private HttpHelp mHttpHelp;

	private ArrayList<String> mfeauredNames; // 推荐名字集合
	private ArrayList<String> mfeauredIntroduce; // 推荐简介集合
	private ArrayList<String> mfeauredIcons; // 推荐图片集合
	private ArrayList<String> mfeauredId; // 推荐id集合
	private ArrayList<String> mfeauredHasfilm; // 是否有资源
	private ArrayList<String> mBF_num; // 播放数目
	private ArrayList<String> mPl_num; // 评论数目
	private ArrayList<String> mC_time; // 上映时间

	private View featuredView; // 像布局中添加动态布局
	private RefreshListView lv_child_home_title_featured; // 展示子菜单 的内容
	private MyChannelPageAdapter adapter; // 电影应用的适配器
	private FrameLayout fl;
	private ProgressBar pb;
	private Activity activity;
	private String url; // 初始加载url
	private String selectedType; // 选择频道类型 电影，电视，动漫，视频
	private String moreUrl; // 更多页面的url，联动搜索
	private boolean isOpen; // 当前状态是否是更多页面

	// 动态设置类型的标题
	private RadioButton rb_child_title_type_1;
	private RadioButton rb_child_title_type_2;
	private RadioButton rb_child_title_type_3;
	private RadioButton rb_child_title_type_4;
	private RadioButton rb_child_title_type_5;
	private RadioButton rb_child_title_type_6;

	private String[] mTypeNames = { "喜剧", "战争", "科幻", "动作", "恐怖", "爱情" }; // 电影类别
	private String[] tTypeNames = { "古装", "家庭", "历史", "偶像", "神话", "剧情" }; // 电视剧类别
	private String[] aTypeNames = { "搞笑", "真人", "少儿", "机战", "格斗", "校园" }; // 动漫类别
	private String[] vTypeNames = { "原创", "搞笑", "新闻", "自拍", "军事", "娱乐" }; // 视频类别
	// 所有地区字段数组
	private String[] areaAllNames = { "&area=dalu", "&area=xianggang",
			"&area=riben", "&area=hanguo", "&area=taiwan", "&area=meiguo" };
	// 所有年份字段集合
	private String[] timeAlls = { "&year=2015", "&year=2014", "&year=2013",
			"&year=2012", "&year=2011", "&year=2010" };
	// 所有电影类型字段集合
	private String[] mTypeAllNames = { "&ctype=xiju", "&ctype=zhanzheng",
			"&ctype=kehuan", "&ctype=dongzuo", "&ctype=kongbu", "&ctype=aiqing" };
	// 所有电视类型字段集合
	private String[] tTypeAllNames = { "&ctype=guzhuang", "&ctype=jiating",
			"&ctype=lishi", "&ctype=ouxiang", "&ctype=shenhua", "&ctype=juqing" };
	// 所有动漫类型字段集合
	private String[] aTypeAllNames = { "&ctype=gaoxiao", "&ctype=zhenren",
			"&ctype=shaoer", "&ctype=jizhan", "&ctype=gedou", "&ctype=xiaoyuan" };
	// 所有视频类型字段集合
	private String[] vTypeAllNames = { "&ctype=yuanchuang", "&ctype=gaoxiao",
			"&ctype=xinwen", "&ctype=zipai", "&ctype=junshi", "&ctype=yule" };
	// 所有其他类型字段集合
	private String[] OtherTypes = { "&order=bf_num", "&order=updatetime",
			"&order=pl_num" };

	String area = ""; // 地区字段值存储地方
	String type = ""; // 类型字段值存储地方
	String time = ""; // 年份字段值存储地方
	String otherTime = OtherTypes[0]; // 其他类型字段值存储地方

	public ShowPage() {
		init();
	}

	// 初始化数据
	private void init() {
		mHttpHelp = new HttpHelp();
		isOpen = isMoreChildPage();
		selectedType = getDtype();

		fl = getview_fl();
		pb = getview_pb();
		if (fl != null && pb != null) {
			fl.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);

		}

		activity = getmActivity();
		url = LoadingURL();

		featuredView = UIUtils.inflate(R.layout.child_home_title_featured);
		lv_child_home_title_featured = (RefreshListView) featuredView
				.findViewById(R.id.lv_child_home_title_featured);
		if (isOpen) {
			String mUrl = url + area + type + time + otherTime;
			moreUrl = mUrl;
			initPageData(mUrl);
		} else {
			initPageData(url);
		}
		loadMoreChildPage(isOpen, selectedType);
	}

	/**
	 * 判断用户是否点击了更多项
	 * 
	 * @param isOpen
	 * @param selectedType
	 */
	private void loadMoreChildPage(boolean isOpen, String selectedType) {

		RadioGroup mRg_child_home_title_area_anime; // 首页标题下的子页-->地区
		RadioGroup mRg_child_home_title_type_anime; // 首页标题下的子页-->类型
		RadioGroup mRg_child_home_title_time_anime; // 首页标题下的子页-->时间
		RadioGroup mRg_child_home_title_favourite_anime; // 首页标题下的子页-->最新热度

		if (isOpen) {
			View mHeaderView = UIUtils
					.inflate(R.layout.child_home_title_more_anime);
			mRg_child_home_title_area_anime = (RadioGroup) mHeaderView
					.findViewById(R.id.rg_child_home_title_area_anime);
			mRg_child_home_title_type_anime = (RadioGroup) mHeaderView
					.findViewById(R.id.rg_child_home_title_type_anime);
			mRg_child_home_title_time_anime = (RadioGroup) mHeaderView
					.findViewById(R.id.rg_child_home_title_time_anime);
			mRg_child_home_title_favourite_anime = (RadioGroup) mHeaderView
					.findViewById(R.id.rg_child_home_title_favourite_anime);

			mRg_child_home_title_favourite_anime
					.check(R.id.rb_child_title_more_paly);
			rb_child_title_type_1 = (RadioButton) mHeaderView
					.findViewById(R.id.rb_child_title_type_1);
			rb_child_title_type_2 = (RadioButton) mHeaderView
					.findViewById(R.id.rb_child_title_type_2);
			rb_child_title_type_3 = (RadioButton) mHeaderView
					.findViewById(R.id.rb_child_title_type_3);
			rb_child_title_type_4 = (RadioButton) mHeaderView
					.findViewById(R.id.rb_child_title_type_4);
			rb_child_title_type_5 = (RadioButton) mHeaderView
					.findViewById(R.id.rb_child_title_type_5);
			rb_child_title_type_6 = (RadioButton) mHeaderView
					.findViewById(R.id.rb_child_title_type_6);

			RadioButton[] mRbs = { rb_child_title_type_1,
					rb_child_title_type_2, rb_child_title_type_3,
					rb_child_title_type_4, rb_child_title_type_5,
					rb_child_title_type_6 };
			setRadioButtonName(selectedType, mRbs);

			mRg_child_home_title_area_anime.setOnCheckedChangeListener(this);
			mRg_child_home_title_type_anime.setOnCheckedChangeListener(this);
			mRg_child_home_title_time_anime.setOnCheckedChangeListener(this);
			mRg_child_home_title_favourite_anime
					.setOnCheckedChangeListener(this);

			lv_child_home_title_featured.addHeaderView(mHeaderView);

		}
	}

	/**
	 * 根据用户的选择的页面，（视频，动漫，电影，电视剧） 动态设置rb的名字
	 * 
	 * @param type
	 * @param mRbs
	 */
	private void setRadioButtonName(String type, RadioButton[] mRbs) {
		switch (type) {
		case MOVIE:
			setName(mTypeNames, mRbs);
			break;
		case TV:
			setName(tTypeNames, mRbs);
			break;
		case ANIM:
			setName(aTypeNames, mRbs);
			break;
		case VIDEO:
			setName(vTypeNames, mRbs);
			break;
		default:
			break;
		}
	}

	/**
	 * 对相应的控件进行设值。。
	 * 
	 * @param name
	 * @param Rbs
	 */
	private void setName(String[] name, RadioButton[] Rbs) {
		for (int i = 0; i < name.length; i++) {
			Rbs[i].setText(name[i]);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {

		case R.id.rb_child_title_area_all: // 地区:全部
			area = "";
			break;
		case R.id.rb_child_title_area_cn: // 地区：中国
			area = areaAllNames[0];
			break;
		case R.id.rb_child_title_area_hk:// 地区：香港
			area = areaAllNames[1];
			break;
		case R.id.rb_child_title_area_jp:// 地区：日本
			area = areaAllNames[2];
			break;
		case R.id.rb_child_title_area_kr:// 地区：韩国
			area = areaAllNames[3];
			break;
		case R.id.rb_child_title_area_tw:// 地区：台湾
			area = areaAllNames[4];
			break;
		case R.id.rb_child_title_area_usa:// 地区：美国
			area = areaAllNames[5];
			break;
		case R.id.rb_child_title_type_all: // 全部
			String[] typeUrl_all = setTypeUrl(selectedType);
			if (typeUrl_all != null) {
				type = "";
			}
			break;
		case R.id.rb_child_title_type_1: // 喜剧 古装 搞笑 原创
			String[] typeUrl_1 = setTypeUrl(selectedType);
			if (typeUrl_1 != null) {
				type = typeUrl_1[0];
			}
			break;
		case R.id.rb_child_title_type_2: // 战争 家庭 真人 搞笑
			String[] typeUrl_2 = setTypeUrl(selectedType);
			if (typeUrl_2 != null) {
				type = typeUrl_2[1];
			}
			break;
		case R.id.rb_child_title_type_3: // 科幻 历史 少儿 新闻
			String[] typeUrl_3 = setTypeUrl(selectedType);
			if (typeUrl_3 != null) {
				type = typeUrl_3[2];
			}
			break;
		case R.id.rb_child_title_type_4: // 动作 偶像 机战 自拍
			String[] typeUrl_4 = setTypeUrl(selectedType);
			if (typeUrl_4 != null) {
				type = typeUrl_4[3];
			}
			break;
		case R.id.rb_child_title_type_5: // 恐怖 神话 格斗 军事
			String[] typeUrl_5 = setTypeUrl(selectedType);
			if (typeUrl_5 != null) {
				type = typeUrl_5[4];
			}
			break;
		case R.id.rb_child_title_type_6: // 爱情 剧情 校园 娱乐
			String[] typeUrl_6 = setTypeUrl(selectedType);
			if (typeUrl_6 != null) {
				type = typeUrl_6[5];
			}
			break;
		case R.id.rb_child_title_time_all: // 年份：全部
			time = "";
			break;
		case R.id.rb_child_title_time_2015: // 年份：2015年
			time = timeAlls[0];
			break;
		case R.id.rb_child_title_time_2014: // 年份：2014年
			time = timeAlls[1];
			break;
		case R.id.rb_child_title_time_2013: // 年份：2013年
			time = timeAlls[2];
			break;
		case R.id.rb_child_title_time_2012: // 年份：2012年
			time = timeAlls[3];
			break;
		case R.id.rb_child_title_time_2011: // 年份：2011年
			time = timeAlls[4];
			break;
		case R.id.rb_child_title_time_2010: // 年份：2010年
			time = timeAlls[5];
			break;
		case R.id.rb_child_title_more_paly: // 最多搜索的
			otherTime = OtherTypes[0];
			orderType = 0;
			break;
		case R.id.rb_child_title_new_update: // 最新上线的
			otherTime = OtherTypes[1];
			orderType = 1;
			break;
		case R.id.rb_child_title_comment_more: // 评论最多的
			otherTime = OtherTypes[2];
			orderType = 2;
			break;
		default:
			break;
		}

		moreUrl = url + area + type + time + otherTime;
		initPageData(moreUrl);// 加载更多界面的网络数据
	}

	/**
	 * 根据用户选择的类型确定相应的字段值
	 * 
	 * @param str
	 * @return
	 */
	private String[] setTypeUrl(String str) {
		switch (str) {
		case MOVIE:
			return mTypeAllNames;
		case TV:
			return tTypeAllNames;
		case ANIM:
			return aTypeAllNames;
		case VIDEO:
			return vTypeAllNames;
		}
		return null;

	}

	/**
	 * 第一次请求网络页面初始化
	 * 
	 * @param u
	 */
	private int orderType = 0;

	private void initPageData(String u) {
		if (mHttpHelp != null) {
			mHttpHelp.stopHttpNET();
		}
		mHttpHelp.sendGet(u, TVbean.class, new MyRequestCallBack<TVbean>() {
			@Override
			public void onSucceed(TVbean bean) {
				// mBean = bean;
				if (bean == null) {
					return;
				}
				fl.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
				initNetworkData_ap(true, bean);
				setViewAdapter_ap(bean, orderType);
				fl.removeAllViews();
				fl.addView(featuredView);
			}
		});
	}

	/**
	 * 设置展示的listview适配器
	 * 
	 * @param bean
	 */
	private void setViewAdapter_ap(TVbean bean, int type) {
		adapter = new MyChannelPageAdapter(mfeauredNames, true, activity, type);
		adapter.setAdata(mfeauredNames, mfeauredIntroduce, mfeauredIcons,
				mfeauredId, mfeauredHasfilm, mBF_num,mPl_num,mC_time);

		lv_child_home_title_featured.setAdapter(adapter);
		lv_child_home_title_featured.setOnRefreshListener(this);
	}

	/**
	 * 获取网络请求的数据进行加载。 第一次加载的时候传入true 多次加载传入false
	 * 
	 * @param b
	 * @param bean
	 */
	private void initNetworkData_ap(boolean b, TVbean bean) {
		List<TV> baseBean = bean.cont;
		if (baseBean == null || baseBean.size() < 6) {
			if (mfeauredNames == null || mfeauredNames.size() <= 0) {
				UIUtils.showToast(UIUtils.getContext(), "暂未开通");
			} else {
				UIUtils.showToast(UIUtils.getContext(), "没有更多的数据了，亲~~~");
			}
			return;
		}
		if (b) {
			mfeauredNames = new ArrayList<String>();
			mfeauredIntroduce = new ArrayList<String>();
			mBF_num = new ArrayList<String>();
			mPl_num = new ArrayList<String>();
			mC_time = new ArrayList<String>();
			mfeauredIcons = new ArrayList<String>();
			mfeauredId = new ArrayList<String>();
			mfeauredHasfilm = new ArrayList<String>();

			mfeauredNames.clear();
			mfeauredIntroduce.clear();
			mfeauredIcons.clear();
			mBF_num.clear();
			mPl_num.clear();
			mC_time.clear();
			mfeauredId.clear();
			mfeauredHasfilm.clear();
		}

		for (int i = 0; i < baseBean.size(); i++) {
			mfeauredNames.add(baseBean.get(i).name);
			mBF_num.add(baseBean.get(i).bf_num);
			mPl_num.add(baseBean.get(i).pl_num);
			mC_time.add(baseBean.get(i).ctime);
			mfeauredIntroduce.add(baseBean.get(i).sdesc);
			mfeauredIcons.add(baseBean.get(i).pic);
			mfeauredId.add(baseBean.get(i).id);
			mfeauredHasfilm.add(baseBean.get(i).hasfilm);
		}

	}

	/**
	 * 实现上拉加载监听
	 */
	@Override
	public void onLoadMore() {

		String lUrl = null;
		if (isOpen) {
			lUrl = moreUrl + mHttpHelp.FLAG_PAGE + loadMoviePage;
		} else {
			lUrl = url + mHttpHelp.FLAG_PAGE + loadMoviePage;
		}

		mHttpHelp.sendGet(lUrl, TVbean.class, new MyRequestCallBack<TVbean>() {

			@Override
			public void onSucceed(TVbean bean) {
				if (bean == null || bean.cont == null) {
					lv_child_home_title_featured.onRefreashFinish();
					UIUtils.showToast(UIUtils.getContext(), "没有更多了");
					return;
				}
				lv_child_home_title_featured.onRefreashFinish();
				if ("1".equals(bean.status) && bean.cont.size() > 0) {
					loadMoviePage++;
					initNetworkData_ap(false, bean); // 上拉加载的时候再次设置网络数据
					adapter.notifyDataSetChanged();
				}
			}
		});

	}

	/**
	 * 获取到界面的地址
	 * 
	 * @param dtype
	 *            总分类~~~~电影，电视，动漫，视频
	 * @param current
	 *            各个分类下的子分类
	 * @return
	 */
	public static String getURL(String dtype, int current, String childType) {
		String url = "";
		switch (current) {
		case FEATURED:
			url = NetworkConfig.getChannelIndex() + dtype + childType;
			break;
		case DALU:
			url = NetworkConfig.getChannelIndex() + dtype + childType;
			break;
		case GANGTAI:
			url = NetworkConfig.getChannelIndex() + dtype + childType;
			break;
		case RIHAN:
			url = NetworkConfig.getChannelIndex() + dtype + childType;
			break;
		case OUMEI:
			url = NetworkConfig.getChannelIndex() + dtype + childType;

			break;
		case MORE:
			url = NetworkConfig.getChannelIndex() + dtype;
			break;

		default:
			break;
		}
		return url;
	}

	/**
	 * 外界返回的url
	 * 
	 * @return
	 */
	public abstract String LoadingURL();

	/**
	 * 判断当前是否是更多标签
	 * 
	 * @return
	 */
	public abstract boolean isMoreChildPage();

	/**
	 * 获取FrameLayout控件 ,添加内容布局
	 */
	public abstract FrameLayout getview_fl();

	/**
	 * 获取ProgressBar控件 ,控制是否隐藏
	 */
	public abstract ProgressBar getview_pb();

	/**
	 * 获取activity
	 * 
	 * @return
	 */
	public abstract Activity getmActivity();

	/**
	 * 获取用户选择的类别
	 * 
	 * @return
	 */
	public abstract String getDtype();

}
