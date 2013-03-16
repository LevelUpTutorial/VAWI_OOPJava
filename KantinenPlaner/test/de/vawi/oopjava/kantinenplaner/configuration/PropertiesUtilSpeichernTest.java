package de.vawi.oopjava.kantinenplaner.configuration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

/**
 * @author Kim Michael Jansen
 */
public class PropertiesUtilSpeichernTest {

	/**
	 * Fehlerbericht von Julia, wonach die Dateipfade nach Speichern nicht mehr funktionieren
	 * "/" -> "\"
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testSpeichereProperties() throws FileNotFoundException, IOException {
		PropertiesUtil util = new PropertiesUtil();
		Properties prop = util.getProperties();
		File testFile = new File("testdaten/tmptest.cfg");
		PropertiesUtil.speichereProperties(prop, testFile.getAbsolutePath());
		PropertiesUtil util2 = new PropertiesUtil(testFile.getAbsolutePath());
		assertEquals(util.getHitliste(), util2.getHitliste());
		assertEquals(util.getOutputDir(), util2.getOutputDir());
		assertEquals(util.getRezepte(), util2.getRezepte());
		
		testFile.delete();
	}

}
