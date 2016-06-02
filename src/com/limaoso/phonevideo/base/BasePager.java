package com.limaoso.phonevideo.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.MainActivity;
import com.limaoso.phonevideo.activity.SearchActivity;
import com.limaoso.phonevideo.activity.home.AnimeActivity;
import com.limaoso.phonevideo.activity.home.MovieActivity;
import com.limaoso.phonevideo.activity.home.TVplayActivity;
import com.limaoso.phonevideo.activity.home.VideoActivity;
import com.limaoso.phonevideo.fragment.ContentFragment;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.top.TopActivity;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;

//import android.view.View.OnTouchListener;

/**
 * 主页下四个子页面的基类
 * 
 * @author Kevin
 * 
 */
public abstract class BasePager implements OnClickListener {

	public Activity mActivity;
	public View mRootView;// 布局对象

	public static final int TITLELAYOUT_1 = 1; // 发现页，个人中心
	public static final int TITLELAYOUT_2 = 2; // 频道页
	public static final int TITLELAYOUT_3 = 3; // 左侧菜单栏，除个人设置
	public static final int TITLELAYOUT_4 = 4; // 个人设置
	public static final int TITLELAYOUT_5 = 5; // 记录，缓存，收藏
	public static final int TITLELAYOUT_6 = 6; // 首页标题标签页
	public static final int TITLELAYOUT_7 = 7; // 首页标题标签页

	public boolean isDelate; // 判断编辑按钮的状态
	public FrameLayout flContent;// 内容
	public RelativeLayout rl_home_title_page; // 首页标题布局，当显示首页的时候将此布局显示，隐藏其他的
	public ImageView iv_basepager_headicon;// 首页标题背景
	public ImageView iv_left_menu; // 首页标题按钮显示左侧菜单
	public EditText et_home_find; // 首页标题的搜索框
	public ImageButton ibtn_home_find; // 首页标题的查找按钮
	public ImageButton btn_menu; // 频道页显示左侧菜单按钮
	public RelativeLayout rl_channel_find; // 频道页搜索框布局
	public EditText et_channel_find; // 频道页输入框
	public ImageButton ibtn_channel_find; // 频道页搜索按钮
	public TextView tv_title; // 页面标题
	public RelativeLayout rl_no_home_pager; // 非首页页面

	public ImageButton ibtn_left_home_back; // 左边返回按钮 返回个人中心
	public ImageButton ibtn_left_home_back_main; // 左边返回按钮 返回主界面

	public ImageButton ibtn_left_home_search; // 右侧查找按钮
	public TextView tv_base_page_right_edit; // 右侧编辑按钮

	public RadioButton btn_home_anime; // 首页标题-动漫
	public RadioButton btn_home_featured; // 首页标题-精选
	public RadioButton btn_home_more; // 首页标题-更多
	public RadioButton btn_home_movie; // 首页标题-电影
	public RadioButton btn_home_tvplay; // 首页标题-电视剧
	public RadioButton btn_home_video; // 首页标题-视频
	public RadioButton btn_home_top;
	public RadioGroup mRadio_Group;
	public MainActivity mMain;
	public ContentFragment contentFragment;
	public LinearLayout ll_base_page_delete_or_cancel; // 收藏，缓存，记录页面下的清除和取消布局
	public Button btn_base_page_all_delete; // 收藏，缓存，记录页面下的清除按钮
	public Button btn_base_page_cancel; // 收藏，缓存，记录页面下的取消按钮

	public ProgressBar pb_base_pager_progress; // 请求网路的时候正在加载的动画

	public TextView tv_basepager_skey;

	public BasePager(Activity activity) {
		mActivity = activity;
		initViews();
	}

	/**
	 * 初始化布局
	 */
	public void initViews() {
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);

		getAllWidgets(); // 获取布局页面所有的小控件

		mMain = (MainActivity) mActivity;
		contentFragment = mMain.getContentFragment();
		mRadio_Group = contentFragment.getRadio_Group();

		BtnOnClickListeners(); // 所有按钮点击监听事件

		tv_title.setText(getCurrentTitle());
	}

	/**
	 * 当前界面请求到数据 加载条隐藏，内容页面显示
	 */
	public void inLoading() {
		pb_base_pager_progress.setVisibility(View.VISIBLE);
		flContent.setVisibility(View.GONE);
	}

	/**
	 * 当前界面请求到数据 内容隐藏，加载条页面显示
	 */
	public void LoadingSuccess() {
		pb_base_pager_progress.setVisibility(View.GONE);
		flContent.setVisibility(View.VISIBLE);
	}

	/**
	 * 所有按钮点击事件监听
	 */
	private void BtnOnClickListeners() {
		iv_left_menu.setOnClickListener(this);
		btn_menu.setOnClickListener(this);
		btn_home_anime.setOnClickListener(this);
		// btn_home_featured.setOnClickListener(this);
		btn_home_more.setOnClickListener(this);
		btn_home_movie.setOnClickListener(this);

		btn_home_top.setOnClickListener(this);

		btn_home_tvplay.setOnClickListener(this);
		btn_home_video.setOnClickListener(this);
		ibtn_left_home_back.setOnClickListener(this);
		ibtn_left_home_search.setOnClickListener(this);
		tv_base_page_right_edit.setOnClickListener(this);
		ibtn_left_home_back_main.setOnClickListener(this);
		btn_base_page_all_delete.setOnClickListener(this);
		btn_base_page_cancel.setOnClickListener(this);

		// setOnTouchListener();

	}

	/*
	 * private void setOnTouchListener() { mRootView.setOnTouchListener(new
	 * OnTouchListener() {
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) {
	 * 
	 * ((InputMethodManager) mActivity
	 * .getSystemService(Context.INPUT_METHOD_SERVICE))
	 * .hideSoftInputFromWindow(mActivity.getCurrentFocus() .getWindowToken(),
	 * InputMethodManager.HIDE_NOT_ALWAYS); et_home_find.clearFocus();
	 * 
	 * return false;
	 * 
	 * } });
	 * 
	 * }
	 */

	/**
	 * 获取布局上所有的小控件
	 */
	private void getAllWidgets() {
		flContent = (FrameLayout) mRootView
				.findViewById(R.id.fl_base_pager_content);
		rl_home_title_page = (RelativeLayout) mRootView
				.findViewById(R.id.ll_home_title_page);

		iv_left_menu = (ImageView) mRootView.findViewById(R.id.iv_left_menu);

		iv_basepager_headicon = (ImageView) mRootView
				.findViewById(R.id.iv_basepager_headicon);

		iv_left_menu = (ImageView) mRootView.findViewById(R.id.iv_left_menu);

		et_home_find = (EditText) mRootView.findViewById(R.id.et_home_find);
		ibtn_home_find = (ImageButton) mRootView
				.findViewById(R.id.ibtn_home_find);
		btn_menu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
		rl_channel_find = (RelativeLayout) mRootView
				.findViewById(R.id.rl_channel_find);
		et_channel_find = (EditText) mRootView
				.findViewById(R.id.et_channel_find);
		ibtn_channel_find = (ImageButton) mRootView
				.findViewById(R.id.ibtn_channel_find);
		tv_title = (TextView) mRootView.findViewById(R.id.tv_title);
		rl_no_home_pager = (RelativeLayout) mRootView
				.findViewById(R.id.rl_no_home_pager);

		ibtn_left_home_back = (ImageButton) mRootView
				.findViewById(R.id.ibtn_left_home_back);

		ibtn_left_home_back_main = (ImageButton) mRootView
				.findViewById(R.id.ibtn_left_home_back_main);

		ibtn_left_home_search = (ImageButton) mRootView
				.findViewById(R.id.ibtn_left_home_search);
		tv_base_page_right_edit = (TextView) mRootView
				.findViewById(R.id.tv_base_page_right_edit);

		btn_home_anime = (RadioButton) mRootView
				.findViewById(R.id.btn_home_anime);
		btn_home_featured = (RadioButton) mRootView
				.findViewById(R.id.btn_home_featured);
		btn_home_featured.setChecked(true);
		btn_home_more = (RadioButton) mRootView
				.findViewById(R.id.btn_home_more);
		btn_home_movie = (RadioButton) mRootView
				.findViewById(R.id.btn_home_movie);
		btn_home_tvplay = (RadioButton) mRootView
				.findViewById(R.id.btn_home_tvplay);
		btn_home_video = (RadioButton) mRootView
				.findViewById(R.id.btn_home_video);
		btn_home_top = (RadioButton) mRootView.findViewById(R.id.btn_home_top);

		ll_base_page_delete_or_cancel = (LinearLayout) mRootView
				.findViewById(R.id.ll_base_page_delete_or_cancel);
		btn_base_page_all_delete = (Button) mRootView
				.findViewById(R.id.btn_base_page_all_delete);
		btn_base_page_cancel = (Button) mRootView
				.findViewById(R.id.btn_base_page_cancel);
		pb_base_pager_progress = (ProgressBar) mRootView
				.findViewById(R.id.pb_base_pager_progress);

		ll_base_page_delete_or_cancel = (LinearLayout) mRootView
				.findViewById(R.id.ll_base_page_delete_or_cancel);
		btn_base_page_all_delete = (Button) mRootView
				.findViewById(R.id.btn_base_page_all_delete);
		btn_base_page_cancel = (Button) mRootView
				.findViewById(R.id.btn_base_page_cancel);
		tv_basepager_skey = (TextView) mRootView
				.findViewById(R.id.tv_basepager_skey);

	}

	/**
	 * 设置当前的布局的标题名称
	 * 
	 * @return
	 */
	public abstract String getCurrentTitle();

	/**
	 * 显示首页的标题布局 隐藏其他无关布局
	 */
	public void isHomePager() {
		rl_home_title_page.setVisibility(View.VISIBLE);
		rl_no_home_pager.setVisibility(View.GONE);
		tv_basepager_skey.setText(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.SKEY, ""));
	}

	/**
	 * 显示其他界面的标题布局 隐藏首页面的布局 通过开关来控制子控件的显示 1，发现页，个人中心 2，频道页 3,左侧菜单栏，除个人设置 7，个人设置
	 * 5,记录，缓存，收藏
	 * 
	 * @param b
	 */
	public void isOtherPager(int key) {
		rl_home_title_page.setVisibility(View.GONE);
		rl_no_home_pager.setVisibility(View.VISIBLE);
		switch (key) {
		case TITLELAYOUT_1:
			btn_menu.setVisibility(View.VISIBLE);
			ibtn_left_home_back_main.setVisibility(View.GONE);
			ibtn_left_home_back.setVisibility(View.GONE);
			rl_channel_find.setVisibility(View.GONE);
			tv_title.setVisibility(View.VISIBLE);
			ibtn_left_home_search.setVisibility(View.GONE);
			tv_base_page_right_edit.setVisibility(View.GONE);
			ll_base_page_delete_or_cancel.setVisibility(View.GONE);

			break;
		case TITLELAYOUT_2:
			btn_menu.setVisibility(View.VISIBLE);
			ibtn_left_home_back_main.setVisibility(View.GONE);
			ibtn_left_home_back.setVisibility(View.GONE);
			rl_channel_find.setVisibility(View.VISIBLE);
			tv_title.setVisibility(View.GONE);
			ibtn_left_home_search.setVisibility(View.GONE);
			tv_base_page_right_edit.setVisibility(View.GONE);
			ll_base_page_delete_or_cancel.setVisibility(View.GONE);

			break;
		case TITLELAYOUT_3:
			btn_menu.setVisibility(View.VISIBLE);
			ibtn_left_home_back_main.setVisibility(View.GONE);
			ibtn_left_home_back.setVisibility(View.GONE);
			rl_channel_find.setVisibility(View.GONE);
			tv_title.setVisibility(View.VISIBLE);
			ibtn_left_home_search.setVisibility(View.GONE);
			tv_base_page_right_edit.setVisibility(View.GONE);
			ll_base_page_delete_or_cancel.setVisibility(View.GONE);
			break;

		case TITLELAYOUT_5:
			btn_menu.setVisibility(View.GONE);
			ibtn_left_home_back.setVisibility(View.VISIBLE);
			ibtn_left_home_back_main.setVisibility(View.GONE);
			rl_channel_find.setVisibility(View.GONE);
			tv_title.setVisibility(View.VISIBLE);
			ibtn_left_home_search.setVisibility(View.GONE);
			tv_base_page_right_edit.setVisibility(View.VISIBLE);
			ll_base_page_delete_or_cancel.setVisibility(View.GONE);
			break;
		case TITLELAYOUT_6:
			btn_menu.setVisibility(View.GONE);
			ibtn_left_home_back_main.setVisibility(View.VISIBLE);
			ibtn_left_home_back.setVisibility(View.GONE);
			rl_channel_find.setVisibility(View.GONE);
			tv_title.setVisibility(View.VISIBLE);
			ibtn_left_home_search.setVisibility(View.VISIBLE);
			tv_base_page_right_edit.setVisibility(View.GONE);
			ll_base_page_delete_or_cancel.setVisibility(View.GONE);
			break;
		case TITLELAYOUT_7: // 个人设置
			btn_menu.setVisibility(View.GONE);
			ibtn_left_home_back_main.setVisibility(View.VISIBLE);
			ibtn_left_home_back.setVisibility(View.GONE);
			rl_channel_find.setVisibility(View.GONE);
			tv_title.setVisibility(View.VISIBLE);
			ibtn_left_home_search.setVisibility(View.GONE);
			tv_base_page_right_edit.setVisibility(View.GONE);
			ll_base_page_delete_or_cancel.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	/**
	 * 切换SlidingMenu的状态
	 * 
	 * @param b
	 */
	protected void toggleSlidingMenu() {
		SlidingMenu slidingMenu = mMain.getSlidingMenu();
		slidingMenu.toggle();// 切换状态, 显示时隐藏, 隐藏时显示
	}

	/**
	 * 初始化数据
	 */
	public void initData() {

	}

	/**
	 * 去除GridView点击的背景色
	 * 
	 * @param gv
	 */
	public void delateItembackgroud(GridView gv) {
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	/**
	 * 设置侧边栏开启或关闭
	 * 
	 * @param enable
	 */
	public void setSlidingMenuEnable(boolean enable) {
		SlidingMenu slidingMenu = mMain.getSlidingMenu();

		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_left_menu:
			toggleSlidingMenu();
			break;
		case R.id.btn_menu:
			toggleSlidingMenu();
			break;
		case R.id.btn_home_featured:
			setCurrentMenuDetailPager(0);
			break;
		case R.id.btn_home_movie:
			mActivity.startActivity(new Intent(mActivity, MovieActivity.class));
			btn_home_featured.setChecked(true);
			break;
		case R.id.btn_home_top:
			mActivity.startActivity(new Intent(mActivity, TopActivity.class));
			btn_home_featured.setChecked(true);
			break;
		case R.id.btn_home_tvplay:
			mActivity
					.startActivity(new Intent(mActivity, TVplayActivity.class));
			btn_home_featured.setChecked(true);
			break;
		case R.id.btn_home_anime:
			mActivity.startActivity(new Intent(mActivity, AnimeActivity.class));
			btn_home_featured.setChecked(true);
			break;
		case R.id.btn_home_video:
			mActivity.startActivity(new Intent(mActivity, VideoActivity.class));
			btn_home_featured.setChecked(true);
			break;
		case R.id.btn_home_more:
			setCurrentMenuDetailPager(1);
			// mRadio_Group.check(R.id.rb_channel);
			break;
		case R.id.ibtn_left_home_back:
			setCurrentMenuDetailPager(3);
			mRadio_Group.check(R.id.rb_oneself);
			break;
		case R.id.ibtn_left_home_back_main:
			setCurrentMenuDetailPager(1);

			mRadio_Group.check(R.id.rb_channel);
			break;
		case R.id.ibtn_left_home_search:
			mMain.startActivity(new Intent(UIUtils.getContext(),
					SearchActivity.class));
			break;
		case R.id.tv_base_page_right_edit:
			// 显示记录，缓存，收藏中的删除布局
			executeEditOperate();
			ll_base_page_delete_or_cancel.setVisibility(View.VISIBLE);
			tv_base_page_right_edit.setVisibility(View.GONE);
			break;
		case R.id.btn_base_page_all_delete:
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle("确定全部删除么？");
			builder.setCancelable(false);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 执行记录，缓存，收藏中的全部删除操作
							executeAllDeleteOperate();
							tv_base_page_right_edit.setVisibility(View.VISIBLE);
							ll_base_page_delete_or_cancel
									.setVisibility(View.GONE);
							dialog.cancel();
						}

					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.create().show();

			break;
		case R.id.btn_base_page_cancel:
			// 隐藏记录，缓存，收藏中的删除布局
			executeCancelOperate();
			ll_base_page_delete_or_cancel.setVisibility(View.GONE);
			tv_base_page_right_edit.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * 点击取消时候要做的操作
	 */
	public void executeCancelOperate() {

	}

	/**
	 * 点击全部删除时候要做的操作
	 */
	public void executeAllDeleteOperate() {

	}

	/**
	 * 点击编辑的时候要做的操作
	 */
	public void executeEditOperate() {

	}

	/**
	 * 将菜单栏中的点击 位置传到contentfragment中
	 * 
	 * @param position
	 */
	protected void setCurrentMenuDetailPager(int position) {
		MainActivity mainUi = (MainActivity) mActivity;
		ContentFragment fragment = mainUi.getContentFragment();// 获取主页面fragment
		fragment.setCurrentMenuDetailPager(position);
	}

}
