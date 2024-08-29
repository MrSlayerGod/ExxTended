package org.dementhium.content.skills.magic;

import org.dementhium.model.Item;

public class Spell {

    private int autocastConfig;
    private int castAnim;
    private int castGfx;
    private int castHeight;
    private boolean drainsAtt;
    private boolean drainsHP;
    private int endGfx;
    private int endHeight;
    private int freezeTime;
    private int interfaceId;
    private int maxHit;
    private int maxPoison;
    private boolean multi;
    private int projectileId;
    private int reqLvl;
    private Item[] requiredRunes;
    private int spellId;
    private String spellName;
    private int staffId;
    private double xp;

    public boolean drainsHP() {
        return drainsHP;
    }

    public int getAutocastConfig() {
        return autocastConfig;
    }

    public int getCastAnim() {
        return castAnim;
    }

    public int getCastGfx() {
        return castGfx;
    }

    public int getCastHeight() {
        return castHeight;
    }

    public int getEndGfx() {
        return endGfx;
    }

    public int getEndHeight() {
        return endHeight;
    }

    public int getFreezeTime() {
        return freezeTime;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public int getMaxPoison() {
        return maxPoison;
    }

    public int getProjectileId() {
        return projectileId;
    }

    public int getReqLvl() {
        return reqLvl;
    }

    public Item[] getRequiredRunes() {
        return requiredRunes;
    }

    public int getSpellId() {
        return spellId;
    }

    public String getSpellName() {
        return spellName;
    }

    public int getStaffId() {
        return staffId;
    }

    public double getXp() {
        return xp;
    }

	public boolean isMulti() {
		return multi;
	}
	
	public boolean isDrainsAtt() {
		return drainsAtt;
	}

	public boolean isAncient() {
		String name = spellName.toLowerCase();
		return name.contains("ice") || name.contains("smoke") || name.contains("shadow") || name.contains("blood");
	}

}
