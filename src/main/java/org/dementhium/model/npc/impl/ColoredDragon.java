package org.dementhium.model.npc.impl;

import java.util.Random;

import org.dementhium.model.Mob;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.npc.NPC;
import org.dementhium.util.Misc;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 */
public class ColoredDragon extends NPC {

	private static final CombatAction DRAGON_COMBAT_ACTION = new DragonCombatAction();

	public ColoredDragon(int id) {
		super(id);
	}

	@Override
	public CombatAction getCustomCombatAction() {
		return DRAGON_COMBAT_ACTION;
	}

	@Override
	public boolean isAggressive() {
		return true;
	}

	private static final class DragonCombatAction extends CombatAction {
		FightType style = null;
		@Override
		public boolean canAttack(Mob dragon, Mob victim) {
			Random r = new Random();
			int randomStyle = r.nextInt(7);
			switch(randomStyle) {
			case 0:
			case 3:
			case 5:
				if (dragon.getLocation().withinDistance(victim.getLocation(), 1))
					this.style = FightType.MELEE;
				else 
					this.style = FightType.MAGIC;
				break;
			case 1:
			case 2:
			case 6:
			case 7:
				this.style = FightType.MAGIC;
				break;
			default:
				this.style = FightType.MAGIC;
			}
			return style != null && dragon.getLocation().withinDistance(victim.getLocation(), 8);
		}

		@Override
		public CombatHit hit(Mob dragon, Mob victim) {
			if (canAttack(dragon, victim)) {
				Random r2 = new Random();
				int atkEmote = r2.nextInt(2);
				int damage = 0;
				switch(style) {
				case MELEE:
					dragon.getCombatState().setAttackDelay(6);
					damage = Misc.random(dragon.getNpc().getDefinition().getMaximumMeleeHit());
					switch(atkEmote) {
					case 1:
						dragon.animate(12252);
						break;
					case 2:
						dragon.animate(12256);
					default:
						dragon.animate(80);
						break;
					}
					break;
				case MAGIC:
					dragon.getCombatState().setAttackDelay(7);
					if (victim.isPlayer()) {
						if (!(victim.getPlayer().getEquipment().getSlot(3) != 1540) || !(victim.getPlayer().getEquipment().getSlot(3) != 11283)) {
							damage = Misc.random(65);
							if (damage < 40) {
								damage = 40;
							}
						}
					}
					dragon.animate(14245);
					dragon.graphics(1164);
					break;
				}
				return new CombatHit(dragon, victim, damage, 2);
			}
			return null;
		}
	}
}