import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class phonearena_mobile_details_and_reviews {
	public static void main(String[] args) {
		try {
			Document doc = Jsoup
					.connect(
							"http://www.phonearena.com/phones/Samsung-Galaxy-S5_id8202/reviews")
					.timeout(10 * 1000).get();
			System.out.println(fillreviews(doc).length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getaka(Document d) {
		Elements phonespecs = d.select("div#phone_specificatons");
		String docstring = phonespecs.text();
		try {
			if (docstring.contains("also known as")) {
				String[] spl = docstring.split("also known as");

				return spl[spl.length - 1].replaceAll(",", "::::");
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static String[][] fillreviews(Document d) throws Exception {
		try {
			Document carry = d;
			LinkedList<String> reviews = new LinkedList<String>();
			reviews = fillreviewsfromdoc(reviews, carry);
			String nexturl = hasnext(carry);

			while (nexturl != null) {
				carry = Jsoup.connect(nexturl).timeout(10 * 1000).get();
				reviews = fillreviewsfromdoc(reviews, carry);
				nexturl = hasnext(carry);

			}

			String[][] ret = getstringfromlist(reviews);
			return ret;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		

	}

	private static String[][] getstringfromlist(LinkedList<String> inplist) {
		try {
			String[] array = inplist.toArray(new String[inplist.size()]);
			String[][] ret = new String[array.length][2];
			for (int i = 0; i < ret.length; i++) {
				String[] spl = array[i].split("::::");
				ret[i][0] = spl[0];
				ret[i][1] = spl[1];
			}
			return ret;

		} catch (Exception e) {
		}
		return null;
	}

	private static LinkedList<String> fillreviewsfromdoc(
			LinkedList<String> inplist, Document d) {
		Elements rev = d.select("div.s_user_review");
		System.out.println(rev.size() + " found on current page");
		String tmp = "";
		
		for (int i = 0; i < rev.size(); i++) {
			Element ur = rev.get(i);
			Elements rating = ur.select("div.s_rating_box");
			Elements user = ur.select("a.s_author");
			if (user.text().equals("") || user.text().length()==0) {
				user = ur.select("h4.s_head");
				tmp = user.text();
				String[] spl = tmp.split("posted on");
				tmp = spl[0];
				inplist.add(tmp  + "::::" + rating.text());
				continue;
			}
			inplist.add(user.text() + "::::" + rating.text());

		}

		return inplist;

	}

	private static String hasnext(Document d) {
		Elements paging = d.select("div.s_pager");
		String strlink = "";
		if (paging.text().contains("Next")) {
			Elements next = paging.select("li.s_next");
			Elements link = next.select("a[href]");
			strlink = link.attr("abs:href");
			System.out.println("Current page is :" + strlink);
			return strlink;
		}
		return null;
	}

}
