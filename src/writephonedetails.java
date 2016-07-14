import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class writephonedetails {
	public static Document doc;
	public static Document docreviews;
	public static Map<String, String> doccookies = null;
	public static String aka = "";
	public static Map<String, String> cks = new HashMap<String, String>();

	public static void write(String path, String phonetitle, String url,
			String[][] cookies) {
		int error = 0;
		String[] spl = path.split("//");
		String brand = spl[0];
		String mobileid="";
		// if (checkdbexists(spl[spl.length-1], phonetitle)) {
		// } else {

		String[][] reviews = null;
		String[] tpath = url.split("www.");
		String turl = "http://m." + tpath[1];
		phonetitle = phonetitle.replaceAll("/", "-");

		mkdir("PHONEARENA//" + path, phonetitle);
		writefile("PHONEARENA//" + path + phonetitle + "//", turl + "",
				"url.txt");

		if (fileexist("PHONEARENA//" + path + phonetitle + "//save.txt")) {
			System.out.println("file exists:" + "PHONEARENA//" + path
					+ phonetitle + "//save.txt");
			return;
		}
		try {
			getdoc(turl);
			turl = turl + "/reviews";
			getdocreviews(turl);
			writehtml.write("PHONEARENA\\" + path + "\\" + phonetitle + "\\"
					+ phonetitle + ".html", doc.toString());
			writehtml.write("PHONEARENA\\" + path + "\\" + phonetitle + "\\"
					+ phonetitle + "-reviews.html", docreviews.toString());

			aka = phonearena_mobile_details_and_reviews.getaka(doc);

			if (fileexist("PHONEARENA//" + path + phonetitle + "//mobileid.txt")) {
				BufferedReader br = new BufferedReader(new FileReader("PHONEARENA//" + path + phonetitle + "//mobileid.txt"));
				try {
				    StringBuilder sb = new StringBuilder();
				    String line = br.readLine();

				    while (line != null) {
				        sb.append(line);
				        sb.append(System.lineSeparator());
				        line = br.readLine();
				    }
				    String everything = sb.toString();
				    mobileid = everything.trim();
				} finally {
				    br.close();
				}
			} else {
				mobileid = savemobile(brand, phonetitle, aka);
			}
			writefile("PHONEARENA//" + path + phonetitle + "//", mobileid + "",
					"mobileid.txt");

			reviews = phonearena_mobile_details_and_reviews
					.fillreviews(docreviews);

			savereviews(reviews, mobileid);

			writefile("PHONEARENA//" + path + phonetitle + "//", "Done" + "",
					"save.txt");
		} catch (Exception e) {
			writefile("PHONEARENA//" + path, phonetitle + "", "error-"
					+ phonetitle + ".txt");
		}

	}

	public static boolean fileexist(String path) {
		try {
			File f = new File(path);
			if (f.exists() && !f.isDirectory()) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	private static void savereviews(String[][] reviews, String mobileid)
			throws SQLException {
		if (reviews.length != 0) {
			String[] cols = new String[3];
			cols[0] = "mobileid";
			cols[1] = "username";
			cols[2] = "rating";

			String[][] rev = new String[reviews.length][3];
			for (int i = 0; i < reviews.length; i++) {
				rev[i][0] = mobileid;
				rev[i][1] = reviews[i][0];
				rev[i][2] = reviews[i][1];
			}

			db.insert_into_table("userreview", cols, rev);
		}
	}

	private static String savemobile(String brand, String model, String ak)
			throws SQLException {

		String[] cols = new String[3];
		cols[0] = "brand";
		cols[1] = "title";
		cols[2] = "aka";
		String[] vals = new String[3];
		vals[0] = brand;
		vals[1] = model;
		vals[2] = ak;

		String id = db.insert_into_table_getID("mobile", cols, vals);
		return id;

	}

	public static String getrelease() {
		String str = doc.text();
		String[] sar = str.split("Announced");
		sar = sar[1].split("Status");

		return sar[0];

	}

	public static int getgsmarenaid(String url) {
		String[] str = url.split("-");
		int id = Integer.parseInt(str[str.length - 1].split("\\.")[0]);
		return id;
	}

	public static void writefile(String path, String data, String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path
					+ filename));
			writer.write(data);

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void mkdir(String path, String dir) {
		try {
			File theDir = null;
			if (path.length() == 0) {
				theDir = new File(dir);
			} else {
				theDir = new File(path + "\\" + dir);
			}
			// if the directory does not exist, create it
			if (!theDir.exists()) {
				System.out.println("creating directory: " + dir);
				boolean result = false;
				try {
					theDir.mkdirs();
					result = true;
				} catch (Exception se) {
					se.printStackTrace();
				}
				if (result) {
					System.out.println("DIR created");
				}
			}

		} catch (Exception e) {
		}
	}

	public static int getfans() {
		Element li = doc.select("li#fan-vote").first();
		int ret = Integer.parseInt(li.text().split(" ")[0]);
		return ret;

	}

	public static int checknetwork() {
		Element table = doc.select("table[class=expandable-table]").first();
		String str = table.text();
		if (!str.contains("No cellular connectivity")) {
			return 1;
		}
		return 0;
	}

	public static void getdoc(String url) throws Exception {
		String[][] ret = null;
		try {
			doc = Jsoup
					.connect(url)
					.timeout(10 * 1000)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
					.get();
			writehtml.write("phn.html", doc.toString());
			Response res = Jsoup.connect(url).execute();
			doccookies = res.cookies();
		} catch (Exception e) {
			throw e;
		}

	}

	public static void getdocreviews(String url) throws Exception {
		try {
			String newurl = url;
			docreviews = Jsoup
					.connect(newurl)
					.timeout(10 * 1000)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
					.get();
			// .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")

			writehtml.write("phn.html", doc.toString());
		} catch (Exception e) {
			throw e;
		}

	}
}
