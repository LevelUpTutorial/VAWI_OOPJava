/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.dienst;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst;
import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Haendler;
import de.vawi.oopjava.kantinenplaner.model.ZuoHaendlerZutat;
import de.vawi.oopjava.kantinenplaner.model.Zutat;
import de.vawi.oopjava.kantinenplaner.model.ZutatTyp;
import de.vawi.oopjava.kantinenplaner.tools.impl.TKProArtikel;
import de.vawi.oopjava.kantinenplaner.tools.impl.TKProKilometer;

/**
 * @author Kim Michael Jansen
 */
public class HaendlerDienstKaufbarFilterTest {

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
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#isZutatImAngebot(java.lang.String)}.
	 */
	@Test
	public void testIsZutatImAngebot() {
		Haendler testHaendler = Haendler.newHaendler("testLokal1", 100f, new TKProKilometer());
		Haendler testHaendler2 = Haendler.newHaendler("testGH1", 100f, new TKProArtikel());
		String testZutat = "Kiwi";
		String testZutat2 = "Rind";
		Zutat kiwi = Zutat.newZutat(testZutat, ZutatTyp.VEGETARISCH, "g");
		Zutat rind = Zutat.newZutat(testZutat2, ZutatTyp.FLEISCH, "");
		testHaendler.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.89f, 100, 1000, kiwi));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.89f, 100, 1000, kiwi));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(6.67f, 1, 1000, rind));
		
		assertTrue(HaendlerDienst.isZutatImAngebot(testZutat));
		assertTrue(HaendlerDienst.isZutatImAngebot(testZutat2));
		assertFalse(HaendlerDienst.isZutatImAngebot("NichtImAngebot"));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#isZutatImAngebot(java.lang.String)}.
	 */
	@Test
	public void isZutatImAngebotNoEntries() {
		Haendler.newHaendler("testLokal1", 100f, new TKProKilometer());
		assertFalse(HaendlerDienst.isZutatImAngebot("NichtImAngebot"));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#isZutatImAngebot(java.lang.String)}.
	 */
	@Test
	public void isZutatImAngebotNoEntries2() {
		assertFalse(HaendlerDienst.isZutatImAngebot("NichtImAngebot"));
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#getKaufbareGerichte(java.util.List, int)}.
	 */
	@Test
	public void testGetKaufbareGerichteAusreichend() {
		Haendler testLokal = Haendler.newHaendler("testLokal1", 100f, new TKProKilometer());
		Haendler testHaendler2 = Haendler.newHaendler("testGH1", 100f, new TKProArtikel());
		Zutat zwiebeln = Zutat.newZutat("Gemuesezwiebeln", ZutatTyp.VEGETARISCH, "g");
		Zutat oel = Zutat.newZutat("Oel", ZutatTyp.VEGETARISCH, "l");
		Zutat rind = Zutat.newZutat("Rinderhack", ZutatTyp.FLEISCH, "g");
		Zutat kartoffeln = Zutat.newZutat("Kartoffeln", ZutatTyp.VEGETARISCH, "g");
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.89f, 200, 1000, zwiebeln));
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.99f, 250, 1000, oel));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(6.89f, 125, 1000, rind));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(1.29f, 2000, 1000, kartoffeln));
		
		Gericht haegges = new Gericht("Haegges");
		haegges.putZutatMenge(zwiebeln, 0.25f);
		haegges.putZutatMenge(oel, 0.75f);
		haegges.putZutatMenge(rind, 125.0f);
		haegges.putZutatMenge(kartoffeln,125.0f);
		
		List<Gericht> kaufbareGerichte = HaendlerDienst.getKaufbareGerichte(Arrays.asList(new Gericht[]{haegges}), 4);
		
		assertTrue(kaufbareGerichte.contains(haegges));
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#getKaufbareGerichte(java.util.List, int)}.
	 */
	@Test
	public void testGetKaufbareGerichteZuWenigKaufbar() {
		Haendler testLokal = Haendler.newHaendler("testLokal1", 100f, new TKProKilometer());
		Haendler testHaendler2 = Haendler.newHaendler("testGH1", 100f, new TKProArtikel());
		Zutat zwiebeln = Zutat.newZutat("Gemuesezwiebeln", ZutatTyp.VEGETARISCH, "g");
		Zutat oel = Zutat.newZutat("Oel", ZutatTyp.VEGETARISCH, "l");
		Zutat rind = Zutat.newZutat("Rinderhack", ZutatTyp.FLEISCH, "g");
		Zutat kartoffeln = Zutat.newZutat("Kartoffeln", ZutatTyp.VEGETARISCH, "g");
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.89f, 200, 1, zwiebeln));
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.99f, 250, 1, oel));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(6.89f, 125, 1, rind));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(1.29f, 2000, 1, kartoffeln));
		
		Gericht haegges = new Gericht("Haegges");
		haegges.putZutatMenge(zwiebeln, 0.25f);
		haegges.putZutatMenge(oel, 0.75f);
		haegges.putZutatMenge(rind, 125.0f);
		haegges.putZutatMenge(kartoffeln,125.0f);
		
		List<Gericht> kaufbareGerichte = HaendlerDienst.getKaufbareGerichte(Arrays.asList(new Gericht[]{haegges}), 4);
		
		assertFalse(kaufbareGerichte.contains(haegges));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#getKaufbareGerichte(java.util.List, int)}.
	 */
	@Test
	public void testGetKaufbareGerichteVerteiltAusreichend() {
		Haendler testLokal = Haendler.newHaendler("testLokal1", 100f, new TKProKilometer());
		Haendler testHaendler2 = Haendler.newHaendler("testGH1", 100f, new TKProArtikel());
		Zutat zwiebeln = Zutat.newZutat("Gemuesezwiebeln", ZutatTyp.VEGETARISCH, "g");
		Zutat oel = Zutat.newZutat("Oel", ZutatTyp.VEGETARISCH, "l");
		Zutat rind = Zutat.newZutat("Rinderhack", ZutatTyp.FLEISCH, "g");
		Zutat kartoffeln = Zutat.newZutat("Kartoffeln", ZutatTyp.VEGETARISCH, "g");
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.89f, 200, 1000, zwiebeln));
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.99f, 250, 1000, oel));
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(6.89f, 125, 2, rind));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(6.89f, 125, 2, rind));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(1.29f, 2000, 1000, kartoffeln));
		
		Gericht haegges = new Gericht("Haegges");
		haegges.putZutatMenge(zwiebeln, 0.25f);
		haegges.putZutatMenge(oel, 0.75f);
		haegges.putZutatMenge(rind, 125.0f);
		haegges.putZutatMenge(kartoffeln,125.0f);
		
		List<Gericht> kaufbareGerichte = HaendlerDienst.getKaufbareGerichte(Arrays.asList(new Gericht[]{haegges}), 4);
		
		assertTrue(kaufbareGerichte.contains(haegges));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst#getKaufbareGerichte(java.util.List, int)}.
	 */
	@Test
	public void testGetKaufbareGerichteUebergebeneListeUnveraendert() {
		Haendler testLokal = Haendler.newHaendler("testLokal1", 100f, new TKProKilometer());
		Haendler testHaendler2 = Haendler.newHaendler("testGH1", 100f, new TKProArtikel());
		Zutat zwiebeln = Zutat.newZutat("Gemuesezwiebeln", ZutatTyp.VEGETARISCH, "g");
		Zutat oel = Zutat.newZutat("Oel", ZutatTyp.VEGETARISCH, "l");
		Zutat rind = Zutat.newZutat("Rinderhack", ZutatTyp.FLEISCH, "g");
		Zutat kartoffeln = Zutat.newZutat("Kartoffeln", ZutatTyp.VEGETARISCH, "g");
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.89f, 200, 1000, zwiebeln));
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(0.99f, 250, 1000, oel));
		testLokal.addZuoHaendlerZutat(new ZuoHaendlerZutat(6.89f, 125, 2, rind));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(6.89f, 125, 2, rind));
		testHaendler2.addZuoHaendlerZutat(new ZuoHaendlerZutat(1.29f, 2000, 1000, kartoffeln));
		
		Gericht haegges = new Gericht("Haegges");
		haegges.putZutatMenge(zwiebeln, 0.25f);
		haegges.putZutatMenge(oel, 0.75f);
		haegges.putZutatMenge(rind, 125.0f);
		haegges.putZutatMenge(kartoffeln,125.0f);
		
		List<Gericht> originList = Arrays.asList(new Gericht[]{haegges});
		List<Gericht> kaufbareGerichte = HaendlerDienst.getKaufbareGerichte(originList, 4000);
		
		assertFalse(kaufbareGerichte.contains(haegges));
		assertTrue(originList.contains(haegges));
	}
}
