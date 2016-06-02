package com.limaoso.phonevideo.top.holder;

import android.view.View;
import android.widget.ImageView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.top.bean.BaseTopBean;
import com.limaoso.phonevideo.top.bean.DTVListBaen.DTvCont;
import com.limaoso.phonevideo.utils.UIUtils;

public class TopNewsHolder extends BaseHolder<BaseTopBean> {

	private ImageView iv_top_news;

	@Override
	protected View initView() {
		View v = UIUtils.inflate(R.layout.item_top_news);
		iv_top_news = (ImageView) v.findViewById(R.id.iv_top_news);
		return v;
	}

	@Override
	public void refreshView(BaseTopBean data) {
		if (data != null && data instanceof DTvCont) {
			DTvCont d = (DTvCont) data;
			bitmapUtils.display(iv_top_news, d.pic);
		}

	}
}
