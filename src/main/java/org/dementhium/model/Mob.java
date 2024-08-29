package org.dementhium.model;

import java.util.HashMap;
import java.util.Map;

import org.dementhium.action.Action;
import org.dementhium.action.ActionManager;
import org.dementhium.content.areas.CoordinateEvent;
import org.dementhium.event.Tickable;
import org.dementhium.model.combat.CombatState;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.map.DoublePathFinder;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphics;
import org.dementhium.model.mask.Mask;
import org.dementhium.model.mask.Sprites;
import org.dementhium.model.npc.NPC;

public abstract class Mob extends Entity {

	public static final Location DEFAULT = Location.create(3162, 3484, 0);

	private int index;
	private boolean hidden;
	private boolean teleporting = false;

	private final Map<String, Object> attributes = new HashMap<String, Object>();
	private final CombatState combatState = new CombatState();
	private final ActionManager actionManager = new ActionManager(this);
	private final Sprites sprite = new Sprites();

	protected final Mask mask = new Mask(this);

	protected WalkingQueue walkingQueue;
	private CoordinateEvent coordinateEvent;

	private int[] forceWalk;

	private final Map<Mob, Integer> enemyHits = new HashMap<Mob, Integer>();

	public Mob() {
		setLocation(DEFAULT);
	}

	public boolean isMulti() {
		if (!World.getWorld().getAreaManager().getAreaByName("Edgeville").contains(getLocation()) && World.getWorld().getAreaManager().getAreaByName("Wilderness").contains(getLocation())) {
			return true;
		} else if (World.getWorld().getAreaManager().getAreaByName("Nex").contains(getLocation())) {
			return true;
		}
		return false;
	}

	public boolean inWilderness() {
		if (World.getWorld().getAreaManager().getAreaByName("Wilderness").contains(getLocation())) {
			return true;
		}
		return false;
	}


	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void resetCombat() {
		combatState.setVictim(null);
	}

	public int getClientIndex() {
		if (isPlayer()) {
			return this.index + 32768;
		} else {
			return this.index;
		}
	}

	public boolean isDead() {
		if (isPlayer()) {
			return getPlayer().getSkills().isDead();
		} else {
			return getNpc().isDead();
		}
	}

	public abstract void heal(int amount);

	public abstract void hit(Mob victim, int damage);

	public abstract void hit(int damage);

	public void turnTemporarilyTo(Mob mob) {
		mask.setInteractingEntity(mob);
		World.getWorld().submit(new Tickable(2) {
			@Override
			public void execute() {
				mask.setInteractingEntity(null);
				this.stop();
			}
		});
	}

	public void turnTo(Mob mob) {
		mask.setInteractingEntity(mob);
	}

	public void resetTurnTo() {
		mask.setInteractingEntity(null);
	}

	public void EntityLoad() {
		this.setHidden(false);
		this.setLocation(getLocation());
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isAutoRetaliating() {
		if (this instanceof NPC)
			return true;
		else
			return getPlayer().getSettings().isAutoRetaliate();
	}

	public void setAttribute(String string, Object object) {
		attributes.put(string, object);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String string) {
		return (T) attributes.get(string);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String string, T fail) {
		T object = (T) attributes.get(string);
		if (object != null) {
			return object;
		}
		return fail;
	}

	public void removeAttribute(String string) {
		attributes.remove(string);
	}

	public CombatState getCombatState() {
		return combatState;
	}

	public Mask getMask() {
		return mask;
	}

	public boolean usingSpecial() {
		if(isPlayer()) {
			return getPlayer().getSettings().isUsingSpecial();
		}
		return false;
	}

	public void teleport(int x, int y, int z) {
		if(isPlayer()) {
			getPlayer().getRegion().teleport(x, y, z);
		} else {
			walkingQueue.reset();
			setLocation(Location.create(x, y, z));
			setTeleporting(true);
		}
	}
	public void teleport(Location loc) {
		teleport(loc.getX(), loc.getY(), loc.getZ());
	}

	public boolean isTeleporting() {
		return teleporting;
	}

	public void setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
	}

	public Sprites getDir() {
		return sprite;
	}

	public void animate(int... args) {
		switch(args.length) {
		case 1:
			mask.setLastAnimation(Animation.create(args[0]));
			break;
		case 2:
			mask.setLastAnimation(Animation.create(args[0], args[1]));
			break;
		default:
			throw new IllegalArgumentException("Animation arguments can't be greater then 2");
		}

	}

	public void graphics(int... args) {
		switch(args.length) {
		case 1:
			mask.setLastGraphics(Graphics.create(args[0]));
			break;
		case 2:
			mask.setLastGraphics(Graphics.create(args[0], args[1]));
			break;
		case 3:
			mask.setLastGraphics(Graphics.create(args[0], args[1], args[2]));
			break;
		default:
			throw new IllegalArgumentException("Graphic arguments can't be greater then 3");
		}
	}

	public void graphics(Graphics graphics) {
		mask.setLastGraphics(graphics);
	}

	public void animate(Animation animation) {
		mask.setLastAnimation(animation);
	}

	public boolean isAnimating() {
		return mask.getLastAnimation() != null;
	}

	public boolean isGfxing() {
		return mask.getLastGraphics() != null;
	}

	public boolean destroyed() {
		if(isPlayer()) {
			return !World.getWorld().getPlayers().contains(this);
		} 
		return !World.getWorld().getNpcs().contains(this);
	}

	public void addPoint(int x, int y) {
		int firstX = x - (getLocation().getRegionX() - 6) * 8;
		int firstY = y - (getLocation().getRegionY() - 6) * 8;
		walkingQueue.addToWalkingQueue(firstX, firstY);
	}

	public void requestWalk(int x, int y) {
		int firstX = x - (getLocation().getRegionX() - 6) * 8;
		int firstY = y - (getLocation().getRegionY() - 6) * 8;
		walkingQueue.reset();
		walkingQueue.addToWalkingQueue(firstX, firstY);
	}

	public void requestWalkPath(int x, int y) {
		World.getWorld().doPath(DoublePathFinder.INSTANCE, this, x, y, false);
	}

	public ActionManager getActionManager() {
		return actionManager;
	}

	public void registerAction(Action action) {
		actionManager.appendAction(action);
	}

	public abstract FightType getFightType();

	public abstract int getDefenceAnimation();

	public abstract int getAttackAnimation();

	public abstract int getAttackDelay();

	public abstract void forceText(String string);

	public int size() {
		if(isPlayer()) {
			return 1;
		} 
		return getNpc().getDefinition().getSize();
	}

	public void setCoordinateEvent(CoordinateEvent coordinateEvent) {
		if(this.coordinateEvent != null) {
			return;
		}
		this.coordinateEvent = coordinateEvent;
		World.getWorld().submitCoordinateEvent(this, coordinateEvent);
	}

	public void stopCoordinateEvent() {
		this.coordinateEvent = null;
	}

	public CoordinateEvent getCoordinateEvent() {
		return coordinateEvent;
	}

	public WalkingQueue getWalkingQueue() {
		if(walkingQueue == null) {
			walkingQueue = new WalkingQueue(this);
		}
		return walkingQueue;
	}

	public int[] getForceWalk() {
		return forceWalk;
	}

	public void forceMovement(final Animation animation, final int[] forceMovement, int ticks, final boolean removeAttribute) {
		World.getWorld().submit(new Tickable(ticks) {
			@Override
			public void execute() {
				animate(animation);
				setForceWalk(forceMovement, removeAttribute);
				mask.setForceMovementUpdate(true);
				this.stop();
			}
		});
	}

	public void setForceWalk(final int[] forceWalk, final boolean removeAttribute) {
		this.forceWalk = forceWalk;
		if(forceWalk.length > 0) {
			World.getWorld().submit(new Tickable(forceWalk[5]) {
				@Override
				public void execute() {
					teleport(Location.create(forceWalk[0], forceWalk[1], 0));
					if(removeAttribute) {
						removeAttribute("busy");
					}
					this.stop();
				}
			});
		}
	}

	public void addEnemyHit(Mob enemy, int damage) {
		if(damage > 0) {
			if(!enemyHits.containsKey(enemy)) {
				enemyHits.put(enemy, damage);
			} else {
				enemyHits.put(enemy, enemyHits.get(enemy) + damage);
			}
		}
	}

	public void clearEnemyHits() {
		enemyHits.clear();
	}
	
	public Mob getKiller() {
		Mob killer = null;
		int mostDamage = 0;
		for(Map.Entry<Mob, Integer> entry : enemyHits.entrySet()) {
			if(entry.getValue() > mostDamage) {
				killer = entry.getKey();
				mostDamage = entry.getValue();
			}
		}
		return killer;
	}

	public abstract boolean isAggressive();

}
