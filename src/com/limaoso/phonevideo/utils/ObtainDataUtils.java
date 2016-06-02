package com.limaoso.phonevideo.utils;


public class ObtainDataUtils {
	public static String obtainLink(String path) {
		// http://bt.box.n0808.com/B2/40/B298DD7E3BF7B300FF1F235B90FD5441002FE440.torrent"
		String rootPath = "http://bt.box.n0808.com/";
		String beg = path.substring(0, 2);
		String e_word = path.substring(path.length() - 3);
		return rootPath + beg + "/" + e_word + path + ".torrent";
	}
}
