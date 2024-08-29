package org.dementhium.model.combat.impl;

import org.dementhium.content.misc.Following;
import org.dementhium.content.skills.magic.MagicHandler;
import org.dementhium.content.skills.magic.Spell;
import org.dementhium.model.Mob;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.Combat.FightType;
/**
 * 
 * @author 'Mystic Flow
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author Stephen
 */
public final class MagicCombat extends CombatAction {

	private static final CombatAction INSTANCE = new MagicCombat();

	public static CombatAction getAction() {
		return INSTANCE;
	}

	@Override
	public CombatHit hit(Mob attacker, Mob victim) {
		if(attacker.getAttribute("spellQueued") != null) {
			return null;
		}
		if(victim.isDead()) {
			return null;
		}
		if(attacker.isPlayer()) {
			Spell spell = (Spell) attacker.getAttribute("autoCastSpell");
			MagicHandler.cast(attacker.getPlayer(), victim, attacker.getPlayer().getSettings().getSpellBook(), spell.getSpellId());
		}
		return null;
	}

	@Override
	public boolean canAttack(Mob mob, Mob victim) {
		if (!mob.getLocation().withinDistance(victim.getLocation(), 8) && (mob.getFightType() == FightType.RANGE || mob.getFightType() == FightType.MAGIC)) {
			if(mob.isPlayer()) {
				Following.combatFollow(mob, victim);
				return false;
			} else {
				Following.npcFollow(mob.getNpc(), victim);
			}
		}
		if(victim.isNPC() && mob.isPlayer()) {
			if(victim.getNpc().getOriginalLocation().distance(mob.getLocation()) > 13 && mob.getPlayer().getEquipment().usingRanged()) {
					mob.getPlayer().sendMessage("You're too far away!");
					mob.resetCombat();
					return false;
			}
		}
		return true;
	}
}
