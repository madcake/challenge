package com.dzencake.challenge;

import android.os.Handler;
import android.os.Looper;

import java.util.Set;

public class SendNotifyHelper {
	private final Handler handler;

	public SendNotifyHelper() {
		this(new Handler(Looper.getMainLooper()));
	}

	public SendNotifyHelper(Handler handler) {
		this.handler = handler;
	}

	/**
	 * Invoke method onError in Main thread
	 *
	 * @param receivers listener references
	 * @param args input arguments(data)
	 * @param err output error
	 */
	public <A, V> void error(final Set<Observer<A, V>> receivers, final A args, final Err err) {
		handler.post(new CallbacksRunnable<>(receivers, args, err));
	}

	/**
	 * Invoke method onSuccess in Main thread
	 *
	 * @param receivers listener references
	 * @param args input arguments(data)
	 * @param data output data
	 */
	public <A, V> void success(final Set<Observer<A, V>> receivers, final A args, final V data) {
		handler.post(new CallbacksRunnable<>(receivers, args, data));
	}

	/**
	 * Invoke method onStart in Main thread
	 *
	 * @param receivers listener references
	 * @param args input arguments(data)
	 */
	public <A, V> void loading(final Set<Observer<A, V>> receivers, final A args) {
		handler.post(new CallbacksRunnable<>(receivers, args));
	}

	private static class CallbacksRunnable<A, V> implements Runnable {

		private static final int ERROR = -1;
		private static final int PROGRESS = 0;
		private static final int SUCCESS = 1;

		private final Set<Observer<A, V>> receivers;
		private final A args;
		private final V data;
		private final Err err;

		private int type;

		/**
		 * Constructor for loading call
		 */
		CallbacksRunnable(Set<Observer<A, V>> receivers, A args) {
			this(receivers, args, null, null);
			type = PROGRESS;
		}

		/**
		 * Constructor for success call
		 */
		CallbacksRunnable(Set<Observer<A, V>> receivers, A args, V data) {
			this(receivers, args, data, null);
			type = SUCCESS;
		}

		/**
		 * Constructor for err call
		 */
		CallbacksRunnable(Set<Observer<A, V>> receivers, A args, Err err) {
			this(receivers, args, null, err);
			type = ERROR;
		}

		CallbacksRunnable(Set<Observer<A, V>> receivers, A args, V data, Err err) {
			this.receivers = receivers;
			this.args = args;
			this.data = data;
			this.err = err;
		}

		@Override
		public void run() {
			if (type == SUCCESS) {
				for (Observer<A, V> receiver : receivers) {
					receiver.onSuccess(args, data);
				}
			} else if (type == ERROR) {
				for (Observer<A, V> receiver : receivers) {
					receiver.onError(args, err);
				}
			} else {
				for (Observer<A, V> receiver : receivers) {
					receiver.onStart(args);
				}
			}
		}
	}
}