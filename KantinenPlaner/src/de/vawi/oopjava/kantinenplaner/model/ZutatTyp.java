/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

/**
 * Repr�sentiert die Art einer Zutat.
 * Zutaten k�nnen entweder die Eigenschaft Fisch, Fleisch oder Veggi haben.
 * @author Julia Meyer
 * @version 2.0
 */
public enum ZutatTyp {
	VEGETARISCH("Veggi"),
	FLEISCH("Fleisch"),
	FISCH("Fisch");
	
	private String bezeichnung;

	/** Konstruktor zur Erzeugung dieses Enums, hier wird die Bezeichnung gesetzt.
	 * @param bezeichnung String Bezeichnung des Zutatentyps.
	 */
	private ZutatTyp(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/** 
	 * Getter f�r <code>this.bezeichnung</code>.
	 * @return String Gibt die Bezeichnung des Enums als String zur�ck.
	 */	
	public String getBezeichnung() {
		return bezeichnung;
	}
	
	public static ZutatTyp getZutatTypByChar(String s) {
	
	switch (s) {
		case "m":
			return ZutatTyp.FLEISCH;	
		case "f":
			return ZutatTyp.FISCH;
		
		case "":
			return ZutatTyp.VEGETARISCH;
		default:
			throw new IllegalArgumentException("Konnte ("+s+") nicht in einen ZutatTyp konvertieren.");
		}
	}
}