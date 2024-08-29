package org.dementhium.content.cutscenes;

import org.dementhium.model.player.Player;


/**
 *
 * @author Steve
 *
 */
public abstract class CutsceneAction {

	final int delay;
	private final Player player;
	
	public CutsceneAction(Player p, int delay) {
		this.player = p;
		this.delay = delay;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public abstract void execute();

	public Player getPlayer() {
		return player;
	}
}
