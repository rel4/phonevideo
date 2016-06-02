package com.limaoso.phonevideo.bean;

import java.io.Serializable;
import java.util.List;

public class HomeBean extends BaseBaen implements Serializable {
	public Cont cont;
	public Url url;
	public Topbg topbg;

	public class Topbg implements Serializable {
		public String pic;
		public String skey;
		public String time;
	}

	public class Url implements Serializable {
		public String hotkeys;
		public String indexrec;
		public String dianying;
		public String dianshi;
		public String dongman;
		public String shipin;

	}

	public class Cont implements Serializable {
		public List<TV> hotkeys;
		public List<TV> indexrec;
		public List<TV> dianying;
		public List<TV> dianshi;
		public List<TV> dongman;
		public List<TV> shipin;

		public class TV implements Serializable {
			public String id;
			public String name;
			public String pic;
			public String sdesc;
			public String cfilm_isdefine;
			public String cfilm_id;
			public String cfilm_de_id;
			public String filmid;
			public String hasfilm;

			public String updatetime;
			public String downloadurl;
			public String num;
			public String isnew;

			public String bf_num;
			public String pl_num;
			public String ctime;

		}
		// public class Indexrec {
		// public String id;
		// public String name;
		// public String pic;
		// public String sdesc;
		// }
		// public class Dianying {
		// public String id;
		// public String name;
		// public String pic;
		// public String sdesc;
		// }
		// public class Dianshi {
		// public String id;
		// public String name;
		// public String pic;
		// public String sdesc;
		// }
		// public class Dongman {
		// public String id;
		// public String name;
		// public String pic;
		// public String sdesc;
		// }
		// public class Shipin {
		// public String id;
		// public String name;
		// public String pic;
		// public String sdesc;
		// }
		//
		//
		// public class Hotkeys {
		// public String id;
		// public String name;
		// public String updatetime;
		// public String isnew;
		// }

	}

}
