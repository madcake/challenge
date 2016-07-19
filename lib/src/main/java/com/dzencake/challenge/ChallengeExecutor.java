package com.dzencake.challenge;

/**
 * Runtime Challenge.
 *
 */
public interface ChallengeExecutor {
	/**
	 *
	 * @param backgroundTask the task to be executed
	 * @param args task arguments or data
	 * @param backgroundListener task callback
	 * @param <A> task arguments or data
	 * @param <V> task result data
	 */
	<A, V> void execute(Challenge<A, V> backgroundTask, A args, Observer<A, V> backgroundListener);

	boolean isTaskDone(Object key);

	<A, V> boolean isTaskDone(Challenge<A, V> backgroundTask, A args);

	<A, V> void cancelTask(Challenge<A, V> task, A args);

	void cancelTask(Object key);
}
