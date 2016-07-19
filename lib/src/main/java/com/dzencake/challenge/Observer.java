package com.dzencake.challenge;

/**
 * {@link Challenge} observer.
 *
 * @param <A> {@link Challenge} input arguments
 * @param <V> {@link Challenge} output data
 */
public interface Observer<A, V> {
    /**
     * Invoked when the {@link Challenge} returns the error {@see err}.
     *
     * @param args {@link Challenge} input arguments
     * @param err the error description
     */
    void onError(A args, Err err);

    /**
     * Invoked when the {@link Challenge} is completed successfully.
     *
     * @param args {@link Challenge} input arguments
     * @param data {@link Challenge} output data
     */
    void onSuccess(A args, V data);

    /**
     * Invoked when it is necessary to announce the beginning of the task.
     *
     * @param args {@link Challenge} input arguments
     */
    void onStart(A args);
}
