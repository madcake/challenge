package com.dzencake.challenge;

/**
 * Factory for unit execution create
 */
public interface RuntimeContainerFactory {
	/**
	 * Make unit execution
	 * @param backgroundTask {@link Challenge} reference
	 * @param args input arguments(data)
	 * @param backgroundListener Callbacks
	 * @param executor execute environment
	 * @param <A> input arguments(data) type
	 * @param <V> output data type
	 * @return {@see RuntimeContainer}
	 */
	<A, V> RuntimeContainer<A, V> make(Challenge<A, V> backgroundTask, A args,
	                                   Observer<A, V> backgroundListener, ChallengePoolExecutor executor);
}