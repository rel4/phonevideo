package com.limaoso.phonevideo.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 消息通知页面----》javabean
 * @author liang
 *
 */
public class InfromPageBean  extends BaseBaen implements Serializable {
	public List<Cont> cont;
	public class Cont implements Serializable{
		public String comment_id;
		public String film_id;
		public String film_name;
		public String msg;
		public String time;
		public String nickname;
		public String user_id;
		public String face;
		public String name;
		public String type;
		public String rel_parm;
	}
}
