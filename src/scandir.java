import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class scandir {
	public static int totalfiles = 0;
	public static List<String> fanspath = new LinkedList<String>();

	public static void walk(String path) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walk(f.getAbsolutePath());
				// System.out.println("Dir:" + f.getAbsoluteFile());
			} else {
				totalfiles = totalfiles + 1;
				String file = f.getAbsoluteFile().toString();
				if (file.contains("fans.txt")) {
					fanspath.add(file);
				}
				// System.out.println("" + file);
			}
		}
	}

	public static void main(String[] args) {
		walk("GSMARENA");
		System.out.println("\nTotal Files:" + fanspath.size());
		String[] array = fanspath.toArray(new String[fanspath.size()]);
		String[][] csv = new String[array.length][5];
		for (int i = 0; i < array.length; i++) {

			String[] sta = array[i].split("\\\\");
			csv[i][0] = sta[sta.length-3];
			csv[i][1] = sta[sta.length - 2];
			String release = loadrelease(array[i]);
			String[] str = release.split(",");

			csv[i][2] = str[0];
			csv[i][3] = str[1];

			csv[i][4] = loadfans(array[i]) + "";
			
		}
		writecsv.write("fans", csv);
	}

	public static String loadrelease(String path) {
		String[] spl = path.split("\\\\");
		String release = "";
		for (int i = 0; i < spl.length - 1; i++) {
			release = release + spl[i] + "\\\\";
		}
		release = release +"release.txt";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(release)));
			String line = "";
			String str = "";
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					str = str + line;
				}
			}

			return str;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "";
	}

	public static int loadfans(String path) {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					path)));
			String line = "";
			String str = "";
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					str = str + line;
				}
			}

			return Integer.parseInt(str);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return 0;
	}
}
