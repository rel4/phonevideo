package com.limaoso.phonevideo.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.limaoso.phonevideo.global.MyApplication;

public class UIUtils {
	private static Toast toast;

	public static void showToast(Context ctx, String text) {
		if (toast == null) {
			toast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}

	// 先将BaseApplication中提供出来的所有的变量提供相应的一种获取方式
	public static Context getContext() {
		return MyApplication.getContext();
	}

	public static Handler getHandler() {
		return MyApplication.getHandler();
	}

	public static int getMainThreadId() {
		return MyApplication.getMainThreadId();
	}

	public static Thread getMainThread() {
		return MyApplication.getMainThread();
	}

	public static Resources getResources() {
		return getContext().getResources();
	}

	// 从string.xml中获取字符串
	public static String getString(int stringId) {
		// 上下文环境获取资源文件夹
		return getResources().getString(stringId);
	}

	// 通过资源文件id获取图片对象
	public static Drawable getDrawable(int drawableID) {
		return getResources().getDrawable(drawableID);
	}

	// 添加string类型数组的方法
	public static String[] getStringArray(int stringArrayId) {
		return getResources().getStringArray(stringArrayId);
	}

	// dip-->px
	public static int dip2px(int dip) {
		// density就是当前代码运行到的手机dp和px的转换关系
		float d = getResources().getDisplayMetrics().density;
		return (int) (dip * d + 0.5);
	}

	// px--->dp
	public static int px2dip(int px) {
		float d = getResources().getDisplayMetrics().density;
		return (int) (px / d + 0.5);
	}

	// 此代码运行的线程是否为主线程判断(子线程开启网络请求操作)
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}

	// handler机制
	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			// 运行在主线程中的任务
			runnable.run();
		} else {
			// 不是运行在主线程中的任务,通过handler机制,将其传递至主线程运行
			getHandler().post(runnable);
		}
	}

	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		return getResources().getColorStateList(mTabTextColorResId);
	}

	public static View inflate(int layoutId) {
		return View.inflate(getContext(), layoutId, null);
	}

	//
	public static int getDimenPx(int dimenId) {
		return getResources().getDimensionPixelSize(dimenId);
	}

	public static void postDelayed(Runnable runnable, int delayMillis) {
		getHandler().postDelayed(runnable, delayMillis);
	}

	public static void removeCallBack(Runnable runnableTask) {
		// 移除传递进来任务
		getHandler().removeCallbacks(runnableTask);
	}

	/**
	 * 查找控件id
	 * 
	 * @param rootView
	 * @param id
	 * @return
	 */
	public static <T extends View> T $(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}

	/**
	 * 将文字转换成全角字符，防止textview排版不一
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
}
