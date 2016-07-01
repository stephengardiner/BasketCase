package com.stephengardiner.basket.items.fruit;

import java.time.LocalDate;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import org.eclipse.jdt.annotation.NonNull;

import com.stephengardiner.basket.items.ShoppingItem;
import com.stephengardiner.pricing.ItemPricingVisitor;

/**
 * @author Stephen Gardiner
 *
 */
public class Fruit implements ShoppingItem {

	@NonNull
	private final LocalDate expiryDate;
	@NonNull
	private final String variety;
	@NonNull
	private final FruitType type;
	@NonNull
	private final String countryOfOrigin;

	// Optional items for non-mandatory Fruit fields
	private final OptionalInt weightInGrammes;
	private final Optional<String> colour;

	/**
	 * Constructor. Non-"Optional" params are manadatory.
	 */
	public Fruit(FruitType fruit, String variety, String countryOfOrigin, LocalDate expiryDate,
			OptionalInt weightInGrammes, Optional<String> colour) {
		this.type = fruit;
		this.variety = variety;
		this.countryOfOrigin = countryOfOrigin;
		this.expiryDate = expiryDate;
		this.weightInGrammes = weightInGrammes;
		this.colour = colour;
	}

	/**
	 * 
	 * @return
	 */
	public LocalDate getExpiryDate() {
		return this.expiryDate;
	}

	/**
	 * 
	 * @return
	 */
	public String getVariety() {
		return this.variety;
	}

	/**
	 * 
	 * @return
	 */
	public FruitType getType() {
		return this.type;
	}

	/**
	 * 
	 * @return
	 */
	public String getCountryOfOrigin() {
		return this.countryOfOrigin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.stephengardiner.basket.items.ShoppingItem#price(com.stephengardiner.
	 * pricing.ItemPricingVisitor)
	 */
	@Override
	public OptionalDouble price(ItemPricingVisitor pricer) {
		// TODO Auto-generated method stub
		return OptionalDouble.empty();
	}

}
