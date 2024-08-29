package org.dementhium.util;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Constants;
import org.dementhium.util.Misc;
import org.dementhium.event.Tickable;
/**
 * 
 * @author 'Mystic Flow
 *
 */
public class InputHandler {

	public static void handleStringInput(final Player player, String string) {
		int inputId = (Integer) player.getAttribute("inputId", -1);
		if (inputId > -1) {
			switch (inputId) {
			case 0: //enter clan name
				String clan = string.replaceAll("_", " ");
				ActionSender.sendString(player, clan, 590, 22);
				World.getWorld().getClanManager().createClan(player, clan);
				break;
			case 1: // Report player
				Player player2 = player;
				boolean foundPlayer = false;
				for(Player pl : World.getWorld().getPlayers()) {
					if(player.reportedPlayer.equalsIgnoreCase(Misc.formatPlayerNameForDisplay(pl.getUsername()))) {
						foundPlayer = true;
						player2 = pl;
					}
				}
				if(foundPlayer && player2 != player) {
					int reportId = Constants.getLogDatabase().createReport(player);
					if(reportId == 0) {
						player.sendMessage("Error reporting user, database might be offline.");
						return;
					}
					player.addNewReport(reportId);
					player2.addNewReport(reportId);
					player.updateLog(Misc.formatPlayerNameForDisplay(player.getUsername())+" reported "+Misc.formatPlayerNameForDisplay(player2.getUsername())+" for "+string+".", player2);
					player.reportWait = true;
					World.getWorld().submit(new Tickable(120) {
						@Override
						public void execute() {
							player.reportWait = false;
							this.stop();
						}
					});
				} else {
					player.sendMessage("That player is not online for you to report them!");
				}
				break;
			}
		}
		resetInput(player);
	}

	public static void resetInput(Player player) {
		player.removeAttribute("inputId");
	}

	public static void requestStringInput(Player player, int inputId, String question) {
		player.setAttribute("inputId", inputId);
		ActionSender.sendClientScript(player, 109, new Object[]{ question }, "s");
	}

	public static void requestIntegerInput(Player player, int inputId, String question) {
		player.setAttribute("inputId", inputId);
		ActionSender.sendClientScript(player, 108, new Object[]{ question }, "s");
	}

	public static void handleIntegerInput(Player player, int value) {
		int inputId = (Integer) player.getAttribute("inputId", -1);
		int slot = (Integer) player.getAttribute("slotId", -1);
		if (inputId > -1 && slot > -1) {
			switch (inputId) {
			case 1:
				if (player.getTradeSession() != null) {
					player.getTradeSession().removeItem(player, slot, value);
					player.setAttribute("slotId", 0);
				} else if (player.getTradePartner() != null) {
					player.getTradePartner().getTradeSession().removeItem(player, slot, value);
					player.setAttribute("slotId", 0);
				}
				break;
			case 2:
				if (player.getTradeSession() != null) {
					player.getTradeSession().offerItem(player, slot, value);
					player.setAttribute("slotId", 0);
				} else if (player.getTradePartner() != null) {
					player.getTradePartner().getTradeSession().offerItem(player, slot, value);
					player.setAttribute("slotId", 0);
				}
				break;
			case 3:
				if (player.getBob() != null) {
					player.getBob().putItem(slot, value);
					player.setAttribute("slotId", 0);
				}
				break;
			case 4:
				if (player.getBob() != null) {
					player.getBob().removeItem(slot, value);
					player.setAttribute("slotId", 0);
				}
				break;
			case 5:
				if (player.getBank() != null) {
					player.getBank().addItem(slot, value);
					player.setAttribute("slotId", 0);
				}
				break;
			case 6:
				if (player.getBank() != null) {
					player.getBank().removeItem(slot, value);
					player.setAttribute("slotId", 0);
				}
				break;
			}
		}
	}
}
