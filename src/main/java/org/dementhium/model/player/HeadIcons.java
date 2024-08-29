package org.dementhium.model.player;

public class HeadIcons {

	private Player player;
	private int cycles = 0;

	public HeadIcons(Player player) {
		this.player = player;
	}
	
	public int getIcon() {
		return player.isSkulled ? 0 : -1;
	}

	public boolean isSkulled() {
		return player.isSkulled;
	}

	public void setSkulledOn(String username) {
		player.skulledOn = username;
	}

	public String getSkulledOn() {
		return player.skulledOn;
	}

	public int getCycles() {
		return cycles;
	}

	public void setCycles(int c) {
		cycles = c;
	}

	public void setSkulled(boolean b) {
		if(b && player.getLocation().atBountyHunter() || b && player.getLocation().atGe()) {
			return;
		}
		if(b) {
			cycles = 2400; // 2400 = 20mins
		} else {
			cycles = 0;
		}
		player.isSkulled = b;
		player.getMask().setApperanceUpdate(true);
	}

	public void tick() {
		if(cycles > 0) {
			cycles--;
		}
		if(cycles == 1) {
			setSkulled(false);
		}
	}

}
