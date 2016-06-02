package com.limaoso.phonevideo.activity.menu;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.BaseActivity;
import com.limaoso.phonevideo.utils.UIUtils;


/**
 * 菜单详情页-热门活动
 * 
 * @author Kevin
 * 
 */
public class VIPMenuDetailActivity extends BaseActivity {

	

	@Override
	public void initView() {
		
		
	}

	@Override
	public String getCurrentTitle() {
		return "热门活动";
	}
	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	@Override
	protected View getRootView() {
		
		return UIUtils.inflate(R.layout.activity_vip_menudetail);
	}



}
