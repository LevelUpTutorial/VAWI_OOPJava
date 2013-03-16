/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Die Klasse ordnet einer Zutat eine Liste von H�ndlern zu, die diese Zutat verkaufen.
 * Die H�ndlerliste ist geordnet, sodass der H�ndler mit dem niedrigsten Index bevorzugt werden sollte.
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
	 * F�gt <code>zuo</code> in <code>this.zuoHaendlerAK</code> hinzu, wenn diese noch nicht enthalten ist.
	 * @param zuo ZuoHaendlerAnschKosten Die hinzuzuf�gende Zuordnung.
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
	 * @return List&lt;Haendler&gt; Gibt eine geordnete Liste der H�ndler zur�ck, die diese Zutat verkaufen. Die Liste ist so sortiert, dass ein H�ndler mit niedrigerem Index bevorzugt wird.
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
	 * Getter f�r <code>this.zutat</code>.
	 * @return Zutat Gibt <code>this.zutat</code> zur�ck.
	 */
	public Zutat getZutat() {
		return zutat;
	}

	/**
	 * Setter f�r <code>this.zutat</code>.
	 * @param zutat Zutat Setzt <code>this.zutat</code> auf <code>zutat</code>.
	 */
	public void setZutat(Zutat zutat) {
		this.zutat = zutat;
	}

	/**
	 * Getter f�r <code>this.haendlerPrio</code>.
	 * @return List&lt;ZuoHaendlerAnschKosten&gt; Gibt eine Kopie von <code>this.haendlerPrio</code> zur�ck.
	 */
	public List<ZuoHaendlerAnschKosten> getZuoHaendlerAK() {
		return new ArrayList<ZuoHaendlerAnschKosten>(zuoHaendlerAK);
	}

	/**
	 * Setter f�r <code>this.haendlerPrio</code>.
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
	 * @return BeschaffungsPriorisierung Gibt die erste gefundene Instanz einer BeschaffungsPriorisierung oder <code>null</code> zur�ck.
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
