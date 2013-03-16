/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.dienst;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst;
import de.vawi.oopjava.kantinenplaner.model.Haendler;
import de.vawi.oopjava.kantinenplaner.model.ZuoHaendlerZutat;
import de.vawi.oopjava.kantinenplaner.model.Zutat;
import de.vawi.oopjava.kantinenplaner.tools.impl.TKProArtikel;
import de.vawi.oopjava.kantinenplaner.tools.impl.TKProKilometer;

/**
 * @author Kim Michael Jansen
 */
public class HaendlerDienstPreislisteTest {
	private static final String PL_BAUER = "testdaten/preisliste_bauer.csv";
	private static final String PL_GROSSHAENDLER = "testdaten/preisliste_grosshaendler.csv";
	private static final String PL_GROSSHAENDLER2 = "testdaten/preisliste_grosshaendler2.csv";
	
	/**
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws java.lang.Exception
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
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#parseHaendlerDatei(String)}.
	 */
	@Test
	public void parsePreislisteBauer() throws IOException {
		HaendlerDienst.parseHaendlerDatei(PL_BAUER);
		Haendler h = Haendler.getHaendler("Anglerkollektiv Nord-West");
		List<ZuoHaendlerZutat> zuoHaendlerZutatListe = h.getZuoHaendlerZutatListe();

		assertTrue(h.getTransportKostenRechner() instanceof TKProKilometer);
		assertTrue(zuoHaendlerZutatListe.size() == 3);
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#parseHaendlerDatei(String)}.
	 */
	@Test
	public void parsePreislisteGrossHaendler() throws IOException {
		HaendlerDienst.parseHaendlerDatei(PL_GROSSHAENDLER);
		Haendler h = Haendler.getHaendler("Metri AG");
		List<ZuoHaendlerZutat> zuoHaendlerZutatListe = h.getZuoHaendlerZutatListe();

		assertTrue(h.getTransportKostenRechner() instanceof TKProArtikel);
		assertTrue(zuoHaendlerZutatListe.size() == 5);
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#parseHaendlerDatei(String)}.
	 */
	@Test
	public void parseMultiplePreislisten() throws IOException {
		HaendlerDienst.parseHaendlerDatei(PL_BAUER);
		HaendlerDienst.parseHaendlerDatei(PL_GROSSHAENDLER);
		HaendlerDienst.parseHaendlerDatei(PL_GROSSHAENDLER2);
		
		List<Haendler> allHaendler = Haendler.getAllHaendler();
		assertTrue(allHaendler.size() == 2);

		Haendler h = Haendler.getHaendler("Metri AG");
		List<ZuoHaendlerZutat> zuoHaendlerZutatListe = h.getZuoHaendlerZutatListe();
		int size = zuoHaendlerZutatListe.size();
		int expectedSize = 8;
		assertTrue("Groesse war "+size+" , erwartet wurde "+expectedSize, size == expectedSize);
	}
}
