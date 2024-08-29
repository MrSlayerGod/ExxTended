package org.dementhium.task;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.dementhium.GameExecutor;
import org.dementhium.model.World;

/**
 * 
 * @author 'Mystic Flow
 */
public class ParallelTask implements Task {

	private final Collection<Task> tasks;

	/**
	 * Creates the parallel task.
	 * @param tasks The child tasks.
	 */
	public ParallelTask(Collection<Task> tasks) {
		this.tasks = Collections.unmodifiableCollection(tasks);
	}

	@Override
	public void execute() {
		final CountDownLatch latch = new CountDownLatch(tasks.size());
		final GameExecutor context = World.getWorld().getExecutor();
		for(final Task task : tasks) {
			context.submitTask(new Runnable() {
				@Override
				public void run() {
					try {
						task.execute();
					} catch (Exception e) {
						e.printStackTrace();
					}
					latch.countDown();
				}
			});
		}
		try {
			latch.await(600, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
