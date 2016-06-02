package com.limaoso.phonevideo.bean;

import java.io.Serializable;
import java.util.List;

import com.limaoso.phonevideo.bean.HomeBean.Cont.TV;

public class UserCenter extends BaseBaen implements Serializable {
	public Cont cont;
	public Url url;
	
	public class Url implements Serializable {
		public String recku;
	}

	public class Cont implements Serializable {
		public List<TV> recku;
		public Baseinfo baseinfo;

		public class Baseinfo implements Serializable {
			public String id;
			public String nickname;
			public String face;
			public String birth;
			public String email;
			public String tel;
			public String email_verify;
			public String tel_verify;
			public String score;
		}
	}

}
