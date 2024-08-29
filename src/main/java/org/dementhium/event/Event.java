package org.dementhium.event;
/**
 * 
 * @author 'Mystic Flow
 *
 */
public abstract class Event {
	
	private int delay;
	private boolean running;
	
	public Event(int delay) {
		this.delay = delay;
		this.running = true;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void stop() {
		this.running = false;
	}
	
	public int getDelay() { return delay; };
	public boolean isRunning() { return running; };
	
	public abstract void run();
	
}
