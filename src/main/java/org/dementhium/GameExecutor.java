package org.dementhium;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.dementhium.event.Event;
import org.dementhium.task.Task;
import org.dementhium.util.DementhiumThreadFactory;


/**
 * 
 * @author 'Mystic Flow
 */
public final class GameExecutor {

	private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS; //quick ref
	private static final int PROCESSORS = Runtime.getRuntime().availableProcessors() + 1;

	private final ExecutorService taskService = Executors.newFixedThreadPool(PROCESSORS, new DementhiumThreadFactory("ParallelService"));
	private final ScheduledExecutorService gameExecutor = Executors.newScheduledThreadPool(1, new DementhiumThreadFactory("GameExecutor"));
	private final ExecutorService workingService = Executors.newFixedThreadPool(1, new DementhiumThreadFactory("WorkingService"));
	private final ExecutorService js5Worker = Executors.newFixedThreadPool(2, new DementhiumThreadFactory("JS5-Worker"));

	public void submitTask(final Runnable task) {
		taskService.submit(task);
	}

	public void submit(final Task task) {
		gameExecutor.submit(new Runnable() {
			public void run() {
				try {
					task.execute();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void schedule(final Event event) {
		schedule(event, event.getDelay());
	}

	public void schedule(final Event event, final long time) {
		gameExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				if (event.isRunning()) {
					try {
						event.run();
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					return;
				}
				long end = System.currentTimeMillis();
				long next = event.getDelay() - (end - start);
				if(event.isRunning()) {
					schedule(event, next); //re schedule after we're done
				}
			}
		}, time, TIME_UNIT);
	}

	public void scheduleAtFixedRate(final Event event) {
		gameExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if(event.isRunning()) {
					try {
						event.run();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, event.getDelay(), event.getDelay(), TIME_UNIT);
	}

	public void submitWork(final Runnable runnable) {
		workingService.submit(new Runnable() {
			@Override
			public void run() {
				try { 
					runnable.run();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void submitJs5(final Runnable runnable) {
		js5Worker.submit(new Runnable() {
			@Override
			public void run() {
				try { 
					runnable.run();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}