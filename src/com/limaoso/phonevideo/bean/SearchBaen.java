package com.limaoso.phonevideo.bean;

import java.util.List;

import com.limaoso.phonevideo.bean.HomeBean.Cont.TV;

public class SearchBaen extends BaseBaen {
	public Cont cont;
	public Url url;

	public class Url {
		public String hotkeys;
		public String faxian;
	}

	public class Cont {
		public List<TV> hotkeys;
		public List<TV> faxian;

	}
}
