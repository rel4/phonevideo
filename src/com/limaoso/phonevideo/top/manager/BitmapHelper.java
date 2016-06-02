package com.limaoso.phonevideo.top.manager;

import com.lidroid.xutils.BitmapUtils;
import com.limaoso.phonevideo.top.utils.FileUtils;
import com.limaoso.phonevideo.utils.UIUtils;

public class BitmapHelper {
	private BitmapHelper() {
	}

	private static BitmapUtils bitmapUtils;

	/**
	 * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
	 * 
	 * @param appContext
	 *            application context
	 * @return
	 */
	public static BitmapUtils getBitmapUtils() {
		if (bitmapUtils == null) {
			// 参数2 图片下载的地址 参数3 图片占用内存(ram断电丢失的)的百分比 8M 0.05-0.8
			bitmapUtils = new BitmapUtils(UIUtils.getContext(),
					FileUtils.getIconDir(), 0.5f);
		}
		return bitmapUtils;
	}
}
