/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.configuration;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import de.vawi.oopjava.kantinenplaner.model.Kantine;

/**
 * @author Kim Michael Jansen
 */
public class PropertiesUtilTest {
	private static final String DATUM_VON = "07.01.2013";
	private static final String DATUM_BIS = "20.01.2013";
	private static final int MA_K1 = 800;
	private static final int MA_K2 = 400;
	private static final String NAME_K1 = "Mühlheim";
	private static final String NAME_K2 = "Essen";
	private PropertiesUtil util;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		util = new PropertiesUtil();
		Properties prop = util.getProperties();
		prop.setProperty(PropertiesUtil.KEY_PREISLISTE, "testdaten/beispielsdateien/preisliste_1.csv,testdaten/beispielsdateien/preisliste_2.csv,testdaten/beispielsdateien/preisliste_3.csv,testdaten/beispielsdateien/preisliste_4.csv,testdaten/beispielsdateien/preisliste_5.csv,testdaten/beispielsdateien/preisliste_6.csv,testdaten/beispielsdateien/preisliste_7.csv");
		prop.setProperty(PropertiesUtil.KEY_REZEPTE, "testdaten/beispielsdateien/rezepte.csv");
		prop.setProperty(PropertiesUtil.KEY_HITLISTE, "testdaten/beispielsdateien/hitliste.csv");
		prop.setProperty(PropertiesUtil.KEY_KANTINE, NAME_K1 + "." + MA_K1 + "," + NAME_K2 + "." + MA_K2);
		prop.setProperty(PropertiesUtil.KEY_STARTDATUM, DATUM_VON);
		prop.setProperty(PropertiesUtil.KEY_ENDEDATUM, DATUM_BIS);
		prop.setProperty(PropertiesUtil.KEY_OUTPUT_DIR, "output/planung/");
		prop.setProperty(PropertiesUtil.KEY_OUTPUT_FORMAT, "TXT");
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil#getPreislisten()}.
	 */
	@Test
	public void testGetPreislisten() {
		String[] expecteds = new String[]{
				"testdaten/beispielsdateien/preisliste_1.csv",
				"testdaten/beispielsdateien/preisliste_2.csv",
				"testdaten/beispielsdateien/preisliste_3.csv",
				"testdaten/beispielsdateien/preisliste_4.csv",
				"testdaten/beispielsdateien/preisliste_5.csv",
				"testdaten/beispielsdateien/preisliste_6.csv",
				"testdaten/beispielsdateien/preisliste_7.csv"
		};
		List<String> value = util.getPreislisten();
		assertArrayEquals(expecteds, value.toArray());
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil#getKantinen()}.
	 */
	@Test
	public void testGetKantinen() {		
		List<Kantine> kantinen = util.getKantinen();
		
		Kantine k1 = kantinen.get(0);
		Kantine k2 = kantinen.get(1);
		assertEquals(MA_K1, k1.getAnzahlMitarbeiter());
		assertEquals(NAME_K1, k1.getName());
		assertEquals(MA_K2, k2.getAnzahlMitarbeiter());
		assertEquals(NAME_K2, k2.getName());
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil#getStartdatum()}.
	 * @throws ParseException 
	 */
	@Test
	public void testGetStartdatum() throws ParseException {
		Date date = util.getStartdatum();
		
		assertEquals(util.getSdf().parse(DATUM_VON), date);
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil#getEndedatum()}.
	 * @throws ParseException 
	 */
	@Test
	public void testGetEndedatum() throws ParseException {
		Date date = util.getEndedatum();
		
		assertEquals(util.getSdf().parse(DATUM_BIS), date);
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil#parseKantinenAsString(List)}.
	 * @throws ParseException 
	 */
	@Test
	public void testParseKantine() {
		int m1 = 100;
		String n1 = "K1";
		int m2 = 200;
		String n2 = "K2";
		String kantinenAsString = PropertiesUtil.parseKantinenAsString(Arrays.asList(new Kantine[]{new Kantine(m1,n1), new Kantine(m2, n2)}));
		assertEquals(n1+"."+m1+","+n2+"."+m2, kantinenAsString);
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil#speichereProperties(Properties, String)}.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Test
	public void testSpeichern() throws IOException {
		String path = "testdaten/tmp.properties";
		FileOutputStream out = new FileOutputStream(path);
		out.flush();
		out.close();
		Properties prop = new Properties();
		String key = PropertiesUtil.KEY_HITLISTE;
		String value = "testvalue";
		prop.put(key, value);
		PropertiesUtil.speichereProperties(prop, path);
		
		File file = new File(path);
		PropertiesUtil util = new PropertiesUtil(path);
		assertEquals(value, util.getHitliste());
		file.delete();
	}
}
