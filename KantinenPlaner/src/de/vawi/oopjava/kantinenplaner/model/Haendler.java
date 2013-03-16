package de.vawi.oopjava.kantinenplaner.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.vawi.oopjava.kantinenplaner.tools.TransportKostenRechner;
import de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil;

/**
 * Repräsentation eines Händlers.
 * 
 * @author Julia Meyer
 * @version 3.1
 * @see http://de.wikipedia.org/wiki/Fabrikmethode
 */
public class Haendler {
	private static final List<Haendler> haendlerInstanzen = new ArrayList<Haendler>();
	private List<ZuoHaendlerZutat> zuoHaendlerZutatListe = new ArrayList<ZuoHaendlerZutat>();
	private String name;
	private float transportKostenBasis;
	private TransportKostenRechner transportKostenRechner;
	private static final Logger logger = LogManager.getLogger(Haendler.class);


	/**
	 * privater Konstruktor
	 * 
	 * @param name String 
	 * 			  Haendlername oder Bezeichnung
	 * @param transportKostenBasis float 
	 *            Die Basis für die TransportKosten-Berechnung
	 *            (€-Angabe; entweder Preis pro Produkt oder KM)
	 * @param transportKostenRechner TransportKostenRechner 
	 *            Die Strategie zur Berechnung der
	 *            Transportkosten.
	 */
	private Haendler(String name, float transportKostenBasis,
			TransportKostenRechner transportKostenRechner) {
		if (transportKostenRechner == null) {
			logger.error("Parameter transportKostenRechner darf nicht NULL sein.");
			throw new IllegalArgumentException(
					"Parameter transportKostenRechner darf nicht NULL sein.");
		}
		if (name == null) {
			logger.error("Parameter name darf nicht NULL sein.");
			throw new IllegalArgumentException(
					"Parameter name darf nicht NULL sein.");
		}
		
		this.name = name;
		this.transportKostenBasis = transportKostenBasis;
		this.transportKostenRechner = transportKostenRechner;
	}

	/**
	 * Findet eine Haendlerinstanz anhand von <code>Haendler.name</code>
	 * @param name String Name des Handlers
	 * @return Gibt die erste gefundene Instanz oder <code>null</code> zurück.
	 */
	public static Haendler getHaendler(String name) {
		for (Haendler h : haendlerInstanzen) {
			if (h.name.equals(name)) {
				logger.debug("Es wurde ein Haendler mit dem Namen "+name+ " gefunden.");
				return h;
			}
		}
		logger.debug("Es wurde kein Haendler mit dem Namen "+name+ " gefunden.");
		return null;
	}

	/**
	 * Ausgabe aller vorhandenen Haendler in einer Liste.
	 * 
	 * @return List Haendler Gibt eine Kopie aller Haendlerinstanzen aus
	 *         <code>this.haendlerInstanzen</code> zurück.
	 */
	public static List<Haendler> getAllHaendler() {
		List<Haendler> copy = new ArrayList<Haendler>(haendlerInstanzen);
		return copy;
	}

	/**
	 * Factorymethode um Haendlerinstanzen zu erzeugen.
	 * 
	 * @param name String 
	 * 			  Haendlername oder Bezeichnung
	 * @param transportKostenBasis float 
	 *            Die Basis für die TransportKosten-Berechnung
	 *            (€-Angabe; entweder Preis pro Produkt oder KM)
	 * @param transportKostenRechner TransportKostenRechner 
	 *            Die Strategie zur Berechnung der
	 *            Transportkosten.
	 * @return Haendler Rückgabe des neu erzeugten Haendlers
	 */
	public static Haendler newHaendler(String name, float transportKostenBasis,
			TransportKostenRechner transportKostenRechner) {
		if (transportKostenRechner == null) {
			throw new IllegalArgumentException(
					"Parameter transportKostenRechner darf nicht NULL sein.");
		}
		Haendler haendler = new Haendler(name, transportKostenBasis,
				transportKostenRechner);
		CollectionUtil.addItemIfNotContained(haendlerInstanzen, haendler);
		logger.debug("Es wurde eine neuer HAendler erzeugt und der Liste haendlerInstanzen hinzugefuegt. ");
		
		return haendler;
	}

	/**
	 * Fügt <code>hizuListeHaendler</code> in
	 * <code>this.haendlerInstanzen</code> hinzu, wenn diese noch nicht
	 * enthalten sind.
	 * 
	 * @param hizuListeHaendler
	 *            Collection Haendler Liste aller hinzuzufuegenden Haendler
	 */
	
	public void addAllHaendler(Collection<Haendler> hizuListeHaendler) {
		CollectionUtil.addAllItemsIfNotContained(haendlerInstanzen,
				hizuListeHaendler);
	}

	/**
	 * Entfernt <code>loeschHaendler</code> aus
	 * <code>this.haendlerInstanzen</code>.
	 * 
	 * @param loeschHaendler
	 *            Haendler zu loeschender Haendler
	 */
	public void removeHaendler(Haendler loeschHaendler) {
		CollectionUtil.removeItem(haendlerInstanzen, loeschHaendler);
	}

	/**
	 * Entfernt alle Händlerpositionen in <code>loeschHaendlerListe</code> aus
	 * <code>this.haendlerInstanzen</code>.
	 * 
	 * @param loeschHaendlerListe
	 *            Collection Haendler Liste, der zu loeschenden Haendlder
	 */
	public void removeAllHaendler(Collection<Haendler> loeschHaendlerListe) {
		CollectionUtil.removeAllItems(haendlerInstanzen, loeschHaendlerListe);
	}

	/**
	 * Getter für <code>this.name</code>.
	 * 
	 * @return String name Haendlername oder Haendlerbezeichnung.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter für <code>this.name</code>.
	 * 
	 * @param name
	 *            String Setzt <code>this.name</code> auf <code>name</code>.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter für <code>this.transportKostenRechner</code>.
	 * 
	 * @return TransportKostenRechner Gibt den TransportKostenRechner zurück.
	 */
	public TransportKostenRechner getTransportKostenRechner() {
		return transportKostenRechner;
	}

	/**
	 * Setter für <code>this.transportKostenRechner</code>.
	 * 
	 * @param transportKostenRechner
	 *            TransportKostenRechner Setzt
	 *            <code>this.transportKostenRechner</code> auf
	 *            <code>transportKostenRechner</code>, wenn
	 *            <code>transportKostenRechner != null</code>.
	 */
	public void setTransportKostenRechner(
			TransportKostenRechner transportKostenRechner) {
		if (transportKostenRechner == null) {
			throw new IllegalArgumentException(
					"Parameter transportKostenRechner darf nicht NULL sein.");
		}
		this.transportKostenRechner = transportKostenRechner;
	}

	/**
	 * Getter für <code>this.transportKostenBasis</code>
	 * 
	 * @return float Gibt <code>transportKostenBasis</code> zurück.
	 */
	public float getTransportKostenBasis() {
		return transportKostenBasis;
	}

	/**
	 * Setter für <code>this.transportKostenBasis</code>
	 * 
	 * @param transportKostenBasis
	 *            float Setzt <code>this.transportKostenBasis</code> auf
	 *            <code>transportKostenBasis</code>.
	 */
	public void setTransportKostenBasis(float transportKostenBasis) {
		this.transportKostenBasis = transportKostenBasis;
	}

	/**
	 * @return List ZuoHaendlerZutat Gibt eine Kopie von
	 *         <code>this.zuoHaendlerZutatListe</code> zurück.
	 */
	public List<ZuoHaendlerZutat> getZuoHaendlerZutatListe() {
		List<ZuoHaendlerZutat> copy = new ArrayList<ZuoHaendlerZutat>(
				zuoHaendlerZutatListe);
		return copy;
	}

	/**
	 * Ersetzt <code>Haenlder.zuoHaendlerZutatListe</code> durch
	 * <code>zuoHaendlerZutatListe</code>.
	 * 
	 * @param zuoHaendlerZutatListe
	 *            List ZuoHaendlerZutat Die Liste mit Zuordnungen von Händlern
	 *            und Zutaten.
	 */
	public void setZuoHaendlerZutatListe(
			List<ZuoHaendlerZutat> zuoHaendlerZutatListe) {
		if (zuoHaendlerZutatListe == null) {
			throw new IllegalArgumentException(
					"Parameter zuoHaendlerZutatListe darf nicht NULL sein.");
		}
		this.zuoHaendlerZutatListe = zuoHaendlerZutatListe;
	}

	/**
	 * Findet die Zuordnung zu der <code>zutat</code>.
	 * 
	 * @param zutat
	 *            Zutat Die zu suchende Zutat.
	 * @return ZuoHaendlerZutat Gibt die gefundene Zuordnung oder
	 *         <code>null</code> zurück.
	 */
	public ZuoHaendlerZutat getZuoHaendlerZutat(Zutat zutat) {
		for (ZuoHaendlerZutat zuo : zuoHaendlerZutatListe) {
			if (zuo.getZutat().equals(zutat)) {
				return zuo;
			}
		}
		return null;
	}

	/**
	 * Fügt <code>hinzuHaendlerZutat</code> in
	 * <code>this.zuoHaendlerZutatListe</code> hinzu, wenn diese noch nicht
	 * enthalten ist.
	 * 
	 * @param hinzuHaendlerZutat
	 *            ZuoHaendlerZutat die hinzuzufügende Haendlerposition.
	 */
	public void addZuoHaendlerZutat(ZuoHaendlerZutat hinzuHaendlerZutat) {
		CollectionUtil.addItemIfNotContained(zuoHaendlerZutatListe,
				hinzuHaendlerZutat);
	}

	/**
	 * Entfernt <code>loeschHaendlerZutat</code> in
	 * <code>this.zuoHaendlerZutatListe</code> hinzu, wenn diese 
	 * enthalten ist.
	 * 
	 * @param haendlerPosition
	 *            HaendlerPos die hinzuzufügende Haendlerposition.
	 */
	public void removeZuoHaendlerZutat(ZuoHaendlerZutat loeschHaendlerZutat) {
		CollectionUtil.removeItem(zuoHaendlerZutatListe, loeschHaendlerZutat);
	}

	/**
	 * Ermittelt den Preis einer Zutat pro verkaufsuebliche Menge.
	 * 
	 * @param zutat
	 *            String Bezeichnung der Zutat
	 * @return float Preis pro Gebinde oder 0, wenn die Zutat nicht im Sortiment
	 *         ist.
	 */
	public float getPreisProGebinde(String zutat) {
		for (ZuoHaendlerZutat zuo : zuoHaendlerZutatListe) {
			if (zuo.getZutat().getName().equals(zutat)) {
				return zuo.getPreisProGebinde();
			}
		}
		return 0;
	}

	/**
	 * Ermittelt den Preis einer Zutat pro Einheit.
	 * 
	 * @param zutat
	 *            String Bezeichnung der Zutat
	 * @return float Preis pro Einheit oder 0, wenn die Zutat nicht im Sortiment
	 *         ist.
	 */
	public float getPreisProEinheit(String zutat) {
		for (ZuoHaendlerZutat zuo : zuoHaendlerZutatListe) {
			if (zuo.getZutat().getName().equals(zutat)) {
				return zuo.getPreisProGebinde() / zuo.getMengeProGebinde();
			}
		}
		return 0;
	}

	/**
	 * Ermittelt die Genindegroeße.
	 * 
	 * @param zutat
	 *            String Bezeichnung der Zutat
	 * @return int gibt an wie groß ein Gebinde ist oder 0, wenn die Zutat nicht
	 *         im Sortiment ist.
	 */
	public int getMengeProGebinde(String zutat) {
		for (ZuoHaendlerZutat zuo : zuoHaendlerZutatListe) {
			if (zuo.getZutat().getName().equals(zutat)) {
				return zuo.getMengeProGebinde();
			}
		}
		return 0;
	}

	/**
	 * Ermittelt die Transportkosten unter zuhilfenahme des
	 * TransportKostenRechners
	 * 
	 * @param zutatenMengenMap
	 *            Map String, Integer Map über <code>Zutat.name</code> und die
	 *            Menge als Integer.
	 * @return float Gibt die Transportkosten zurück.
	 */
	public float berechneTransportkosten(int artikelMenge) {
		float transportkosten;
		transportkosten = this.transportKostenRechner.getTransportkosten(
				artikelMenge, transportKostenBasis);

		return transportkosten;
	}

	/**
	 * Erzeugt einen Hash-Code aus dem Objekt mit den Attributen Einheit, Name und Typ
	 * @return int Hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(transportKostenBasis);
		result = prime
				* result
				+ ((transportKostenRechner == null) ? 0
						: transportKostenRechner.hashCode());
		result = prime
				* result
				+ ((zuoHaendlerZutatListe == null) ? 0 : zuoHaendlerZutatListe
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
		Haendler other = (Haendler) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Float.floatToIntBits(transportKostenBasis) != Float
				.floatToIntBits(other.transportKostenBasis))
			return false;
		if (transportKostenRechner == null) {
			if (other.transportKostenRechner != null)
				return false;
		} else if (!transportKostenRechner.equals(other.transportKostenRechner))
			return false;
		if (zuoHaendlerZutatListe == null) {
			if (other.zuoHaendlerZutatListe != null)
				return false;
		} else if (!zuoHaendlerZutatListe.equals(other.zuoHaendlerZutatListe))
			return false;
		return true;
	}

}
