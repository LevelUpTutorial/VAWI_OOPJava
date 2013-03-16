/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.dienst;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.vawi.oopjava.kantinenplaner.dienste.GerichtDienst;
import de.vawi.oopjava.kantinenplaner.model.Gericht;

/**
 * @author Kim Michael Jansen
 */
public class GerichtDienstHitlisteTest {
	private static final String HITLISTE = "testdaten/hitliste_ok.csv";

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.dienste.GerichtDienst#parseHitlistenDatei(java.lang.String, java.util.List)}.
	 * @throws IOException 
	 */
	@Test
	public void testParseHitlistenDatei() throws IOException {
		List<Gericht> gerichte = new ArrayList<Gericht>();
		Gericht bohneneintopf = new Gericht("Bohneneintopf Mexiko");
		Gericht seelachs = new Gericht("Seelachs mit Kartoffel-Knusperkruste");
		Gericht nudelsuppe = new Gericht("Nudelsuppe");
		Gericht nichtInHitliste = new Gericht("NichtInHitliste");
		gerichte.add(bohneneintopf);
		gerichte.add(seelachs);
		gerichte.add(nudelsuppe);
		gerichte.add(nichtInHitliste);
		GerichtDienst.parseHitlistenDatei(HITLISTE, gerichte);
		
		assertTrue(bohneneintopf.getPrioritaet() == 1);
		assertTrue(seelachs.getPrioritaet() == 3);
		assertTrue(nudelsuppe.getPrioritaet() == 4);
		assertTrue(nichtInHitliste.getPrioritaet() == GerichtDienst.PRIO_MAX);
	}

}
