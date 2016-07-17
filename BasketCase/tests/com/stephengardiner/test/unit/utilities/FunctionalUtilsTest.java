package com.stephengardiner.test.unit.utilities;

import static org.junit.Assert.assertTrue;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

import com.stephengardiner.utilities.FunctionalUtils;

/**
 * @author Stephen Gardiner
 *
 */
public class FunctionalUtilsTest {

	/**
	 * Test method for
	 * {@link com.stephengardiner.utilities.FunctionalUtils#curry(java.util.function.BiFunction)}
	 * .
	 */
	@Test
	public void testCurry() {
		assertTrue(true);
		BiFunction<Integer, Integer, Integer> curryTestPart1 = (Integer a, Integer b) -> {
			return a * b;
		};
		BiFunction<Integer, Integer, Integer> curryTestPart2 = (Integer c, Integer d) -> {
			return c + d;
		};
		Function<Integer, Function<Integer, Integer>> curried = FunctionalUtils.curry(curryTestPart2);

		// test BiFunctions separately
		Integer test1 = curryTestPart1.apply(3, 4);
		assertTrue(test1 == 12);
		Integer test2 = curryTestPart2.apply(test1, 5);
		assertTrue(test2 == 17);

		// test currying gets same result ((3 * 4) + 5 = 17)
		Integer testCurried = curryTestPart1.andThen(curried.apply(5)).apply(3, 4);
		assertTrue(testCurried == test2);

		// test opposite way gives expected result ((3 + 4) * 5 = 35)
		testCurried = curryTestPart2.andThen(FunctionalUtils.curry(curryTestPart1).apply(5)).apply(3, 4);
		assertTrue(testCurried == 35);

		// test last expected result (3 * (4 + 5) = 27)
		testCurried = curryTestPart2.andThen(FunctionalUtils.curry(curryTestPart1).apply(3)).apply(4, 5);
		assertTrue(testCurried == 27);
	}

}
