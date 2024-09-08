package org.dementhium.content.skills.magic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.dementhium.action.Action;
import org.dementhium.event.Tickable;
import org.dementhium.identifiers.IdentifierManager;
import org.dementhium.io.XMLHandler;
import org.dementhium.model.Mob;
import org.dementhium.model.Item;
import org.dementhium.model.ProjectileManager;
import org.dementhium.model.World;
import org.dementhium.model.combat.Combat;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.map.Region;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Bonuses;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.skills.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.content.skills.Prayer;
import org.dementhium.util.Misc;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 */
public class MagicHandler {

	private static HashMap<Integer, Spell> modernSpells = new HashMap<Integer, Spell>();
	private static HashMap<Integer, Spell> ancientSpells = new HashMap<Integer, Spell>();
	private static Random r = new Random();

	public static void load() throws IOException {
		System.out.println("Loading spells...");
		List<Spell> spells = XMLHandler.fromXML("data/xml/spells.xml");
		for (Spell spell : spells) {
			if(spell.isAncient()) {
				MagicHandler.ancientSpells.put(spell.getSpellId(), spell);
			} else {
				MagicHandler.modernSpells.put(spell.getSpellId(), spell);
			}
		}
		System.out.println("Loaded " + (MagicHandler.modernSpells.size() +  MagicHandler.ancientSpells.size()) + " spells.");
	}

	public static Spell forId(int spellId, int book) {
		return book == 193 ? ancientSpells.get(spellId) : book == 192 ? modernSpells.get(spellId) : null;
	}

	public static boolean canCast(Player player, Mob victim, int spellId, int book){
		if(player.getSettings().getSpellBook() != book) {
			return false;
		}
		Spell spell = forId(spellId, book);

		if(spell != null) {
			if(victim != null) {
				if(victim.isNPC()) {
					if(victim.getNpc().getOriginalLocation().distance(player.getLocation()) > 14 && victim.getNpc().getLocation().distance(player.getLocation()) > victim.size()) {
						player.sendMessage("You need to get closer to do that.");
						return false;
					}
				}
				if(!Combat.canAttack(player, victim)) {
					return false;
				}
			}
			if (player.getSkills().getLevel(Skills.Magic) < spell.getReqLvl()) {
				ActionSender.sendMessage(player, "You need a magic level of " + spell.getReqLvl() + " to cast " + spell.getSpellName() + ".");
				return false;
			}
			if(!(player.getAttribute("spellQueued")  == null)) {
				return false;
			}
			for(Item item : spell.getRequiredRunes()) {
				if(player.getInventory().getContainer().contains(item)) {
					continue;
				}
				ActionSender.sendMessage(player, "You do not have enough " + item.getDefinition().getName().toLowerCase() + "s.");
				return false;
			}
			return true;
		} 
		ActionSender.sendMessage(player, "Spell " + spellId + " hasn't been added yet.");
		return false;
	}


	public static void handleAutocastButtons(Player p, int interfaceId, int button) {
		if(canCast(p, null, button, interfaceId)) {
			Spell spell = forId(button, interfaceId);
			Spell autoCast = (Spell) p.getAttribute("autoCastSpell", null);
			if(autoCast != null) {
				if(autoCast.getSpellName().equals(spell.getSpellName())) {
					ActionSender.sendMessage(p, "You are no longer autocasting.");
					p.getSettings().setCurrentWeaponInterface(p.getSettings().getLastWeaponInterface());
					ActionSender.sendConfig(p, 43, WeaponInterface.getConfig(p, p.getSettings().getCurrentWeaponInterface()));
					ActionSender.sendConfig(p, 108, -1);
					p.resetCombat();
					p.setAttribute("autoCastSpell", null);
					return;
				}
			}
			p.getSettings().setLastWeaponInterface(p.getSettings().getCurrentWeaponInterface());
			ActionSender.sendConfig(p, 43, 4);//set attack style config blank
			ActionSender.sendConfig(p, 108, 1);
			ActionSender.sendConfig(p, 108, spell.getAutocastConfig());
			p.setAttribute("autoCastSpell", spell);
			p.setAttribute("book", interfaceId);
			ActionSender.sendMessage(p, "You are now autocasting "+spell.getSpellName());
		}
	}

	public static void cast(final Player player, final Mob victim, final int bookId, final int spellId) {
		if(player == null || victim == null) {
			return;
		}
		if(canCast(player, victim, spellId, bookId)) {
			if(player.getAttribute("autoCastSpell") == null) {
				player.resetCombat();
			}
			if(player.getCombatState().getSpellDelay() != 0) {
				player.getActionManager().clearActions();
				player.setAttribute("spellQueued", Boolean.TRUE);
				player.registerAction(new Action(player.getCombatState().getSpellDelay()) {
					@Override
					public void execute() {
						if(player.getAttribute("spellQueued")  == null) {
							this.stop();
							return;
						}
						this.stop();
						player.removeAttribute("spellQueued");
						if(player.getCombatState().getSpellDelay() == 0 && player.getCombatState().getAttackDelay() == 0) {
							cast(player, victim, bookId, spellId);
						}
					}
				});
				return;
			}
			final Spell spell = forId(spellId, bookId);
			for(Item item : spell.getRequiredRunes()) {
				player.getInventory().getContainer().remove(item);
			}
			if(player.getWalkingQueue().isMoving()) {
				player.getWalkingQueue().reset();
			}
			player.getInventory().refresh();
			if(spell.getSpellId() == 86 && victim.isPlayer()) {
				victim.getPlayer().getTeleblock().setTeleblocked(true);
				victim.getPlayer().sendMessage("You have been teleblocked!");
			}
			player.getCombatState().setSpellDelay(6);
			player.getCombatState().setAttackDelay(4);
			if(spell.getCastAnim() > 0)
				player.animate(spell.getCastAnim());
			if(spell.getCastGfx() > 0)
				player.graphics(spell.getCastGfx(), spell.getCastHeight() << 16);
			if(spell.getProjectileId() > 0)
				ProjectileManager.sendDelayedProjectile(player, victim, spell.getProjectileId(), false);
			victim.getCombatState().setLastAttacker(player);
			player.turnTo(victim);
			World.getWorld().submit(new Tickable(spell.getProjectileId() != -1 ? 3 : 2) {
				@Override
				public void execute() {
					this.stop();
					if(spell.isMulti() && player.isMulti()) {
						ArrayList<Mob> toAttack = new ArrayList<Mob>();
						toAttack.add(victim);
						if(victim.isPlayer()) {
							for(Player other : Region.getLocalPlayers(victim.getLocation(), 1)) {
								if(other != player && other != victim && other.getPlayer().isMulti()) {
									toAttack.add(other);
								}
							}
						} else {
							for(NPC other : Region.getLocalNPCs(victim.getLocation(), 1)) {
								if(other != victim) {
									toAttack.add(other);
								}
							}
						}
						for(final Mob mob : toAttack) {
							int damage = playerDamage(player, mob, spell.getMaxHit()); 
							if(spell.getFreezeTime() > 0) {
								int gfx = spell.getEndGfx();
								if (spell.getSpellName().equals("Ice Barrage") && mob.getCombatState().isFrozen()) {
									gfx = 1677;
								}
								if(!mob.getCombatState().isFrozen() && damage > 0) {
									if(!(mob.isPlayer() && mob.getPlayer().hitCap)) {
										mob.getCombatState().setFrozenTime(spell.getFreezeTime());
										mob.getWalkingQueue().reset();
									}
									World.getWorld().submit(new Tickable(1) {
										public void execute() {
											if(mob.isPlayer()) {
												if(mob.getPlayer().hitCap) {
													this.stop();
												} else {
													mob.getCombatState().setFrozenTime(spell.getFreezeTime());
													mob.getWalkingQueue().reset();
												}
											}
											this.stop();
										}
									});
								}
								if(damage != 0) {
									mob.graphics(gfx, gfx == 1677 ? 100 << 16 : spell.getEndHeight());
									mob.hit(damage);
								} else {
									mob.graphics(85, 100 << 16);
								}
							} else {
								if(damage != 0) {
									mob.graphics(spell.getEndGfx(), spell.getEndHeight() << 16);
									mob.hit(damage);
								} else {
									mob.graphics(85, 100 << 16);
								}
							}
							mob.addEnemyHit(player, damage);
							if(mob.isPlayer() && player.isPlayer()) {
								if(!mob.getPlayer().getHeadIcons().isSkulled() || mob.getPlayer().getHeadIcons().isSkulled() && !mob.getPlayer().getHeadIcons().getSkulledOn().equalsIgnoreCase(player.getUsername())) {
									player.getHeadIcons().setSkulled(true);
									player.getHeadIcons().setSkulledOn(mob.getPlayer().getUsername());
								}
							}
						}
					} else {
						int damage = playerDamage(player, victim, spell.getMaxHit());
						int gfx = spell.getEndGfx();
						if(spell.getFreezeTime() > 0) {
							if (spell.getSpellName().equals("Ice Barrage") && victim.getCombatState().isFrozen()) {
								gfx = 1677;
							}
							if(!victim.getCombatState().isFrozen() && damage > 0) {
								if(!(victim.isPlayer() && victim.getPlayer().hitCap)) {
									victim.getCombatState().setFrozenTime(spell.getFreezeTime());
								}												victim.getWalkingQueue().reset();
								World.getWorld().submit(new Tickable(1) {
									public void execute() {
										if(!(victim.isPlayer() && victim.getPlayer().hitCap)) {
											victim.getCombatState().setFrozenTime(spell.getFreezeTime());
											victim.getWalkingQueue().reset();
										}
										this.stop();
									}
								});
							}
						}
						if(damage != 0) {
							victim.graphics(gfx, gfx == 1677 ? 100 << 16 : spell.getEndHeight() << 16);
							victim.hit(damage);
						} else {
							victim.graphics(85, 100 << 16);
						}
						player.getCombatState().setLastHit(damage);
						IdentifierManager.get("combat_after_effect").identify(player, victim, null, FightType.MAGIC);
						victim.addEnemyHit(player, damage);
						if(victim.isPlayer() && player.isPlayer()) {
							if(!victim.getPlayer().getHeadIcons().isSkulled() || victim.getPlayer().getHeadIcons().isSkulled() && !victim.getPlayer().getHeadIcons().getSkulledOn().equalsIgnoreCase(player.getUsername())) {
								player.getHeadIcons().setSkulled(true);
								player.getHeadIcons().setSkulledOn(victim.getPlayer().getUsername());
							}
						}
					}
				}
			});
		}
	} 


	public static int playerDamage(Player player, Mob opp, int damage) {
		int def = (int) (opp.isPlayer() ? (opp.getPlayer().getBonuses().getBonus(Bonuses.MAGIC_DEFENCE) * 1.20) : opp.getNpc().getDefinition().getDefenceBonus());

		int atk = player.getSkills().getLevel(Skills.Magic);

		int atkBonus = player.getBonuses().getBonus(Bonuses.MAGIC_ATTACK);
			
		if(atkBonus < 0) {
			atkBonus = (atkBonus - 1) * 5;
		}
		atk = atk + atkBonus;
		if (player.getPrayer().usingPrayer(0, Prayer.MYSTIC_WILL)) {
			atk *= 1.05;
		} else if (player.getPrayer().usingPrayer(0, Prayer.MYSTIC_LORE)) {
			atk *= 1.10;
		} else if (player.getPrayer().usingPrayer(0, Prayer.MYSTIC_MIGHT)) {
			atk *= 1.15;
		}
		if(player.getEquipment().voidSet(3)) {
			atk *= 1.20;
		}
		if(player.getCombatState().getLastAttacker() != null && player.getCombatState().getLastAttacker().isPlayer()) {
			if(player.getCombatState().getLastAttacker().getPlayer().getPrayer().usingPrayer(1, Prayer.LEECH_MAGIC)) {
				atk -= atk / 10;
			}
		}
		if(player.getPrayer().usingPrayer(1, Prayer.LEECH_MAGIC)) {
			atk += atk * 0.05;
		}
		if(atk < 1) atk = 1;
		if(def < 1) def = 1;
		if(Misc.random(def) > Misc.random(atk)) {
			return 0;
		}
		return (int) (r.nextDouble() * (damage * 10));
	}
}
