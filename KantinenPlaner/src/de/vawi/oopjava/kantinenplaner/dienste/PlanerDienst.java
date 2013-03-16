/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.dienste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vawi.oopjava.kantinenplaner.model.BeschaffungsPriorisierung;
import de.vawi.oopjava.kantinenplaner.model.Beschaffungsliste;
import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Haendler;
import de.vawi.oopjava.kantinenplaner.model.Kantine;
import de.vawi.oopjava.kantinenplaner.model.SpeiseplanPos;
import de.vawi.oopjava.kantinenplaner.model.ZuoHaendlerAnschKosten;
import de.vawi.oopjava.kantinenplaner.model.ZuoHaendlerZutat;
import de.vawi.oopjava.kantinenplaner.model.Zutat;
import de.vawi.oopjava.kantinenplaner.tools.impl.HitlistenComparator;

/**
 * In dieser Dienstklasse werden Algorithmen zur Planung ausgelagert.
 * <br><br>Infos zu Designentscheidungen:
 * <br>"Enforce noninstantiability with a private constructor" (Item 4, Effective Java 2nd Edition)
 * @author Kim Michael Jansen
 * @version 1.10
 */
public final class PlanerDienst {
	private static final int FAKTOR_ANDERE_GERICHTE = 1;
	private static final Logger logger = LogManager.getLogger(PlanerDienst.class);
	/** Anzahl der Gerichte, pro Tag */
	private static final int GERICHTE_PRO_TAG = 3;
	/** Faktor f�r das Verh�ltnis des beliebtesten Gerichts pro Tag zu anderen Gerichten */
	private static final float FAKTOR_HITLISTEN_GERICHT = 2;
	/** Faktor f�r Portionen pro Tag */
	private static final float FAKTOR_PORTIONEN_PRO_TAG = 1.5f;
	/** Methoden�bergreifender Merker */
	private static boolean hasFisch = false;
	/** Einkaufspriorisierung f�r Zutaten */
	private static List<BeschaffungsPriorisierung> beschPrioListe = new ArrayList<BeschaffungsPriorisierung>();
	
	/**
	 * Erstellt eine {@link Beschaffungsliste} f�r die Kantinen in <code>kantinenListe</code>
	 * @param kantinenListe List&lt;Kantine&gt; Die Liste der Kantinen, f�r die geplant werden soll.
	 * @return Beschaffungsliste Gibt die erstellte {@link Beschaffungsliste} zur�ck.
	 */
	public static Beschaffungsliste erstelleBeschaffungsliste(List<Kantine> kantinenListe) {
		Beschaffungsliste beschaffungsliste = new Beschaffungsliste();
		/*
		 *  Gesamtbedarf aller Kantinen ermittlen
		 *	@see PlanerDienst.ermittleMengeProZutat 
		 */
		Map<Zutat, Float> gesamtBedarf = ermittleGesamtbedarf(kantinenListe);
		// Erstellung der Beschaffungsliste
		for (Zutat z : gesamtBedarf.keySet()) {
			int bedarfZutat = (int) Math.ceil(gesamtBedarf.get(z));
			BeschaffungsPriorisierung bPrio = BeschaffungsPriorisierung.getBeschaffungsPriorisierung(beschPrioListe, z);
			List<Haendler> haendlerPrio = bPrio.getHaendlerPrio();
			int size = haendlerPrio.size();
			for (int i=0; i<size && bedarfZutat > 0; i++) {
				Haendler h = haendlerPrio.get(i);
				ZuoHaendlerZutat zuoHaendlerZutat = h.getZuoHaendlerZutat(z);
				int verkaufteMenge = zuoHaendlerZutat.getMaximaleGebinde() * zuoHaendlerZutat.getMengeProGebinde();
				int restVerfMenge = verkaufteMenge - bedarfZutat;
				/*
				* Wenn die verkaufte Menge ausreicht, um den Bedarf zu befriedigen, wird der gesamte Bedarf gekauft.
				* Ansonsten, wird soviel gekauft, wie verkauft wird. Im Rahmen der Schleife wird dann der Restbedarf bei
				* anderen H�ndler, die niedriger Priorisiert sind, befriedigt.
				*/
				int gekaufteMenge = (restVerfMenge >= 0 ? bedarfZutat : verkaufteMenge);
				logger.trace("Erstelle BeschaffungslistenPosition: Haendler("+h.getName()+"), Zutat("+z.getName()+"), Menge ("+gekaufteMenge+")");
				beschaffungsliste.addZutatPos(h, z, gekaufteMenge);
				bedarfZutat = bedarfZutat - gekaufteMenge;
			}
		}
		
		return beschaffungsliste;
	}

	private static Map<Zutat, Float> ermittleGesamtbedarf(List<Kantine> kantinenListe) {
		Map<Zutat, Float> gesamtBedarf = new HashMap<Zutat, Float>();
		for (Kantine k : kantinenListe) {
			// Bedarf der Kantine ermittlen
			Map<Zutat, Float> kantinenBedarf = ermittleMengeProZutat(k);
			
			// Kantinenbedarf mit dem Gesamtbedarf konsolidieren
			for (Zutat z : kantinenBedarf.keySet()) {
				if (gesamtBedarf.containsKey(z)) {
					gesamtBedarf.put(z, gesamtBedarf.get(z) + kantinenBedarf.get(z));
				}
				else {
					gesamtBedarf.put(z, kantinenBedarf.get(z));
				}
			}
		}
		
		return gesamtBedarf;
	}
	
	private static Map<Zutat, Float> ermittleMengeProZutat(Kantine kantine) {
		// Menge ermitteln, unter ber�cksichtigung des Speiseplans und der Mengenerh�hung f�r das beliebteste Tagesgericht
		List<SpeiseplanPos> speiseplan = kantine.getSpeiseplan();
		Map<Zutat, Float> kantinenBedarf = new HashMap<Zutat, Float>(speiseplan.size()*GERICHTE_PRO_TAG);
		int mitarbeiter = kantine.getAnzahlMitarbeiter();
		for (SpeiseplanPos pos : speiseplan) {
			/*
			 * Beliebtestes Tagesgericht finden.
			 * Das beliebteste Gericht soll h�ufiger gekauft werden
			 *@see PlanerDienst.FAKTOR_HITLISTEN_GERICHT
			 */
			List<Gericht> gerichte = pos.getGerichte();
			Collections.sort(gerichte, new HitlistenComparator());
			int size = gerichte.size();
			for (int i=0; i<size; i++) {
				// Menge pro Gericht ermittlen, da das beliebteste Tagesgericht h�ufiger gew�nscht wird
				Map<Zutat, Float> mengeByFaktor = new HashMap<Zutat, Float>(GERICHTE_PRO_TAG);
				Gericht g = gerichte.get(i);
				if (i==0) {
					float portionen = ermittleFaktorBeliebtesTagesgericht(mitarbeiter);
					logger.debug("Kalkuliere Tageshit mit "+portionen+" Potionen je Gericht.");
					mengeByFaktor = ermittleMengeByFaktor(Arrays.asList(new Gericht[]{g}), portionen);
				}
				else {
					float portionen = mitarbeiter * FAKTOR_PORTIONEN_PRO_TAG * (FAKTOR_ANDERE_GERICHTE/(GERICHTE_PRO_TAG+FAKTOR_HITLISTEN_GERICHT-1));
					logger.debug("Kalkuliere sonstiges Tagesgericht mit "+portionen+" Potionen je Gericht.");
					mengeByFaktor = ermittleMengeByFaktor(Arrays.asList(new Gericht[]{g}), portionen);
				}
				// Menge zu Gesamtmenge hinzuf�gen
				for (Zutat z : mengeByFaktor.keySet()) {
					float menge = (kantinenBedarf.containsKey(z) ? kantinenBedarf.get(z) : 0);
					menge = menge + mengeByFaktor.get(z);
					kantinenBedarf.put(z, menge);
				}
			}
		}
		return kantinenBedarf;
	}
	
	/**
	 * Ermittelt den Gesamtbedarf an Zutaten, die f�r die Gerichte und die Anzahl an Mitarbeitern ben�tigt werden.
	 * <br>Pro Tag soll die Anzahl der gekochten Gerichte 150% der Anzahl Mitarbeiter betragen
	 * <br>Da zu diesem Planungszeitpunkt keine Priorit�ten ber�cksichtigt werden k�nnen, muss jedes Gericht in der folgenden Menge vorliegen:
	 * <br>Anzahl Mitarbeiter (<code>anzahl</code>) * {@link PlanerDienst#FAKTOR_PORTIONEN_PRO_TAG} / {@link PlanerDienst#GERICHTE_PRO_TAG} = Portionen je Gericht
	 * <br>Z.B. 800 Mitarbeiter * 150% / 3 Gerichte pro Tag = 400 Protionen je Gericht
	 * @param gerichteListe List&lt;Gericht&gt; Die Gerichteliste.
	 * @param anzahl int Anzahl der Mitarbeiter
	 * @return Map&lt;Zutat, Float&gt; Eine Map �ber Zutaten (key) und die Menge (value).
	 */
	public static Map<Zutat, Float> ermittleMengeProZutat(List<Gericht> gerichteListe, int anzahl) {
		/*
		 *  Hitlistengerichte ber�cksichtigen, vgl Forum 2:1:1
		 *  Jedes Gericht muss zun�chst f�r 75% der Mitarbeiter zur Verf�gung stehen, da an dieser Stelle
		 *  noch nicht feststeht, welches Gericht beliebter ist. Der Speiseplan wird erst im zweiten Schritt erstellt.
		 *  Dies kann potentiell dazu f�hren, dass Gerichte, die neben einem beliebteren Gericht angeboten werden k�nnten
		 *  nicht eingeplant werden. Dies wird im Rahmen des Algorithmus als Unsch�rfe in Kauf genommen.
		 */
		float portionen = ermittleFaktorBeliebtesTagesgericht(anzahl);
		logger.debug("Kalkuliere "+portionen+" Potionen je Gericht, da noch kein Speiseplan vorliegt.");
		Map<Zutat, Float> zutatMengenMap = ermittleMengeByFaktor(gerichteListe, portionen);
		return zutatMengenMap;
	}

	private static float ermittleFaktorBeliebtesTagesgericht(int anzahl) {
		// Genaue Anzahl, da Rundung sich zu stark aufsummiert und ungenau wird.
		float tagesHitFaktor = anzahl * FAKTOR_PORTIONEN_PRO_TAG * (FAKTOR_HITLISTEN_GERICHT/(GERICHTE_PRO_TAG+FAKTOR_HITLISTEN_GERICHT-1));
		return tagesHitFaktor;
	}

	/**
	 * Ermittelt die ben�tigte Zutatenmenge anhand eines �bergebenen Faktors
	 * @param gerichteListe
	 * @param faktor
	 * @return
	 */
	private static Map<Zutat, Float> ermittleMengeByFaktor(List<Gericht> gerichteListe, float faktor) {
		Map<Zutat, Float> zutatMengenMap = new HashMap<Zutat, Float>();
		for (Gericht g : gerichteListe) {
			HashMap<Zutat, Float> gerichtZutaten = g.getZutatenMengenMap();
			for (Zutat z : gerichtZutaten.keySet()) {
				float menge = (zutatMengenMap.containsKey(z) ? zutatMengenMap.get(z) : 0);
				menge = menge + gerichtZutaten.get(z) * faktor;
				zutatMengenMap.put(z, menge);
			}
		}
		return zutatMengenMap;
	}
	
	/**
	 * Kalkuliert die Verkaufsmodalit�ten der H�ndler f�r die zu kochenden Gerichte und erzeugt eine BeschaffungsPriorisierung.
	 * @param rezepte List&lt;Gericht&gt; Die Rezepte, die gekocht werden sollen.
	 * @param maGesamt int Die Anzahl Mitarbeiter, f�r die gekocht wird, um die ben�tigte Zutatenmenge zu ermitteln.
	 * @return List&lt;BeschaffungsPriorisierung&gt; Gibt eine Liste der erstellten BeschaffungsPriorisierungen zur�ck.
	 */
	public static List<BeschaffungsPriorisierung> erstelleBeschaffungsPriorisierung(List<Gericht> rezepte, int maGesamt) {
		/*
		 * zutatenMengenMap f�r die Anzahl Mitarbeiter ermitteln.
		 * Da die Planung in mehrern Schritten erfolgt und hier die Tagesgerichte noch nicht feststehen, muss jedes Gericht
		 * in maximaler Menge kaufbar sein.
		 */
		Map<Zutat, Float> mengeProZutatMap = ermittleMengeProZutat(rezepte, maGesamt);
		
		List<BeschaffungsPriorisierung> beschPrio = ermittleBeschaffungsPriorisierung(mengeProZutatMap);
		return beschPrio;
	}
	
	/**
	 * Kalkuliert die Verkaufsmodalit�ten der H�ndler f�r die zu kochenden Gerichte und erzeugt eine BeschaffungsPriorisierung.
	 * @param kantinenListe List&lt;Kantine&gt; Die Rezepte, die gekocht werden sollen.
	 * @return List&lt;BeschaffungsPriorisierung&gt; Gibt eine Liste der erstellten BeschaffungsPriorisierungen zur�ck.
	 */
	public static List<BeschaffungsPriorisierung> erstelleBeschaffungsPriorisierung(List<Kantine> kantinenListe) {
		/*
		 * zutatenMengenMap f�r die Anzahl Mitarbeiter ermitteln.
		 * Aus den Kantinen kann der Speiseplan genutzt werden, um die Mengen exakt zu bestimmten.
		 * Dies schlie�t die ber�cksichtigung vom Verh�ltnis Hitgericht zu anderen Gerichten mit ein.
		 */
		Map<Zutat, Float> mengeProZutatMap = ermittleGesamtbedarf(kantinenListe);
		
		List<BeschaffungsPriorisierung> beschPrio = ermittleBeschaffungsPriorisierung(mengeProZutatMap);
		return beschPrio;
	}

	/**
	 * Strategie, beim Vergleich der Einkaufspreise der H�ndler und Ber�cksichtigung der verschiedenen Transportkostenermittlungen.
	 * Die Strategie ist allgemein gehalten. Dadurch kann ist zu Abweichungen bei der sp�teren Planung kommen, jedoch bleibt das
	 * System unabh�ngig von den bisher bekannten Transportkostenermittlungen und es k�nnten weitere Transportkosten aufgenommen werden.
	 * 
	 * Alle H�ndler durchgehen, und Anschaffungskosten inklusive anteiliger Transportkosten pro Zutat ermittlen
	 * Dabei wird unterstellt, dass alle ben�tigten Zutaten in der ben�tigten Menge, maximal die verkaufte Menge, abgenommen werden.
	 * Um die H�ndler untereinander verlgeichen zu k�nnen, werden die Anschaffungskosten pro Einheit ermittelt.
	 * Dass es bei der sp�teren Einkaufsplanung zu Verschiebungen aufgrund der Gebindemenge und den Maximalen Gebinden kommen kann, wird dabei in Kauf genommen.
	 * @param Map&lt;Zutat, Float&gt; mengeProZutatMap
	 * @return List&lt;BeschaffungsPriorisierung&gt; Gibt eine Liste der erstellten BeschaffungsPriorisierungen zur�ck.
	 */
	private static List<BeschaffungsPriorisierung> ermittleBeschaffungsPriorisierung(Map<Zutat, Float> mengeProZutatMap) {
		List<BeschaffungsPriorisierung> beschPrio = new ArrayList<BeschaffungsPriorisierung>();
		for (Haendler h : Haendler.getAllHaendler()) {
			Map<Zutat, Float> preisProZutatImAngebot = new HashMap<Zutat, Float>();
			// Ermittlung, welche Zutaten im Angebot sind
			float einheitenGesamt = 0;
			for (Zutat z : mengeProZutatMap.keySet()) {
				float preisProEinheit = h.getPreisProEinheit(z.getName());
				if (preisProEinheit > 0) {
					Float mengeProZutat = mengeProZutatMap.get(z);
					// Anzahl Einheite gesamt ermitteln, f�r Durchschnittsberechnung. Dabei wird unterstellt, dass auf jede Einheit gleich viele anteilige Transportkosten entfallen (Gramm, Liter, St�ck)
					einheitenGesamt += mengeProZutat;
					// Merken, das diese Zutat im Angebot ist, f�r die Transportkostenermittlung
					preisProZutatImAngebot.put(z, preisProEinheit);
				}
			}
			// Gesamte Transportkosten ermitteln
			float gesamtTransportkosten = h.berechneTransportkosten(mengeProZutatMap.size());
			float tkProEinheit = gesamtTransportkosten / einheitenGesamt;
			
			// Pro Zutat die Anschaffungskosten vergleichen, und die BeschaffungsPriorisierung erstellen und die H�ndler nach Anschaffungskosten zuordnen
			for (Zutat z : preisProZutatImAngebot.keySet()) {
				BeschaffungsPriorisierung zutatPrio = BeschaffungsPriorisierung.getBeschaffungsPriorisierung(beschPrio, z);
				if (zutatPrio == null) {
					zutatPrio = new BeschaffungsPriorisierung(z);
					beschPrio.add(zutatPrio);
				}
				float bruttoAnschKosten = preisProZutatImAngebot.get(z) + tkProEinheit;
				zutatPrio.addZuoHaendlerAnschKosten(new ZuoHaendlerAnschKosten(h, bruttoAnschKosten));
			}
		}
		
		// BeschaffungsPriorit�t f�r sp�tere Verwendung in der Planung merken
		beschPrioListe = beschPrio;
		return beschPrio;
	}
	
	/**
	 * Erstellt Speisepl�ne und weist diese einer {@link Kantine} zu. Die Zuweisung erfolgt individuell.
	 * Die Kantinen in <code>kantinenListe</code> k�nnen somit ungleiche Speisepl�ne haben.
	 * @param datumVon Date Das Startdatum f�r die Speisepl�ne
	 * @param datumBis Date Das einschlie�liche Endedatum f�r die Speisepl�ne
	 * @param kantinenListe List&lt;Kantine&gt; Liste der Kantinen, f�r die Speisepl�ne berechnet werden sollen.
	 */
	public static void erstelleSpeiseplan(Date datumVon, Date datumBis, List<Kantine> kantinenListe) {
		if (kantinenListe == null || kantinenListe.size() == 0) {
			String warn = "Parameter kantinenListe darf nicht NULL und nicht leer sein!";
			logger.warn(warn);
			throw new IllegalArgumentException(warn);
		}
		Calendar von = Calendar.getInstance();
		von.setTime(datumVon);
		Calendar bis = Calendar.getInstance();
		bis.setTime(datumBis);
		if (von.after(bis)) {
			String warn = "Parameter datumVon ("+datumVon+") darf nicht nach Parameter datumBis ("+datumBis+") liegen!";
			logger.warn(warn);
			throw new IllegalArgumentException(warn);
		}
		
		/* 
		 * Bekannte Gerichte und Mitarbeiter sammeln (dies erfolgt als eine fiktive Kantine!)
		 */
		List<Gericht> gerichteListe = kantinenListe.get(0).getGerichteListe();
		int anzahlMA = 0;
		for (Kantine k : kantinenListe) {
			anzahlMA = anzahlMA + k.getAnzahlMitarbeiter();
		}
		// Reduktion auf kaufbare Gerichte
		gerichteListe = HaendlerDienst.getKaufbareGerichte(gerichteListe, anzahlMA);
		
		// Speiseplan zusammenstellen
		erstelleSpeiseplan(kantinenListe, von, bis, gerichteListe);
	}

	private static void erstelleSpeiseplan(List<Kantine> kantinenListe, Calendar von, Calendar bis, List<Gericht> gerichteListe) {
		List<SpeiseplanPos> speiseplan = new ArrayList<SpeiseplanPos>();
		GerichtDienst.sort(gerichteListe);
		while (von.compareTo(bis) <= 0) {
			SpeiseplanPos speiseplanPos = new SpeiseplanPos(von.getTime());
			List<Gericht> tagesGerichte;
			int currentDayOfWeek = von.get(Calendar.DAY_OF_WEEK);
			if (currentDayOfWeek == Calendar.SATURDAY) {
				// Samstags ist nichts zu tun, ggf sp�ter per Konfig 6 Tage Woche m�glich
			}
			else if (currentDayOfWeek == Calendar.SUNDAY) {
				// Fisch wird nur am Wochenende zur�ckgesetzt
				hasFisch = false;				
			}
			else {
				// Montag bis Freitag
				tagesGerichte = erzeugeTagesGerichte(gerichteListe);
				// Pr�fung, ob das Angebot aller H�ndler ausgereicht hat, um den Zeitraum zu befriedigen.
				if (tagesGerichte.size() < GERICHTE_PRO_TAG) {
					String err = "Die kaufbaren Gerichte ("+gerichteListe.size()+") reichen nicht aus, um einen Speiseplan fuer den angeforderten Zeitraum zu erstellen!";
					logger.error(err);
					throw new IllegalArgumentException(err);
				}
				speiseplanPos.setGerichte(tagesGerichte);
			}
			speiseplan.add(speiseplanPos);
			von.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		/*
		 *  Speiseplan in Kantinen anh�ngen
		 *  Es wird eine Kopie erstellt, damit die Kantinen ihren Speiseplan individualisieren k�nnen, ohne den Plan der anderen zu ver�ndern.
		 *  Jede Kantine hat also eine individuelle Instanz des Speiseplans, sodass gilt (kantineX.getSpeiseplan != kantineY.getSpeiseplan).
		 */
		for (Kantine k : kantinenListe) {
			k.setSpeiseplan(new ArrayList<SpeiseplanPos>(speiseplan));
		}
	}
	
	private static List<Gericht> erzeugeTagesGerichte(List<Gericht> gerichteListe) {
		// Tagesschleife f�r this.GERICHTE_PRO_TAG Gerichte
		List<Gericht> tagesGerichte = new ArrayList<Gericht>(GERICHTE_PRO_TAG);
		boolean hasFleisch = false;
		boolean hasVegetarisch = false;
		for (int i=0; i<gerichteListe.size() && tagesGerichte.size() < GERICHTE_PRO_TAG; i++) {
			Gericht gerichtAtI = gerichteListe.get(i);
			if (!hasFleisch) {
				if (gerichtAtI.isFleisch()) {
					tagesGerichte.add(gerichtAtI);
					gerichteListe.remove(i);
					i = -1;
					hasFleisch = true;
				}
			}
			else if (!hasFisch) {
				if (gerichtAtI.isFisch()) {
					tagesGerichte.add(gerichtAtI);
					gerichteListe.remove(i);
					i = -1;
					hasFisch = true;
				}
			}
			else if (!hasVegetarisch){
				if (gerichtAtI.isVegetarisch()) {
					tagesGerichte.add(gerichtAtI);
					gerichteListe.remove(i);
					i = -1;
					hasVegetarisch = true;
				}
			}
			else {
				tagesGerichte.add(gerichtAtI);
				gerichteListe.remove(i);
				i = -1;
			}
		}
		return tagesGerichte;
	}

	/** 
	 * Private Konstruktor.
	 * Dienstklasse zustandslos (stateless) und soll nicht instanziiert werden k�nnen.
	 */
	private PlanerDienst(){}

}
