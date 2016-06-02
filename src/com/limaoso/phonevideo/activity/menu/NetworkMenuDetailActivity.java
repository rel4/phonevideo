package com.limaoso.phonevideo.activity.menu;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.APPDetailActivity;
import com.limaoso.phonevideo.activity.BaseActivity;
import com.limaoso.phonevideo.activity.MostHtmlActivity;
import com.limaoso.phonevideo.activity.SearchActivity;
import com.limaoso.phonevideo.activity.SearchResultPageMainActivity;
import com.limaoso.phonevideo.adapter.AppBaseAdapter;
import com.limaoso.phonevideo.adapter.MyGridAndListViewBaseAdapter;
import com.limaoso.phonevideo.bean.RecommendBean;
import com.limaoso.phonevideo.bean.RecommendBean.Cont.BaseDate;
import com.limaoso.phonevideo.bean.RefreshTitlebean;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.AnimUtil;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.MyGridView;

/**
 * 菜单详情页-上网必备
 * 
 * @author Kevin
 * 
 */
public class NetworkMenuDetailActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	public static final int FLAG_NETWORK = 1;// 网址推荐标记
	public static final int FLAG_APP = 2;// 应用推荐标记
	public static final int FLAG_HOTKEY = 3;// 热门推荐标记
	public static final int FLAG_KEYWORD = 4;// 关键字标记
	private MyGridView gv_network_game_app;
	private RecommendBean mbean;
	private ArrayList<String> mAppNames; // 游戏应用的名字
	private ArrayList<String> mAppIcons; // 游戏应用的图片
	private ArrayList<String> mAppIds; // 游戏应用的id

	private TextView tv_home_hot_1; // 热门搜索的数据
	private TextView tv_home_hot_2;
	private TextView tv_home_hot_3;
	private TextView tv_home_hot_4;
	private TextView tv_home_hot_5;
	private TextView tv_home_hot_6;
	private ImageView tv_home_hot_icon_1;
	private ImageView tv_home_hot_icon_2;
	private ImageView tv_home_hot_icon_3;
	private ImageView tv_home_hot_icon_4;
	private ImageView tv_home_hot_icon_5;
	private ImageView tv_home_hot_icon_6;

	private HttpHelp mHttpHelp; // 网络请求工具类

	private ImageView iv_home_1; // 点击刷新按钮 热门推荐
	private ImageView iv_network_refresh; // 点击刷新按钮 推荐应用
	private AnimUtil animUtil;
	private int refreshMoviePage_app = 2; // 刷新电影的下一页
	private int refreshMoviePage_hotkey = 2; // 刷新热门推荐下一页
	private MyNetworkGridViewAdapter gridViewAdapter; // 推荐应用适配器
	private MyGridView gv_net_layout;
	private List<BaseDate> siteurl1;

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.left_network);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	@Override
	protected String getCurrentTitle() {
		return "上网必备";
	}

	@Override
	public void initView() {
		iv_home_1 = (ImageView) rootView.findViewById(R.id.iv_home_1);
		gv_net_layout = (MyGridView) rootView.findViewById(R.id.gv_net_layout);
		iv_network_refresh = (ImageView) rootView
				.findViewById(R.id.iv_network_refresh);

		tv_home_hot_1 = (TextView) rootView.findViewById(R.id.tv_home_hot_1);
		tv_home_hot_2 = (TextView) rootView.findViewById(R.id.tv_home_hot_2);
		tv_home_hot_3 = (TextView) rootView.findViewById(R.id.tv_home_hot_3);
		tv_home_hot_4 = (TextView) rootView.findViewById(R.id.tv_home_hot_4);
		tv_home_hot_5 = (TextView) rootView.findViewById(R.id.tv_home_hot_5);
		tv_home_hot_6 = (TextView) rootView.findViewById(R.id.tv_home_hot_6);

		tv_home_hot_icon_1 = (ImageView) rootView
				.findViewById(R.id.tv_home_hot_icon_1);
		tv_home_hot_icon_2 = (ImageView) rootView
				.findViewById(R.id.tv_home_hot_icon_2);
		tv_home_hot_icon_3 = (ImageView) rootView
				.findViewById(R.id.tv_home_hot_icon_3);
		tv_home_hot_icon_4 = (ImageView) rootView
				.findViewById(R.id.tv_home_hot_icon_4);
		tv_home_hot_icon_5 = (ImageView) rootView
				.findViewById(R.id.tv_home_hot_icon_5);
		tv_home_hot_icon_6 = (ImageView) rootView
				.findViewById(R.id.tv_home_hot_icon_6);

		gv_network_game_app = (MyGridView) rootView
				.findViewById(R.id.gv_network_game_app);
		iv_home_1.setOnClickListener(this);
		iv_network_refresh.setOnClickListener(this);
		gv_network_game_app.setOnItemClickListener(this);
		mHttpHelp = new HttpHelp();
		// initNet();

	}

	/**
	 * 初始化网络请求
	 */
	protected void initNet() {
		mHttpHelp.sendGet(NetworkConfig.getLeftNetworkpage(),
				RecommendBean.class, new MyRequestCallBack<RecommendBean>() {

					@Override
					public void onSucceed(RecommendBean bean) {
						if (bean == null) {
							return;
						}
						mbean = bean;
						initNetData();
						initNetworkData_app(bean.cont.faxian);
						initNetworkDate_hotkey(bean.cont.hotkeys);
						isNewOrClicked(bean.cont.hotkeys);
						setViewAdapter();
					}

				});
	}

	/**
	 * 网络数据
	 */
	protected void initNetData() {
		siteurl1 = mbean.cont.siteurl1;
		if (siteurl1 == null || siteurl1.size() == 0) {
			if (gv_net_layout != null) {
				gv_net_layout.setVisibility(View.GONE);
			}
			return;
		}
		gv_net_layout.setAdapter(new MyNetAdapter(siteurl1.size()));
		gv_net_layout.setOnItemClickListener(this);

	}

	private class MyNetAdapter extends AppBaseAdapter {

		public MyNetAdapter(int count) {
			super(count);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHodle hodle;
			if (convertView == null) {
				hodle = new ViewHodle();
				convertView = View.inflate(UIUtils.getContext(),
						R.layout.item_net_add, null);
				hodle.iv_pic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				hodle.tv_net_name = (TextView) convertView
						.findViewById(R.id.tv_net_name);
				convertView.setTag(hodle);
			} else {
				hodle = (ViewHodle) convertView.getTag();
			}
			if (siteurl1 != null) {
				BaseDate baseDate = siteurl1.get(position);
				if (baseDate != null) {
					hodle.tv_net_name.setText(baseDate.name);
					mHttpHelp.showImage(hodle.iv_pic, baseDate.pic);
				}
			}

			return convertView;
		}

	}

	private class ViewHodle {
		public TextView tv_net_name;
		ImageView iv_pic;
	}

	/**
	 * 设置gridview适配器
	 */
	protected void setViewAdapter() {
		gridViewAdapter = new MyNetworkGridViewAdapter(mAppNames, false);
		gv_network_game_app.setAdapter(gridViewAdapter);
		// 去除gv点击的背景色
		gv_network_game_app.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	class MyNetworkGridViewAdapter extends MyGridAndListViewBaseAdapter {

		public MyNetworkGridViewAdapter(List<String> list, boolean b) {
			super(list, b);
		}

		@Override
		public int getCount() {
			return mAppNames.size();
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = UIUtils.inflate(R.layout.list_item_search);

			ImageView ivHome = (ImageView) view.findViewById(R.id.iv_home);
			TextView tvHome = (TextView) view.findViewById(R.id.tv_home);

			tvHome.setText(mAppNames.get(arg0));

			mHttpHelp.showImage(ivHome, mAppIcons.get(arg0));
			return view;
		}

	}

	/**
	 * 显示最新标记，以及设置点击监听事件
	 * 
	 * @param hotkeys
	 */
	protected void isNewOrClicked(List<BaseDate> hotkeys) {

		isNewMovie(hotkeys.get(0).isnew, tv_home_hot_icon_1);
		isNewMovie(hotkeys.get(1).isnew, tv_home_hot_icon_2);
		isNewMovie(hotkeys.get(2).isnew, tv_home_hot_icon_3);
		isNewMovie(hotkeys.get(3).isnew, tv_home_hot_icon_4);
		isNewMovie(hotkeys.get(4).isnew, tv_home_hot_icon_5);
		isNewMovie(hotkeys.get(5).isnew, tv_home_hot_icon_6);

		setHomeOnClickListener(tv_home_hot_1, hotkeys.get(0).name);
		setHomeOnClickListener(tv_home_hot_2, hotkeys.get(1).name);
		setHomeOnClickListener(tv_home_hot_3, hotkeys.get(2).name);
		setHomeOnClickListener(tv_home_hot_4, hotkeys.get(3).name);
		setHomeOnClickListener(tv_home_hot_5, hotkeys.get(4).name);
		setHomeOnClickListener(tv_home_hot_6, hotkeys.get(5).name);
	}

	/**
	 * 初始化应用的关键信息
	 * 
	 * @param b
	 */
	protected void initNetworkData_app(List<BaseDate> list) {

		mAppNames = new ArrayList<String>();
		mAppIcons = new ArrayList<String>();
		mAppIds = new ArrayList<String>();

		mAppNames.clear();
		mAppIcons.clear();
		mAppIds.clear();

		for (int i = 0; i < list.size(); i++) {
			mAppNames.add(list.get(i).name);
		}

		for (int i = 0; i < list.size(); i++) {
			mAppIcons.add(list.get(i).pic);
		}
		for (int i = 0; i < list.size(); i++) {
			mAppIds.add(list.get(i).id);
		}

	}

	/**
	 * 初始化热门推荐的关键信息
	 * 
	 * @param list
	 */
	private void initNetworkDate_hotkey(List<BaseDate> list) {

		tv_home_hot_1.setText(list.get(0).name);
		tv_home_hot_2.setText(list.get(1).name);
		tv_home_hot_3.setText(list.get(2).name);
		tv_home_hot_4.setText(list.get(3).name);
		tv_home_hot_5.setText(list.get(4).name);
		tv_home_hot_6.setText(list.get(5).name);
	}

	/**
	 * 判断该影片是否是最新电影资源
	 * 
	 * @param isnew
	 * @param iv
	 */

	private void isNewMovie(String isnew, ImageView iv) {
		if ("1".equals(isnew)) {
			iv.setVisibility(View.VISIBLE);
		} else {
			iv.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		/*
		 * 点击刷新图片刷新电影资源
		 */
		case R.id.iv_home_1:
			if (mbean == null || mbean.url == null || mbean.url.hotkeys == null) {
				break;
			}
			String rUrl = mbean.url.hotkeys + mHttpHelp.FLAG_PAGE
					+ refreshMoviePage_app;
			if (animUtil == null) {
				animUtil = new AnimUtil();
			}
			iv_home_1.setFocusable(false);
			iv_home_1.setClickable(false);
			animUtil.startRotateAnimation(iv_home_1);
			mHttpHelp.sendGet(rUrl, RefreshTitlebean.class,
					new MyRequestCallBack<RefreshTitlebean>() {

						@Override
						public void onSucceed(RefreshTitlebean bean) {
							if (bean.cont.size() < 6) {
								UIUtils.showToast(
										NetworkMenuDetailActivity.this,
										"没有更多的数据了……");
							}
							iv_home_1.setFocusable(true);
							iv_home_1.setClickable(true);
							getBeanTypeAndUp(FLAG_HOTKEY, bean.cont, null);
							iv_home_1.clearAnimation();
						}
					});

			break;
		case R.id.iv_network_refresh:
			if (animUtil == null) {
				animUtil = new AnimUtil();
			}
			if (mbean == null || mbean.url == null || mbean.url.faxian == null) {
				break;
			}
			iv_network_refresh.setFocusable(false);
			iv_network_refresh.setClickable(false);
			animUtil.startRotateAnimation(iv_network_refresh);
			String fUrl = mbean.url.faxian + mHttpHelp.FLAG_PAGE
					+ refreshMoviePage_app;
			mHttpHelp.sendGet(fUrl, RefreshTitlebean.class,
					new MyRequestCallBack<RefreshTitlebean>() {

						@Override
						public void onSucceed(RefreshTitlebean bean) {
							if (bean != null) {
								if (bean.cont.size() < 6) {
									UIUtils.showToast(
											NetworkMenuDetailActivity.this,
											"没有更多的数据了……");
								}
								iv_network_refresh.setFocusable(true);
								iv_network_refresh.setClickable(true);
								getBeanTypeAndUp(FLAG_APP, bean.cont,
										gridViewAdapter);
							}
							iv_network_refresh.clearAnimation();
						}

					});

			break;

		default:
			break;
		}
	}

	/**
	 * 判断类别和界面更新
	 * 
	 * @param flag
	 * @return
	 */
	private void getBeanTypeAndUp(int flag, List<BaseDate> list,
			MyNetworkGridViewAdapter adapter) {
		if (list == null || list.size() < 3) {
			UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
			return;
		}
		switch (flag) {
		case FLAG_APP:
			refreshMoviePage_app++;
			break;

		case FLAG_HOTKEY:
			refreshMoviePage_hotkey++;
			break;

		}
		if (adapter == null) {

			initNetworkDate_hotkey(list);
			isNewOrClicked(list);
		} else {
			initNetworkData_app(list);

			adapter.notifyDataSetInvalidated();
		}
	}

	/**
	 * 设置电影标题点击事件
	 * 
	 * @param tv_home_hot_12
	 * @param name
	 */
	private void setHomeOnClickListener(TextView view, final String name) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UIUtils.showToast(UIUtils.getContext(), name);
				Intent intent = new Intent(UIUtils.getContext(),
						SearchResultPageMainActivity.class);
				intent.putExtra(GlobalConstant.APP_SEARCH_KEY, name);
				startActivity(intent);
				PrefUtils.saveHotKey(name);

			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.gv_network_game_app:
			if (mAppNames != null) {
				// showAlertDialog(mAppNames.get(position));
				Intent intent = new Intent(NetworkMenuDetailActivity.this,
						APPDetailActivity.class);
				intent.putExtra(GlobalConstant.DL_APP_ID, mAppIds.get(position));
				startActivity(intent);
			}
			break;
		case R.id.gv_net_layout:
			enterNet(position);
			break;

		}
	}

	private void showAlertDialog(String string) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setMessage("确定下载" + string + "应用吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();

	}

	private void enterNet(int position) {
		if (siteurl1 == null) {
			return;
		}
		Intent intent = new Intent(this, WebViewActivity.class);
		intent.putExtra("url", siteurl1.get(position).url);
		intent.putExtra("title", siteurl1.get(position).name);
		this.startActivity(intent);
	}

}
