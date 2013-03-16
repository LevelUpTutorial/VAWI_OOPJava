package de.vawi.oopjava.kantinenplaner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * @author Kim
 */
public class Log4j2Test {
	// Erstellen des Loggers f�r diese Klasse
	private final Logger logger = LogManager.getLogger(Log4j2Test.class);
	
	/**
	 * Test method for {@link org.apache.logging.log4j.Logger}.
	 */
	@Test
	public void classLogger() {
		/*
		 *  Beispiele f�r Logausgaben und die verschiedenen Loglevel.
		 *  Auf den unteren Logleveln werden diese und alle h�heren Loglevel ausgegeben
		 *  
		 *  Bsp. info gibt info, warn und error aus.
		 */
		logger.trace("Nachricht auf TRACE-Level"); // relativ unwichtige Nachricht, die nur f�r bestimmte Analysen ausgegeben werden sollen
		logger.debug("Nachricht auf DEBUG-Level"); // Ausgaben, um als Entwickler den Programmverlauf zu verfolgen und Fehlersituationen nachvollziehen zu k�nnen (typischerweise Variableninhalte, Methodenbeginn, Methodenende)
		logger.info("Nachricht auf INFO-Level"); // Infos zum Programmverlauf. Grds. f�r Entwickler gedacht und ein bischen wichtiger als debug
		logger.warn("Nachricht auf WARN-Level"); // Warnungen, wenn ein Fehler auftrat, aber das Programm noch sinnvoll weiter laufen kann. Ggf. wenn Fehler automatisiert berichtigt wurden, um dies zu dokumentieren.
		logger.error("Nachricht auf ERROR-Level"); // Schwere Fehler, die nicht ohne Benutzereingriff behoben werden k�nnen. Das Programm st�rtzt nicht ab, muss aber kontrolliert beendet werden, weil ein sinnvoller Programmlauf nicht mehr stattfinden kann. Meist muss das Programm neugestartet werden.
	}
}
