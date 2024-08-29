package org.dementhium.event;
/**
 * 
 * @author 'Mystic Flow
 */
public abstract class Tickable {
	
	private int cycles;
	
	private int attempts;
	private boolean running = true;
	
	public Tickable(int cycles) {
		this.cycles = cycles;
		this.attempts = 0;
	}
	
	public void run() {
		attempts++;
		if(attempts >= cycles) {
			attempts = 0;
			execute();
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setTicks(int cycles) {
		this.cycles = cycles;
	}
	
	public void stop() {
		running = false;
	}
	
	public abstract void execute();
	
}
