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
 * Dient zum schreiben der Preisliste in verschiedenen Formaten (Text und Html
 * sind derzeit vorgesehen). Je Händler werden die Kosten je Zutat, die Tranportkosten
 * sowie die Gesamtkosten dargestellt.
 * 
 * @author Matthias Rohe
 * @version 2.2k
 */
public class KostenWriter extends AbstractBeschaffungslistenWriter {
	
	private static final  DecimalFormat df = new DecimalFormat("0.00"); // Formatierung von Werten auf zwei Nachkommastellen
	
	/** 
	 * In dieser Methode wird der gepufferte String (Text-Format) in den Outputstream geschrieben
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.Writer#writeAsText(java.io.OutputStream)
	 * @throws IOException Wird geworfen, wenn beim Schreiben der Ausgabe ein Fehler auftritt.
	 * @param outputStream OutputStream Der Ausgangsstrom.
	 */


	@Override
	public void writeAsText(OutputStream outputStream) throws IOException {

		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream)); // Öffnen des Outputstreams

		w.append(createText());  // Schreiben der Text-Ausgabe in den Outputstream

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

		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream)); // Öffnen des Outputstreams

		w.append(createHtml());    // Schreiben der Html-Ausgabe in den Outputstream
		
		w.flush(); // Schreiben der gepufferten Daten
		w.close(); // Schließen des Outputstreams

	}

	/** 
	 * In dieser Methode wird die Textausgabe der Einkaufsliste in den StringBuffer geschrieben
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.Writer#createText.
	 * @return Text-Ausgabe als String
	 */
	
	@Override
	public String createText() {
		
		// Deklaration StringBuffer
		
		StringBuffer buffer = new StringBuffer();
		
		for (HaendlerPos haendlerpos : beschaffungsliste.getHaendlerPositionenListe()) {   //Schleifendurchlauf Händler

			String haendlername = haendlerpos.getHaendler().getName();
			
			buffer.append("-----------------------------------------\n");
			buffer.append(haendlername+"\n");
			buffer.append("-----------------------------------------\n\n");			

			for (ZutatPos zutatenpos : haendlerpos.getZutatPostionenListe()) {    // Schleifendurchlauf Zutat und Kosten je Händler

				String zutatname = zutatenpos.getZutat().getName();
				float zutatpreis = haendlerpos.getHaendler().getPreisProEinheit(zutatname) * zutatenpos.getMenge();
				String preis = df.format(zutatpreis); // Formatierung zutatpreis mit zwei Nachkommastellen
				
				buffer.append(zutatname + ";" + preis + " €\n");
			}

			String t_kosten = df.format(haendlerpos.getTransportkosten());   // Formatierung transportkosten mit zwei Nachkommastellen
			buffer.append("\n" + "Transportkosten: " + t_kosten + " €" + "\n\n");

		}

		// Gesamtkosten wiedergeben

		String g_kosten = df.format(beschaffungsliste.getGesamtkosten());   // Formatierung gesamtkosten mit zwei Nachkommastellen

		buffer.append("-----------------------------------------------------------\n");
		buffer.append("Gesamtkosten: " + g_kosten + " €\n");
		buffer.append("-----------------------------------------------------------\n");		
		
		return buffer.toString();
	}

	/** 
	 * In dieser Methode wird die Html-Ausgabe der Einkaufsliste in den StringBuffer geschrieben
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.Writer#createHtml
	 * @throws IOException Wird geworfen, wenn beim Schreiben der Ausgabe ein Fehler auftritt.
	 * @return Html-Ausgabe als String
	 */
	
	@Override
	public String createHtml() {
		
		// Deklaration StringBuffer
		
		StringBuffer buffer = new StringBuffer();

		buffer.append("<!DOCTYPE html PUBLIC \'-//W3C//DTD HTML 4.01//EN\' \'http://www.w3.org/TR/html4/strict.dtd\'>");
		buffer.append("<html><head>");
		buffer.append("<title>Kantinenplaner Deluxe</title>");
		buffer.append("</head>");
		buffer.append("<body>");

		// Variable Name der Kantine einfügen

		buffer.append("<h1 bgcolor=\'#000080\'><font face=\'Comic Sans MS\' color= \'#FFFFFF\' size=\'6\'> Kostenübersicht </font></h1>");


		
		for (HaendlerPos haendlerpos : beschaffungsliste.getHaendlerPositionenListe()) {
			
			String haendlername = haendlerpos.getHaendler().getName();
			
			int position = 1;
			float h_kosten = 0;     // zur Aufsummierung der Händlerkosten (Händlergesamtkosten)
			
			buffer.append("<h3>Händler: " + haendlername + "</h3>");
			buffer.append("<table width = \'500\' frame=\'void\' style=\'border-color: silver; border-style:none\' border=\'1\'>");
			buffer.append("<tbody>");
			buffer.append("<tr><th>Position</th><th>Zutat</th><th>Preis</th></tr>");
			buffer.append("<tr>");

			for (ZutatPos zutatenpos : haendlerpos.getZutatPostionenListe()) {

				String zutatname = zutatenpos.getZutat().getName();
				
				float zutatpreis = haendlerpos.getHaendler().getPreisProEinheit(zutatname) * zutatenpos.getMenge();
				
				h_kosten = h_kosten + zutatpreis;
				
				String preis = df.format(zutatpreis);  // Formatierung zutatpreis mit zwei Nachkommastellen
				
				buffer.append("<td width=\'15%\' >" + position + "</td>");
				buffer.append("<td width=\'65%\' >" + zutatname + "</td>");
				buffer.append("<td width=\'20%\' >" + preis + " €</td>");
				buffer.append("</tr>\n");		
				
				position = position + 1;
			}
			
			// Einfügen der Transportkosten
			buffer.append("<tr>");
			buffer.append("<td></td>");
			buffer.append("<td><b>Tansportkosten:</b></td>");
			
			String t_kosten = df.format(haendlerpos.getTransportkosten());   // Formatierung transportkosten mit zwei Nachkommastellen

			buffer.append("<td><b>" + t_kosten + " €</b></td>");
			buffer.append("</tr>");

			// Einfügen der Gesamskosten des Händlers

			buffer.append("<tr>");
			buffer.append("<td></td>");
			buffer.append("<td><b>gesamte Kosten:</b></td>");
			
			h_kosten = h_kosten + haendlerpos.getTransportkosten();
			String hk_kosten = df.format(h_kosten);   // Formatierung Händlergesamtkosten mit zwei Nachkommastellen
			
			buffer.append("<td><b>" + hk_kosten + " €</b></td>");
			buffer.append("</tr>");
			
			buffer.append("</tbody></table>");

		}

		// Gesamtkosten (aller Händler) wiedergeben

		String g_kosten = df.format(beschaffungsliste.getGesamtkosten()); // Formatierung Gesamtkosten mit zwei Nachkommastellen
		
		buffer.append("<h3><font face=\'Comic Sans MS\' size=\'6\'> Gesamtkosten : "+ g_kosten + " €</font></h3>");


		buffer.append("</body>");
		buffer.append("</html>");
		
		
		return buffer.toString();
		
	}
}
