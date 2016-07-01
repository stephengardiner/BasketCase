package com.stephengardiner.pricing;

import java.util.Optional;

/**
 * @author Stephen Gardiner
 *
 */
public interface ItemPricingVisitor {
	public abstract ItemPricingStrategy getPricingStrategy();

	public abstract Optional<ItemDiscountStrategy> getDiscountingStrategy();
}
