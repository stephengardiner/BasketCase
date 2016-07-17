package com.stephengardiner.test.unit.basket.pricing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.stephengardiner.basket.items.BasketItem;
import com.stephengardiner.basket.items.impl.FruitBasketItem;
import com.stephengardiner.basket.pricing.PricerResult;
import com.stephengardiner.basket.pricing.impl.ConcretePricerResult;
import com.stephengardiner.basket.pricing.impl.ConcreteVisitingPricer;
import com.stephengardiner.utilities.FunctionalUtils;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

/**
 * @author Stephen Gardiner
 *
 */
@RunWith(JMockit.class)
public class ConcreteVisitingPricerTest {

	/**
	 * Mocked result class
	 */
	public static final class ConcretePricerResultMockup extends MockUp<ConcretePricerResult> {

		private double value;

		public void $init(double d) {
			value = d;
		}

		@Mock
		public double getValue() {
			return value;
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// creates a Mocked version of the pricer result object
		new ConcretePricerResultMockup();
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcreteVisitingPricer#ConcreteVisitingPricer(java.util.function.Function, java.util.Optional)}
	 * .
	 */
	@Test
	public void testConcreteVisitingPricerConstructor(
			@Injectable final Function<BasketItem, PricerResult> mockPricingStrategy,
			@Injectable final BiFunction<BasketItem, PricerResult, PricerResult> mockDiscountingStrategy) {
		ConcreteVisitingPricer cvp = new ConcreteVisitingPricer(mockPricingStrategy,
				Optional.of(mockDiscountingStrategy));
		assertNotNull(cvp);
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcreteVisitingPricer#price(com.stephengardiner.basket.items.BasketItem)}
	 * .
	 */
	@Test
	public void testPrice(@Injectable final Function<BasketItem, PricerResult> mockPricingStrategy,
			@Injectable final BiFunction<BasketItem, PricerResult, PricerResult> mockDiscountingStrategy,
			@Injectable FruitBasketItem testApple) {
		ConcretePricerResult mockedPricerResult = new ConcretePricerResult(127.34d);
		ConcretePricerResult mockedDiscounterResult = new ConcretePricerResult(101.12d);
		new Expectations() {
			{
				mockPricingStrategy.apply(testApple);
				result = mockedPricerResult;
				times = 1;
				Function<BasketItem, Function<PricerResult, PricerResult>> curried = FunctionalUtils
						.curry(mockDiscountingStrategy);
				curried.apply(testApple);
				result = mockedDiscounterResult;
				times = 1;
			}
		};
		ConcreteVisitingPricer cvp = new ConcreteVisitingPricer(mockPricingStrategy,
				Optional.of(mockDiscountingStrategy));
		cvp.price(testApple);
		new Verifications() {
			{
				cvp.storePricingResult(testApple, mockedDiscounterResult);
				times = 1;
			}
		};
		assertEquals(cvp.getPricingResult(testApple), Optional.of(mockedDiscounterResult));
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcreteVisitingPricer#storePricingResult(com.stephengardiner.basket.items.BasketItem, com.stephengardiner.basket.pricing.PricerResult)}
	 * .
	 */
	@Test
	public void testStorePricingResult(@Injectable final Function<BasketItem, PricerResult> mockPricingStrategy,
			@Injectable final BiFunction<BasketItem, PricerResult, PricerResult> mockDiscountingStrategy,
			@Injectable FruitBasketItem testApple) {
		ConcretePricerResult mockedResult = new ConcretePricerResult(127.34d);
		ConcreteVisitingPricer cvp = new ConcreteVisitingPricer(mockPricingStrategy,
				Optional.of(mockDiscountingStrategy));
		cvp.storePricingResult(testApple, mockedResult);
		assertEquals(Optional.of(mockedResult), cvp.getPricingResult(testApple));
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcreteVisitingPricer#getPricingResult(com.stephengardiner.basket.items.BasketItem)}
	 * .
	 */
	@Test
	public void testGetPricingResult(@Injectable final Function<BasketItem, PricerResult> mockPricingStrategy,
			@Injectable final BiFunction<BasketItem, PricerResult, PricerResult> mockDiscountingStrategy,
			@Injectable FruitBasketItem testApple) {
		ConcretePricerResult mockedResult = new ConcretePricerResult(127.34d);
		ConcreteVisitingPricer cvp = new ConcreteVisitingPricer(mockPricingStrategy,
				Optional.of(mockDiscountingStrategy));
		cvp.storePricingResult(testApple, mockedResult);
		assertNotNull(cvp.getPricingResult(testApple));
		assertEquals(Optional.of(mockedResult), cvp.getPricingResult(testApple));
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcreteVisitingPricer#clearPricingResults()}
	 * .
	 */
	@Test
	public void testClearPricingResults(@Injectable final Function<BasketItem, PricerResult> mockPricingStrategy,
			@Injectable final BiFunction<BasketItem, PricerResult, PricerResult> mockDiscountingStrategy,
			@Injectable FruitBasketItem testApple) {
		ConcretePricerResult mockedResult = new ConcretePricerResult(127.34d);
		ConcreteVisitingPricer cvp = new ConcreteVisitingPricer(mockPricingStrategy,
				Optional.of(mockDiscountingStrategy));
		cvp.storePricingResult(testApple, mockedResult);
		assertNotNull(cvp.getPricingResult(testApple));
		cvp.clearPricingResults();
		assertFalse(cvp.getPricingResult(testApple).isPresent());
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcreteVisitingPricer#hashCode()}
	 * .
	 */
	@Test
	public void testHashCode(@Injectable final Function<BasketItem, PricerResult> mockPricingStrategy,
			@Injectable final BiFunction<BasketItem, PricerResult, PricerResult> mockDiscountingStrategy,
			@Injectable FruitBasketItem testApple) {
		ConcreteVisitingPricer cvp1 = new ConcreteVisitingPricer(mockPricingStrategy,
				Optional.of(mockDiscountingStrategy));
		ConcreteVisitingPricer cvp2 = new ConcreteVisitingPricer(mockPricingStrategy,
				Optional.of(mockDiscountingStrategy));
		assertEquals(cvp1.hashCode(), cvp2.hashCode());
		ConcretePricerResult mockedResult = new ConcretePricerResult(127.34d);
		cvp1.storePricingResult(testApple, mockedResult);
		assertNotEquals(cvp1.hashCode(), cvp2.hashCode());
	}

	/**
	 * Test method for
	 * {@link com.stephengardiner.basket.pricing.impl.ConcreteVisitingPricer#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public void testEqualsObject(@Injectable final Function<BasketItem, PricerResult> mockPricingStrategy,
			@Injectable final BiFunction<BasketItem, PricerResult, PricerResult> mockDiscountingStrategy,
			@Injectable FruitBasketItem testApple) {
		ConcreteVisitingPricer cvp1 = new ConcreteVisitingPricer(mockPricingStrategy,
				Optional.of(mockDiscountingStrategy));
		ConcreteVisitingPricer cvp2 = new ConcreteVisitingPricer(mockPricingStrategy,
				Optional.of(mockDiscountingStrategy));
		assertEquals(cvp1, cvp2);
		ConcretePricerResult mockedResult = new ConcretePricerResult(127.34d);
		cvp1.storePricingResult(testApple, mockedResult);
		assertNotEquals(cvp1, cvp2);
	}

}
