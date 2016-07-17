package com.stephengardiner.utilities;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Stephen Gardiner
 *
 */
public class FunctionalUtils {

	/**
	 * Borrowed from dzone.com:
	 * 
	 * @see <a href="https://dzone.com/articles/whats-wrong-java-8-currying-vs">
	 *      What's Wrong with Java 8: Currying vs Closures</a>
	 * 
	 * 
	 * @param f
	 * @return
	 */
	public static final <A, B, C> Function<A, Function<B, C>> curry(final BiFunction<A, B, C> f) {
		return (A a) -> (B b) -> f.apply(a, b);
	};

}
