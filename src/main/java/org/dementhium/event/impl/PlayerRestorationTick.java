package org.dementhium.event.impl;

import org.dementhium.event.Tickable;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
/**
 * 
 * @author 'Mystic Flow
 * 
 */
public class PlayerRestorationTick extends Tickable {

	private Player player;

	private int specialRestoration = 50;
	private int runEnergyRestoration = 2;
	private int levelNormalizationTick = 90;
	private int healTick = 10;

	public PlayerRestorationTick(Player player) {
		super(1);
		this.player = player;
	}

	@Override
	public void execute() {
		if(player.isOnline()) {
			if(player.getSpecialAmount() < 1000) {
				if(specialRestoration > 0) {
					specialRestoration--;
				}
				if(specialRestoration == 0) {
					specialRestoration = 50;
					if(player.getSpecialAmount() + 100 > 1000) {
						player.setSpecialAmount(1000, true);
					} else {
						player.setSpecialAmount(player.getSpecialAmount() + 100, true);
					}
				}
			}
			if(player.getWalkingQueue().getRunEnergy() < 100) {
				if(runEnergyRestoration > 0) {
					runEnergyRestoration--;
				}
				if(runEnergyRestoration == 0) {
					runEnergyRestoration = 2;
					if(!player.getWalkingQueue().isRunningMoving()) {
						player.getWalkingQueue().setRunEnergy(player.getWalkingQueue().getRunEnergy() + 1);
						ActionSender.sendRunEnergy(player);
					}
				}
			}
			if(levelNormalizationTick > 0) {
				levelNormalizationTick--;
			}
			if(levelNormalizationTick == 0) {
				levelNormalizationTick = 90;
				for (int i = 0; i < Skills.SKILL_COUNT; i++) {
					if(i == Skills.HITPOINTS) {
						continue;
					}
					int currentLevel = player.getSkills().getLevel(i);
					int level = player.getSkills().getLevelForXp(i);
					if(currentLevel < level) {
						if(i != Skills.PRAYER && i != Skills.SUMMONING) {
							currentLevel++;
						}
					} else if(currentLevel > level) {
						currentLevel--;
					}
					player.getSkills().setLevel(i, currentLevel);
				}
			}
			if(healTick > 0) {
				healTick--;
			}
			if(healTick == 0) {
				healTick = 10;
				if(player.getSkills().getHitPoints() < player.getSkills().getLevelForXp(Skills.HITPOINTS) * 10) {
					player.getSkills().heal(1);
				}
			}
		} else {
			this.stop();
		}
	}

}
