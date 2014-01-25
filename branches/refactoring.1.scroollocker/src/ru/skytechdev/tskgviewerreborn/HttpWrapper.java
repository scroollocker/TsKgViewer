package ru.skytechdev.tskgviewerreborn;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpWrapper {
	
	public static void sendCounter() {
		try {
			Jsoup.connect("http://www.net.kg/img.php?id=991")
					.ignoreContentType(true)
					.userAgent("TsKgViewer Agent v1.0")
					.get();
		} catch (IOException e) {
		}
	}
	
	public static Document getHttpDoc(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url)
					.ignoreContentType(true)
					.userAgent("TsKgViewer Agent v1.0")
					.timeout(5000000)
					.get();
		} catch (IOException e) {
			doc = null;
		}
		HttpWrapper.sendCounter();
		return doc;
	}
	
	public static Document postHttpAjax(String url, Map<String,String> data) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url)					
					.header("Accept", "application/json, text/javascript, */*; q=0.01")
					.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
					.ignoreContentType(true)
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
}
