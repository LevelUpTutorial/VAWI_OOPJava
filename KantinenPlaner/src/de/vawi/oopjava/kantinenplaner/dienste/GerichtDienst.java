/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.dienste;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Zutat;
import de.vawi.oopjava.kantinenplaner.tools.impl.CSVReader;

/**
 * Dienstklasse für {@link Gericht}.
 * <br><br>Infos zu Designentscheidungen:
 * <br>"Enforce noninstantiability with a private constructor" (Item 4, Effective Java 2nd Edition)
 * <br>"Make defensive copies when needed" (Item 39, Effective Java 2nd Edition)
 * @author Kim Michael Jansen
 * @version 1.5
 */
public final class GerichtDienst {
	private static final Logger logger = LogManager.getLogger(GerichtDienst.class);
	/** 
	 * Höchste Priorität, die ein Gericht erhalten kann. Dies ist gleichbedeutend mit Gerichten, die laut Hitliste keine Priorität haben. 
	 * Es muss beachtet werden, dass <code>Gericht.prioritaet == 0</code> nicht keine Priorität bedeutet, sondern die höchstmögliche
	 * Priorität repräsentiert.
	 */
	public static final int PRIO_MAX = Integer.MAX_VALUE;
	/**
	 * Gibt die Priorität der Sortierkriterien vor, die jeweils durch einen {@link java.util.Comparator} realisiert sind.
	 * <br> Ein niedrieger Listenindex (0<=i) hat eine höhere Priorität, als ein hoher Listenindex (i>=~).
	 */
	private static List<Comparator<Gericht>> gerichteSortierer = new ArrayList<Comparator<Gericht>>();
	
	/**
	 * Sortiert <code>gerichteListe</code> anhand der in <code>this.gerichteSortiere</code> gehaltenen Sortierer und Prioritäten.
	 * <br> Ein niedrieger Listenindex (0<=i) hat eine höhere Priorität, als ein hoher Listenindex (i>=~).
	 * <br> Nach der Rückkehr, ist die Reihenfolge in <code>gerichteListe</code> entsprechend der Sortierung verändert.
	 * @param gerichteListe List&lt;Gericht&gt; Die zu sortierenden Gerichte.
	 */
	public static void sort(List<Gericht> gerichteListe) {
		for (int i=gerichteSortierer.size()-1; i>=0; i--) {
			Comparator<Gericht> c = gerichteSortierer.get(i);
			Collections.sort(gerichteListe, c);
		}
	}
	
	/**
	 * Ließt die Hitliste und setzt die Prioritäten in Gerichten aus <code>bekannteGerichte</code>. 
	 * Gerichte, die nicht in der Hitliste enhalten sind, bekommen die niedrigste Priorität.
	 * <br><b>Beachte: Bei der Verarbeitung wird vorausgesetzt, dass die Hitliste bei Priorität 1 beginnt!</b>
	 * @param gerichteDatei String Der relative oder absolute Dateipfad.
	 * @param bekannteGerichte List&lt;Gericht&gt; Liste gerichte, für die die Priorität gesetzt werden soll.
	 * @throws FileNotFoundException Wenn die Datei nicht gefunden wurde.
	 * @throws IOException Wenn Fehler beim Lesen der Datei auftraten, die nicht korrigiert werden konnten (z.B. wenn kein lesbares Gericht enthalten war). Die Methode versucht fehlerhafte Zeilen zu überlesen und loggt einen entsprechenden Hinweis.
	 * @return List&lt;Gericht&gt; Gibt eine Liste der Gerichte zurück, die nicht in der Hitliste enthalten waren.
	 */
	public static List<Gericht> parseHitlistenDatei(String gerichteDatei, List<Gericht> bekannteGerichte) throws IOException {
		CSVReader reader = new CSVReader();
		Scanner in = new Scanner(new FileInputStream(gerichteDatei));
		setzeGelesenePrioritaet(bekannteGerichte, reader, in);
		in.close();
		return setzeMaximalePrioritaet(bekannteGerichte);
	}

	/**
	 * Setzt die Priorität, die aus der Hitliste gelesen wurde.
	 * @param bekannteGerichte
	 * @param reader
	 * @param in
	 */
	private static void setzeGelesenePrioritaet(List<Gericht> bekannteGerichte, CSVReader reader, Scanner in) {
		while (in.hasNext()) {
			String nextLine = in.nextLine();
			List<String> zellen = reader.readLine(nextLine);
			if (zellen.size() == 2) { 
				int prio = Integer.parseInt(zellen.get(0));
				String name = zellen.get(1);
				
				for (Gericht g : bekannteGerichte) {
					if (g.getName().equalsIgnoreCase(name)) {
						logger.debug("Setze Prioritaet ("+prio+") fuer Gericht ("+name+")");
						g.setPrioritaet(prio);
					}
				}
			}
			else {
				logger.warn("Eingabedaten fuer Hitliste nicht plausibel:);");
				for (int i=0; i<zellen.size(); i++) {
					logger.warn("zellen["+i+"] = "+zellen.get(i));
				}
			}
		}
	}

	/**
	 * Prüft alle Gerichte, ob diese in der Hitliste enthalten waren und setzt bei nicht priorisierten Gerichten (<code>Gericht.prioritaet == 0</code>) die höchste Priorität.
	 * @see {@link GerichtDienst#PRIO_MAX}
	 * @param bekannteGerichte
	 * @return Gibt 
	 */
	private static List<Gericht> setzeMaximalePrioritaet(List<Gericht> bekannteGerichte) {
		List<Gericht> maxPrioGerichte = new ArrayList<Gericht>();
		for (Gericht g : bekannteGerichte) {
			if (g.getPrioritaet() == 0) {
				logger.debug("Setze Prioritaet ("+PRIO_MAX+") fuer Gericht ("+g.getName()+")");
				g.setPrioritaet(PRIO_MAX);
				maxPrioGerichte.add(g);
			}
		}
		return maxPrioGerichte;
	}
	
	/**
	 * Ließt die <code>gerichteDatei</code> und erzeugt die eingelesenen Gerichte und Zutaten.
	 * @param gerichteDatei String Der relative oder absolute Dateipfad.
	 * @throws FileNotFoundException Wenn die Datei nicht gefunden wurde.
	 * @throws IOException Wenn Fehler beim Lesen der Datei auftraten, die nicht korrigiert werden konnten (z.B. wenn kein lesbares Gericht enthalten war). Die Methode versucht fehlerhafte Zeilen zu überlesen und loggt einen entsprechenden Hinweis.
	 * @return List&lt;Gericht&gt; Gibt die eingelesenen Gerichte als Liste zurück.
	 */
	public static List<Gericht> parseBekannteGerichteDatei(String gerichteDatei) throws IOException {
		List<Gericht> gerichte = new ArrayList<Gericht>();
		CSVReader reader = new CSVReader();
		Scanner in = new Scanner(new FileInputStream(gerichteDatei));
		while (in.hasNext()) {
			List<String> zellen = reader.readLine(in.nextLine());
			if (zellen.size() == 4) { 
				addZutatZuGericht(gerichte, zellen);
			}
			else {
				logger.warn("Eingabedaten fuer Rezeptdatei nicht plausibel:);");
				for (int i=0; i<zellen.size(); i++) {
					logger.warn("zellen["+i+"] = "+zellen.get(i));
				}
			}
		}
		in.close();
		return gerichte;
	}

	/**
	 * Fügt der Gerichteliste die Zutat hinzu, die in durch die Zellenliste beschrieben ist.
	 * @param gerichte
	 * @param zellen
	 */
	private static void addZutatZuGericht(List<Gericht> gerichte, List<String> zellen) {
		Gericht newGericht = null;
		String name = zellen.get(0);
		float menge = Float.parseFloat(zellen.get(1));
		String einheit = zellen.get(2);
		String zutat = zellen.get(3);
		
		for (int i=0; newGericht == null && i<gerichte.size(); i++) {
			Gericht g = gerichte.get(i);
			if (g.getName().equalsIgnoreCase(name)) {
				newGericht = g;
			}
		}
		
		if (newGericht == null) {
			newGericht = new Gericht(name);
			gerichte.add(newGericht);
		}
		Zutat z = Zutat.getZutat(zutat);
		if (z == null) {
			logger.debug("Gericht konnte nicht mit unbekannter Zutat verknuepft werden");
			logger.debug("Das Gericht wird nicht erstellt, da es nicht kaeuflich ist.");
		}
		else {
			if (!z.getEinheit().equals(einheit)) {
				logger.warn("Zutaten werden in verschiedenen Einheiten gemessen! Es erfolgt keine Umrechnung");
			}
			newGericht.putZutatMenge(z, menge);
		}
	}
	
	/**
	 * Fügt <code>gerichteSortierer</code> in <code>GerichtDienst.gerichteSortierer</code> hinzu, wenn dieser noch nicht enthalten ist.
	 * @param gerichteSortierer Comparator&lt;Gericht&gt; Der hinzuzufügende Sortierer.
	 * @throws IllegalArgumentException Für <code>gerichteSortierer == null</code>
	 */
	public static void addGerichteSortierer(Comparator<Gericht> gerichteSortierer) {
		if (gerichteSortierer == null) {
			throw new IllegalArgumentException("Parameter gerichteSortierer darf nicht NULL sein.");
		}
		if (!GerichtDienst.gerichteSortierer.contains(gerichteSortierer)) {
			GerichtDienst.gerichteSortierer.add(gerichteSortierer);
		}
	}
	
	/**
	 * Fügt alle <code>gerichteSortierer</code> in <code>GerichtDienst.gerichteSortierer</code> hinzu, wenn der jeweilige Sortierer noch nicht enthalten ist.
	 * Nachdem die Methode zurückkehrt, sind alle Elemente aus <code>gerichteSortierer</code> genau einmal in <code>GerichtDienst.gerichteSortierer</code> enthalten.
	 * @param gerichteSortierer Collection&lt;Gericht&gt; Der hinzuzufügende Sortierer.
	 * @throws IllegalArgumentException Für <code>gerichteSortierer == null</code>
	 * @see GerichtDienst#addGerichteSortierer(Comparator)
	 */
	public static void addAllGerichteSortierer(Collection<Comparator<Gericht>> gerichteSortierer) {
		if (gerichteSortierer == null) {
			throw new IllegalArgumentException("Parameter gerichteSortierer darf nicht NULL sein.");
		}
		for (Comparator<Gericht> c : gerichteSortierer) {
			try {
				addGerichteSortierer(c);
			}
			catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("In Parameter gerichteSortierer darf kein Element == NULL enthalten sein.");
			}
		}
	}
	
	/**
	 * Entfernt <code>gerichteSortierer</code> aus <code>GerichtDienst.gerichteSortierer</code>.
	 * @param gerichteSortierer Comparator&lt;Gericht&gt; Der zu entfernende Sortierer.
	 */
	public static void removeGerichteSortierer(Comparator<Gericht> gerichteSortierer) {
		GerichtDienst.gerichteSortierer.remove(gerichteSortierer);
	}
	
	/**
	 * Entfernt alle Sortierer in <code>gerichteSortierer</code> aus <code>GerichtDienst.gerichteSortierer</code>.
	 * @param gerichteSortierer Collection&lt;Gericht&gt; Die zu entfernenden Sortierer.
	 */
	public static void removeAllGerichteSortierer(Collection<Comparator<Gericht>> gerichteSortierer) {
		GerichtDienst.gerichteSortierer.removeAll(gerichteSortierer);
	}
	
	/**
	 * Gibt eine Kopie von <code>GerichtDienst.gerichteSortierer</code> zurück.
	 * @return List<Comparator<Gericht>> Die Kopie von <code>GerichtDienst.gerichteSortierer</code>.
	 */
	public static List<Comparator<Gericht>> getGerichteSortierer() {
		return new ArrayList<Comparator<Gericht>>(gerichteSortierer);
	}

	/**
	 * Ersetzt <code>GerichtDienst.gerichteSortierer</code> durch <code>gerichteSortierer</code>.
	 * @param gerichteSortierer List&lt;Comparator&lt;Gericht&gt;&gt; Die zu setzenden <code>gerichteSortierer</code>.
	 * @throws IllegalArgumentException Für <code>gerichteSortierer == null</code>
	 */
	public static void setGerichteSortierer(List<Comparator<Gericht>> gerichteSortierer) {
		if (gerichteSortierer == null) {
			throw new IllegalArgumentException("Parameter gerichteSortierer darf nicht NULL sein.");
		}
		GerichtDienst.gerichteSortierer = gerichteSortierer;
	}

	/** 
	 * Privater Konstruktor.
	 * Reine Dienstklasse soll nicht instanziiert werden können.
	 */
	private GerichtDienst(){}
}
