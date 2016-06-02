package com.limaoso.phonevideo.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.MainActivity;
import com.limaoso.phonevideo.activity.QuitLoginActivity;
import com.limaoso.phonevideo.activity.home.MovieActivity;
import com.limaoso.phonevideo.activity.menu.CollectDetailActivity;
import com.limaoso.phonevideo.activity.menu.InformMenuDetailActivity;
import com.limaoso.phonevideo.activity.menu.NetworkMenuDetailActivity;
import com.limaoso.phonevideo.activity.menu.PlayCacheAcivity;
import com.limaoso.phonevideo.activity.menu.RecommendMenuDetailActivity;
import com.limaoso.phonevideo.activity.menu.RecordDetailActivity;
import com.limaoso.phonevideo.activity.menu.SettingMenuDetailActivity;
import com.limaoso.phonevideo.activity.menu.VIPMenuDetailActivity;
import com.limaoso.phonevideo.bean.UserBean;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.LoadRequestCallBack;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.CheckUtils;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.CircleImageView;

/**
 * 左侧边栏
 * 
 * @author liang
 * 
 */
public class LeftMenuFragment extends BaseFragment implements OnClickListener {
	public final static String ALREADY_READ = "already_read";// 已经读取消息
	private HttpHelp httpHelp;
	private ListView lv_list; // 左侧边栏
	public MenuAdapter mAdapter;
	private ArrayList<String> mMenuList; // 设置左边栏的标题集合
	private Button btn_record; // 记录按钮
	private Button btn_cache; // 缓存按钮
	private Button btn_collect; // 收藏按钮
	private MainActivity main;
	private Context content;
	private Receiver receiver;
	private CircleImageView ci_left_user_icon;// 左侧头像
	private TextView tv_user_name;// 左侧名字
	private ContentFragment contentFragment;
	private boolean neticon = false;

	private SlidingMenu slidingMenu;

	@Override
	public View initViews() {

		// 文字集合
		mMenuList = new ArrayList<String>();
		mMenuList.add("上网必备");
		mMenuList.add("电影频道");
		mMenuList.add("消息通知");
		mMenuList.add("必看推荐");
		mMenuList.add("热门活动");
		mMenuList.add("播放设置");
		// mMenuList.add("积分商城");
		init();
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		lv_list = (ListView) view.findViewById(R.id.lv_list);
		btn_record = (Button) view.findViewById(R.id.btn_record);
		btn_cache = (Button) view.findViewById(R.id.btn_cache);
		btn_collect = (Button) view.findViewById(R.id.btn_collect);
		ci_left_user_icon = (CircleImageView) view
				.findViewById(R.id.ci_left_user_icon);
		tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);

		btn_cache.setOnClickListener(this);
		btn_record.setOnClickListener(this);
		btn_collect.setOnClickListener(this);

		main = (MainActivity) mActivity;
		contentFragment = main.getContentFragment();
		slidingMenu = main.getSlidingMenu();

		mAdapter = new MenuAdapter();
		lv_list.setAdapter(mAdapter);

		httpHelp = new HttpHelp();
		// if (SystemUtils.getSystemVersion() >= SystemUtils.V4_0) {
		// ci_left_user_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		// }
		initDatas();
		return view;
	}

	private void initDatas() {
		if (!PrefUtils.getBoolean(UIUtils.getContext(), GlobalConstant.QUIT,
				false)) {
			tv_user_name.setText(PrefUtils.getString(
					GlobalConstant.USER_NICKNAME, ""));
			// 看本地有没有存有头像，
			if (CheckUtils.isIcon(PrefUtils.getString(UIUtils.getContext(),
					GlobalConstant.HEAD_ICON, null))) {
				httpHelp.showImage(
						ci_left_user_icon,
						PrefUtils.getString(UIUtils.getContext(),
								GlobalConstant.HEAD_ICON, null) + "##");
				neticon = false;
			} else {
				netWork();
			}
		} else {
			ci_left_user_icon.setImageResource(R.drawable.wdltx);
			tv_user_name.setText("未登录……");
		}
	}

	private void init() {
		IntentFilter filter = new IntentFilter(ALREADY_READ);
		if (broadcastReceiver == null) {
			broadcastReceiver = new MyBroadcastReceiver();
		}
		mActivity.registerReceiver(broadcastReceiver, filter);

	}

	private class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ALREADY_READ.equals(intent.getAction())) {
				if (mAdapter != null) {
					mAdapter.notifyDataSetInvalidated();
				}
			}

		}

	}

	private void netWork() {
		httpHelp.sendGet(NetworkConfig.getMyData("leftMeNUfRAGMENT"),
				UserBean.class, new MyRequestCallBack<UserBean>() {

					@Override
					public void onSucceed(UserBean bean) {
						if (bean == null) {
							return;
						}
						if (neticon) {
							httpHelp.showImage(ci_left_user_icon,
									bean.cont.face + "##");
							// httpHelp = new HttpHelp();
							httpHelp.downLoad(bean.cont.face,
									GlobalConstant.HEAD_ICON_PATH,
									new LoadRequestCallBack() {

										@Override
										public void onLoading(long total,
												long current,
												boolean isUploading) {
										}

										@Override
										public void onSucceed(ResponseInfo t) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onFailure(
												HttpException arg0, String arg1) {
											// TODO Auto-generated method stub

										}
									});
							PrefUtils.setString(UIUtils.getContext(),
									GlobalConstant.HEAD_ICON,
									GlobalConstant.HEAD_ICON_PATH);
						}
						tv_user_name.setText(bean.cont.nickname);
					}
				});
	}

	@Override
	public void initData() {
		update();
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
				if (!PrefUtils.getBoolean(UIUtils.getContext(),
						GlobalConstant.IS_LOGINED, false)) {
					mActivity.startActivity(new Intent(mActivity,
							QuitLoginActivity.class));
					return;
				}
				Intent intent = null;
				switch (position) {
				case 0:
					// 若当前点击的条目为频道时，就直接让其跳转到主页的频道栏
					intent = new Intent(mActivity,
							NetworkMenuDetailActivity.class);
					break;
				case 1:
					// 若当前点击的条目为频道时，就直接让其跳转到主页的频道栏
					intent = new Intent(mActivity, MovieActivity.class);
					break;
				case 2:
					// 若当前点击的条目为频道时，就直接让其跳转到主页的频道栏
					intent = new Intent(mActivity,
							InformMenuDetailActivity.class);
					break;
				case 3:
					intent = new Intent(mActivity,
							RecommendMenuDetailActivity.class);
					break;
				case 4:
					intent = new Intent(mActivity, VIPMenuDetailActivity.class);
					break;
				case 5:
					// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
					intent = new Intent(main, SettingMenuDetailActivity.class);
					break;

				}
				if (intent != null) {
					mActivity.startActivity(intent);
					// 隐藏
					new Handler().postDelayed(new DelayRunnable(), 1000);
				}
			}
		});
	}

	private class DelayRunnable implements Runnable {

		@Override
		public void run() {
			toggleSlidingMenu();
		}

	}

	/**
	 * 切换SlidingMenu的状态
	 * 
	 * @param b
	 */
	protected void toggleSlidingMenu() {

		slidingMenu.toggle();// 切换状态, 显示时隐藏, 隐藏时显示
	}

	/**
	 * 将左侧菜单栏中的点击 位置传到contentfragment中
	 * 
	 * @param position
	 */
	public void setCurrentMenuDetailPager(int position) {
		ContentFragment fragment = main.getContentFragment();// 获取主页面fragment
		fragment.setCurrentMenuDetailPager(position);
	}

	/**
	 * 侧边栏数据适配器
	 * 
	 * @author Kevin
	 * 
	 */
	public class MenuAdapter extends BaseAdapter {
		private ViewHolder viewHolder;

		@Override
		public int getCount() {
			return mMenuList.size();
		}

		@Override
		public String getItem(int position) {
			return mMenuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_menu_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.iv_left_menu_iconl = (ImageView) convertView
						.findViewById(R.id.iv_left_menu_iconl);
				viewHolder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewHolder.tv_lmi_num = (TextView) convertView
						.findViewById(R.id.tv_lmi_num);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// setBadgeView(position, tvTitle, ll_left_menu_layout);

			if (position == 2) {
				String str = PrefUtils.getString(mActivity,
						GlobalConstant.INFORM_NUM, null);
				if (str != null) {
					String[] split = str.split("##");
					if (Integer.parseInt(split[0]) + Integer.parseInt(split[1]) == 0) {
						viewHolder.tv_lmi_num.setVisibility(View.GONE);
					} else {
						viewHolder.tv_lmi_num.setVisibility(View.VISIBLE);
						viewHolder.tv_lmi_num.setText(""
								+ (Integer.parseInt(split[0]) + Integer
										.parseInt(split[1])));
					}
				}
			} else {
				viewHolder.tv_lmi_num.setVisibility(View.GONE);
			}
			String title = getItem(position);
			viewHolder.tvTitle.setText(title);
			viewHolder.iv_left_menu_iconl.setImageLevel(position);

			return convertView;
		}

	}

	class ViewHolder {
		public TextView tvTitle;
		public ImageView iv_left_menu_iconl;
		public TextView tv_lmi_num;
	}

	/**
	 * 设置消息通知的消息提醒数字
	 * 
	 * @param position
	 * @param tvTitle
	 * @param ll
	 */
	private BadgeView mBadgeView;
	private MyBroadcastReceiver broadcastReceiver;

	private void setBadgeView(int position, TextView tvTitle, RelativeLayout ll) {
		if (position == 2) {
			String str = PrefUtils.getString(mActivity,
					GlobalConstant.INFORM_NUM, null);
			if (str == null) {
				return;
			}
			String[] split = str.split("##");
			int informNum = Integer.parseInt(split[0])
					+ Integer.parseInt(split[1]);

			if (mBadgeView != null) {
				ll.removeView(mBadgeView);
			}
			// l
			mBadgeView = new BadgeView(mActivity);
			mBadgeView.setBadgeCount(informNum);
			// FrameLayout.LayoutParams params = (LayoutParams) mBadgeView
			// .getLayoutParams();
			RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mBadgeView
					.getLayoutParams();
			// params.leftMargin = 10;
			// params.gravity = Gravity.CENTER_VERTICAL;
			// params.t
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			ll.addView(mBadgeView, params);
		}
	}

	public MenuAdapter getLVAdapter() {
		return mAdapter;
	}

	@Override
	public void onClick(View arg0) {
		if (!PrefUtils.getBoolean(UIUtils.getContext(),
				GlobalConstant.IS_LOGINED, false)) {
			main.startActivity(new Intent(main, QuitLoginActivity.class));
			return;
		}
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.btn_record:
			// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
			intent = new Intent(main, RecordDetailActivity.class);
			break;
		case R.id.btn_cache:
			// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
			intent = new Intent(main, PlayCacheAcivity.class);
			break;
		case R.id.btn_collect:
			// 判断当前状况是否是获取到用户信息，未获取用户信息跳到登陆界面，否则进入相应的界面
			intent = new Intent(main, CollectDetailActivity.class);
			break;
		}
		if (intent != null) {
			mActivity.startActivity(intent);
			new Handler().postDelayed(new DelayRunnable(), 800);
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
			initDatas();

			// content.unregisterReceiver(receiver);
		}

	}

	@Override
	public void onDestroy() {
		if (broadcastReceiver != null) {
			mActivity.unregisterReceiver(broadcastReceiver);
		}
		super.onDestroy();
	}

}
