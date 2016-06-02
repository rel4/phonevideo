package com.limaoso.phonevideo.notificationmanafer;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.limaoso.phonevideo.activity.PlayActivity;
import com.limaoso.phonevideo.activity.PlaySourceActivity;
import com.limaoso.phonevideo.activity.menu.RecommendDetailActivity;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.utils.UIUtils;

public class NotificationClickHandler {
	/**
	 * 來源頁
	 */
	public static final String source_activity = "1";
	/**
	 * 专题页
	 */
	public static final String subject_activity = "2";
	/**
	 * 播放页
	 */
	public static final String paly_activity = "3";
	/**
	 * 设置行为
	 * @param map
	 */
	public void setAction(Map<String, String> map) {
		if (map==null) {
			throw new RuntimeException("map not null");
		}
		String type = map.get("type");
		if (!TextUtils.isEmpty(type)) {
			String id = map.get("id");
			selectAction(type, id);
		}
	}
	/**
	 * 判断行为
	 * @param action
	 * @param id
	 */
	private void selectAction(String action, String id) {
		switch (action) {
		case source_activity:
			enterActivity(PlaySourceActivity.class, GlobalConstant.TV_ID, id);
			break;
		case subject_activity:
			enterActivity(RecommendDetailActivity.class, GlobalConstant.RECOMMEND_DETAIL_RECID, id);
			break;
		case paly_activity:
			enterActivity(PlayActivity.class, GlobalConstant.TV_ID, id);
			break;

		}
	}
	/**
	 *进入页面
	 * @param context
	 * @param clz
	 * @param flag
	 * @param id
	 */
	private void enterActivity(Class clz,String flag ,String id){
		Context context = UIUtils.getContext();
		Intent intent = new Intent(context,clz);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(flag, id);
		context.startActivity(intent);
		
	}
		

}
