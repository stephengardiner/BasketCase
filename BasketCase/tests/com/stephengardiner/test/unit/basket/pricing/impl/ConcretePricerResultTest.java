package com.stephengardiner.test.unit.basket.pricing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.stephengardiner.basket.pricing.impl.ConcretePricerResult;

/**
 * @author Stephen Gardiner
 *
 */
public class ConcretePricerResultTest {

	public ConcretePricerResult result;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		result = new ConcretePricerResult(99.99d);
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcretePricerResult#ConcretePricerResult(double)}
	 * .
	 */
	@Test
	public void testConcretePricerResultDoubleConstructor() {
		ConcretePricerResult cpr = new ConcretePricerResult(99.99d);
		assertNotNull(cpr);
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcretePricerResult#ConcretePricerResult(com.stephengardiner.basket.pricing.PricerResult)}
	 * .
	 */
	@Test
	public void testConcretePricerResultCopyConstructor() {
		assertNotNull(result);
		ConcretePricerResult cpr = new ConcretePricerResult(result);
		assertNotNull(cpr);
		assertEquals(cpr, result);
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcretePricerResult#getValue()}
	 * .
	 */
	@Test
	public void testGetValue() {
		assertTrue(result.getValue() == 99.99d);
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcretePricerResult#hashCode()}
	 * .
	 */
	@Test
	public void testHashCode() {
		ConcretePricerResult cpr = new ConcretePricerResult(result);
		assertTrue(cpr.hashCode() == result.hashCode());
		cpr = new ConcretePricerResult(0.0d);
		assertFalse(cpr.hashCode() == result.hashCode());
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcretePricerResult#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsObject() {
		ConcretePricerResult cpr = new ConcretePricerResult(result);
		assertTrue(cpr.equals(result));
		cpr = new ConcretePricerResult(0.0d);
		assertFalse(cpr.equals(result));
	}
}
