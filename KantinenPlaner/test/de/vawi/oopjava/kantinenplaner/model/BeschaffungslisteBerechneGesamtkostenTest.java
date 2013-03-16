package de.vawi.oopjava.kantinenplaner.model;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import de.vawi.oopjava.kantinenplaner.tools.impl.TKProArtikel;

/**
 * @author Julia
 *
 */
public class BeschaffungslisteBerechneGesamtkostenTest {

		@Test
		@Ignore
		public void testBerechneGesamtkosten() throws IOException {
			// Before und After
			
			Zutat bratwurst = Zutat.newZutat("Bratwurst", ZutatTyp.getZutatTypByChar("m"), "");
			ZuoHaendlerZutat bratwurstPreis = new ZuoHaendlerZutat((float) 5.15, 100, 1000, bratwurst);
			
			Zutat forelle = Zutat.newZutat("Forelle", ZutatTyp.getZutatTypByChar("f"), "g");
			ZuoHaendlerZutat forellenPreis = new ZuoHaendlerZutat((float) 11.34, 1000, 1, forelle);
			
			Haendler metri = Haendler.newHaendler("Metri AG", 1, new TKProArtikel());
			metri.addZuoHaendlerZutat(forellenPreis);
			metri.addZuoHaendlerZutat(bratwurstPreis);
			
			HaendlerPos metriPos = new HaendlerPos(metri, bratwurst, 200);	
						
			Beschaffungsliste bliste = new Beschaffungsliste();
			bliste.addZutatPos(metri, forelle, 1000 );
			
			
			System.out.println("Gebinde"+metri.getPreisProGebinde(bratwurst.getName()));
			System.out.println("Einheit"+metri.getPreisProEinheit(bratwurst.getName()));
			System.out.println("Gebinde"+metri.getPreisProGebinde(forelle.getName()));
			System.out.println("Einheit"+metri.getPreisProEinheit(forelle.getName()));
			
			System.out.println("TK="+metriPos.getTransportkosten());
			System.out.println("AK="+metriPos.berechneAnschaffungskosten());			
			System.out.println("GK="+bliste.getGesamtkosten());
		
			assertTrue(metri.getPreisProGebinde(bratwurst.getName()) == 5.15f);
			assertTrue(metri.getPreisProEinheit(bratwurst.getName()) == 0.0515f);
			assertTrue(metri.getPreisProGebinde(forelle.getName()) == 11.34f);
			assertTrue(metri.getPreisProEinheit(forelle.getName()) == 0.01134f);
			
			assertTrue(metriPos.getTransportkosten() == 2.00f);
			assertTrue(metriPos.berechneAnschaffungskosten() == 23.64f);
			
			assertTrue(bliste.getGesamtkosten() == 23.64f);
		}
}
