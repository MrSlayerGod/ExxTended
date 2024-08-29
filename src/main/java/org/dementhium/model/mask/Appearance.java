package org.dementhium.model.mask;


public class Appearance {

    private short npcType;
    private byte gender;
    private int[] look;
    private int[] colour;
    private boolean male;
    private boolean female;

    public Appearance() {
        this.setMale(true);
        this.setNpcType((short) -1);
        this.resetAppearence();
    }

    public void resetAppearence() {
        this.setLook(new int[7]);
        this.setColour(new int[5]);
        look[0] = 3; // Hair
        look[1] = 14; // Beard
        look[2] = 18; // Torso
        look[3] = 26; // Arms
        look[4] = 34; // Bracelets
        look[5] = 38; // Legs
        look[6] = 42; // Shoes
        colour[2] = 16;
        colour[1] = 16;
        for (int i = 0; i < 5; i++) {
            colour[2] = 16;
            colour[1] = 16;
            colour[0] = 7;
            gender = 0;
        }
    }

	public void female() {
    			this.setLook(new int[7]);
    			this.setColour(new int[5]);
                    		look[0] = 48; // Hair
                    		look[1] = 1000; // Beard
                    		look[2] = 57; // Torso
                    		look[3] = 64; // Arms
                    		look[4] = 68; // Bracelets
                    		look[5] = 77; // Legs
                    		look[6] = 80; // Shoes
        colour[2] = 16;
        colour[1] = 16;
        for (int i = 0; i < 5; i++) {
            colour[2] = 16;
            colour[1] = 16;
            colour[0] = 7;
            gender = 1;
        }
	}

    public void setNpcType(short npcType) {
        this.npcType = npcType;
    }

    public short getNpcType() {
        return npcType;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public byte getGender() {
        return gender;
    }

    public void setLook(int[] look) {
        this.look = look;
    }

    public int[] getLook() {
        return look;
    }

    public void setColour(int[] colour) {
        this.colour = colour;
    }

    public int[] getColour() {
        return colour;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean isMale() {
        return male;
    }
    public void setFemale(boolean female) {
    	this.female = female;
    }
    public boolean isFemale() {
    	return female;
    }

}
