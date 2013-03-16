/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

import java.util.ArrayList;
import java.util.List;

import de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil;

/**
 * Repräsentation einer Zutat.
 * Hier wird die Zutat mit ihren Eigenschaften verwaltet
 * @author Julia Meyer
 * @version 3.0
 * @see http://de.wikipedia.org/wiki/Fabrikmethode
 * @see http://de.wikipedia.org/wiki/Singleton_%28Entwurfsmuster%29
 * 
 */
public class Zutat {
	private static List<Zutat> zutatInstanzen = new ArrayList<Zutat>();
	/** Ein Eindeutiger Name einer Zutat */
	private String name;
	private String einheit;
	private ZutatTyp typ;

	/**
	 * Konstruktor
	 * @param name String Bezeichnung der Zutat
	 * @param einheit String Maßeinheit der Zutat (g, ml etc.)
	 * @param typ ZutatTyp Eigenschaft der Zutat (Veggi, Fisch oder Fleisch)
	 */
	private Zutat(String name, ZutatTyp typ, String einheit) {
		if (name == null) {
			throw new IllegalArgumentException("Der Parameter name darf nicht NULL sein.");
		}
		this.name = name;
		this.typ = typ;
		this.einheit = einheit;
	}

	/**
	 * Ausgabe einer Zutat bei Angabe des Names
	 * @param name String Bezeichnung der Zutat
	 * @return Zutat gibt die gefundene Zutat oder <code>null</code> zurück.
	 */
	public static Zutat getZutat(String name) {
		for (Zutat z : zutatInstanzen) {
			if (z.name.equals(name)) {
				return z;
			}
		}
		return null;
	}
	
	/**
	 * Factorymethode zum Erstellen einer Zutaten.
	 * @param name String Bezeichnung der zuerstellenden Zutat
	 * @param typ ZutatTyp Eigenschaft der Zutat (Veggi, Fisch oder Fleisch)
	 * @param Einheit String Masseinheit der Zutat 
	 * @return Zutat gibt die erzeugte Zutaten-Objekt zurück 
	 */
	public static Zutat newZutat(String name, ZutatTyp typ, String einheit) {
		if (name == null) {
			throw new IllegalArgumentException("Der Parameter name darf nicht NULL sein.");
		}
		if (getZutat(name) != null) {
			throw new IllegalArgumentException("Zu diesem Namen ist bereits eine Zutat vorhanden.");
		}
		Zutat zutat = new Zutat(name, typ, einheit);
		CollectionUtil.addItemIfNotContained(zutatInstanzen, zutat);
		return zutat;
	}
	
	/**
	 * Getter für <code>this.typ</code>.
	 * @return ZutatTyp Gibt die Art der Zutat zurück.
	 */
	public ZutatTyp getTyp() {
		return typ;
	}

	/**
	 * Setter für <code>this.typ</code>.
	 * @param typ ZutatTyp Setzt <code>this.typ</code> auf <code>typ</code>.
	 */
	public void setTyp(ZutatTyp typ) {
		this.typ = typ;
	}

	/**
	 * Getter für <code>this.einheit</code>
	 * @return String Einheit Gibt die Einheit der Zutat zurück.
	 */
	public String getEinheit() {
		return einheit;
	}

	/**
	 * Setter für <code>this.einheit</code>.
	 * @param einheit String Setzt <code>this.einheit</code> auf <code>einheit</code>.
	 */
	public void setEinheit(String einheit) {
		this.einheit = einheit;
	}

	/**
	 * Getter für <code>this.name</code>.
	 * @return String Gibt den Namen der Zutat zurück.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter für <code>this.name</code>.
	 * @param name String Setzt <code>this.name</code> auf <code>name</code>, wenn gilt <code>Zutat.getZutat(name) == null</code>.
	 */
	public void setName(String name) {
		if (getZutat(name) != null) {
			throw new IllegalArgumentException("Der Name ("+name+") ist bereits vergeben!");
		}
		this.name = name;
	}

	/**
	 * Erzeugt einen Hash-Code aus dem Objekt mit den Attributen Einheit, Name und Typ
	 * @return int Hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((einheit == null) ? 0 : einheit.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((typ == null) ? 0 : typ.hashCode());
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
		Zutat other = (Zutat) obj;
		if (einheit == null) {
			if (other.einheit != null)
				return false;
		} else if (!einheit.equals(other.einheit))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (typ != other.typ)
			return false;
		return true;
	}

}
