package com.dzencake.challenge;

/**
 * Container operations.
 *
 * @param <A> input arguments(data)
 * @param <V> output data
 */
public interface Challenge<A, V> {
	/**
	 *
	 * @param args input arguments(data)
	 * @return results of operations
	 * @throws Exception runtime errors, determined by the specific {@see Challenge}
	 */
	V run(A args) throws Exception;
}