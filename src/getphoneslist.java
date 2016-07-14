import java.util.LinkedList;
import java.util.List;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class getphoneslist {

	public static String[][] ret = null;
	public static List<String> plist = null;
	public static int hasnext = 0;
	public static String nexturl = "";
	public static Document doc = null;

	public static String[][] get(String inp) {
		try {
			plist = new LinkedList<String>();
			doc = Jsoup.connect(inp).timeout(10 * 1000).get();
			writehtml.write("phonelist.html", doc.toString());

			// get 1 of X nav

			fillplist();

			while (hasnext != 0) {
				doc = Jsoup.connect(nexturl).timeout(10 * 1000).get();
				writehtml.write("phonelist.html", doc.toString());
				fillplist();
			}

			generateStringList();

			return ret;

			//

		} catch (Exception e) {

		}

		return null;
	}

	public static void generateStringList() {
		String[] array = plist.toArray(new String[plist.size()]);
		ret = new String[array.length][2];

		for (int i = 0; i < plist.size(); i++) {
			String[] spl = array[i].split("####");
			ret[i][0] = spl[0];
			ret[i][1] = spl[1];
		}
	}

	public static void fillplist() {

		Elements phonediv = doc.select("div#phones");
		Elements phones = phonediv.select("div.s_block_4");
		System.out.println(phones.size());
		for (int i = 0; i < phones.size(); i++) {
			String mobile = "";
			String linkstr = "";

			Element block = phones.get(i);
			Elements links = block.select("a[href]");
			for (int j = 0; j < links.size(); j++) {
				Element link = links.get(j);
				try {
					if (link.select("span.title").text().length() != 0) {
						mobile = link.select("span.title").text();
						linkstr = link.attr("abs:href");
						plist.add(mobile + "####" + linkstr);
					}
				} catch (Exception e) {
				}
			}

			// System.out.println(links.size());
			// System.out.println(links.select("span.title").text());
			// System.out.println("ok");
		}
		setnext();

		/*
		 * for (int i = 0; i < pe; i++) { try { Document doc; doc =
		 * Jsoup.connect(template + (i + 1) + ".php") .timeout(10 * 1000).get();
		 * writehtml.write("p_phonelist_" + (i + 1) + ".html", doc.toString());
		 * 
		 * Elements div = doc.select("div#list-brands");
		 * 
		 * Elements li = div.select("li");
		 * 
		 * String name = ""; String URL = "";
		 * 
		 * for (int j = 0; j < li.size(); j++) { Element sli = li.get(j); name =
		 * sli.text(); Element link = sli.select("a").first(); String absHref =
		 * link.attr("abs:href"); // "http://jsoup.org/" URL =
		 * absHref.toString();
		 * 
		 * plist.add(name + "####" + URL); }
		 * 
		 * } catch (Exception e) { } }
		 * 
		 * String[] array = plist.toArray(new String[plist.size()]); ret = new
		 * String[array.length][2];
		 * 
		 * for (int i = 0; i < plist.size(); i++) { String[] spl =
		 * array[i].split("####"); ret[i][0] = spl[0]; ret[i][1] = spl[1]; }
		 */
	}

	public static void setnext() {
		Elements paging = doc.select("div.s_pager.s_pager_s_size_2.s_p_20_0");
		try {
			Elements li = doc.select("li.s_next");
			if (li.text().length() != 0) {
				hasnext = 1;
				Elements lix = li.select("a[href]");
				nexturl = lix.get(0).attr("abs:href");
				System.out.println(nexturl);

			} else {
				hasnext = 0;
				nexturl = "";
			}
		} catch (Exception e) {
		}

	}

}
