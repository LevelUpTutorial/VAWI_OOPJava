/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

/**
 * Containerklasse, um einen Händler und einen Preis zu verbinden. 
 * Der Preis setzt sich aus dem Preis pro Einheit, einer bestimmten Zutat und den anteiligen Transportkosten zusammen, 
 * die bei einer bestimmten Bestellung anteilig zugeordnet werden können.
 * 
 * @see {@link BeschaffungsPriorisierung}
 * @author Kim
 * @version 1.0
 */
public class ZuoHaendlerAnschKosten implements Comparable<ZuoHaendlerAnschKosten>{
	/** Der Händler */
	private Haendler haendler;
	/** Der Preis pro Einheit einer Zutat inklusive der anteiligen Transportkosten */
	private float bruttoPreis;
	
	/**
	 * Konstruktor
	 * @param haendler Haenlder Der Händler
	 * @param bruttoPreis float Der Bruttopreis inklusive der anteiligen Transportkosten
	 */
	public ZuoHaendlerAnschKosten(Haendler haendler, float bruttoPreis) {
		this.haendler = haendler;
		this.bruttoPreis = bruttoPreis;
	}

	/**
	 * Vergleicht zwei Instanzen von <code>HaendlerPriorisierung</code>, durch Vergleich von <code>bruttoPreis</code>.
	 * Diese Methode liefert das selbe Ergebnis wie der Aufruf <code>Float.compare(this.bruttoPreis, o.bruttoPreis);</code>.
	 * @see {@link java.lang.Comparable#compareTo(Object)}
	 */
	@Override
	public int compareTo(ZuoHaendlerAnschKosten o) {
		return Float.compare(this.bruttoPreis, o.bruttoPreis);
	}

	/**
	 * Getter für <code>this.haendler</code>.
	 * @return Haendler Gibt <code>this.haendler</code> zurück.
	 */
	public Haendler getHaendler() {
		return haendler;
	}

	/**
	 * Getter für <code>this.bruttoPreis</code>.
	 * @return float Gibt <code>this.bruttoPreis</code> zurück.
	 */
	public float getBruttoPreis() {
		return bruttoPreis;
	}

}
