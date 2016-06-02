package com.limaoso.phonevideo.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.adapter.HomeAdapter;
import com.limaoso.phonevideo.adapter.MyGridAndListViewBaseAdapter;
import com.limaoso.phonevideo.bean.HomeBean.Cont.TV;
import com.limaoso.phonevideo.bean.SearchBaen;
import com.limaoso.phonevideo.bean.TVbean;
import com.limaoso.phonevideo.bean.SearchBaen.Cont;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.AnimUtil;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.FlowLayout;
import com.limaoso.phonevideo.view.RoundedImageView;
import com.renn.rennsdk.oauth.RRException;

/**
 * 查找页面
 * 
 * @author liang
 * 
 */
public class SearchActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {
	private Button btn_search_pager_cancel;
	private GridView gv_search_app;
	private ArrayList<String> mAppNames; // 游戏应用的名字
	private ArrayList<Integer> mAppIcons; // 游戏应用的图片
	private TextView tv_home_hot_1, tv_home_hot_2, tv_home_hot_3,
			tv_home_hot_4, tv_home_hot_5, tv_home_hot_6, tv_del_all;
	private MyGridAndListViewBaseAdapter mAdapter;
	private SearchBaen mBean;// 搜索数据
	private HttpHelp mHelp;
	private AnimUtil animUtil;
	public static final int FLAG_HOTKEY = 1;
	public static final int FLAG_APP = 2;

	@Override
	protected View getRootView() {
		// TODO Auto-generated method stub
		return UIUtils.inflate(R.layout.activity_search);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setSearchData();
	}

	private class ViewHolder {
		RoundedImageView ivHome;
		TextView tvHome;
	}

	/**
	 * 初始视图
	 */
	protected void initView() {
		btn_search_pager_cancel = (Button) findViewById(R.id.btn_search_pager_cancel);
		gv_search_app = (GridView) findViewById(R.id.gv_search_app);
		tv_del_all = (TextView) findViewById(R.id.tv_del_all);
		tv_home_hot_1 = (TextView) findViewById(R.id.tv_home_hot_1);
		tv_home_hot_2 = (TextView) findViewById(R.id.tv_home_hot_2);
		tv_home_hot_3 = (TextView) findViewById(R.id.tv_home_hot_3);
		tv_home_hot_4 = (TextView) findViewById(R.id.tv_home_hot_4);
		tv_home_hot_5 = (TextView) findViewById(R.id.tv_home_hot_5);
		tv_home_hot_6 = (TextView) findViewById(R.id.tv_home_hot_6);
		et_search_pager_find = (EditText) findViewById(R.id.et_channel_find);
		addTextChangedListener(et_search_pager_find);
		et_search_pager_find.setHint(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.SKEY, ""));
		tv_del_all.setOnClickListener(this);
		show_del_layout = findViewById(R.id.show_del_layout);
		iv_home_1 = findViewById(R.id.iv_home_1);
		fl_erach_cache = (FlowLayout) findViewById(R.id.fl_erach_cache);
		iv_app_refrsuh = findViewById(R.id.iv_app_refrsuh);
		setAnim(iv_home_1, FLAG_HOTKEY, null);

	}

	/**
	 * 输入监听
	 * 
	 * @param et_search_pager_find2
	 */
	private boolean isSearch = false; // 是否搜索

	private void addTextChangedListener(final EditText edittext) {
		edittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String enter_key = edittext.getText().toString();
				if (enter_key.length() == 0) {
					isSearch = false;
					btn_search_pager_cancel.setText("取消");
				} else {
					btn_search_pager_cancel.setText("搜索");
					isSearch = true;
				}
			}
		});

	}

	private void setSearchData() {
		String key = PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.SERACH_CACHE_KEY, "");
		if (key == null || key == "") {
			show_del_layout.setVisibility(View.GONE);
			return;
		}
		show_del_layout.setVisibility(View.VISIBLE);
		mSearchKeys = key.split(GlobalConstant.FLAG_APP_SPLIT);

		LayoutInflater mInflater = LayoutInflater.from(this);
		for (int i = 0; i < mSearchKeys.length; i++) {
			final int pos = i;
			TextView tv = (TextView) mInflater.inflate(
					R.layout.activity_search_tv, fl_erach_cache, false);
			tv.setText(mSearchKeys[i]);
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goSearchResultPage(pos);
				}

			});
			fl_erach_cache.addView(tv);
		}
	}

	/**
	 * 点击标签跳到相应的视频处
	 * 
	 * @param i
	 */
	private void goSearchResultPage(int i) {
		if (mSearchKeys != null && mSearchKeys[i] != null) {
			Intent intent = new Intent(SearchActivity.this,
					SearchResultPageMainActivity.class);
			intent.putExtra(GlobalConstant.APP_SEARCH_KEY, mSearchKeys[i]);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 设置动画
	 * 
	 * @param view
	 * @param flag
	 * @param adapter
	 */
	private void setAnim(final View view, final int flag,
			final BaseAdapter adapter) {

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
					// UIUtils.showToast(UIUtils.getContext(), "路径错误");
					return;
				}
				if (mHelp == null) {
					mHelp = new HttpHelp();
				}
				mHelp.sendGet(url, TVbean.class,
						new MyRequestCallBack<TVbean>() {

							@Override
							public void onSucceed(TVbean bean) {
								view.setFocusable(true);
								view.setClickable(true);
								getBeanTypeAndUp(flag, bean.cont, adapter);
								view.clearAnimation();
							}
						});

			}
		});
	}

	/**
	 * 判断类别和界面更新
	 * 
	 * @param flag
	 * @return
	 */
	private void getBeanTypeAndUp(int flag, List<TV> list, BaseAdapter adapter) {
		if (list == null || list.size() < 6) {
			UIUtils.showToast(UIUtils.getContext(), "没有更多数据");
			return;
		}
		if (mBean == null || mBean.cont == null) {
			return;
		}
		switch (flag) {
		case FLAG_HOTKEY:
			hotKeysPage++;
			mBean.cont.hotkeys = list;
			break;

		case FLAG_APP:
			faxianPage++;
			mBean.cont.faxian = list;
			break;
		}
		if (adapter == null) {
			if (mBean != null && mBean.cont != null
					&& mBean.cont.hotkeys != null) {
				initHotkeys();
			}
		} else {

			adapter.notifyDataSetInvalidated();
		}
	}

	/**
	 * 获取URL
	 * 
	 * @param flag
	 * @return
	 */
	private int hotKeysPage = 2;
	private int faxianPage = 2;
	private View iv_home_1, show_del_layout;
	private View iv_app_refrsuh;
	private FlowLayout fl_erach_cache;
	private String[] mSearchKeys;
	private EditText et_search_pager_find;

	protected String getUrl(int flag) {
		String url = null;

		if (mBean == null || mBean.url == null) {
			return url;
		}
		switch (flag) {
		case FLAG_HOTKEY:
			if (mBean.url.hotkeys == null) {
				return url;
			}
			url = mBean.url.hotkeys + HttpHelp.FLAG_PAGE + hotKeysPage;
			break;

		case FLAG_APP:
			if (mBean.url.faxian == null) {
				return url;
			}
			url = mBean.url.faxian + HttpHelp.FLAG_PAGE + faxianPage;
			break;

		}
		return url;
	}

	/**
	 * 请求网络
	 */
	protected void initNet() {
		if (mHelp == null) {
			mHelp = new HttpHelp();
		}
		mHelp.sendGet(NetworkConfig.getSearchAll(), SearchBaen.class,
				new MyRequestCallBack<SearchBaen>() {

					@Override
					public void onSucceed(SearchBaen bean) {
						// if (mBean==null) {
						// if (rootView!=null) {
						// rootView.setClickable(false);
						// rootView.setFocusable(false);
						// }
						// return;
						// }
						mBean = bean;
						setAdapter();
						initHotkeys();
					}
				});
	}

	/**
	 * 初始化关键字
	 */
	protected void initHotkeys() {
		if (mBean == null || mBean.cont == null || mBean.cont.hotkeys == null) {
			return;
		}
		List<TV> tv = mBean.cont.hotkeys;
		tv_home_hot_1.setText(tv.get(0).name);
		tv_home_hot_2.setText(tv.get(1).name);
		tv_home_hot_3.setText(tv.get(2).name);
		tv_home_hot_4.setText(tv.get(3).name);
		tv_home_hot_5.setText(tv.get(4).name);
		tv_home_hot_6.setText(tv.get(5).name);
		setHomeOnClickListener(tv_home_hot_1, tv.get(0).name);
		setHomeOnClickListener(tv_home_hot_2, tv.get(1).name);
		setHomeOnClickListener(tv_home_hot_3, tv.get(2).name);
		setHomeOnClickListener(tv_home_hot_4, tv.get(3).name);
		setHomeOnClickListener(tv_home_hot_5, tv.get(4).name);
		setHomeOnClickListener(tv_home_hot_6, tv.get(5).name);
	}

	private void setAdapter() {
		if (mBean == null || mBean.cont == null || mBean.cont.faxian == null) {
			return;
		}
		if (mAdapter == null) {

			mAdapter = new MyGridAndListViewBaseAdapter(mBean.cont.faxian,
					false) {

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					ViewHolder holder;
					if (convertView == null) {
						holder = new ViewHolder();
						convertView = View.inflate(getApplicationContext(),
								R.layout.list_item_search, null);
						holder.ivHome = (RoundedImageView) convertView
								.findViewById(R.id.iv_home);
						holder.tvHome = (TextView) convertView
								.findViewById(R.id.tv_home);
						convertView.setTag(holder);
					} else {
						holder = (ViewHolder) convertView.getTag();
					}
					TV tv = mBean.cont.faxian.get(position);
					holder.tvHome.setText(tv.name);
					if (mHelp == null) {
						mHelp = new HttpHelp();
					}
					mHelp.showImage(holder.ivHome, tv.pic);
					return convertView;
				}
			};
			setAnim(iv_app_refrsuh, FLAG_APP, mAdapter);
			gv_search_app.setAdapter(mAdapter);
			gv_search_app.setOnItemClickListener(this);
			btn_search_pager_cancel.setOnClickListener(this);
			// 去除gv点击的背景色
			gv_search_app.setSelector(new ColorDrawable(Color.TRANSPARENT));
		} else {
			mAdapter.notifyDataSetInvalidated();
		}
	}

	/**
	 * 点击事件
	 * 
	 * @param view
	 * @param key
	 */
	private void setHomeOnClickListener(View view, final String key) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UIUtils.showToast(UIUtils.getContext(), key);
				Intent intent = new Intent(SearchActivity.this,
						SearchResultPageMainActivity.class);
				intent.putExtra(GlobalConstant.APP_SEARCH_KEY, key);
				startActivity(intent);
				// PrefUtils.saveHotKey(key);
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search_pager_cancel:
			if (isSearch) {
				String string = et_search_pager_find.getText().toString();

				Intent intent = new Intent(this,
						SearchResultPageMainActivity.class);
				intent.putExtra(GlobalConstant.APP_SEARCH_KEY, string);
				startActivity(intent);
				// PrefUtils.saveHotKey(string);
			}
			setExitSwichLayout();
			finish();
			break;
		case R.id.tv_del_all:// 清除记录
			PrefUtils.setString(UIUtils.getContext(),
					GlobalConstant.SERACH_CACHE_KEY, "");
			setSearchData();
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		// case R.id.gv_serach_cache:
		// Intent intent = new Intent(this, SearchResultPageMainActivity.class);
		// intent.putExtra(GlobalConstant.APP_SEARCH_KEY,
		// mSearchKeys[position]);
		// startActivity(intent);
		//
		// finish();
		// // PrefUtils.saveHotKey(mSearchKeys[position]);
		// break;
		case R.id.gv_search_app:
			Intent intent = new Intent(SearchActivity.this,
					APPDetailActivity.class);
			intent.putExtra(GlobalConstant.DL_APP_ID,
					mBean.cont.faxian.get(position).id);
			startActivity(intent);
			break;

		}

	}
}
