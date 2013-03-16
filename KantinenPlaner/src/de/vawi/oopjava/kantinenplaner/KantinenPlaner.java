/**
 * 
 */
package de.vawi.oopjava.kantinenplaner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vawi.oopjava.kantinenplaner.configuration.OutputFormat;
import de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil;
import de.vawi.oopjava.kantinenplaner.dienste.GerichtDienst;
import de.vawi.oopjava.kantinenplaner.dienste.HaendlerDienst;
import de.vawi.oopjava.kantinenplaner.dienste.PlanerDienst;
import de.vawi.oopjava.kantinenplaner.model.BeschaffungsPriorisierung;
import de.vawi.oopjava.kantinenplaner.model.Beschaffungsliste;
import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Kantine;
import de.vawi.oopjava.kantinenplaner.oberflaeche.OberflaecheMain;
import de.vawi.oopjava.kantinenplaner.tools.impl.AbstractBeschaffungslistenWriter;
import de.vawi.oopjava.kantinenplaner.tools.impl.EinkaufslistenWriter;
import de.vawi.oopjava.kantinenplaner.tools.impl.HitlistenComparator;
import de.vawi.oopjava.kantinenplaner.tools.impl.KostenWriter;
import de.vawi.oopjava.kantinenplaner.tools.impl.SpeiseplanWriter;

/**
 * Der KantinenPlaner dient als Fassade zur Benutzerinteratkionsschicht (z.B. Shell, GUI).
 * <br><br>Infos zu Designentscheidungen:
 * <br>http://de.wikipedia.org/wiki/Fassade_%28Entwurfsmuster%29
 * <br>"Make defensive copies when needed" (Item 39, Effective Java 2nd Edition)
 * @author Kim Michael Jansen
 * @version 1.9
 * 
 * @see {@link KantinenPlanerShell}
 * @see {@link OberflaecheMain}
 */
public final class KantinenPlaner {
	private static final Logger logger = LogManager.getLogger(KantinenPlaner.class);
	private List<Kantine> kantinenListe = new ArrayList<Kantine>();
	private SpeiseplanWriter speiseplanWriter = new SpeiseplanWriter();
	private AbstractBeschaffungslistenWriter einkaufslistenWriter = new EinkaufslistenWriter();
	private AbstractBeschaffungslistenWriter kostenWriter = new KostenWriter();
	private String speiseplanFilename = "Speiseplan";
	private String einkaufslistenFilename = "Einkaufsliste";
	private String kostenuebersichtFilename = "Kostenuebersicht";
	
	/**
	 * Verarbeitet die Preisliste. Dabei werden die Händler und Zutaten angelegt und verknüpft.
	 * @param preisliste
	 */
	public void verarbeitePreisliste(String preisliste) {
		try {
			HaendlerDienst.parseHaendlerDatei(preisliste);
		} catch (IOException e) {
			logger.warn("Preisliste ("+preisliste+") konnte nicht gelesen werden.");
		}
	}
	
	/**
	 * Verarbeitet die Hitliste.
	 * @param hitliste String Der Dateipfad zur Hitliste.
	 * @return Comparator&lt;Gericht&gt; Gibt einen Comparator zurück, der Gerichte unter Berücksichtigung ihrer Priorität vergleicht.
	 * @throws IOException Wird geworfen, wenn beim Lesezugriff auf <code>hitliste</code> ein Fehler auftritt.
	 * @see {@link HitlistenComparator}
	 */
	public Comparator<Gericht> verarbeiteHitliste(String hitliste, List<Gericht> rezepte) throws IOException {
		GerichtDienst.parseHitlistenDatei(hitliste, rezepte);
		return new HitlistenComparator();
	}
	
	/**
	 * Liest die Rezept und fügt diese in den Kantinen als bekannte Gerichte hinzu.
	 * Die eingelesenen Rezepte werden als {@link Gericht} den Kantinen hinzugefügt.
	 * @param rezepte String Der Dateipfad zur Rezeptedatei
	 * @throws IOException Wird geworfen, wenn beim Lesezugriff auf <code>rezepte</code> ein Fehler auftritt.
	 * @return List&lt;Gericht&gt; Gibt eine Liste der Rezepte zurück.
	 */
	public List<Gericht> verarbeiteRezepte(String rezepte, List<Kantine> kantinenListe) throws IOException {
		List<Gericht> gerichteListe = GerichtDienst.parseBekannteGerichteDatei(rezepte);
		for (Kantine k : kantinenListe) {
			k.setGerichteListe(gerichteListe);
		}
		return gerichteListe;
	}
	
	/**
	 * Erstellt einen Speiseplan beginnend mit <code>datumVon</code> und endend mit einschließlich <code>datumBis</code>.
	 * Das Startdatum muss vor oder gleich dem Endedatum sein. Ansonsten wird kein Speiseplan erstellt.
	 * @param datumVon Date Das Startdatum
	 * @param datumBis Date Das Endedatum
	 */
	public void erstelleSpeiseplan(Date datumVon, Date datumBis) {
		PlanerDienst.erstelleSpeiseplan(datumVon, datumBis, kantinenListe);
		speiseplanWriter.setKantine(kantinenListe);
	}
	
	/**
	 * Erstellt eine Beschaffungsliste für die Speisepläne, die in den Kantinen in <code>this.kantinenListe</code> enthalten sind.
	 * Im EinkaufslistenWriter und KostenWriter wird außerdem die erstellte Beschaffungsliste gesetzt.
	 */
	public void erstelleBeschaffungsliste() {
		Beschaffungsliste beschaffungsliste = PlanerDienst.erstelleBeschaffungsliste(kantinenListe);
		einkaufslistenWriter.setBeschaffungsliste(beschaffungsliste);
		kostenWriter.setBeschaffungsliste(beschaffungsliste);
	}
	
	/**
	 * Erstellt die Dateiausgaben im gewünschten Format.
	 * @param outputDir File Das Zielverzeichnis, in das geschrieben werden soll.
	 * @param format OutputFormat Das Ausgabeformat.
	 * @throws IOException Wird geworfen, wenn beim Schreiben der Ausgabe ein Fehler auftritt.
	 * @see {@link KantinenPlaner#speiseplanWriter}
	 * @see {@link KantinenPlaner#einkaufslistenWriter}
	 * @see {@link KantinenPlaner#kostenWriter}
	 */
	public void erstelleAusgaben(File outputDir, OutputFormat format) throws IOException {
		/*
		 * Streams werden vom Try-Ressource-Block automatisch geöffnet, geflushed und geschlossen.
		 * @see java.lang.AutoClosable
		 */
		switch (format) {
		case HTML:
			try (
				FileOutputStream speiseplanOutStream = new FileOutputStream(outputDir+"/"+speiseplanFilename+".html");
				FileOutputStream einkaufsOutStream = new FileOutputStream(outputDir+"/"+einkaufslistenFilename+".html");
				FileOutputStream kostenOutStream = new FileOutputStream(outputDir+"/"+kostenuebersichtFilename+".html");
				){
			speiseplanWriter.writeAsHtml(speiseplanOutStream);
			einkaufslistenWriter.writeAsHtml(einkaufsOutStream);
			kostenWriter.writeAsHtml(kostenOutStream);
			}
			break;
		default:
			try (
				FileOutputStream speiseplanOutStream = new FileOutputStream(outputDir+"/"+speiseplanFilename+".txt");
				FileOutputStream einkaufsOutStream = new FileOutputStream(outputDir+"/"+einkaufslistenFilename+".txt");
				FileOutputStream kostenOutStream = new FileOutputStream(outputDir+"/"+kostenuebersichtFilename+".txt");
				) {
			speiseplanWriter.writeAsText(speiseplanOutStream);
			einkaufslistenWriter.writeAsText(einkaufsOutStream);
			kostenWriter.writeAsText(kostenOutStream);
			}
			break;
		}
		
	}
	
	/**
	 * @return Gibt die Summe aller Mitarbeiter zurück, die einer Kantine in <code>this.kantinenListe</code> zugeordnet sind.
	 */
	public int getMAGesamt() {
		int maGesamt = 0;
		for (Kantine k : kantinenListe) {
			maGesamt += k.getAnzahlMitarbeiter();
		}
		return maGesamt;
	}

	/**
	 * Erstellt eine grobe Priorisierung, auf Grundlage der Rezepte, der Mitarbeiterzahl aller Kantinen.
	 * Wenn der Speiseplan bereits bekannt ist, sollte die genauere Methode {@link this#erstelleBeschaffungsPriorisierung(List, float)} verwendet werden!
	 * @param rezepte List&lt;Gericht&gt; Die bekannten Rezepte
	 * @param maGesamt int Die Anzahl Mitarbeiter
	 * @return List&lt;BeschaffungsPriorisierung&gt; Gibt die erstellte Priorisierung zurück.
	 */
	public List<BeschaffungsPriorisierung> erstelleBeschaffungsPriorisierung(List<Gericht> rezepte, int maGesamt) {
		return PlanerDienst.erstelleBeschaffungsPriorisierung(rezepte, maGesamt);
	}
	
	/**
	 * Erstellt eine Priorisierung, die basierend auf den Speiseplänen der Kantinen optimiert wird.
	 * @param kantinenListe List&lt;Kantine&gt; Die Kantinenliste
	 * @return List&lt;BeschaffungsPriorisierung&gt; Gibt die erstellte Priorisierung zurück.
	 */
	public List<BeschaffungsPriorisierung> erstelleBeschaffungsPriorisierung(List<Kantine> kantinenListe) {
		return PlanerDienst.erstelleBeschaffungsPriorisierung(kantinenListe);
	}
	
	/**
	 * Liest die Configutrationsdatei
	 * @throws IOException Wird geworfen, wenn beim Lese- oder Schreibzugriff auf die Konfigurationsdatei ein Fehler auftritt.
	 * @throws FileNotFoundException Wird geworfen, wenn die Konfigurationsdatei nicht gefunden wurde.
	 * @return Liest die Konfigurationsdatei und gibt sie als Properties-Instanz zurück.
	 */
	public Properties leseProperties() throws FileNotFoundException, IOException {
		return new PropertiesUtil().getProperties();
	}
	
	/**
	 * Aktualisiert die Configurationsdatei mit den übergebenen Properties. Werte, die in <code>propterties</code> nicht enthalten sind, bleiben unverändert.
	 * @param properties Properties Das Properties Objekt, mit den neuen oder zu ändernden Schlüsseln
	 * @throws IOException Wird geworfen, wenn beim Lese- oder Schreibzugriff auf die Konfigurationsdatei ein Fehler auftritt.
	 * @throws FileNotFoundException Wird geworfen, wenn die Konfigurationsdatei nicht gefunden wurde.
	 */
	public void aktualisiereProperties(Properties properties) throws FileNotFoundException, IOException {
		new PropertiesUtil().aktualisiereProperties(properties);
	}
	
	/**
	 * Fügt <code>kantine</code> in <code>this.kantinenListe</code> hinzu, wenn diese noch nicht enthalten ist.
	 * @param kantine Kantine Die hinzuzufügende Kantine.
	 * @throws IllegalArgumentException Für <code>kantine == null</code>
	 */
	public void addKantine(Kantine kantine) {
		if (kantine == null) {
			throw new IllegalArgumentException("Parameter kantine darf nicht NULL sein.");
		}
		if (!kantinenListe.contains(kantine)) {
			kantinenListe.add(kantine);
		}
	}
	
	/**
	 * Fügt alle Kantinen in <code>this.kantinenListe</code> hinzu, wenn die jeweilige Kantine noch nicht enthalten ist.
	 * Nachdem die Methode zurückkehrt, sind alle Elemente aus <code>kantinenListe</code> genau einmal in <code>this.kantinenListe</code> enthalten.
	 * @param kantinenListe Collection&lt;Kantine&gt; Die hinzuzufügenden Kantinen.
	 * @throws IllegalArgumentException Für <code>kantinenListe == null</code>
	 * @see KantinenPlaner#addKantine(Kantine)
	 */
	public void addAllKantinen(Collection<Kantine> kantinenListe) {
		if (kantinenListe == null) {
			throw new IllegalArgumentException("Parameter kantinenListe darf nicht NULL sein.");
		}
		for (Kantine k : kantinenListe) {
			try {
				addKantine(k);
			}
			catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("In Parameter kantinenListe darf kein Element == NULL enthalten sein.");
			}
		}
	}
	
	/**
	 * Entfernt <code>kantine</code> aus <code>this.kantinenListe</code>.
	 * @param kantine Kantine Die zu entfernende Kantine.
	 */
	public void removeKantine(Kantine kantine) {
		kantinenListe.remove(kantine);
	}
	
	/**
	 * Entfernt alle Kantinen in <code>kantinenListe</code> aus <code>this.kantinenListe</code>.
	 * @param kantinenListe Collection&lt;Kantine&gt; Die zu entfernenden Kantinen.
	 */
	public void removeAllKantinen(Collection<Kantine> kantinenListe) {
		this.kantinenListe.removeAll(kantinenListe);
	}
	
	/**
	 * Gibt eine Kopie von <code>this.kantinenListe</code> zurück.
	 * @return List&lt;Kantine&gt; Die Kopie von <code>this.kantinenListe</code>.
	 */
	public List<Kantine> getKantinenListe() {
		return new ArrayList<Kantine>(kantinenListe);
	}
	
	/**
	 * Ersetzt <code>this.kantinenListe</code> durch <code>kantineListe</code>.
	 * @param kantinenListe List&lt;Kantine&gt; Die zu setzende <code>kantinenListe</code>
	 * @throws IllegalArgumentException Für <code>kantinenListe == null</code>
	 */
	public void setKantinenListe(List<Kantine> kantinenListe) {
		if (kantinenListe == null) {
			throw new IllegalArgumentException("Parameter kantinenListe darf nicht NULL sein.");
		}
		this.kantinenListe = kantinenListe;
	}

	/**
	 * Getter für <code>this.speiseplanFilename</code>.
	 * @return String Gibt <code>this.speiseplanFilename</code> zurück.
	 */
	public String getSpeiseplanFilename() {
		return speiseplanFilename;
	}

	/**
	 * Getter für <code>this.einkaufslistenFilename</code>.
	 * @return String Gibt <code>this.einkaufslistenFilename</code> zurück.
	 */
	public String getEinkaufslistenFilename() {
		return einkaufslistenFilename;
	}

	/**
	 * Getter für <code>this.kostenuebersichtFilename</code>.
	 * @return String Gibt <code>this.kostenuebersichtFilename</code> zurück.
	 */
	public String getKostenuebersichtFilename() {
		return kostenuebersichtFilename;
	}
	
	/**
	 * Gibt die Einkaufsliste im gewünschten Format zurück.
	 * @param format OutputFormat Das Format
	 * @return String Die Einkaufsliste als String.
	 */
	public String createEinkaufslistenString(OutputFormat format) {
		switch (format) {
		case HTML:
			return einkaufslistenWriter.createHtml();
		default:
			return einkaufslistenWriter.createText();
		}
	}
	
	/**
	 * Gibt den Speiseplan im gewünschten Format zurück.
	 * @param format OutputFormat Das Format
	 * @return String Der Speiseplan als String. 
	 */
	public String createSpeiseplanString(OutputFormat format) {
		switch (format) {
		case HTML:
			return speiseplanWriter.createHtml();
		default:
			return speiseplanWriter.createText();
		}
	}
	
	/**
	 * Gibt die Kostenübersicht im gewünschten Format zurück.
	 * @param format OutputFormat Das Format
	 * @return String Die Kostenübersicht als String.
	 */
	public String createKostenuebersichtString(OutputFormat format) {
		switch (format) {
		case HTML:
			return kostenWriter.createHtml();
		default:
			return kostenWriter.createText();
		}
	}
}
