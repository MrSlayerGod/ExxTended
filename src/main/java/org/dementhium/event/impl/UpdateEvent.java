package org.dementhium.event.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dementhium.event.Event;
import org.dementhium.model.World;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.task.ConsecutiveTask;
import org.dementhium.task.ParallelTask;
import org.dementhium.task.Task;
import org.dementhium.task.impl.NPCResetTask;
import org.dementhium.task.impl.NPCTickTask;
import org.dementhium.task.impl.PlayerResetTask;
import org.dementhium.task.impl.PlayerTickTask;
import org.dementhium.task.impl.PlayerUpdateTask;
/**
 * 
 * @author Graham Edgecombe
 * @author 'Mystic Flow
 */
public class UpdateEvent extends Event {

	public static final int TICK = 600;

	public UpdateEvent() {
		super(TICK);
	}

	public void run() {
		World.getWorld().processTickables();

		List<Task> tickTasks = new ArrayList<Task>();
		List<Task> updateTasks = new ArrayList<Task>();
		List<Task> resetTasks = new ArrayList<Task>();

		for(NPC npc : World.getWorld().getNpcs()) {
			tickTasks.add(new NPCTickTask(npc));
			resetTasks.add(new NPCResetTask(npc));
		}

		Iterator<Player> globalIterator = World.getWorld().getPlayers().iterator();
		while(globalIterator.hasNext()) {
			Player player = globalIterator.next();
			if(player.getConnection().isDisconnected() && player.getCombatState().getLastAttacker() == null) {
				if(player.logoutDelay > 0) {
					player.logoutDelay--;
					tickTasks.add(new PlayerTickTask(player));
					updateTasks.add(new PlayerUpdateTask(player));
					resetTasks.add(new PlayerResetTask(player));
				} else if(player.getSkills().getHitPoints() <= 0) {
					player.logoutDelay = 60;
					player.getSkills().sendDead();
					tickTasks.add(new PlayerTickTask(player));
					updateTasks.add(new PlayerUpdateTask(player));
					resetTasks.add(new PlayerResetTask(player));
				} else {
					World.getWorld().unregister(player);
					globalIterator.remove();
				}
			} else {
				if(player.getConnection().isDisconnected() && player.getCombatState().getLastAttacker() != null) {
					player.logoutDelay = 60;
				}
				if(player.isOnline()) {
					tickTasks.add(new PlayerTickTask(player));
					updateTasks.add(new PlayerUpdateTask(player));
					resetTasks.add(new PlayerResetTask(player));
				}
			}
		}

		Iterator<Player> lobbyIterator = World.getWorld().getLobbyPlayers().iterator();

		while(lobbyIterator.hasNext()) {
			Player player = lobbyIterator.next();
			if(player.getConnection().isDisconnected() || player.isOnline()) {
				lobbyIterator.remove();
			} else {
				if(player.getConnection().isInLobby()) {
					tickTasks.add(new PlayerTickTask(player));
				}
			}
		}

		Task tickTask = new ConsecutiveTask(tickTasks.toArray(new Task[0]));
		Task updateTask = new ParallelTask(updateTasks);
		Task resetTask = new ParallelTask(resetTasks);

		World.getWorld().submit(new ConsecutiveTask(tickTask, updateTask, resetTask));
	}
}