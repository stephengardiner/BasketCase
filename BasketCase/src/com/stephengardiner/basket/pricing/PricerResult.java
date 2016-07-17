package com.stephengardiner.basket.pricing;

/**
 * @author Stephen Gardiner
 *
 */
public interface PricerResult {

	/**
	 * Return a value captured from a pricing strategy.
	 * 
	 * @return
	 */
	public abstract double getValue();
}
