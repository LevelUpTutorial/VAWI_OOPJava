/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import java.util.Comparator;

import de.vawi.oopjava.kantinenplaner.model.Gericht;

/**
 * Comparator, um Gerichte anhand ihrer Priorität zu sortieren.
 * @author Kim
 * @version 1.0
 */
public class HitlistenComparator implements Comparator<Gericht> {
	/**
	 * Vergleicht zwei Gerichte (o1 und o2) anhand ihrer Prioritaet. Der Methodenaufruf liefert 
	 * dasselbe Ergebnis wie <code>Integer.compare(o1.getPrioritaet(), o2.getPrioritaet())</code>.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Gericht o1, Gericht o2) {
		return Integer.compare(o1.getPrioritaet(), o2.getPrioritaet());
	}
}
