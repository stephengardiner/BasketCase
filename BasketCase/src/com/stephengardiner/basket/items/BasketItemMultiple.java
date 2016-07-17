package com.stephengardiner.basket.items;

/**
 * @author Stephen Gardiner
 * 
 *         The idea of this is so you can define bunches, bags, punnets,
 *         multipacks (grapes, apples, strawberries, avocados etc).
 * 
 *         Note Scrapped due to time limits and being guilty of some invention
 *         of requirements!
 *
 */
@Deprecated
public interface BasketItemMultiple extends BasketItem {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stephengardiner.basket.items.ShoppingItem#getUnitOfSale()
	 */
	@Override
	public default ItemUnitOfSale getUnitOfSale() {
		// This is a item that is sold in multiples.
		return ItemUnitOfSale.MULTIPLE;
	}
}
