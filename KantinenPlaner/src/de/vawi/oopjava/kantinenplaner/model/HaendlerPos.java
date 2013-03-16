/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

import java.util.ArrayList;
import java.util.List;

import de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil;

/**
 * Zuordnungsklasse der Haendler - Positionen. Hier werden die Haendler mit den
 * zu beschaffenden Zutaten verbunden.
 * 
 * @author Julia Meyer
 * @version 3.1
 */
public class HaendlerPos {
	private List<ZutatPos> zutatPostionenListe = new ArrayList<ZutatPos>();
	private Haendler haendler;

	/**
	 * Konstruktor
	 * 
	 * @param haendler
	 *            Haendler gewuenschter Haendler
	 * @param zutat
	 *            Zutat Benötigte Zutat
	 * @param menge
	 *            Int Mengenangabe der benoetigten Zutaten
	 */
	public HaendlerPos(Haendler haendler, Zutat zutat, int menge) {
		this.haendler = haendler;
		zutatPostionenListe.add(new ZutatPos(menge, zutat));
	}

	/**
	 * Getter für <code>this.zutatPositionenListe</code>
	 * 
	 * @return List ZutatPos Gibt eine Kopie von
	 *         <code>this.zutatPostionenListe</code> zurück.
	 */
	public List<ZutatPos> getZutatPostionenListe() {
		List<ZutatPos> copy = new ArrayList<ZutatPos>(zutatPostionenListe);
		return copy;
	}

	/**
	 * Ersetzt <code>this.zutatPostionenListe</code> durch
	 * <code>zutatPostionenListe</code>.
	 * 
	 * @param zutatPostionenListe
	 *            List ZutatPos Setzt <code>this.zutatPostionenListe</code> auf
	 *            <code>zutatPostionenListe</code>, wenn
	 *            <code>zutatPostionenListe != null</code>.
	 */
	public void setZutatPostionenListe(List<ZutatPos> zutatPostionenListe) {
		if (zutatPostionenListe == null) {
			throw new IllegalArgumentException(
					"Parameter zutatPostionenListe darf nicht NULL sein.");
		}
		this.zutatPostionenListe = zutatPostionenListe;
	}

	/**
	 * Fügt die <code>hinzuZutatPosition</code> der Liste 
	 * <code>zutatPostionenListe</code> hinzu.
	 * Wenn die Zutat bereits vorhanden ist, wird nur die Menge erhoeht.
	 * 
	 * @param hinzuZutatPosition
	 *            ZutatPos Die hinzuzufügende Zutatposition.
	 */
	public void addZutatPosition(ZutatPos hinzuZutatPosition) {
		boolean gefunden = false;
		for (ZutatPos zutatpos : zutatPostionenListe) {
			if (zutatpos.getZutat().equals(hinzuZutatPosition.getZutat())) {
				float neueSumme= zutatpos.getMenge()+hinzuZutatPosition.getMenge();
				zutatpos.setMenge(neueSumme);
				gefunden = true;
			}
		}
		if (!gefunden) {
			CollectionUtil.addItemIfNotContained(zutatPostionenListe,
					hinzuZutatPosition);
		}
	}

	public void removeZutatPosition(ZutatPos loeschZutatPosition) {
		CollectionUtil.removeItem(zutatPostionenListe, loeschZutatPosition);
	}

	/**
	 * Getter für <code>this.haendler</code>
	 * 
	 * @return Haendler Gibt den verbundenen Händler zurück.
	 */
	public Haendler getHaendler() {
		return haendler;
	}

	/**
	 * Setter für <code>this.haendler</code>
	 * 
	 * @param haendler
	 *            Haendler Setzt <code>this.haendler</code> auf
	 *            <code>haendler</code>.
	 */
	public void setHaendler(Haendler haendler) {
		this.haendler = haendler;
	}

	/**
	 * Berechnet die Transportkosten. Es wird die Anzahl der unterschiedlichen
	 * Artikel bestimmt und auf Basis dieser Transportkosten beim Haendler
	 * erfragt.
	 * 
	 * @return float Gibt die Transportkosten zurück.
	 */
	public float getTransportkosten() {
		int artikelmenge = 0;
		List<ZutatPos> zutatenPosList = this.getZutatPostionenListe();

		artikelmenge = zutatenPosList.size();

		return haendler.berechneTransportkosten(artikelmenge);
	}

	/**
	 * Berechnet die Gesamtanschaffungskosten aller Zutaten, aus
	 * <code>this.zutatPostionenListe</code>. <br>
	 * Die Anschaffungskosten berechnen sich wie folgt:
	 * Anschaffungskosten = Einkaufspreise der Zutaten + Transportkosten
	 * Für jedes Produkt wird die benoetigte Menge in die 
	 * handelsuebliche Verkaufsmenge umgerechnet und der Einkaufpreis berechnet.
	 * Alle Einkaufspreise der zutatenPosList werden aufsummiert 
	 * und anschließend um die Transportkosten erhöht. 
	 * 
	 * @see {@link HaendlerPos#getEinkaufspreise()}
	 * @see {@link HaendlerPos#getTransportkosten()}
	 * @return float Gibt die Anschaffungskosten der Zutat zurück.
	 */
	public float berechneAnschaffungskosten() {
		List<ZutatPos> zutatenPosList = this.getZutatPostionenListe();
		// Gesamtpreis
		float summe = 0;
		// Benötigte Gebinde
		int verkaufsmenge = 0;
		
		// Preis der benötigten Gebinde
		float position = 0;
		

		for (ZutatPos zutat : zutatenPosList) {
			int gebindeGroesse = this.getHaendler().getMengeProGebinde(zutat.getZutat().getName());
			float benoetigteMenge = zutat.getMenge();
			
			verkaufsmenge = (int) Math.ceil(benoetigteMenge / gebindeGroesse);

			position = this.getHaendler().getPreisProGebinde(zutat.getZutat().getName()) * verkaufsmenge;
			summe += position;
			position=0;

		}
		summe += this.getTransportkosten();

		return summe;
	}

	/**
	 * Erzeugt einen Hash-Code aus dem Objekt mit den Attributen Einheit, Name und Typ
	 * @return int Hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((haendler == null) ? 0 : haendler.hashCode());
		result = prime
				* result
				+ ((zutatPostionenListe == null) ? 0 : zutatPostionenListe
						.hashCode());
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
		HaendlerPos other = (HaendlerPos) obj;
		if (haendler == null) {
			if (other.haendler != null)
				return false;
		} else if (!haendler.equals(other.haendler))
			return false;
		if (zutatPostionenListe == null) {
			if (other.zutatPostionenListe != null)
				return false;
		} else if (!zutatPostionenListe.equals(other.zutatPostionenListe))
			return false;
		return true;
	}
}
