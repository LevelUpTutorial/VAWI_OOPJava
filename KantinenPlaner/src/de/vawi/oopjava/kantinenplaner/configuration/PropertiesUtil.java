/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vawi.oopjava.kantinenplaner.model.Kantine;
import de.vawi.oopjava.kantinenplaner.oberflaeche.DatumPlausiHelper;
import de.vawi.oopjava.kantinenplaner.tools.impl.PreisComparator;

/**
 * Utilklasse zum vereinfachten und typsicheren Zugriff auf die Konfigurationsdatei.
 * @author Kim
 * @version 1.9
 */
public final class PropertiesUtil {
	private static final Logger logger = LogManager.getLogger(PropertiesUtil.class);
	private static final String DEFAULT_CONFIGPATH = "./config/planer.cfg";
	private final DateFormat sdf = new SimpleDateFormat(DatumPlausiHelper.getFormatDatum());
	private final Properties properties = new Properties();
	/** Schlüssel der Preisliste(n). N Preislisten können durch Komma getrennt konfiguriert werden. */
	public static final String KEY_PREISLISTE = "FILE.PREISLISTE";
	/** Schlüssel der Rezepte */
	public static final String KEY_REZEPTE = "FILE.REZEPTE";
	/** Schlüssel der Hitliste */
	public static final String KEY_HITLISTE = "FILE.HITLISTE";
	/** Schlüssel der Kantine(n). N Kantinen können durch das Muster [(Kantinenname).(Mitarbeiterzahl)][,(Kantinenname).(Mitarbeiterzahl)] konfiguriert werden. */
	public static final String KEY_KANTINE = "CLASS.KANTINE";
	/** 
	 * Schlüssel des Startdatums 
	 * @see {@link PropertiesUtil#FORMAT_DATUM} 
	 */
	public static final String KEY_STARTDATUM = "PERIODE.STARTDATUM";
	/** 
	 * Schlüssel des Enddatums 
	 * @see {@link PropertiesUtil#FORMAT_DATUM} 
	 */
	public static final String KEY_ENDEDATUM = "PERIODE.ENDEDATUM";
	/** Schlüssel des Ausgabeverzeichnisses */
	public static final String KEY_OUTPUT_DIR = "DIR.OUTPUT";
	/** Schlüssel des Ausgabeformats */
	public static final String KEY_OUTPUT_FORMAT = "FORMAT.OUTPUT";
	/**
	 * Schlüssel der Toleranz
	 * @see {@link PreisComparator}
	 */
	public static final String KEY_TOLERANZ = "PERIODE.TOLERANZ";
	/** Dateipfad der Konfigurationsdatei */	
	public String config = "./config/planer.cfg";
	
	/**
	 * Konstruktor
	 * @throws IOException Wird geworfen, wenn beim Lesen der Konfigurationsdatei (<code>PropertiesUtil.DEFAULT_CONFIGPATH</code>) ein Fehler auftritt.
	 * @throws FileNotFoundException Wird geworfen, wenn die Konfigurationsdatei (<code>PropertiesUtil.DEFAULT_CONFIGPATH</code>) nicht gefunden werden kann.
	 */
	public PropertiesUtil() throws FileNotFoundException, IOException {
		this(DEFAULT_CONFIGPATH);
	}
	
	/**
	 * Konstruktor
	 * @param configpath String Der Pfad zu einer beliebigen ".properties"-Datei.
	 * @throws IOException Wird geworfen, wenn beim Lesen der Konfigurationsdatei (<code>PropertiesUtil.config</code>) ein Fehler auftritt.
	 * @throws FileNotFoundException Wird geworfen, wenn die Konfigurationsdatei (<code>PropertiesUtil.config</code>) nicht gefunden werden kann.
	 */
	public PropertiesUtil(String configpath) throws FileNotFoundException, IOException {
		this.config = configpath;
		try (InputStream in = new FileInputStream(configpath)) {
			properties.load(in);
		} catch (Throwable t) {
			logger.error("Fehler beim Zugriff auf "+configpath, t);
			throw t;
		}
	}
	
	/**
	 * Aktualisiert den Inhalt der Konfigurationsdatei
	 * @param newProperties Properties Die neuen Werte
	 * @throws IOException Wird geworfen, wenn beim Schreiben in die Konfigurationsdatei ein Fehler auftritt.
	 */
	public void aktualisiereProperties(Properties newProperties) throws IOException {
		speichereProperties(newProperties, config);
	}
	
	/**
	 * Speichert den Inhalt der Konfigurationsdatei an einem beliebigen Ort.
	 * @param newProperties Properties Die neuen Werte
	 * @param path String Der Speicherpfad
	 * @throws IOException Wird geworfen, wenn beim Schreiben in die Konfigurationsdatei ein Fehler auftritt.
	 */
	public static void speichereProperties(Properties newProperties, String path) throws IOException {
		/*
		 *  Try-Resource-Block aus Java 1.7
		 *  Stream wird automatisch geöffnet, geflushed und geschlossen
		 *  @see java.lang.Autoclosable
		 */
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)))) {
			writer.write("# Konfigurationsdatei zum Kantinenplaner"+"\n");
			writer.write("# Autor Kim Michael Jansen"+"\n");
			writer.write("# Properties wurde vom System aktualisiert am "+Calendar.getInstance().getTime()+"\n");
			writer.write("# Version 1.5"+"\n");
			writer.write("# Wichtig! Die Werte zu den Konfigurationsschlüsseln sollten keine Leerzeichen \" \" enthalten."+"\n");
			writer.write("# Dateipfade dürfen keinesfalls Leerzeichen \" \" enthalten."+"\n");
			writer.write("# Liste der Dateipfade für Preislisten der Händler (relativ oder absolut)"+"\n");
			writer.write(KEY_PREISLISTE+"="+newProperties.getProperty(KEY_PREISLISTE)+"\n");
			writer.write("# Dateipfad der Rezeptedatei alias \"bekannte Gerichte des Kochs\""+"\n");
			writer.write(KEY_REZEPTE+"="+newProperties.getProperty(KEY_REZEPTE)+"\n");
			writer.write("# Dateipfad der Hitliste alias \"beliebte Gerichtedatei\""+"\n");
			writer.write(KEY_HITLISTE+"="+newProperties.getProperty(KEY_HITLISTE)+"\n");
			writer.write("# Liste der Kantinen im Format [(Name Kantine 1).(Anzahl Mitarbeiter)][,(Name Kantine n).(Anzahl Mitarbeiter)]"+"\n");
			writer.write("# Die Kantinen werden durch Komma (,) getrennt"+"\n");
			writer.write("# Die Parameter eine Kantine werden durch Punkt (.) getrennt"+"\n");
			writer.write(KEY_KANTINE+"="+newProperties.getProperty(KEY_KANTINE)+"\n");
			writer.write("# Planungszeitraum, Datumsformat = "+DatumPlausiHelper.getFormatDatum()+"\n");
			writer.write("# d = Tag, M = Monat, y = Jahr"+"\n");
			writer.write(KEY_STARTDATUM+"="+newProperties.getProperty(KEY_STARTDATUM)+"\n");
			writer.write(KEY_ENDEDATUM+"="+newProperties.getProperty(KEY_ENDEDATUM)+"\n");
			writer.write("# Die Toleranz in Euro. Bei der Planung werden grundsätzlich die günstigsten Gerichte bevorzugt."+"\n");
			writer.write("# Durch Angabe einer Toleranz ist es möglich z.B. Hitlistengerichte zu bevorzugen, "+"\n");
			writer.write("# wenn die Preisdifferenz die Toleranz nicht überschreitet."+"\n");
			writer.write(KEY_TOLERANZ+"="+newProperties.getProperty(KEY_TOLERANZ)+"\n");
			writer.write("# Verzeichnis für Ausgabedateien (relativ oder absolut)"+"\n");
			writer.write(KEY_OUTPUT_DIR+"="+newProperties.getProperty(KEY_OUTPUT_DIR)+"\n");
			writer.write("# Wahl der Ausgabeformate"+"\n");
			StringBuffer formatBuffer = new StringBuffer("# Gültige Werte sind ");
			OutputFormat[] formats = OutputFormat.values();
			int length = formats.length;
			for (int i=0; i<length;i++) {
				formatBuffer.append("\""+formats[i].name()+"\"");
				if (i<length-1) {
					formatBuffer.append(", ");
				}
			}
			writer.write(formatBuffer.toString()+"\n");
			writer.write(KEY_OUTPUT_FORMAT+"="+newProperties.getProperty(KEY_OUTPUT_FORMAT)+"\n");
			
		} catch (IOException e) {
			logger.warn("Fehler beim Schreiben in "+path, e);
			throw e;
		}
	}

	/**
	 * @return List&lt;String&gt; Gibt eine Liste mit den Pfaden der Preislisten zurück.
	 */
	public List<String> getPreislisten() {
		String value = properties.getProperty(KEY_PREISLISTE);
		logger.trace("Lade KEY_PREISLISTE: "+value);
		return Arrays.asList(value.split(","));
	}
	
	/**
	 * @return String Gibt den Pfad der Rezeptedatei zurück.
	 */
	public String getRezepte() {
		return properties.getProperty(KEY_REZEPTE);
	}
	
	/**
	 * @return String Gibt den Pfad der Hitlistendatei zurück.
	 */
	public String getHitliste() {
		return properties.getProperty(KEY_HITLISTE);
	}
	
	/**
	 * @return List&lt;Kantine&gt; Gibt eine Liste der Konfigurierten Kantinen als Objektinstanzen zurück.
	 */
	public List<Kantine> getKantinen() {
		List<Kantine> kantinenListe = new ArrayList<Kantine>();
		String value = properties.getProperty(KEY_KANTINE);
		logger.trace("Lade KEY_KANTINE: "+value);
		String[] kantinenValues = value.split(",");
		for (String kValue : kantinenValues) {
			String[] kParameter = kValue.split("\\.");
			kantinenListe.add(new Kantine(Integer.parseInt(kParameter[1]), kParameter[0]));
		}
		
		return kantinenListe;
	}
	
	/**
	 * @return Date Gibt das Startdatum der Planung zurück.
	 * @throws ParseException Wird geworfen, wenn der Datumsstring nicht dem Format in <code>DatumPlausiHelper.getFormatDatum()</code> entspricht (default="dd.MM.yyyy");
	 * @see {@link DatumPlausiHelper#getFormatDatum()}
	 */
	public Date getStartdatum() throws ParseException {
		String value = properties.getProperty(KEY_STARTDATUM);
		logger.trace("Lade KEY_STARTDATUM: "+value);
		logger.trace("SimpleDateFormat = "+DatumPlausiHelper.getFormatDatum());
		return sdf.parse(value);
	}
	
	/**
	 * @return Date Gibt das Endedatum der Planung zurück.
	 * @throws ParseException Wird geworfen, wenn der Datumsstring nicht dem Format in <code>DatumPlausiHelper.getFormatDatum()</code> entspricht (default="dd.MM.yyyy");
	 * @see {@link DatumPlausiHelper#getFormatDatum()}
	 */
	public Date getEndedatum() throws ParseException {
		String value = properties.getProperty(KEY_ENDEDATUM);
		logger.trace("Lade KEY_ENDEDATUM: "+value);
		logger.trace("SimpleDateFormat = "+DatumPlausiHelper.getFormatDatum());
		return sdf.parse(value);
	}
	
	/**
	 * @return File Gibt das Verzeichnis für die Ausgabedateien als <code>java.io.File</code> zurück.
	 */
	public File getOutputDir() {
		String value = properties.getProperty(KEY_OUTPUT_DIR);
		logger.trace("Lade KEY_OUTPUT_DIR: "+value);
		return new File(value);
	}
	
	/**
	 * @return String Gibt den Wert des Ausgabeformats zurück.
	 */
	public String getOutputFormat() {
		return properties.getProperty(KEY_OUTPUT_FORMAT);
	}
	
	/**
	 * @return Gibt die Toleranz zurück.
	 */
	public float getToleranz() {
		String toleranz = properties.getProperty(KEY_TOLERANZ);
		return Float.parseFloat(toleranz.replace(",", "."));
	}
	
	/**
	 * Getter für <code>this.properties</code>.
	 * @return Properties Gibt <code>this.properties</code> zurück.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Getter für <code>this.sdf</code>.
	 * @return DateFormat Gibt <code>sdf</code> zurück.
	 */
	public DateFormat getSdf() {
		return sdf;
	}

	/**
	 * Getter für <code>this.config</code>.
	 * @return String Gibt <code>this.config</code> zurück.
	 */
	public String getConfig() {
		return config;
	}
	
	/**
	 * Getter für <code>PropertiesUtil.DEFAULT_CONFIGPATH</code>.
	 * @return String Gibt <code>PropertiesUtil.DEFAULT_CONFIGPATH</code> zurück.
	 */
	public static String getDefaultConfigPath() {
		return DEFAULT_CONFIGPATH;
	}
	
	/**
	 * Wandelt eine Liste von Kantinen in ein Stringformat um, sodass es zum Schlüssel {@link PropertiesUtil#KEY_KANTINE} abgelegt und wieder ausgelesen werden kann.
	 * @see {@link PropertiesUtil#getKantinen()}
	 * @param kantinen List&lt;Kantine&gt; Die Kantinenliste.
	 * @return Gibt die Kantinen als String zurück. Für eine leere Kantinenliste, wird ein leerer String ("") zurückgegeben.
	 */
	public static String parseKantinenAsString(List<Kantine> kantinen) {
		StringBuffer buffer = new StringBuffer();
		int size = kantinen.size();
		for (int i=0;i<size; i++) {
			if (i > 0 ) {
				buffer.append(",");
			}
			Kantine kantine = kantinen.get(i);
			buffer.append(kantine.getName()+"."+kantine.getAnzahlMitarbeiter());
		}
		return buffer.toString();
	} 
	
}
