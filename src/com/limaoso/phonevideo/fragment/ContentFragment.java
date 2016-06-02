package com.limaoso.phonevideo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.MainActivity;
import com.limaoso.phonevideo.base.BasePager;
import com.limaoso.phonevideo.base.impl.hometitlepager.FeaturedPager;
import com.limaoso.phonevideo.base.impl.hometitlepager.MorePager;
import com.limaoso.phonevideo.base.impl.menudetail.ChannelMenuDetailPager;
import com.limaoso.phonevideo.base.impl.menudetail.IntegralMallMenuDetailPager;
import com.limaoso.phonevideo.base.impl.pager.ChannelPager;
import com.limaoso.phonevideo.base.impl.pager.FindPager;
import com.limaoso.phonevideo.base.impl.pager.HomePager;
import com.limaoso.phonevideo.base.impl.pager.MySpacePager;

/**
 * 主页内容
 * 
 * @author liang
 * 
 */
public class ContentFragment extends BaseFragment implements
		OnCheckedChangeListener, OnPageChangeListener {

	public ViewPager vpContent;
	public RadioGroup rg_Group;

	List<BasePager> mPagerList; // 所有子页面的集合

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		vpContent = (ViewPager) view.findViewById(R.id.vp_content);
		rg_Group = (RadioGroup) view.findViewById(R.id.rg_group);

		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		return view;
	}

	@Override
	public void initData() {

		LoadingPager();

		vpContent.setAdapter(new ContentAdapter());

		// 监听viewpager页面改变的事件
		vpContent.setOnPageChangeListener(this);

	}

	/**
	 * 加载数据
	 */
	private void LoadingPager() {
		rg_Group.check(R.id.rb_home);// 默认勾选首页
		// 初始化子页面
		initChildPager();

		// 监听RadioGroup的选择事件
		rg_Group.setOnCheckedChangeListener(this);
		mPagerList.get(0).initData();// 初始化首页数据
	}

	private void initChildPager() {
		mPagerList = new ArrayList<BasePager>();
		/**
		 * 首页按钮组
		 */
		mPagerList.add(new HomePager(mActivity));// 1
		mPagerList.add(new ChannelPager(mActivity));// 2
		mPagerList.add(new FindPager(mActivity));// 3
		mPagerList.add(new MySpacePager(mActivity));// 4

		/**
		 * 初始化左侧边栏的子页面
		 */
		// mPagerList.add(new NetworkMenuDetailPager(mActivity));// 5sss
		mPagerList.add(new ChannelMenuDetailPager(mActivity));// 6
		// mPagerList.add(new InformMenuDetailPager(mActivity));// 7
		// mPagerList.add(new RecommendMenuDetailPager(mActivity));// 8
		// mPagerList.add(new VIPMenuDetailPager(mActivity));// 9
		mPagerList.add(new IntegralMallMenuDetailPager(mActivity));// 10

		/**
		 * 初始化首页标题的子页面
		 */
		mPagerList.add(new FeaturedPager(mActivity));// 11
		// mPagerList.add(new MoviePager(mActivity));// 12
		// mPagerList.add(new TVplayPager(mActivity));// 13
		// mPagerList.add(new AnimePager(mActivity));// 14
		// mPagerList.add(new VideoPager(mActivity));// 15
		mPagerList.add(new MorePager(mActivity));// 16

	}

	// 对外提供一个获取侧边栏RadioGroup的方法
	public RadioGroup getRadio_Group() {
		return rg_Group;
	}

	/**
	 * 设置左菜单栏点击的界面
	 * 
	 * @param position
	 */
	public void setCurrentMenuDetailPager(int position) {
		vpContent.setCurrentItem(position, false);// 去掉切换页面的动画

	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagerList.get(position);
			container.addView(pager.mRootView);
			return pager.mRootView;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);

		}

	}

	/**
	 * 监听底部按钮组的点击事件
	 */

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.rb_home:
			// mViewPager.setCurrentItem(0);// 设置当前页面
			vpContent.setCurrentItem(0, false);// 去掉切换页面的动画
			break;
		case R.id.rb_channel:
			vpContent.setCurrentItem(1, false);// 设置当前页面
			break;
		case R.id.rb_find:
			vpContent.setCurrentItem(2, false);// 设置当前页面
			break;
		case R.id.rb_oneself:
			vpContent.setCurrentItem(3, false);// 设置当前页面
			break;
		default:
			break;
		}

	}

	/**
	 * 监听viewpager页面改变的事件
	 */
	@Override
	public void onPageSelected(int arg0) {
		// 获取当前被选中的页面, 初始化该页面数据
		mPagerList.get(arg0).initData();

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

}
