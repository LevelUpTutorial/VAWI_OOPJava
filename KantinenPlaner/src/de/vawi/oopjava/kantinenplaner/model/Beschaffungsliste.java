/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

import java.util.ArrayList;
import java.util.List;

import de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil;

/**
 * Containerklasse für die Händler und Zutatenlisten, Cache. 
 * Hier werden die Daten für die Einkaufsliste einschließlich der Kosten verwaltet.
 * 
 * @author Julia Meyer
 * @version 3.1
 */
public class Beschaffungsliste {
	private List<HaendlerPos> haendlerPositionenListe = new ArrayList<HaendlerPos>();

	/**
	 * Rueckgabe der Liste <code>haendlerPositionenListe</code>
	 * 
	 * @return List HaendlerPos gibt eine Kopie von <code>this.haendlerPositionenListe</code> zurück.
	 */
	public List<HaendlerPos> getHaendlerPositionenListe() {
		List<HaendlerPos> copy = new ArrayList<HaendlerPos>(
				haendlerPositionenListe);
		return copy;
	}

	/**
	 * Ersetzt <code>this.haendlerPositionenListe</code> durch
	 * <code>haendlerPositionenListe</code>.
	 * 
	 * @param List HaendlerPos setzt
	 *            <code>this.haendlerPositionenListe</code> auf
	 *            <code>haendlerPositionenListe</code>
	 */
	public void setHaendlerPositionenListe(
			List<HaendlerPos> haendlerPositionenListe) {
		if (haendlerPositionenListe == null) {
			throw new IllegalArgumentException(
					"Parameter haendlerPositionenListe darf nicht NULL sein.");
		}
		this.haendlerPositionenListe = haendlerPositionenListe;
	}


	/**
	 * Fügt <code>zutat</code> und <code>menge</code> als {@link ZutatPos} der
	 * entsprechenden {@link HaendlerPos} hinzu. 
	 * Um zu gewaehrleisten, dass pro Haendler nur eine HaendlerPos existiert, 
	 * wird überprüft, ob bereits eine HaendlerPos mit dem Haendler besteht.
	 * Wenn ja wird die bestehende um eine ZutatPos erweitert, ansonsten wird eine neue HaendlerPos erzeugt.
	 * 
	 * @param Haendler
	 *            haendler Händler-Objekt, bei dem die Zutat gekauft werden soll.
	 * @param zutat
	 *            Zutat Zutat-Objekt, die gekauft werden soll.
	 * @param menge
	 *            int Menge die von <code>zutat</code> gekauft werden soll.
	 */
	public void addZutatPos(Haendler haendler, Zutat zutat, int menge) {
		boolean gefunden = false;
		for (HaendlerPos haendlerpos : haendlerPositionenListe) {
			if (haendlerpos.getHaendler().equals(haendler)) {
				haendlerpos.addZutatPosition(new ZutatPos(menge, zutat));
				gefunden = true;
			}
		}
		if (!gefunden) {
			CollectionUtil.addItemIfNotContained(haendlerPositionenListe, new HaendlerPos(haendler, zutat, menge));
		}
	}

	/**
	 * Ausgabe der Gesamtkosten in Euro.
	 * Sie beinhalten die Produktkosten sowie die Transportkosten.
	 * 
	 * @return float Gesamtkosten gibt die Gesamtkosten der Beschaffungsliste in
	 *         Euro zurück. 
	 */
	public float getGesamtkosten() {
		float gesamtkosten = 0;

		for (HaendlerPos haendler : haendlerPositionenListe) {
			gesamtkosten += haendler.berechneAnschaffungskosten();
		}

		return gesamtkosten;
	}

	/**
	 * Erzeugt einen Hash-Code aus dem Objekt mit den Attributen Einheit, Name und Typ
	 * @return int Hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((haendlerPositionenListe == null) ? 0
						: haendlerPositionenListe.hashCode());
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
		Beschaffungsliste other = (Beschaffungsliste) obj;
		if (haendlerPositionenListe == null) {
			if (other.haendlerPositionenListe != null)
				return false;
		} else if (!haendlerPositionenListe
				.equals(other.haendlerPositionenListe))
			return false;
		return true;
	}


}
