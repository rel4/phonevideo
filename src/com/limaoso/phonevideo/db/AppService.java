package com.limaoso.phonevideo.db;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.limaoso.phonevideo.db.findpage.FindInfo;
import com.limaoso.phonevideo.download.DownServerControl;
import com.limaoso.phonevideo.manager.DLAppManager;
import com.limaoso.phonevideo.utils.LogUtil;

public class AppService extends Service {

	public static final String TAG = "AppService";
	// P2P管理类
	private DownServerControl p2pControl;

	private String currentTaskName;

	public class MyBinder extends Binder {
		public Service getService() {
			return AppService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.e(TAG, "app service开启");
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 获取P2管理类
	 * 
	 * @return
	 */
	private DownServerControl getDownServerControl() {
		if (p2pControl == null) {
			p2pControl = new DownServerControl();
		}
		return p2pControl;
	}

	// 下载app方法
	public void DLapp(FindInfo info) {
		if (info == null) {
			return;
		}
		LogUtil.e(TAG, "开始下载：" + info.getAppname());
		DLAppManager.getInstance().DLapp(info);
	}

}
