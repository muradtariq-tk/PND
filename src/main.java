import java.util.Scanner;

public class main {
	// first array element for maker name
	// second array element for maker URL

	public static String[][] makers = null;
	public static String[][] phones = null;
	public static String[][] cookies = null;

	public static void main(String[] args) {

		db.setuser("root");
		db.setpassword("");
		db.setdatabase("phonearena");

		Scanner reader = new Scanner(System.in); // Reading from System.in
		System.out.println("Enter number of cookies:");
		int n = reader.nextInt();
		cookies = new String[n][2];

		for (int i = 0; i < cookies.length; i++) {
			System.out.println("Enter String:");

			String[] spl = reader.next().split("=");
			String var = spl[0];
			String val = "";
			for (int j = 1; j < spl.length; j++) {
				if (j != spl.length - 1) {
					val = val + spl[j] + "=";
				} else {
					val = val + spl[j];
				}

			}

			cookies[i][0] = var;
			cookies[i][1] = val;

		}

		makers = getmakerlist.get();
		createmakerdirectories.create(makers);
		for (int i = 0; i < makers.length; i++) {
			phones = null;
			phones = getphoneslist.get(makers[i][1]);
			for (int j = 0; j < phones.length; j++) {
				String path = makers[i][0] + "//";
				writephonedetails.write(path, phones[j][0], phones[j][1],
						cookies);
			}
		}
	}
}