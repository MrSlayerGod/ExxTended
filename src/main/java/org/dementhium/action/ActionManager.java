package org.dementhium.action;

import java.util.LinkedList;
import java.util.Queue;

import org.dementhium.model.Mob;
import org.dementhium.model.World;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class ActionManager {

	private Queue<Action> queuedActions = new LinkedList<Action>();

	private Action currentAction;
	private Mob mob;

	public ActionManager(Mob mob) {
		this.mob = mob;
	}

	public void appendAction(Action action) {
		if(currentAction != null) {
			queuedActions.add(action);
		} else {
			currentAction = action;
			currentAction.setEntity(mob);
			World.getWorld().submit(action);
		}
	}

	public void clearActions() {
		if(currentAction != null) {
			currentAction.stop();
			currentAction = null;
		}
		queuedActions = new LinkedList<Action>();
		if(mob.isPlayer()) {
			if(!(mob.getPlayer().getAttribute("spellQueued")  == null)) {
				mob.getPlayer().removeAttribute("spellQueued");
			}
		}
		mob.stopCoordinateEvent();
		mob.getMask().setFacePosition(null);
		mob.getMask().setInteractingEntity(null);
		mob.getCombatState().setVictim(null);
		if(mob.getMask().getInteractingEntity() != null) {
			mob.resetTurnTo();
		}
	}

	public Action poll() {
		return queuedActions.poll();
	}

}
