package org.dementhium.model.npc.impl.godwars;

import org.dementhium.model.Mob;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.npc.NPC;
import org.dementhium.util.Misc;

/**
 * @author Stephen
 */
public class GeneralGraardor extends NPC {
	//id = 6260
	
	private final CombatAction GRAADOR_COMBAT_ACTION = new GrardorCombatAction();
	public static final String[] WAR_CRIES = {"For the glory of Bandos!", "Death to our enemies!", "Brargh!", "Split their skulls!", "We feast on the bones of our enemies tonight!", "CHAAARGE!", "Crush them underfoot!", "All glory to Bandos!", "GRAAAAAAAAAR!"};
	
	public GeneralGraardor(int id) {
		super(id);
	}

	@Override
	public CombatAction getCustomCombatAction() {
		return GRAADOR_COMBAT_ACTION;
	}
	
	@Override
	public void tick() {
		if (r.nextBoolean() && r.nextBoolean() && r.nextBoolean() && getCombatState().getVictim() != null && !isDead()) {
			forceText(WAR_CRIES[r.nextInt(WAR_CRIES.length)]);
		}
	}
	
	public class GrardorCombatAction extends CombatAction {

		@Override
		public CombatHit hit(Mob mob, Mob victim) {
			getCombatState().setAttackDelay(7);
			int randomAttack = Misc.random(1);
			switch (randomAttack) {
			case 0: //Melee 
				int meleeDamage = Misc.random(getDefinition().getMaximumMeleeHit() / 10);
				animate(7060);
				if (victim.isPlayer()) {
					if (victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.MELEE)) {
						meleeDamage = 0;
					}
				}
				victim.hit(meleeDamage);
				break;
			case 1: //Magical Range
				animate(7063);
				int rangeDamage = Misc.random(355);
				if (victim.isPlayer()) {
					if (victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.RANGE)) {
						rangeDamage = 0;
					}
				}
				victim.hit(rangeDamage);
				break;
			}
			return null;
		}

		@Override
		public boolean canAttack(Mob mob, Mob victim) {
			if (mob.isDead() || victim.isDead()) {
				resetCombat();
				return false;
			}
			return true;
		}
		
	}
	
}
