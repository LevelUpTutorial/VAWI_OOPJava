/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import de.vawi.oopjava.kantinenplaner.tools.TransportKostenRechner;

/**
 * Konkrete Strategie zur Transportkostenberechnung.
 * 
 * <br>Die Kosten werden pauschal pro Lieferung berechnet. <code>TKProBestellung</code> h�lt H�ndlerpauschale als
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
	 * Getter f�r <code>this.entfernungsKm</code>.
	 * @return float Gibt <code>entfernungsKm</code> zur�ck.
	 */
	public float getEntfernungsKm() {
		return entfernungsKm;
	}
	
	/**
	 * Setter f�r <code>this.entfernungsKm</code>.
	 * 
	 * @param float entfernungsKm 
	 * 							Setzt <code>this.entfernungsKm</code> auf <code>entfernungsKm</code>.
	 */
	public void setEntfernungsKm(float entfernungsKm) {
		this.entfernungsKm = entfernungsKm;
	}
	
	/**
	 * Getter f�r <code>this.pauschaleProKm</code>.
	 * 
	 * @return float 
	 * 					Gibt die H�ndlerpauschale zur�ck.
	 */
	public float getPauschaleProKm() {
		return pauschaleProKm;
	}

	/**
	 * Setter f�r <code>this.pauschaleProKm</code>.
	 * @param float pauschaleProKm
	 * 					Setzt <code>this.pauschaleProKm</code> auf <code>pauschaleProKm</code>.
	 */
	public void setPauschaleProKm(float pauschaleProKm) {
		TKProKilometer.pauschaleProKm = pauschaleProKm;
	}
}
