/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Die Klasse ordnet einer Zutat eine Liste von Händlern zu, die diese Zutat verkaufen.
 * Die Händlerliste ist geordnet, sodass der Händler mit dem niedrigsten Index bevorzugt werden sollte.
 * @author Kim Jansen
 * @version 1.2
 */
public class BeschaffungsPriorisierung {
	private Zutat zutat;
	private List<ZuoHaendlerAnschKosten> zuoHaendlerAK = new ArrayList<ZuoHaendlerAnschKosten>();
	
	/**
	 * Konstruktor
	 * @param zutat Zutat Die Zutat
	 */
	public BeschaffungsPriorisierung(Zutat zutat) {
		this.zutat = zutat;
	}
	
	/**
	 * Fügt <code>zuo</code> in <code>this.zuoHaendlerAK</code> hinzu, wenn diese noch nicht enthalten ist.
	 * @param zuo ZuoHaendlerAnschKosten Die hinzuzufügende Zuordnung.
	 */
	public void addZuoHaendlerAnschKosten(ZuoHaendlerAnschKosten zuo) {
		if (zuo == null) {
			throw new IllegalArgumentException("Parameter zuo darf nicht NULL sein.");
		}
		if (!zuoHaendlerAK.contains(zuo)) {
			zuoHaendlerAK.add(zuo);
		}
	}
	
	/**
	 * @see {@link ZuoHaendlerAnschKosten#compareTo(ZuoHaendlerAnschKosten)}
	 * @return List&lt;Haendler&gt; Gibt eine geordnete Liste der Händler zurück, die diese Zutat verkaufen. Die Liste ist so sortiert, dass ein Händler mit niedrigerem Index bevorzugt wird.
	 */
	public List<Haendler> getHaendlerPrio() {
		Collections.sort(zuoHaendlerAK);
		List<Haendler> hPrioList = new ArrayList<Haendler>(zuoHaendlerAK.size());
		for (ZuoHaendlerAnschKosten zuo : zuoHaendlerAK) {
			hPrioList.add(zuo.getHaendler());
		}
		return hPrioList;
	}

	/**
	 * Getter für <code>this.zutat</code>.
	 * @return Zutat Gibt <code>this.zutat</code> zurück.
	 */
	public Zutat getZutat() {
		return zutat;
	}

	/**
	 * Setter für <code>this.zutat</code>.
	 * @param zutat Zutat Setzt <code>this.zutat</code> auf <code>zutat</code>.
	 */
	public void setZutat(Zutat zutat) {
		this.zutat = zutat;
	}

	/**
	 * Getter für <code>this.haendlerPrio</code>.
	 * @return List&lt;ZuoHaendlerAnschKosten&gt; Gibt eine Kopie von <code>this.haendlerPrio</code> zurück.
	 */
	public List<ZuoHaendlerAnschKosten> getZuoHaendlerAK() {
		return new ArrayList<ZuoHaendlerAnschKosten>(zuoHaendlerAK);
	}

	/**
	 * Setter für <code>this.haendlerPrio</code>.
	 * @param haendlerPrio List&lt;ZuoHaendlerAnschKosten&gt; Setzt <code>this.haendlerPrio</code> auf <code>haendlerPrio</code>.
	 */
	public void setZuoHaendlerAK(List<ZuoHaendlerAnschKosten> haendlerPrio) {
		if (haendlerPrio == null) {
			throw new IllegalArgumentException("Parameter haendlerPrio darf nicht NULL sein!");
		}
		this.zuoHaendlerAK = haendlerPrio;
	}
	
	/**
	 * Findet einen BeschaffungsPriorisierung zu einer bestimmten Zutat innerhalb einer Liste von BeschaffungsPriorisierungen
	 * @param liste List&lt;BeschaffungsPriorisierung&gt; Die Liste
	 * @param zutat Zutat Die Zutat
	 * @return BeschaffungsPriorisierung Gibt die erste gefundene Instanz einer BeschaffungsPriorisierung oder <code>null</code> zurück.
	 */
	public static BeschaffungsPriorisierung getBeschaffungsPriorisierung(List<BeschaffungsPriorisierung> liste, Zutat zutat) {
		for (BeschaffungsPriorisierung prio : liste) {
			if (prio.getZutat().equals(zutat)) {
				return prio;
			}
		}
		return null;
	}
}
