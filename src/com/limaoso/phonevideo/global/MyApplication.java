package com.limaoso.phonevideo.global;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;

import com.limaoso.phonevideo.activity.AlertDialogActivity;
import com.limaoso.phonevideo.app.APPManager;
import com.limaoso.phonevideo.notificationmanafer.NotificationClickHandler;
import com.limaoso.phonevideo.p2p.P2PConfig;
import com.limaoso.phonevideo.p2p.P2PManager;
import com.limaoso.phonevideo.p2p.P2PManager.P2PCacheListener;
import com.limaoso.phonevideo.utils.CommonUtils;
import com.limaoso.phonevideo.utils.FileUtils;
import com.limaoso.phonevideo.utils.LogUtil;
import com.limaoso.phonevideo.utils.UIUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * 获取全局application对象 在里面获取相应的对象，对外提供 处理全局未扑获的异常
 * 
 * @author liang
 * 
 */
public class MyApplication extends Application {
	private String TAG = "MyApplication";

	public static Uri bitmap;
	private static Context appContext; // context对象
	private static Handler handler;
	private static int mainThreadId; // 主线程id
	private static Thread mainThread; // 当前线程
	private static Activity mainActivity;
	public static boolean ISKEYBORD_OPEN = false;// 判断当前是否打开了键盘

	/**
	 * 当前下载任务的hash
	 */
	private static String data;

	public static String getData() {
		return data;
	}

	public static void setData(String data) {
		MyApplication.data = data;
	}

	public static Activity getMainActivity() {
		return mainActivity;
	}

	public static void setMainActivity(Activity mainActivity) {
		MyApplication.mainActivity = mainActivity;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		appContext = this;
		handler = new Handler();
		mainThreadId = android.os.Process.myTid();
		mainThread = Thread.currentThread();
		initData();
		// 设置未扑获异常的处理器
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler()); // 设置未扑获的异常
	}

	private void initData() {
		initPush();
		P2PConfig.getInstance().init(appContext);
	}

	/**
	 * 友盟推送
	 */
	private void initPush() {
		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				if (msg != null && msg.extra != null) {

					NotificationClickHandler clickHandler = new NotificationClickHandler();
					clickHandler.setAction(msg.extra);
				}

			}
		};
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setNotificationClickHandler(notificationClickHandler);

	}

	public static Handler getHandler() {
		return handler;
	}

	public static Context getContext() {
		return appContext;
	}

	public static int getMainThreadId() {
		return mainThreadId;
	}

	public static Thread getMainThread() {
		return mainThread;
	}

	/**
	 * 自定义全局未扑获异常的处理器
	 * 
	 * @author liang
	 * 
	 */
	class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {
		/**
		 * 一旦发生未扑获的异常，产生崩溃就会回调此方法
		 */
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {

			ex.printStackTrace();
			// 结束线程
			FileUtils.writeFileLog(ex.getMessage(), "exception.txt");
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

}
