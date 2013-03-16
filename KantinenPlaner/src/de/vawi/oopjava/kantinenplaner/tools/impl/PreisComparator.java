/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vawi.oopjava.kantinenplaner.model.BeschaffungsPriorisierung;
import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Zutat;

/**
 * Comparator, um Gerichte anhand ihrer Anschaffungskosten zu sortieren.
 * <br>Innerhalb der <code>this.toleranz</code> werden Gerichte mit unterschiedlichen Anschaffungskosten als gleichwertig angesehen (default = 0).
 * Bei gleichwertigen Gerichten, werden diese anhand ihre Priorität sortiert.
 * @author Kim
 * @version 1.1
 */
public class PreisComparator implements Comparator<Gericht> {
	private static final Logger logger = LogManager.getLogger(PreisComparator.class);
	private List<BeschaffungsPriorisierung> beschPrioList = new ArrayList<BeschaffungsPriorisierung>();
	/** Toleranz beim Vergleich von Gerichten, z.B. aufgrund einer höhren Beliebtheit und eines höheren Preises */
	private float toleranz;

	/**
	 * Konstruktor
	 * @param beschPrioList List&lt;BeschaffungsPriorisierung&gt; Die BeschaffungsPriorisierung
	 * @param toleranz float Die Toleranz
	 */
	public PreisComparator(List<BeschaffungsPriorisierung> beschPrioList, float toleranz) {
		this.beschPrioList = beschPrioList;
		this.toleranz = toleranz;
	}	

	/**
	 * Vergleicht zwei Gerichte aufgrund ihrer durchschnittlichen Anschaffungskosten aus der BeschaffungsPriorisierung.
	 * Neben den Anschaffungskosten wird eine Toleranz berücksichtigt. 
	 * Gerichte, die beim Vergleich von {@link Gericht#getPrioritaet()} eine höhere Priorität haben, werden bevorzugt, 
	 * wenn die Preisdifferenz innerhalb der Toleranz liegt.
	 * @see {@link BeschaffungsPriorisierung}
	 */
	@Override
	public int compare(Gericht g1, Gericht g2) {
		float akG1 = ermittleAKGericht(g1);
		float akG2 = ermittleAKGericht(g2);
		int prioG1 = g1.getPrioritaet();
		int prioG2 = g2.getPrioritaet();
		int akVergl = Float.compare(akG1, akG2);
		logger.trace("Vergleiche g1(AK="+akG1+"; Prio="+prioG1+") und g2(AK="+akG2+"; Prio="+prioG2+")");
		
		if (akVergl < 0 && (akG2 - akG1 - toleranz) > 0) {
			akVergl = -1;
		}
		else if (akVergl == 0) {
			akVergl = Integer.compare(prioG1, prioG2);
		}
		else if (akVergl > 0 && (akG1 - akG2 - toleranz) > 0) {
			akVergl = +1;
		}
		else {
			akVergl = +1;
		}
		
		logger.trace("Vergleichsergebnis="+akVergl);
		return akVergl;
	}
	
	private float ermittleAKGericht(Gericht gericht) {
		float ak = 0;
		HashMap<Zutat, Float> zutatenMengenMap = gericht.getZutatenMengenMap();
		for (Zutat z : zutatenMengenMap.keySet()) {
			for (BeschaffungsPriorisierung prio : beschPrioList) {
				if (z.equals(prio.getZutat())) {
					ak += prio.getZuoHaendlerAK().get(0).getBruttoPreis() * zutatenMengenMap.get(z);
				}
			}
		}
		return ak;
	}

	/**
	 * Getter für <code>this.toleranz</code>.
	 * @return float Gibt die Toleranz zurück.
	 */
	public float getToleranz() {
		return toleranz;
	}

	/**
	 * Setter für <code>this.toleranz</code>.
	 * @param toleranz float Setzt <code>this.toleranz</code> auf <code>toleranz</code>.
	 */
	public void setToleranz(float toleranz) {
		this.toleranz = toleranz;
	}

	/**
	 * Getter für <code>this.beschPrioList</code>.
	 * @return List&lt;BeschaffungsPriorisierung&gt; Gibt eine Kopie von <code>this.beschPrioList</code> zurück.
	 */
	public List<BeschaffungsPriorisierung> getBeschPrioList() {
		return new ArrayList<BeschaffungsPriorisierung>(beschPrioList);
	}

	/**
	 * Setter für <code>this.beschPrioList</code>.
	 * @param beschPrio List&lt;BeschaffungsPriorisierung&gt; Setzt <code>this.beschPrioList</code> auf <code>beschPrio</code>.
	 */
	public void setBeschPrio(List<BeschaffungsPriorisierung> beschPrio) {
		if (beschPrio == null) {
			throw new IllegalArgumentException("Parameter beschPrio darf nicht NULL sein!");
		}
		this.beschPrioList = beschPrio;
	}
	
}
