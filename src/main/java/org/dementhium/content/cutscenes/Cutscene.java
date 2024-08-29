package org.dementhium.content.cutscenes;

import org.dementhium.model.player.Player;

/**
 *
 * @author Steve
 *
 */
public class Cutscene {
	
	private final CutsceneAction[] actions;
	private final Player player;

	public Cutscene(Player p, CutsceneAction[] actions) {
		this.player = p;
		this.actions = actions;
	}
	
	public void start() {
//		for (CutsceneAction action : actions) {
//			
//		}
	}

	public CutsceneAction[] getActions() {
		return actions;
	}

	public Player getPlayer() {
		return player;
	}

}
