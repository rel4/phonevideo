package com.limaoso.phonevideo.base.impl.hometitlepager;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.adapter.MyGridAndListViewBaseAdapter;
import com.limaoso.phonevideo.base.BasePager;
import com.limaoso.phonevideo.holder.AllViewholder;
import com.limaoso.phonevideo.utils.UIUtils;
/**
 * 首页标题-精选
 * %%%%%%%%%%%%%%%%%%%%%%
 * 去除精选界面
 * %%%%%%%%%%%%%%%%%%%%%%%%
 * @author liang
 *
 */
public class FeaturedPager extends BasePager implements OnCheckedChangeListener  {
	


	public FeaturedPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void initData() {
		super.initData();
		setSlidingMenuEnable(false);
		LoadingSuccess();
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getCurrentTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}










