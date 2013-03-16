/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * Repräsentation einer Kantine. 
 * @author Matthias Rohe
 * @version 2.1
 */
public class Kantine {
	private static final Logger logger = LogManager.getLogger(Kantine.class); // Informationen gehen ans Loggingsystem
	private List<Gericht> gerichteListe = new ArrayList<Gericht>();
	private List<SpeiseplanPos> speiseplan = new ArrayList<SpeiseplanPos>();
	private int anzahlMitarbeiter;
	private String name;
	
	/**
	 * Konstruktor
	 * @param anzahlMitarbeiter int 
	 * 							Die Anzahl Mitarbeiter dieses Standortes.
	 * @param name String 
	 * 							Der Name der Kantine.
	 */
	public Kantine(int anzahlMitarbeiter, String name) {
		this.anzahlMitarbeiter = anzahlMitarbeiter;
		this.name = name;
	}

	/**
	 * Getter für <code>this.gerichteListe</code>.
	 * @return List&lt;Gericht&gt; Gibt eine Kopie von <code>this.gerichteListe</code> zurück.
	 */
	public List<Gericht> getGerichteListe() {
		List<Gericht> copy = new ArrayList<Gericht>(gerichteListe);
		return copy;
	}

	/**
	 * Ersetzt <code>this.gerichteListe</code> durch <code>gerichteListe</code>, wenn <code>gerichteListe != null</code>.
	 * @param gerichteListe List&lt;Gericht&gt; Die neue Gerichteliste.
	 */
	public void setGerichteListe(List<Gericht> gerichteListe) {
		if (gerichteListe == null) {
			logger.debug("Parameter gerichteListe hat den Wert NULL.");
			throw new IllegalArgumentException("Parameter gerichteListe darf nicht NULL sein.");		

		}
		this.gerichteListe = gerichteListe;
	}

	/**
	 * Getter für <code>this.anzahlMitarbeiter</code>.
	 * @return int Gibt die Anzahl der Mitarbeiter zurück.
	 */
	public int getAnzahlMitarbeiter() {
		return anzahlMitarbeiter;
	}

	/**
	 * Setter für <code>this.anzahlMitarbeiter</code>.
	 * @param anzahlMitarbeiter int Die neue Anzahl Mitarbeiter.
	 */
	public void setAnzahlMitarbeiter(int anzahlMitarbeiter) {
		this.anzahlMitarbeiter = anzahlMitarbeiter;
	}

	/**
	 * Getter für <code>this.name</code>.
	 * @return String Der Name der Kantine.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter für <code>this.name</code>
	 * @param name String Der neue Name der Kantine.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
     * Gibt eine Kopie von <code>this.speiseplan</code> zurück.
     * @return List&lt;SpeiseplanPos&gt; Gibt den Speiseplan als Map ein Datum 
     * als Schlüssel und eine Gerichteliste als Wert zurück.
     */
    public List<SpeiseplanPos> getSpeiseplan() {
        return new ArrayList<SpeiseplanPos>(speiseplan);
    }

    /**
     * Ersetzt <code>this.speiseplan</code> durch <code>speiseplan</code>, <code>speiseplan != null</code>.
     * @param speiseplan List&lt;SpeiseplanPos&gt; Der neue Speiseplan.
     */
    public void setSpeiseplan(List<SpeiseplanPos> speiseplan) {
        if (speiseplan == null) {
        	logger.debug("Parameter speiseplan hat den Wert NULL.");
            throw new IllegalArgumentException("Parameter speiseplan darf nicht NULL sein.");
        }
        this.speiseplan = speiseplan;
    }


	/**
	 * Erzeugt einen Hash-Code aus dem Objekt mit den Attributen AnzahlMitarbieter, Name und Gericht
	 * 
	 * @return int Hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahlMitarbeiter;
		result = prime * result
				+ ((gerichteListe == null) ? 0 : gerichteListe.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((speiseplan == null) ? 0 : speiseplan.hashCode());
		return result;
	}

	/** 
	 * * Vergleicht das Objekt mit einem Anderen auf Gleichheit
	 * 
	 * @param Object obj Vergleichsobjekt
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @return Ergebnis des Vergleichs
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kantine other = (Kantine) obj;
		if (anzahlMitarbeiter != other.anzahlMitarbeiter)
			return false;
		if (gerichteListe == null) {
			if (other.gerichteListe != null)
				return false;
		} else if (!gerichteListe.equals(other.gerichteListe))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (speiseplan == null) {
			if (other.speiseplan != null)
				return false;
		} else if (!speiseplan.equals(other.speiseplan))
			return false;
		return true;
	}
}
