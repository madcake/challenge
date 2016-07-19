package com.dzencake.challenge;

import android.os.Process;

/**
 * Unit execution for the thread pool. Contains {@link Challenge} reference, callback and arguments.
 *
 * @param <A> input arguments(data)
 * @param <V> output data
 */
public abstract class RuntimeContainer<A, V> implements Runnable {
	private final ChallengePoolExecutor endListener;
	final A args;
	final Observer<A, V> backgroundListener;
	protected final Challenge<A, V> backgroundTask;

	/**
	 *
	 * @param backgroundTask {@link Challenge}
	 * @param args input arguments(data)
	 * @param backgroundListener {@link Challenge} callbacks
	 * @param endListener to remove links from the cache
	 */
	public RuntimeContainer(Challenge<A, V> backgroundTask,
	                        A args,
	                        Observer<A, V> backgroundListener,
	                        ChallengePoolExecutor endListener) {
		this.backgroundTask = backgroundTask;
		this.args = args;
		this.backgroundListener = backgroundListener;
		this.endListener = endListener;
	}

	public abstract Object key();

	public final void exec() throws Exception {
		try {
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			backgroundListener.onStart(args);
			V data = backgroundTask.run(args);
			backgroundListener.onSuccess(args, data);
		} finally {
			endListener.onNodeFinish(this);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		RuntimeContainer other = (RuntimeContainer) obj;

		return key().equals(other.key());
	}

	@Override
	public int hashCode() {
		return key().hashCode();
	}
}