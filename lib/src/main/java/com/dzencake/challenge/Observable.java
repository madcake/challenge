package com.dzencake.challenge;

public interface Observable<A, V> {
	void register(Observer<A, V> observer);
	void unregister(Observer<A, V> observer);

	void notifyError(A args, Err err);
	void notifySuccess(A args, V data);
	void notifyStart(A args);
}
