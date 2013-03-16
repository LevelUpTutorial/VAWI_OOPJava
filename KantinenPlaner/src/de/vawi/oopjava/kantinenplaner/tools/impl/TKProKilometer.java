/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import de.vawi.oopjava.kantinenplaner.tools.TransportKostenRechner;

/**
 * Konkrete Strategie zur Transportkostenberechnung.
 * 
 * <br>Die Kosten werden pauschal pro Lieferung berechnet. <code>TKProBestellung</code> hält Händlerpauschale als
 * default Wert vor.
 * 
 * @author Matthias Rohe
 * @version 2.1
 */
public class TKProKilometer implements TransportKostenRechner {
	private static float pauschaleProKm = 2;
	private float entfernungsKm;
	
	/**
	 * Berechnung Transportkosten: Entfernung in Kilometer multipliziert
	 * mit Pauschale pro Kilometer
	 * 
	 * @ param int anzahlArtikel
	 * @ param float kostenBasis
	 * 
	 * @see de.vawi.oopjava.kantinenplaner.tools.TransportKostenRechner#getTransportkosten(int, float)
	 * @return float entfernungsKm * kostenBasis
	 */
	@Override
	public float getTransportkosten(int anzahlArtikel, float kostenBasis) {
		if (this.entfernungsKm == 0) {
			this.entfernungsKm=pauschaleProKm;
		}
		
		return (float) (entfernungsKm * kostenBasis);
	}

	/**
	 * Getter für <code>this.entfernungsKm</code>.
	 * @return float Gibt <code>entfernungsKm</code> zurück.
	 */
	public float getEntfernungsKm() {
		return entfernungsKm;
	}
	
	/**
	 * Setter für <code>this.entfernungsKm</code>.
	 * 
	 * @param float entfernungsKm 
	 * 							Setzt <code>this.entfernungsKm</code> auf <code>entfernungsKm</code>.
	 */
	public void setEntfernungsKm(float entfernungsKm) {
		this.entfernungsKm = entfernungsKm;
	}
	
	/**
	 * Getter für <code>this.pauschaleProKm</code>.
	 * 
	 * @return float 
	 * 					Gibt die Händlerpauschale zurück.
	 */
	public float getPauschaleProKm() {
		return pauschaleProKm;
	}

	/**
	 * Setter für <code>this.pauschaleProKm</code>.
	 * @param float pauschaleProKm
	 * 					Setzt <code>this.pauschaleProKm</code> auf <code>pauschaleProKm</code>.
	 */
	public void setPauschaleProKm(float pauschaleProKm) {
		TKProKilometer.pauschaleProKm = pauschaleProKm;
	}
}
