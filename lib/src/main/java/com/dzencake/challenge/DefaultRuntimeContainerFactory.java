package com.dzencake.challenge;

class DefaultRuntimeContainerFactory implements RuntimeContainerFactory {

	@Override
	public <A, V> RuntimeContainer<A, V> make(
			Challenge<A, V> backgroundTask,
			A args,
			Observer<A, V> backgroundListener,
			ChallengePoolExecutor executor) {
		return new DefaultRuntimeContainer<>(backgroundTask, args, backgroundListener, executor);
	}
}