/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reader für "comma separated values" (csv).
 * @author Kim
 * @version 1.0
 */
public class CSVReader {
	private static final String DECIMAL_SEPARATOR = ".";
	private static final String DECIMAL_DELIMITER = "\"";
	private static final String CSV_DELIMITER = ",";

	/**
	 * Liest einen Text, der durch Komma ( , ) getrennt Werte enthält. 
	 * In Hochkomma ("") eingeschlossene Kommas ("text,text") werden in Punkte (text.text) konvertiert.
	 * <br><br><b>Hinweis: Beginnende "," und endende "," werden überlesen!</b> 
	 * @param line String Der zu trennende Text
	 * @return List&lt;String&gt; Gibt die gelesenen Zellen als Liste zurück.
	 */
	public List<String> readLine(String line) {
		List<String> zellen = new ArrayList<String>();
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(CSV_DELIMITER);
		while (scanner.hasNext()) {
			String next = scanner.next();
			if (next.contains(DECIMAL_DELIMITER)) {
				while (!next.endsWith(DECIMAL_DELIMITER) || next.equals(DECIMAL_DELIMITER)) {					
					next = next + DECIMAL_SEPARATOR + scanner.next();
				}
				next = next.replace(DECIMAL_DELIMITER, "");
			}
			zellen.add(next);
		}
		scanner.close();
		return zellen;
	}
}
