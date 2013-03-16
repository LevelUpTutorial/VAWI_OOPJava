/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import de.vawi.oopjava.kantinenplaner.tools.TransportKostenRechner;

/**
 * Konkrete Strategie zur Transportkostenberechnung.
 * 
 * <br>Die Kosten werden pro Artikel berechnet.
 * <code>getTransportkosten</code>: benötigt Anzahl Artikel und Kostenbasis und gibt Kosten pro Artikel zurück
 * @author Matthias Rohe
 * @version 2.0
 */
public class TKProArtikel implements TransportKostenRechner {

	/**
	 *  Methode, in der Anzahl Artikel mit der Kostenbasis
	 *  multipliziert wird.
	 * 
	 * @param anzahlArtikel Anzahl Artikel
	 * @param kostenbasis  Kostenbasis
	 * @see de.vawi.oopjava.kantinenplaner.tools.TransportKostenRechner#getTransportkosten(int, float)
	 * @return anzahlArtikel * kostenBasis 
	 */
	@Override
	public float getTransportkosten(int anzahlArtikel, float kostenBasis) {

		
		return (float) (anzahlArtikel * kostenBasis);
	}

}
