/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Writer ist eine Schnittstellenklasse und definiert zwei Methoden, 
 * um Daten in verschiedenen Formaten in einen Ausgangsstrom zu schreiben.
 *
 * @author Matthias Rohe
 * @version 2.2
 */
public interface Writer {
	/**
	 * Schreibt das Subjekt/ die Subjekte der Implementierung als Text in einen {@link java.io.OutputStream}.
	 * @param outputStream OutputStream Der Ausgangsstrom.
	 * @throws IOException 
	 */
	public void writeAsText(OutputStream outputStream) throws IOException;
	/**
	 * Schreibt das Subjekt/ die Subjekte der Implementierung als Html in einen {@link java.io.OutputStream}.
	 * @param outputStream OutputStream Der Ausgangsstrom.
	 */
	public void writeAsHtml(OutputStream outputStream) throws IOException;
	
	
	/**
	 * Schreibt den Outputstream für die Text-Ausgabe in einen StringBuffer
	 * @throws IOException Wird geworfen, wenn beim Schreiben der Ausgabe ein Fehler auftritt.
	 */	
	public String createText();

	/**
	 * Schreibt den Outputstream für die Html-Ausgabe in einen StringBuffer
	 * @throws IOException Wird geworfen, wenn beim Schreiben der Ausgabe ein Fehler auftritt.
	 */	
	public String createHtml();
	
}
