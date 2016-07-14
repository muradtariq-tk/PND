import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class createmakerdirectories {
	public static void create(String[][] inp) {
		// Create root folder GSMARENA
		mkdir("", "PHONEARENA");
		for (int i = 0; i < inp.length; i++) {
			mkdir("PHONEARENA", inp[i][0]);
			String url = inp[i][1];
			String[] spl = url.split("-");
			spl = spl[spl.length - 1].split("\\.");
			String makerid = spl[0];
			writestring("PHONEARENA\\"+inp[i][0]+"\\id.txt", makerid);
			writestring("PHONEARENA\\"+inp[i][0]+"\\url.txt", inp[i][1]);
		}

	}

	public static void writestring(String path, String text) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(text);

			// Close writer
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
					theDir.mkdir();
					result = true;
				} catch (SecurityException se) {
					// handle it
				}
				if (result) {
					System.out.println("DIR created");
				}
			}

		} catch (Exception e) {
		}
	}
}
