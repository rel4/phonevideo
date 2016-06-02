package com.limaoso.phonevideo.base.impl.hometitlepager;

import android.app.Activity;

import com.limaoso.phonevideo.base.BasePager;
/**
 * 首页标题-更多
 * ###################
 *	去除更多界面
 *####################
 * @author liang
 *
 */
public class MorePager extends BasePager {

	public MorePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void initData() {
		super.initData();
		setSlidingMenuEnable(true);
		LoadingSuccess();
	}
	@Override
	public String getCurrentTitle() {
		// TODO Auto-generated method stub
		return "更多";
	}

}
