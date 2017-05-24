package MTM;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
	public static void main(String[] args) {
		// System.out.println(createBatData(getYesterdayDateString()));
		// createBatch(new
		// String[]{"D:\\Alexey\\TestEnvironment\\Esma\\Esmaquery.bat"});
		createBatch(args);
	}

	public static void createBatch(String[] args) {
		if (args.length == 2) {
			String yd = getYesterdayDateString();
			writeFile(args[0], createBatData(yd, args[1]));
		} else
			System.err.println("wrong usage");
	}

	static String createBatData(String date, String dest) {
		String[] block = {
				"curl -g -G \"https://registers.esma.europa.eu/solr/esma_registers_mifid_sha/select?q=({!parent%%20which=%%27type_s:parent%%27})&fq=((((sha_modificationDate:[*%%20TO%%20",
				"T23:59:59.000Z])%%20AND%%20(sha_modificationBDate:[",
				"T00:00:00.000Z%%20TO%%20*])%%20AND%%20!sha_status:Not\\%%20effective\\%%20yet)%%20OR%%20((sha_modificationDate:[*%%20TO%%20",
				"T23:59:59.000Z])%%20AND%%20(sha_modificationBDate:[NOW%%20TO%%20*])%%20AND%%20(sha_modificationDate:[*%%20TO%%20",
				"T23:59:59.000Z])%%20AND%%20(sha_modificationBDate:[",
				"T00:00:00.000Z%%20TO%%20*])%%20AND%%20sha_status:Not\\%%20effective\\%%20yet)))&wt=json&indent=true&rows=100000\" > "
						+ dest + "\\esmadata_",
				".txt" };
		StringBuilder sb = new StringBuilder(block[0]);
		for (int i = 1; i < block.length; i++) {
			sb.append(date);
			sb.append(block[i]);
		}

		return sb.toString();
	}

	private static Date yesterday() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	private static String getYesterdayDateString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(yesterday());
	}

	public static void writeFile(String file, String text) {
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println(text);
			writer.close();
		} catch (IOException e) {
			// do something
		}
	}
}
