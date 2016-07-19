package com.dzencake.challenge;

import android.util.Log;

import java.io.IOException;

final class DefaultRuntimeContainer<A, V> extends RuntimeContainer<A, V> {

	public DefaultRuntimeContainer(
			Challenge<A, V> backgroundTask, A args, Observer<A, V> backgroundListener,
			ChallengePoolExecutor executor) {
		super(backgroundTask, args, backgroundListener, executor);
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& (obj == this || obj.getClass().equals(this.getClass())
				&& backgroundTask.getClass().equals(((DefaultRuntimeContainer) obj).backgroundTask.getClass())
				&& (args != null && args.equals(((DefaultRuntimeContainer) obj).args))
				|| (args == null && ((DefaultRuntimeContainer) obj).args == null));
	}

	@Override
	public int hashCode() {
		return (backgroundTask.getClass().getCanonicalName() + "_" + (args == null ? "" : args.hashCode()))
				.hashCode();
	}

	@Override
	public Object key() {
		return this;
	}

	@Override
	public void run() {
		try {
			exec();
		} catch (ChallengeException ex) {
			Log.d("BACKGROUND_TASK_ERROR", "Trace: ", ex);
			backgroundListener.onError(args, ex.err);
		} catch (IOException ex) {
			Log.d("BACKGROUND_TASK_ERROR", "Trace: ", ex);
			backgroundListener.onError(args, new Err(Err.IO_ERROR, ""));
		} catch (Exception ex) {
			Log.d("BACKGROUND_TASK_ERROR", "Trace: ", ex);
			backgroundListener.onError(args, new Err(Err.UNKNOWN_ERROR, ""));
		}
	}
}