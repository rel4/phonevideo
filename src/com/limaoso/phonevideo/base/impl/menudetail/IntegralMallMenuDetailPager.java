package com.limaoso.phonevideo.base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.limaoso.phonevideo.base.BasePager;


/**
 * 菜单详情页-积分商城
 * 此页面临时隐藏，做保留处理
 * @author Kevin
 * 
 */
public class IntegralMallMenuDetailPager extends BasePager {

	public IntegralMallMenuDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		
		isOtherPager(3);
		setSlidingMenuEnable(true);
		
		/*此页面临时隐藏，做保留处理*/
		LoadingSuccess();
		
		TextView text = new TextView(mActivity);
		text.setText("菜单详情页-积分商城");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		flContent.addView(text);
	}

	@Override
	public String getCurrentTitle() {
		return "积分商城";
	}

}
