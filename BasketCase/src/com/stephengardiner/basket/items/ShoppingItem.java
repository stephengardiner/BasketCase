package com.stephengardiner.basket.items;

import java.util.OptionalDouble;

import com.stephengardiner.pricing.ItemPricingVisitor;

/**
 * @author Stephen Gardiner
 *
 */
public interface ShoppingItem {
	public abstract OptionalDouble price(ItemPricingVisitor pricer);

	public default UnitOfSale getUnitOfSale() {
		return UnitOfSale.INDIVIDUAL;
	}
}

/**
 * Tasks: Item properties<br>
 * Fruit properties<br>
 * Fruit concretes<br>
 * Pricing strategies<br>
 * - Ripeness<br>
 * - Seasonal<br>
 * - Discounting strategies<br>
 * Visitor pattern?<br>
 * Collections of fruits (multipacks, bunches, bags)<br>
 * Units tests<br>
 * - Mocks<br>
 * - Jython?
 **/
