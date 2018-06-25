package com.pera_software.html;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OptionParser {

	public List<Option> parse(String url, Duration timeout) throws Exception {
		List<String> names = new ArrayList<>();
		String description = "";
		List<Option> options = new ArrayList<>();
		Elements elements = parseElements(url, timeout);
		for (Element element : elements) {
			if (element.is("dt")) {
				names.add(element.text());
			}
			if (element.is("dd")) {
				description += element.text();
				options.add(new Option(names, description));
				names.clear();
				description = "";
			}
		}
		return options;
	}
	
	private static Elements parseElements(String url, Duration timeout) throws Exception {
		Document document = Jsoup.parse(new URL(url), (int)timeout.toMillis());
		return document.select("html > body > dl > dt, html > body > dl > dd");
	}
}
