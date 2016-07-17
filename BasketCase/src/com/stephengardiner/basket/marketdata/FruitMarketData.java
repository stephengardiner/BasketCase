package com.stephengardiner.basket.marketdata;

/**
 * @author Stephen Gardiner
 *
 */
public interface FruitMarketData {

	/**
	 * How much in currency a kilo of item costs.
	 * 
	 * @return
	 */
	public abstract Double getPricePerKg();

	/**
	 * How much in currency to charge for single item.
	 * 
	 * @return
	 */
	public abstract Double getPriceSingleItem();

}
