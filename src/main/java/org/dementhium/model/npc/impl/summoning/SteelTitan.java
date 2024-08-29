package org.dementhium.model.npc.impl.summoning;

import org.dementhium.event.Tickable;
import org.dementhium.model.Mob;
import org.dementhium.model.ProjectileManager;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.player.Player;
import org.dementhium.util.Misc;

/**
 * @author Steve
 */
public class SteelTitan extends Familiar {

	public SteelTitan(Player owner) {
		super(7343, owner, true);
		animate(8188);
	}

	@Override
	public void specialAttack(final Mob victim) {
		graphics(1449);
		World.getWorld().submit(new Tickable(1) {

			@Override
			public void execute() {
				victim.hit(Misc.random(getNpc().getDefinition().getMaximumMeleeHit()));
				victim.hit(Misc.random(getNpc().getDefinition().getMaximumMeleeHit()));
				victim.hit(Misc.random(getNpc().getDefinition().getMaximumMeleeHit()));
				victim.hit(Misc.random(getNpc().getDefinition().getMaximumMeleeHit()));
				getCombatState().setAttackDelay(8);
			}
			
		});
		
	}

	@Override
	public CombatHit getHit(Mob mob, final Mob victim) {
		if (canAttackPlayer(mob, victim)) {
			getCombatState().setAttackDelay(8);
			int randomAttack = r.nextInt(2);
			switch (randomAttack) {
			case 0: //Melee
				animate(8183);
				int meleeDamage = Misc.random(getNpc().getDefinition().getMaximumMeleeHit() * 10);
				if (victim.isPlayer()) {
					if (victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.MELEE)) {
						meleeDamage = 0;
					}
				}
				victim.hit(meleeDamage);
				break;
			case 1: //Mage
				animate(8190);
				ProjectileManager.sendDelayedProjectile(this, victim, 1445, 190, 140, false);
				int mageDamage = Misc.random((getNpc().getDefinition().getMaximumMeleeHit() + 20) * 10);
				if (victim.isPlayer()) {
					if (victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.MAGIC)) {
						mageDamage = 0;
					}
				}
				final int finalMageDamage = mageDamage;
				World.getWorld().submit(new Tickable(2) {
					@Override
					public void execute() {
						victim.hit(finalMageDamage);
						this.stop();
					}
				});
				break;
			case 2: //Range
				animate(8190);
				int rangeDamage = Misc.random(getNpc().getDefinition().getMaximumMeleeHit() * 10);
				ProjectileManager.sendDelayedProjectile(this, victim, 1445, 190, 140, false);
				if (victim.isPlayer()) {
					if (victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.RANGE)) {
						rangeDamage = 0;
					}
				}
				final int finalRangeDamage = rangeDamage;
				World.getWorld().submit(new Tickable(2) {
					@Override
					public void execute() {
						victim.hit(finalRangeDamage);
						this.stop();
					}
				});
				break;
			}

		}
		return null;
	}


}
