package org.dementhium.model.player;

import org.dementhium.content.clans.Clan;
import org.dementhium.model.combat.Combat.CombatStyle;
import org.dementhium.model.combat.Combat.CombatType;
import org.dementhium.model.definition.WeaponInterface;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Settings {
	
	private Clan currentClan;
	
	private int spellBook = 192;
	private int privateTextColor;

	private boolean autoRetaliate;
	private int specialAmount = 1000;
	private boolean usingSpecial = false;
	
	private CombatStyle combatStyle;
    private CombatType combatType;
    private WeaponInterface currentWeaponInterface;
    private WeaponInterface lastWeaponInterface;
    
	public void setCurrentClan(Clan currentClan) {
		this.currentClan = currentClan;
	}

	public Clan getCurrentClan() {
		return currentClan;
	}

	public void setSpellBook(int spellBook) {
		this.spellBook = spellBook;
	}

	public int getSpellBook() {
		return spellBook;
	}

	public void setUsingSpecial(boolean usingSpecial) {
		this.usingSpecial = usingSpecial;
	}

	public boolean isUsingSpecial() {
		return usingSpecial;
	}

	public boolean isAutoRetaliate() {
		return autoRetaliate;
	}

	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}

	public int getSpecialAmount() {
		return specialAmount;
	}

	public void setSpecialAmount(int specialAmount) {
		this.specialAmount = specialAmount;
	}
	
	public void setCombatStyle(CombatStyle combatStyle) {
		this.combatStyle = combatStyle;
	}

	public void setCombatType(CombatType combatType) {
		this.combatType = combatType;
	}

	public CombatStyle getCombatStyle() {
		return combatStyle;
	}

	public CombatType getCombatType() {
		return combatType;
	}

	public void setLastWeaponInterface(WeaponInterface lastWeaponInterface) {
		this.lastWeaponInterface = lastWeaponInterface;
	}

	public WeaponInterface getLastWeaponInterface() {
		return lastWeaponInterface;
	}

	public void setCurrentWeaponInterface(WeaponInterface currentWeaponInterface) {
		this.currentWeaponInterface = currentWeaponInterface;
	}

	public WeaponInterface getCurrentWeaponInterface() {
		return currentWeaponInterface;
	}

	public void setPrivateTextColor(int privateTextColor) {
		this.privateTextColor = privateTextColor;
	}

	public int getPrivateTextColor() {
		return privateTextColor;
	}
}
