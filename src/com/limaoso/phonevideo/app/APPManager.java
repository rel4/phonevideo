package com.limaoso.phonevideo.app;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.limaoso.phonevideo.db.AppService;
import com.limaoso.phonevideo.manager.DLAppManager;

public class APPManager {
	private AppService us;
	private final static APPManager instance = new APPManager();
	private static Context mContext;

	/**
	 * 获取P2P管理对象
	 * 
	 * @return
	 */
	public static APPManager getInstance(Context context) {
		if (mContext == null) {
			mContext = context;
		}
		return instance;
	}

	/**
	 * 服务开启
	 */
	private void startConnectionService() {
		if (!isServiceRunning(AppService.class)) {
			mContext.startService(new Intent(mContext, AppService.class));
		}
		if (us == null) {
			mContext.bindService(new Intent(mContext, AppService.class), conn,
					Context.BIND_AUTO_CREATE);
		}
	}

	/**
	 * 服务连接对象
	 */
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			us = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service instanceof AppService.MyBinder) {
				AppService.MyBinder binder = (AppService.MyBinder) service;
				Service sv = binder.getService();
				if (sv instanceof AppService) {
					us = (AppService) sv;
					us.DLapp(DLAppManager.getInstance().getFindInfo().get(0));
				}

			}

		}
	};
	private boolean isStart = false;

	// 调用下载方法
	public void startDLApp() {
		if (us != null) {
			if (DLAppManager.getInstance().getFindInfo().size() > 0) {
				us.DLapp(DLAppManager.getInstance().getFindInfo().get(0));
			}
		} else {
			startConnectionService();
		}
	}

	/**
	 * 判断服务开启
	 * 
	 * @param serviceName
	 * @return
	 */
	private boolean isServiceRunning(Class clz) {
		String serviceName = clz.getName();
		ActivityManager manager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 开启服务
	 */
	public void startAPPsService() {
		if (us == null) {
			startConnectionService();
		}

	}

}
