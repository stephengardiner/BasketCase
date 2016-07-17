package com.stephengardiner.basket.items.impl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.OptionalInt;

import com.google.common.base.Strings;
import com.stephengardiner.basket.items.BasketItem;
import com.stephengardiner.basket.pricing.VisitingPricer;

/**
 * @author Stephen Gardiner
 *
 */
public class FruitBasketItem implements BasketItem {

	// Mandatory Fruit fields
	private final FruitType type;
	private final LocalDate expiryDate;
	private final String variety;
	private final String countryOfOrigin;

	/*
	 * Optional items for non-mandatory Fruit fields; weight being optional as
	 * item might sell on per item basis.
	 */
	private final OptionalInt weightInGrammes;
	private final Optional<String> colour;

	/**
	 * First four params are for mandatory fields (if it's not already obvious).
	 */
	private FruitBasketItem(FruitType fruit, String variety, String countryOfOrigin, LocalDate expiryDate,
			Integer optionalWeightInGrammes, String optionalColour) {
		this.type = fruit;
		this.variety = variety;
		this.countryOfOrigin = countryOfOrigin;
		this.expiryDate = expiryDate;
		this.weightInGrammes = (optionalWeightInGrammes == null ? OptionalInt.empty()
				: OptionalInt.of(optionalWeightInGrammes));
		this.colour = Optional.ofNullable(optionalColour);
	}

	/**
	 * Creator pattern for this object - assumes not a massively changing set of
	 * fields, Builder or Factory patterns might be better for wide variety of
	 * BasketItem implementations or where number of fields are commonly being
	 * extended.
	 * 
	 * @param fruit
	 * @param variety
	 * @param countryOfOrigin
	 * @param expiryDate
	 * @param optionalWeightInGrammes
	 * @param optionalColour
	 * @return
	 */
	public static FruitBasketItem createFruitBasketItem(FruitType fruit, String variety, String countryOfOrigin,
			LocalDate expiryDate, Integer optionalWeightInGrammes, String optionalColour) {
		if (fruit == null)
			throw new IllegalArgumentException("parameter fruit cannot be null");
		if (Strings.isNullOrEmpty(variety))
			throw new IllegalArgumentException("parameter variety cannot be null or empty");
		if (Strings.isNullOrEmpty(countryOfOrigin))
			throw new IllegalArgumentException("parameter countryOfOrigin cannot be null or empty");
		if (expiryDate == null)
			throw new IllegalArgumentException("parameter expiryDate cannot be null");

		return new FruitBasketItem(fruit, variety, countryOfOrigin, expiryDate, optionalWeightInGrammes,
				optionalColour);
	}

	public LocalDate getExpiryDate() {
		return this.expiryDate;
	}

	public String getVariety() {
		return this.variety;
	}

	public FruitType getType() {
		return this.type;
	}

	public String getCountryOfOrigin() {
		return this.countryOfOrigin;
	}

	public OptionalInt getWeightInGrammes() {
		return weightInGrammes;
	}

	public Optional<String> getColour() {
		return colour;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.function.Consumer#accept(java.lang.Object)
	 */
	@Override
	public void accept(VisitingPricer pricer) {
		/*
		 * Call price - state will be accumulated in the VisitingPricer
		 * instance.
		 */
		pricer.price(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countryOfOrigin == null) ? 0 : countryOfOrigin.hashCode());
		result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((variety == null) ? 0 : variety.hashCode());
		result = prime * result + ((weightInGrammes.isPresent()) ? weightInGrammes.getAsInt() : 0);
		result = prime * result + ((colour.isPresent()) ? colour.get().hashCode() : 0);
		return result;
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
		FruitBasketItem other = (FruitBasketItem) obj;
		if (!countryOfOrigin.equals(other.countryOfOrigin)) {
			return false;
		}
		if (!expiryDate.equals(other.expiryDate)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (!variety.equals(other.variety)) {
			return false;
		}
		if (weightInGrammes.isPresent() != other.weightInGrammes.isPresent()) {
			return false;
		}
		/*
		 * if we got here, and the first test below is false, both must be
		 * Optional.empty(), so only test equality if they value isPresent.
		 */
		if (weightInGrammes.isPresent() && (weightInGrammes.getAsInt() != other.weightInGrammes.getAsInt())) {
			return false;
		}
		if (colour.isPresent() != other.colour.isPresent()) {
			return false;
		}
		/*
		 * Same again, if first test evaluates false, both are Optional.empty()
		 * and we should fall skip this test.
		 */
		if (colour.isPresent() && !colour.get().equals(other.colour.get())) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getType().toString());
		sb.append(" - variety: ").append(this.getVariety());
		sb.append(", country of origin: ").append(this.getCountryOfOrigin());
		sb.append(", expires on: ").append(this.getExpiryDate().toString());
		if (this.getWeightInGrammes().isPresent())
			sb.append(", weight:").append(this.getWeightInGrammes().getAsInt()).append("g");
		if (this.colour.isPresent())
			sb.append(", colour: ").append(this.getColour().get());
		return sb.toString();
	}

}
