package com.limaoso.phonevideo.activity.home;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.BaseActivity;
import com.limaoso.phonevideo.base.BasePager;
import com.limaoso.phonevideo.base.impl.hometitlepager.base.ShowPage;
import com.limaoso.phonevideo.utils.UIUtils;

/**
 * 首页标题-动漫
 * 
 * @author liang
 * 
 */
public class AnimeActivity extends BaseActivity implements
		OnCheckedChangeListener {

	public static final String FEATURED_TV = ""; // 精彩
	public static final String DALU_TV = "&area=dalu"; // 大陆
	public static final String GANGTAI_TV = "&area=gangtai"; // 港台
	public static final String RIHAN_TV = "&area=rihan"; // 日韩
	public static final String OUMEI_TV = "&area=oumei"; // 欧美

	private RadioGroup rg_home_title_all_pager; // 获取按钮组的空件
	private FrameLayout fl_home_content_all_pager; // 获取变化内容的控件
	private ProgressBar pb_home_content_all_progress; // 获取变化内容的控件

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.home_title_all_pager);
	}

	@Override
	public void initView() {

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
		return "动漫";
	}

	/**
	 * 监听按钮组点击的状态
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_home_title_all_featured: // 精选
			childPageAddContent(ShowPage.ANIM, ShowPage.FEATURED, FEATURED_TV,
					false); // 给精彩页面添加内容
			break;
		case R.id.rb_home_title_all_china: // 大陆
			childPageAddContent(ShowPage.ANIM, ShowPage.DALU, DALU_TV, false); // 给大陆页面添加内容
			break;
		case R.id.rb_home_title_all_tvb: // 港台
			childPageAddContent(ShowPage.ANIM, ShowPage.GANGTAI, GANGTAI_TV,
					false); // 给港台页面添加内容
			break;
		case R.id.rb_home_title_all_jandk: // 日韩
			childPageAddContent(ShowPage.ANIM, ShowPage.RIHAN, RIHAN_TV, false); // 给日韩页面添加内容
			break;
		case R.id.rb_home_title_all_america: // 美剧
			childPageAddContent(ShowPage.ANIM, ShowPage.OUMEI, OUMEI_TV, false); // 给欧美页面添加内容
			break;
		case R.id.rb_home_title_all_more: // 更多
			childPageAddContent(ShowPage.ANIM, ShowPage.MORE, null, true); // 给更多页面添加内容

			break;

		default:
			break;
		}
	}

	/**
	 * 
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
