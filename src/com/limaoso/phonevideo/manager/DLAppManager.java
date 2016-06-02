package com.limaoso.phonevideo.manager;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.limaoso.phonevideo.app.APPManager;
import com.limaoso.phonevideo.base.impl.pager.FindPager;
import com.limaoso.phonevideo.db.findpage.FindDao;
import com.limaoso.phonevideo.db.findpage.FindInfo;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.LoadRequestCallBack;
import com.limaoso.phonevideo.p2p.P2PManager;
import com.limaoso.phonevideo.utils.AppUtils;
import com.limaoso.phonevideo.utils.UIUtils;

public class DLAppManager {
	private static DLAppManager dLAppManager;
	private List<FindInfo> findInfo;
	private Handler appHandler;
	private boolean startDL = false;// 判断有没有开始下载
	private String Appid;// 记录当前的appid和下载的appid是否一样，如果一样则发送下载进度
	private FindPager findPager;

	private DLAppManager() {
		findInfo = new ArrayList<FindInfo>();
	}

	public static synchronized DLAppManager getInstance() {
		if (dLAppManager == null) {
			dLAppManager = new DLAppManager();
		}
		return dLAppManager;
	}

	// 添加info
	public void addFindInfo(FindInfo info) {
		for (FindInfo finfo : getFindInfo()) {
			if (info.getAppid().equals(finfo.getAppid())) {
				return;
			}
		}
		getFindInfo().add(info);
		if (!startDL) {
			startDL = true;
			APPManager.getInstance(UIUtils.getContext()).startDLApp();
		}
	}

	private HttpHelp httpHelp;
	private Message obtain;
	private FindDao findDao;

	// 下载方法
	@SuppressWarnings("unchecked")
	public void DLapp(final FindInfo info) {
		if (httpHelp == null) {
			httpHelp = new HttpHelp();
		}
		if (findDao == null) {
			findDao = new FindDao();
		}
		httpHelp.downLoad(info.getDownloadurl(), info.getLocalurl(),
				new LoadRequestCallBack() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (DLAppManager.getInstance().getAppHandler() != null
								&& DLAppManager.getInstance().getAppid() != null
								&& DLAppManager
										.getInstance()
										.getAppid()
										.equals(DLAppManager.getInstance()
												.getFindInfo().get(0)
												.getAppid())) {
							obtain = Message.obtain();
							obtain.what = 1;
							if (current / total == 1) {
								obtain.arg1 = 100;
							} else {
								obtain.arg1 = (int) ((current * 100) / total);
							}
							DLAppManager.getInstance().getAppHandler()
									.sendMessage(obtain);
						}
					}

					@Override
					public void onSucceed(ResponseInfo t) {
						findDao.UpdateState(DLAppManager.getInstance()
								.getFindInfo().get(0).getAppid(), 2);
						DLAppManager.getInstance().getFindInfo().remove(0);
						AppUtils.installApk(info);
						if (DLAppManager.getInstance().getFindInfo().size() > 0) {
							DLAppManager.getInstance().getFindInfo().get(0);
						} else {
							DLAppManager.getInstance().setStartDL(false);
						}

						if (getFindPager() != null) {
							getFindPager().initDB();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	public List<FindInfo> getFindInfo() {
		return findInfo;
	}

	public void setFindInfo(List<FindInfo> findInfo) {
		this.findInfo = findInfo;
	}

	public Handler getAppHandler() {
		return appHandler;
	}

	public void setAppHandler(Handler appHandler) {
		this.appHandler = appHandler;
	}

	public boolean isStartDL() {
		return startDL;
	}

	public void setStartDL(boolean startDL) {
		this.startDL = startDL;
	}

	public String getAppid() {
		return Appid;
	}

	public void setAppid(String appid) {
		Appid = appid;
	}

	public FindPager getFindPager() {
		return findPager;
	}

	public void setFindPager(FindPager findPager) {
		this.findPager = findPager;
	}

}
