/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Kim Michael Jansen
 * @version 1.0
 */
public class SpeiseplanPos implements Comparable<SpeiseplanPos> {
	private Date datum;
	private List<Gericht> gerichte = new ArrayList<Gericht>();

	/**
	 * Konstruktor
	 * @param datum Date Das Datum
	 */
	public SpeiseplanPos(Date datum) {
		this.datum = datum;
	}
	
	/**
	 * Vergleicht zwei Speisepl�nen anhand ihres Datums. Der Methodenaufruf liefert das selbe Ergebnis wie
	 * <code>this.getDatum().compareTo(speiseplanPos.getDatum());</code>
	 * @see {@link java.lang.Comparable#compareTo(Object)}
	 */
	@Override
	public int compareTo(SpeiseplanPos speiseplanPos) {
		return datum.compareTo(speiseplanPos.getDatum());
	}

	/**
	 * Getter f�r <code>this.datum</code>
	 * @return Date Gibt <code>this.datum</code> zur�ck.
	 */
	public Date getDatum() {
		return datum;
	}

	/**
	 * Setter f�r <code>this.datum</code>.
	 * @param datum Date Setzt <code>this.datum</code> auf <code>datum</code>.
	 */
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	/**
	 * Getter f�r <code>this.gerichte</code>.
	 * @return List<Gericht> Gibt eine Kopie von <code>this.gerichte</code> zur�ck.
	 */
	public List<Gericht> getGerichte() {
		return new ArrayList<Gericht>(gerichte);
	}

	/**
	 * Setter f�r <code>gerichte</code>
	 * @param gerichte List<Gericht> Setzt <code>gerichte</code> auf <code>gerichte</code>
	 */
	public void setGerichte(List<Gericht> gerichte) {
		if (gerichte == null) {
			throw new IllegalArgumentException("Parameter gerichte darf nicht NULL sein");
		}
		this.gerichte = gerichte;
	}

	/**
	 * F�gt <code>gericht</code> in <code>this.gerichte<code> hinzu, wenn dieses noch nicht enthalten ist.
	 * @param gericht Gericht Das Gericht.
	 */
	public void addGerichte(Gericht gericht) {
		if (gerichte == null) {
			throw new IllegalArgumentException("Parameter gericht darf nicht NULL sein");
		}
		if (!gerichte.contains(gericht)) {
			gerichte.add(gericht);
		}
	}
	
	/**
	 * L�scht <code>gericht</code> aus <code>this.gerichte<code>.
	 * @param gericht Gericht Das Gericht.
	 */
	public void removeGerichte(Gericht gericht) {
		gerichte.remove(gericht);
	}

	/** 
	 * Textuelle Repr�sentation einer SpeiseplanPosition
	 * @see java.lang.Object#toString()
	 * @return String "SpeiseplanPos (datum=[this.datum])[\nGericht: [Name des Gerichts]]*"
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("SpeiseplanPos (datum=" + datum + ")");
		for (Gericht g : gerichte) {
			buffer.append("\nGericht: " + g.getName());
		}
		return  buffer.toString();
	}
}
