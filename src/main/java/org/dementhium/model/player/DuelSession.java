package org.dementhium.model.player;

import java.text.NumberFormat;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;
import org.dementhium.content.misc.ValidItems;

public class DuelSession {

	public static final int ALL_ITEMS = -5;
	private final Player dueler, partner;
	private DuelState currentState = DuelState.STATE_ONE;
	private final Container duelerItemsOffered = new Container(28, false), partnerItemsOffered = new Container(28, false);
	private boolean duelerDidAccept, partnerDidAccept;

	/*
	 * Some info for the future,
	 * 44 = wealth transfer
	 * 43 = left limit
	 * 45 = right limit
	 */



	public DuelSession(Player dueler, Player partner) {
		this.dueler = dueler;
		this.partner = partner;
		dueler.setAttribute("didRequestDuel", Boolean.FALSE);
		partner.setAttribute("didRequestDuel", Boolean.FALSE);
		openFirstDuelScreen(dueler);
		openFirstDuelScreen(partner);
	}

	public Player getPartner() {
		return partner;
	}

	public void openFirstDuelScreen(Player p) {
		ActionSender.sendDuelOptions(p);
		ActionSender.sendInventoryInterface(p, 628);
		ActionSender.sendInterface(p, 631);
		ActionSender.sendItems(p, 134, duelerItemsOffered, false);
		ActionSender.sendItems(p, 134, partnerItemsOffered, true);
		refreshScreen();
	}

	public void openSecondDuelScreen(Player p) {
		currentState = DuelState.STATE_TWO;
		partnerDidAccept = false;
		duelerDidAccept = false;
		ActionSender.sendInterface(p, 626);
	}

	private String buildString(Container container) {
		if(container.freeSlots() == container.getSize()) {
			return "<col=FFFFFF>Absolutely nothing!";
		} else {
			StringBuilder bldr = new StringBuilder();
			for(int i = 0; i < container.getSize(); i++) {
				Item item = container.get(i);
				if(item != null) {
					bldr.append("" + item.getDefinition().getName());
					if(item.getAmount() > 1) {
						bldr.append(" x " + item.getAmount());
					}
					bldr.append("<br>");
				}
			}
			return bldr.toString();
		}
	}

	public void offerItem(Player pl, int slot, int amt) {
		if(duelerDidAccept && partnerDidAccept || currentState == DuelState.STATE_TWO) {
			return;
		}
		if (pl.equals(dueler)) {
			if(duelerDidAccept) {
				return;
			}
			Item item = new Item(pl.getInventory().getContainer().get(slot).getId(), amt);
			if (item != null) {
				duelerDidAccept = false;
				partnerDidAccept = false;
				//ActionSender.sendString(dueler, "", 335, 38);
				//ActionSender.sendString(partner, "", 335, 38);
				if (pl.getInventory().getContainer().getNumberOff(item.getId()) < amt) {
					item.setAmount(pl.getInventory().getContainer().getNumberOff(item.getId()));
					amt = pl.getInventory().getContainer().getNumberOff(item.getId());
				}
				if (duelerItemsOffered.getFreeSlots() < amt && !pl.getInventory().getContainer().get(slot).getDefinition().isNoted() && !pl.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
					item.setAmount(duelerItemsOffered.getFreeSlots());
				}
				duelerItemsOffered.add(item);
				pl.getInventory().getContainer().remove(new Item(pl.getInventory().getContainer().get(slot).getId(), amt));
				pl.getSt().getContainer().add(item);
				pl.getInventory().refresh();
			}
		} else if (pl.equals(partner)) {
			if(partnerDidAccept) {
				return;
			}
			Item item = new Item(pl.getInventory().getContainer().get(slot).getId(), amt);
			if (item != null) {
				duelerDidAccept = false;
				partnerDidAccept = false;
				//ActionSender.sendString(dueler, "", 335, 38);
				//ActionSender.sendString(partner, "", 335, 38);
				if (pl.getInventory().getContainer().getNumberOff(item.getId()) < amt) {
					item.setAmount(pl.getInventory().getContainer().getNumberOff(item.getId()));
					amt = pl.getInventory().getContainer().getNumberOff(item.getId());
				}
				if (partnerItemsOffered.getFreeSlots() < amt && !pl.getInventory().getContainer().get(slot).getDefinition().isNoted() && !pl.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
					item.setAmount(partnerItemsOffered.getFreeSlots());
				}
				partnerItemsOffered.add(item);
				pl.getInventory().getContainer().remove(item);
				pl.getSt().getContainer().add(item);
				pl.getInventory().refresh();
			}
		}
		refreshScreen();
	}

	public void removeItem(Player pl, int slot, int amt) {
		if(duelerDidAccept && partnerDidAccept || currentState == DuelState.STATE_TWO) {
			return;
		}
		if (pl.equals(dueler)) {
			if(duelerDidAccept) {
				return;
			}
			Item item = new Item(duelerItemsOffered.get(slot).getId(), amt);
			if (item != null) {
				duelerDidAccept = false;
				partnerDidAccept = false;
				//ActionSender.sendString(dueler, "", 335, 38);
				//ActionSender.sendString(partner, "", 335, 38);
				if (duelerItemsOffered.getNumberOff(item.getId()) < amt) {
					item.setAmount(duelerItemsOffered.getNumberOff(item.getId()));
					amt = duelerItemsOffered.getNumberOff(item.getId());
				}
				if (pl.getInventory().getFreeSlots() < amt && !duelerItemsOffered.get(slot).getDefinition().isNoted() && !duelerItemsOffered.get(slot).getDefinition().isStackable()) {
					item.setAmount(pl.getInventory().getFreeSlots());
				}
				dueler.getInventory().getContainer().add(new Item(duelerItemsOffered.get(slot).getId(), item.getAmount()));
				dueler.getInventory().refresh();

				duelerItemsOffered.remove(item);
				dueler.getSt().getContainer().remove(item);
			}
		} else if (pl.equals(partner)) {
			if(partnerDidAccept) {
				return;
			}
			Item item = new Item(partnerItemsOffered.get(slot).getId(), amt);
			if (item != null) {
				duelerDidAccept = false;
				partnerDidAccept = false;
				//ActionSender.sendString(dueler, "", 335, 38);
				//ActionSender.sendString(partner, "", 335, 38);
				if (partnerItemsOffered.getNumberOff(item.getId()) < amt) {
					item.setAmount(partnerItemsOffered.getNumberOff(item.getId()));
					amt = partnerItemsOffered.getNumberOff(item.getId());
				}
				if (pl.getInventory().getFreeSlots() < amt && !partnerItemsOffered.get(slot).getDefinition().isNoted() && !partnerItemsOffered.get(slot).getDefinition().isStackable()) {
					item.setAmount(pl.getInventory().getFreeSlots());
				}
				partner.getInventory().getContainer().add(new Item(partnerItemsOffered.get(slot).getId(), item.getAmount()));
				partner.getInventory().refresh();
				partnerItemsOffered.remove(item);
				partner.getSt().getContainer().remove(item);
			}
		}
		refreshScreen();
	}

	private void refreshScreen() {
		ActionSender.sendItems(dueler, 134, duelerItemsOffered, false);
		ActionSender.sendItems(partner, 134, partnerItemsOffered, false);
		ActionSender.sendItems(dueler, 134, partnerItemsOffered, true);
		ActionSender.sendItems(partner, 134, duelerItemsOffered, true);
		/*ActionSender.sendString(dueler, 335, 22, Misc.formatPlayerNameForDisplay(partner.getUsername()));
		ActionSender.sendString(dueler, 335, 21,  " has " + partner.getInventory().getFreeSlots() + " free inventory slots.");
		ActionSender.sendString(partner, 335, 22, Misc.formatPlayerNameForDisplay(dueler.getUsername()));
		ActionSender.sendString(partner, 335, 21,  " has " + dueler.getInventory().getFreeSlots() + " free inventory slots.");
		ActionSender.sendString(partner, 335, 43,  "Value: " + formatPrice(getPartnersItemsValue()));
		ActionSender.sendString(dueler, 335, 43,  "Value: " + formatPrice(getduelersItemsValue()));
		ActionSender.sendString(dueler, 335, 45,  "Value: " + formatPrice(getPartnersItemsValue()));
		ActionSender.sendString(partner, 335, 45,  "Value: " + formatPrice(getduelersItemsValue()));*/
	}

	private long getduelersItemsValue() {
		long initialPrice = 0;
		for (Item item : duelerItemsOffered.getItems()) {
			if (item != null) {
				initialPrice += item.getDefinition().getExchangePrice();
			}
		}
		return initialPrice;
	}

	private long getPartnersItemsValue() {
		long initialPrice = 0;
		for (Item item : partnerItemsOffered.getItems()) {
			if (item != null) {
				initialPrice += item.getDefinition().getExchangePrice();
			}
		}
		return initialPrice;
	}

	@SuppressWarnings("unused")
	private void flashSlot(Player player, int slot) {
		ActionSender.sendClientScript(player, 143, new Object[]{slot, 7, 4, player.equals(dueler) ? 21954591 : 21954593}, "Iiii"); //Guess this wouldn't work for both screens.
	}

	public void acceptPressed(Player pl, int interfaceId) {
		if(pl.equals(dueler) && partner.getConnection().isDisconnected()) {
			return;
		} else if(pl.equals(partner) && dueler.getConnection().isDisconnected()) {
			return;
		}
		if(interfaceId == 335 && currentState != DuelState.STATE_ONE) {
			return;
		}
		if(interfaceId == 334 && currentState != DuelState.STATE_TWO) {
			return;
		}
		if (!duelerDidAccept && pl.equals(dueler)) {
			duelerDidAccept = true;
		} else if (!partnerDidAccept && pl.equals(partner)) {
			partnerDidAccept = true;
		}
		switch (currentState) {
		case STATE_ONE:
			if (pl.equals(dueler)) {
				if (partnerDidAccept && duelerDidAccept) {
					openSecondDuelScreen(dueler);
					openSecondDuelScreen(partner);
				} else {
					//ActionSender.sendString(dueler, "Waiting for other player...", 335, 38);
					//ActionSender.sendString(partner, "The other player has accepted", 335, 38);
				}
			} else if (pl.equals(partner)) {
				if (partnerDidAccept && duelerDidAccept) {
					openSecondDuelScreen(dueler);
					openSecondDuelScreen(partner);
				} else {
					//ActionSender.sendString(partner, "Waiting for other player...", 335, 38);
					//ActionSender.sendString(dueler, "The other player has accepted", 335, 38);
				}
			}
			break;

		case STATE_TWO:
			if (pl.equals(dueler)) {
				if (partnerDidAccept && duelerDidAccept) {
					giveItems();
				} else {
					//ActionSender.sendString(dueler, "Waiting for other player...", 334, 33);
					//ActionSender.sendString(partner, "The other player has accepted", 334, 33);
				}
			} else if (pl.equals(partner)) {
				if (partnerDidAccept && duelerDidAccept) {
					giveItems();
				} else {
					//ActionSender.sendString(partner, "Waiting for other player...", 334, 33);
					//ActionSender.sendString(dueler, "The other player has accepted", 334, 33);
				}
			}
			break;
		}

	}

	public void duelFailed() {
		for (Item itemAtIndex : duelerItemsOffered.getItems()) {
			if (itemAtIndex != null) {
				dueler.getInventory().getContainer().add(itemAtIndex);
			}
		}
		for (Item itemAtIndex : partnerItemsOffered.getItems()) {
			if (itemAtIndex != null) {
				partner.getInventory().getContainer().add(itemAtIndex);
			}
		}
		dueler.sendMessage("Duel declined.");
		partner.sendMessage("Duel declined.");
		dueler.getSt().reset();
		partner.getSt().reset();
		duelerItemsOffered.reset();
		partnerItemsOffered.reset();
		dueler.getInventory().refresh();
		partner.getInventory().refresh();
		endSession(dueler);
		endSession(partner);
	}

	public void endSession(Player player) {
		ActionSender.closeInter(dueler);
		ActionSender.closeInter(partner);
		ActionSender.closeInventoryInterface(dueler);
		ActionSender.closeInventoryInterface(partner);
		//ActionSender.sendLoginMasks(dueler);
		//ActionSender.sendLoginMasks(partner);
		player.setDuelSession(null);
		player.setDuelPartner(null);
	}

	private void giveItems() {
		if (!dueler.getInventory().getContainer().hasSpaceFor(partnerItemsOffered)) {
			ActionSender.sendMessage(partner, "The other player does not have enough space in their inventory.");
			ActionSender.sendMessage(dueler, "You do not have enough space in your inventory.");
			duelFailed();
			return;
		} else if (!partner.getInventory().getContainer().hasSpaceFor(duelerItemsOffered)) { 
			ActionSender.sendMessage(dueler, "The other player does not have enough space in their inventory.");
			ActionSender.sendMessage(partner, "You do not have enough space in your inventory.");
			duelFailed();
			return;
		}
		for (Item itemAtIndex : duelerItemsOffered.getItems()) {
			if (itemAtIndex != null) {
				partner.getInventory().getContainer().add(itemAtIndex);
			}
		}
		for (Item itemAtIndex : partnerItemsOffered.getItems()) {
			if (itemAtIndex != null) {
				dueler.getInventory().getContainer().add(itemAtIndex);
			}
		}
		dueler.getSt().reset();
		partner.getSt().reset();
		duelerItemsOffered.reset();
		partnerItemsOffered.reset();
		partner.getInventory().refresh();
		dueler.getInventory().refresh();
		endSession(partner);
		endSession(dueler);
	}

	public Container getPlayerItemsOffered(Player p) {
		return (p.equals(dueler) ? duelerItemsOffered : partnerItemsOffered);
	}
	
	public String formatPrice(long price) {
		return NumberFormat.getInstance().format(price);
	}
	

	public enum DuelState {
		STATE_ONE,
		STATE_TWO;
	}
}
