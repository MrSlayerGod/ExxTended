package org.dementhium.model.player;

import java.text.NumberFormat;
import java.util.ArrayList;

import org.dementhium.event.Tickable;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.definition.PriceDefinition;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;


public class Shop {

	public static final int[] unsellableItems = new int[] { 6529 };
	private static final int RESTOCK_TIME = 70;

	private int STORE_SIZE = 40;
	Container shop = new Container(STORE_SIZE, true);
	private ArrayList<Player> playersViewing = new ArrayList<Player>();
	private boolean generalStore = false;
	private boolean donatorPointShop = false;
	private final Object[] params = new Object[]{"Sell 50", "Sell 10", "Sell 5", "Sell 1", "Value",  -1, 1, 7, 4, 93, 40697856};

	@SuppressWarnings("unused")
	private int shopId = 0;

	public Shop(int id, boolean isGeneralStore, int[] items) {
		this.shopId = id;
		this.generalStore = isGeneralStore;
		for (int itemSlot = 0; itemSlot < items.length; itemSlot++) {
			shop.set(shop.getFreeSlot(), new Item(
					items[itemSlot], 1000));
		}
		startRestocking();
	}

	 private void startRestocking() {
       /* World.getWorld().submit(new Tickable(RESTOCK_TIME) {

			@Override
			public void execute() {
                for (int i = 0; i < shop.getSize(); i++) {
                    if (shop.get(i) != null) {
                        shop.add(new Item(shop.get(i).getId(), 1));
                    }
                }
                update();
			}
        });*/

    }
	public void open(Player player) {
		ActionSender.sendConfig(player, 118, 3);
		ActionSender.sendConfig(player, 1496, 553);
		ActionSender.sendConfig(player, 532, 6529);
		//ActionSender.sendBConfig(player, 199, -1);
		ActionSender.sendInterface(player, 620);
		ActionSender.sendInventoryInterface(player, 621);
		ActionSender.sendClientScript(player, 149, params, "IviiiIsssss");
		ActionSender.sendAMask(player, 0, 27, 621, 0, 36, 1086);
		ActionSender.sendAMask(player, 0, 12, 620, 26, 0, 1150);
		ActionSender.sendAMask(player, 0, 240, 620, 25, 0, 1150);
		ActionSender.sendItems(player, 93, player.getInventory().getContainer(), false);
		ActionSender.sendItems(player, 3, shop, false);
		for (int index = 0; index < 40; index++) {
			if (shop.get(index) != null) {
				//ActionSender.sendBConfig(player, 946 + index, 2048);
			}
		}
	}


	public void addPlayer(Player player) {
		playersViewing.add(player);
	}

	public void removePlayer(Player player) {
		playersViewing.remove(player);
	}

	public void handleOption(Player p, int interfaceId, int buttonId,
			int buttonId2, int packetId, int itemIdd) {
		switch (interfaceId) {
		case 620:
			switch (buttonId) {
			case 18:
				removePlayer(p);
				p.setAttribute("shopId", null);
				break;
			case 25:
			case 26:
				if (buttonId2 > 0) {
					buttonId2 /= 6;
				}
				switch (packetId) {
				case 79:
					int price = PriceDefinition.forId(shop.get(buttonId2).getId()).getVotePrice();
					p.sendMessage(shop.get(buttonId2).getDefinition().getName()+": currently costs "+Misc.formatInteger(price)+" tokkul. Right-click item to buy.");
					break;
				case 24:
					buyItem(p, shop.get(buttonId2).getId(), 1);
					break;
				case 48:
					buyItem(p, shop.get(buttonId2).getId(), 5);
					break;
				case 40:
					buyItem(p, shop.get(buttonId2).getId(), 10);
					break;
				case 13:
					buyItem(p, shop.get(buttonId2).getId(), 50);
					break;
				case 55:
					buyItem(p, shop.get(buttonId2).getId(), 500);
					break;
					//case 14:
					//ActionSender.sendMessage(p, ItemDefinition.forId( shop.get(buttonId2).getId()).get)
				}
				break;
			}
			break;
		case 621:
			Item definition = p.getInventory().getContainer().get(buttonId2);
			int itemId = definition.getId();
			switch (buttonId) {
			case 0:
				switch (packetId) {
				case 24:
					sellItem(p, itemId, 1);
					break;
				case 48:
					sellItem(p, itemId, 5);
					break;
				case 40:
					sellItem(p, itemId, 10);
					break;
				case 13:
					sellItem(p, itemId, 50);
					break;
				case 55:
					sellItem(p, itemId, 500);
					break;
				}
			}
			break;
		}
	}

	private void sellItem(Player p, int itemId, int amount) {
		ActionSender.sendMessage(p, "You can't sell items to this shop.");
		/*if (ItemDefinition.forId(itemId).isTradeable()) {
			int price = (int) ((ItemDefinition.forId(itemId).getExchangePrice() / 1.6) * amount);
			if (!p.getInventory().contains(itemId)) {
				return;
			}
			if (!p.getInventory().hasRoomFor(6529, price)) {
				ActionSender.sendMessage(p, "Not enough space in your inventory.");
				return;
			}
			if (p.getInventory().getContainer().getNumberOff(itemId) < amount) {
				if (ItemDefinition.forId(itemId).isNoted()
						|| ItemDefinition.forId(itemId).isStackable()) {
					amount = p.getInventory().lookup(itemId).getAmount();
				} else {
					amount = p.getInventory().getContainer().getNumberOff(itemId);
				}
				price = (int) ((int) ItemDefinition.forId(itemId).getExchangePrice() / 1.6 * amount);
				ActionSender.sendMessage(p, "You don't have enough to complete this offer.");
			}
			if (shop.freeSlots() <= 0) {
				ActionSender.sendMessage(p, "The shop is full.");
			}
			if (!shop.contains(new Item(itemId, 1)) && !generalStore) {
				ActionSender.sendMessage(p, "You cannot sell that item to this shop.");
				return;
			}
			shop.add(new Item(itemId, amount));
			p.getInventory().deleteItem(itemId, amount);
			p.getInventory().addItem(6529, price);
			update();
		} else {
			ActionSender.sendMessage(p, "You cannot sell that item to this shop.");
		}*/
	}

	private void update() {
		for (Player player : playersViewing) {
			ActionSender.sendItems(player, 93, player.getInventory().getContainer(),
					false);
			ActionSender.sendItems(player, 3, shop, false);
			for (int index = 0; index < 40; index++) {
				if (shop.get(index) != null) {
					//ActionSender.sendBConfig(player, 946 + index, 2048);
				}
			}
		}
	}

	private void buyItem(Player p, int id, int amount) {
		for (int i = 0; i < p.donorItems.length; i++) {
			if (id == p.donorItems[i] && p.getRights() < 2 && !p.getGroup().equalsIgnoreCase("Donator") && !p.getGroup().equalsIgnoreCase("Premium") && !p.getGroup().equalsIgnoreCase("Super")) {
				p.sendMessage("You need to be atleast a donator to buy this item.");
				return;
			}
		}
		for (int i = 0; i < p.premItems.length; i++) {
			if (id == p.premItems[i] && p.getRights() < 2 && !p.getGroup().equalsIgnoreCase("Premium") && !p.getGroup().equalsIgnoreCase("Super")) {
				p.sendMessage("You need to be atleast a premium donator to buy this item.");
				return;
			}
		}
		for (int i = 0; i < p.superItems.length; i++) {
			if (id == p.superItems[i] && p.getRights() < 2 && !p.getGroup().equalsIgnoreCase("Super")) {
				p.sendMessage("You need to be a super donator to buy this item.");
				return;
			}
		}
		amount = 1; // Make it 1 for now until Armo fixes the dupes.
		PriceDefinition def = PriceDefinition.forId(id); // Armo's price definition class
		int tokkulPrice = def.getVotePrice();
		//if (ItemDefinition.forId(id).isTradeable()) {
			Item item = new Item(id, 1);
			int price = (int) ((tokkulPrice) * amount);
			if (p.getInventory().getFreeSlots() < amount && !(item.getDefinition().isNoted() || item.getDefinition().isStackable())) {
				ActionSender.sendMessage(p, "Not enough space in your inventory.");
				if (p.getInventory().getFreeSlots() == 0) {
					return;
				}
				amount = p.getInventory().getFreeSlots();
				price = tokkulPrice * amount;
			}
			if (shop.getNumberOff(id) < amount) {
				ActionSender.sendMessage(p, "The shop has run out of stock of that item.");
				amount = shop.getNumberOff(id);
				price = tokkulPrice * amount;
			}
			if (p.getInventory().getContainer().getNumberOff(6529) < price) {
				ActionSender.sendMessage(p, "You do not have enough tokkul for that many.");
				amount = p.getInventory().getContainer().getNumberOff(6529) / tokkulPrice;
				price = tokkulPrice * amount;
				if (price < tokkulPrice) {
					return;
				}
			}
			p.getInventory().deleteItem(6529, price);
			p.getInventory().addItem(id, amount);
			shop.remove(new Item(id, amount));
			if (!generalStore) {
				if (shop.getNumberOff(id) == 0) {
					shop.add(new Item(id, 1));
				}
			}
			update();
		//} else {
			//ActionSender.sendMessage(p, "You cannot buy that item from this shop");
		//}
	}

	public String formatPrice(int price) {
		return NumberFormat.getInstance().format(price);
	}

}