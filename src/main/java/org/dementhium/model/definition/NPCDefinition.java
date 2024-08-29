package org.dementhium.model.definition;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dementhium.io.XMLHandler;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 *
 */
public class NPCDefinition {

	private static final Map<Integer, NPCDefinition> definitions = new HashMap<Integer, NPCDefinition>();

	public static void init() throws IOException {
		System.out.println("Loading npc definitions...");

		List<NPCDefinition> definitions = XMLHandler.fromXML("data/npcs/npcDefinitions.xml");

		for(NPCDefinition def : definitions) {
			NPCDefinition.definitions.put(def.id, def);
		}
		loadMiscData();

		System.out.println("Loaded " + definitions.size() + " npc definitions.");
	}

	public static NPCDefinition forId(int id) {
		NPCDefinition def = definitions.get(id);
		if(def == null) {
			def = new NPCDefinition();
			def.id = id;
			definitions.put(id, def);
		}
		return def;
	}

	private static void loadMiscData() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("data/npcs/npc_renderAnims.txt"));
			String name;
			while ((name = in.readLine()) != null) {
				String[] array = name.split(",");
				int itemId = Integer.parseInt(array[0]);
				int renderId = Integer.parseInt(array[1]);
				NPCDefinition def = definitions.get(itemId);
				if(def == null) {
					def = new NPCDefinition();
					def.id = itemId;
					definitions.put(itemId, def);
				}
				def.renderAnim = renderId;
			}
			in.close();
			in = new BufferedReader(new FileReader("data/npcs/npc_sizes.txt"));
			while ((name = in.readLine()) != null) {
				String[] array = name.split(",");
				int npcId = Integer.parseInt(array[0]);
				int size = Integer.parseInt(array[1]);
				NPCDefinition def = definitions.get(npcId);
				if(def == null) {
					def = new NPCDefinition();
					def.id = npcId;
					definitions.put(npcId, def);
				}
				def.size = size;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int id, attackAnimation = 423, defenceAnimation = 1156, attackDelay = 6, hitpoints = 1000, deathAnimation = 9055;

	private int attackBonus = 5, defenceBonus = 5, maximumMeleeHit = 1, renderAnim = -1, size = 1;

	public int getId() {
		return id;
	}

	public int getAttackAnimation() {
		return attackAnimation;
	}

	public int getDefenceAnimation() {
		return defenceAnimation;
	}

	public int getAttackDelay() {
		return attackDelay;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public int getAttackBonus() {
		return attackBonus;
	}

	public int getDefenceBonus() {
		return defenceBonus;
	}

	public int getMaximumMeleeHit() {
		return maximumMeleeHit * 10;
	}

	public int getRenderAnim() {
		return renderAnim == 0 ? -1 : renderAnim;
	}

	public int getDeathAnimation() {
		return deathAnimation;
	}

	public int getSize() {
		return size;
	}

	public String getName() {
		return "Man";
	}

}
