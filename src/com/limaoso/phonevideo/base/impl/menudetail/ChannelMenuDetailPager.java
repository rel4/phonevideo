package com.limaoso.phonevideo.base.impl.menudetail;

import android.app.Activity;
import android.view.View;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.base.BasePager;
import com.limaoso.phonevideo.global.MyApplication;



/**
 * 菜单详情页-频道
 * 此页面不要写入任何数据，暂时废除！左侧菜单栏频道条目内容和底部频道相同
 * @author Kevin
 * 
 */
public class ChannelMenuDetailPager extends BasePager {


	public ChannelMenuDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		
		setSlidingMenuEnable(true);
		isOtherPager(3);
		LoadingSuccess();
		/*TextView text = new TextView(mActivity);
		text.setText("菜单详情页-频道");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);*/
		/**
		 * 此页面不要写入任何数据，暂时废除！左侧菜单栏频道条目内容和底部频道相同
		 */
	/*	flContent.removeAllViews();
		View view = View.inflate(MyApplication.getContext(), R.layout.channel_videopage, null);
	
		flContent.addView(view);*/
	}

	@Override
	public String getCurrentTitle() {
		return "精选频道";
	}



	

}
