package org.dementhium.action;

import org.dementhium.model.Item;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public abstract class HarvestingAction extends Action {

	public static final int TOOL_LEVEL = 0, NO_TOOL = 1, OBJECT_LEVEL = 2, HARVESTED_ITEM = 3;
	
	public interface HarvestTool {
		
		public Animation getAnimation();
		
		public int getRequiredLevel();
	}
	
	public interface HarvestObject {
		
		public int getRequiredLevel();
		
		public double getExperience();
		
	}
	
	private int cycles;
	
	private int animationCycles;
	
	private boolean started;
	
	public HarvestingAction() {
		super(1);
	}

	@Override
	public void execute() {
		HarvestObject object = getGameObject();
		if(object == null) {
			stop();
			return;
		}
		HarvestTool tool = getTool();
		Player player = getMob().getPlayer();
		int level = player.getSkills().getLevel(getSkill());
		if(++animationCycles >= 3 && tool != null) {
			player.animate(tool.getAnimation());
		}
		if(cycles-- > 0) {
			return;
		}
		if(object.getRequiredLevel() > level) {
			player.sendMessage(getMessage(OBJECT_LEVEL));
			stop();
			return;
		}
		if(tool != null) {
			if(tool.getRequiredLevel() > level) {
				player.sendMessage(getMessage(TOOL_LEVEL));
				stop();
				return;
			}
			cycles = getCycleTime();
			if(!started) {
				player.sendMessage(getStartMessage());
				player.animate(tool.getAnimation());
				started = true;
				return;
			}
			Item reward = getReward();
			if(!player.getInventory().addItem(reward.getId(), reward.getAmount())) {
				stop();
				return;
			}
			player.getSkills().addXp(getSkill(), object.getExperience());
			player.sendMessage(getMessage(HARVESTED_ITEM));
		} else {
			player.sendMessage(getMessage(NO_TOOL));
			stop();
			return;
		}
	}
	
	public abstract Item getReward();

	public abstract String getStartMessage();
	
	public abstract String getMessage(int type);
	
	public abstract int getSkill();
	
	public abstract int getCycleTime();
	
	public abstract HarvestTool getTool();

	public abstract HarvestObject getGameObject();
	
}
