package com.limaoso.phonevideo.adapter;

import java.util.List;
import java.util.Random;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.HomeBean.Cont.TV;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.utils.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdapter extends MyGridAndListViewBaseAdapter {

	private HttpHelp mHelp;
	private List<TV> mList;

	public void setAdata(List<TV> list) {
		this.mList = list;
	}

	public HomeAdapter(List list) {
		super(list, false);
		mHelp = new HttpHelp();
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = UIUtils.inflate(R.layout.list_item_home);
			holder.iv_item_home_icon = (ImageView) view
					.findViewById(R.id.iv_item_home_icon);
			holder.tv_item_home_title = (TextView) view
					.findViewById(R.id.tv_item_home_title);
			holder.tv_lih_show = (TextView) view.findViewById(R.id.tv_lih_show);
			holder.tv_item_home_introduce = (TextView) view
					.findViewById(R.id.tv_item_home_introduce);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (mList == null) {
			UIUtils.showToast(UIUtils.getContext(), "weilllllllllllllll");
		}
		TV tv = mList.get(arg0);
		if (tv != null) {
			holder.tv_item_home_title.setText(tv.name);
			mHelp.showImage(holder.iv_item_home_icon, tv.pic);
			holder.tv_item_home_introduce.setText(tv.sdesc);
			if (tv.bf_num == null) {
				holder.tv_lih_show
						.setText((int) (Math.random() * 10000) + "人搜");
			} else {
				holder.tv_lih_show.setText(tv.bf_num + "人搜");
			}
		}
		return view;
	}

	private class ViewHolder {
		ImageView iv_item_home_icon;
		TextView tv_item_home_introduce, tv_item_home_title, tv_lih_show;

	}

}
