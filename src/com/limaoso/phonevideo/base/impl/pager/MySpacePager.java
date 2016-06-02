package com.limaoso.phonevideo.base.impl.pager;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.MainActivity;
import com.limaoso.phonevideo.activity.MyDataActivity;
import com.limaoso.phonevideo.activity.PlaySourceActivity;
import com.limaoso.phonevideo.activity.QuitLoginActivity;
import com.limaoso.phonevideo.activity.menu.CollectDetailActivity;
import com.limaoso.phonevideo.activity.menu.PlayCacheAcivity;
import com.limaoso.phonevideo.activity.menu.RecordDetailActivity;
import com.limaoso.phonevideo.activity.menu.SettingMenuDetailActivity;
import com.limaoso.phonevideo.adapter.MyGridAndListViewBaseAdapter;
import com.limaoso.phonevideo.base.BasePager;
import com.limaoso.phonevideo.bean.HomeBean;
import com.limaoso.phonevideo.bean.TVbean;
import com.limaoso.phonevideo.bean.UserCenter;
import com.limaoso.phonevideo.fragment.LeftMenuFragment;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.holder.AllViewholder;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.LoadRequestCallBack;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.AnimUtil;
import com.limaoso.phonevideo.utils.CheckUtils;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.CircleImageView;

/**
 * 个人中心实现
 * 
 * @author liang
 * 
 */
public class MySpacePager extends BasePager implements OnItemClickListener {

	Activity activity;

	private HttpHelp httpHelp;
	private UserCenter userRecommend;

	private View mFrame_my_space_pager; // 个人中心的内容展示
	private RelativeLayout mRl_my_space_record; // 记录按钮
	private RelativeLayout mRl_my_space_cache; // 缓存按钮
	private RelativeLayout mRl_my_space_collect; // 收藏按钮
	private RelativeLayout mRl_my_space_setting; // 设置按钮
	private RelativeLayout mRl_my_space_user_date; // 进入用户资料
	private GridView mGv_my_space_recommend_show; // 展示精彩推荐的电影

	private LeftMenuFragment fragment; // 左侧边栏

	private View iv_myspace_recommend;
	private MyspaceGirdAdapter myspaceGirdAdapter;
	private int myspaceRecommendPage = 2;// 推荐下一页
	private AnimUtil animUtil;// 设置动画
	private String centerUrl;
	private CircleImageView iv_left_user_icon;// 用户头像
	private TextView tv_mysapce_nickname;// 用户名字
	private TextView tv_myspace_integral;// 用户积分
	private TextView tv_myspace_id;// 用户id
	private boolean neticon = false;
	private Context content;
	private Receiver receiver;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		};

	};

	public MySpacePager(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	@Override
	public void initData() {
		super.initData();
		isOtherPager(1);
		setSlidingMenuEnable(true);
		// inLoading();
		LoadingSuccess();
		// 获取左侧边栏的管理器
		MainActivity main = (MainActivity) activity;
		fragment = main.getLeftMenuFragment();
		init();// 初始化
		initMyData();
		update();
		setListener();// 设置监听器
		requestNetworkdate_login();
		flContent.removeAllViews();
		flContent.addView(mFrame_my_space_pager);
	}

	private void initMyData() {
		// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
		if (!PrefUtils.getBoolean(GlobalConstant.QUIT, false)) {
			// 看本地有没有存有头像，
			String iconUrl = PrefUtils.getString(UIUtils.getContext(),
					GlobalConstant.HEAD_ICON, "");
			if (CheckUtils.isIcon(iconUrl)) {
				httpHelp.showImage(iv_left_user_icon, iconUrl + "##");
				neticon = false;
			} else {
				neticon = true;
			}
			tv_mysapce_nickname.setText(PrefUtils.getString(
					GlobalConstant.USER_NICKNAME, ""));
			if (!"".equals(PrefUtils.getString(GlobalConstant.USER_SCORE, ""))) {
				tv_myspace_integral.setText(PrefUtils.getString(
						GlobalConstant.USER_SCORE, ""));
			} else {
				tv_myspace_integral.setText("0");
			}
			tv_myspace_id.setText(PrefUtils.getString(GlobalConstant.USER_ID,
					""));

		} else {
			iv_left_user_icon.setImageResource(R.drawable.wdltx);
			tv_mysapce_nickname.setText("未登录……");
			tv_myspace_integral.setText("0");
		}

	}

	private void update() {
		if (content == null) {
			content = UIUtils.getContext();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction("myspaceUpdate");
		receiver = new Receiver();
		content.registerReceiver(receiver, filter);
	}

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			initMyData();
			if (neticon) {
				requestNetworkdate_login();
			}
		}

	}

	private void setListener() {
		mGv_my_space_recommend_show.setOnItemClickListener(this);
		// setHomeItemClick(mGv_my_space_recommend_show, FLAG_INDEXREC);
		// 设置4个按钮的点击监听事件
		mRl_my_space_record.setOnClickListener(this);
		mRl_my_space_cache.setOnClickListener(this);
		mRl_my_space_collect.setOnClickListener(this);
		mRl_my_space_setting.setOnClickListener(this);
		// mTv_my_space_upload.setOnClickListener(new MyOnClickListener()); 暂时隐藏
		mRl_my_space_user_date.setOnClickListener(this);
	}

	private void setAdapter() {
		myspaceGirdAdapter = new MyspaceGirdAdapter(userRecommend.cont.recku);
		mGv_my_space_recommend_show.setAdapter(myspaceGirdAdapter);
	}

	class MyspaceGirdAdapter extends MyGridAndListViewBaseAdapter {

		public MyspaceGirdAdapter(List list) {
			super(list, false);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			AllViewholder homeChildPagerViewholder = null;
			if (convertView == null) {
				convertView = UIUtils.inflate(R.layout.list_item_home);
				homeChildPagerViewholder = new AllViewholder();

				homeChildPagerViewholder.movieIcon = (ImageView) convertView
						.findViewById(R.id.iv_item_home_icon);
				homeChildPagerViewholder.movieName = (TextView) convertView
						.findViewById(R.id.tv_item_home_title);
				homeChildPagerViewholder.movieIntroduce = (TextView) convertView
						.findViewById(R.id.tv_item_home_introduce);

				homeChildPagerViewholder.tv_lih_show = (TextView) convertView
						.findViewById(R.id.tv_lih_show);

				convertView.setTag(homeChildPagerViewholder);

			} else {
				homeChildPagerViewholder = (AllViewholder) convertView.getTag();
			}
			if (homeChildPagerViewholder != null) {
				/*
				 * homeChildPagerViewholder.movieIcon
				 * .setImageResource(mfeauredIcons.get(position));
				 */
				httpHelp.showImage(homeChildPagerViewholder.movieIcon,
						userRecommend.cont.recku.get(position).pic);
				if (userRecommend.cont.recku.get(position).bf_num == null) {
					homeChildPagerViewholder.tv_lih_show.setText((int) (Math
							.random() * 10000) + "人搜");
				} else {
					homeChildPagerViewholder.tv_lih_show
							.setText(userRecommend.cont.recku.get(position).bf_num
									+ "人搜");
				}
				homeChildPagerViewholder.movieName
						.setText(userRecommend.cont.recku.get(position).name);
				homeChildPagerViewholder.movieIntroduce
						.setText(userRecommend.cont.recku.get(position).sdesc);
			}

			return convertView;
		}

	}

	private void init() {
		mFrame_my_space_pager = UIUtils.inflate(R.layout.frame_my_space_pager);
		mRl_my_space_record = (RelativeLayout) mFrame_my_space_pager
				.findViewById(R.id.rl_my_space_record);
		mRl_my_space_cache = (RelativeLayout) mFrame_my_space_pager
				.findViewById(R.id.rl_my_space_cache);
		mRl_my_space_collect = (RelativeLayout) mFrame_my_space_pager
				.findViewById(R.id.rl_my_space_collect);
		mRl_my_space_setting = (RelativeLayout) mFrame_my_space_pager
				.findViewById(R.id.rl_my_space_setting);

		mRl_my_space_user_date = (RelativeLayout) mFrame_my_space_pager
				.findViewById(R.id.rl_my_space_user_date);
		mGv_my_space_recommend_show = (GridView) mFrame_my_space_pager
				.findViewById(R.id.gv_my_space_recommend_show);
		iv_myspace_recommend = mFrame_my_space_pager
				.findViewById(R.id.iv_myspace_recommend);

		iv_left_user_icon = (CircleImageView) mFrame_my_space_pager
				.findViewById(R.id.iv_left_user_icon);
		tv_mysapce_nickname = (TextView) mFrame_my_space_pager
				.findViewById(R.id.tv_mysapce_nickname);
		tv_myspace_integral = (TextView) mFrame_my_space_pager
				.findViewById(R.id.tv_myspace_integral);
		tv_myspace_id = (TextView) mFrame_my_space_pager
				.findViewById(R.id.tv_myspace_id);

		delateItembackgroud(mGv_my_space_recommend_show);
		httpHelp = new HttpHelp();
		mRl_my_space_user_date.setFocusable(true);
		mRl_my_space_user_date.setFocusableInTouchMode(true);
		mRl_my_space_user_date.requestFocus();

		// if (SystemUtils.getSystemVersion() >= SystemUtils.V4_0) {
		// iv_left_user_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		// }

	}

	@Override
	public String getCurrentTitle() {
		return "个人中心";
	}

	/**
	 * 请求的网络数据
	 */
	private void requestNetworkdate_login() {
		String userCenter = null;
		if (PrefUtils.getBoolean(UIUtils.getContext(),
				GlobalConstant.IS_LOGINED, false)) {
			userCenter = NetworkConfig.getUserCenter();
		} else {
			userCenter = NetworkConfig.getUserCenter_un();
		}
		if (userCenter == null) {
			return;
		}
		httpHelp.sendGet(userCenter, UserCenter.class,
				new MyRequestCallBack<UserCenter>() {

					@Override
					public void onSucceed(UserCenter bean) {
						if (bean == null) {
							return;
						}
						// LoadingSuccess();
						userRecommend = bean;
						centerUrl = bean.url.recku;
						if (neticon) {
							if (bean.cont.baseinfo != null) {
								if (neticon) {
									httpHelp.showImage(iv_left_user_icon,
											bean.cont.baseinfo.face + "##");
									downloadHeadIcon(bean.cont.baseinfo.face);
								}
								tv_mysapce_nickname
										.setText(bean.cont.baseinfo.nickname);
								tv_myspace_integral
										.setText(bean.cont.baseinfo.score);
							}
						}
						setAdapter();
						initAnim();// 初始化动画
					}
				});
	}

	private void downloadHeadIcon(String url) {
		if (url == null) {
			return;
		}
		httpHelp.downLoad(url, GlobalConstant.HEAD_ICON_PATH,
				new LoadRequestCallBack() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSucceed(ResponseInfo t) {
						PrefUtils.setString(GlobalConstant.HEAD_ICON,
								GlobalConstant.HEAD_ICON_PATH);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void initAnim() {
		setAnim(iv_myspace_recommend, myspaceGirdAdapter);
	}

	/**
	 * 设置动画
	 * 
	 * @param view
	 * @param flag
	 * @param adapter
	 */
	private void setAnim(final View view,
			final MyspaceGirdAdapter myspaceGirdAdapter) {

		if (animUtil == null) {
			animUtil = new AnimUtil();
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				view.setFocusable(false);
				view.setClickable(false);
				animUtil.startRotateAnimation(view);

				String url = centerUrl + httpHelp.FLAG_PAGE
						+ myspaceRecommendPage;

				httpHelp.sendGet(url, TVbean.class,
						new MyRequestCallBack<TVbean>() {

							@Override
							public void onSucceed(TVbean bean) {
								view.setFocusable(true);
								view.setClickable(true);
								if (bean == null) {
									view.clearAnimation();
									return;
								}
								if (bean.cont == null || bean.cont.size() < 6) {
									UIUtils.showToast(UIUtils.getContext(),
											"没有更多数据");
									view.clearAnimation();
									return;
								}
								userRecommend.cont.recku = bean.cont;
								myspaceRecommendPage++;
								myspaceGirdAdapter.notifyDataSetChanged();
								view.clearAnimation();
							}
						});

			}
		});
	}

	/**
	 * 界面更新
	 * 
	 * @param flag
	 * @return
	 */

	/**
	 * 设置点击事件的监听类
	 * 
	 * @author liang
	 * 
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_my_space_record: // 记录
			// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
			if (!PrefUtils.getBoolean(UIUtils.getContext(),
					GlobalConstant.QUIT, false)) {
				// fragment.setCurrentMenuDetailPager(11); // 进入左侧边栏的记录界面
				activity.startActivity(new Intent(activity,
						RecordDetailActivity.class));
			} else {
				activity.startActivity(new Intent(activity,
						QuitLoginActivity.class));
			}

			break;
		case R.id.rl_my_space_cache: // 缓存
			// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
			if (!PrefUtils.getBoolean(UIUtils.getContext(),
					GlobalConstant.QUIT, false)) {
				// fragment.setCurrentMenuDetailPager(12); // 进入左侧边栏的缓存界面
				activity.startActivity(new Intent(activity,
						PlayCacheAcivity.class));
			} else {
				activity.startActivity(new Intent(activity,
						QuitLoginActivity.class));
			}

			break;
		case R.id.rl_my_space_collect: // 收藏
			// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
			if (!PrefUtils.getBoolean(UIUtils.getContext(),
					GlobalConstant.QUIT, false)) {
				// fragment.setCurrentMenuDetailPager(13); // 进入左侧边栏的收藏
				activity.startActivity(new Intent(activity,
						CollectDetailActivity.class));
			} else {
				activity.startActivity(new Intent(activity,
						QuitLoginActivity.class));
			}

			break;

		case R.id.rl_my_space_setting: // 设置
			if (!PrefUtils.getBoolean(UIUtils.getContext(),
					GlobalConstant.QUIT, false)) {
				// fragment.setCurrentMenuDetailPager(9); // 进入左侧边栏的个人设置界面
				activity.startActivity(new Intent(activity,
						SettingMenuDetailActivity.class));
			} else {
				activity.startActivity(new Intent(activity,
						QuitLoginActivity.class));
			}
			break;

		case R.id.rl_my_space_user_date: // 编辑用户资料
			// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
			if (!PrefUtils.getBoolean(UIUtils.getContext(),
					GlobalConstant.QUIT, false)) {
				activity.startActivity(new Intent(activity,
						MyDataActivity.class));
			} else {
				activity.startActivity(new Intent(activity,
						QuitLoginActivity.class));
			}
			break;
		case R.id.btn_menu:
			toggleSlidingMenu();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if ("0".equals(userRecommend.cont.recku.get(position).hasfilm)) {

			UIUtils.showToast(mActivity,
					userRecommend.cont.recku.get(position).name + "暂时无资源……");
			return;
		}
		Intent intent = new Intent(mActivity, PlaySourceActivity.class);
		String str = userRecommend.cont.recku.get(position).id;
		intent.putExtra(GlobalConstant.TV_ID, str);
		mActivity.startActivity(intent);

	}

}
