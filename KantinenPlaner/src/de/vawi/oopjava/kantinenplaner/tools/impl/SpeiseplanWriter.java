/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Kantine;
import de.vawi.oopjava.kantinenplaner.model.SpeiseplanPos;
import de.vawi.oopjava.kantinenplaner.tools.Writer;

/**
 * Dient zum Schreiben eines Speiseplans in verschiedenen Formaten. Der
 * SpeiseplanWriter kennt den Speiseplan. Dies wird durch die Methode
 * <code>setSpeiseplan</code> sichergestellt.
 * 
 * @author Matthias Rohe
 * @version 2.2
 */
public class SpeiseplanWriter implements Writer {
	
	private List<Kantine> kantinenliste = new ArrayList<Kantine>(); // Formatierung von Werten auf zwei Nachkommastellen


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

		w.write(createText());   // Schreiben der Text-Ausgabe in den Outputstream
		
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

		w.write(createHtml());   // Schreiben der Html-Ausgabe in den Outputstream
		
		w.flush(); // Schreiben der gepufferten Daten
		w.close(); // Schließen des Outputstreams
	}

	/**
	 * Getter für <code>kantine</code>.
	 * 
	 * @return List<Kantine> Gibt <code>kantine</code> zurück.
	 */
	public List<Kantine> getKantine() {
		return kantinenliste;
	}

	/**
	 * Setter für <code>kantine</code>.
	 * 
	 * @param kantine List<Kantine> 
	 * Setzt <code>kantine</code> auf <code>kantine</code>.
	 */
	public void setKantine(List<Kantine> kantine) {
		this.kantinenliste = kantine;
	}
	
	
	
	/**
	 * Methode, in der die Anzahl Gerichte pro Tag in eine html-Tabelle geschrieben werden.
	 * Implementierung als StringBuffer.
	 * 
	 * @param w Buffered Writer Gepufferte Werte
	 * @param Tag String Tag, an dem ein Gericht angeboten wird
	 * @param Datum String Datum, an dem ein Gericht angeboten wird
	 * @param inhalte SpeiseplanPos Liste mit den Gerichten
	 * @throws IOException Wird geworfen, wenn beim Schreiben der Ausgabe ein Fehler auftritt.
	 * @see: writeAsHtml
	 */

	private String schreibeTag(String tag, String Datum, SpeiseplanPos inhalte) {
		

		
		StringBuffer buffer = new StringBuffer();	

		buffer.append("<table  width = \'165\'  frame=\'void\' style=\'border-color: silver; border-style:none\'><tbody>");
		
		//buffer.append("<table style=\'align:left;width:150px\'>"); 
		// Formatierung der Tabelle
		
		buffer.append("<tr bgcolor=\'#000080\' ><th height = \'60\'><font color= \'#FFFFFF\' size =\'3\'>"
				+ tag + ", den " + Datum + "</font></th></tr>");
		
		//Falls  an einem Tag die Kantine geschlossen hat, soll "Kein Angebot" geschrieben werden
		if (inhalte.getGerichte().size() == 0) {

			buffer.append("<tr><td height = \'60\' bgcolor=\'#F5DEB3\' style=\'border-style:none\'>Kein Angebot</td></tr>");
			buffer.append("<tr><td height = \'60\' bgcolor=\'#F5DEB3\' style=\'border-style:none\'>Kein Angebot</td></tr>");
			buffer.append("<tr><td height = \'60\' bgcolor=\'#F5DEB3\' style=\'border-style:none\'>Kein Angebot</td></tr>");
		} else {

			for (int k = 0; k < inhalte.getGerichte().size(); k++) { // Start Befüllung der Gerichte 

				buffer.append("<tr><td height = \'60\' bgcolor=\'#DCDCDC\' style=\'border-style:none\'> " + inhalte.getGerichte().get(k).getName() + "</td></tr>");
				
			} // Ende Befüllung		
		}
		buffer.append("</tbody></table>");
		
		return buffer.toString();

	}

	/**
	 * In dieser Methode wird der gepufferte String (Text-Format) in den Outputstream geschrieben
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.Writer#createText
	 * 
	 */
	@Override
	public String createText() {
		
		StringBuffer buffer = new StringBuffer();

		Calendar cal = Calendar.getInstance();

		buffer.append("kantine;datum;wochentag;name\n");

		for (Kantine k : kantinenliste) {
			List<SpeiseplanPos> speiseplan = k.getSpeiseplan();
			int size = speiseplan.size();
			for (int i = 0; i < size; i++) {
				SpeiseplanPos pos = speiseplan.get(i);
				Date date = pos.getDatum();
				cal.setTime(date);
				String Datum = cal.get(Calendar.DATE) + "."
						+ (cal.get(Calendar.MONTH) + 1) + "."
						+ cal.get(Calendar.YEAR);
				buffer.append("\n" + k.getName() + ";" + Datum + ";"
						+ cal.get(Calendar.DAY_OF_WEEK) + "\n");
				for (Gericht gericht : pos.getGerichte()) {
					buffer.append(gericht.getName() + "\n");
				}

			}
		}
		
		return buffer.toString();
	}

	/** 
	 * In dieser Methode wird die Html-Ausgabe des Speisplans in den StringBuffer geschrieben
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.Writer#createHtml
	 * @throws IOException Wird geworfen, wenn beim Schreiben der Ausgabe ein Fehler auftritt.
	 * @return Html-Ausgabe als String
	 */
	
	@Override
	public String createHtml() {
		
		StringBuffer buffer = new StringBuffer();
		
		Calendar cal = Calendar.getInstance();


		buffer.append("<!DOCTYPE html PUBLIC \'-//W3C//DTD HTML 4.01//EN\' \'http://www.w3.org/TR/html4/strict.dtd\'>");
		buffer.append("<html><head>");
		//buffer.append("<meta http-equiv=\'content-type\' content=\'text/html; charset=ISO-8859-1\'>");
		buffer.append("<title>Kantinenplaner Deluxe</title>");
		buffer.append("</head>");
		buffer.append("<body>");

		for (Kantine k : kantinenliste) {
			
			// Variable Name der Kantine einfügen
			
			buffer.append("<h1 bgcolor=\'#000080\'><font face=\'Comic Sans MS\' color= \'#FFFFFF\' size=\'6\'> Speiseplan Kantine "
					+ k.getName() + "</font></h1>"); 
			buffer.append("<br");
			
            // Ist der erste Listeneintrag kein Montag, werden Datumswerte dem Speiseplan hinzugefügt
			// Damit wird sichergestellt, dass die Kalenderwoche mit einem Montag startet
			
			List<SpeiseplanPos> speiseplan = k.getSpeiseplan();
			SpeiseplanPos calspeiseplanPos = speiseplan.get(0);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(calspeiseplanPos.getDatum());
			while (cal2.get(Calendar.DAY_OF_WEEK) > Calendar.MONDAY) {
				cal2.add(Calendar.DAY_OF_YEAR, -1);
				speiseplan.add(0, new SpeiseplanPos(cal2.getTime()));
			}
			
			// Prüfen, um welchen Wochentag es sich handelt
			// entsprechend wird über die schreibeTag-Methode ein Tabellenelement eingefügt
			
			
			
			for (SpeiseplanPos pos : speiseplan) {
				
				String tag = "";
				Date date = pos.getDatum();
				cal.setTime(date);
				String Datum = cal.get(Calendar.DATE) + "."
						+ (cal.get(Calendar.MONTH) + 1) + "."
						+ cal.get(Calendar.YEAR);
				int i = cal.get(Calendar.DAY_OF_WEEK);
				switch (i) {
				case Calendar.MONDAY:
					tag = "Montag";
					buffer.append("<h2>Kalenderwoche "
							+ cal.get(Calendar.WEEK_OF_YEAR) + " ("
							+ cal.get(Calendar.DATE) + ". bis  "
							+ (cal.get(Calendar.DATE) + 6) + "."
							+ (cal.get(Calendar.MONTH) + 1) + "."
							+ cal.get(Calendar.YEAR) + ")</h2>");
					buffer.append("<table width = \'1200\'><td>");  //äußere Tabelle zur Formatierung
					buffer.append(schreibeTag(tag, Datum, pos));
					buffer.append("</td>");
					break;
				case Calendar.TUESDAY:
					tag = "Dienstag";
					buffer.append("<td>");
					buffer.append(schreibeTag(tag, Datum, pos));
					buffer.append("</td>");
					break;
				case Calendar.WEDNESDAY:
					tag = "Mittwoch";
					buffer.append("<td>");
					buffer.append(schreibeTag(tag, Datum, pos));
					buffer.append("</td>");
					break;
				case Calendar.THURSDAY:
					tag = "Donnerstag";
					buffer.append("<td>");
					buffer.append(schreibeTag(tag, Datum, pos));
					buffer.append("</td>");
					break;
				case Calendar.FRIDAY:
					tag = "Freitag";
					buffer.append("<td>");
					buffer.append(schreibeTag(tag, Datum, pos));
					buffer.append("<td>");
					break;
				case Calendar.SATURDAY:
					tag = "Samstag";
					buffer.append("<td>");
					buffer.append(schreibeTag(tag, Datum, pos));
					buffer.append("</td>");
					break;
				case Calendar.SUNDAY:
					tag = "Sonntag";
					buffer.append("<td>");
					buffer.append(schreibeTag(tag, Datum, pos));
					buffer.append("</td></table>");
					buffer.append("<br"); 
					break;
					
					
				}

			}
		}

		buffer.append("</body>");
		buffer.append("</html>");
		
		return buffer.toString();
	}
}
