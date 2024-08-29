package org.dementhium.model.npc.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.dementhium.content.skills.Prayer;
import org.dementhium.event.Tickable;
import org.dementhium.model.Mob;
import org.dementhium.model.ProjectileManager;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.npc.NPC;
import org.dementhium.util.Misc;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 *
 */
public class TzTokJad extends NPC {

	private static final CombatAction JAD_COMBAT_ACTION = new JadCombatAction();
	private static final Random random = new Random();
	private static int style;
	private static final List<NPC> healers = new ArrayList<NPC>();
	private static boolean addedHealers = false;
	private static boolean healersNeeded = false;

	public TzTokJad(int id) {
		super(id);
	}

	@Override
	public CombatAction getCustomCombatAction() {
		return JAD_COMBAT_ACTION;
	}

	private static final class JadCombatAction extends CombatAction {

		@Override
		public CombatHit hit(final Mob jad, final Mob victim) {
			jad.getCombatState().setAttackDelay(6);
			int damage = 0;
			jad.turnTo(victim);
			if (jad.getNpc().getHp() <= 1250) {
				healersNeeded = true;
			}
			switch(style) {
			case 0:// melee
				jad.animate(9277);
				damage = Misc.random(jad.getNpc().getDefinition().getMaximumMeleeHit());
				final int fDamageMelee = damage;
				World.getWorld().submit(new Tickable(2) {
					@Override
					public void execute() {
						int meleeDamage = fDamageMelee;
						if (victim.isPlayer()) {
							Prayer prayer = victim.getPlayer().getPrayer();
							if (prayer.usingCorrispondingPrayer(FightType.MELEE)) {
								meleeDamage = 0;
							}
						}
						victim.hit(meleeDamage);
						this.stop();
					}
				});
				break;
			case 1:// range
				jad.animate(9276);
				jad.graphics(1625);
				damage = Misc.random(jad.getNpc().getDefinition().getMaximumMeleeHit());
				World.getWorld().submit(new Tickable(2) {
					@Override
					public void execute() {
						victim.graphics(451);
						this.stop();
					}
				});
				final int fDamageRange = damage;
				World.getWorld().submit(new Tickable(4) {
					@Override
					public void execute() {
						int rangeDamage = fDamageRange;
						if (victim.isPlayer()) {
							Prayer prayer = victim.getPlayer().getPrayer();
							if (prayer.usingCorrispondingPrayer(FightType.RANGE)) {
								rangeDamage = 0;
							}
						}
						victim.hit(rangeDamage);
						this.stop();
					}
				});
				break;
			case 2:// magic
				jad.animate(9300);
				jad.graphics(1626);
				damage = Misc.random(jad.getNpc().getDefinition().getMaximumMeleeHit() - 10);
				World.getWorld().submit(new Tickable(2) {
					@Override
					public void execute() {
						ProjectileManager.sendGlobalProjectile(1627, jad, victim, 120, 140, 20);
						this.stop();
					}
				});
				final int fDamageMage = damage;
				World.getWorld().submit(new Tickable(4) {
					@Override
					public void execute() {
						int mageDamage = fDamageMage;
						if (victim.isPlayer()) {
							Prayer prayer = victim.getPlayer().getPrayer();
							if (prayer.usingCorrispondingPrayer(FightType.MAGIC)) {
								mageDamage = 0;
							}
						}
						victim.hit(mageDamage);
						this.stop();
					}
				});
				break;
			}
			if (healersNeeded) {
				if (!addedHealers) {
					addedHealers = true;
					NPC healer1 = new NPC(2746, jad.getLocation().getX() + 3, jad.getLocation().getY() - 3, jad.getLocation().getZ());
					NPC healer2 = new NPC(2746, healer1.getLocation().getX() - 2, healer1.getLocation().getY() + 2, healer1.getLocation().getZ());
					NPC healer3 = new NPC(2746, healer2.getLocation().getX() - 1, healer2.getLocation().getY() + 1, healer1.getLocation().getZ());
					NPC healer4 = new NPC(2746, healer3.getLocation().getX() + 2, healer1.getLocation().getY() - 1, healer1.getLocation().getZ());
					healers.add(healer1);
					healers.add(healer2);
					healers.add(healer3);
					healers.add(healer4);
					for (NPC heal : healers) {
						World.getWorld().getNpcs().add(heal);
					}
				}
				Iterator<NPC> it = healers.iterator();
				while(it.hasNext()) {
					NPC heal = it.next();
					if (heal.isDead()) {
						World.getWorld().getNpcs().remove(heal);
						it.remove();
						continue;
					}
					heal.animate(9254);
					heal.graphics(444);
					jad.getNpc().setHp(jad.getNpc().getHp() + 50);
				}
			}
			return null;
		}

		@Override
		public boolean canAttack(Mob jad, Mob victim) {
			int randomStyle = random.nextInt(100);
			int distance = jad.getLocation().getDistance(victim.getLocation());
			if (randomStyle <= 30) {
				if (random.nextBoolean()) {
					if (distance <= 1 && random.nextBoolean()) {
						style = 0;
					} else if (randomStyle > 20 && randomStyle < 30) {
						style = 1;
					} else if (randomStyle > 10 && randomStyle < 20) {
						style = 2;
					} else if (distance <= 1) {
						style = 0;
					}
				} else {
					style = r.nextInt(3);
				}
			} else {
				if (distance <= 1 && random.nextBoolean() && randomStyle > 40 && randomStyle < 55) {
					style = 0;
				} else if (randomStyle > 55 && randomStyle < 80 && random.nextBoolean()) {
					style = 1;
				} else if (randomStyle > 80 && randomStyle < 95 && !random.nextBoolean() ) {
					style = 2;
				} else if (distance <= 1) {
					style = 0;
				} else {
					style = r.nextInt(3);
				}
			}
			return style == 0 || style == 1 || style == 2 && jad.getLocation().withinDistance(victim.getLocation(), 8);
		}
	}
}