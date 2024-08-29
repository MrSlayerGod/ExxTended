package org.dementhium.model.npc.impl.godwars;

import org.dementhium.model.Mob;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.npc.NPC;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class Nex extends NPC {

	//private static final int DEFAULT = 13447;
	
	private static final CombatAction NEX_COMBAT_ACTION = new NexCombatAction();
	
	public Nex(int id) {
		super(id);
	}
	
	
	@Override
	public CombatAction getCustomCombatAction() {
		return NEX_COMBAT_ACTION;
	}
	
	private static final class NexCombatAction extends CombatAction {

		@Override
		public CombatHit hit(Mob mob, Mob victim) {
			return null;
		}

		@Override
		public boolean canAttack(Mob mob, Mob victim) {
			return true;
		}
		
	}

}
