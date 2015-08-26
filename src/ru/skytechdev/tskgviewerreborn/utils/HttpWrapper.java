package ru.skytechdev.tskgviewerreborn.utils;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpWrapper {
	
	public static Document getHttpDoc(String url) {
		Document doc = null;
		
		if (url.substring(0, 1).equals("/")) {
			url = TsUtils.getBasePath() + url;
		}
		
		try {
			doc = Jsoup.connect(url)
					.ignoreContentType(true)
					.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:35.0) Gecko/20100101 Firefox/35.0")
					.timeout(5000000)
					.get();
		} catch (IOException e) {
			doc = null;
		}
		return doc;
	}
	
	public static Document postHttpAjax(String url, Map<String,String> data, String refer) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url)					
					.header("Accept", "application/json, text/javascript, */*; q=0.01")
					.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")					
					.header("Referer", refer)
					.header("X-Requested-With", "XMLHttpRequest")
					.ignoreContentType(true)
					.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:35.0) Gecko/20100101 Firefox/35.0")
					.data(data)
					.ignoreContentType(true)
					.followRedirects(false)
					.timeout(5000000)
					.post();
		} catch (IOException e) {
			doc = null;
		}		
		return doc;
	}
	
	public static Document getHttpAjax(String url, String refer) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url)					
					.header("Accept", "application/json, text/javascript, */*; q=0.01")				
					.header("Referer", refer)
					.header("X-Requested-With", "XMLHttpRequest")
					.ignoreContentType(true)
					.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:35.0) Gecko/20100101 Firefox/35.0")
					.ignoreContentType(true)
					.followRedirects(false)
					.timeout(5000000)
					.get();
		} catch (IOException e) {
			doc = null;
		}		
		return doc;
	}

}
