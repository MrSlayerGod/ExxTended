package org.dementhium.action;

import org.dementhium.event.Tickable;
import org.dementhium.model.Mob;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public abstract class Action extends Tickable {

	protected Mob mob;
	
	public Action(int cycles) {
		super(cycles);
	}
	
	public void setEntity(Mob mob) {
		this.mob = mob;
	}
	
	@Override
	public void stop() {
		super.stop();//stops the main tickable
		mob.getActionManager().appendAction(mob.getActionManager().poll());
	}
}
