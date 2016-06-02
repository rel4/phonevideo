package com.limaoso.phonevideo.bean;

import java.io.Serializable;
import java.util.List;

import com.limaoso.phonevideo.bean.HomeBean.Cont.TV;
import com.limaoso.phonevideo.bean.PlaySoursBase.Cont.Cfilmlist;
import com.limaoso.phonevideo.bean.PlaySoursBase.Cont.Ckuinfo;
import com.limaoso.phonevideo.bean.PlaySoursBase.Cont.Shareinfo;

public class PlayBaen extends BaseBaen implements Serializable {
	public Cont cont;
	public Url url;

	public class Url {
		public String recku;
		public String replylist;
		public String postmsg;
		public String commentlist;
		public String getsource;
		public String goDingCai;
		public String goFav;
	}

	public class Cont {
		public Shareinfo shareinfo;
		public Cfilminfo cfilminfo;
		public List<TV> recku;
		public Ckuinfo ckuinfo;
		public String nickname;
		public String commentCount;
		public Celinfo relinfo;
		public List<Commentlist> commentlist;
		public List<Cfilmlist> jujiinfo;

		public class Celinfo {
			public String user_fav_zan_c;
			public String user_fav_find_c;
		}

		public class Cfilminfo {
			public String id;
			public String hash;
			public String name;
			public String up;
			public String down;
			public String bfclick;
			public String type;
			public String lmlink;
			public String hasht;
			public String lmhash;
			public String pic;
			public long filesize;
		}

	}

}
