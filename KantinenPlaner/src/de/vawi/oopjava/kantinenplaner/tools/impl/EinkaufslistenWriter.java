/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

import de.vawi.oopjava.kantinenplaner.model.HaendlerPos;
import de.vawi.oopjava.kantinenplaner.model.ZutatPos;

/**
 * Dient zum Schreiben der Einkaufsliste
 * Ausgegeben werden Händler, Zutat, Menge und Einheit
 * Mögliche Ausgabeformate: Text und HTML
 * Beide Formate werden in einen Outputstream geschrieben
 * 
 * @author Matthias Rohe
 * @version 2.1
 */

public class EinkaufslistenWriter extends AbstractBeschaffungslistenWriter {

	private static final DecimalFormat df = new DecimalFormat("0.00"); // Formatierung von Werten auf zwei Nachkommastellen

	/**
	 * In dieser Methode wird der gepufferte String (Text-Format) in den Outputstream geschrieben.
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.Writer#writeAsText(java.io.OutputStream)
	 * @throws IOException Wird geworfen, wenn beim Schreiben der Ausgabe ein Fehler auftritt.
	 * @param outputStream OutputStream Der Ausgangsstrom.
	 */
	@Override
	public void writeAsText(OutputStream outputStream) throws IOException {

		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream)); // Öffnen des Outputstreams

		w.write(createText());  // Schreiben der Text-Ausgabe in den Outputstream
		
		w.flush(); // Schreiben der gepufferten Daten
		w.close(); // Schließen des Outputstreams

	}

	/**
	 * In dieser Methode wird der gepufferte String (Html-Format) in den Outputstream geschrieben
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.Writer#writeAsText(java.io.OutputStream)
	 * @throws IOException Wird geworfen, wenn beim Schreiben der Ausgabe ein Fehler auftritt.
	 * @param outputStream OutputStream Der Ausgangsstrom.
	 */
	@Override
	public void writeAsHtml(OutputStream outputStream) throws IOException {
		

		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream));  // Öffnen Outputwriter
		
		w.write(createHtml());  // Schreiben der Html-Ausgabe in den Outputstream
		
		w.flush(); // Schreiben der gepufferten Daten
		w.close(); // Schließen des Outputstreams

	}


	/**
	 * In dieser Methode wird die Textausgabe der Einkaufsliste in den StringBuffer geschrieben
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.Writer#createText(java.io.OutputStream)
	 * @return Text-Ausgabe als String
	 */
	
	@Override
	public String createText() {
		
		// Deklaration StringBuffer
		
		StringBuffer buffer = new StringBuffer();		

		for (HaendlerPos haendlerpos : beschaffungsliste.getHaendlerPositionenListe()) { //Schleifendurchlauf Händler

			String haendlername = haendlerpos.getHaendler().getName();

			// Überschrift: Händlername
			
			buffer.append("-----------------------------------------\n");
			buffer.append(haendlername+"\n");
			buffer.append("-----------------------------------------\n\n");
			
			for (ZutatPos zutatenpos : haendlerpos.getZutatPostionenListe()) { // Schleifendurchlauf Zutat und Menge je Händler
				String zutatname = zutatenpos.getZutat().getName();
				float zutatmenge = zutatenpos.getMenge();
				String menge = df.format(zutatmenge);  // Formatierung Zutatmenge mit zwei Nachkommastellen
				buffer.append(zutatname + ";" + menge + "\n");
			}
			buffer.append("\n");
		}
		
		return buffer.toString();
	}

	/**
	 * In dieser Methode wird die Html-Ausgabe der Einkaufsliste in den StringBuffer geschrieben
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.Writer#createHtml(java.io.OutputStream)
	 * @return  Html-Ausgabe als String
	 */
	
	@Override
	public String createHtml() {
		
		// Deklaration StringBuffer
		
		StringBuffer buffer = new StringBuffer();	
		
		// Infos zum HTML und Überschrift
		
		buffer.append("<!DOCTYPE html PUBLIC \'-//W3C//DTD HTML 4.01//EN\' \'http://www.w3.org/TR/html4/strict.dtd\'>");
		buffer.append("<html><head>");
		buffer.append("<title>Kantinenplaner Einkaufsliste</title>");
		buffer.append("</head>");
		buffer.append("<body>");

		// Variable Name der Kantine einfügen
		
		buffer.append("<h1 bgcolor=\'#000080\'><font face=\'Comic Sans MS\' color= \'#FFFFFF\' size=\'6\'> Einkaufsliste </font></h1>");


		for (HaendlerPos haendlerpos : beschaffungsliste.getHaendlerPositionenListe()) {  //Schleifendurchlauf Händler

			int position = 1;		   //Index für Postition Zutat/Menge	
			String haendlername = haendlerpos.getHaendler().getName();
			buffer.append("<h3>Händler: " + haendlername + "</h3>");     
			buffer.append("<table width = \'400\'  frame=\'void\' style=\'border-color: silver; border-style:none\' border=\'1\'>");
			buffer.append("<tbody>");
			buffer.append("<tr><th>Position</th><th>Zutat</th><th>Menge</th></tr>");
			buffer.append("<tr>");

			for (ZutatPos zutatenpos : haendlerpos.getZutatPostionenListe()) {  // Schleifendurchlauf Zutat und Menge je Händler
				String zutatname = zutatenpos.getZutat().getName();
				float zutatmenge = zutatenpos.getMenge();
				buffer.append("<td>" + position + "</td>");
				buffer.append("<td>" + zutatname + "</td>");
				String menge = df.format(zutatmenge);     // Formatierung Zutatmenge mit zwei Nachkommastellen
				buffer.append("<td>" + menge + "</td>");
				position = position + 1;	
				buffer.append("</tr>");
			}
			//buffer.append("</tbody><table>");
			buffer.append("</tbody></table>");
		}
		buffer.append("</body>");
		buffer.append("</html>");
		
		return buffer.toString();        
	}


}
