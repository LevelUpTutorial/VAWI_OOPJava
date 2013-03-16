/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.dienste;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Haendler;
import de.vawi.oopjava.kantinenplaner.model.ZuoHaendlerZutat;
import de.vawi.oopjava.kantinenplaner.model.Zutat;
import de.vawi.oopjava.kantinenplaner.model.ZutatTyp;
import de.vawi.oopjava.kantinenplaner.tools.TransportKostenRechner;
import de.vawi.oopjava.kantinenplaner.tools.impl.CSVReader;
import de.vawi.oopjava.kantinenplaner.tools.impl.TKProArtikel;
import de.vawi.oopjava.kantinenplaner.tools.impl.TKProKilometer;

/**
 * Dienstklasse für {@link Haendler} <br>
 * <br>
 * Infos zu Designentscheidungen: <br>
 * "Enforce noninstantiability with a private constructor" (Item 4, Effective
 * Java 2nd Edition)
 * 
 * @author Kim Michael Jansen
 * @version 1.8
 */
public final class HaendlerDienst {
	private static final Logger logger = LogManager.getLogger(HaendlerDienst.class);
	/*
	 * Konstanten, um Händlertypen zu identifizieren. Hieraus folgt die
	 * Zuordnung der Transportkostenstrategie
	 */
	private static final String GROSSHAENDLER = "Grosshandel";
	private static final String LOKALERHAENDLER = "Bauer";

	/**
	 * Prüft ob <code>zutat</code> von mindestens einem Händler erworben werden
	 * kann.
	 * 
	 * @param zutat String Der eindeutige Name der Zutat.
	 * @return Gibt <code>true</code> zurück, wenn mindestens ein Händler diese Zutat verkauft.
	 * @see Zutat
	 */
	public static boolean isZutatImAngebot(String zutat) {
		for (Haendler h : Haendler.getAllHaendler()) {
			if (h.getPreisProEinheit(zutat) > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liest die <code>haendlerDatei</code> und legt den entsprechen Händler an.
	 * 
	 * @param haendlerDatei String Der relative oder absolute Dateipfad.
	 * @throws FileNotFoundException Wenn die Datei nicht gefunden wurde.
	 * @throws IOException Wenn Fehler beim Lesen der Datei auftraten, die nicht
	 *             korrigiert werden konnten (z.B. wenn kein lesbarer Händler
	 *             enthalten war). Die Methode versucht fehlerhafte Zeilen zu
	 *             überlesen und loggt einen entsprechenden Hinweis.
	 */
	public static void parseHaendlerDatei(String haendlerDatei) throws IOException {
		CSVReader reader = new CSVReader();
		Scanner in = new Scanner(new FileInputStream(haendlerDatei));
		if (in.hasNext()) {
			String nextLine = in.nextLine();
			List<String> zellen = reader.readLine(nextLine);
			int size = zellen.size();
			// Prüfung, ob die Anzahl der Zellen plausibel ist.
			if (size == 5 || size == 6) {
				String art = zellen.get(0);
				String name = zellen.get(1);
				float tkBasis = Float.parseFloat(zellen.get(2));
				Haendler haendler = Haendler.getHaendler(name);
				if (haendler == null) {
					TransportKostenRechner tkRechner;
					if (art.equalsIgnoreCase(LOKALERHAENDLER)) {
						tkRechner = new TKProKilometer();
					} else if (art.equalsIgnoreCase(GROSSHAENDLER)) {
						tkRechner = new TKProArtikel();
					} else {
						logger.warn("Haendler konnte nicht erstellt werden, weil die Art (" + art + ") nicht zugeordnet werden konnte.");
						in.close();
						throw new IllegalArgumentException("Konnte den Schluessel (" + art + ") keiner Lieferantenart zuweisen!");
					}
					haendler = Haendler.newHaendler(name, tkBasis, tkRechner);
				} else {
					logger.warn("Haendler (" + name + ") ist bereits bekannt. Er wird nicht neu erstellt!");
				}
				parseZutaten(reader, in, haendler);
			} else {
				logZellen(zellen, size);
			}
		} else {
			logger.error("Die Preisliste (" + haendlerDatei + ") ist leer!");
			in.close();
			throw new IllegalArgumentException("Die Preisliste (" + haendlerDatei + ") ist leer!");
		}
		in.close();
	}

	private static void parseZutaten(CSVReader reader, Scanner in, Haendler haendler) {
		while (in.hasNext()) {
			String nextLine = in.nextLine();
			List<String> zellen = reader.readLine(nextLine);
			int size = zellen.size();
			if (size == 6) {
				int gebindeMenge = Integer.parseInt(zellen.get(0));
				String einheit = zellen.get(1);
				String zutatName = zellen.get(2);
				ZutatTyp typ = ZutatTyp.getZutatTypByChar(zellen.get(3));
				float preisProGebinde = Float.parseFloat(zellen.get(4));
				int gebindeMax = Integer.parseInt(zellen.get(5));
				Zutat zutat = getZutat(einheit, zutatName, typ);
				ZuoHaendlerZutat zuoHaendlerZutat = new ZuoHaendlerZutat(preisProGebinde, gebindeMenge, gebindeMax, zutat);
				haendler.addZuoHaendlerZutat(zuoHaendlerZutat);
			} else {
				logZellen(zellen, size);
			}
		}
	}

	private static Zutat getZutat(String einheit, String zutatName, ZutatTyp typ) {
		Zutat zutat = Zutat.getZutat(zutatName);
		if (zutat == null) {
			zutat = Zutat.newZutat(zutatName, typ, einheit);
		}
		return zutat;
	}

	private static void logZellen(List<String> zellen, int size) {
		logger.warn("Eingabedaten fuer Preisliste nicht plausibel:);");
		for (int i = 0; i < size; i++) {
			logger.warn("zellen[" + i + "] = " + zellen.get(i));
		}
	}

	/**
	 * Prüft die <code>gerichteListe</code> darauf, ob alle enthaltenen Zutat
	 * erworben werden können. Gerichte, deren Zutaten nicht oder nicht in
	 * ausreichender Menge käuflich sind, werden herausgefiltert.
	 * 
	 * @param gerichteListe
	 *            List&lt;Gericht&gt; Die zu prüfenden Gerichte.
	 * @param anzahl int Die Anzahl Mitarbeiter, für die eingekauft werden muss.
	 * @return List&lt;Gericht&gt; Gibt eine Liste aller ausreichend kaufbaren Gerichte aus
	 *         <code>gerichteListe</code> zurück.
	 */
	public static List<Gericht> getKaufbareGerichte(List<Gericht> gerichteListe, int anzahl) {
		List<Gericht> copy = new ArrayList<Gericht>(gerichteListe);
		// Gerichte mit nicht kaufbaren Zutaten filtern
		Iterator<Gericht> iter = copy.iterator();
		while (iter.hasNext()) {
			Gericht next = iter.next();
			for (Zutat z : next.getZutatenMengenMap().keySet()) {
				if (!isZutatImAngebot(z.getName())) {
					logger.trace("Die Zutat " + z.getName() + " wird benoetigt, aber nicht verkauft. Das Gericht kann nicht gekocht werden.");
					iter.remove();
				}
			}
		}
		// Verfügbare Menge pro Zutat ermittlen
		Map<Zutat, Float> verfuegbareZutatenMengenMap = ermittleVerfZutatenMengen();
		GerichtDienst.sort(copy);
		logger.debug("Beginne die Ermittlung kaufbarer Gerichte mit " + copy.size() + " Gerichten");
		return getVerfGerichte(verfuegbareZutatenMengenMap, copy, anzahl);
	}

	private static List<Gericht> getVerfGerichte(Map<Zutat, Float> verfuegbareZutatenMengenMap, List<Gericht> gerichteListe, int anzahl) {
		List<Gericht> copy = new ArrayList<Gericht>(gerichteListe);
		/*
		 * Menge pro Zutat ermitteln Da in diesem Schritt noch kein Speisplan
		 * erstellt wurde, muss jedes Gericht in maximaler Menge verfügbar sein.
		 * 
		 * @see PlanerDienst.ermittleMengeProZutat
		 */
		Map<Zutat, Float> zutatMengenMap = PlanerDienst.ermittleMengeProZutat(copy, anzahl);
		// Prüfen ob alle Zutaten verfuegbar sind
		Zutat unZutat = sindZutatenVerfuegbar(verfuegbareZutatenMengenMap, zutatMengenMap);
		if (unZutat == null) {
			return copy;
		} else {
			int size = copy.size();
			if (size > 0) {
				/*
				 * Rekursiver Aufruf mit redudzierten Gerichten, dabei wird das
				 * Gericht, dass die unzureichende Zutat enthält entfernt. Wenn
				 * diese in mehreren Gerichten enthalten ist, wird das Gericht
				 * mit der niedrigsten Priorität entfernt.
				 */
				for (int i = size - 1; i >= 0; i--) {
					Gericht g = copy.get(i);
					if (g.getZutatenMengenMap().keySet().contains(unZutat)) {
						logger.debug("Gericht " + g.getName() + " wird wegen zu geringem Angebot gestrichen.");
						copy.remove(g);
						// Austrittsbedingung immer erfüllt
						i = -1;
					}
				}

				return getVerfGerichte(verfuegbareZutatenMengenMap, copy, anzahl);
			}
			return new ArrayList<Gericht>();
		}
	}

	private static Map<Zutat, Float> ermittleVerfZutatenMengen() {
		Map<Zutat, Float> verfuegbareZutatenMengenMap = new HashMap<Zutat, Float>();
		for (Haendler h : Haendler.getAllHaendler()) {
			for (ZuoHaendlerZutat zuo : h.getZuoHaendlerZutatListe()) {
				Zutat z = zuo.getZutat();
				float menge = (verfuegbareZutatenMengenMap.containsKey(z) ? verfuegbareZutatenMengenMap.get(z) : 0);
				menge = menge + zuo.getMaximaleGebinde() * zuo.getMengeProGebinde();
				verfuegbareZutatenMengenMap.put(z, menge);
			}
		}
		return verfuegbareZutatenMengenMap;
	}

	private static Zutat sindZutatenVerfuegbar(Map<Zutat, Float> verfZutaten, Map<Zutat, Float> benZutaten) {
		for (Zutat z : benZutaten.keySet()) {
			Float benMenge = benZutaten.get(z);
			Float verfMenge = verfZutaten.get(z);
			if (verfMenge < benMenge) {
				logger.trace("Zutat \"" + z.getName() + "\" benoetigte Menge (" + benMenge + "), aber sind verfuegbar nur (" + verfMenge + ").");
				return z;
			}
		}
		return null;
	}

	/**
	 * Privater Konstruktor. Dienstklasse ist zustandslos (stateless) und soll
	 * nicht instanziiert werden können.
	 */
	private HaendlerDienst() {
	}
}
