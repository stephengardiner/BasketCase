package com.stephengardiner.basket.pricing;

import java.util.Optional;

import com.stephengardiner.basket.items.BasketItem;

/**
 * @author Stephen Gardiner
 *
 */
public interface VisitingPricer {

	/**
	 * Retrieve a PricerResult for this BasketItem from the result store.
	 * 
	 * @return
	 */
	public abstract Optional<PricerResult> getPricingResult(BasketItem item);

	/**
	 * Store a PricerResult against an BasketItem key.
	 * 
	 * @param result
	 */
	public abstract void storePricingResult(BasketItem item, PricerResult result);

	/**
	 * Clear the map of PricerResults keyed to BasketItems.
	 */
	public abstract void clearPricingResults();

	/**
	 * 
	 * @param item
	 */
	public abstract void price(BasketItem item);
}