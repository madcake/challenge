package com.dzencake.challenge;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Runs in separate threads unique challenges.
 * The uniqueness of the task based on the task arguments and her style.
 */
public class ChallengePoolExecutor implements ChallengeExecutor {
	private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	private static final int KEEP_ALIVE_TIME = 60;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	/**
	 * Хендлеры потоков. Необходимы для управления потоком (остановить, убить и прочее)
	 */
	private final ConcurrentHashMap<Object, Future<?>> FUTURES = new ConcurrentHashMap<>();
	private final RuntimeContainerFactory nodeFactory;

	private ThreadPoolExecutor threadPoolExecutor;

	public ChallengePoolExecutor() {
		this(new DefaultRuntimeContainerFactory());
	}

	public ChallengePoolExecutor(RuntimeContainerFactory nodeFactory) {
		threadPoolExecutor = new ThreadPoolExecutor(
				CORE_POOL_SIZE, CORE_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT,
				new LinkedBlockingQueue<Runnable>());
		this.nodeFactory = nodeFactory;
	}

	/**
	 * @see {@link ChallengeExecutor}
	 */
	public <A, V> void execute(Challenge<A, V> backgroundTask, A args, Observer<A, V> backgroundListener) {
		if (backgroundTask == null) {
			throw new IllegalArgumentException("Background Task can't null");
		}
		// Формируем узел и проверяем есть ли такой на выполнении.
		// Так как после выполнения узел удаляется можем не проверять статус потока.
		RuntimeContainer<A, V> node = nodeFactory.make(backgroundTask, args, backgroundListener, this);
		// Если ранее не было добавленно похожего узла, то отправляем его на выполнение и добавляем
		// в список работающих потоков.
		Future<?> future = FUTURES.get(node);
		if (future == null) {
			future = threadPoolExecutor.submit(node);
			FUTURES.put(node, future);
		}
	}

	/**
	 * Return task status.
	 *
	 * @param task The task that to be stopped
	 * @param args The arguments with which the task was started
	 * @param <A> task arguments type
	 * @param <V> task result type
	 *
	 * @return true if task is done
	 *
	 * @deprecated It works incorrectly, because after end, task removed from pool.
	 * It lays in the future, when it will be necessary to wait for the data collection.
	 *
	 */
	@Override
	public <A, V> boolean isTaskDone(Challenge<A, V> task, A args) {
		return FUTURES.get(nodeFactory.make(task, args, null, this)).isDone();
	}

	/**
	 * * @deprecated It works incorrectly, because after end, task removed from pool.
	 * It lays in the future, when it will be necessary to wait for the data collection.
	 *
	 * @param key task identification
	 * @return true if is done
	 */
	@Override
	public boolean isTaskDone(Object key) {
		return FUTURES.get(key).isDone();
	}

	/**
	 * End the task by task object and her arguments
	 *
	 * @param task The task that to be stopped
	 * @param args The arguments with which the task was started
	 * @param <A> task arguments type
	 * @param <V> task result type
	 */
	@Override
	public <A, V> void cancelTask(Challenge<A, V> task, A args) {
		FUTURES.get(nodeFactory.make(task, args, null, this)).cancel(true);
	}

	/**
	 * End the task by @see key
	 *
	 * @param key task identification
	 */
	@Override
	public void cancelTask(Object key) {
		FUTURES.get(key).cancel(true);
	}

	/**
	 * Remove ended task
	 */
	void onNodeFinish(RuntimeContainer node) {
		FUTURES.remove(node);
	}
}
