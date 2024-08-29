package org.dementhium.task.impl;

import org.dementhium.model.combat.Combat;
import org.dementhium.model.combat.CombatState;
import org.dementhium.model.npc.NPC;
import org.dementhium.task.Task;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class NPCTickTask implements Task {

	private final NPC npc;
	
	public NPCTickTask(NPC npc) {
		this.npc = npc;
	}
	
	@Override
	public void execute() {
		npc.processQueuedHits();
		npc.tick();
		npc.getWalkingQueue().getNextEntityMovement();
		
		CombatState cState = npc.getCombatState();
		Combat.process(npc);
		if(cState.getAttackDelay() > 0) {
			cState.setAttackDelay(cState.getAttackDelay() - 1);
		}
	}

}
