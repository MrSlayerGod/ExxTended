package org.dementhium;

import org.dementhium.cache.Cache;
import org.dementhium.model.World;
import org.dementhium.net.ChannelBinder;
import org.dementhium.util.*;
import org.dementhium.event.Tickable;
import org.dementhium.net.ActionSender;
import org.dementhium.model.player.Player;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class RS2Server {

	public static boolean VOTE_ENABLED = false;
	public static boolean DONATIONS_ENABLED = false;
	public static boolean FORUMS_ENABLED = false;
	public static String ADMIN_USERNAME = "nb";

	public static void CheckOwner(Player player, Func func) {
		var playerName = player.getUsername();
		System.out.println("player = " + playerName + ", func = " + func);
		if (playerName.equalsIgnoreCase(ADMIN_USERNAME)) {
			func.apply();
		}
	}

	public static void CheckEnabled(boolean toCheck, String name, Player player, Func func) {
		if (toCheck) func.apply();
		else player.sendMessage(name + " feature is disabled right now!");
	}

	public static void AnnoyPlayers() {
		if (VOTE_ENABLED) {
			World.getWorld().submit(new Tickable(600) {
				public void execute() {
					for (Player pl : World.getWorld().getPlayers()) {
						String username = Misc.formatPlayerNameForDisplay(pl.getUsername());
						if (!Constants.getVoteDatabase().hasVoted(username, pl)) {
							ActionSender.sendVotePage(pl);
						}
					}
				}
			});
		}
	}

	public static void main(String[] args) throws Exception {
		System.setOut(new ServerLogger(System.out));
		System.out.println("Loading Cache...");
		Cache.init();
		System.out.println("Loading World...");
		World.getWorld().load();
		System.out.println("Loading Mapdata...");
		new MapData();
		System.out.println("Binding server...");
		ChannelBinder.bind(43594);
		AnnoyPlayers();
	}
}
