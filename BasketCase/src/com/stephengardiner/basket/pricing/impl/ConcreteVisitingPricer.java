package com.stephengardiner.basket.pricing.impl;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.stephengardiner.basket.items.BasketItem;
import com.stephengardiner.basket.pricing.PricerResult;
import com.stephengardiner.basket.pricing.VisitingPricer;
import com.stephengardiner.utilities.FunctionalUtils;

/**
 * Hopefully thread-safe; strategy functions are final, result store is
 * thread-safe implementation of HashMap.
 * 
 * @author Stephen Gardiner
 *
 */
public class ConcreteVisitingPricer implements VisitingPricer {

	/*
	 * Implemented as a concurrent HashMap as the same visitor may be used from
	 * multiple threads if the basket of items is split up for multiprocessing
	 * (i.e. thinking Java 8 stream processing here).
	 */
	private final ConcurrentMap<BasketItem, Optional<PricerResult>> pricingResults = new ConcurrentHashMap<>();
	private final Function<BasketItem, PricerResult> pricingStrategy;
	private final Optional<BiFunction<BasketItem, PricerResult, PricerResult>> discountingStrategy;

	/**
	 * Constructor that expects a mandatory pricing strategy function and an
	 * optional discounting strategy.
	 */
	public ConcreteVisitingPricer(Function<BasketItem, PricerResult> priceStrategy,
			Optional<BiFunction<BasketItem, PricerResult, PricerResult>> discountStrategy) {
		this.pricingStrategy = priceStrategy;
		if (discountStrategy.isPresent()) {
			this.discountingStrategy = discountStrategy;
		} else {
			this.discountingStrategy = Optional.empty();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.stephengardiner.pricing.ItemPricingVisitor#price(com.stephengardiner.
	 * basket.items.BasketItem)
	 */
	@Override
	public void price(BasketItem item) {
		PricerResult result = null;

		/*
		 * Discounting will be applied after pricing, if optional discounting
		 * strategy is defined.
		 */
		if (discountingStrategy.isPresent()) {
			Function<BasketItem, Function<PricerResult, PricerResult>> discounter = FunctionalUtils
					.curry(discountingStrategy.get());
			result = pricingStrategy.andThen(discounter.apply(item)).apply(item);
		} else {
			result = pricingStrategy.apply(item);
		}
		this.storePricingResult(item, result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.stephengardiner.pricing.ItemPricingVisitor#storePricingResult(com.
	 * stephengardiner.basket.items.BasketItem,
	 * com.stephengardiner.pricing.ItemPricingResult)
	 */
	@Override
	public void storePricingResult(BasketItem item, PricerResult result) {
		if (result == null) {
			/*
			 * We store the Optional in the Map, so that we know if pricing was
			 * done. If we don't store the empty result, it cannot easily be
			 * detected if there was a pricing error or if the item was skipped
			 * for some reason; this is something I have found important for
			 * management/auditor reporting.
			 */
			this.pricingResults.put(item, Optional.empty());
		} else {
			// wrap in Optional
			this.pricingResults.put(item, Optional.of(result));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stephengardiner.pricing.ItemPricingVisitor#getPricingResult(com.
	 * stephengardiner.basket.items.BasketItem)
	 */
	@Override
	public Optional<PricerResult> getPricingResult(BasketItem item) {
		Optional<PricerResult> result = Optional.empty();
		if (this.pricingResults.containsKey(item)) {
			result = this.pricingResults.get(item);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.stephengardiner.basket.pricing.VisitingPricer#clearPricingResults()
	 */
	@Override
	public void clearPricingResults() {
		this.pricingResults.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((!discountingStrategy.isPresent()) ? 0 : discountingStrategy.get().hashCode());
		result = prime * result + pricingResults.hashCode();
		result = prime * result + pricingStrategy.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ConcreteVisitingPricer other = (ConcreteVisitingPricer) obj;
		if (discountingStrategy.isPresent() != other.discountingStrategy.isPresent()) {
			return false;
		} else if (discountingStrategy.isPresent()
				&& !discountingStrategy.get().equals(other.discountingStrategy.get())) {
			return false;
		}
		if (!pricingResults.equals(other.pricingResults)) {
			return false;
		}
		if (!pricingStrategy.equals(other.pricingStrategy)) {
			return false;
		}
		return true;
	}

}
