package com.limaoso.phonevideo.bean;

import java.io.Serializable;

public class InformNum extends BaseBaen implements Serializable {
	public Cont cont;
	
	public class Cont implements Serializable{
		public String reply_num;
		public String system_num;
	}
}
