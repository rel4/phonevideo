package com.limaoso.phonevideo.utils;

import android.view.ViewGroup;
import android.view.ViewParent;

import com.limaoso.phonevideo.top.ui.LoadingPage;

public class ViewUtils {
	public static void removeParent(LoadingPage mFrameLayout) {
		// 爹移除孩子 // 首先获取到孩子的爹
		ViewParent parent = mFrameLayout.getParent();
		ViewGroup group = (ViewGroup) parent;
		group.removeView(mFrameLayout);

	}
}
