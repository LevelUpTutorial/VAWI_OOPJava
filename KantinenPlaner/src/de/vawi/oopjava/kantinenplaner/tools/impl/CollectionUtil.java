package de.vawi.oopjava.kantinenplaner.tools.impl;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generische Klasse mit Add/Remove/Copy Methoden bezüglich {@link java.util.Collection}. Die
 * Klasse enthält eigene generische Methoden, zum Hinzufügen und Entfernen von Elementen.
 * 
 * @author Matthias
 * @version 2.2
 */
public final class CollectionUtil {
	private static final Logger logger = LogManager.getLogger(CollectionUtil.class);

	/**
	 * Generische Methode zum Hinzufügen von einem Element, sofern Element noch nicht
	 * enthalten ist. Für <code>collection == null</code>, wird eine Exception
	 * geworfen. In jedem Fall wird (das Ergebnis der Prüfung) in die Log-Datei
	 * geschrieben.
	 * 
	 * @param collection Collection&lt;T&gt; Die Collection
	 * @param item T Das Element
	 */
	public static <T> void addItemIfNotContained(Collection<T> collection, T item) {
		if (collection == null) {
			String msg = "Parameter collection darf nicht NULL sein";
			logger.warn(msg);
			throw new IllegalArgumentException(msg);
		}
		if (!collection.contains(item)) {
			logger.trace("Item : (" + item + ") wurde zu Collection (" + collection + ") hinzugefuegt.");
			collection.add(item);
		} else {
			logger.trace("Item : (" + item + ") ist bereits Collection (" + collection + ") enthalten und wurde nicht erneut hinzugefuegt.");
		}
	}

	/**
	 * Generische Methode zum Hinzufügen von Elementen, sofern Elemente noch nicht
	 * enthalten ist. Für <code>collection == null</code>, wird eine Exception
	 * geworfen. In jedem Fall wird (das Ergebnis der Prüfung) in die Log-Datei
	 * geschrieben.
	 * 
	 * @param collection Collection&lt;T&gt; Die Collection
	 * @param items Collection&lt;T&gt; Die Elemente
	 */
	public static <T> void addAllItemsIfNotContained(Collection<T> collection, Collection<T> items) {
		for (T t : items) {
			addItemIfNotContained(collection, t);
		}
	}

	/**
	 * Generische Methode zum Löschen von Elementen. Für <code>collection == null</code>, wird eine Exception
	 * geworfen. In jedem Fall wird (das Ergebnis der Prüfung) in die Log-Datei geschrieben.
	 * 
	 * @param collection Collection&lt;T&gt; Die Collection
	 * @param item T Das Element
	 */
	public static <T> void removeItem(Collection<T> collection, T item) {
		if (collection == null) {
			String msg = "Parameter collection darf nicht NULL sein";
			logger.warn(msg);
			throw new IllegalArgumentException(msg);
		}
		if (!collection.contains(item)) {
			logger.trace("Item : (" + item + ") wurde aus Collection (" + collection + ") entfernt.");
			collection.add(item);
		} else {
			logger.trace("Item : (" + item + ") ist in Collection (" + collection + ") nicht enthalten gewesen.");
		}
	}

	/**
	 * Generische Methode zum Löschen aller Elemente. Für <code>collection == null</code>, wird eine Exception
	 * geworfen. In jedem Fall wird (das Ergebnis der Prüfung) in die Log-Datei geschrieben.
	 * 
	 * @param collection Collection&lt;T&gt; Die Collection
	 * @param items Collection&lt;T&gt; Die Elemente
	 */
	public static <T> void removeAllItems(Collection<T> collection, Collection<T> items) {
		if (collection == null) {
			String msg = "Parameter collection darf nicht NULL sein";
			logger.warn(msg);
			throw new IllegalArgumentException(msg);
		}
		for (T t : items) {
			removeItem(collection, t);
		}
	}
	
}
