package com.limaoso.phonevideo.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.Lmp2pMediaPlayer;
import tv.danmaku.ijk.media.sample.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.sample.widget.media.MediaController;
import tv.danmaku.ijk.media.sample.widget.media.MediaController.OnHiddenListener;
import tv.danmaku.ijk.media.sample.widget.media.MediaController.OnShownListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.http.RequestParams;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.adapter.AppBaseAdapter;
import com.limaoso.phonevideo.bean.BaseBaen;
import com.limaoso.phonevideo.bean.CommentBean;
import com.limaoso.phonevideo.bean.Commentlist;
import com.limaoso.phonevideo.bean.HomeBean.Cont.TV;
import com.limaoso.phonevideo.bean.PlayBaen;
import com.limaoso.phonevideo.bean.PlayBaen.Cont.Cfilminfo;
import com.limaoso.phonevideo.bean.PlayRefreshBean;
import com.limaoso.phonevideo.bean.PlaySoursBase.Cont.Cfilmlist;
import com.limaoso.phonevideo.bean.PlaySoursBase.Cont.Ckuinfo;
import com.limaoso.phonevideo.bean.PlaySoursBase.Cont.Shareinfo;
import com.limaoso.phonevideo.bean.ReplyChildBean;
import com.limaoso.phonevideo.bean.ReplyChildBean.Cont.Currrent_comment;
import com.limaoso.phonevideo.bean.Son;
import com.limaoso.phonevideo.bean.TVbean;
import com.limaoso.phonevideo.broadcastreceiver.ScreenObserver;
import com.limaoso.phonevideo.broadcastreceiver.ScreenObserver.ScreenStateListener;
import com.limaoso.phonevideo.db.CacheInfo;
import com.limaoso.phonevideo.db.CacheMessgeDao;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.p2p.P2PManager;
import com.limaoso.phonevideo.utils.AnimUtil;
import com.limaoso.phonevideo.utils.LogUtil;
import com.limaoso.phonevideo.utils.NetWorkUtil;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.SharePlatforms;
import com.limaoso.phonevideo.utils.StringNumUtils;
import com.limaoso.phonevideo.utils.SystemUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.TabPageIndicator;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * 播放页面
 * 
 * @author jiang
 * 
 */
@SuppressLint("ResourceAsColor")
public class PlayActivity extends BaseActivity implements OnClickListener,
		OnHiddenListener, OnShownListener, OnDismissListener, OnScrollListener {
	private PullToRefreshListView play_process_listview;
	private PlayProcessAapter mAapter;
	private HttpHelp mHttpHelp;
	private List<Commentlist> mCommentlists;
	private int tvListNum;// 剧集
	private List<TV> recku;
	private AnimUtil animUtil;
	private PlayBaen mbean;
	private IjkVideoView vv_player;
	private boolean isPaly = false;
	private String TAG = "PlayActivity";
	// 来自缓存页面
	private boolean isComeCachePage = false;
	private String hash;
	private TextView tv_step_up, tv_step_down, tv_Play_number, tv_collect,
			tv_play_speed, tv_play_name, tv_play_speed_porgress, tv_down_speed;
	// 首先在您的Activity中添加如下成员变量
	// 分享
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private SharePlatforms mPlatforms;

	protected View getRootView() {
		P2PManager.getInstance(getApplicationContext()).stopTask();
		getIntentData();
		return View.inflate(this, R.layout.page_play_process, null);
	}

	/**
	 * 获取进入页面的数据
	 */
	private void getIntentData() {
		if (mDao == null) {
			mDao = new CacheMessgeDao(this);
		}
		Intent intent = getIntent();
		if (intent != null) {
			Serializable serializableExtra = intent
					.getSerializableExtra(GlobalConstant.FLAG_PALY_CACHE_HASH);
			if (serializableExtra != null
					&& serializableExtra instanceof String) {
				hash = (String) serializableExtra;
				CacheInfo cacheInfo = mDao.getCacheInfo(hash);
				if (cacheInfo != null) {
					String tvPlaynum = cacheInfo.getTvPlaynum();
					current_play_position_num = (int) StringNumUtils
							.string2Num(tvPlaynum);
					tv_id = cacheInfo.getTvId();
					isComeCachePage = true;
				}
			} else {
				String tv = intent.getStringExtra(GlobalConstant.TV_ID);
				String[] split = tv.split(GlobalConstant.FLAG_APP_SPLIT);
				if (split.length > 1) {
					current_play_position_num = (int) StringNumUtils
							.string2Num(split[1]);
				}
				tv_id = split[0];
			}
		}
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
	}

	protected void initView() {

		play_show_name_layout = findViewById(R.id.play_show_name_layout);
		buffering_indicator = findViewById(R.id.buffering_indicator);
		findViewById(R.id.tv_share).setOnClickListener(this);
		play_show_name_layout.setOnClickListener(this);
		tv_play_speed = (TextView) findViewById(R.id.tv_play_speed);
		tv_down_speed = (TextView) findViewById(R.id.tv_down_speed);
		tv_play_speed_porgress = (TextView) findViewById(R.id.tv_play_speed_porgress);
		tv_play_name = (TextView) findViewById(R.id.tv_play_name);
		vv_player = (IjkVideoView) findViewById(R.id.video_view);
		vv_player.setActivity(this);
		tv_collect = (TextView) findViewById(R.id.tv_collect);
		tv_Play_number = (TextView) findViewById(R.id.tv_Play_number);
		tv_step_up = (TextView) findViewById(R.id.tv_step_up);
		tv_step_down = (TextView) findViewById(R.id.tv_step_down);
		play_process_listview = (PullToRefreshListView) findViewById(R.id.play_process_listview);
		play_process_listview.setMode(Mode.PULL_FROM_END);
		initVideoView();
	}

	/**
	 * 播放页面进入请求网络
	 */
	protected void initNet() {
		if (mHttpHelp == null) {
			mHttpHelp = new HttpHelp();
		}
		mHttpHelp.sendGet(NetworkConfig.getPlayActivityUrl(tv_id),
				PlayBaen.class, new MyRequestCallBack<PlayBaen>() {
					@Override
					public void onSucceed(PlayBaen bean) {
						if (bean == null || bean.cont.recku == null) {
							play_process_listview.setVisibility(View.GONE);
							if (isComeCachePage && !TextUtils.isEmpty(hash)) {
								startCachePaly(hash);
							}
							return;
						}
						mbean = bean;
						recku = bean.cont.recku;
						mCommentlists = bean.cont.commentlist;
						initPlayInfo();
						initTVListNum();
						initData();
						initShare();
						initVideoPlay(bean.cont.cfilminfo);
					}
				});
	}

	/**
	 * 来自页面播放
	 * 
	 * @param hash
	 */
	protected void startCachePaly(String hash) {
		if (!TextUtils.isEmpty(hash)) {
			startVideoView(hash);
		}

	}

	/**
	 * 开启播放器播放
	 * 
	 * @param cfilminfo
	 */
	protected void initVideoPlay(Cfilminfo cfilminfo) {
		if (cfilminfo == null) {
			return;
		}

		switch (NetWorkUtil.getNetworkClass(PlayActivity.this)) {
		case NetWorkUtil.NETWORK_CLASS_UNAVAILABLE:
			// alertDialogNotNet();
			break;
		case NetWorkUtil.NETWORK_CLASS_2_G:
			alertDialogPhoneNet(cfilminfo);
			break;
		case NetWorkUtil.NETWORK_CLASS_3_G:
			alertDialogPhoneNet(cfilminfo);
			break;
		case NetWorkUtil.NETWORK_CLASS_4_G:
			alertDialogPhoneNet(cfilminfo);
			break;
		case NetWorkUtil.NETWORK_CLASS_WIFI:
			hash = cfilminfo.lmlink;
			CacheInfo cacheInfo = mDao.getCacheInfo(hash);
			if (cacheInfo == null || TextUtils.isEmpty(cacheInfo.getTvId())) {
				saveInfo(cfilminfo);
			}
			startVideoView(hash);
			break;
		case NetWorkUtil.NETWORK_CLASS_UNKNOWN:
			// alertDialogNotNet();
			break;

		}
	}

	/**
	 * 没有网络 进行网络设置
	 */
	private void alertDialogNotNet() {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog alertDialog = builder.create();
		View inflate = UIUtils.inflate(R.layout.dialog_alert_net);
		alertDialog.setView(inflate);
		alertDialog.show();

	}

	/**
	 * 弹窗提示使用手机网络
	 * 
	 * @param cfilminfo
	 * 
	 */
	private void alertDialogPhoneNet(final Cfilminfo cfilminfo) {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog alertDialog = builder.create();
		View inflate = UIUtils.inflate(R.layout.dialog_alert_net);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		alertDialog.getWindow().setContentView(inflate);
		// 继续播放
		inflate.findViewById(R.id.tv_dialog_continue).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.cancel();
						if (cfilminfo != null) {
							if (cfilminfo != null) {
								P2PManager.getInstance(getApplicationContext())
										.notifyWifi(1);

								hash = cfilminfo.lmlink;
								CacheInfo cacheInfo = mDao.getCacheInfo(hash);
								if (cacheInfo == null
										|| TextUtils.isEmpty(cacheInfo
												.getTvId())) {
									saveInfo(cfilminfo);
								}
								startVideoView(cfilminfo.lmlink);
							}

						}

					}
				});
		// 取消播放
		inflate.findViewById(R.id.tv_dialog_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.cancel();

					}
				});
	}

	protected void initShare() {
		Shareinfo shareinfo = mbean.cont.shareinfo;
		if (shareinfo != null && mPlatforms == null) {
			mController.getConfig().closeToast();
			mPlatforms = new SharePlatforms(this, mController, shareinfo);

		}

	}

	/**
	 * 分享
	 */

	private void sharePlatform() {
		if (mPlatforms != null) {
			mPlatforms.initView();
			mController.openShare(this, false);
		}

	}

	/**
	 * 设置动画
	 * 
	 * @param view
	 * @param flag
	 * @param adapter
	 */
	private int reckuPage = 2;

	private void setAnim(final ViewHolder2 holder, final View view) {

		if (animUtil == null) {
			animUtil = new AnimUtil();
		}
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				view.setFocusable(false);
				view.setClickable(false);
				animUtil.startRotateAnimation(view);
				String url = mbean.url.recku + "&page=" + reckuPage;
				mHttpHelp.sendGet(url, TVbean.class,
						new MyRequestCallBack<TVbean>() {
							@Override
							public void onSucceed(TVbean bean) {
								if (bean == null || bean.cont.size() == 0) {
									view.clearAnimation();
									view.setFocusable(true);
									view.setClickable(true);
									return;
								}
								reckuPage++;
								view.setFocusable(true);
								view.setClickable(true);
								recku = bean.cont;
								initHeadView(holder);
								view.clearAnimation();
							}
						});
			}
		});
	}

	/**
	 * 关闭键盘
	 */
	private void closeSoftInput() {
		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
			// 隐藏软键盘
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}

	}

	/**
	 * 关闭评论输入框
	 */
	private void closeEditText() {
		View etComment = findViewById(R.id.etComment);
		if (etComment != null && etComment.isShown()) {
			etComment.setVisibility(View.GONE);
			findViewById(R.id.btnSendComment).setVisibility(View.GONE);
		}
	}

	public static final int SELECT_NUM_LAYOUT = 1;// 选集
	public static final int CACHE_LAYOUT = 2;// 缓存
	public static final int PLAY_SOURCE_LAYOUT = 3;// 播放来源
	public static final int REPLY_LIST_LAYOUT = 4;// 回复
	private int popuLayout = PLAY_SOURCE_LAYOUT;

	private boolean isOpenPopu = true;

	private void showPopopWindow(final String id) {
		if (isOpenPopu) {
			setPopuWindowLayout();// 设置布局
			// if (mPopupWindow == null) {
			int popuHeight = SystemUtils.getScreenHeight()
					- (int) PlayActivity.this.getResources().getDimension(
							R.dimen.dp258);
			// popwindow_layout.setFocusable(true); // 这个很重要
			// popwindow_layout.setFocusableInTouchMode(true);
			mPopupWindow = new PopupWindow(popwindow_layout,
					LayoutParams.FILL_PARENT, popuHeight);
			mPopupWindow.setOnDismissListener(this);
			// 设置可以获取焦点，否则弹出菜单中的EditText是无法获取输入的
			mPopupWindow.setFocusable(true);
			// 这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			// 防止虚拟软键盘被弹出菜单遮住

			mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			mPopupWindow
					.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			mPopupWindow.setAnimationStyle(R.style.AnimCommentList);
			// }
			// 设置布局数据
			if (popuLayout == REPLY_LIST_LAYOUT) {
				reqCommNet(lv_comment_list, id);// 回复
			} else {
				setPopuData();// 选集等
			}
			mPopupWindow.showAtLocation(
					findViewById(R.id.play_process_listview), Gravity.RIGHT
							| Gravity.BOTTOM, 0, 0);
		} else {
			mPopupWindow.dismiss();
		}
		isOpenPopu = !isOpenPopu;
	}

	/**
	 * 设置布局数据
	 */
	private void setPopuData() {
		switch (popuLayout) {
		case SELECT_NUM_LAYOUT:
		case CACHE_LAYOUT:
			if (PrefUtils.getBoolean(UIUtils.getContext(),
					GlobalConstant.IS_LOGINED, false)) {

				ViewPager pager = (ViewPager) popwindow_layout
						.findViewById(R.id.pager);
				GoogleMusicAdapter googleMusicAdapter = new GoogleMusicAdapter();
				pager.setAdapter(googleMusicAdapter);
				TabPageIndicator indicator = (TabPageIndicator) popwindow_layout
						.findViewById(R.id.indicator);
				indicator.setVisibility(View.VISIBLE);
				indicator.setViewPager(pager);
			}

			break;
		case PLAY_SOURCE_LAYOUT:
			final PullToRefreshListView lv_comment_list = (PullToRefreshListView) popwindow_layout
					.findViewById(R.id.lv_comment_list);
			lv_comment_list.setMode(Mode.PULL_FROM_END);
			lv_comment_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					tv_id = playSourceNets.get(position - 1).id;
					initNet();
					closePop();
				}
			});
			playSourceNet(lv_comment_list);
			lv_comment_list
					.setOnRefreshListener(new OnRefreshListener<ListView>() {
						@Override
						public void onRefresh(
								PullToRefreshBase<ListView> refreshView) {
							playSourceNet(lv_comment_list);
						}
					});

			break;
		}
	}

	/**
	 * 资源来源
	 */
	private List<Cfilmlist> playSourceNets;

	private class PlaySourceNetBean extends BaseBaen {
		List<Cfilmlist> cont;
	}

	private int sourcePage = 1;

	private void playSourceNet(final PullToRefreshListView view) {
		mHttpHelp.sendGet(mbean.url.getsource + "page=" + sourcePage,
				PlaySourceNetBean.class,
				new MyRequestCallBack<PlaySourceNetBean>() {
					@Override
					public void onSucceed(PlaySourceNetBean bean) {
						if (bean == null) {
							view.onRefreshComplete();
							UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
							return;
						}
						if (playSourceNets == null) {
							playSourceNets = new ArrayList<Cfilmlist>();
						}
						playSourceNets.addAll(bean.cont);
						sourcePage++;
						if (playSourceNets.size() > 0) {
							setPlaySourceAdapter(view);
						} else {
							UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
						}
						view.onRefreshComplete();
					}
				});

	}

	/**
	 * 请求网络获取数据
	 * 
	 * @param lv_comment_list2
	 */
	PlaysourceAdapter playsourceAdapter;

	private void setPlaySourceAdapter(PullToRefreshListView view) {
		if (playsourceAdapter == null) {
			playsourceAdapter = new PlaysourceAdapter(0);
			view.setAdapter(playsourceAdapter);
		} else {
			playsourceAdapter.notifyDataSetInvalidated();
		}

	}

	/**
	 * 播放来源页adapter
	 * 
	 * @author jjm
	 * 
	 */
	private class PlaysourceAdapter extends AppBaseAdapter {
		public PlaysourceAdapter(int count) {
			super(count);
		}

		@Override
		public int getCount() {
			return playSourceNets.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(UIUtils.getContext(),
						R.layout.textview_date, null);
			}
			TextView tv = (TextView) convertView
					.findViewById(R.id.tv_textview_date);
			tv.setText(playSourceNets.get(position).name.trim());
			return convertView;
		}

	}

	/**
	 * 得到popu布局
	 */
	private void setPopuWindowLayout() {
		switch (popuLayout) {
		case REPLY_LIST_LAYOUT:
			popwindow_layout = (LinearLayout) LayoutInflater.from(this)
					.inflate(R.layout.music_popwindow, null);
			lv_comment_list = (PullToRefreshListView) popwindow_layout
					.findViewById(R.id.lv_comment_list);
			popwindow_layout.findViewById(R.id.popu_back).setOnClickListener(
					this);
			break;
		case CACHE_LAYOUT:
			popwindow_layout = (LinearLayout) LayoutInflater.from(this)
					.inflate(R.layout.play_sourc_popwindow, null);
			popwindow_layout.findViewById(R.id.popu_back).setOnClickListener(
					this);
			popwindow_layout.findViewById(R.id.down_layout).setVisibility(
					View.VISIBLE);
			popwindow_layout.findViewById(R.id.tv_down_btn).setOnClickListener(
					this);
			break;
		case SELECT_NUM_LAYOUT:
			popwindow_layout = (LinearLayout) LayoutInflater.from(this)
					.inflate(R.layout.play_sourc_popwindow, null);
			popwindow_layout.findViewById(R.id.popu_back).setOnClickListener(
					this);
			break;
		case PLAY_SOURCE_LAYOUT:
			popwindow_layout = (LinearLayout) LayoutInflater.from(this)
					.inflate(R.layout.play_list_popwindow, null);

			popwindow_layout.findViewById(R.id.popu_back).setOnClickListener(
					this);
			break;
		}
	}

	class GoogleMusicAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return gridPage;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(UIUtils.getContext(),
					R.layout.item_tv_list_tabpageindicator, null);
			GridView item_tv_list = (GridView) view
					.findViewById(R.id.item_tv_list);
			setItemAdapter(item_tv_list, position);
			container.addView(view);
			return view;
		}
	}

	/**
	 * 
	 * @param lv_comment_list
	 */
	private int CommReplyPage = 1;// 回复页面数
	private ReplyChildBean mChildBean;// 评论数据
	private List<Son> mChilds;

	private void reqCommNet(final PullToRefreshListView view, final String id) {
		mHttpHelp.sendGet(mbean.url.replylist + HttpHelp.FLAG_PAGE
				+ CommReplyPage + "&pid=" + id, ReplyChildBean.class,
				new MyRequestCallBack<ReplyChildBean>() {
					private ReplyChildAdater replyChildAdater;

					@Override
					public void onSucceed(ReplyChildBean bean) {
						if (bean == null || bean.cont.son == null
								|| bean.cont.son.size() == 0) {
							UIUtils.showToast(UIUtils.getContext(), "没有更多数据了");
							view.onRefreshComplete();
							return;
						}

						mChildBean = bean;

						if (mChilds == null) {

							mChilds = bean.cont.son;
						} else {
							mChilds.addAll(bean.cont.son);
						}
						if (replyChildAdater == null) {
							replyChildAdater = new ReplyChildAdater(0);
							view.setAdapter(replyChildAdater);
							view.setMode(Mode.PULL_FROM_END);
							view.setOnRefreshListener(new OnRefreshListener<ListView>() {

								@Override
								public void onRefresh(
										PullToRefreshBase<ListView> refreshView) {
									reqCommNet(view, id);
								}
							});
						} else {
							replyChildAdater.notifyDataSetInvalidated();
						}
						CommReplyPage++;
						view.onRefreshComplete();
					}
				});
	}

	public void setItemAdapter(GridView view, int position) {
		ItemAdapter itemAdapter = new ItemAdapter(position);
		view.setAdapter(itemAdapter);
		// 选集条目点击事件
	}

	private class ItemAdapter extends AppBaseAdapter {
		private int pagePosition;

		public ItemAdapter(int position) {
			super(position);
			this.pagePosition = position;
		}

		@Override
		public int getCount() {
			return tvListNum - 50 * pagePosition > 50 ? 50 : tvListNum - 50
					* pagePosition;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderCross holder;
			if (convertView == null) {
				holder = new ViewHolderCross();
				convertView = View.inflate(PlayActivity.this,
						R.layout.item_scroll_view, null);
				holder.setView(convertView);
			} else {
				holder = (ViewHolderCross) convertView.getTag();
			}
			holder.setPopuData(pagePosition, position);
			return convertView;
		}
	}

	/**
	 * 初始化播放信息
	 */
	protected void initPlayInfo() {
		Cfilminfo cfilminfo = mbean.cont.cfilminfo;
		if (cfilminfo == null) {
			return;
		}
		tv_step_down.setText(cfilminfo.down);
		tv_step_up.setText(cfilminfo.up);
		tv_Play_number.setText(cfilminfo.bfclick);
		String user_fav_zan_c = mbean.cont.relinfo.user_fav_zan_c;
		flag_thumbUp = user_fav_zan_c;
		flag_collect = mbean.cont.relinfo.user_fav_find_c;
		if (!"0".equals(mbean.cont.relinfo.user_fav_find_c)) {
			tv_collect.setSelected(true);
		}
		tv_step_down.setOnClickListener(this);
		tv_step_up.setOnClickListener(this);
		tv_collect.setOnClickListener(this);
		if ("1".equals(user_fav_zan_c)) {
			tv_step_up.setSelected(true);
			tv_step_down.setSelected(false);
		} else if ("2".equals(user_fav_zan_c)) {
			tv_step_down.setSelected(true);
			tv_step_up.setSelected(false);
		} else {
			tv_step_down.setSelected(false);
			tv_step_up.setSelected(false);
		}

	}

	/**
	 * 初始化电视剧剧集
	 */
	private boolean isHasTVNum = false;// 默认没有

	protected void initTVListNum() {
		tvListNum = (int) StringNumUtils.string2Num(mbean.cont.ckuinfo.jujinum);
		if (tvListNum > 0) {
			isHasTVNum = true;
			getPageTitle();
		}
	}

	private int gridPage;// 弹框列表页数
	private String[] titles;// 标头

	/**
	 * 获取选集表头
	 */
	private void getPageTitle() {

		if (tvListNum % 50 == 0) {
			gridPage = tvListNum / 50;
		} else {
			gridPage = tvListNum / 50 + 1;
		}
		titles = new String[gridPage];
		for (int i = 1; i <= gridPage; i++) {
			if ((tvListNum - (i - 1) * 50) > 50) {
				titles[i - 1] = (i - 1) * 50 + 1 + "-" + i * 50;
			} else {
				titles[i - 1] = (i - 1) * 50 + 1 + "-" + (tvListNum);
			}
		}
	}

	/***
	 * 初始化listview头部信息
	 * 
	 * @param holder
	 */
	private void initHeadView(ViewHolder2 holder) {
		if (recku == null || recku.size() != 3) {
			return;
		}
		mHttpHelp.showImage(holder.iv_icon_1, recku.get(0).pic);
		mHttpHelp.showImage(holder.iv_icon_2, recku.get(1).pic);
		mHttpHelp.showImage(holder.iv_icon_3, recku.get(2).pic);
		String msg = mbean.cont.commentCount == "" ? "0"
				: mbean.cont.commentCount;
		holder.tv_total_reply.setText("已有" + msg + "条评论，快来说两句吧！");
		holder.tv_name_1.setText(recku.get(0).name);
		holder.tv_name_2.setText(recku.get(1).name);
		holder.tv_name_3.setText(recku.get(2).name);
		holder.tv_des_1.setText(recku.get(0).sdesc);
		holder.tv_des_2.setText(recku.get(1).sdesc);
		holder.tv_des_3.setText(recku.get(2).sdesc);
		setIconClick(holder.iv_icon_1, recku.get(0));
		setIconClick(holder.iv_icon_2, recku.get(1));
		setIconClick(holder.iv_icon_3, recku.get(2));
	}

	private void setIconClick(View view, final TV tv) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!"1".equals(tv.hasfilm)) {
					UIUtils.showToast(
							UIUtils.getContext(),
							UIUtils.getContext().getResources()
									.getString(R.string.state_not_resource));
					return;
				}

				Intent intent = new Intent(PlayActivity.this,
						PlaySourceActivity.class);
				intent.putExtra(GlobalConstant.TV_ID, tv.id);
				startActivity(intent);
				finish();
				sendBroadcast(new Intent(
						GlobalConstant.CLOSE_PLAY_SOURCE_ACTIVITY));
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (isHasTVNum) {

		}
		if (mAapter == null) {
			mAapter = new PlayProcessAapter(0);
			play_process_listview.setAdapter(mAapter);

			play_process_listview.setOnScrollListener(this);
			setOnRefreshListener(play_process_listview);
		} else {
			mAapter.notifyDataSetInvalidated();
		}
	}

	/**
	 * 设置刷新监听
	 * 
	 * @param play_process_listview
	 */
	private void setOnRefreshListener(
			PullToRefreshListView play_process_listview) {

		play_process_listview
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						refreshNet();
					}
				});
	}

	/**
	 * 刷新请求网络
	 */
	// private int reckuPag = 2;// 默认电影页数s
	private int commentlisPage = 2;// 默认回复页数

	protected void refreshNet() {
		mHttpHelp.sendGet(mbean.url.commentlist + HttpHelp.FLAG_PAGE
				+ commentlisPage + "&cfilmid=" + tv_id, PlayRefreshBean.class,
				new MyRequestCallBack<PlayRefreshBean>() {
					@Override
					public void onSucceed(PlayRefreshBean bean) {
						if (bean == null || bean.cont == null
								|| bean.cont.size() == 0) {
							play_process_listview.onRefreshComplete();
							return;
						}
						commentlisPage++;
						mCommentlists.addAll(bean.cont);
						mAapter.notifyDataSetInvalidated();
						play_process_listview.onRefreshComplete();
					}
				});
	}

	/**
	 * listview
	 * 
	 * @author jjm
	 * 
	 */
	private class ViewHolder1 {
		TextView tv_user_name, tv_content, tv_user_time;
		LinearLayout add_comment_layout;
		ImageView iv_note_dialogue, iv_user_icon;
		TextView tv_show_reply;
		RelativeLayout bg_child_reply_layout;
		View item_reply_line;

		public void setView(View convertView) {
			add_comment_layout = (LinearLayout) convertView
					.findViewById(R.id.add_comment_layout);
			bg_child_reply_layout = (RelativeLayout) convertView
					.findViewById(R.id.bg_child_reply_layout);
			iv_note_dialogue = (ImageView) convertView
					.findViewById(R.id.iv_note_dialogue);
			item_reply_line = (View) convertView
					.findViewById(R.id.item_reply_line);
			tv_show_reply = (TextView) convertView
					.findViewById(R.id.tv_show_reply);
			tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			tv_user_name = (TextView) convertView
					.findViewById(R.id.tv_user_name);
			iv_user_icon = (ImageView) convertView
					.findViewById(R.id.iv_user_icon);
			tv_user_time = (TextView) convertView
					.findViewById(R.id.tv_user_time);
			convertView.setTag(this);
		}

		public void setData(Commentlist commentlist) {
			tv_user_name.setText(commentlist.nickname);
			String url = commentlist.face + "##";
			mHttpHelp.showImage(iv_user_icon, url);
			tv_content.setText(commentlist.msg);
			tv_user_time.setText(commentlist.time);

		}
	}

	private class ViewHolder2 {
		TextView tv_name_1, tv_name_2, tv_name_3, tv_des_1, tv_des_2, tv_des_3,
				tv_total_reply;
		ImageView iv_icon_1, iv_icon_2, iv_icon_3;
		View iv_refrush_icon;
		LinearLayout rl_reply_layout;

		public void setView(View convertView) {
			rl_reply_layout = (LinearLayout) convertView
					.findViewById(R.id.rl_reply_layout);
			tv_total_reply = (TextView) convertView
					.findViewById(R.id.tv_total_reply);
			iv_icon_1 = (ImageView) convertView.findViewById(R.id.iv_icon_1);
			iv_icon_2 = (ImageView) convertView.findViewById(R.id.iv_icon_2);
			iv_icon_3 = (ImageView) convertView.findViewById(R.id.iv_icon_3);
			tv_name_1 = (TextView) convertView.findViewById(R.id.tv_name_1);
			tv_name_2 = (TextView) convertView.findViewById(R.id.tv_name_2);
			tv_name_3 = (TextView) convertView.findViewById(R.id.tv_name_3);
			tv_des_1 = (TextView) convertView.findViewById(R.id.tv_des_1);
			tv_des_2 = (TextView) convertView.findViewById(R.id.tv_des_2);
			tv_des_3 = (TextView) convertView.findViewById(R.id.tv_des_3);
			iv_refrush_icon = convertView.findViewById(R.id.iv_refrush_icon);// 刷新

			setAnim(this, iv_refrush_icon);
			convertView.setTag(this);
		}
	}

	/**
	 * 播放列表
	 * 
	 * @author jjm
	 * 
	 */
	private TvListAdapter tvListAdapter;

	private class ViewHolder3 implements OnItemClickListener, OnClickListener {
		GridView grid_play_list;
		TextView tv_play_source;
		TextView tv_dowm_cache;
		TextView tv_select_num;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_play_source:
				popuLayout = PLAY_SOURCE_LAYOUT;
				break;
			case R.id.tv_dowm_cache:
				if (PrefUtils.getBoolean(UIUtils.getContext(),
						GlobalConstant.IS_LOGINED, false)) {
					// 若当前状态为登陆时，设置相应状态，
					popuLayout = CACHE_LAYOUT;
				} else {
					// 否则跳到退出页面
					startActivity(new Intent(getApplicationContext(),
							QuitLoginActivity.class));
				}

				break;
			case R.id.tv_select_num:
				popuLayout = SELECT_NUM_LAYOUT;
				break;
			}
			showPopopWindow("");
		}

		public void setView(View convertView) {

			grid_play_list = (GridView) convertView
					.findViewById(R.id.grid_play_list);
			tv_play_source = (TextView) convertView
					.findViewById(R.id.tv_play_source);
			tv_dowm_cache = (TextView) convertView
					.findViewById(R.id.tv_dowm_cache);
			tv_select_num = (TextView) convertView
					.findViewById(R.id.tv_select_num);
			tv_dowm_cache.setOnClickListener(this);
			tv_select_num.setOnClickListener(this);
			tv_play_source.setOnClickListener(this);
			convertView.setTag(this);
		}

		public void setData() {// 横向剧集
			tvListAdapter = new TvListAdapter(tvListNum > 8 ? 8 : tvListNum);

			grid_play_list.setAdapter(tvListAdapter);
			grid_play_list.setOnItemClickListener(this);
			tvListAdapter.notifyDataSetInvalidated();
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 7 && tvListNum > 8) {
				popuLayout = SELECT_NUM_LAYOUT;
				showPopopWindow("");
				return;
			}
			List<Cfilmlist> jujiinfos = mbean.cont.jujiinfo;
			boolean isHasPlay = false;
			for (int i = 0; i < jujiinfos.size(); i++) {
				if (((position + current_play_position_num) + "")
						.equals(jujiinfos.get(i).jindex)) {
					tv_id = jujiinfos.get(i).id;
					initNet();
					land_gridClick_Item = position;

					if (tvListAdapter != null) {
						tvListAdapter.notifyDataSetChanged();
					}
					isHasPlay = true;
					break;
				}
			}
			if (!isHasPlay) {
				UIUtils.showToast(UIUtils.getContext(), getResources()
						.getString(R.string.state_not_resource));
			}

		}
	}

	private int land_gridClick_Item = 0;// 横向剧集点击位置
	private int current_play_position_num = 1;// 当前横向播放位置

	class ViewHolderCross {
		TextView tv_scroll;
		View mView;

		public void setView(View convertView) {
			tv_scroll = (TextView) convertView.findViewById(R.id.tv_scroll);
			convertView.setTag(this);
		}

		public void setData(int position) {
			if (land_gridClick_Item == position) {
				tv_scroll.setTextColor(getResources().getColor(
						R.color.main_color));
			} else {
				tv_scroll.setTextColor(getResources().getColor(
						R.color.word_black));
			}
			if (position == 7 && tvListNum > 8) {
				tv_scroll.setText("...");
			} else {
				tv_scroll.setText((position + current_play_position_num) + "");
			}
		}

		public void setPopuData(int pagePosition, int position) {
			tv_scroll.setText(pagePosition * 50 + position + 1 + "");
			tv_scroll.setTextColor(getResources().getColor(R.color.word_black));
			setClickItem(tv_scroll);
			// if (gridPopuClickItem == position) {
			// tv_scroll.setTextColor(getResources()
			// .getColor(R.color.main_color));
			// } else {
			// tv_scroll.setTextColor(getResources()
			// .getColor(R.color.word_black));
			// }

		}
	}

	/**
	 * 
	 */
	private List<String> dowmArrList;

	public void setClickItem(final TextView tv_scroll) {
		tv_scroll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tvNum = tv_scroll.getText().toString();
				if (TextUtils.isEmpty(tvNum)) {
					return;
				}
				boolean isHasPlay = false;
				List<Cfilmlist> jujiinfos = mbean.cont.jujiinfo;
				if (jujiinfos == null || jujiinfos.size() == 0) {
					return;
				}
				String hash = null;
				for (Cfilmlist cfilmlist : jujiinfos) {
					if (tvNum.equals(cfilmlist.jindex)) {
						hash = cfilmlist.lmlinkurl;
						tv_scroll.setTextColor(getResources().getColor(
								R.color.main_color));
						if (popuLayout == SELECT_NUM_LAYOUT) {
							tv_id = cfilmlist.id;
							initNet();
							current_play_position_num = (int) StringNumUtils
									.string2Num(tvNum);
							if (tvListAdapter != null) {
								land_gridClick_Item = 0;
								tvListAdapter.notifyDataSetChanged();
							}
						}
						isHasPlay = true;
					}
				}
				if (!isHasPlay) {
					UIUtils.showToast(UIUtils.getContext(), "该剧集暂时没有");
					return;
				}
				if (popuLayout == CACHE_LAYOUT) {
					if (dowmArrList == null) {
						dowmArrList = new ArrayList<String>();
					}
					if (!isHasPlay || dowmArrList.contains(tvNum)) {
						dowmArrList.remove(tvNum);
						tv_scroll.setTextColor(getResources().getColor(
								R.color.word_black));
					} else {
						if (!TextUtils.isEmpty(hash)) {
							dowmArrList.add(hash);
						}
						tv_scroll.setTextColor(getResources().getColor(
								R.color.main_color));
						if (popwindow_layout != null) {
							((TextView) popwindow_layout
									.findViewById(R.id.tv_down_num))
									.setText("已选择" + dowmArrList.size() + "个");
						}
					}
					return;
				}

				closePop();
			}
		});

	}

	private class TvListAdapter extends AppBaseAdapter {
		public TvListAdapter(int count) {
			super(count);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderCross holder;
			if (convertView == null) {
				holder = new ViewHolderCross();
				convertView = View.inflate(PlayActivity.this,
						R.layout.item_scroll_view, null);
				holder.setView(convertView);

			} else {
				holder = (ViewHolderCross) convertView.getTag();
			}
			holder.setData(position);
			return convertView;
		}

	}

	public static final int FLAG_PLAY_LIST = 2;// 头
	public static final int FLAG_HEAD = 0;// 头
	public static final int FLAG_DEFAULT = 1;// 默认
	private LinearLayout popwindow_layout;
	private PopupWindow mPopupWindow;
	private PullToRefreshListView lv_comment_list;

	private class PlayProcessAapter extends AppBaseAdapter {
		public PlayProcessAapter(int count) {
			super(count);
		}

		@Override
		public int getViewTypeCount() {
			return 3;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == 0) {
				return FLAG_PLAY_LIST;
			}
			if (position == 1) {
				return FLAG_HEAD;
			}
			return FLAG_DEFAULT;
		}

		@Override
		public int getCount() {
			return mCommentlists.size() + 2;
		}

		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		ViewHolder3 holder3 = null;

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			int itemViewType = getItemViewType(position);

			if (convertView == null) {
				switch (itemViewType) {
				case FLAG_PLAY_LIST:
					holder3 = new ViewHolder3();
					convertView = View.inflate(getApplicationContext(),
							R.layout.item_play_list, null);
					holder3.setView(convertView);
					break;
				case FLAG_HEAD:
					holder2 = new ViewHolder2();
					convertView = View.inflate(getApplicationContext(),
							R.layout.tv_list_layout, null);
					holder2.setView(convertView);
					holder2.rl_reply_layout
							.setOnClickListener(new ListViewButtonOnClickListener(
									-1, -2));
					break;
				case FLAG_DEFAULT:
					holder1 = new ViewHolder1();
					convertView = View.inflate(getApplicationContext(),
							R.layout.item_play_process_listview, null);
					holder1.setView(convertView);

					break;
				}
			} else {
				switch (itemViewType) {
				case FLAG_HEAD:
					holder2 = (ViewHolder2) convertView.getTag();
					break;
				case FLAG_DEFAULT:
					holder1 = (ViewHolder1) convertView.getTag();
					break;
				case FLAG_PLAY_LIST:
					holder3 = (ViewHolder3) convertView.getTag();
					break;
				}
			}

			switch (itemViewType) {
			case FLAG_PLAY_LIST:
				holder3.setData();
				break;
			case FLAG_HEAD:
				initHeadView(holder2);
				break;
			case FLAG_DEFAULT:
				holder1.iv_note_dialogue
						.setOnClickListener(new ListViewButtonOnClickListener(
								position - 2, -1));

				Commentlist commentlist = mCommentlists.get(position - 2);
				// Commentlist commentlist = mCommentlists.get(position);
				holder1.setData(commentlist);

				if (commentlist.son != null) {
					holder1.bg_child_reply_layout.setVisibility(View.VISIBLE);
					if (StringNumUtils.string2Num(commentlist.replynum) <= 3) {
						holder1.tv_show_reply.setVisibility(View.GONE);
						holder1.item_reply_line.setVisibility(View.GONE);
					} else {
						holder1.tv_show_reply.setText("查看全部("
								+ commentlist.replynum + ")");
						holder1.tv_show_reply.setVisibility(View.VISIBLE);
						holder1.item_reply_line.setVisibility(View.VISIBLE);
						setReplyListener(holder1.tv_show_reply, commentlist.id);
					}
					addReplyNum(holder1.tv_show_reply,
							holder1.add_comment_layout, commentlist,
							position - 2);
				} else {
					holder1.add_comment_layout.removeAllViews();
					holder1.bg_child_reply_layout.setVisibility(View.GONE);
					holder1.tv_show_reply.setVisibility(View.GONE);
				}
				break;

			}
			return convertView;
		}

		private class ListViewButtonOnClickListener implements OnClickListener {
			private int position;// 记录ListView中Button所在的Item的位置
			private int clickItem;

			// private LinearLayout layout;

			public ListViewButtonOnClickListener(int position, int clickItem) {
				this.position = position;
				this.clickItem = clickItem;

			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.iv_note_dialogue:
					if (PrefUtils.getBoolean(UIUtils.getContext(),
							GlobalConstant.IS_LOGINED, false)) {
						InputMethodManager imm = (InputMethodManager) v
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
						findViewById(R.id.etComment)
								.setVisibility(View.VISIBLE);
						findViewById(R.id.btnSendComment).setVisibility(
								View.VISIBLE);
						if (clickItem == -2) {

						} else if (clickItem == -1) {
							((EditText) findViewById(R.id.etComment))
									.setHint("回复： "
											+ mCommentlists.get(position).nickname);
						} else {

							((EditText) findViewById(R.id.etComment))
									.setHint("回复： "
											+ mCommentlists.get(position).son
													.get(clickItem).nickname);
						}
						findViewById(R.id.etComment).setFocusable(true);
						findViewById(R.id.etComment).requestFocus();
						findViewById(R.id.btnSendComment).setOnClickListener(
								new ListViewButtonOnClickListener(position,
										clickItem));

					} else {
						startActivity(new Intent(getApplicationContext(),
								QuitLoginActivity.class));
					}
					break;
				case R.id.rl_reply_layout:
					if (PrefUtils.getBoolean(UIUtils.getContext(),
							GlobalConstant.IS_LOGINED, false)) {
						InputMethodManager imm3 = (InputMethodManager) v
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						imm3.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
						findViewById(R.id.etComment)
								.setVisibility(View.VISIBLE);
						findViewById(R.id.btnSendComment).setVisibility(
								View.VISIBLE);
						if (clickItem == -2) {
							((EditText) findViewById(R.id.etComment))
									.setHint("输入评论");
						} else if (clickItem == -1) {

							((EditText) findViewById(R.id.etComment))
									.setHint("回复： "
											+ mCommentlists.get(position).nickname);
						} else {
							((EditText) findViewById(R.id.etComment))
									.setHint("回复： "
											+ mCommentlists.get(position).son
													.get(clickItem).nickname);
						}
						findViewById(R.id.etComment).setFocusable(true);
						findViewById(R.id.etComment).requestFocus();
						findViewById(R.id.btnSendComment).setOnClickListener(
								new ListViewButtonOnClickListener(position,
										clickItem));
					} else {
						startActivity(new Intent(getApplicationContext(),
								QuitLoginActivity.class));
					}

					break;
				case R.id.btnSendComment:
					String commentString = ((EditText) findViewById(R.id.etComment))
							.getEditableText().toString();
					if ("".equals(commentString.trim())) {
						UIUtils.showToast(UIUtils.getContext(), "评论不能为空");
					} else if (commentString.trim().length() > 150) {
						UIUtils.showToast(UIUtils.getContext(), "评论字数不能超过150");
					} else {
						List<Son> sons = null;
						Commentlist commentlist = null;
						Son son = null;
						if (clickItem != -2) {
							son = new Son();
							son.nickname = "我";
							son.msg = commentString;
							son.time = " 刚刚 ";
							sons = mCommentlists.get(position).son;
							if (sons == null) {
								sons = new ArrayList<Son>();
							}
						} else {
							commentlist = new Commentlist();
						}
						((EditText) findViewById(R.id.etComment)).setText("");
						findViewById(R.id.etComment).setVisibility(View.GONE);
						findViewById(R.id.btnSendComment).setVisibility(
								View.GONE);
						InputMethodManager imm2 = (InputMethodManager) v
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						imm2.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);
						if (clickItem == -2) {
							sendReply("0", "0", commentString);
						} else if (clickItem == -1) {

							sendReply(mCommentlists.get(position).id,
									mCommentlists.get(position).id,
									commentString);
						} else {
							sendReply(mCommentlists.get(position).id,
									mCommentlists.get(position).son
											.get(clickItem).id, commentString);
						}
						if (clickItem != -2) {

							if (sons.size() > 2) {
								sons.remove(2);
							}
							sons.add(0, son);
							mCommentlists.get(position).replynum = ((int) StringNumUtils
									.string2Num(mCommentlists.get(position).replynum) + 1)
									+ "";
							mCommentlists.get(position).son = sons;
						} else {
							if (commentlist != null) {

								// commentlist.nickname = mbean.cont.nickname;
								commentlist.nickname = PrefUtils.getString(
										GlobalConstant.USER_NICKNAME, "");
								commentlist.time = "刚刚";
								commentlist.face = GlobalConstant.HEAD_ICON_PATH;
								commentlist.msg = commentString;
								mCommentlists.add(0, commentlist);
							}
						}
						mAapter.notifyDataSetChanged();
					}
					break;
				}
			}
		}

		private void addReplyNum(TextView tv_show_reply, LinearLayout view,
				Commentlist commentlist, int position) {

			view.removeAllViews();

			int size = commentlist.son.size();
			View item_reply;
			for (int i = 0; i < size; i++) {
				item_reply = LinearLayout.inflate(UIUtils.getContext(),
						R.layout.item_reply_listview, null);
				if (i == 0) {
					item_reply.findViewById(R.id.item_reply_line)
							.setVisibility(View.GONE);
				}
				((TextView) item_reply.findViewById(R.id.tv_user_name))
						.setText(commentlist.son.get(i).nickname);
				TextView tv_item_user_name = (TextView) item_reply
						.findViewById(R.id.tv_item_user_name);
				tv_item_user_name.setText(commentlist.nickname);
				((TextView) item_reply.findViewById(R.id.tv_content))
						.setText(commentlist.son.get(i).msg);
				// ((TextView) item_reply.findViewById(R.id.tv_user_time))
				// .setText(commentlist.son.get(i).time);
				// tv_show_reply.setTextColor(R.color.word_black);
				tv_show_reply.setText(commentlist.son.get(i).time);
				item_reply.findViewById(R.id.iv_note_dialogue)
						.setOnClickListener(
								new ListViewButtonOnClickListener(position, i));

				view.addView(item_reply);
			}

		}
	}

	/**
	 * 设置查看更多监听
	 * 
	 * @param tv_show_reply
	 * @param id
	 */
	public void setReplyListener(View view, final String id) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popuLayout = REPLY_LIST_LAYOUT;
				showPopopWindow(id);
			}
		});

	}

	public static final int MAIN_COMM = 0;
	public static final int CHILD_COMM = 1;
	private String tv_id;

	public class ReplyChildAdater extends AppBaseAdapter {
		public ReplyChildAdater(int count) {
			super(count);

		}

		@Override
		public int getItemViewType(int position) {
			if (position == 0) {
				return MAIN_COMM;
			}
			return CHILD_COMM;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getCount() {
			return mChilds.size() + 1;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder1 holder1 = null;// 主
			ViewHolder1 holder2 = null;// 子评论
			int itemViewType = getItemViewType(position);
			if (convertView == null) {

				switch (itemViewType) {
				case MAIN_COMM:
					holder1 = new ViewHolder1();
					convertView = View.inflate(getApplicationContext(),
							R.layout.item_play_process_listview, null);
					holder1.setView(convertView);
					holder1.iv_note_dialogue
							.setOnClickListener(new ListViewButtonOnClickListener(
									position, holder1.add_comment_layout));

					convertView.setTag(holder1);

					break;
				case CHILD_COMM:
					holder2 = new ViewHolder1();
					convertView = View.inflate(getApplicationContext(),
							R.layout.item_play_child_listview, null);
					holder2.setView(convertView);
					holder2.iv_note_dialogue
							.setOnClickListener(new ListViewButtonOnClickListener(
									position, holder2.add_comment_layout));

					convertView.setTag(holder2);
					break;
				}

			} else {
				switch (itemViewType) {
				case MAIN_COMM:
					holder1 = (ViewHolder1) convertView.getTag();
					break;
				case CHILD_COMM:
					holder2 = (ViewHolder1) convertView.getTag();
					break;

				}
			}

			switch (itemViewType) {
			case MAIN_COMM:
				Currrent_comment currrent_comment = mChildBean.cont.currrent_comment;
				holder1.tv_user_name.setText(currrent_comment.nickname);
				String url = currrent_comment.face + "##";
				mHttpHelp.showImage(holder1.iv_user_icon, url);
				holder1.tv_content.setText(currrent_comment.msg);
				holder1.tv_user_time.setText(currrent_comment.time);
				break;
			case CHILD_COMM:
				Son sons = mChilds.get(position - 1);
				holder2.tv_user_name.setText(sons.nickname);
				String url1 = sons.face + "##";
				mHttpHelp.showImage(holder2.iv_user_icon, url1);
				holder2.tv_content.setText(sons.msg);
				holder2.tv_user_time.setText(sons.time);
				break;
			}

			return convertView;
		}

		/**
		 * 评论列表监听
		 * 
		 * @author jjm
		 * 
		 */
		private class ListViewButtonOnClickListener implements OnClickListener {
			private int position;// 记录ListView中Button所在的Item的位置
			private LinearLayout layout;

			public ListViewButtonOnClickListener(int position,
					LinearLayout layout) {
				this.position = position;
				this.layout = layout;

			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.iv_note_dialogue:
					if (PrefUtils.getBoolean(UIUtils.getContext(),
							GlobalConstant.IS_LOGINED, false)) {
						InputMethodManager imm = (InputMethodManager) v
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
						popwindow_layout.findViewById(R.id.popu_etComment)
								.setVisibility(View.VISIBLE);
						popwindow_layout.findViewById(R.id.popu_btnSendComment)
								.setVisibility(View.VISIBLE);
						if (position == 0) {
							((EditText) popwindow_layout
									.findViewById(R.id.popu_etComment))
									.setHint("回复： "
											+ mChildBean.cont.currrent_comment.nickname);
						} else {

							((EditText) popwindow_layout
									.findViewById(R.id.popu_etComment))
									.setHint("回复： "
											+ mChilds.get(position - 1).nickname);
						}
						popwindow_layout.findViewById(R.id.popu_etComment)
								.setFocusable(true);
						popwindow_layout.findViewById(R.id.popu_etComment)
								.requestFocus();
						popwindow_layout.findViewById(R.id.popu_btnSendComment)
								.setOnClickListener(
										new ListViewButtonOnClickListener(
												position, layout));
					} else {
						startActivity(new Intent(getApplicationContext(),
								QuitLoginActivity.class));
					}

					break;
				case R.id.popu_btnSendComment:

					String commentString = ((EditText) popwindow_layout
							.findViewById(R.id.popu_etComment)).getText()
							.toString();
					Son son = new Son();
					son.nickname = "我";
					son.msg = commentString;
					if (position == 0) {
						son.nickname = mChildBean.cont.currrent_comment.nickname;
					} else {
						son.nickname = mChilds.get(position - 1).nickname;
					}
					son.time = "刚刚";
					mChilds.add(0, son);
					mAapter.notifyDataSetChanged();
					((EditText) popwindow_layout
							.findViewById(R.id.popu_etComment)).setText("");
					popwindow_layout.findViewById(R.id.popu_etComment)
							.setVisibility(View.GONE);
					popwindow_layout.findViewById(R.id.popu_btnSendComment)
							.setVisibility(View.GONE);
					InputMethodManager imm2 = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					imm2.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					if (position == 0) {
						sendReply(mChildBean.cont.currrent_comment.id,
								mChildBean.cont.currrent_comment.id,
								commentString);
					} else {
						Son son2 = mChilds.get(position - 1);
						sendReply(mChildBean.cont.currrent_comment.id, son2.id,
								commentString);
					}
					break;
				}
			}
		}
	}

	/**
	 * 点赞功能
	 */
	private String flag_thumbUp;// 点赞
	private String flag_collect;// 收藏

	private void thumbUpNet(int flag) {
		if ("1".equals(flag_thumbUp) || "2".equals(flag_thumbUp)) {
			UIUtils.showToast(UIUtils.getContext(),
					"您已经" + ("1".equals(flag_thumbUp) ? "点赞" : "点踩") + "了");
			return;
		}
		flag_thumbUp = flag + "";

		if (1 == flag) {
			int upNum = (int) StringNumUtils
					.string2Num(mbean.cont.cfilminfo.up);
			tv_step_up.setText((upNum + 1) + "");

			tv_step_up.setSelected(true);
			;
		} else {
			int downNum = (int) StringNumUtils
					.string2Num(mbean.cont.cfilminfo.down);
			tv_step_down.setText((downNum + 1) + "");
			tv_step_down.setSelected(true);
			;
		}
		tv_step_up.setClickable(false);
		tv_step_up.setFocusable(false);
		tv_step_down.setClickable(false);
		tv_step_down.setFocusable(false);
		mHttpHelp.sendGet(mbean.url.goDingCai + "&type=" + flag + "&filmid="
				+ tv_id + "&ucode=" + NetworkConfig.getUcode(), BaseBaen.class,
				new MyRequestCallBack<BaseBaen>() {

					@Override
					public void onSucceed(BaseBaen bean) {
						UIUtils.showToast(UIUtils.getContext(),
								bean == null ? "操作失败" : bean.msg);
						tv_step_up.setClickable(true);
						tv_step_up.setFocusable(true);
						tv_step_down.setClickable(true);
						tv_step_down.setFocusable(true);
					}
				});
	}

	/**
	 * 点击收集改变
	 */
	private void changeCollect() {
		String url = mbean.url.goFav;
		if ("0".equals(flag_collect)) {
			flag_collect = "1";
			tv_collect.setSelected(true);
			UIUtils.showToast(UIUtils.getContext(), "您已收藏成功");
			url += "&ucode=" + NetworkConfig.getUcode() + "&filmid="
					+ mbean.cont.cfilminfo.id + "&type=1";
		} else {
			flag_collect = "0";
			tv_collect.setSelected(false);
			UIUtils.showToast(UIUtils.getContext(), "您已取消收藏");
			url += "&ucode=" + NetworkConfig.getUcode() + "&filmid="
					+ mbean.cont.cfilminfo.id + "&type=0";

		}
		mHttpHelp.sendGet(url, BaseBaen.class,
				new MyRequestCallBack<BaseBaen>() {

					@Override
					public void onSucceed(BaseBaen bean) {
						if (bean == null) {
							return;
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_collect:// 收藏
			if (PrefUtils.getBoolean(UIUtils.getContext(),
					GlobalConstant.IS_LOGINED, false)) {// 判断当前状态是否为登陆状态
				changeCollect();
			} else {
				startActivity(new Intent(getApplicationContext(),
						QuitLoginActivity.class));
			}
			break;
		case R.id.play_show_name_layout:// 播放器返回
			if (vv_player == null || mediaController == null) {
				return;
			}
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else {
				stopVideoview();
				finish();
			}
			break;
		case R.id.tv_step_down:// 点踩
			thumbUpNet(2);
			break;
		case R.id.tv_step_up:// 点赞
			if (PrefUtils.getBoolean(UIUtils.getContext(),
					GlobalConstant.IS_LOGINED, false)) {
				thumbUpNet(1);
			} else {
				startActivity(new Intent(getApplicationContext(),
						QuitLoginActivity.class));
			}
			break;
		case R.id.popu_back:// 关闭弹窗恢复初始值
			closePop();
			break;
		case R.id.tv_share:// 分享
			sharePlatform();
			break;
		case R.id.tv_down_btn:
			if (dowmArrList == null || dowmArrList.size() == 0) {
				UIUtils.showToast(UIUtils.getContext(), "您还没有选择下载资源");
				return;
			}
			for (int i = 0; i < dowmArrList.size(); i++) {
				P2PManager.getInstance(getApplicationContext()).cacheP2PFile(
						dowmArrList.get(i));
			}
			break;
		}

	}

	private void stopVideoview() {
		// if (!isVideoviewStop) {
		// return;
		// }

		if (vv_player != null) {
			// isVideoviewStop = true;

			vv_player.stopPlayback();
			vv_player.release(true);
			vv_player.stopBackgroundPlay();
			vv_player.destroyDrawingCache();
			vv_player.closeCustomParmeter();
			IjkMediaPlayer.native_profileEnd();
			if (lmp2pMediaPlayer != null) {
				lmp2pMediaPlayer.finalize(hash);
				lmp2pMediaPlayer = null;
			}
			vv_player = null;
		}
		// else {
		// vv_player.enterBackground();
		// }

	}

	/**
	 * 关闭弹窗popu,恢复初始值
	 */
	private void closePop() {
		if (!isOpenPopu && mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	@Override
	public void onPause() {
		if (vv_player != null && mbean != null) {

			current_Play_Position = vv_player.getCurrentPosition();
			LogUtil.i(TAG, "停止播放时长 ： " + current_Play_Position);
			String fileHash = mbean.cont.cfilminfo.lmlink;
			// if (fileHash != null && fileHash.contains("//")) {
			// fileHash = fileHash.substring(fileHash.indexOf("//") + 2);
			// }
			ContentValues values = new ContentValues();
			values.put(CacheMessgeDao.COLUMN_TV_PLAY_END_TIME,
					vv_player.getDuration());
			values.put(CacheMessgeDao.COLUMN_TV_PLAY_POSITION,
					current_Play_Position);
			mDao.updateMessage(fileHash, values);
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (mHttpHelp != null) {
			mHttpHelp.stopHttpNET();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		clear();
		if (vv_player != null && mbean != null) {
			String fileHash = mbean.cont.cfilminfo.lmlink;
			P2PManager.getInstance(getApplicationContext()).stopP2PCache(
					fileHash);
		}
		if (NetWorkUtil.isWifiAvailable(this)) {
			P2PManager.getInstance(getApplicationContext()).startTask();
		} else {
			P2PManager.getInstance(getApplicationContext()).notifyWifi(0);
		}
		super.onDestroy();
	}

	/**
	 * 退出页面，清除
	 */
	private void clear() {
		if (!isOpenPopu && mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
			isOpenPopu = !isOpenPopu;
		}
		if (vv_player != null) {
			final String fileHash = lmp2pMediaPlayer.getLMHash();
			vv_player.stopPlayback();
			vv_player.destroyDrawingCache();
			// vv_player.unregisterReceiver();

			if (isPaly) {
				IjkMediaPlayer.native_profileEnd();
			}
			vv_player = null;
		}
		if (mObserver != null) {
			mObserver.stopScreenStateUpdate();
		}
	}

	/**
	 * 回复评论
	 * 
	 * @param topid
	 *            顶级ID
	 * @param pid
	 *            父级ID
	 * @param filmid
	 *            视频ID
	 * @param msg
	 *            消息
	 */
	private void sendReply(String topid, String pid, String msg) {
		Ckuinfo ckuinfo = mbean.cont.ckuinfo;
		if (mbean == null || ckuinfo == null || ckuinfo.id == null
				|| "".equals(ckuinfo.id)) {
			return;
		}
		if (mbean.cont.commentCount != null && topid == "0" && pid == "0") {
			mbean.cont.commentCount = (int) (StringNumUtils
					.string2Num(mbean.cont.commentCount) + 1) + "";
		}
		RequestParams param = new RequestParams();
		param.addBodyParameter("topid", topid);
		param.addBodyParameter("pid", pid);
		param.addBodyParameter("ckuid", ckuinfo.id);
		param.addBodyParameter("msg", msg);
		mHttpHelp.sendPost(NetworkConfig.getReplyComment(), param,
				CommentBean.class, new MyRequestCallBack<CommentBean>() {
					@Override
					public void onSucceed(CommentBean bean) {
						if (bean == null) {
							return;
						}
						if (Integer.parseInt(bean.status) == 1) {
							UIUtils.showToast(UIUtils.getContext(), "发送成功");
						} else {
							UIUtils.showToast(UIUtils.getContext(),
									"网络请求失败，请再发送一次");
						}
					}
				});

	}

	/**
	 * 初始化播放器
	 */
	private void initVideoView() {
		IjkMediaPlayer.loadLibrariesOnce(null);
		IjkMediaPlayer.native_profileBegin("libijkplayer.so");

		mediaController = new MediaController(this);
		vv_player.setMediaController(mediaController);
		vv_player.setMediaBufferingIndicator(buffering_indicator);
		vv_player.requestFocus();
		mediaController.setOnHiddenListener(this);
		mediaController.setOnShownListener(this);
		vv_player.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(IMediaPlayer mp, int what, int extra) {
				isPaly = false;
				return true;
			}
		});

	}

	/**
	 * 开始播放
	 * 
	 * @param fileHash
	 * 
	 * @param hash
	 */
	private Lmp2pMediaPlayer lmp2pMediaPlayer;

	private void startVideoView(String fileHash) {
		if (TextUtils.isEmpty(fileHash)) {
			LogUtil.e(TAG, "播放电影信息为null ");
			return;
		}

		if (tv_play_name != null) {
			CacheInfo cacheInfo = mDao.getCacheInfo(fileHash);
			if (cacheInfo != null) {
				LogUtil.e(TAG, "电影名称 ： " + cacheInfo.getTvName());
				tv_play_name.setText(cacheInfo.getTvName());
				current_Play_Position = cacheInfo.getTvPlayPosition();
			}
		}

		LogUtil.e(TAG, "电影hash ： " + fileHash);
		LogUtil.e(TAG, "电影已经播放时长 ： " + current_Play_Position);

		lmp2pMediaPlayer = new Lmp2pMediaPlayer();
		if (tv_play_speed_porgress != null) {
			lmp2pMediaPlayer.setPorgressShow(tv_play_speed_porgress);
		}
		lmp2pMediaPlayer.setLMPlayHash(hash, vv_player);
		// if (LogUtil.getDeBugState()) {
		// P2PManager.getInstance(getApplicationContext()).addQureyTask(new
		// Runnable() {
		//
		// @Override
		// public void run() {
		// runOnUiThread(new Runnable() {
		//
		// @SuppressWarnings("static-access")
		// @Override
		// public void run() {
		// if (tv_down_speed != null) {
		// tv_down_speed.setText("下载速度："
		// + lmp2pMediaPlayer
		// ._getP2pDownloadSpeed(hash));
		// }
		//
		// }
		// });
		// }
		// });
		// }

		// PlayManager.prepareToPlay(getApplicationContext(), vv_player,
		// fileHash,
		// "mp4", current_Play_Position, new OnPreparedListener() {
		//
		// @Override
		// public void onPrepared(String fileHash, String path,
		// int progress) {
		// if (vv_player != null) {
		// vv_player.setLimaoPlayMode();
		// vv_player.setVideoPath(path);
		// vv_player.seekTo(current_Play_Position);
		// vv_player.start();
		// screenObserver();
		// if (vv_player.mMediaBufferingIndicator != null) {
		//
		// vv_player.mMediaBufferingIndicator
		// .setVisibility(View.GONE);
		// }
		// }
		//
		// }
		//
		// @Override
		// public void onDownloadFailed(String hash, int state,
		// long fileSize) {
		//
		// if (fileSize > SDUtils.getCacheAvailableSize()) {
		// Intent intent = new Intent(PlayActivity.this,
		// AlertDialogActivity.class);
		// PlayActivity.this.startActivity(intent);
		// }
		//
		// }
		// });

		// 监听速度 --- test

		// if (LogUtil.getDeBugState()) {
		// PlayManager.setOnSpeedUpdateListener(fileHash,
		// new OnSpeedUpdateListener() {
		//
		// @Override
		// public void onSpeedUpdate(String fileHash, int speed) {
		// if (old_KB > 0) {
		// new_KB = NetWorkUtil
		// .getUidRxBytes(PlayActivity.this)
		// - old_KB;
		// }
		// old_KB = NetWorkUtil
		// .getUidRxBytes(PlayActivity.this);
		// if (tv_down_speed != null) {
		// if (iscolor) {
		// tv_down_speed.setTextColor(UIUtils
		// .getContext().getResources()
		// .getColor(R.color.bg_green));
		// } else {
		// tv_down_speed.setTextColor(UIUtils
		// .getContext().getResources()
		// .getColor(R.color.bg_white));
		// }
		// iscolor = !iscolor;
		// tv_down_speed.setText("   下载速度  : " + speed
		// + " KB/S" + "\n   实时网速  : " + new_KB
		// + "KB/S");
		// }
		// }
		// });
		// }

	}

	private boolean iscolor;

	/**
	 * 处理下载速度
	 */
	public long old_KB = 0;
	public long new_KB = 0;

	/**
	 * 注册屏幕关闭监听
	 */
	protected void screenObserver() {
		if (mObserver == null) {
			mObserver = new ScreenObserver(PlayActivity.this);
		}
		mObserver.requestScreenStateUpdate(new ScreenStateListener() {

			@Override
			public void onScreenOn() {
				// 屏幕开启时
				if (vv_player != null && !vv_player.isPlaying()) {
					vv_player.start();
				}
			}

			@Override
			public void onScreenOff() {
				// 屏幕关闭时
				if (vv_player != null && vv_player.isPlaying()) {
					vv_player.pause();
				}
			}
		});

	}

	/**
	 * 保存下载信息 Cfilminfo cfilminfo
	 * 
	 * @param cfilmlist
	 */
	private void saveInfo(Cfilminfo cfilminfo) {
		if (cfilminfo == null) {
			return;
		}
		CacheInfo info = new CacheInfo();
		String lmlinkurl = cfilminfo.lmlink;
		if (TextUtils.isEmpty(lmlinkurl)) {
			return;
		}
		info.setTvId(tv_id);
		info.setTvPlaynum(current_play_position_num + "");
		info.setTvHash(lmlinkurl);
		info.setTvName(cfilminfo.name);
		info.setTvPicPath(cfilminfo.pic);
		info.setTvDownFileSize(cfilminfo.filesize);
		mDao.saveMessage(info);
	}

	private View play_show_name_layout;
	private MediaController mediaController;
	// 当前播放的位置
	private long current_Play_Position;
	private View buffering_indicator;
	private CacheMessgeDao mDao;
	private ScreenObserver mObserver;

	// 获取总的接受字节数，包含Mobile和WiFi等

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (vv_player == null) {
			return;
		}
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (mPopupWindow != null && mPopupWindow.isShowing())
				mPopupWindow.dismiss();
			closeEditText();
			landscapeView();// 横
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			portraitView();// 竖
		}

	}

	public void portraitView() {
		if (vv_player != null) {
			showStatusBar(false, this);
			vv_player
					.setConfigurationChanged(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

	}

	public void landscapeView() {
		if (vv_player != null) {
			showStatusBar(true, this);
			vv_player
					.setConfigurationChanged(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	/**
	 * 去掉状态栏
	 * 
	 * @param enable
	 * @param activity
	 */
	public void showStatusBar(boolean enable, Activity activity) {

		if (enable) {
			WindowManager.LayoutParams lp = activity.getWindow()
					.getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			activity.getWindow().setAttributes(lp);
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = activity.getWindow()
					.getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			activity.getWindow().setAttributes(attr);
			activity.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	/**
	 * 播放器控制面板显示
	 */
	@Override
	public void onHidden() {
		if (play_show_name_layout != null) {
			play_show_name_layout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onShown() {
		if (play_show_name_layout != null) {
			play_show_name_layout.setVisibility(View.VISIBLE);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (vv_player != null
					&& this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				// return true;
			} else {
				stopVideoview();
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * popu消失监听
	 */
	@Override
	public void onDismiss() {
		isOpenPopu = true;
		if (mPopupWindow != null) {
			mPopupWindow = null;
		}
		if (dowmArrList != null && dowmArrList.size() != 0) {
			dowmArrList.clear();
		}
		if (playsourceAdapter != null) {
			playsourceAdapter = null;
			playSourceNets.clear();
		}
		if (CommReplyPage != 1) {
			CommReplyPage = 1;
		}
		if (mChilds != null) {
			mChilds.clear();
			mChilds = null;
		}

		// collectNet();// 收藏状态上传服务器
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		closeEditText();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

}
