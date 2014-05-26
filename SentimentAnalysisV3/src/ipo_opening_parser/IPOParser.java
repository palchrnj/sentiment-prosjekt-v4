package ipo_opening_parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class IPOParser {
	
	public static void main(String[] args) throws Exception {
		System.out.println(getReturnOnFirstDayOfTrading("NATTO", "OAX"));
	}
	
	// Exchange is either OAX for Oslo Access or OSE for Oslo Stock Exchange 
	public static double getReturnOnFirstDayOfTrading(String ticker, String exchange) throws Exception {
		InputStream input = new URL("http://norma.netfonds.no/paperhistory.php?paper=+" + ticker + "." + exchange + "&csv_format=csv").openStream();
		Reader reader = new InputStreamReader(input, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader); 
		String cols[] = null;
		while (bufferedReader.ready()) {
			String line = bufferedReader.readLine();
			System.out.println(line);
			cols = line.split(",");
		}
//		for (int i = 0; i < cols.length; i++) {
//			System.out.print(cols[i] + " - ");
//		}
//		double opening = Double.parseDouble(cols[3]);
//		double closing = Double.parseDouble(cols[6]);
//		double ret = (closing - opening) / opening;
//		System.out.println("Return of ticker " + ticker + " first day of trading, " + cols[0] + ", on exchange " + exchange + " was: " + ret);
		return ret = 0;
	}
}