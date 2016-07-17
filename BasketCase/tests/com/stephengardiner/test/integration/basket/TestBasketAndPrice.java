package com.stephengardiner.test.integration.basket;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.BeforeClass;
import org.junit.Test;

import com.stephengardiner.basket.items.BasketItem;
import com.stephengardiner.basket.items.impl.FruitBasketItem;
import com.stephengardiner.basket.items.impl.FruitType;
import com.stephengardiner.basket.marketdata.FruitMarketData;
import com.stephengardiner.basket.pricing.PricerResult;
import com.stephengardiner.basket.pricing.impl.ConcretePricerResult;
import com.stephengardiner.basket.pricing.impl.ConcreteVisitingPricer;
import com.stephengardiner.utilities.YamlUtils;

/**
 * Some smoke tests to see if this all hangs together properly.
 * 
 * @author Stephen Gardiner
 *
 */
public class TestBasketAndPrice {

	// Yaml resource data
	private static Map<FruitType, Map<String, FruitMarketData>> marketPrices = new HashMap<>();

	// Set of static test data
	@SuppressWarnings("serial")
	private static final Set<BasketItem> basket = new HashSet<BasketItem>() {
		{
			add(FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Cox", "United Kingdom",
					LocalDate.now().plusDays(4), 160, "Red/Green"));
			add(FruitBasketItem.createFruitBasketItem(FruitType.APPLE, "Granny Smith", "South Africa",
					LocalDate.now().plusDays(2), 189, "Green"));
			add(FruitBasketItem.createFruitBasketItem(FruitType.BANANA, "Chiquita", "Costa Rica",
					LocalDate.now().plusDays(3), 205, "Yellow"));
			add(FruitBasketItem.createFruitBasketItem(FruitType.BANANA, "Dwarf Cavendish", "Jamaica",
					LocalDate.now().plusDays(4), 175, "Yellow"));
			add(FruitBasketItem.createFruitBasketItem(FruitType.ORANGE, "Navel", "Spain", LocalDate.now().plusDays(6),
					198, "Orange"));
			add(FruitBasketItem.createFruitBasketItem(FruitType.LEMON, "Berna", "Spain", LocalDate.now().plusDays(8),
					137, "Yellow"));
			add(FruitBasketItem.createFruitBasketItem(FruitType.PEACH, "Rochester", "United Kingdom",
					LocalDate.now().plusDays(2), 150, "Yellow/Red"));
			add(FruitBasketItem.createFruitBasketItem(FruitType.PEACH, "Rochester", "United Kingdom",
					LocalDate.now().plusDays(2), null, "Yellow/Red"));
		}
	};

	// Very simple fixed-price pricing strategy for test purposes
	private static final Function<BasketItem, PricerResult> fixedPricingStrategy = (BasketItem fruit) -> {
		ConcretePricerResult priceResult = null;
		if (FruitBasketItem.class.isInstance(fruit)) {
			priceResult = new ConcretePricerResult(0.23d);
			System.out.println("Priced @ " + priceResult.getValue());
		}
		return priceResult;
	};

	// Very simple discounting strategy for integration test
	private static final BiFunction<BasketItem, PricerResult, PricerResult> fixedDiscountingStrategy = (
			BasketItem fruit, PricerResult result) -> {
		ConcretePricerResult discountResult = null;
		if (FruitBasketItem.class.isInstance(fruit)) {
			discountResult = new ConcretePricerResult(result.getValue() * 0.66d);
			System.out.println("Discounted to: " + discountResult.getValue());
		}
		return discountResult;
	};

	/**
	 * Load and format the test market data
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Iterable<Object> data = YamlUtils.loadYaml(new File("resources/integration/fruitMarketData.yaml"));
		for (Object obj : data) {
			if (Map.class.isInstance(obj)) {
				@SuppressWarnings("unchecked")
				Map<String, Map<String, List<Double>>> marketData = (Map<String, Map<String, List<Double>>>) obj;
				for (String key : marketData.keySet()) {
					// let bad values in data break the loading of the data
					FruitType type = FruitType.valueOf(key);
					Map<String, List<Double>> values = marketData.get(key);
					if (values != null) {
						Map<String, FruitMarketData> fruitMarketPrices = new HashMap<>();
						/*
						 * Attempt to create interface for market data (rushed
						 * this a bit) from loosely structured yaml; first
						 * element is price per kilo, 2nd is price per item (see
						 * file : resources/unittest/fruitMarketData.yaml)
						 */
						values.entrySet().stream()
								.forEach((es) -> fruitMarketPrices.put(es.getKey(), new FruitMarketData() {

									@Override
									public Double getPriceSingleItem() {
										return es.getValue().get(1);
									}

									@Override
									public Double getPricePerKg() {
										return es.getValue().get(0);
									}
								}));
						marketPrices.put(type, fruitMarketPrices);
					}
				}
			} else {
				throw new RuntimeException("Bad test data");
			}
		}
	}

	/**
	 * 
	 * Very basic test using simplest pricing and discounting strategies.
	 * 
	 */
	@Test
	public void testSimpleIntegration() {

		System.out.println("--------------- Pricing with simple fixed value");

		// For the logs (TODO: should use a logger interface, here)
		basket.stream().forEach(System.out::println);

		// Price and discount
		ConcreteVisitingPricer cpv = new ConcreteVisitingPricer(fixedPricingStrategy,
				Optional.of(fixedDiscountingStrategy));
		basket.stream().forEach(fruit -> fruit.accept(cpv));

		// total up the results of pricing and discounting
		double total = basket.stream().mapToDouble(i -> cpv.getPricingResult(i).orElse(() -> 0.0d).getValue()).sum();
		System.out.println("The value totalled is: " + total);
		assertEquals(1.2144d, total, 0.0000001);
	}

	/*
	 * Ever-so-slightly more sophisticated pricing strategy which looks up
	 * market data supplied from a file - this could fairly easily be adapted to
	 * a more well-defined interface with a number of potential implementations;
	 * i.e. one that looks up prices from a Commodities exchange or a perhaps an
	 * internal company database based on open market prices and taking into
	 * account warehouse stock levels, for instance.
	 */
	private static final Function<BasketItem, PricerResult> marketDataDrivenPricingStrategy = (BasketItem fruit) -> {
		ConcretePricerResult priceResult = null;
		if (FruitBasketItem.class.isInstance(fruit)) {
			FruitBasketItem myFruit = (FruitBasketItem) fruit;
			// look for fruit type in market data
			if (!marketPrices.containsKey(myFruit.getType()))
				throw new IllegalArgumentException("Missing market data for type: " + myFruit.getType());
			else {
				// filter by variety, throwing exception if not found
				Map<String, FruitMarketData> varietyPrices = marketPrices.get(myFruit.getType());
				FruitMarketData priceStream = varietyPrices.entrySet().stream()
						.filter((e) -> e.getKey().equals(myFruit.getVariety())).findFirst()
						.orElseThrow(() -> new RuntimeException("Missing market data for variety")).getValue();

				// got some market data, so price if optional weight is present
				if (myFruit.getWeightInGrammes().isPresent()) {
					priceResult = new ConcretePricerResult(
							(priceStream.getPricePerKg() / 1000.0d) * myFruit.getWeightInGrammes().getAsInt());
				} else {
					// use single item price
					priceResult = new ConcretePricerResult(priceStream.getPriceSingleItem());
				}
			}
			System.out.println("Priced " + fruit + " @ " + priceResult.getValue());
		}
		return priceResult;
	};

	/**
	 * 
	 * Test that uses external market data for pricing strategy.
	 * 
	 */
	@Test
	public void testMarketDataDrivenPricingIntegration() {

		System.out.println("--------------- Pricing with market data");

		// Price and discount
		ConcreteVisitingPricer cpv = new ConcreteVisitingPricer(marketDataDrivenPricingStrategy, Optional.empty());
		basket.stream().forEach(fruit -> fruit.accept(cpv));

		// total up the results of pricing and discounting
		double total = basket.stream().mapToDouble(i -> cpv.getPricingResult(i).orElse(() -> 0.0d).getValue()).sum();
		System.out.println("The value totalled is: " + total);
		assertEquals(3.12991d, total, 0.0000001);
	}

}
