package org.dementhium.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class DementhiumThreadFactory implements ThreadFactory {

	private final String name;

	
	private final AtomicInteger threadCount = new AtomicInteger();
	
	public DementhiumThreadFactory(String name) {
		this.name = name;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, new StringBuilder(name).append("-").append(threadCount.getAndIncrement()).toString());
		//thread.setPriority(priority);
		return thread;
	}

}
