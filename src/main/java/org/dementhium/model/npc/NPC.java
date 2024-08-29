package org.dementhium.model.npc;

import java.io.*;
import java.util.*;

import org.dementhium.event.Tickable;
import org.dementhium.model.GroundItemManager;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.mask.ForceText;
import org.dementhium.model.mask.Hits;
import org.dementhium.model.mask.Hits.Hit;
import org.dementhium.model.npc.NPCDropLoader.Drop;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Equipment;
import org.dementhium.util.Misc;

public class NPC extends Mob {

	public static final int[] UNRESPAWNABLE_NPCS = {
		2746
	};

	public static final Random r = new Random();

	private String name;
	private int id;
	private int hp = 1000;

	private Queue<Hit> queuedHits;
	private Hits hits;
	private Location originalLoc;
	private boolean isDead;

	private NPCDefinition definition;

	private boolean doesWalk = false;

	private int faceDir = 1;

	public NPC(int id) {
		super();
		this.setId(id);
		this.setQueuedHits(new LinkedList<Hit>());
		this.setHits(new Hits());
		this.definition = NPCDefinition.forId(id);
		this.hp = definition.getHitpoints();
		getWalkingQueue();
	}

	public NPC(int id, Location location) {
		super();
		this.setId(id);
		this.setQueuedHits(new LinkedList<Hit>());
		this.setHits(new Hits());
		this.definition = NPCDefinition.forId(id);
		this.hp = definition.getHitpoints();
		setLocation(location);
		setOriginalLocation(location);
		getWalkingQueue();
	}

	public NPC(int id, int x, int y, int z) {
		super();
		this.setId(id);
		this.setQueuedHits(new LinkedList<Hit>());
		this.setHits(new Hits());
		this.definition = NPCDefinition.forId(id);
		this.hp = definition.getHitpoints();
		setOriginalLocation(Location.create(x, y, z));
		setLocation(originalLoc);
		getWalkingQueue();
	}
	
	public void tick() {
		if(this.getHp() <= 0) {
			sendDead();
			return;
		}
		if(r.nextBoolean() && r.nextBoolean() && r.nextBoolean() && getCombatState().getVictim() == null && doesWalk && !isDead()) {
			int randomX = r.nextInt(14);
			int randomY = r.nextInt(14);
			int moveX = originalLoc.getX() + (r.nextBoolean() ? -randomX : randomX), moveY = originalLoc.getY() + (r.nextBoolean() ? -randomY : randomX);
			int firstX = moveX - (getLocation().getRegionX() - 6) * 8;
			int firstY = moveY - (getLocation().getRegionY() - 6) * 8;
			getWalkingQueue().reset();
			getWalkingQueue().addClippedWalkingQueue(firstX, firstY);
		}
	}

	public void walkTo(int moveX, int moveY) {
		if(location.getX() == moveX && location.getY() == moveY) {
			return;
		}
		int firstX = moveX - (location.getRegionX() - 6) * 8;
		int firstY = moveY - (location.getRegionY() - 6) * 8;
		walkingQueue.reset();
		walkingQueue.addToWalkingQueue(firstX, firstY);
	}

	public void setOriginalLocation(Location location) {
		this.originalLoc = location;
	}

	private void setHits(Hits hits2) {
		hits = hits2;
	}

	public void setQueuedHits(Queue<Hit> queuedHits) {
		this.queuedHits = queuedHits;
	}

	public Queue<Hit> getQueuedHits() {
		return queuedHits;
	}

	@Override
	public void heal(int amount) {

	}

	public int getDeathDelay() {
		switch(getId()) {
		case 1265:
			return 2;
		}
		return 4;
	}

	public void processQueuedHits() {
		if(hits.getPrimaryHit() == null) {
			if(queuedHits.size() > 0) {
				Hit h = queuedHits.poll();
				this.hit(h.getDamage(), h.getType());
			}
		}
		if(hits.getSecondaryHit() == null) {
			if(queuedHits.size() > 0) {
				Hit h = queuedHits.poll();
				this.hit(h.getDamage(), h.getType());
			}
		}
	}

	public void hit(int damage, Hits.HitType type) {
		if(getHp() <= 0) {
			World.getWorld().submit(new Tickable(1) {
				public void execute() {
					sendDead();
					this.stop();
				}
			});
			return;
		}
		if(hits.getPrimaryHit() == null) {
			hits.setHit1(new Hit(damage, type));
			setHp(this.getHp() - damage);
		} else if(hits.getSecondaryHit() == null) {
			hits.setHit2(new Hit(damage, type));
			setHp(this.getHp() - damage);
		} else {
			queuedHits.add(new Hit(damage, type));
		}
		if(getHp() == 0) {
			World.getWorld().submit(new Tickable(1) {
				public void execute() {
					sendDead();
					this.stop();
				}
			});
		}
	}

	public void dropItem() {
		Random rand = new Random();
		try {
			Mob killer = getKiller();
			Player player = ((Player) killer);
			int ringId = player.getEquipment().get(Equipment.SLOT_RING) == null ? -1 : player.getEquipment().get(Equipment.SLOT_RING).getId();
			BufferedReader in = new BufferedReader(new FileReader("data/npcs/npcdrops.cfg"));
			String input;
			int on = 0;
			String[] splitEQL;
			String[] splitCOM;
			String[] splitDSH;
			String[] splitCLN;
			String[] splitSCL;
			while ((input = in.readLine()) != null) {
				splitEQL = null; splitEQL = null; splitDSH = null; splitCLN = null; splitSCL = null;
				if (!input.startsWith("/") && input.contains("=") && input.contains(",") && input.contains("-") && input.contains(":")) {
					try {
						splitEQL = input.split("=");
						if (Integer.parseInt(splitEQL[0]) == getId()) {
							splitSCL = splitEQL[1].split(";");
							int Wealth=0;
							if (ringId == 2572) {
								if (Misc.random(3) == 1) {
									Wealth=10;
								}
							}
							for (int i = Wealth; i < splitSCL.length; i++) {
								splitCOM = splitSCL[i].split(",");
								splitDSH = splitCOM[1].split("-");
								splitCLN = splitCOM[2].split(":");
								int item = Integer.parseInt(splitCOM[0]);
								int min = Integer.parseInt(splitDSH[0]);
								int max = Integer.parseInt(splitDSH[1]);
								int chance = Integer.parseInt(splitCLN[0]);
								int outOf = Integer.parseInt(splitCLN[1]);
								int amount = rand.nextInt((max - min)+1) + min; 
								int ifDrop = rand.nextInt(outOf)+1;
								if (ifDrop <= chance) {
									World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create((Player) killer, new Item(item, amount), getLocation()), false);
								}
							}
						}
					} catch (Exception e) {							
						System.out.println("Exception dropping item:\n"+e);
					}
					++on;
				}
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void sendDead() {
		if(isDead()) {
			return;
		}
		setDead(true);
		if(getCombatState().getLastAttacker() != null) {
			getCombatState().getLastAttacker().getCombatState().setLastAttacker(null);
			getCombatState().setLastAttacker(null);
		}
		resetTurnTo();
		animate(getDeathAnimation());
		for(int i : UNRESPAWNABLE_NPCS) {
			if(id == i) {
				World.getWorld().getNpcs().remove(this);
				return;
			}
		}
		World.getWorld().submit(new Tickable(getDeathDelay()) {
			@Override
			public void execute() {
				setHidden(true);
				dropItem();
				clearEnemyHits();
				this.stop();
			}
		});
		World.getWorld().submit(new Tickable(60) {
			@Override
			public void execute() {
				resetCombat();
				setHidden(false);
				setLocation(getOriginalLocation());
				setHp(getMaxHp());
				setDead(false);
				this.stop();
			}
		});
	}

	private int getDeathAnimation() {
		return definition.getDeathAnimation();
	}

	public Location getOriginalLocation() {
		return originalLoc;
	}

	public Hits getHits() {
		return hits;
	}

	@Override
	public void hit(int damage) {
		if(this.getHp() <= 0) {
			damage = 0;
		}
		if(damage > this.getHp())
			damage = this.getHp();
		if(damage == 0) {
			hit(damage, Hits.HitType.NO_DAMAGE);
		} else if(damage >= 100) {
			hit(damage, Hits.HitType.NORMAL_BIG_DAMAGE);
		}else {
			hit(damage, Hits.HitType.NORMAL_DAMAGE);
		}
	}

	@Override
	public void hit(Mob victim, int damage) {
		if (damage > getHp())
			damage = getHp();
		if (damage == 0) {
			hit(damage, Hits.HitType.NO_DAMAGE);
		} else if (damage >= 100) {
			hit(damage, Hits.HitType.NORMAL_BIG_DAMAGE);
		} else {
			hit(damage, Hits.HitType.NORMAL_DAMAGE);
		}
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getHp() {
		return hp;
	}

	public void forceText(String string) {
		this.getMask().setLastForceText(new ForceText(string));
	}

	public int getMaxHp() {
		return definition.getHitpoints();
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean dead) {
		this.isDead = dead;
	}

	@Override
	public FightType getFightType() {
		return FightType.MELEE;
	}

	@Override
	public int getAttackAnimation() {
		return definition.getAttackAnimation();
	}

	@Override
	public int getAttackDelay() {
		return definition.getAttackDelay();
	}

	@Override
	public int getDefenceAnimation() {
		return definition.getDefenceAnimation();
	}

	public CombatAction getCustomCombatAction() {
		return null;
	}

	public NPCDefinition getDefinition() {
		return definition;
	}
	
	@Override
	public boolean isNPC() {
		return true;
	}
	
	@Override
	public NPC getNpc() {
		return this;
	}

	@Override
	public boolean isAggressive() {
		return false;
	}

	public boolean isDoesWalk() {
		return doesWalk;
	}

	public void setDoesWalk(boolean doesWalk) {
		this.doesWalk = doesWalk;
	}

	public void setFaceDir(int faceDir) {
		this.faceDir = faceDir;
	}
	
	public int getFaceDir() {
		return faceDir;
	}
}
