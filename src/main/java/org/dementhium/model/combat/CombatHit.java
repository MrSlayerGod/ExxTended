package org.dementhium.model.combat;

import org.dementhium.model.Mob;

/**
 * 
 * @author 'Mystic Flow
 *
 */
public class CombatHit {
	
	private final Mob mob;
	
	private final Mob victim;
	
	private final int damage;
	
	private final int ticks;
	
	private final String identifier;
	
	private final Object[] identification;
	
	public CombatHit(Mob mob, Mob victim, int damage, int ticks) {
		this(mob, victim, damage, ticks, null, null);
	}
	
	public CombatHit(Mob mob, Mob victim, int damage, int ticks, String identifier, Object[] identification) {
		this.mob = mob;
		this.victim = victim;
		this.damage = damage;
		this.ticks = ticks;
		this.identifier = identifier;
		this.identification = identification;
	}

	public String getIdentifier() {
		return identifier;
	}

	public Object[] getIdentification() {
		return identification;
	}

	public Mob getEntity() {
		return mob;
	}

	public Mob getVictim() {
		return victim;
	}

	public int getDamage() {
		return damage;
	}

	public int getTicks() {
		return ticks;
	}
	
}
