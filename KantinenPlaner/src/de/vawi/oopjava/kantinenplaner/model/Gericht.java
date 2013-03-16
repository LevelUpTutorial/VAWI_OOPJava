package de.vawi.oopjava.kantinenplaner.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Repräsentation eines Gerichts.
 * 
 * @author Matthias Rohe
 * @version 2.2
 */
public class Gericht {
	private static final Logger logger = LogManager.getLogger(Gericht.class); // Informationen gehen ans Loggingsystem
	private String name;
	private int prioritaet;
	private Map<Zutat, Float> zutatenMengenMap = new HashMap<Zutat, Float>();
	
	/**
	 * Konstruktor
	 * 
	 * @param name String 
	 * 					Der Name des Gerichts.
	 */
	public Gericht(String name) {
		this.name = name;
	}


	/**
	 * Getter für <code>this.name</code>
	 * @return String Der Name des Gerichts.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter für <code>this.name</code>
	 * @param name String Setzt <code>this.name</code> auf name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Kopie erstellen und zurückgeben: Eine neue leere map anlegen und über eine Schleife über den 
	 * Schlüssel laufen und in die neue map einfügen. (multi-threading)
	 * Kopie von der Liste in die Kopie der Liste eingegeben.
	 * 
	 * Getter <code>this.zutatenMengenMap</code>
	 * @return Map Zutat,Float; Gibt eine Kopie von <code>this.zutatenMengenMap</code> zurück.
	 */
	public HashMap<Zutat, Float> getZutatenMengenMap() {
		
		HashMap<Zutat, Float> zutatenMengenMapKopie = new HashMap<Zutat, Float>();
		
		for (Entry<Zutat, Float> entry : zutatenMengenMap.entrySet()) {
		    zutatenMengenMapKopie.put((Zutat)entry.getKey(), ((Float) entry.getValue()));
		}
		
	   return zutatenMengenMapKopie;
	}

	/**
	 * Ersetzt <code>this.zutatenMengenMap</code> durch <code>zutatenMengenMap</code>.
	 * @param zutatenMengenMap Map Zutat,Float; Setzt <code>this.zutatenMengenMap</code> auf <code>zutatenMengenMap</code>, wenn <code>zutatenMengenMap != null</code>.
	 */
	public void setZutatenMengenMap(Map<Zutat, Float> zutatenMengenMap) {
		if (zutatenMengenMap == null) {
			logger.debug("Parameter zutatenMengenMap hat den Wert NULL.");
			throw new IllegalArgumentException("Parameter zutatenMengenMap darf nicht NULL sein.");
			
		}
		this.zutatenMengenMap = zutatenMengenMap;
	}
	
	public void putZutatMenge(Zutat zutat, Float menge) {
		zutatenMengenMap.put(zutat, menge);
	}
	
	public void removeZutat(Zutat zutat) {
		zutatenMengenMap.remove(zutat);
	}
	
	
	/**
	 * Getter für <code>this.prioritaet</code>
	 * @return int Gibt <code>this.prioritaet</code> zurück.
	 */
	public int getPrioritaet() {
		return prioritaet;
	}

	/**
	 * Setter für <code>this.prioritaet</code>
	 * @param prioritaet int Setzt <code>this.prioritaet</code> auf <code>prioritaet</code>.
	 */
	public void setPrioritaet(int prioritaet) {
		this.prioritaet = prioritaet;
	}
	
	/**
	 * Prüft die Art des Gerichts anhand seiner Zutaten.
	 * @return boolean Gibt <code>true</code> zurück, wenn mindestens eine Zutat vom Typ <code>ZutatTyp.Fleisch</code> ist.
	 */
	public boolean isFleisch() {
		for (Zutat zutat : zutatenMengenMap.keySet()) {
			if (ZutatTyp.FLEISCH.equals(zutat.getTyp())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Prüft die Art des Gerichts anhand seiner Zutaten.
	 * @return boolean Gibt <code>true</code> zurück, wenn mindestens eine Zutat vom Typ <code>ZutatTyp.Fisch</code> ist und es kein Fleischgericht ist.
	 * @see Gericht#isFleisch()
	 */
	public boolean isFisch() {
        if (!isFleisch()) {
            for (Zutat zutat : zutatenMengenMap.keySet()) {
                if (ZutatTyp.FISCH.equals(zutat.getTyp())) {
                    return true;
                }
            }
        }
        return false;
    }

	
	/**
	 * Prüft die Art des Gerichts anhand seiner Zutaten.
	 * 
	 * @return boolean Gibt <code>true</code> zurück, wenn und nur wenn es kein Fleischgericht und kein Fischgericht ist.
	 * @see Gericht#isFleisch()
	 * @see Gericht#isFleisch()
	 */
	public boolean isVegetarisch() {
		for (Zutat zutat : zutatenMengenMap.keySet()) {
			if (ZutatTyp.FLEISCH.equals(zutat.getTyp())) {
				return false;
			}
			if (ZutatTyp.FISCH.equals(zutat.getTyp())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Erzeugt einen Hash-Code aus dem Objekt
	 * 
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + prioritaet;
		result = prime
				* result
				+ ((zutatenMengenMap == null) ? 0 : zutatenMengenMap.hashCode());
		return result;
	}


	/**
	 * Vergleicht das Objekt mit einem Anderen auf Gleichheit
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
		Gericht other = (Gericht) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (prioritaet != other.prioritaet)
			return false;
		if (zutatenMengenMap == null) {
			if (other.zutatenMengenMap != null)
				return false;
		} else if (!zutatenMengenMap.equals(other.zutatenMengenMap))
			return false;
		return true;
	}
}
