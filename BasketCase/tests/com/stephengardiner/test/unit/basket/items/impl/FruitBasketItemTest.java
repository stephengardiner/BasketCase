package com.stephengardiner.test.unit.basket.items.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.stephengardiner.basket.items.impl.FruitBasketItem;
import com.stephengardiner.basket.items.impl.FruitType;
import com.stephengardiner.basket.pricing.VisitingPricer;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;

/**
 * @author Stephen Gardiner
 *
 */
@RunWith(JMockit.class)
public class FruitBasketItemTest {

	public static FruitBasketItem testApple;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testApple = FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Cox", "United Kingdom",
				LocalDate.of(2016, 07, 4), 160, "Red/Green");
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#FruitBasketItem(com.stephengardiner.basket.items.impl.FruitType, java.lang.String, java.lang.String, java.time.LocalDate, java.lang.Integer, java.lang.String)}
	 * .
	 */
	@Test
	public void testFruitBasketItemCreation() {
		// check a legal object can be created
		assertNotNull(FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Cox", "United Kingdom",
				LocalDate.now().plusDays(4), 160, "Red/Green"));
		// test null optional weight param
		assertNotNull(FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Cox", "United Kingdom",
				LocalDate.now().plusDays(4), null, "Red/Green"));
		// test null optional colour param
		assertNotNull(FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Cox", "United Kingdom",
				LocalDate.now().plusDays(4), 160, null));
		// test empty string optional colour param
		assertNotNull(FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Cox", "United Kingdom",
				LocalDate.now().plusDays(4), 160, ""));

		// test mandatory parameters for FruitBaksetItem creation
		try {
			// FruitType
			FruitBasketItem.createFruitBasketItem(null, "Cox", "United Kingdom", LocalDate.now().plusDays(4), 160,
					"Red/Green");
			fail("Should have thrown an illegal argument exption here");
		} catch (IllegalArgumentException illegalArgEx) {
			// expected
		}
		try {
			// Variety
			FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "", "United Kingdom", LocalDate.now().plusDays(4),
					160, "Red/Green");
			fail("Should have thrown an illegal argument exption here");
		} catch (IllegalArgumentException illegalArgEx) {
			// expected
		}
		// Country of origin
		try {
			FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Cox", null, LocalDate.now().plusDays(4), 160,
					"Red/Green");
			fail("Should have thrown an illegal argument exption here");
		} catch (IllegalArgumentException illegalArgEx) {
			// expected
		}
		try {
			FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Cox", "United Kingdom", null, 160, "Red/Green");
			fail("Should have thrown an illegal argument exption here");
		} catch (IllegalArgumentException illegalArgEx) {
			// expected
		}
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#hashCode()}
	 * .
	 */
	@Test
	public void testHashCode() {
		// test where there should be equality
		assertTrue(testApple.hashCode() == FruitBasketItem.createFruitBasketItem(FruitType.APPLE,
				testApple.getVariety(), testApple.getCountryOfOrigin(), testApple.getExpiryDate(),
				testApple.getWeightInGrammes().getAsInt(), testApple.getColour().get()).hashCode());
		// test optional param changes
		assertFalse(testApple.hashCode() == FruitBasketItem
				.createFruitBasketItem(testApple.getType(), testApple.getVariety(), testApple.getCountryOfOrigin(),
						testApple.getExpiryDate(), 5, testApple.getColour().get())
				.hashCode());
		assertFalse(testApple.hashCode() == FruitBasketItem
				.createFruitBasketItem(testApple.getType(), testApple.getVariety(), testApple.getCountryOfOrigin(),
						testApple.getExpiryDate(), testApple.getWeightInGrammes().getAsInt(), "Mauve")
				.hashCode());
		// test mandatory params
		assertFalse(testApple.hashCode() == FruitBasketItem.createFruitBasketItem(FruitType.BANANA,
				testApple.getVariety(), testApple.getCountryOfOrigin(), testApple.getExpiryDate(),
				testApple.getWeightInGrammes().getAsInt(), testApple.getColour().get()).hashCode());
		assertFalse(testApple.hashCode() == FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Golden Delicious",
				testApple.getCountryOfOrigin(), testApple.getExpiryDate(), testApple.getWeightInGrammes().getAsInt(),
				testApple.getColour().get()).hashCode());
		assertFalse(testApple.hashCode() == FruitBasketItem.createFruitBasketItem(FruitType.APPLE,
				testApple.getVariety(), "New Zealand", testApple.getExpiryDate(),
				testApple.getWeightInGrammes().getAsInt(), testApple.getColour().get()).hashCode());
		assertFalse(testApple.hashCode() == FruitBasketItem
				.createFruitBasketItem(testApple.getType(), testApple.getVariety(), testApple.getCountryOfOrigin(),
						LocalDate.now(), testApple.getWeightInGrammes().getAsInt(), testApple.getColour().get())
				.hashCode());
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsObject() {
		// test equals where equality is expected
		assertTrue(testApple.equals(FruitBasketItem.createFruitBasketItem(testApple.getType(), testApple.getVariety(),
				testApple.getCountryOfOrigin(), testApple.getExpiryDate(), testApple.getWeightInGrammes().getAsInt(),
				testApple.getColour().get())));
		// batch of tests where inequality is expected
		assertFalse(testApple.equals(FruitBasketItem.createFruitBasketItem(testApple.getType(), testApple.getVariety(),
				testApple.getCountryOfOrigin(), LocalDate.now(), testApple.getWeightInGrammes().getAsInt(),
				testApple.getColour().get())));
		assertFalse(testApple.equals(FruitBasketItem.createFruitBasketItem(testApple.getType(), testApple.getVariety(),
				testApple.getCountryOfOrigin(), testApple.getExpiryDate(), 5, testApple.getColour().get())));
		assertFalse(testApple.equals(FruitBasketItem.createFruitBasketItem(testApple.getType(), testApple.getVariety(),
				testApple.getCountryOfOrigin(), testApple.getExpiryDate(), testApple.getWeightInGrammes().getAsInt(),
				"Mauve")));
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#getExpiryDate()}
	 * .
	 */
	@Test
	public void testGetExpiryDate() {
		assertNotNull(testApple.getExpiryDate());
		assertEquals(testApple.getExpiryDate(), LocalDate.of(2016, 07, 04));
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#getVariety()}
	 * .
	 */
	@Test
	public void testGetVariety() {
		assertNotNull(testApple.getVariety());
		assertEquals(testApple.getVariety(), "Cox");
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#getType()}.
	 */
	@Test
	public void testGetType() {
		assertNotNull(testApple.getType());
		assertTrue(testApple.getType() == FruitType.APPLE);
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#getCountryOfOrigin()}
	 * .
	 */
	@Test
	public void testGetCountryOfOrigin() {
		assertNotNull(testApple.getCountryOfOrigin());
		assertEquals(testApple.getCountryOfOrigin(), "United Kingdom");
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#getWeightInGrammes()}
	 * .
	 */
	@Test
	public void testGetWeightInGrammes() {
		assertNotNull(testApple.getWeightInGrammes());
		assertTrue(testApple.getWeightInGrammes().isPresent());
		assertEquals(testApple.getWeightInGrammes().getAsInt(), 160);
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#getColour()}
	 * .
	 */
	@Test
	public void testGetColour() {
		assertNotNull(testApple.getColour());
		assertTrue(testApple.getColour().isPresent());
		assertEquals(testApple.getColour().get(), "Red/Green");
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.items.impl.FruitBasketItem#accept(com.stephengardiner.pricing.VisitingPricer)}
	 * .
	 */
	@Test
	public void testAcceptWithMockedVisitor(@Injectable final VisitingPricer mock) {
		new Expectations() {
			{
				mock.price(testApple);
				times = 1;
			}
		};
		/*
		 * accept Pricer visitor and check that the Fruit BasketItem
		 * implementation calls it.
		 */
		testApple.accept(mock);
	}

}
