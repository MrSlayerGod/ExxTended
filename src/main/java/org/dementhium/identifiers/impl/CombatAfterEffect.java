package org.dementhium.identifiers.impl;

import java.util.Random;

import org.dementhium.content.skills.Prayer;
import org.dementhium.event.Tickable;
import org.dementhium.identifiers.Identifier;
import org.dementhium.identifiers.IdentifierManager;
import org.dementhium.model.Mob;
import org.dementhium.model.ProjectileManager;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.ChatMessage;
import org.dementhium.util.Misc;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class CombatAfterEffect extends Identifier {

	public static final int DEFLECT_ANIMATION = 12573;

	public static final int LEECH_ANIMATION = 12575;

	private final Random r = new Random();


	@Override
	public void identify(Object... args) {
		Mob mob = (Mob) args[0];
		Mob victim = (Mob) args[1];
		CombatHit hit = (CombatHit) args[2];
		FightType type = (FightType) args[3];
		if(hit != null && type != FightType.MAGIC) {
			if(hit.getIdentifier() != null) {
				IdentifierManager.get(hit.getIdentifier()).identify(hit.getIdentification());
			}
			victim.addEnemyHit(mob, hit.getDamage());
			if(victim.isPlayer() && mob.isPlayer()) {
				if(!victim.getPlayer().getHeadIcons().isSkulled() || victim.getPlayer().getHeadIcons().isSkulled() && !victim.getPlayer().getHeadIcons().getSkulledOn().equalsIgnoreCase(mob.getPlayer().getUsername())) {
					mob.getPlayer().getHeadIcons().setSkulled(true);
					mob.getPlayer().getHeadIcons().setSkulledOn(victim.getPlayer().getUsername());
				}
			}
		}
		if(victim.isAutoRetaliating()) {
			if(victim.isPlayer()) {
				if(victim.getPlayer().dontRetaliate) {
					return;
				}
			}
			victim.getCombatState().setVictim(mob);
		}
		if (victim.getAttribute("vengeance") == Boolean.TRUE) {
			vengeance(mob, victim);
		}
		if (mob.isPlayer() && hit != null) {
			cursePrayers(mob.getPlayer(), victim, type, hit.getDamage());
		} else if(mob.isPlayer() && hit == null) {
			cursePrayers(mob.getPlayer(), victim, type, 0);
		}
	}

	public void cursePrayers(final Player player, final Mob victim, final FightType fightType, int lastHit) {
		if(fightType == FightType.MAGIC) {
			lastHit = player.getCombatState().getLastHit();
		}
		if(player.getPrayer().usingPrayer(1, Prayer.SOUL_SPLIT)) {
			int type = 0;
			if(lastHit >= 5) {
				int healed = lastHit / 5;
				if(healed > 0) {
					player.getSkills().heal(healed);
					type = 1;
				}
				if(lastHit >= 50) {
					int remove = lastHit / 50;
					if(remove > 0) {
						if(victim.isPlayer()) {
							victim.getPlayer().getSkills().drainPray(remove);
						}
						type = 2;
					}
				}
				if(type != 0) {
					player.getCombatState().setLastHit(type == 1 ? lastHit - (lastHit/5) : type == 2 ? lastHit - (lastHit/50) : 0);
				}
				ProjectileManager.sendGlobalProjectile(2263, player, victim, 120, 130, 20);
				World.getWorld().submit(new Tickable(2) {
					public void execute() {
						victim.graphics(2264);
						ProjectileManager.sendGlobalProjectile(2263, victim, player, 120, 130, 20);
						this.stop();
					}
				});
			}
		} else {
			if(victim.isPlayer()) {
				Player pVictim = victim.getPlayer();
				int deflectGraphics = -1;
				if(pVictim.getPrayer().usingPrayer(1, Prayer.DEFLECT_MAGIC) && fightType == FightType.MAGIC) {
					deflectGraphics = 2228;
				} else if(pVictim.getPrayer().usingPrayer(1, Prayer.DEFLECT_MELEE) && fightType == FightType.MELEE) {
					deflectGraphics = 2230;
				} else if(pVictim.getPrayer().usingPrayer(1, Prayer.DEFLECT_MISSILES) && fightType == FightType.RANGE) {
					deflectGraphics = 2229;
				}
				if(deflectGraphics > -1 && r.nextInt(3) == 1 && lastHit > 0) {
					if(!pVictim.isGfxing()) {
						pVictim.animate(DEFLECT_ANIMATION);
						pVictim.graphics(deflectGraphics);
					}
					player.hit((int) (lastHit * 0.10));
				}
			}
		}
		if(victim.isPlayer()) {
			if(r.nextInt(7) == 1) {
				int leechProj = -1;
				int leechGfxRise = -1;
				int leechGfxDown = -1;
				String leechName = "N/A";
				if(player.getPrayer().usingPrayer(1, Prayer.LEECH_ATTACK)) {
					leechProj = 2231;
					leechGfxRise = 2232;
					leechGfxDown = 2233;
					leechName = "attack skill";
				} else if(player.getPrayer().usingPrayer(1, Prayer.LEECH_RANGE)) {
					leechProj = 2236;
					leechGfxRise = 2238;
					leechGfxDown = 2237;
					leechName = "ranged skill";
				} else if(player.getPrayer().usingPrayer(1, Prayer.LEECH_MAGIC)) {
					leechProj = 2240;
					leechGfxRise = 2242;
					leechGfxDown = 2241;
					leechName = "magic skill";
				} else if(player.getPrayer().usingPrayer(1, Prayer.LEECH_DEFENCE)) {
					leechProj = 2244;
					leechGfxRise = 2246;
					leechGfxDown = 2245;
					leechName = "defence skill";
				} else if(player.getPrayer().usingPrayer(1, Prayer.LEECH_STRENGTH)) {
					leechProj = 2248;
					leechGfxRise = 2250;
					leechGfxDown = 2249;
					leechName = "strength skill";
				} else if(player.getPrayer().usingPrayer(1, Prayer.LEECH_ENERGY)) {
					leechProj = 2252;
					leechGfxRise = 2254;
					leechGfxDown = 2253;
					leechName = "run energy";
				} else if(player.getPrayer().usingPrayer(1, Prayer.LEECH_SPECIAL_ATTACK)) {
					leechProj = 2256;
					leechGfxRise = 2258;
					leechGfxDown = 2257;
					leechName = "special attack";
				}
				final int lpt = leechProj;
				final int lgr = leechGfxRise;
				final int lgd = leechGfxDown;
				final String ln = leechName;
				if(leechProj > -1 && leechGfxRise > -1 && leechGfxDown > -1 && !leechName.equals("N/A")) {
					World.getWorld().submit(new Tickable(1) {
						public void execute() {
							player.animate(LEECH_ANIMATION);
							World.getWorld().submit(new Tickable(2) {
								public void execute() {
									ProjectileManager.sendGlobalProjectile(lpt, player, victim, 120, 130, 20);
									this.stop();
								}
							});
							World.getWorld().submit(new Tickable(4) {
								public void execute() {
									double deductSpec = ((int) victim.getPlayer().getSpecialAmount() - (victim.getPlayer().getSpecialAmount() / (Misc.random(5) + 5)));
									double boostSpec = ((int) player.getPlayer().getSpecialAmount() + (player.getPlayer().getSpecialAmount() * 0.05));
									double deductEnergy = ((int) victim.getPlayer().getWalkingQueue().getRunEnergy() - (victim.getPlayer().getWalkingQueue().getRunEnergy() / (Misc.random(5) + 5)));
									double boostEnergy = ((int) player.getPlayer().getWalkingQueue().getRunEnergy() + (player.getPlayer().getWalkingQueue().getRunEnergy() * 0.05));
									if(!victim.isGfxing()) {
										victim.graphics(lgr);
										victim.getPlayer().sendMessage("You feel leeched and weakened of your "+ln+".");
										if(player.getPrayer().usingPrayer(1, Prayer.LEECH_ENERGY)) {
											if(deductEnergy < 0) {
												victim.getPlayer().getWalkingQueue().setRunEnergy(0);
											} else {
												victim.getPlayer().getWalkingQueue().setRunEnergy((int) deductEnergy);
											}
											if(boostEnergy > 100) {
												player.getPlayer().getWalkingQueue().setRunEnergy(100);
											} else {
												player.getPlayer().getWalkingQueue().setRunEnergy((int) boostEnergy);
											}
										}
										if(player.getPrayer().usingPrayer(1, Prayer.LEECH_SPECIAL_ATTACK)) {
											if(deductSpec < 0) {
												victim.getPlayer().setSpecialAmount(0, true);
											} else {
												victim.getPlayer().setSpecialAmount((int) deductSpec, true);
											}
											if(boostSpec > 1000) {
												player.getPlayer().setSpecialAmount(1000, true);
											} else {
												player.getPlayer().setSpecialAmount((int) boostSpec, true);
											}
										}
									}
									this.stop();
								}
							});
							World.getWorld().submit(new Tickable(6) {
								public void execute() {
									if(!player.isGfxing()) {
										player.graphics(lgd);
									}
									this.stop();
								}
							});
							this.stop();
						}
					});
				}
			}
		}
	}

	public void vengeance(Mob mob, Mob victim) {
		if(mob.isPlayer() && mob.getPlayer().hitCap) {
			return;
		}
		if (mob.getCombatState().getLastHit() > 0) {
			mob.hit((int) (mob.getCombatState().getLastHit() * 0.75));
			victim.setAttribute("vengeance", Boolean.FALSE);
			victim.getMask().setLastChatMessage(new ChatMessage(0, 0, "Taste vengeance!"));
		}
	}

}
