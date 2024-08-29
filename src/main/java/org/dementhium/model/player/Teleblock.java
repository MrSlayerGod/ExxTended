package org.dementhium.model.player;

public class Teleblock {

	private Player player;
	private int cycles = 0;

	public Teleblock(Player player) {
		this.player = player;
	}

	public boolean isTeleblocked() {
		return player.isTeleblocked;
	}

	public void setTeleblocked(boolean b) {
		if(b) {
			cycles = 600; // 600 = 5mins
		} else {
			cycles = 0;
		}
		player.isTeleblocked = b;
	}

	public int getCycles() {
		return cycles;
	}

	public void setCycles(int c) {
		cycles = c;
	}

	public void tick() {
		if(cycles > 0) {
			cycles--;
		}
		if(cycles == 1) {
			setTeleblocked(false);
		}
	}

}
