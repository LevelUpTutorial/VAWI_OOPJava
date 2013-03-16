/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.dienst;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vawi.oopjava.kantinenplaner.dienste.GerichtDienst;
import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Zutat;
import de.vawi.oopjava.kantinenplaner.model.ZutatTyp;

/**
 * @author Kim Michael Jansen
 */
public class GerichtDienstRezepteTest {
	private static final String REZEPTE = "testdaten/rezepte_mixed.csv";
	
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
		Field zutatInstanzen = Zutat.class.getDeclaredField("zutatInstanzen");
		zutatInstanzen.setAccessible(true);
		zutatInstanzen.set(null, new ArrayList<Zutat>());
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.GerichtDienst#parseBekannteGerichteDatei(java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testParseBekannteGerichteDatei() throws IOException {
		Zutat.newZutat("Kartoffeln", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Ei", ZutatTyp.VEGETARISCH, "");
		Zutat.newZutat("Butter", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Mehl", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Salz", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Zucker", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Piment", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Lorbeerblatt", ZutatTyp.VEGETARISCH, "");
		Zutat.newZutat("Pfeffer", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Senf", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Milch", ZutatTyp.VEGETARISCH, "l");
		Zutat.newZutat("Pizzateig vorgebacken", ZutatTyp.VEGETARISCH, "");
		Zutat.newZutat("Analogkaese", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Italienische Gewuerzmischung", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Passierte Tomaten", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Backpulver", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Vanillinzucker", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Apfelmus", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Kirschen", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Dosenananas", ZutatTyp.VEGETARISCH, "g");
		Zutat.newZutat("Kochschinken", ZutatTyp.VEGETARISCH, "g");
		
		List<Gericht> gerichte = GerichtDienst.parseBekannteGerichteDatei(REZEPTE);
		int size = gerichte.size();
		int expectedSize = 4;
		assertTrue("Groesse war "+size+" , erwartet war "+expectedSize, size == expectedSize);
		assertTrue(gerichte.get(0).getZutatenMengenMap().size() == 11);
		assertTrue(gerichte.get(1).getZutatenMengenMap().size() == 7);
		assertTrue(gerichte.get(2).getZutatenMengenMap().size() == 8);
		assertTrue(gerichte.get(3).getZutatenMengenMap().size() == 8);
	}

}
