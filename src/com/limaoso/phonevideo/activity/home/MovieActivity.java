package com.limaoso.phonevideo.activity.home;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.BaseActivity;
import com.limaoso.phonevideo.base.BasePager;
import com.limaoso.phonevideo.base.impl.hometitlepager.base.ShowPage;
import com.limaoso.phonevideo.utils.UIUtils;

/**
 * 首页标题-电影
 * 
 * @author liang
 * 
 */
public class MovieActivity extends BaseActivity implements
		OnCheckedChangeListener {

	public static final String XIJU_TV = "&ctype=xiju"; // 喜剧
	public static final String ZHANZHENG_TV = "&ctype=zhanzheng"; // 战争
	public static final String KEHUAN_TV = "&ctype=kehuan"; // 科幻
	public static final String DONGZUO_TV = "&ctype=dongzuo"; // 动作
	public static final String KONGBU_TV = "&ctype=kongbu"; // 恐怖

	private RadioGroup rg_home_title_all_pager; // 获取按钮组的空件
	private FrameLayout fl_home_content_all_pager; // 获取变化内容的控件
	private ProgressBar pb_home_content_all_progress; // 获取变化内容的控件

	private RadioButton mRb_home_title_all_america;
	private RadioButton mRb_home_title_all_china;
	private RadioButton mRb_home_title_all_featured;
	private RadioButton mRb_home_title_all_jandk;
	private RadioButton mRb_home_title_all_tvb;

	@Override
	protected View getRootView() {
		// TODO Auto-generated method stub
		return UIUtils.inflate(R.layout.home_title_all_pager);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	@Override
	protected void initView() {
		mRb_home_title_all_america = (RadioButton) rootView
				.findViewById(R.id.rb_home_title_all_america);
		mRb_home_title_all_china = (RadioButton) rootView
				.findViewById(R.id.rb_home_title_all_china);
		mRb_home_title_all_featured = (RadioButton) rootView
				.findViewById(R.id.rb_home_title_all_featured);
		mRb_home_title_all_jandk = (RadioButton) rootView
				.findViewById(R.id.rb_home_title_all_jandk);
		mRb_home_title_all_tvb = (RadioButton) rootView
				.findViewById(R.id.rb_home_title_all_tvb);

		setTitleNames();
		// 按钮组空间
		rg_home_title_all_pager = (RadioGroup) rootView
				.findViewById(R.id.rg_home_title_all_pager);
		rg_home_title_all_pager.setOnCheckedChangeListener(this);

		// 内容空件
		fl_home_content_all_pager = (FrameLayout) rootView
				.findViewById(R.id.fl_home_content_all_pager);
		pb_home_content_all_progress = (ProgressBar) rootView
				.findViewById(R.id.pb_home_content_all_progress);

		rg_home_title_all_pager.check(R.id.rb_home_title_all_featured); // 设置默认点击
		// fl_home_content_all_pager.setVisibility(View.INVISIBLE);
		// pb_home_content_all_progress.setVisibility(View.VISIBLE);

	}

	/**
	 * 设置标题的名字 （后期需求修改，将原来的名字替换现在的）
	 */
	private void setTitleNames() {
		mRb_home_title_all_featured.setText("喜剧");
		mRb_home_title_all_china.setText("战争");
		mRb_home_title_all_tvb.setText("科幻");
		mRb_home_title_all_jandk.setText("动作");
		mRb_home_title_all_america.setText("恐怖");

	}

	@Override
	public String getCurrentTitle() {
		return "电影";
	}

	/**
	 * 监听按钮组点击的状态
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_home_title_all_featured: // 喜剧
			childPageAddContent(ShowPage.MOVIE, ShowPage.FEATURED, XIJU_TV,
					false); // 给喜剧页面添加内容
			break;
		case R.id.rb_home_title_all_china: // 战争
			childPageAddContent(ShowPage.MOVIE, ShowPage.DALU, ZHANZHENG_TV,
					false); // 给战争页面添加内容
			break;
		case R.id.rb_home_title_all_tvb: // 科幻
			childPageAddContent(ShowPage.MOVIE, ShowPage.GANGTAI, KEHUAN_TV,
					false); // 给科幻页面添加内容
			break;
		case R.id.rb_home_title_all_jandk: // 动作
			childPageAddContent(ShowPage.MOVIE, ShowPage.RIHAN, DONGZUO_TV,
					false); // 给动作页面添加内容
			break;
		case R.id.rb_home_title_all_america: // 恐怖
			childPageAddContent(ShowPage.MOVIE, ShowPage.OUMEI, KONGBU_TV,
					false); // 给恐怖页面添加内容
			break;
		case R.id.rb_home_title_all_more: // 更多
			childPageAddContent(ShowPage.MOVIE, ShowPage.MORE, null, true); // 给更多页面添加内容
		default:
			break;
		}
	}

	/**
	 * 给精彩子页面添加内容
	 * 
	 * @param b
	 */
	private void childPageAddContent(final String dtype, int currentChild,
			String type, final boolean b) {

		final String URL = ShowPage.getURL(dtype, currentChild, type);
		new ShowPage() {

			@Override
			public FrameLayout getview_fl() {
				return fl_home_content_all_pager;
			}

			@Override
			public ProgressBar getview_pb() {
				return pb_home_content_all_progress;
			}

			@Override
			public Activity getmActivity() {
				return mActivity;
			}

			@Override
			public String LoadingURL() {
				return URL;
			}

			@Override
			public boolean isMoreChildPage() {
				return b;
			}

			@Override
			public String getDtype() {
				return dtype;
			}
		};
	}

}
