package com.limaoso.phonevideo.play.bean;

import java.util.List;

import com.limaoso.phonevideo.bean.BaseBaen;

public class PlayCommentBaen extends BaseBaen {

	private static final long serialVersionUID = 1960434317463230100L;
	public Cont cont;

	public class Cont {
		public List<Comlist> comlist;
	}
}
