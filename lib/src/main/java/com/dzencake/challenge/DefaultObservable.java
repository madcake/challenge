package com.dzencake.challenge;

import android.os.Looper;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DefaultObservable<A, V> implements Observable<A, V> {

	protected final Set<Observer<A, V>> receivers = new CopyOnWriteArraySet<>();
	private final SendNotifyHelper notificationThread;

	public DefaultObservable() {
		this(new SendNotifyHelper());
	}

	public DefaultObservable(SendNotifyHelper notificationThread) {
		this.notificationThread = notificationThread;
	}

	@Override
	public void register(Observer<A, V> observer) {
		receivers.add(observer);

	}

	@Override
	public void unregister(Observer<A, V> observer) {
		receivers.remove(observer);
	}

	@Override
	public void notifyError(A args, Err err) {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			notificationThread.error(receivers, args, err);
		} else {
			for (Observer<A, V> receiver : receivers) {
				receiver.onError(args, err);
			}
		}
	}

	@Override
	public void notifySuccess(A args, V data) {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			notificationThread.success(receivers, args, data);
		} else {
			for (Observer<A, V> receiver : receivers) {
				receiver.onSuccess(args, data);
			}
		}
	}

	@Override
	public void notifyStart(A args) {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			notificationThread.loading(receivers, args);
		} else {
			for (Observer<A, V> receiver : receivers) {
				receiver.onStart(args);
			}
		}
	}
}