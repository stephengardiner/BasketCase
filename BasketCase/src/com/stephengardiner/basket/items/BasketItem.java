package com.stephengardiner.basket.items;

import java.util.function.Consumer;

import com.stephengardiner.basket.pricing.VisitingPricer;

/**
 * @author Stephen Gardiner
 * 
 *         Base basket item is for a single item, i.e. an apple, a banana, etc
 *
 */
public interface BasketItem extends Consumer<VisitingPricer> {
	/**
	 * This is a singular item and is priced and sold as such.
	 * 
	 * @return
	 */
	public default ItemUnitOfSale getUnitOfSale() {
		return ItemUnitOfSale.INDIVIDUAL;
	}
}

/**
 * Tasks: Item properties<br>
 * Fruit properties<br>
 * Fruit concretes<br>
 * Pricing strategies<br>
 * Discounting strategies<br>
 * - Ripeness<br>
 * - Seasonal<br>
 * Visitor pattern?<br>
 * Mutliples of fruits (multipacks, bunches, bags)? - inventing requirements<br>
 * Unit tests<br>
 * - Mocks<br>
 * - Jython for testing? - too complicated to setup for this demo program, in a
 * way that reviewer could easily setup.<br>
 * Integration test<br>
 * - Very simple pricing and discounting strategy<br>
 * - Market data driven pricing
 **/
