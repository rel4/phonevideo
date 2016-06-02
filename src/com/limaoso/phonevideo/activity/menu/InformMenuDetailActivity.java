package com.limaoso.phonevideo.activity.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.BaseActivity;
import com.limaoso.phonevideo.activity.CommentActivity;
import com.limaoso.phonevideo.activity.PlayActivity;
import com.limaoso.phonevideo.bean.InfromPageBean;
import com.limaoso.phonevideo.bean.InfromPageBean.Cont;
import com.limaoso.phonevideo.fragment.LeftMenuFragment;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.holder.AllViewholder;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.LogUtil;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.RefreshListView;
import com.limaoso.phonevideo.view.RefreshListView.OnRefreshListener;

/**
 * 菜单详情页-消息通知
 * 
 * @author Kevin
 * 
 */
public class InformMenuDetailActivity extends BaseActivity implements
		OnRefreshListener, OnItemClickListener, OnClickListener {

	private static final int COMMENT_TAG = 0; // 评论标记
	private static final int REPLY_TAG = 1;// 回复标记
	private static final int SYSTEM_REPLY_TAG = 2; // 系统回复标记

	private static final String NOTICE_TAG = "1"; // 系统回复的公告类型
	private static final String VIDEO_TAG = "2"; // 系统回复的视频类型
	private static final String APP_TAG = "3"; // 系统回复的app应用类型

	private int CURRENT_TAG = 0; // 当前状态标记

	private TextView tv_System_Notifications, tv_reply_comment,
			tv_message_comment;
	private View line_message_comment, line_System_Notifications,
			line_reply_comment;

	private HttpHelp mHttpHelp; // 网络请求工具类
	private List<Cont> mBaseBean; // 消息条目的javabean
	public int loadMoviePage = 2; // 加载更多的下一页

	private ArrayList<String> mAllTitles; // 评论,回复的标题
	private ArrayList<String> mAllContent; // 评论，回复的内容
	private ArrayList<String> mAllTime; // 评论，回复的时间
	private ArrayList<String> mReplyNickName; // 用户的昵称
	private ArrayList<String> mReplyface; // 用户的头像
	private ArrayList<String> mCommentId; // 用户评论的影片id

	@Override
	public String getCurrentTitle() {
		return "消息";
	}

	@Override
	protected View getRootView() {
		return View.inflate(mActivity, R.layout.messag_page_root, null);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	@Override
	public void initView() {
		splits = new String[2];
		splits[0] = "0";
		splits[1] = "0";
		mBadgeView1 = new BadgeView(mActivity);
		mBadgeView2 = new BadgeView(mActivity);
		mHttpHelp = new HttpHelp();
		CURRENT_TAG = COMMENT_TAG;// 设置初始状态为评论
		initLV();// 初始化
		setMessageNums();
		setListener();// 设置监听器

	}

	/**
	 * 设置消息标题数量
	 */
	private BadgeView mBadgeView1;
	private BadgeView mBadgeView2;

	private void setMessageNums() {
		getInformNum(tv_reply_comment, true); // 获取回复消息
		getInformNum(tv_System_Notifications, false); // 获取系统消息

	}

	/**
	 * 获取消息数量
	 * 
	 * @param tv
	 * @param b
	 */
	private String[] splits;

	private void getInformNum(TextView tv, boolean b) {

		String str = PrefUtils.getString(mActivity, GlobalConstant.INFORM_NUM,
				null);
		int informNum = 0;
		if (str == null) {
			return;
		} else {
			if (str != null) {
				String[] splits2 = str.split("##");
				splits[0] = splits2[0];
				splits[1] = splits2[1];
			}
			if (b) {
				informNum = Integer.parseInt(splits[0]);
				setBadgeView(tv, informNum, mBadgeView1);
			} else {
				informNum = Integer.parseInt(splits[1]);
				setBadgeView(tv, informNum, mBadgeView2);
			}

		}
	}

	/**
	 * 设置badgeview
	 * 
	 * @param tv
	 * @param informNum
	 * @param bv
	 */
	private void setBadgeView(TextView tv, int informNum, BadgeView bv) {
		bv.setBadgeCount(informNum);
		bv.setBadgeGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		bv.setBadgeMargin(0, 0, 10, 0);
		bv.setTargetView(tv);
	}

	/**
	 * 点击后（回复，系统回复）清除消息数目的显示
	 */

	private void clickInformNum(TextView tv, BadgeView bv) {
		// MainActivity main = (MainActivity) mActivity;
		// MenuAdapter lvAdapter = main.getLeftMenuFragment().getLVAdapter();
		switch (tv.getId()) {
		case R.id.tv_reply_comment:
			if (splits == null) {
				PrefUtils.setString(mActivity, GlobalConstant.INFORM_NUM, "0"); // 点击后用于消除当前标题的信息数，再次打开侧边栏实现数目的减少
			} else {
				splits[0] = "0";
				PrefUtils.setString(mActivity, GlobalConstant.INFORM_NUM,
						splits[0] + "##" + splits[1]); // 点击后用于消除当前标题的信息数，再次打开侧边栏实现数目的减少
			}
			sendBroadcast(new Intent(LeftMenuFragment.ALREADY_READ));
			break;

		case R.id.tv_System_Notifications:
			if (splits == null) {
				PrefUtils.setString(mActivity, GlobalConstant.INFORM_NUM, "0"); // 点击后用于消除当前标题的信息数，再次打开侧边栏实现数目的减少
			} else {
				splits[1] = "0";
				PrefUtils.setString(mActivity, GlobalConstant.INFORM_NUM,
						splits[0] + "##" + splits[1]); // 点击后用于消除当前标题的信息数，再次打开侧边栏实现数目的减少
			}
			sendBroadcast(new Intent(LeftMenuFragment.ALREADY_READ));
			break;
		}
		bv.setVisibility(View.GONE);

		// if (lvAdapter != null) {
		// lvAdapter.notifyDataSetChanged();
		// }
	}

	private void setListener() {
		// iv_left_menu.setOnClickListener(this);
		rootView.findViewById(R.id.layout_message_comment).setOnClickListener(
				this);
		rootView.findViewById(R.id.layout_System_Notifications)
				.setOnClickListener(this);
		rootView.findViewById(R.id.layout_reply_comment).setOnClickListener(
				this);

	}

	private List<View> views;
	private RefreshListView message_list_view;
	private ProgressBar pb_message_progress;

	private void initLV() {

		pb_message_progress = (ProgressBar) rootView
				.findViewById(R.id.pb_message_progress);
		message_list_view = (RefreshListView) rootView
				.findViewById(R.id.message_list_view);
		views = new ArrayList<View>();
		tv_System_Notifications = (TextView) rootView
				.findViewById(R.id.tv_System_Notifications);
		tv_reply_comment = (TextView) rootView
				.findViewById(R.id.tv_reply_comment);
		tv_message_comment = (TextView) rootView
				.findViewById(R.id.tv_message_comment);
		line_message_comment = rootView.findViewById(R.id.line_message_comment);

		line_System_Notifications = rootView
				.findViewById(R.id.line_System_Notifications);
		line_reply_comment = rootView.findViewById(R.id.line_reply_comment);
		views.add(line_message_comment);
		views.add(line_reply_comment);
		views.add(line_System_Notifications);
		views.add(tv_message_comment);
		views.add(tv_reply_comment);
		views.add(tv_System_Notifications);

		initNet(COMMENT_TAG); // 设置初始化界面，为评论界面

	}

	/**
	 * 初始化网络请求
	 */
	private void initNet(int tag) {
		String currentURL = getUrl(tag);// 获取url
		if (currentURL != null) {

			mHttpHelp.sendGet(currentURL, InfromPageBean.class,
					new MyRequestCallBack<InfromPageBean>() {

						@Override
						public void onSucceed(InfromPageBean bean) {
							if (bean == null) {
								return;
							}
							isLoaded();
							switch (CURRENT_TAG) {
							case COMMENT_TAG:
							case REPLY_TAG:
								initNetworkData(true, bean); // 当前状态是评论和回复时候
								break;
							case SYSTEM_REPLY_TAG:
								initNetworkData_s(true, bean); // 当前状态是系统回复
								break;

							}
							setViewAdapter();

						}

					});
		} else {
			LogUtil.e("Error", "***********获取地址url失败！为空！************");
		}
	}

	/**
	 * 根据当前的状态获取url
	 * 
	 * @param Tag
	 * 
	 * @return
	 */
	private String getUrl(int Tag) {
		switch (Tag) {
		case COMMENT_TAG:
			return NetworkConfig.getInformPage_c();
		case REPLY_TAG:
			return NetworkConfig.getInformPage_r();
		case SYSTEM_REPLY_TAG:
			return NetworkConfig.getInformPage_s();
		}
		return null;
	}

	/**
	 * 初始网络数据
	 * 
	 * @param bean
	 */
	private void initNetworkData(boolean b, InfromPageBean bean) {
		if (bean == null || bean.cont == null) {
			return;
		}
		mBaseBean = bean.cont;
		if (mBaseBean.size() < 2) {
			UIUtils.showToast(UIUtils.getContext(), "没有更多的数据了，亲~~~");
		}
		if (b) {
			mAllTitles = new ArrayList<String>();
			mAllContent = new ArrayList<String>();
			mAllTime = new ArrayList<String>();
			mCommentId = new ArrayList<String>();
			if (CURRENT_TAG == REPLY_TAG) {
				mReplyNickName = new ArrayList<String>();
				mReplyface = new ArrayList<String>();
				mReplyNickName.clear();
				mReplyface.clear();
			}

			mAllTitles.clear();
			mAllContent.clear();
			mAllTime.clear();
			mCommentId.clear();

		}

		// 评论回复标题
		for (int i = 0; i < mBaseBean.size(); i++) {
			mAllTitles.add(mBaseBean.get(i).film_name);
		}
		// 评论回复内容
		for (int i = 0; i < mBaseBean.size(); i++) {
			mAllContent.add(mBaseBean.get(i).msg);
		}
		// 电影回复图标
		for (int i = 0; i < mBaseBean.size(); i++) {
			mAllTime.add(mBaseBean.get(i).time);
		}
		// 用户评论的影片id
		for (int i = 0; i < mBaseBean.size(); i++) {
			mCommentId.add(mBaseBean.get(i).comment_id);
		}
		if (CURRENT_TAG == REPLY_TAG) {
			// 用户昵称
			for (int i = 0; i < mBaseBean.size(); i++) {
				mReplyNickName.add(mBaseBean.get(i).nickname);
			}
			// 用户头像
			for (int i = 0; i < mBaseBean.size(); i++) {
				mReplyface.add(mBaseBean.get(i).face);
			}
		}

	}

	private ArrayList<String> mSystemReplyName; // 系统回复的标题内容
	private ArrayList<String> mSystemReplyType; // 系统回复的类型
	private ArrayList<String> mSystemReplyTime; // 系统回复的视频时间
	private ArrayList<String> mSystemReplyParm; // 系统回复的视频参数

	/**
	 * 初始网络状态，当前状态是系统回复
	 * 
	 * @param bean
	 * @param b
	 */
	protected void initNetworkData_s(boolean b, InfromPageBean bean) {
		mBaseBean = bean.cont;
		if (mBaseBean.size() < 2) {
			UIUtils.showToast(UIUtils.getContext(), "没有更多的数据了，亲~~~");
		}
		if (b) {
			mSystemReplyName = new ArrayList<String>();
			mSystemReplyTime = new ArrayList<String>();
			mSystemReplyType = new ArrayList<String>();
			mSystemReplyParm = new ArrayList<String>();
			mSystemReplyName.clear();
			mSystemReplyParm.clear();
			mSystemReplyTime.clear();
			mSystemReplyType.clear();
		}
		// 系统回复标题
		for (int i = 0; i < mBaseBean.size(); i++) {
			mSystemReplyName.add(mBaseBean.get(i).name);
		}
		// 系统回复时间
		for (int i = 0; i < mBaseBean.size(); i++) {
			mSystemReplyTime.add(mBaseBean.get(i).time);
		}
		// 系统回复类型
		for (int i = 0; i < mBaseBean.size(); i++) {
			mSystemReplyType.add(mBaseBean.get(i).type);
		}
		// 系统回复视频参数
		for (int i = 0; i < mBaseBean.size(); i++) {
			mSystemReplyParm.add(mBaseBean.get(i).rel_parm);
		}
	}

	/**
	 * 设置listview的适配器
	 */
	private MessgeAdapter mAdapter;

	private void setViewAdapter() {
		mAdapter = new MessgeAdapter();
		message_list_view.setAdapter(mAdapter);
		message_list_view.setOnRefreshListener(this);
		message_list_view.setOnItemClickListener(this);

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.layout_message_comment:
			CURRENT_TAG = COMMENT_TAG;
			isLoadding();
			loadMoviePage = 2;
			moveLine(CURRENT_TAG);
			initNet(CURRENT_TAG);
			break;
		case R.id.layout_reply_comment:
			CURRENT_TAG = REPLY_TAG;
			isLoadding();
			loadMoviePage = 2;
			moveLine(CURRENT_TAG);
			initNet(CURRENT_TAG);
			clickInformNum(tv_reply_comment, mBadgeView1);
			break;
		case R.id.layout_System_Notifications:
			CURRENT_TAG = SYSTEM_REPLY_TAG;
			isLoadding();
			loadMoviePage = 2;
			moveLine(CURRENT_TAG);
			initNet(CURRENT_TAG);
			clickInformNum(tv_System_Notifications, mBadgeView2);
			break;
		// case R.id.btn_menu:
		// toggleSlidingMenu();
		// break;

		}

	}

	/**
	 * 当前为正在加载状态
	 */
	private void isLoadding() {
		message_list_view.setVisibility(View.GONE);
		pb_message_progress.setVisibility(View.VISIBLE);
	}

	/**
	 * 当前为加载完毕状态
	 */
	private void isLoaded() {
		message_list_view.setVisibility(View.VISIBLE);
		pb_message_progress.setVisibility(View.GONE);
	}

	/**
	 * 控制线条和字体的显示颜色
	 * 
	 * @param clickId
	 */
	private void moveLine(int clickId) {
		for (int i = 0; i < views.size() / 2; i++) {
			if (clickId == i) {
				views.get(i).setBackgroundColor(
						mActivity.getResources().getColor(R.color.main_color));
				((TextView) views.get(i + 3)).setTextColor(mActivity
						.getResources().getColor(R.color.main_color));
			} else {
				views.get(i).setBackgroundColor(
						mActivity.getResources().getColor(R.color.bg_gray));
				((TextView) views.get(i + 3)).setTextColor(mActivity
						.getResources().getColor(R.color.bg_black));
			}
		}
	}

	private class MessgeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (CURRENT_TAG == SYSTEM_REPLY_TAG) {
				if (mSystemReplyName != null && mSystemReplyName.size() > 0) {
					return mSystemReplyName.size();
				} else {
					return 0;
				}
			} else {
				if (mAllTitles != null && mAllTitles.size() > 0) {
					return mAllTitles.size();
				} else {
					return 0;
				}
			}

		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AllViewholder viewholder = new AllViewholder();
			if (convertView == null) {
				switch (CURRENT_TAG) {
				case COMMENT_TAG:
					convertView = getCommentchildWidget(viewholder, convertView);
					break;
				case REPLY_TAG:
					convertView = getReplychildWidget(viewholder, convertView);
					break;
				case SYSTEM_REPLY_TAG:
					convertView = getSystemReplychildWidget(viewholder,
							convertView);
					break;
				}

			} else {
				viewholder = (AllViewholder) convertView.getTag();
			}
			/*
			 * 根据相应的状态进行赋值操作
			 */
			switch (CURRENT_TAG) {
			case COMMENT_TAG:
				setCommentchildWidget(viewholder, position);
				break;
			case REPLY_TAG:
				setReplychildWidget(viewholder, position);
				break;
			case SYSTEM_REPLY_TAG:
				setSystemReplychildWidget(viewholder, position);
				break;
			}
			return convertView;
		}

	}

	/**
	 * 设置系统回复布局的子控件值
	 * 
	 * @param viewholder
	 * @param position
	 */
	public void setSystemReplychildWidget(AllViewholder viewholder, int position) {
		viewholder.allTitle.setText(mSystemReplyName.get(position));
		viewholder.allTime.setText(mSystemReplyTime.get(position));
		if ("1".equals(mSystemReplyType.get(position))) {
			viewholder.goIcon.setVisibility(View.GONE);
		} else {
			viewholder.goIcon.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置回复布局的子控件值
	 * 
	 * @param viewholder
	 * @param position
	 */
	public void setReplychildWidget(AllViewholder viewholder, int position) {
		if (viewholder != null) {
			mHttpHelp.showImage(viewholder.replyIcon, mReplyface.get(position)
					+ "##"); // 头像
			viewholder.replyNickName.setText(mReplyNickName.get(position)); // 昵称
			viewholder.allTime.setText(mAllTime.get(position)); // 时间
			viewholder.allContent.setText("回复我：" + mAllContent.get(position)); // 内容
			viewholder.allTitle.setText(mAllTitles.get(position)); // 评论影片标题
		} else {
			LogUtil.e("Error", "------->回复holder为空<---------");
		}
	}

	/**
	 * 设置评论布局的子控件值
	 * 
	 * @param viewholder
	 * @param position
	 */
	public void setCommentchildWidget(AllViewholder viewholder, int position) {

		if (viewholder != null) {
			if (viewholder.allContent == null) {
				View cv = View.inflate(mActivity,
						R.layout.item_message_comment, null);
				viewholder.allContent = (TextView) cv
						.findViewById(R.id.tv_comment_content);
			}
			viewholder.allTitle.setText(mAllTitles.get(position));
			viewholder.allContent.setText(mAllContent.get(position));
			viewholder.allTime.setText(mAllTime.get(position));
		} else {
			LogUtil.e("Error", "------->评论holder为空<---------");
		}
	}

	/**
	 * 获取评论布局的子控件
	 * 
	 * @param vh
	 * @param cv
	 * @return
	 */
	private View getCommentchildWidget(AllViewholder vh, View cv) {
		cv = View.inflate(mActivity, R.layout.item_message_comment, null);
		vh.allTitle = (TextView) cv.findViewById(R.id.tv_comment_title);
		vh.allContent = (TextView) cv.findViewById(R.id.tv_comment_content);
		vh.allTime = (TextView) cv.findViewById(R.id.tv_comment_time);
		cv.setTag(vh);
		return cv;

	}

	/**
	 * 获取系统回复布局的子控件
	 * 
	 * @param vh
	 * @param cv
	 * @return
	 */
	public View getSystemReplychildWidget(AllViewholder vh, View cv) {
		cv = View.inflate(mActivity, R.layout.item_system_notifications, null);
		vh.allTitle = (TextView) cv.findViewById(R.id.tv_system_reply_name);
		vh.allTime = (TextView) cv.findViewById(R.id.tv_system_reply_time);
		vh.goIcon = (ImageView) cv.findViewById(R.id.iv_system_reply_go_icon);
		vh.allContent = (TextView) cv.findViewById(R.id.tv_comment_content);
		cv.setTag(vh);
		return cv;
	}

	/**
	 * 获取回复布局的子控件
	 * 
	 * @param vh
	 * @param cv
	 * @return
	 */
	public View getReplychildWidget(AllViewholder vh, View cv) {
		cv = View.inflate(mActivity, R.layout.item_reply_comment, null);
		vh.replyIcon = (ImageView) cv.findViewById(R.id.iv_reply_user_iocn);
		vh.replyNickName = (TextView) cv.findViewById(R.id.tv_reply_nick_name);
		vh.allTime = (TextView) cv.findViewById(R.id.tv_reply_time);
		vh.allContent = (TextView) cv.findViewById(R.id.tv_reply_content);
		vh.allTitle = (TextView) cv.findViewById(R.id.tv_reply_movie_title);
		cv.setTag(vh);
		return cv;
	}

	/**
	 * 实现上拉加载更多操作
	 */
	@Override
	public void onLoadMore() {
		String loadUrl = null;
		switch (CURRENT_TAG) {
		case COMMENT_TAG:
			loadUrl = getUrl(COMMENT_TAG) + mHttpHelp.FLAG_PAGE + loadMoviePage;
			break;
		case REPLY_TAG:
			loadUrl = getUrl(REPLY_TAG) + mHttpHelp.FLAG_PAGE + loadMoviePage;
			break;
		case SYSTEM_REPLY_TAG:
			loadUrl = getUrl(SYSTEM_REPLY_TAG) + mHttpHelp.FLAG_PAGE
					+ loadMoviePage;
			break;
		}

		mHttpHelp.sendGet(loadUrl, InfromPageBean.class,
				new MyRequestCallBack<InfromPageBean>() {

					@Override
					public void onSucceed(InfromPageBean bean) {
						switch (CURRENT_TAG) {
						case COMMENT_TAG:
						case REPLY_TAG:
							initNetworkData(false, bean); // 当前状态是评论和回复时候
							break;
						case SYSTEM_REPLY_TAG:
							initNetworkData_s(false, bean); // 当前状态是系统回复
							break;
						}
						mAdapter.notifyDataSetChanged();
						message_list_view.onRefreashFinish();
					}

				});
		loadMoviePage++;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (CURRENT_TAG) {
		case COMMENT_TAG: // 评论 跳转评论页面的时候携带影片的id
			Intent intent_com = new Intent(UIUtils.getContext(),
					CommentActivity.class);
			intent_com.putExtra(GlobalConstant.FILM_COMMENT_ID,
					mCommentId.get(position));
			mActivity.startActivity(intent_com);
			break;
		case REPLY_TAG: // 回复 跳转回复页面的时候携带影片的id
			Intent intent_rep = new Intent(UIUtils.getContext(),
					CommentActivity.class);
			intent_rep.putExtra(GlobalConstant.FILM_COMMENT_ID,
					mCommentId.get(position));
			mActivity.startActivity(intent_rep);
			break;
		case SYSTEM_REPLY_TAG: // 系统回复
			switch (mSystemReplyType.get(position)) {
			case NOTICE_TAG: // 公告
				break;
			case VIDEO_TAG: // 相应的视频界面
				Intent intent = new Intent(mActivity, PlayActivity.class);
				intent.putExtra(GlobalConstant.TV_ID,
						mSystemReplyParm.get(position));
				mActivity.startActivity(intent);
				break;
			case APP_TAG: // 相应的应用界面
				UIUtils.showToast(mActivity, "跳转到相应的应用界面…………");
				break;
			}
			break;
		}

	}

}
