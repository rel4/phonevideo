package com.limaoso.phonevideo.bean;

public class VersionInfo extends BaseBaen {
	public Cont cont;
	public class Cont {
		public String title;
		public String vname;
		public String desc;
		public String vcode;
		public String durl;
		public String fsize;
		public String pdate;
		/**
		 * 强制更新 
		 * 		0  不强制更新
		 * 		1 强制更新
		 */
		public String forcedUpdating;
	}
}
