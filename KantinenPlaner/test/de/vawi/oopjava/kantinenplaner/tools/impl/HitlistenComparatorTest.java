/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.tools.impl.HitlistenComparator;

/**
 * @author Kim
 *
 */
public class HitlistenComparatorTest {
	private Comparator<Gericht> comparator;
	private Gericht g1;
	private Gericht g2;
	
	@Before
	public void setup() {
		comparator = new HitlistenComparator();
		g1 = new Gericht("test1");
		g2 = new Gericht("test2");
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.HitlistenComparator#compare(Gericht, Gericht)}.
	 */
	@Test
	public void compare1To10Test() {
		g1.setPrioritaet(1);
		g2.setPrioritaet(10);
		assertTrue(comparator.compare(g1, g2) < 0);
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.HitlistenComparator#compare(Gericht, Gericht)}.
	 */
	@Test
	public void compareSameTest() {
		g1.setPrioritaet(1);
		g2.setPrioritaet(g1.getPrioritaet());
		assertTrue(comparator.compare(g1, g2) == 0);
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.HitlistenComparator#compare(Gericht, Gericht)}.
	 */
	@Test
	public void compareNegativToPositivTest() {
		g1.setPrioritaet(-100);
		g2.setPrioritaet(g1.getPrioritaet() * -1);
		assertTrue(comparator.compare(g1, g2) < 0);
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.HitlistenComparator#compare(Gericht, Gericht)}.
	 */
	@Test
	public void compare1ToMaxTest() {
		g1.setPrioritaet(1);
		g2.setPrioritaet(Integer.MAX_VALUE);
		assertTrue(comparator.compare(g2, g1) > 0);
	}
}
