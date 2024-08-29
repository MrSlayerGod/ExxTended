package org.dementhium.model.definition;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import org.dementhium.model.player.Equipment;
import org.dementhium.util.BufferUtils;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class ItemDefinition {

	public static int MAX_SIZE = 20175;
	private static ItemDefinition[] definitions;

	public static void init() throws IOException {
		System.out.println("Loading item definitions...");
		FileChannel channel = new RandomAccessFile("data/item/itemDefinitions.bin", "r").getChannel();

		ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());

		int length = buffer.getShort();

		definitions = new ItemDefinition[MAX_SIZE];

		for (int i = 0; i < length; i++) {
			int id = buffer.getShort();
			if (id == -1 || id == 11284) {
				continue;
			}
			int equipId = buffer.getShort();
			int renderEmote = buffer.getShort();
			String name = BufferUtils.readRS2String(buffer);
			boolean extraDefinition = buffer.get() == 1;
			int[] bonus = new int[15];
			boolean tradeable = true;
			int attackSpeed = 5;
			int equipmentSlot = -1;
			String examine = "";
			double weight = 0;
			int highAlchPrice = 0, lowAlchPrice = 0, storePrice = 0, exchangePrice = 0;
			if (extraDefinition) {
				examine = BufferUtils.readRS2String(buffer);
				weight = buffer.getDouble();
				buffer.get();
				tradeable = buffer.get() == 1;
				attackSpeed = buffer.get();
				if (buffer.get() == 1) {
					for (int x = 0; x < 15; x++) {
						bonus[x] = buffer.getShort();
					}
				}
				highAlchPrice = buffer.getInt();
				lowAlchPrice = buffer.getInt();
				storePrice = buffer.getInt();
				exchangePrice = buffer.getInt();
			} 
			if (isHardcoded(id)) {
				Object[] data = hardcode(id, examine, weight, equipmentSlot, attackSpeed, bonus);
				examine = (String) data[0];
				weight = (Double) data[1];
				equipmentSlot = (Integer) data[2];
				attackSpeed = (Integer) data[3];
				bonus = (int[]) data[4];
			}
			boolean stackable = buffer.get() == 1;
			boolean noted = buffer.get() == 1;
			boolean hasReq = buffer.get() == 1;
			ArrayList<Integer> skillRequirementId = null;
			ArrayList<Integer> skillRequirementLvl = null;
			if (hasReq) {
				skillRequirementId = new ArrayList<Integer>();
				skillRequirementLvl = new ArrayList<Integer>();
				int size = buffer.get();
				for (int j = 0; j < size; j++) {
					skillRequirementId.add((int) buffer.get());
					skillRequirementLvl.add((int) buffer.get());
				}
			}
			if (id == 11283) {
				id = 11284;
			}
			definitions[id] = new ItemDefinition(id, name, examine, equipId, renderEmote, bonus, stackable, noted, tradeable, skillRequirementId, skillRequirementLvl, weight, highAlchPrice, lowAlchPrice, storePrice, exchangePrice, attackSpeed, equipmentSlot);
			definitions[id].setExtraDefinitions(extraDefinition);
		}
		loadMiscData();
		System.out.println("Loaded " + definitions.length + " item definitions.");
		channel.close();
		channel = null;
	}

	private static void loadMiscData() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("data/item/cache_info.txt"));
			String name;
			while ((name = in.readLine()) != null) {
				String[] array = name.split(",");
				int itemId = Integer.parseInt(array[0]);
				int renderId = Integer.parseInt(array[1]);
				int equipId = Integer.parseInt(array[2]);
				if (definitions[itemId] == null) {
					definitions[itemId] = new ItemDefinition(itemId, "", "", -1, -1, new int[15], false, false, true, null, null, 0.0, 0, 0, 0, 0, 0, 0);
				}
				definitions[itemId].renderId = renderId;
				definitions[itemId].equipId = equipId;
			}
			in.close();
			in = new BufferedReader(new FileReader("data/item/equipment.txt"));
			while ((name = in.readLine()) != null) {
				String[] array = name.split(":");
				int itemId = Integer.parseInt(array[0]);
				int equipmentSlot = Integer.parseInt(array[1]);
				definitions[itemId].equipmentSlot = equipmentSlot;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ItemDefinition forId(int id) {
		ItemDefinition def = definitions[id];
		if(def == null) {
			definitions[id] = new ItemDefinition(id, "", "", -1, -1, new int[15], false, false, true, null, null, 0.0, 0, 0, 0, 0, 0, 0);
		}
		return definitions[id];
	}

	private static Object[] hardcode(int id, String examine, double weight, int equipmentSlot, int attackSpeed, int[] bonus) {
		boolean degrades = false;
		switch (id) {
		case 13887: // vesta chain
			weight = 6.0;
			degrades = true;
			equipmentSlot = Equipment.SLOT_CHEST;
			bonus = new int[]{5, 7, 7, -15, 0, 120, 131, 145, -3, 140, 60, 6, 0, 0, 0};
			break;
		case 13893: // vesta plateskirt
			weight = 8.0;
			degrades = true;
			equipmentSlot = Equipment.SLOT_LEGS;
			bonus = new int[]{3, 5, 5, -17, -4, 86, 100, 112, -2, 118, 30, 3, 0, 0, 0};
			break;
		case 13858: // zuriels robe top
			weight = 4.0;
			degrades = true;
			equipmentSlot = Equipment.SLOT_CHEST;
			bonus = new int[]{0, 0, 0, 35, -10, 63, 45, 74, 35, 0, 60, 0, 0, 0, 0};
			break;
		case 13864: // zuriels hood
			weight = 0.0;
			degrades = true;
			equipmentSlot = Equipment.SLOT_HAT;
			bonus = new int[]{0, 0, 0, 8, -2, 20, 16, 22, 8, 0, 15, 0, 0, 0, 0};
			break;
		case 13861: // zuriels robe bottom
			weight = 5.0;
			degrades = true;
			equipmentSlot = Equipment.SLOT_LEGS;
			bonus = new int[]{0, 0, 0, 25, -7, 38, 35, 44, 24, 0, 30, 0, 0, 0, 0};
			break;
		case 13879: // morrigan's javelin
			weight = 0.0;
			degrades = true;
			equipmentSlot = Equipment.SLOT_WEAPON;
			bonus = new int[]{0, 0, 0, 0, 105, 0, 0, 0, 0, 0, 0, 0, 145, 0, 0};
			break;
		case 13880: // morrigan's javelin
			weight = 0.0;
			degrades = true;
			equipmentSlot = Equipment.SLOT_WEAPON;
			bonus = new int[]{0, 0, 0, 0, 105, 0, 0, 0, 0, 0, 0, 0, 145, 0, 0};
			break;
		case 13881: // morrigan's javelin
			weight = 0.0;
			degrades = true;
			equipmentSlot = Equipment.SLOT_WEAPON;
			bonus = new int[]{0, 0, 0, 0, 105, 0, 0, 0, 0, 0, 0, 0, 145, 0, 0};
			break;
		case 13882: // morrigan's javelin
			weight = 0.0;
			degrades = true;
			equipmentSlot = Equipment.SLOT_WEAPON;
			bonus = new int[]{0, 0, 0, 0, 105, 0, 0, 0, 0, 0, 0, 0, 145, 0, 0};
			break;
		case 18353: // chaotic maul
			weight = 0.0;
			degrades = false;
			equipmentSlot = Equipment.SLOT_WEAPON;
			bonus = new int[]{-4, -4, 135, 0, 0, 0, 0, 0, 0, 0, 0, 135, 0, 0, 0};
			break;
		}
		if (degrades) {
			examine = "This item degrades in combat, and will turn to dust.";
		}
		return new Object[]{examine, weight, equipmentSlot, attackSpeed, bonus};
	}

	public static boolean isHardcoded(int id) {//add the item id here
		switch (id) {
		case 13887:
		case 13893:
		case 13858:
		case 13861:
		case 13864:
		case 20072:
		case 13879:
		case 13880:
		case 13881:
		case 13882:
		case 18353:
			return true;
		}
		return false;
	}

	public static void clear() {
		definitions = new ItemDefinition[MAX_SIZE];
	}

	private String name;
	private String examine;
	private int id;
	private int equipId;
	private int renderId;
	private int[] bonus;
	private boolean stackable;
	private boolean noted;
	private boolean tradeable;
	private List<Integer> skillRequirementId;
	private List<Integer> skillRequirementLvl;
	private double weight;
	private boolean members;
	private int highAlchPrice, lowAlchPrice, storePrice, exchangePrice, attackSpeed, equipmentSlot;

	private boolean extraDefinitions;

	public ItemDefinition(int id, String name, String examine, int equipId, int renderId, int[] bonus, boolean stackable, boolean noted, boolean tradeable, List<Integer> skillRequirementId, List<Integer> skillRequirementLvl, double weight, int highAlchPrice, int lowAlchPrice, int storePrice, int exchangePrice, int attackSpeed, int equipmentSlot) {
		this.id = id;
		this.name = name;
		if (examine.length() == 0 || examine == null) {
			if(name.length() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("It's a");
				char c = name.charAt(0);
				if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
					sb.append("n");
				}
				sb.append(" ");
				sb.append(name);
				this.examine = sb.toString();
			}
		} else {
			this.examine = examine;
		}
		this.equipId = equipId;
		this.renderId = renderId;
		this.bonus = bonus;
		this.stackable = stackable;
		this.noted = noted;
		this.tradeable = tradeable;
		this.skillRequirementId = skillRequirementId;
		this.skillRequirementLvl = skillRequirementLvl;
		this.weight = weight;
		this.highAlchPrice = highAlchPrice;
		this.lowAlchPrice = lowAlchPrice;
		this.storePrice = storePrice;
		this.exchangePrice = exchangePrice;
		this.attackSpeed = attackSpeed;
		this.equipmentSlot = equipmentSlot;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getEquipId() {
		return equipId;
	}

	public int getRenderId() {
		return renderId;
	}

	public int[] getBonus() {
		return bonus;
	}

	public boolean isStackable() {
		return stackable;
	}

	public boolean isNoted() {
		return noted;
	}

	public List<Integer> getSkillRequirementId() {
		return skillRequirementId;
	}

	public List<Integer> getSkillRequirementLvl() {
		return skillRequirementLvl;
	}

	public String getExamine() {
		return examine;
	}

	public boolean isTradeable() {
		return tradeable;
	}

	public double getWeight() {
		return weight;
	}

	public int getHighAlchPrice() {
		return highAlchPrice;
	}

	public int getLowAlchPrice() {
		return lowAlchPrice;
	}

	public int getStorePrice() {
		return storePrice;
	}

	public int getExchangePrice() {
		return exchangePrice;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

	public int getEquipmentSlot() {
		return equipmentSlot;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExamine(String examine) {
		this.examine = examine;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public void setRenderId(int renderId) {
		this.renderId = renderId;
	}

	public void setBonus(int[] bonus) {
		this.bonus = bonus;
	}

	public void setBonusAtIndex(int index, int value) {
		this.bonus[index] = value;
	}

	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}

	public void setNoted(boolean noted) {
		this.noted = noted;
	}

	public void setTradeable(boolean tradeable) {
		this.tradeable = tradeable;
	}

	public void setSkillRequirementId(List<Integer> skillRequirementId) {
		this.skillRequirementId = skillRequirementId;
	}

	public void setSkillRequirementLvl(List<Integer> skillRequirementLvl) {
		this.skillRequirementLvl = skillRequirementLvl;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setHighAlchPrice(int highAlchPrice) {
		this.highAlchPrice = highAlchPrice;
	}

	public void setLowAlchPrice(int lowAlchPrice) {
		this.lowAlchPrice = lowAlchPrice;
	}

	public void setStorePrice(int storePrice) {
		this.storePrice = storePrice;
	}

	public void setExchangePrice(int exchangePrice) {
		this.exchangePrice = exchangePrice;
	}

	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public void setEquipmentSlot(int equipmentSlot) {
		this.equipmentSlot = equipmentSlot;
	}

	public void setMembers(boolean members) {
		this.members = members;
	}

	public boolean isMembers() {
		return members;
	}

	public void setExtraDefinitions(boolean extraDefinition) {
		this.extraDefinitions = extraDefinition;
	}

	public boolean isExtraDefinitions() {
		return extraDefinitions;
	}

}
