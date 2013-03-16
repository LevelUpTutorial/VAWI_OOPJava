package de.vawi.oopjava.kantinenplaner.tools.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.vawi.oopjava.kantinenplaner.tools.impl.CSVReader;

/**
 * @author Kim
 */
public class CVSReaderTest {
	private CSVReader reader;
	
	@Before
	public void init() {
		reader = new CSVReader();
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CSVReader#readLine(String)}.
	 */
	@Test
	public void readLineValidNoQuotes() {
		String stringAtNull = "1";
		String stringAtOne = "2";
		String stringAtEnd = "hallo du!";
		List<String> antwort = reader.readLine(stringAtNull+","+stringAtOne+",3,4,hallo welt,"+stringAtEnd);
		int size = antwort.size();
		int expectedSize = 6;
		String resultNull = antwort.get(0);
		String resultOne = antwort.get(1);
		String resultEnd = antwort.get(size - 1);
		assertTrue("Groesse war "+size+" , erwartet war "+expectedSize, size == expectedSize);
		assertTrue("Zelle war "+resultNull+" , erwartet war "+stringAtNull, resultNull.equals(stringAtNull));
		assertTrue("Zelle war "+resultOne+" , erwartet war "+stringAtOne, resultOne.equals(stringAtOne));
		assertTrue("Zelle war "+resultEnd+" , erwartet war "+stringAtOne, resultEnd.equals(stringAtEnd));
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CSVReader#readLine(String)}.
	 */
	@Test
	public void readLineValidWithQuotes() {
		String stringAtNull = "\"1,5\"";
		String stringAtOne = "\"2,abc\"";
		String stringAtEnd = "\"hallo,du!\"";
		List<String> antwort = reader.readLine(stringAtNull+","+stringAtOne+",3,4,hallo welt,"+stringAtEnd);
		int size = antwort.size();
		int expectedSize = 6;
		stringAtNull = stringAtNull.replace("\"", "");
		stringAtOne = stringAtOne.replace("\"", "");
		stringAtEnd = stringAtEnd.replace("\"", "");
		String resultNull = antwort.get(0);
		String resultOne = antwort.get(1);
		String resultEnd = antwort.get(size - 1);
		assertTrue("Groesse war "+size+" , erwartet war "+expectedSize, size == expectedSize);
		assertTrue("Zelle war "+resultNull+" , erwartet war "+stringAtNull, resultNull.equals(stringAtNull.replace(",", ".")));
		assertTrue("Zelle war "+resultOne+" , erwartet war "+stringAtOne, resultOne.equals(stringAtOne.replace(",", ".")));
		assertTrue("Zelle war "+resultEnd+" , erwartet war "+stringAtOne, resultEnd.equals(stringAtEnd.replace(",", ".")));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CSVReader#readLine(String)}.
	 */
	@Test
	public void readLineValidWithMultipleQuotes() {
		String stringAtNull = "\"1,,,,5\"";
		String stringAtOne = "\"2,a,b,c\"";
		String stringAtEnd = "\",,,h,all,o,du,!,,,\"";
		List<String> antwort = reader.readLine(stringAtNull+","+stringAtOne+",3,4,hallo welt,"+stringAtEnd);
		int size = antwort.size();
		int expectedSize = 6;
		stringAtNull = stringAtNull.replace("\"", "");
		stringAtOne = stringAtOne.replace("\"", "");
		stringAtEnd = stringAtEnd.replace("\"", "");
		String resultNull = antwort.get(0);
		String resultOne = antwort.get(1);
		String resultEnd = antwort.get(size - 1);
		assertTrue("Groesse war "+size+" , erwartet war "+expectedSize, size == expectedSize);
		assertTrue("Zelle war "+resultNull+" , erwartet war "+stringAtNull, resultNull.equals(stringAtNull.replace(",", ".")));
		assertTrue("Zelle war "+resultOne+" , erwartet war "+stringAtOne, resultOne.equals(stringAtOne.replace(",", ".")));
		assertTrue("Zelle war "+resultEnd+" , erwartet war "+stringAtOne, resultEnd.equals(stringAtEnd.replace(",", ".")));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CSVReader#readLine(String)}.
	 */
	@Test
	public void readLineWithHoles() {
		List<String> antwort = reader.readLine("3,4,hallo welt,,\",,,\"");
		int size = antwort.size();
		int expectedSize = 5;
		assertTrue("Erwartete Groesse ist "+expectedSize+" aber tatsaechliche Grosse war "+size, size == expectedSize);
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CSVReader#readLine(String)}.
	 */
	@Test
	@Ignore("Beginnende und endende ',' werden überlesen")
	public void readLineWithStartingAndEndingHoles() {
		List<String> antwort = reader.readLine(",3,4,hallo welt,,\",,,\",");
		int size = antwort.size();
		int expectedSize = 7;
		assertTrue("Erwartete Groesse ist "+expectedSize+" aber tatsaechliche Grosse war "+size, size == expectedSize);
	}
}
