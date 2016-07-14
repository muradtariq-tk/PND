import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class getmakerlist {
	public static String[][] get() {
		String[][] ret = null;
		try {
			Document doc;
			doc = Jsoup.connect("http://www.phonearena.com/phones/manufacturers")
					.timeout(10 * 1000).get();
			writehtml.write("makers.html", doc.toString());
			Elements title = doc.select("div#brands");
			Elements li = title.select("a[href].ahover");
			ret = new String[li.size()][2];
			for (int i = 0; i < li.size(); i++) {
				Element sli = li.get(i);
				ret[i][0] = sli.text();
				Element link = sli.select("a").first();
				String absHref = link.attr("abs:href"); // "http://jsoup.org/"
				ret[i][1] = absHref.toString();

			}

			System.out.println("Maker list returned");
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
