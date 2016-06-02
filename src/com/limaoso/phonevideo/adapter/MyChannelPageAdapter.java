package com.limaoso.phonevideo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.PlaySourceActivity;
import com.limaoso.phonevideo.bean.HomeBean.Cont.TV;
import com.limaoso.phonevideo.bean.TVbean;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.global.MyApplication;
import com.limaoso.phonevideo.holder.AllViewholder;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.utils.UIUtils;

/**
 * 频道页面的适配器
 * 
 * @author liang
 * 
 */
public class MyChannelPageAdapter extends MyGridAndListViewBaseAdapter {
	private HttpHelp mHelp;
	private List<String> mNames;
	private List<String> mIntroduces;
	private List<String> mIcons;
	private Activity mActivity;
	private List<String> mIds;
	private List<String> mHasfilm;
	private ArrayList<String> mBF_num;
	private ArrayList<String> mPl_num;
	private ArrayList<String> mC_time;

	public void setAdata(List<String> names, List<String> introduces,
			List<String> icons, List<String> ids, List<String> hasfilm,
			ArrayList<String> bf_num, ArrayList<String> pl_num, ArrayList<String> ctime) {
		this.mNames = names;
		this.mBF_num = bf_num;
		this.mPl_num = pl_num;
		this.mC_time = ctime;
		this.mIntroduces = introduces;
		this.mIcons = icons;
		this.mIds = ids;
		this.mHasfilm = hasfilm;
	}

	private int type = 0;

	public MyChannelPageAdapter(List list, boolean b, Activity mActivity,
			int type) {
		super(list, b);
		this.type = type;
		mHelp = new HttpHelp();
		this.mActivity = mActivity;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		AllViewholder homeChildPagerViewholder = null;
		if (convertView == null) {
			convertView = UIUtils.inflate(R.layout.item_grid_recommend);
			homeChildPagerViewholder = new AllViewholder();

			homeChildPagerViewholder.movieIcon1 = (ImageView) convertView
					.findViewById(R.id.iv_item_recommend_icon_1);
			homeChildPagerViewholder.movieName1 = (TextView) convertView
					.findViewById(R.id.tv_item_recommend_title_1);
			homeChildPagerViewholder.movieIntroduce1 = (TextView) convertView
					.findViewById(R.id.tv_item_recommend_introduce_1);
			homeChildPagerViewholder.tv_igr_show_1 = (TextView) convertView
					.findViewById(R.id.tv_igr_show_1);

			homeChildPagerViewholder.movieIcon2 = (ImageView) convertView
					.findViewById(R.id.iv_item_recommend_icon_2);
			homeChildPagerViewholder.movieName2 = (TextView) convertView
					.findViewById(R.id.tv_item_recommend_title_2);
			homeChildPagerViewholder.movieIntroduce2 = (TextView) convertView
					.findViewById(R.id.tv_item_recommend_introduce_2);
			homeChildPagerViewholder.tv_igr_show_2 = (TextView) convertView
					.findViewById(R.id.tv_igr_show_2);

			homeChildPagerViewholder.movieIcon3 = (ImageView) convertView
					.findViewById(R.id.iv_item_recommend_icon_3);
			homeChildPagerViewholder.movieName3 = (TextView) convertView
					.findViewById(R.id.tv_item_recommend_title_3);
			homeChildPagerViewholder.movieIntroduce3 = (TextView) convertView
					.findViewById(R.id.tv_item_recommend_introduce_3);
			homeChildPagerViewholder.tv_igr_show_3 = (TextView) convertView
					.findViewById(R.id.tv_igr_show_3);

			convertView.setTag(homeChildPagerViewholder);

		} else {
			homeChildPagerViewholder = (AllViewholder) convertView.getTag();
		}
		if (homeChildPagerViewholder != null) {
			try {

				// 第一组

				homeChildPagerViewholder.movieName1.setText(mNames
						.get(arg0 * 3));
				homeChildPagerViewholder.movieIntroduce1.setText(mIntroduces
						.get(arg0 * 3));

				homeChildPagerViewholder.movieIcon1
						.setImageResource(R.drawable.default_logo);
				mHelp.showImage(homeChildPagerViewholder.movieIcon1,
						mIcons.get(arg0 * 3));
				clickPlayMorve(homeChildPagerViewholder.movieIcon1, arg0 * 3);

				// 第二组
				homeChildPagerViewholder.movieName2.setText(mNames
						.get(arg0 * 3 + 1));
				homeChildPagerViewholder.movieIntroduce2.setText(mIntroduces
						.get(arg0 * 3 + 1));
				homeChildPagerViewholder.movieIcon2
						.setImageResource(R.drawable.default_logo);
				mHelp.showImage(homeChildPagerViewholder.movieIcon2,
						mIcons.get(arg0 * 3 + 1));
				clickPlayMorve(homeChildPagerViewholder.movieIcon2,
						arg0 * 3 + 1);
				// 第三组
				homeChildPagerViewholder.movieName3.setText(mNames
						.get(arg0 * 3 + 2));
				homeChildPagerViewholder.movieIntroduce3.setText(mIntroduces
						.get(arg0 * 3 + 2));
				homeChildPagerViewholder.movieIcon3
						.setImageResource(R.drawable.default_logo);
				mHelp.showImage(homeChildPagerViewholder.movieIcon3,
						mIcons.get(arg0 * 3 + 2));
				clickPlayMorve(homeChildPagerViewholder.movieIcon3,
						arg0 * 3 + 2);

				// 显示信息
				if (type == 0) {
					if (mBF_num.get(arg0 * 3) == null) {
						homeChildPagerViewholder.tv_igr_show_1
								.setText((int) (Math.random() * 10000) + "人搜");
					} else {
						homeChildPagerViewholder.tv_igr_show_1
								.setText(mBF_num.get(arg0 * 3)
										+ "人搜");
					}

					if (mBF_num.get(arg0 * 3 + 1) == null) {
						homeChildPagerViewholder.tv_igr_show_2
								.setText((int) (int) (Math.random() * 10000)
										+ "人搜");
					} else {
						homeChildPagerViewholder.tv_igr_show_2
								.setText(mBF_num.get(arg0 * 3 + 1)
										+ "人搜");
					}

					if (mBF_num.get(arg0 * 3 + 2) == null) {
						homeChildPagerViewholder.tv_igr_show_3
								.setText((int) (int) (Math.random() * 10000)
										+ "人搜");
					} else {
						homeChildPagerViewholder.tv_igr_show_3
								.setText(mBF_num.get(arg0 * 3 + 2)
										+ "人搜");
					}
				} else if (type == 1) {
					homeChildPagerViewholder.tv_igr_show_1.setText(mC_time.get(arg0 * 3));
					homeChildPagerViewholder.tv_igr_show_2.setText(mC_time.get(arg0 * 3 + 1));
					homeChildPagerViewholder.tv_igr_show_3.setText(mC_time.get(arg0 * 3 + 2));

				} else if (type == 2) {
					if (mPl_num.get(arg0 * 3) == null) {
						homeChildPagerViewholder.tv_igr_show_1
								.setText((int) (Math.random() * 1000) + "好评论数");
					} else {
						homeChildPagerViewholder.tv_igr_show_1
								.setText(mPl_num.get(arg0 * 3)+ "好评论数");
					}

					if (mPl_num.get(arg0 * 3 + 1) == null) {
						homeChildPagerViewholder.tv_igr_show_2
								.setText((int) (Math.random() * 1000) + "好评论数");
					} else {
						homeChildPagerViewholder.tv_igr_show_2
								.setText(mPl_num.get(arg0 * 3 + 1)+ "好评论数");
					}

					if (mPl_num.get(arg0 * 3 + 2) == null) {
						homeChildPagerViewholder.tv_igr_show_3
								.setText((int) (Math.random() * 1000) + "好评论数");
					} else {
						homeChildPagerViewholder.tv_igr_show_3
								.setText(mPl_num.get(arg0 * 3 + 2)+ "好评论数");
					}
				}
			} catch (IndexOutOfBoundsException e) {
			}
		}

		return convertView;
	}

	/**
	 * 设置点击播放电影
	 * 
	 * @param v
	 * @param i
	 */
	public void clickPlayMorve(View v, final int i) {
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("0".equals(mHasfilm.get(i))) {
					UIUtils.showToast(
							mActivity,
							UIUtils.getContext().getResources()
									.getString(R.string.state_not_resource));
					return;
				}

				Intent intent = new Intent(mActivity, PlaySourceActivity.class);
				intent.putExtra(GlobalConstant.TV_ID, mIds.get(i));
				mActivity.startActivity(intent);
			}
		});
	}

}
