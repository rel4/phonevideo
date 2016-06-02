package com.limaoso.phonevideo.base.impl.pager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.SearchActivity;
import com.limaoso.phonevideo.activity.home.AnimeActivity;
import com.limaoso.phonevideo.activity.home.MovieActivity;
import com.limaoso.phonevideo.activity.home.TVplayActivity;
import com.limaoso.phonevideo.activity.home.VideoActivity;
import com.limaoso.phonevideo.activity.menu.NetworkMenuDetailActivity;
import com.limaoso.phonevideo.activity.menu.RecommendMenuDetailActivity;
import com.limaoso.phonevideo.base.BasePager;
import com.limaoso.phonevideo.global.MyApplication;
import com.limaoso.phonevideo.top.TopActivity;

/**
 * 频道页实现
 * 
 * @author liang
 * 
 */
public class ChannelPager extends BasePager implements OnFocusChangeListener {

	// private LinearLayout mRl_channel_btn_movie; // 电影
	// private LinearLayout mRl_channel_btn_tv; // 电视
	// private LinearLayout mRl_channel_btn_anime; // 动漫
	// private LinearLayout mRl_channel_btn_video; // 视频
	private MyOnClickListener myOnClickListener;

	public ChannelPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		// 显示频道页标题
		isOtherPager(3);
		setSlidingMenuEnable(true);
		LoadingSuccess();
		flContent.removeAllViews();
		View view = View.inflate(MyApplication.getContext(),
				R.layout.channel_videopage, null);
		myOnClickListener = new MyOnClickListener();
		// 设置4个按钮的监听事件
		view.findViewById(R.id.rl_channel_btn_movie).setOnClickListener(
				myOnClickListener);
		view.findViewById(R.id.rl_channel_btn_tv).setOnClickListener(
				myOnClickListener);
		view.findViewById(R.id.rl_channel_btn_anime).setOnClickListener(
				myOnClickListener);
		view.findViewById(R.id.rl_channel_btn_video).setOnClickListener(
				myOnClickListener);
		view.findViewById(R.id.rl_channel_btn_network).setOnClickListener(
				myOnClickListener);
		view.findViewById(R.id.rl_channel_btn_recommend).setOnClickListener(
				new MyOnClickListener());
		view.findViewById(R.id.rl_channel_btn_top).setOnClickListener(
				myOnClickListener);
		et_channel_find.setOnFocusChangeListener(this);
		// 向FrameLayout中动态添加布局
		flContent.addView(view);
	}

	@Override
	public String getCurrentTitle() {
		return "频道页面";
	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {

			case R.id.rl_channel_btn_movie:
				intent = new Intent(mActivity, MovieActivity.class);
				break;
			case R.id.rl_channel_btn_tv:
				intent = new Intent(mActivity, TVplayActivity.class);
				break;
			case R.id.rl_channel_btn_anime:
				intent = new Intent(mActivity, AnimeActivity.class);
				break;
			case R.id.rl_channel_btn_video:
				intent = new Intent(mActivity, VideoActivity.class);
				break;
			case R.id.rl_channel_btn_network:
				intent = new Intent(mActivity, NetworkMenuDetailActivity.class);
				break;
			case R.id.rl_channel_btn_recommend:
				intent = new Intent(mActivity,
						RecommendMenuDetailActivity.class);
				break;
			case R.id.rl_channel_btn_top:
				intent = new Intent(mActivity, TopActivity.class);
				break;
			}
			if (intent != null) {
				mActivity.startActivity(intent);
			}
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus == true) {

			et_channel_find.clearFocus();
			mActivity
					.startActivity(new Intent(mActivity, SearchActivity.class));
		}

	}
}
