package com.stephengardiner.basket.pricing.impl;

import com.stephengardiner.basket.pricing.PricerResult;

/**
 * @author Stephen Gardiner
 *
 */
public class ConcretePricerResult implements PricerResult {

	// Immutability is important here, as values can be used during discounting
	// and that could be split across threads potentially.
	private final double value;

	/**
	 * Constructor. Uses double primitive to avoid use of null
	 * 
	 * @param value
	 */
	public ConcretePricerResult(double value) {
		this.value = value;
	}

	/**
	 * Copy constructor. Sometimes useful for immutable objects.
	 * 
	 * @param result
	 */
	public ConcretePricerResult(PricerResult result) {
		this.value = result.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stephengardiner.pricing.ItemPricingResult#getValue()
	 */
	@Override
	public double getValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Double.hashCode(this.value);
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
		ConcretePricerResult other = (ConcretePricerResult) obj;
		if (other.getValue() != this.value) {
			return false;
		}
		return true;
	}

}
