/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.dienst;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vawi.oopjava.kantinenplaner.dienste.GerichtDienst;
import de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst;
import de.vawi.oopjava.kantinenplaner.dienste.PlanerDienst;
import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Haendler;
import de.vawi.oopjava.kantinenplaner.model.Kantine;
import de.vawi.oopjava.kantinenplaner.model.SpeiseplanPos;
import de.vawi.oopjava.kantinenplaner.model.ZuoHaendlerZutat;
import de.vawi.oopjava.kantinenplaner.model.Zutat;

/**
 * @author Kim Michael Jansen
 */
public class PlanerDienstSpeiseplanTest {
	private static final Logger logger = LogManager.getLogger(PlanerDienstSpeiseplanTest.class);
	private static final String PREISLISTE1 = "testdaten/beispieldateien/preisliste_1.csv";
	private static final String PREISLISTE2 = "testdaten/beispieldateien/preisliste_2.csv";
	private static final String PREISLISTE3 = "testdaten/beispieldateien/preisliste_3.csv";
	private static final String PREISLISTE4 = "testdaten/beispieldateien/preisliste_4.csv";
	private static final String PREISLISTE5 = "testdaten/beispieldateien/preisliste_5.csv";
	private static final String PREISLISTE6 = "testdaten/beispieldateien/preisliste_6.csv";
	private static final String PREISLISTE7 = "testdaten/beispieldateien/preisliste_7.csv";
	private static final String REZEPTE = "testdaten/beispieldateien/rezepte.csv";
	private static final DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	/**
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Before
	@After
	public void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field hInstanceField = Haendler.class.getDeclaredField("haendlerInstanzen");
		hInstanceField.setAccessible(true);
		@SuppressWarnings("unchecked")
		List<Haendler> haendlerInstanzenListe = (List<Haendler>) hInstanceField.get(null);
		haendlerInstanzenListe.clear();
		
		Field zuoField = Haendler.class.getDeclaredField("zuoHaendlerZutatListe");
		zuoField.setAccessible(true);
		for (Haendler h : Haendler.getAllHaendler()) {			
			zuoField.set(h, new ArrayList<ZuoHaendlerZutat>());
		}
		
		Field zutatField = Zutat.class.getDeclaredField("zutatInstanzen");
		zutatField.setAccessible(true);
		zutatField.set(null, new ArrayList<Zutat>());
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.PlanerDienst#erstelleSpeiseplan(java.util.Date, java.util.Date, java.util.List)}.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Test
	public void erstelleSpeiseplan() throws IOException, ParseException {
		HaendlerDienst.parseHaendlerDatei(PREISLISTE1);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE2);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE3);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE4);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE5);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE6);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE7);
		List<Gericht> gerichte = GerichtDienst.parseBekannteGerichteDatei(REZEPTE);
		Kantine k1 = new Kantine(300, "Test300");
		Kantine k2 = new Kantine(100, "Test100");
		List<Kantine> kantineListe = Arrays.asList(new Kantine[]{k1,k2});
		for (Kantine k : kantineListe) {
			k.setGerichteListe(gerichte);
		}
		
		PlanerDienst.erstelleSpeiseplan(sdf.parse("07.01.2013"), sdf.parse("20.01.2013"), kantineListe);
		
		for (Kantine k : kantineListe) {
			List<SpeiseplanPos> speiseplan = k.getSpeiseplan();
			if (logger.isDebugEnabled()) {
				logger.debug("Speiseplan ("+k.getName()+")");
				for (SpeiseplanPos sPos : speiseplan) {
					logger.debug("Datum "+sPos.getDatum());
					for (Gericht g : sPos.getGerichte()) {
						logger.debug("Gericht "+g.getName());
					}
				}
			}
			int size = speiseplan.size();
			int expectedSize = 10;
			assertTrue("Groesse ist "+size+" erwartete war "+expectedSize, size > expectedSize);
		}
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.PlanerDienst#erstelleSpeiseplan(java.util.Date, java.util.Date, java.util.List)}.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void erstelleSpeiseplanAngebotZuGeringFuerAnzahlMA() throws IOException, ParseException {
		HaendlerDienst.parseHaendlerDatei(PREISLISTE1);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE2);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE3);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE4);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE5);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE6);
		HaendlerDienst.parseHaendlerDatei(PREISLISTE7);
		List<Gericht> gerichte = GerichtDienst.parseBekannteGerichteDatei(REZEPTE);
		Kantine k1 = new Kantine(100000, "Test300");
		List<Kantine> kantineListe = Arrays.asList(new Kantine[]{k1});
		k1.setGerichteListe(gerichte);
		
		PlanerDienst.erstelleSpeiseplan(sdf.parse("07.01.2013"), sdf.parse("20.01.2013"), kantineListe);
		
		fail("IllegalArgumentException erwartet");
	}
}
