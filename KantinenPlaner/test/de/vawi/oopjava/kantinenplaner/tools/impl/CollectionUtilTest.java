/**
 * 
 */
package de.vawi.oopjava.kantinenplaner.tools.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Kim
 */
@Ignore("Nicht alle Testkriterien erfüllt")
public class CollectionUtilTest {

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#addItemIfNotContained(java.util.Collection, java.lang.Object)}.
	 */
	@Test
	public void addItem() {
		String testObject = "testString";
		List<String> testCollection = new ArrayList<String>();
		
		CollectionUtil.addItemIfNotContained(testCollection, testObject);
		assertTrue(testCollection.contains(testObject));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#addItemIfNotContained(java.util.Collection, java.lang.Object)}.
	 */
	@Test
	public void addingNullWhenCollectionAllowsIt() {
		String testObject = null;
		List<String> testCollection = new ArrayList<String>();
		
		CollectionUtil.addItemIfNotContained(testCollection, testObject);
		assertTrue(testCollection.contains(testObject));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#addItemIfNotContained(java.util.Collection, java.lang.Object)}.
	 */
	@Test
	public void addItemOnlyOnce() {
		String testObject = "testString";
		List<String> testCollection = new ArrayList<String>();
		testCollection.add(testObject);
		
		CollectionUtil.addItemIfNotContained(testCollection, testObject);
		assertTrue(testCollection.size() == 1);
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#addAllItemsIfNotContained(java.util.Collection, java.util.Collection)}.
	 */
	@Test
	public void addAllItems() {
		List<String> testCollection = Arrays.asList(new String[]{"testString", "testString2", "testString3"});
		ArrayList<String> c2 = new ArrayList<String>();
		
		CollectionUtil.addAllItemsIfNotContained(c2, testCollection);
		assertTrue(c2.size() == testCollection.size());
		assertTrue(c2.containsAll(testCollection));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#addAllItemsIfNotContained(java.util.Collection, java.util.Collection)}.
	 */
	@Test
	public void addAllItemsOnlyIfNotContained() {
		List<String> testCollection = Arrays.asList(new String[]{"testString", "testString", "testString2"});
		ArrayList<String> c2 = new ArrayList<String>();
		
		CollectionUtil.addAllItemsIfNotContained(c2, testCollection);
		assertTrue(c2.size() == 2);
		assertTrue(testCollection.containsAll(c2));
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeItem(java.util.Collection, java.lang.Object)}.
	 */
	@Test
	public void removeItem() {
		String testObject = "testString";
		List<String> testCollection = new ArrayList<String>();
		testCollection.add(testObject);
		
		CollectionUtil.removeItem(testCollection, testObject);
		assertTrue(testCollection.isEmpty());
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeItem(java.util.Collection, java.lang.Object)}.
	 */
	@Test
	public void removeNullWhenCollectionAllowsIt() {
		String testObject = null;
		List<String> testCollection = new ArrayList<String>();
		testCollection.add(testObject);
		
		CollectionUtil.removeItem(testCollection, testObject);
		assertTrue(testCollection.isEmpty());
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeItem(java.util.Collection, java.lang.Object)}.
	 */
	@Test
	public void removeItemOnlyOneItemWhenDuplicated() {
		String testObject = "testString";
		List<String> testCollection = new ArrayList<String>();
		testCollection.add(testObject);
		testCollection.add(testObject);
		
		CollectionUtil.removeItem(testCollection, testObject);
		assertTrue(testCollection.size() == 1);
		assertTrue(testCollection.contains(testObject));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeItem(java.util.Collection, java.lang.Object)}.
	 */
	@Test
	public void removeItemDoNotBreakRemovingNotContainedItem() {		
		CollectionUtil.removeItem(new ArrayList<String>(), "testString");
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeAllItems(java.util.Collection, java.util.Collection)}.
	 */
	@Test
	public void testRemoveAllItems() {
		String s1 = "testString";
		String s2 = "testString2";
		String s3 = "testString3";
		List<String> testCollection = Arrays.asList(new String[]{s1, s2, s3});
		ArrayList<String> c2 = new ArrayList<String>(testCollection);
		
		CollectionUtil.removeAllItems(c2, testCollection);
		assertTrue(c2.size() == 0);
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeAllItems(java.util.Collection, java.util.Collection)}.
	 */
	@Test
	public void testRemoveAllItems2() {
		String s1 = "testString";
		String s2 = "testString2";
		String s3 = "testString3";
		List<String> testCollection = Arrays.asList(new String[]{s1, s2});
		ArrayList<String> c2 = new ArrayList<String>(Arrays.asList(new String[]{s1, s2, s3}));
		
		CollectionUtil.removeAllItems(c2, testCollection);
		assertTrue(c2.size() == 1);
		assertTrue(testCollection.contains(s3));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeAllItems(java.util.Collection, java.util.Collection)
	 */
	@Test
	public void removeAllItemsDoNotBreakRemovingNotContainedItem() {
		CollectionUtil.removeAllItems(new ArrayList<String>(), Arrays.asList(new String[]{"testString"}));
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeItem(java.util.Collection, Object)
	 */
	@Test(expected=IllegalArgumentException.class)
	public void removeItemNull() {
		CollectionUtil.removeItem(null, Arrays.asList(new String[]{"testString"}));
	}

	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeAllItems(java.util.Collection, java.util.Collection)
	 */
	@Test(expected=IllegalArgumentException.class)
	public void removeAllItemsNull() {
		CollectionUtil.removeAllItems(null, Arrays.asList(new String[]{"testString"}));
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#removeAllItems(java.util.Collection, java.util.Collection)
	 */
	@Test(expected=IllegalArgumentException.class)
	public void removeAllItemsNull2() {
		CollectionUtil.removeAllItems(Arrays.asList(new String[]{"testString"}), null);
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#addItemIfNotContained(java.util.Collection, Object)
	 */
	@Test(expected=IllegalArgumentException.class)
	public void addItemNull() {
		CollectionUtil.addItemIfNotContained(null, "");
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#addAllItemsIfNotContained(java.util.Collection, java.util.Collection)
	 */
	@Test(expected=IllegalArgumentException.class)
	public void addAllItemsNull() {
		CollectionUtil.addAllItemsIfNotContained(null, new ArrayList<Object>());
	}
	
	/**
	 * Test method for {@link de.vawi.oopjava.kantinenplaner.tools.impl.CollectionUtil#addAllItemsIfNotContained(java.util.Collection, java.util.Collection)
	 */
	@Test(expected=IllegalArgumentException.class)
	public void addAllItemsNull2() {
		CollectionUtil.addAllItemsIfNotContained(new ArrayList<Object>(), null);
	}
}
