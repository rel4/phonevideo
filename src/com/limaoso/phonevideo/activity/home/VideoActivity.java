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
 * 首页标题-视频
 * 
 * @author liang
 * 
 */
public class VideoActivity extends BaseActivity implements
		OnCheckedChangeListener {

	public static final String YUANCHUANG_TV = "&ctype=yuanchuang"; // 原创
	public static final String GAOXIAO_TV = "&ctype=gaoxiao"; // 搞笑
	public static final String XINWEN_TV = "&ctype=xinwen"; // 新闻
	public static final String ZIPAI_TV = "&ctype=zipai"; // 自拍
	public static final String JUNSHI_TV = "&ctype=junshi"; // 军事

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
		return UIUtils.inflate(R.layout.home_title_all_pager);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	@Override
	public void initView() {
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

	@Override
	public String getCurrentTitle() {
		return "视频";
	}

	/**
	 * 设置标题的名字 （后期需求修改，将原来的名字替换现在的）
	 */
	private void setTitleNames() {
		mRb_home_title_all_featured.setText("原创");
		mRb_home_title_all_china.setText("搞笑");
		mRb_home_title_all_tvb.setText("新闻");
		mRb_home_title_all_jandk.setText("自拍");
		mRb_home_title_all_america.setText("军事");

	}

	/**
	 * 监听按钮组点击的状态
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_home_title_all_featured: // 原创
			childPageAddContent(ShowPage.VIDEO, ShowPage.FEATURED,
					YUANCHUANG_TV, false); // 给原创添加内容

			break;
		case R.id.rb_home_title_all_china: // 搞笑
			childPageAddContent(ShowPage.VIDEO, ShowPage.FEATURED, GAOXIAO_TV,
					false); // 给搞笑添加内容
			break;
		case R.id.rb_home_title_all_tvb: // 新闻
			childPageAddContent(ShowPage.VIDEO, ShowPage.FEATURED, XINWEN_TV,
					false); // 给新闻页面添加内容
			break;
		case R.id.rb_home_title_all_jandk: // 自拍
			childPageAddContent(ShowPage.VIDEO, ShowPage.FEATURED, ZIPAI_TV,
					false); // 给自拍页面添加内容
			break;
		case R.id.rb_home_title_all_america: // 军事
			childPageAddContent(ShowPage.VIDEO, ShowPage.FEATURED, JUNSHI_TV,
					false); // 给军事页面添加内容
			break;
		case R.id.rb_home_title_all_more: // 更多
			childPageAddContent(ShowPage.VIDEO, ShowPage.MORE, null, true); // 给更多页面添加内容

		default:
			break;
		}
	}

	/**
	 * @param dtype
	 *            频道页的四大分类
	 * @param currentChild
	 *            每个分类下的子标签
	 * @param b
	 *            判断当前是否是“更多子标签”
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
