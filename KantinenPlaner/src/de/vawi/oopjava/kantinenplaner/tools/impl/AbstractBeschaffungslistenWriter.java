/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import de.vawi.oopjava.kantinenplaner.model.Beschaffungsliste;
import de.vawi.oopjava.kantinenplaner.tools.Writer;

/**
 * Abstrakter Writer für eine Beschaffungsliste.
 * 
 * @author Matthias Rohe
 * @version 1.1
 */
public abstract class AbstractBeschaffungslistenWriter implements Writer {
	protected Beschaffungsliste beschaffungsliste;

	/**
	 * Getter für <code>this.beschaffungsliste</code>.
	 * @return Beschaffungsliste Gibt die Beschaffungsliste zurück.
	 */
	public Beschaffungsliste getBeschaffungsliste() {
		return beschaffungsliste;
	}

	/**
	 * Setter für <code>this.beschaffungsliste</code>.
	 * @param beschaffungsliste Beschaffungsliste Setzt <code>this.beschaffungsliste</code> auf <code>beschaffungsliste</code>.
	 */
	public void setBeschaffungsliste(Beschaffungsliste beschaffungsliste) {
		this.beschaffungsliste = beschaffungsliste;
	}
	
}