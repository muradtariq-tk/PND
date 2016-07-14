import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class writehtml {
	public static void write(String path, String data) {
		try (PrintWriter out = new PrintWriter(path)) {
			out.println(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
