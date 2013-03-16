/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

/**
 * Position von Zutaten, Cache.
 * Hier werden die Zutaten mit Mengenangaben mit Haendler verknüpft.
 * @version 3.0
 * @author Julia Meyer
 */
public class ZutatPos {
	private float menge;
	private Zutat zutat;
	
	/**
	 * Konstruktor
	 * @param menge int Anzahl der Gebinde.
	 * @param zutat Zutat Zutat die gekauft werden soll.
	 */
	public ZutatPos(int menge, Zutat zutat) {
		this.menge = menge;
		this.zutat = zutat;
	}

	/**
	 * Getter für <code>this.menge</code>.
	 * @return int Gibt zu kaufende Menge zurück.
	 */
	public float getMenge() {
		return menge;
	}

	/**
	 * Setter für <code>this.menge</code>.
	 * @param menge int Setzt <code>this.menge</code> auf <code>menge</code>.
	 */
	public void setMenge(float menge) {
		this.menge = menge;
	}

	/**
	 * Getter für <code>this.zutat</code>.
	 * @return Zutat Gibt die Zutat zurück.
	 */
	public Zutat getZutat() {
		return zutat;
	}

	/**
	 * Setter für <code>this.zutat</code>.
	 * @param zutat Zutat Setzt <code>this.zutat</code> auf <code>zutat</code>.
	 */
	public void setZutat(Zutat zutat) {
		this.zutat = zutat;
	}

	/**
	 * Erzeugt einen Hash-Code aus dem Objekt mit den Attributen Einheit, Name und Typ
	 * @return int Hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(menge);
		result = prime * result + ((zutat == null) ? 0 : zutat.hashCode());
		return result;
	}

	/**
	 * Vergleicht das Objekt mit einem Anderen auf Gleichheit
	 * @param Object obj Vergleichsobjekt
	 * return boolean Ergebnis des Vergleichs
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZutatPos other = (ZutatPos) obj;
		if (Float.floatToIntBits(menge) != Float.floatToIntBits(other.menge))
			return false;
		if (zutat == null) {
			if (other.zutat != null)
				return false;
		} else if (!zutat.equals(other.zutat))
			return false;
		return true;
	}

	
	
}
