/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools;

/**
 * Strategie für die Berechnung von Transportkosten. Der Transportkostenrechner definiert eine Methode zur Berechnung
 * der Transportkosten anhand der Anzahl gekaufter Artikel und einer Transportkostenbasis.
 * 
 * @see http://de.wikipedia.org/wiki/Strategie_%28Entwurfsmuster%29
 * @author Matthias Rohe
 * @version 2.1
 */
public interface TransportKostenRechner {
	/**
	 * Berechnet die Transportkosten
	 * @param anzahlArtikel int Anzahl der Artikel
	 * @param kostenBasis float Die Basis zur Berechnung der Transportkosten.
	 * @return float Gibt die Transportkosten aufgrund einer konkreten Strategie zurück.
	 */
	public float getTransportkosten(int anzahlArtikel, float kostenBasis);
}
