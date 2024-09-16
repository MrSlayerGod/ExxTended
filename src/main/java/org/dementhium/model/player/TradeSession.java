package org.dementhium.model.player;

import java.text.NumberFormat;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;
import org.dementhium.content.misc.ValidItems;
import org.dementhium.util.Logger;

/**
 * Represents a trade session
 * @author Stephen
 */
public class TradeSession {

	public static final int ALL_ITEMS = -5;
	private final Player trader, partner;
	private TradeState currentState = TradeState.STATE_ONE;
	private final Container traderItemsOffered = new Container(28, false), partnerItemsOffered = new Container(28, false);
	private boolean traderDidAccept, partnerDidAccept;

	/*
	 * Some info for the future,
	 * 44 = wealth transfer
	 * 43 = left limit
	 * 45 = right limit
	 */



	public TradeSession(Player trader, Player partner) {
		this.trader = trader;
		this.partner = partner;
		trader.setAttribute("didRequestTrade", Boolean.FALSE);
		partner.setAttribute("didRequestTrade", Boolean.FALSE);
		openFirstTradeScreen(trader);
		openFirstTradeScreen(partner);
	}

	public Player getPartner() {
		return partner;
	}
	public void openFirstTradeScreen(Player p) {
		ActionSender.sendTradeOptions(p);
		ActionSender.sendInterface(p, 335);
		ActionSender.sendInventoryInterface(p, 336);
		ActionSender.sendItems(p, 90, traderItemsOffered, false);
		ActionSender.sendItems(p, 90, partnerItemsOffered, true);
		ActionSender.sendString(p, "", 335, 38);
		ActionSender.sendString(p, "Trading with: " + Misc.formatPlayerNameForDisplay(p.equals(trader) ? partner.getUsername() : trader.getUsername()), 335, 15);
		refreshScreen();
	}

	public void openSecondTradeScreen(Player p) {
		currentState = TradeState.STATE_TWO;
		partnerDidAccept = false;
		traderDidAccept = false;
		ActionSender.sendInterface(p, 334);
		ActionSender.sendString(p, "<col=00FFFF>Trading with:<br><col=00FFFF>" + Misc.formatPlayerNameForDisplay(p.equals(trader) ? partner.getUsername() : trader.getUsername()), 334, 45);
		ActionSender.sendString(p, buildString(p.equals(trader) ? traderItemsOffered : partnerItemsOffered), 334, 37);
		ActionSender.sendString(p, buildString(p.equals(trader) ? partnerItemsOffered : traderItemsOffered), 334, 41);
		ActionSender.sendInterfaceConfig(p, 334, 37, false);
		ActionSender.sendInterfaceConfig(p, 334, 41, false);
		ActionSender.sendInterfaceConfig(p, 334, 45, false);
		ActionSender.sendInterfaceConfig(p, 334, 46, false);
		ActionSender.sendString(p, "", 334, 33);
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
		if(traderDidAccept && partnerDidAccept || currentState == TradeState.STATE_TWO) {
			return;
		}
		if (pl.equals(trader)) {
			if(traderDidAccept) {
				return;
			}
			Item item = new Item(pl.getInventory().getContainer().get(slot).getId(), amt);
			if (item != null) {
				traderDidAccept = false;
				partnerDidAccept = false;
				ActionSender.sendString(trader, "", 335, 38);
				ActionSender.sendString(partner, "", 335, 38);
				if (pl.getInventory().getContainer().getNumberOff(item.getId()) < amt) {
					item.setAmount(pl.getInventory().getContainer().getNumberOff(item.getId()));
					amt = pl.getInventory().getContainer().getNumberOff(item.getId());
				}
				if (traderItemsOffered.getFreeSlots() < amt && !pl.getInventory().getContainer().get(slot).getDefinition().isNoted() && !pl.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
					item.setAmount(traderItemsOffered.getFreeSlots());
				}
				traderItemsOffered.add(item);
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
				traderDidAccept = false;
				partnerDidAccept = false;
				ActionSender.sendString(trader, "", 335, 38);
				ActionSender.sendString(partner, "", 335, 38);
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
		if(traderDidAccept && partnerDidAccept || currentState == TradeState.STATE_TWO) {
			return;
		}
		if (pl.equals(trader)) {
			if(traderDidAccept) {
				return;
			}
			Item item = new Item(traderItemsOffered.get(slot).getId(), amt);
			if (item != null) {
				traderDidAccept = false;
				partnerDidAccept = false;
				ActionSender.sendString(trader, "", 335, 38);
				ActionSender.sendString(partner, "", 335, 38);
				if (traderItemsOffered.getNumberOff(item.getId()) < amt) {
					item.setAmount(traderItemsOffered.getNumberOff(item.getId()));
					amt = traderItemsOffered.getNumberOff(item.getId());
				}
				if (pl.getInventory().getFreeSlots() < amt && !traderItemsOffered.get(slot).getDefinition().isNoted() && !traderItemsOffered.get(slot).getDefinition().isStackable()) {
					item.setAmount(pl.getInventory().getFreeSlots());
				}
				trader.getInventory().getContainer().add(new Item(traderItemsOffered.get(slot).getId(), item.getAmount()));
				trader.getInventory().refresh();

				traderItemsOffered.remove(item);
				trader.getSt().getContainer().remove(item);
			}
		} else if (pl.equals(partner)) {
			if(partnerDidAccept) {
				return;
			}
			Item item = new Item(partnerItemsOffered.get(slot).getId(), amt);
			if (item != null) {
				traderDidAccept = false;
				partnerDidAccept = false;
				ActionSender.sendString(trader, "", 335, 38);
				ActionSender.sendString(partner, "", 335, 38);
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
		ActionSender.sendItems(trader, 90, traderItemsOffered, false);
		ActionSender.sendItems(partner, 90, partnerItemsOffered, false);
		ActionSender.sendItems(trader, 90, partnerItemsOffered, true);
		ActionSender.sendItems(partner, 90, traderItemsOffered, true);
		ActionSender.sendString(trader, 335, 22, Misc.formatPlayerNameForDisplay(partner.getUsername()));
		ActionSender.sendString(trader, 335, 21,  " has " + partner.getInventory().getFreeSlots() + " free inventory slots.");
		ActionSender.sendString(partner, 335, 22, Misc.formatPlayerNameForDisplay(trader.getUsername()));
		ActionSender.sendString(partner, 335, 21,  " has " + trader.getInventory().getFreeSlots() + " free inventory slots.");
		ActionSender.sendString(partner, 335, 43,  "Value: " + formatPrice(getPartnersItemsValue()));
		ActionSender.sendString(trader, 335, 43,  "Value: " + formatPrice(getTradersItemsValue()));
		ActionSender.sendString(trader, 335, 45,  "Value: " + formatPrice(getPartnersItemsValue()));
		ActionSender.sendString(partner, 335, 45,  "Value: " + formatPrice(getTradersItemsValue()));
	}

	private long getTradersItemsValue() {
		long initialPrice = 0;
		for (Item item : traderItemsOffered.getItems()) {
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
		ActionSender.sendClientScript(player, 143, new Object[]{slot, 7, 4, player.equals(trader) ? 21954591 : 21954593}, "Iiii"); //Guess this wouldn't work for both screens.
	}

	public void acceptPressed(Player pl, int interfaceId) {
		if(pl.equals(trader) && partner.getConnection().isDisconnected()) {
			return;
		} else if(pl.equals(partner) && trader.getConnection().isDisconnected()) {
			return;
		}
		if(interfaceId == 335 && currentState != TradeState.STATE_ONE) {
			return;
		}
		if(interfaceId == 334 && currentState != TradeState.STATE_TWO) {
			return;
		}
		if (!traderDidAccept && pl.equals(trader)) {
			traderDidAccept = true;
		} else if (!partnerDidAccept && pl.equals(partner)) {
			partnerDidAccept = true;
		}
		switch (currentState) {
		case STATE_ONE:
			if (pl.equals(trader)) {
				if (partnerDidAccept && traderDidAccept) {
					openSecondTradeScreen(trader);
					openSecondTradeScreen(partner);
				} else {
					ActionSender.sendString(trader, "Waiting for other player...", 335, 38);
					ActionSender.sendString(partner, "The other player has accepted", 335, 38);
				}
			} else if (pl.equals(partner)) {
				if (partnerDidAccept && traderDidAccept) {
					openSecondTradeScreen(trader);
					openSecondTradeScreen(partner);
				} else {
					ActionSender.sendString(partner, "Waiting for other player...", 335, 38);
					ActionSender.sendString(trader, "The other player has accepted", 335, 38);
				}
			}
			break;

		case STATE_TWO:
			if (pl.equals(trader)) {
				if (partnerDidAccept && traderDidAccept) {
					giveItems();
				} else {
					ActionSender.sendString(trader, "Waiting for other player...", 334, 33);
					ActionSender.sendString(partner, "The other player has accepted", 334, 33);
				}
			} else if (pl.equals(partner)) {
				if (partnerDidAccept && traderDidAccept) {
					giveItems();
				} else {
					ActionSender.sendString(partner, "Waiting for other player...", 334, 33);
					ActionSender.sendString(trader, "The other player has accepted", 334, 33);
				}
			}
			break;
		}

	}

	public void tradeFailed() {
		for (Item itemAtIndex : traderItemsOffered.getItems()) {
			if (itemAtIndex != null) {
				trader.getInventory().getContainer().add(itemAtIndex);
			}
		}
		for (Item itemAtIndex : partnerItemsOffered.getItems()) {
			if (itemAtIndex != null) {
				partner.getInventory().getContainer().add(itemAtIndex);
			}
		}
		trader.sendMessage("Trade declined.");
		partner.sendMessage("Trade declined.");
		trader.getSt().reset();
		partner.getSt().reset();
		traderItemsOffered.reset();
		partnerItemsOffered.reset();
		trader.getInventory().refresh();
		partner.getInventory().refresh();
		endSession(trader);
		endSession(partner);
	}

	public void endSession(Player player) {
		ActionSender.closeInter(trader);
		ActionSender.closeInter(partner);
		ActionSender.closeInventoryInterface(trader);
		ActionSender.closeInventoryInterface(partner);
		//ActionSender.sendLoginMasks(trader);
		//ActionSender.sendLoginMasks(partner);
		player.setTradeSession(null);
		player.setTradePartner(null);
	}

	private void giveItems() {
		if (!trader.getInventory().getContainer().hasSpaceFor(partnerItemsOffered)) {
			ActionSender.sendMessage(partner, "The other player does not have enough space in their inventory.");
			ActionSender.sendMessage(trader, "You do not have enough space in your inventory.");
			tradeFailed();
			return;
		} else if (!partner.getInventory().getContainer().hasSpaceFor(traderItemsOffered)) { 
			ActionSender.sendMessage(trader, "The other player does not have enough space in their inventory.");
			ActionSender.sendMessage(partner, "You do not have enough space in your inventory.");
			tradeFailed();
			return;
		}
		for (Item itemAtIndex : traderItemsOffered.getItems()) {
			if (itemAtIndex != null) {
				partner.getInventory().getContainer().add(itemAtIndex);
			}
		}
		for (Item itemAtIndex : partnerItemsOffered.getItems()) {
			if (itemAtIndex != null) {
				trader.getInventory().getContainer().add(itemAtIndex);
			}
		}
		trader.sendMessage("Trade completed.");
		partner.sendMessage("Trade completed.");
		trader.getSt().reset();
		partner.getSt().reset();
		Logger.writeTradeLog(trader, partner, traderItemsOffered, partnerItemsOffered);
		traderItemsOffered.reset();
		partnerItemsOffered.reset();
		partner.getInventory().refresh();
		trader.getInventory().refresh();
		endSession(trader);
		endSession(partner);
	}

	public Container getPlayerItemsOffered(Player p) {
		return (p.equals(trader) ? traderItemsOffered : partnerItemsOffered);
	}
	
	public String formatPrice(long price) {
		return NumberFormat.getInstance().format(price);
	}
	

	public enum TradeState {
		STATE_ONE,
		STATE_TWO;
	}
}
