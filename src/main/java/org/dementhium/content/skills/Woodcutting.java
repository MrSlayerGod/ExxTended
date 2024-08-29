package org.dementhium.content.skills;

import java.util.HashMap;
import java.util.Map;

import org.dementhium.action.HarvestingAction;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.util.Misc;
/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * 
 * @credits Scu11
 */
public final class Woodcutting extends HarvestingAction {

	public static enum Hatchet implements HarvestTool {

		DRAGON(6739, 61, Animation.create(2846)),
		RUNE(1359, 41, Animation.create(867)),
		ADAMANT(1357, 31, Animation.create(869)),
		MITHRIL(1355, 21, Animation.create(871)),
		BLACK(1361, 6, Animation.create(873)),
		STEEL(1353, 6, Animation.create(875)),
		IRON(1349, 1, Animation.create(877)),
		BRONZE(1351, 1, Animation.create(879));

		private int id;
		private int level;
		private Animation animation;

		private static final Map<Integer, Hatchet> hatchets = new HashMap<Integer, Hatchet>();

		static {
			for(Hatchet hatchet : Hatchet.values()) {
				hatchets.put(hatchet.id, hatchet);
			}
		}

		private Hatchet(int id, int level, Animation animation) {
			this.id = id;
			this.level = level;
			this.animation = animation;
		}

		@Override
		public Animation getAnimation() {
			return animation;
		}
		
		@Override
		public int getRequiredLevel() {
			return level;
		}
	}

	@SuppressWarnings("unused")
	public static enum Tree implements HarvestObject {

		NORMAL(1511, 1, 50, 50, 1, new int[] { 1276, 1277, 1278, 1279, 1280, 1282,
				1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318,
				1319, 1330, 1331, 1332, 1365, 1383, 1384, 3033, 3034, 3035,
				3036, 3881, 3882, 3883, 5902, 5903, 5904 }),
				WILLOW(1519, 30, 135, 22, 4, new int[] { 1308, 5551, 5552, 5553 }),
				OAK(1521, 15, 75, 22, 2, new int[] { 1281, 3037 }),
				MAGIC(1513, 75, 500, 310, 9, new int[] { 1306 }),
				MAPLE(1517, 45, 200, 100, 5, new int[] { 1307, 4677 }),
				MAHOGANY(6332, 50, 250, 22, 4, new int[] { 9034 }),
				TEAK(6333, 35, 170, 22, 4, new int[] { 9036 }),
				ACHEY(2862, 1, 50, 22, 4, new int[] { 2023 }),
				YEW(1515, 60, 350, 160, 7, new int[] { 1309 }),
				DRAMEN(771, 36, 0, 22, 4, new int[] { 1292 });

		private int[] objects;
		private int level;
		private int log;
		private int respawnTimer;
		private int maxLogs;
		private double experience;

		private static Map<Integer, Tree> trees = new HashMap<Integer, Tree>();

		public static Tree forId(int object) {
			return trees.get(object);
		}

		static {
			for(Tree tree : Tree.values()) {
				for(int object : tree.objects) {
					trees.put(object, tree);
				}
			}
		}

		private Tree(int log, int level, double experience, int respawnTimer, int maxLogs, int[] objects) {
			this.objects = objects;
			this.level = level;
			this.experience = experience;
			this.respawnTimer = respawnTimer;
			this.maxLogs = maxLogs;
			this.log = log;
		}

		@Override
		public int getRequiredLevel() {
			return level;
		}
		
		@Override
		public double getExperience() {
			return experience;
		}
	}

	private final Tree tree;

	private Hatchet hatchet;

	public Woodcutting(Tree tree) {
		this.tree = tree;
	}

	@Override
	public Item getReward() {
		return new Item(tree.log, 1);
	}
	
	@Override
	public String getMessage(int type) {
		switch(type) {
		case TOOL_LEVEL:
			return "You do not have the required woodcutting level to use this hatchet.";
		case NO_TOOL:
			return "You do not have a hatchet to cut this tree with.";
		case OBJECT_LEVEL:
			return "You do not have the required woodcutting level to chop this tree.";
		case HARVESTED_ITEM:
			return "You chop some " + ItemDefinition.forId(tree.log).getName().toLowerCase() +".";
		}
		return "";
	}

	@Override
	public String getStartMessage() {
		return "You start swinging your axe at the tree...";
	}

	@Override
	public int getSkill() {
		return Skills.WOODCUTTING;
	}

	@Override
	public int getCycleTime() {
		int skill = getMob().getPlayer().getSkills().getLevel(Skills.WOODCUTTING);
		int level = tree.level;
		int modifier = hatchet.level;
		int randomAmt = Misc.random(3);
		double cycleCount =  Math.ceil((level * 50 - skill * 10) / modifier * 0.25 - randomAmt * 4);
		if (cycleCount < 1) {
			cycleCount = 1;
		}
		return (int) cycleCount + 1;
	}

	@Override
	public HarvestObject getGameObject() {
		return tree;
	}

	@Override
	public HarvestTool getTool() {
		if(hatchet == null) {
			Player player = getMob().getPlayer();
			for(Map.Entry<Integer, Hatchet> entry : Hatchet.hatchets.entrySet()) {
				if(player.getInventory().contains(entry.getKey()) || player.getEquipment().getSlot(3) == entry.getKey()) {
					hatchet = entry.getValue();
					if(getMob().getPlayer().getSkills().getLevel(Skills.WOODCUTTING) >= hatchet.level) {
						break;
					}
				}
			}
		}
		return hatchet;
	}


}
