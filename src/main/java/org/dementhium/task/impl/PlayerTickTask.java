package org.dementhium.task.impl;

import org.dementhium.model.combat.Combat;
import org.dementhium.model.combat.CombatState;
import org.dementhium.model.player.Player;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.task.Task;
import org.dementhium.util.WorldList;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PlayerTickTask implements Task {

	private final Player player;

	public PlayerTickTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		player.processPackets();
		if(!player.getConnection().isInLobby()) {
			player.getWalkingQueue().getNextEntityMovement();
			player.processQueuedHits();
			CombatState cState = player.getCombatState();
			Combat.process(player);
			if(cState.getAttackDelay() > 0) {
				cState.setAttackDelay(cState.getAttackDelay() - 1);
			}
			if(cState.getSpellDelay() > 0) {
				cState.setSpellDelay(cState.getSpellDelay() - 1);
			}
			player.getHeadIcons().tick();
			player.getTeleblock().tick();
			player.getPrayer().tick();
			player.checkDoubleWeekend();
			player.refreshAttackOptions();
		} else {
			player.getConnection().write(new MessageBuilder(46).toMessage());
			player.getConnection().write(WorldList.getData(true, true));
		}
	}

}
