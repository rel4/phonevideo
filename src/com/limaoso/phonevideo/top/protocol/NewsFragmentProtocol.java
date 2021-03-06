package com.limaoso.phonevideo.top.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.limaoso.phonevideo.parse.JsonParse;
import com.limaoso.phonevideo.top.bean.BaseTopBean;
import com.limaoso.phonevideo.top.bean.DTVListBaen;

public class NewsFragmentProtocol extends BaseProtocol<List<BaseTopBean>> {

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<BaseTopBean> parserJson(String json) {
		DTVListBaen dt = null;
		try {
			dt = JsonParse.parserJson(json, DTVListBaen.class);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		ArrayList<BaseTopBean> datas = new ArrayList<BaseTopBean>();
		if (dt != null) {

			if (dt.zhiding != null) {

				datas.addAll(dt.zhiding);
			}
			if (dt.cont != null) {

				datas.addAll(dt.cont);
			}
		}

		return datas;
	}

}
