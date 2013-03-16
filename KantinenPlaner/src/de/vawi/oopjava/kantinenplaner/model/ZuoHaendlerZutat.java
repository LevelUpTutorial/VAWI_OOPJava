/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

/**
 * Zuordnung von Händlern und Zutaten.
 * Hier werden die Haendler mit den von ihnen angebotenen Zutaten verknüpft.
 * @author Julia Meyer
 * @version 3.1
 */
public class ZuoHaendlerZutat {
	private float preisProGebinde;
	private int mengeProGebinde;
	private int maximaleGebinde;
	private Zutat zutat;
		
	/**
	 * Konstruktor
	 * @param preisProGebinde	float 	Verkaufspreis pro üblicher Abgabemenge 
	 * @param mengeProGebinde	int 	Händeler übliche Abgabemenge, Gebindegroeße
	 * @param maximaleGebinde	int 	Maximale Bestellmenge an Gebinden
	 * @param zutat 			Zutat 	dem Händler zugeordnete Zutat
	 */
	public ZuoHaendlerZutat(float preisProGebinde, int mengeProGebinde,
			int maximaleGebinde, Zutat zutat) {
		this.preisProGebinde = preisProGebinde;
		this.mengeProGebinde = mengeProGebinde;
		this.maximaleGebinde = maximaleGebinde;
		this.zutat = zutat;
	}

	/**
	 * Getter für <code>this.preisProGebinde<code>.
	 * @return float Gibt der Preis in Euro pro Gebinde zurück.
	 */
	public float getPreisProGebinde() {
		return preisProGebinde;
	}

	/**
	 * Setter für <code>this.preisProGebinde</code>.
	 * @param preisProGebinde float Der neue Preis pro Gebinde in Euro.
	 */
	public void setPreisProGebinde(float preisProGebinde) {
		this.preisProGebinde = preisProGebinde;
	}

	/**
	 * Getter für <code>this.zutat<code>.
	 * @return Zutat Gibt die zugeordnete Zutat zurück.
	 */
	public Zutat getZutat() {
		return zutat;
	}

	/**
	 * Setter für <code>this.zutat</code>.
	 * @param zutat Zutat Die neuen Zutat.
	 */
	public void setZutat(Zutat zutat) {
		this.zutat = zutat;
	}

	/**
	 * Getter für <code>this.mengeProGebinde</code>.
	 * @return int Gibt die Gebindegroeße zurück.
	 */
	public int getMengeProGebinde() {
		return mengeProGebinde;
	}

	/**
	 * Setter für <code>this.mengeProGebinde</code>
	 * @param mengeProGebinde int Setzt <code>this.mengeProGebinde</code> auf <code>mengeProGebinde</code>.
	 */
	public void setMengeProGebinde(int mengeProGebinde) {
		this.mengeProGebinde = mengeProGebinde;
	}

	/**
	 * Getter für <code>this.maximaleGebinde</code>.
	 * @return int Gibt die maximal zu verkaufenden Gebinde zurück.
	 */
	public int getMaximaleGebinde() {
		return maximaleGebinde;
	}

	/**
	 * Setter für <code>this.maximaleGebinde</code>
	 * @param maximaleGebinde int Setzt <code>this.maximaleGebinde</code> auf <code>maximaleGebinde</code>.
	 */
	public void setMaximaleGebinde(int maximaleGebinde) {
		this.maximaleGebinde = maximaleGebinde;
	}
	
	/**
	 * Erzeugt einen Hash-Code aus dem Objekt mit den Attributen Einheit, Name und Typ
	 * @return int Hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maximaleGebinde;
		result = prime * result + mengeProGebinde;
		result = prime * result + Float.floatToIntBits(preisProGebinde);
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
		ZuoHaendlerZutat other = (ZuoHaendlerZutat) obj;
		if (maximaleGebinde != other.maximaleGebinde)
			return false;
		if (mengeProGebinde != other.mengeProGebinde)
			return false;
		if (Float.floatToIntBits(preisProGebinde) != Float
				.floatToIntBits(other.preisProGebinde))
			return false;
		if (zutat == null) {
			if (other.zutat != null)
				return false;
		} else if (!zutat.equals(other.zutat))
			return false;
		return true;
	}
}
