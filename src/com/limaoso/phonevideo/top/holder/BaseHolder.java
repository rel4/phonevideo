package com.limaoso.phonevideo.top.holder;

import android.graphics.Bitmap;
import android.view.View;

import com.lidroid.xutils.BitmapUtils;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.top.manager.BitmapHelper;

public abstract class BaseHolder<T> {
	protected BitmapUtils bitmapUtils;
	protected View contentView;

	public BaseHolder() {
		if (bitmapUtils == null) {
			bitmapUtils = BitmapHelper.getBitmapUtils();
			bitmapUtils.configDefaultLoadingImage(R.drawable.default_logo); // 加载中显示的图片
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_logo); // 加载失败
			bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		}
		contentView = initView();
		contentView.setTag(this);
	}

	/**
	 * 初始化每个控件
	 * 
	 * @return
	 */
	protected abstract View initView();

	/**
	 * 给每个控件赋值
	 * 
	 * @param data
	 */
	public abstract void refreshView(T data);

	/**
	 * 获取布局
	 * 
	 * @return
	 */
	public View getContentView() {
		return contentView;
	}
}
