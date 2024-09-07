package org.dementhium.model.combat;

import org.dementhium.model.Mob;

/**
 * 
 * @author 'Mystic Flow
 *
 */
public final class CombatState {
	
	private Mob victim;
	
	private int attackDelay, spellDelay, lastHit, frozenTime;
	
	private long freezeTimer;
	
	private Mob lastAttacker;
	
	private long lastAttacked;
	
	public void setAttackDelay(int delay) {
		this.attackDelay = delay;
	}

	public void setSpellDelay(int spellDelay) {
		this.spellDelay = spellDelay;
	}
	
	public int getAttackDelay() {
		return attackDelay;
	}

	public int getSpellDelay() {
		return spellDelay;
	}

	public void setVictim(Mob victim) {
		this.victim = victim;
	}
	
	public Mob getVictim() {
		return victim;
	}

	public void setLastHit(int lastHit) {
		this.lastHit = lastHit;
	}

	public int getLastHit() {
		return lastHit;
	}

	public void setFrozenTime(int frozenTime) {
		this.frozenTime = frozenTime;
		this.freezeTimer = System.currentTimeMillis();
	}

	public Mob getLastAttacker() {
		if(System.currentTimeMillis() - lastAttacked > 5000) {
			lastAttacker = null;
		}
		return lastAttacker;
	}

	public void setLastAttacker(Mob lastAttacker) {
		this.lastAttacker = lastAttacker;
		this.lastAttacked = System.currentTimeMillis();
	}

	public void reset() {
		if (lastAttacker != null) {
			getLastAttacker().getCombatState().setLastAttacker(null);
		}
		setLastAttacker(null);
	}

	public boolean isFrozen() {
		return System.currentTimeMillis() - freezeTimer < frozenTime;
	}
	
}
