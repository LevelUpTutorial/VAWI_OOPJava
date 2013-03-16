/**
 * 
 */
package de.vawi.oopjava.kantinenplaner;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vawi.oopjava.kantinenplaner.configuration.OutputFormat;
import de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil;
import de.vawi.oopjava.kantinenplaner.dienste.GerichtDienst;
import de.vawi.oopjava.kantinenplaner.model.BeschaffungsPriorisierung;
import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.oberflaeche.DatumPlausiHelper;
import de.vawi.oopjava.kantinenplaner.tools.impl.PreisComparator;

/**
 * Programmsteuerung ohne weitere Benutzerinteraktion.
 * Die Konfiguration erfolg über per Konfigurationsdatei.
 * Die Ausgaben werden ebenfalls per Konfigurationsdatei voreingestellt.
 * <br><br>
 * Zusätzlich zur Bildschirmausgabe erfolgt ein Bericht in Form eines Logs. Der Inhalt des Logs ist in der Regel umfangreicher als die Bildschirmausgabe.
 * Die Logger können vor dem Programmstart durch Anpassung der &quotlog4j2.xml&quot angepasst werden.
 * Für eine nähe Beschreibung der Konfiguration von Log4j2 wird auf die offizielle Dokumentation verwiesen (<a>http://logging.apache.org/log4j/2.x/manual/configuration.html#AutomaticConfiguration</a>).
 * <br><br>
 * Der &quotReturnCode&quot der Anwendung ist null (0), wenn die Verarbeitung kontrolliert durchgeführt werden konnte.
 * In einem Fehlerfall ist der &quotReturnCode&quot größer null (>0). In diesem Fall, kann das Logfile zur Analyse herangezogen werden.
 * 
 * @see {@link PropertiesUtil#config}
 * @author Kim
 * @version 1.5
 */
public final class KantinenPlanerShell {
	private static final Logger logger = LogManager.getLogger(KantinenPlanerShell.class);
	/** 
	 * Der ReturnCode, der beim Beenden der JVM an das Laufzeitsystem zurückgegeben wird 
	 * Diese kann z.B. von einem Skript geprüft werden, ob das Programm, aus Sicht des Programms, fehlerfrei durchlaufen wurde.
	 */
	private static int returncode = 0;
	/** Fassade zur Planung */
	private KantinenPlaner planer;
	
	/**
	 * Einstiegspunkt in die automatisierte Planung.
	 * @param args Die Aufrufparameter
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		try {
			print("Starte die Verarbeitung");
			if (logger.isDebugEnabled() && args.length > 0) {
				logger.debug("Ausgabe der Aufrufparameter:");
				for (String s : args) {
					logger.debug(s);
				}
			}
			new KantinenPlanerShell().run(args);
			print("Ende der Verarbeitung");
			
		} catch (KantinenPlanerException fachlicherFehler) {
			String msg = "Bei der Verarbeitung ist ein Fehler aufgetreten. Das Programm wird beendet. Um mehr Informationen zu erhalten, kann die Logdatei geprueft werden.";
			logProgrammabbruch(fachlicherFehler, msg);
		} catch (Throwable t) {
			String msg = "Bei der Verarbeitung ist ein unbekannter Fehler aufgetreten. Das Programm wird beendet. Um mehr Informationen zu erhalten, kann die Logdatei geprueft werden.";
			logProgrammabbruch(t, msg);
		}
		logger.debug("Programmende mit ReturnCode = "+returncode);
		logger.info("Laufzeit betrug "+(System.currentTimeMillis()-start)+" Millisekunden");
		System.exit(returncode);
	}
	
	/**
	 * Start des Programmlaufs auf Instanzebene.
	 * Dient zur sauberen Trennung zwischen statischer und objektorientierter Ebene.
	 * @param args Die Aufrufparameter
	 * @throws KantinenPlanerException Wird geworfen, wenn ein bei der Verarbeitung ein Fehler auftritt.
	 */
	public void run(String[] args) throws KantinenPlanerException {
		logger.trace("Konfiguration lesen");
		PropertiesUtil config;
		try {
			config = new PropertiesUtil();
		} catch (IOException e) {
			logger.error("Die Konfigurationsdatei ("+PropertiesUtil.getDefaultConfigPath()+") konnte gelesen werden.");
			throw new KantinenPlanerException(e);
		}
		
		logger.trace("Erstelle PlanungsFassade");
		planer = new KantinenPlaner();
		
		logger.trace("Preislisten verarbeiten");
		for (String preisliste : config.getPreislisten()) {
			planer.verarbeitePreisliste(preisliste);
		}
		logger.trace("Erstelle Kantinen");
		planer.setKantinenListe(config.getKantinen());
		
		logger.trace("Rezepte verarbeiten");
		String rezepteDatei = config.getRezepte();
		List<Gericht> rezepte = null;
		try {
			rezepte = planer.verarbeiteRezepte(rezepteDatei, planer.getKantinenListe());
		} catch (IOException e) {
			logger.error("Rezepte in ("+rezepteDatei+") konnten nicht gelesen werden.");
			throw new KantinenPlanerException(e);
		}
		
		logger.trace("Hitliste verarbeiten");
		String hitliste = config.getHitliste();
		Comparator<Gericht> hitlistenComparator = null;
		try {
			hitlistenComparator = planer.verarbeiteHitliste(hitliste, rezepte);
		} catch (IOException e) {
			logger.warn("Hitliste in ("+hitliste+") konnten nicht gelesen werden.");
		}
		
		/*
		 * Erste grobe Kalkulation, da die Speisepläne noch nicht erstellt wurden.
		 * Für deren Erstellung soll aber bereits berücksichtigt werden, welche Händler potentiell teuerer sind als andere.
		 */
		logger.trace("Kalkuliere die Preismodalitaeten der Haendler fuer die bekannten Rezepte.");
		int maGesamt = planer.getMAGesamt();
		List<BeschaffungsPriorisierung> beschPrioListe = planer.erstelleBeschaffungsPriorisierung(rezepte, maGesamt);

		logger.trace("Setze Comparator fuer den Vergleich von Zutaten.");
		PreisComparator preisComparator = new PreisComparator(beschPrioListe, config.getToleranz());
		GerichtDienst.addGerichteSortierer(preisComparator);
		GerichtDienst.addGerichteSortierer(hitlistenComparator);
		
		logger.trace("Ermittle Planungsperiode");
		Date startdatum;
		Date endedatum;
		try {
			startdatum = config.getStartdatum();
			logger.trace("Startdatum="+startdatum);
			endedatum = config.getEndedatum();
			logger.trace("Startdatum="+endedatum);
		} catch (ParseException e1) {
			throw new KantinenPlanerException("Konnte die Planungsperiode nicht einlesen. Bitte prüfen Sie das Format ["+DatumPlausiHelper.getFormatDatum()+"] in der Konfigurationsdatei.");
		}
		logger.trace("Erstelle Speiseplan ("+startdatum+" bis "+endedatum+")");
		planer.erstelleSpeiseplan(startdatum, endedatum);
		
		logger.trace("Nachkalkulation für Gerichte, die auch tatsächlich gekocht werden sollen laut Speiseplaenen.");
		beschPrioListe = planer.erstelleBeschaffungsPriorisierung(planer.getKantinenListe());
		// Berichtigung der vorläufigen BeschaffungsPriorisierung im PreisComparator, um die nachkalkulierte Priorisierung
		preisComparator.setBeschPrio(beschPrioListe);
		
		logger.trace("Erstelle Beschaffungsliste");
		planer.erstelleBeschaffungsliste();
		
		logger.trace("Erzeuge Ausgabedateien");
		File outputDir = config.getOutputDir();
		String outputError = "Konnte die Ausgabedateien nicht erzeugen, weil auf das Ausgabeverzeichnis nicht zugegriffen werden konnte.";
		if (!outputDir.exists()) {
			logger.debug("Verzeichnis für Ausgaben existiert nicht ("+outputDir+"). Es wird versucht das Verzeichnis anzulegen.");
			if (!outputDir.mkdir()) {
				logger.error("Konnte Verzeichnis fuer die Ausgaben nicht erzeugen.");
				throw new KantinenPlanerException(outputError);
			}
		}
		try {
			OutputFormat format = OutputFormat.valueOf(config.getOutputFormat().toUpperCase());
			planer.erstelleAusgaben(outputDir, format);
		} catch (IOException e) {
			logger.error("Fehler beim erzeugen der Ausgabedateien");
			throw new KantinenPlanerException(outputError);
		}
	}

	private static void print(String msg) {
		System.out.println(msg);
		logger.info(msg);
	}
	
	private static void logProgrammabbruch(Throwable t, String msg) {
		System.out.println(msg);
		logger.fatal(msg, t);
		returncode = 1;
	}

	private KantinenPlanerShell() {}
}
