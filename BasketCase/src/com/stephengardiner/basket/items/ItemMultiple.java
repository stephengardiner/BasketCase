package com.stephengardiner.basket.items;

/**
 * @author Stephen Gardiner
 *
 */
public interface ItemMultiple extends ShoppingItem {
	@Override
	public default UnitOfSale getUnitOfSale() {
		return UnitOfSale.MULTIPLE;
	}
}
