package com.limaoso.phonevideo.base.impl.pager;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.PlaySourceActivity;
import com.limaoso.phonevideo.activity.SearchActivity;
import com.limaoso.phonevideo.activity.SearchResultPageMainActivity;
import com.limaoso.phonevideo.adapter.HomeAdapter;
import com.limaoso.phonevideo.base.BasePager;
import com.limaoso.phonevideo.bean.HomeBean;
import com.limaoso.phonevideo.bean.HomeBean.Cont.TV;
import com.limaoso.phonevideo.bean.TVbean;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.global.MyApplication;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.LoadRequestCallBack;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.play.PlayActivity;
import com.limaoso.phonevideo.top.bean.TopBean;
import com.limaoso.phonevideo.utils.AnimUtil;
import com.limaoso.phonevideo.utils.CheckUtils;
import com.limaoso.phonevideo.utils.IconUtils;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;

/**
 * 首页实现
 * 
 * @author liang
 * 
 */
public class HomePager extends BasePager implements OnFocusChangeListener {
	private HomeBean mBean;
	private View mView;
	private GridView gv_home_recommend; // 精彩推荐展示条目
	private GridView gv_home_movie; // 电影展示条目
	private GridView gv_home_tv; // 电视展示条目
	private GridView gv_home_anime; // 动漫展示条目
	private GridView gv_home_video; // 视频展示条目
	private GridView gv_home_top;
	public List<TV> hotkeys;
	public List<TV> indexrec;
	public List<TV> dianying;
	public List<TV> dianshi;
	public List<TV> dongman;
	public List<TV> shipin;
	private TextView tv_home_hot_1, tv_home_hot_2, tv_home_hot_3,
			tv_home_hot_4, tv_home_hot_5, tv_home_hot_6;

	private LinearLayout ll_fhp_movies, ll_fhp_recommend, ll_fhp_tv,
			ll_fhp_cartoon, ll_fhp_video, ll_fhp_top;

	private View view_fhp_movies, view_fhp_recommend, view_fhp_tv,
			view_fhp_cartoon, view_fhp_video, view_fhp_top;

	private ImageView iv_home_7;
	private HttpHelp mHttpHelp;

	private TopBean topBean;
	private HomeAdapter topAdapter;
	private int topPage = 1;

	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		if (mView == null) {
			super.initData();
			setSlidingMenuEnable(true);
			inLoading();
			// 显示首页的标题
			isHomePager();
			initView();
			initNet();
		}
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		mView = UIUtils.inflate(R.layout.frame_home_pager);

		ll_fhp_movies = (LinearLayout) mView.findViewById(R.id.ll_fhp_movies);
		ll_fhp_recommend = (LinearLayout) mView
				.findViewById(R.id.ll_fhp_recommend);
		ll_fhp_tv = (LinearLayout) mView.findViewById(R.id.ll_fhp_tv);
		ll_fhp_cartoon = (LinearLayout) mView.findViewById(R.id.ll_fhp_cartoon);
		ll_fhp_video = (LinearLayout) mView.findViewById(R.id.ll_fhp_video);
		ll_fhp_top = (LinearLayout) mView.findViewById(R.id.ll_fhp_top);

		view_fhp_movies = (View) mView.findViewById(R.id.view_fhp_movies);
		view_fhp_recommend = (View) mView.findViewById(R.id.view_fhp_recommend);
		view_fhp_tv = (View) mView.findViewById(R.id.view_fhp_tv);
		view_fhp_cartoon = (View) mView.findViewById(R.id.view_fhp_cartoon);
		view_fhp_video = (View) mView.findViewById(R.id.view_fhp_video);
		view_fhp_top = (View) mView.findViewById(R.id.view_fhp_top);

		gv_home_recommend = (GridView) mView
				.findViewById(R.id.gv_home_recommend);
		gv_home_movie = (GridView) mView.findViewById(R.id.gv_home_movie);
		gv_home_tv = (GridView) mView.findViewById(R.id.gv_home_tv);
		gv_home_anime = (GridView) mView.findViewById(R.id.gv_home_anime);
		gv_home_video = (GridView) mView.findViewById(R.id.gv_home_video);
		gv_home_top = (GridView) mView.findViewById(R.id.gv_home_top);

		tv_home_hot_1 = (TextView) mView.findViewById(R.id.tv_home_hot_1);
		tv_home_hot_2 = (TextView) mView.findViewById(R.id.tv_home_hot_2);
		tv_home_hot_3 = (TextView) mView.findViewById(R.id.tv_home_hot_3);
		tv_home_hot_4 = (TextView) mView.findViewById(R.id.tv_home_hot_4);
		tv_home_hot_5 = (TextView) mView.findViewById(R.id.tv_home_hot_5);
		tv_home_hot_6 = (TextView) mView.findViewById(R.id.tv_home_hot_6);
		tv_home_hot_icon_1 = (ImageView) mView
				.findViewById(R.id.tv_home_hot_icon_1);
		tv_home_hot_icon_2 = (ImageView) mView
				.findViewById(R.id.tv_home_hot_icon_2);
		tv_home_hot_icon_3 = (ImageView) mView
				.findViewById(R.id.tv_home_hot_icon_3);
		tv_home_hot_icon_4 = (ImageView) mView
				.findViewById(R.id.tv_home_hot_icon_4);
		tv_home_hot_icon_5 = (ImageView) mView
				.findViewById(R.id.tv_home_hot_icon_5);
		tv_home_hot_icon_6 = (ImageView) mView
				.findViewById(R.id.tv_home_hot_icon_6);
		et_home_find.setOnFocusChangeListener(this);
		mHttpHelp = new HttpHelp();
		mView.findViewById(R.id.layout_home_hot_1).setOnClickListener(
				new HotListener(tv_home_hot_1));
		mView.findViewById(R.id.layout_home_hot_2).setOnClickListener(
				new HotListener(tv_home_hot_2));
		mView.findViewById(R.id.layout_home_hot_3).setOnClickListener(
				new HotListener(tv_home_hot_3));
		mView.findViewById(R.id.layout_home_hot_4).setOnClickListener(
				new HotListener(tv_home_hot_4));
		mView.findViewById(R.id.layout_home_hot_5).setOnClickListener(
				new HotListener(tv_home_hot_5));
		mView.findViewById(R.id.layout_home_hot_6).setOnClickListener(
				new HotListener(tv_home_hot_6));

		iv_home_7 = (ImageView) mView.findViewById(R.id.iv_home_7);
		iv_home_7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (animUtil == null) {
					animUtil = new AnimUtil();
				}
				iv_home_7.setFocusable(false);
				iv_home_7.setClickable(false);
				animUtil.startRotateAnimation(iv_home_7);
				initTop();
			}
		});
		tv_basepager_skey.setText(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.SKEY, ""));
		if (CheckUtils.isIcon(PrefUtils.getString(GlobalConstant.BG_HOME_PAGE,
				""))) {
			mHttpHelp.showImage(iv_basepager_headicon,
					PrefUtils.getString(GlobalConstant.BG_HOME_PAGE, ""));
		}

	}

	private void initTop() {
		getHttpHelp().sendGet(NetworkConfig.getRecIndex(topPage),
				TopBean.class, new MyRequestCallBack<TopBean>() {

					@Override
					public void onSucceed(TopBean bean) {
						iv_home_7.setFocusable(true);
						iv_home_7.setClickable(true);
						iv_home_7.clearAnimation();
						if (bean == null || bean.cont == null) {
							if (topPage == 1) {
								setTopVisible(GlobalConstant.ZERO);
							} else {
								topPage = 2;
								UIUtils.showToast(UIUtils.getContext(), "没有更多了");
							}
							return;
						}
						if ("1".equals(bean.status)) {
							if (topPage == 1) {
								topBean = bean;
							} else {
								topBean.cont.clear();
								topBean.cont.addAll(bean.cont);
							}
							initTopGV();
							topPage++;
						}
					}
				});
	}

	private void setTopVisible(int type) {
		if (type == GlobalConstant.ZERO) {
			ll_fhp_top.setVisibility(View.GONE);
			view_fhp_top.setVisibility(View.GONE);
		} else {
			ll_fhp_top.setVisibility(View.VISIBLE);
			view_fhp_top.setVisibility(View.VISIBLE);
		}
	}

	protected void initTopGV() {
		if (topAdapter == null) {
			topAdapter = new HomeAdapter(topBean.cont);
			topAdapter.setAdata(topBean.cont);
			gv_home_top.setAdapter(topAdapter);
			gv_home_top.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(MyApplication.getMainActivity(),
							PlayActivity.class);
					intent.putExtra(GlobalConstant.PLAY_VIDEO_ID,
							topBean.cont.get(position).id);
					MyApplication.getMainActivity().startActivity(intent);
				}
			});
		} else {
			topAdapter.notifyDataSetChanged();
		}
	}

	private HttpHelp httpHelp;

	private HttpHelp getHttpHelp() {
		if (httpHelp == null) {
			httpHelp = new HttpHelp();
		}
		return httpHelp;
	}

	private class HotListener implements View.OnClickListener {
		private TextView textView;

		private HotListener(TextView textView) {
			this.textView = textView;
		}

		@Override
		public void onClick(View v) {
			String key = textView.getText().toString();
			Intent intent = new Intent(UIUtils.getContext(),
					SearchResultPageMainActivity.class);
			intent.putExtra(GlobalConstant.APP_SEARCH_KEY, key);
			mActivity.startActivity(intent);
			PrefUtils.saveHotKey(key);

		}

	}

	// private MyGridAndListViewBaseAdapter myGridAndListViewBaseAdapter;
	private AnimUtil animUtil;

	/**
	 * 初始化动画
	 */
	private void initAnim() {
		View iv_home_1 = mView.findViewById(R.id.iv_home_1);
		View iv_home_2 = mView.findViewById(R.id.iv_home_2);
		View iv_home_3 = mView.findViewById(R.id.iv_home_3);
		View iv_home_4 = mView.findViewById(R.id.iv_home_4);
		View iv_home_5 = mView.findViewById(R.id.iv_home_5);
		View iv_home_6 = mView.findViewById(R.id.iv_home_6);
		setAnim(iv_home_1, FLAG_SERACH, null);
		setAnim(iv_home_2, FLAG_INDEXREC, indexrecAadpter);
		setAnim(iv_home_3, FLAG_MOVIE, movieAadpter);
		setAnim(iv_home_4, FLAG_TV, tvAadpter);
		setAnim(iv_home_5, FLAG_ANIM, animAadpter);
		setAnim(iv_home_6, FLAG_VEDIE, vedioAadpter);

	}

	/**
	 * 设置动画
	 * 
	 * @param view
	 * @param flag
	 * @param adapter
	 */
	private void setAnim(final View view, final int flag,
			final HomeAdapter adapter) {

		if (animUtil == null) {
			animUtil = new AnimUtil();
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				view.setFocusable(false);
				view.setClickable(false);
				animUtil.startRotateAnimation(view);
				String url = getUrl(flag);
				if (url == null) {
					return;
				}
				mHttpHelp.sendGet(url, TVbean.class,
						new MyRequestCallBack<TVbean>() {

							@Override
							public void onSucceed(TVbean bean) {
								view.setFocusable(true);
								view.setClickable(true);
								view.clearAnimation();
								if (bean == null) {
									view.clearAnimation();
									return;
								}
								getBeanTypeAndUp(flag, bean.cont, adapter);
							}
						});

			}
		});
	}

	/**
	 * 获取URL
	 * 
	 * @param flag
	 * @return
	 */
	protected String getUrl(int flag) {
		String url = mBean.url.dianying + mHttpHelp.FLAG_PAGE + moviePage;
		switch (flag) {
		case FLAG_TV:
			url = mBean.url.dianshi + mHttpHelp.FLAG_PAGE + TVPage;
			break;

		case FLAG_MOVIE:
			url = mBean.url.dianying + mHttpHelp.FLAG_PAGE + moviePage;
			break;
		case FLAG_ANIM:
			url = mBean.url.dongman + mHttpHelp.FLAG_PAGE + AnimPage;
			break;
		case FLAG_VEDIE:
			url = mBean.url.shipin + mHttpHelp.FLAG_PAGE + vediePage;
			break;
		case FLAG_INDEXREC:
			url = mBean.url.indexrec + mHttpHelp.FLAG_PAGE + indexrecPage;
			break;
		case FLAG_SERACH:
			url = mBean.url.hotkeys + mHttpHelp.FLAG_PAGE + serachPage;
			break;
		}
		return url;
	}

	private int moviePage = 2;
	private int TVPage = 2;
	private int AnimPage = 2;
	private int vediePage = 2;
	private int indexrecPage = 2;
	private int serachPage = 2;
	public static final int FLAG_TV = 1;// 电视
	public static final int FLAG_MOVIE = 2;// 电影
	public static final int FLAG_ANIM = 3;// 动画
	public static final int FLAG_VEDIE = 4;// 视频
	public static final int FLAG_INDEXREC = 5;// 推荐
	public static final int FLAG_SERACH = 6;// 推荐
	public static final int FLAG_TOP = 7;// 小视频
	private HomeAdapter indexrecAadpter;// 精彩推荐
	private HomeAdapter vedioAadpter;// 视频
	private HomeAdapter animAadpter;// 动漫
	private HomeAdapter tvAadpter;// 动漫
	private HomeAdapter movieAadpter;
	private ImageView tv_home_hot_icon_1, tv_home_hot_icon_2,
			tv_home_hot_icon_3, tv_home_hot_icon_4, tv_home_hot_icon_5,
			tv_home_hot_icon_6;

	/**
	 * 判断类别和界面更新
	 * 
	 * @param flag
	 * @return
	 */
	private void getBeanTypeAndUp(int flag, List<TV> list, HomeAdapter adapter) {
		if (list == null) {
			UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
			return;
		}
		switch (flag) {
		case FLAG_TV:
			if (list.size() < 2) {
				UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
				TVPage = 0;
				return;
			}
			TVPage++;
			dianshi = list;
			break;
		case FLAG_MOVIE:
			if (list.size() < 2) {
				UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
				moviePage = 0;
				return;
			}
			moviePage++;
			dianying = list;
			break;
		case FLAG_ANIM:
			if (list.size() < 2) {
				UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
				AnimPage = 0;
				return;
			}
			AnimPage++;
			dongman = list;
			break;
		case FLAG_VEDIE:
			if (list.size() < 2) {
				UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
				vediePage = 0;
				return;
			}
			vediePage++;
			shipin = list;
			break;
		case FLAG_INDEXREC:
			if (list.size() < 4) {
				UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
				indexrecPage = 0;
				return;
			}
			indexrecPage++;
			indexrec = list;
			break;
		case FLAG_SERACH:
			if (list.size() < 6) {
				UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
				serachPage = 0;
				return;
			}
			serachPage++;
			break;
		}
		if (adapter == null) {
			mBean.cont.hotkeys = list;
			initHotkeys();
		} else {

			adapter.setAdata(list);
			adapter.notifyDataSetInvalidated();
		}
	}

	private void initNet() {
		if (mHttpHelp == null) {
			mHttpHelp = new HttpHelp();
		}
		mHttpHelp.sendGet(NetworkConfig.getIndex(), HomeBean.class,
				new MyRequestCallBack<HomeBean>() {

					@Override
					public void onSucceed(HomeBean bean) {

						if (bean == null) {
							return;
						}

						LoadingSuccess();

						if (CheckUtils.isIcon(PrefUtils.getString(
								GlobalConstant.BG_HOME_PAGE, ""))) {
							initHeadIcon(bean);
						} else {
							mHttpHelp.showImage(iv_basepager_headicon,
									bean.topbg.pic);
							downloadHeadIcon(bean);
						}
						// downloadHeadIcon(bean);
						mBean = bean;
						initHotkeys();
						allItemAdapter();
						flContent.removeAllViews();
						initAnim();// 初始化动画
						initItemClickListener();
						flContent.addView(mView);
					}
				});
		initTop();
	}

	// 初始化首页标题布局
	private void initHeadIcon(HomeBean bean) {
		String time = PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.HOMEPAGE_TIME, null);
		if (time != null && bean.topbg.time.trim().equals(time.trim())) {
		} else {
			mHttpHelp.showImage(iv_basepager_headicon, bean.topbg.pic);
			PrefUtils.setString(UIUtils.getContext(), GlobalConstant.SKEY,
					bean.topbg.skey);
			downloadHeadIcon(bean);
		}
		tv_basepager_skey.setText(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.SKEY, ""));
	}

	private void downloadHeadIcon(final HomeBean bean) {
		if (CheckUtils.isIcon(PrefUtils.getString(GlobalConstant.BG_HOME_PAGE,
				""))) {
			File f = new File(PrefUtils.getString(GlobalConstant.BG_HOME_PAGE,
					""));
			f.delete();
			PrefUtils.setString(GlobalConstant.BG_HOME_PAGE, "");
		}
		mHttpHelp.downLoad(bean.topbg.pic, GlobalConstant.HEAD_ICON_SAVEPATH
				+ "limaohomeicon2.jpg", new LoadRequestCallBack() {
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSucceed(ResponseInfo t) {
				if (IconUtils.changeBitmap(GlobalConstant.HEAD_ICON_SAVEPATH,
						"limaohomeicon1.jpg", GlobalConstant.HEAD_ICON_SAVEPATH
								+ "limaohomeicon2.jpg")) {
					PrefUtils.setString(GlobalConstant.BG_HOME_PAGE,
							GlobalConstant.HEAD_ICON_SAVEPATH
									+ "limaohomeicon1.jpg");
				}
				PrefUtils.setString(UIUtils.getContext(),
						GlobalConstant.HOMEPAGE_TIME, bean.topbg.time.trim());
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 初始化条目点击监听
	 */

	protected void initItemClickListener() {
		setHomeItemClick(gv_home_anime, FLAG_ANIM);
		setHomeItemClick(gv_home_movie, FLAG_MOVIE);
		setHomeItemClick(gv_home_tv, FLAG_TV);
		setHomeItemClick(gv_home_video, FLAG_VEDIE);
		setHomeItemClick(gv_home_recommend, FLAG_INDEXREC);
	}

	/**
	 * 进入播放页面
	 * 
	 * @param flagTv
	 */
	private void enterPlay(TV tv, int flagTv) {
		if ("0".equals(tv.hasfilm)) {
			UIUtils.showToast(mActivity, UIUtils.getContext().getResources()
					.getString(R.string.state_not_resource));
			return;
		}
		Intent intent = new Intent(mActivity, PlaySourceActivity.class);
		if (flagTv == FLAG_TV) {
			intent.putExtra("IS_TV", true);
		}
		intent.putExtra(GlobalConstant.TV_ID, tv.id);
		PrefUtils.setString(UIUtils.getContext(), "playsourceactivity_id",
				tv.id);
		mActivity.startActivity(intent);
	}

	/**
	 * 设置条目点击事件
	 * 
	 * @param view
	 * @param list
	 */
	private void setHomeItemClick(AbsListView view, final int flag) {
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (flag) {
				case FLAG_TV:
					enterPlay(dianshi.get(position), FLAG_TV);
					break;
				case FLAG_MOVIE:
					enterPlay(dianying.get(position), FLAG_MOVIE);
					break;
				case FLAG_ANIM:
					enterPlay(dongman.get(position), FLAG_ANIM);
					break;
				case FLAG_VEDIE:
					enterPlay(shipin.get(position), FLAG_VEDIE);
					break;
				case FLAG_INDEXREC:
					enterPlay(indexrec.get(position), FLAG_INDEXREC);
					break;
				case FLAG_TOP:
					break;
				}

			}
		});
	}

	/**
	 * 初始化关键字
	 */
	protected void initHotkeys() {

		hotkeys = mBean.cont.hotkeys;
		if (hotkeys == null) {
			return;
		}
		TV tv_1 = hotkeys.get(0) != null ? hotkeys.get(0) : null;
		TV tv_2 = hotkeys.get(1) != null ? hotkeys.get(1) : null;
		TV tv_3 = hotkeys.get(2) != null ? hotkeys.get(2) : null;
		TV tv_4 = hotkeys.get(3) != null ? hotkeys.get(3) : null;
		TV tv_5 = hotkeys.get(4) != null ? hotkeys.get(4) : null;
		TV tv_6 = hotkeys.get(5) != null ? hotkeys.get(5) : null;
		if (tv_1 == null || tv_2 == null || tv_3 == null || tv_3 == null
				|| tv_4 == null || tv_5 == null || tv_6 == null) {
			return;
		}
		tv_home_hot_icon_1.setVisibility("1".equals(tv_1.isnew) ? View.VISIBLE
				: View.GONE);
		tv_home_hot_icon_2.setVisibility("1".equals(tv_2.isnew) ? View.VISIBLE
				: View.GONE);
		tv_home_hot_icon_3.setVisibility("1".equals(tv_3.isnew) ? View.VISIBLE
				: View.GONE);
		tv_home_hot_icon_4.setVisibility("1".equals(tv_4.isnew) ? View.VISIBLE
				: View.GONE);
		tv_home_hot_icon_5.setVisibility("1".equals(tv_5.isnew) ? View.VISIBLE
				: View.GONE);
		tv_home_hot_icon_6.setVisibility("1".equals(tv_6.isnew) ? View.VISIBLE
				: View.GONE);
		tv_home_hot_1.setText(tv_1.name);
		tv_home_hot_2.setText(tv_2.name);
		tv_home_hot_3.setText(tv_3.name);
		tv_home_hot_4.setText(tv_4.name);
		tv_home_hot_5.setText(tv_5.name);
		tv_home_hot_6.setText(tv_6.name);
		// setHomeOnClickListener(tv_home_hot_1, tv_1.name);
		// setHomeOnClickListener(tv_home_hot_2, tv_2.name);
		// setHomeOnClickListener(tv_home_hot_3, tv_3.name);
		// setHomeOnClickListener(tv_home_hot_4, tv_4.name);
		// setHomeOnClickListener(tv_home_hot_5, tv_5.name);
		// setHomeOnClickListener(tv_home_hot_6, tv_6.name);

	}

	/**
	 * 首页所有分类的适配器
	 */

	private void allItemAdapter() {
		// 精彩推荐
		indexrec = mBean.cont.indexrec;
		if (indexrec == null || indexrec.size() <= 0) {
			view_fhp_recommend.setVisibility(View.GONE);
			ll_fhp_recommend.setVisibility(View.GONE);
		} else {
			indexrecAadpter = new HomeAdapter(indexrec);
			indexrecAadpter.setAdata(indexrec);
			gv_home_recommend.setAdapter(indexrecAadpter);
		}

		// 电影
		dianying = mBean.cont.dianying;
		if (dianying == null || dianying.size() <= 0) {
			view_fhp_movies.setVisibility(View.GONE);
			ll_fhp_movies.setVisibility(View.GONE);
		} else {
			movieAadpter = new HomeAdapter(dianying);
			movieAadpter.setAdata(dianying);
			gv_home_movie.setAdapter(movieAadpter);
		}

		// 电视
		dianshi = mBean.cont.dianshi;
		if (dianshi == null || dianshi.size() <= 0) {
			view_fhp_tv.setVisibility(View.GONE);
			ll_fhp_tv.setVisibility(View.GONE);
		} else {
			tvAadpter = new HomeAdapter(dianshi);
			tvAadpter.setAdata(dianshi);
			gv_home_tv.setAdapter(tvAadpter);
		}

		// 动漫
		dongman = mBean.cont.dongman;
		if (dongman == null || dongman.size() <= 0) {
			view_fhp_cartoon.setVisibility(View.GONE);
			ll_fhp_cartoon.setVisibility(View.GONE);
		} else {
			animAadpter = new HomeAdapter(dongman);
			animAadpter.setAdata(dongman);
			gv_home_anime.setAdapter(animAadpter);
		}

		// 视频
		shipin = mBean.cont.shipin;
		if (shipin == null || shipin.size() <= 0) {
			view_fhp_video.setVisibility(View.GONE);
			ll_fhp_video.setVisibility(View.GONE);
		} else {
			vedioAadpter = new HomeAdapter(shipin);
			vedioAadpter.setAdata(shipin);
			gv_home_video.setAdapter(vedioAadpter);
		}

	}

	@Override
	public String getCurrentTitle() {
		return null;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus == true) {

			et_home_find.clearFocus();
			mActivity
					.startActivity(new Intent(mActivity, SearchActivity.class));
		}

	}

}
