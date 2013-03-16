/**
 * 
 */
package de.vawi.oopjava.kantinenplaner;

/**
 * Fachlicher ExceptionWrapper
 * @author Kim
 * @version 1.0
 */
public class KantinenPlanerException extends Exception {
	private static final long serialVersionUID = -4110844706674923560L;

	/**
	 * Konstruktor
	 */
	public KantinenPlanerException() {}

	/**
	 * Konstruktor
	 * @param arg0
	 */
	public KantinenPlanerException(String arg0) {
		super(arg0);
	}

	/**
	 * Konstruktor
	 * @param arg0
	 */
	public KantinenPlanerException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Konstruktor
	 * @param arg0
	 * @param arg1
	 */
	public KantinenPlanerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Konstruktor
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public KantinenPlanerException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
