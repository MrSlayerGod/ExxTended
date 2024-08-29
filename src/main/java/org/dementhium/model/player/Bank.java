package org.dementhium.model.player;

import java.util.ArrayList;
import java.util.Collections;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.net.ActionSender;

public class Bank {

	public static int SIZE = 468;
	public static int TAB_SIZE = 11;

	private final Container bank = new Container(SIZE, true);
	private final Player player;
	private final int[] tabStartSlot = new int[TAB_SIZE];
	private int[] bankPin = new int[4];
	private boolean pinSet;
	private ArrayList<Integer> numbers = new ArrayList<Integer>();
	private int[] pinButton = new int[10];
	private int[] enteredPin = new int[4];
	private int index = 0;
	private boolean hasEnteredPin;

	public static void handleButton(final Player p, int buttonId, int buttonId2, int buttonId3) {
		if(buttonId == 30){
			p.getBank().setHasEnteredPin(false);
			resetPinScreen(p);
			return;
		}
		p.getBank().enteredPin[p.getBank().index] = p.getBank().pinButton[buttonId - 1];
		refreshPinScreen(p);
		p.getBank().index++;
		ActionSender.sendString(p, 13, 20+p.getBank().index, "*");
		if(p.getBank().index == 4){
			for(int i = 0; i < 4; i++){
				if(p.getBank().getPin(i) == p.getBank().enteredPin[i]){
					p.getBank().index++;
				}
			}
			if(p.getBank().index == 8){
				p.getBank().setHasEnteredPin(true);
				resetPinScreen(p);
				p.getBank().openBank();
				return;
			}else{
				p.getBank().setHasEnteredPin(false);
				resetPinScreen(p);
				p.sendMessage("You have entered the wrong pin number. Try again.");
				ActionSender.closeInter(p);
				return;
			}
		}
		//System.out.println("[Bank Pin Unhandled] Button ID: "+buttonId+" ButtonId2: "+buttonId2+" ButtonId3: "+buttonId3);
	}

	public void deleteAll(int item) {
		bank.removeAll(new Item(item));
		refresh();
	}

	public void enterPin(){
		for(int i = 0; i < 10; i++){
			player.getBank().numbers.add(i);
		}
		Collections.shuffle(player.getBank().numbers);
		for(int i = 0; i < 10; i++){
			player.getBank().pinButton[i] = player.getBank().numbers.get(i);
		}
		for(int i = 0; i < 10; i++){
			ActionSender.sendString(player, 13, 11 + i, ""+player.getBank().pinButton[i]);
		}
		ActionSender.sendString(player, 13, 31, "");//Your pin will expire in blah blah blah days
		ActionSender.sendInterface(player, 13);
	}

	public static void refreshPinScreen(Player p){
		Collections.shuffle(p.getBank().numbers);
		for(int i = 0; i < 10; i++){
			p.getBank().pinButton[i] = p.getBank().numbers.get(i);
		}
		for(int i = 0; i < 10; i++){
			ActionSender.sendString(p, 13, 11 + i, ""+p.getBank().pinButton[i]);
		}
	}

	public static void resetPinScreen(Player p){
		p.getBank().index = 0;
		p.getBank().numbers.clear();
		for(int i2 = 0; i2 < 4; i2++){
			p.getBank().enteredPin[i2] = -1;
		}
		p.setAttribute("bankScreen", 0);
	}

	public Bank(Player player) {
		this.player = player;
		if(!pinSet){
			for(int i = 0; i < 4; i++) {
				bankPin[i] = -1;
			}
		}
	}

	public void openBank() {
		if(!hasEnteredPin && pinSet){
			enterPin();
			player.setAttribute("bankScreen", 1);
			return;
		}
		player.setAttribute("bankScreen", 2);
		player.setAttribute("currentTab", 10);
		ActionSender.sendConfig2(player, 563, 4194304);
		ActionSender.sendConfig(player, 1248, -2013265920);
		ActionSender.sendInterface(player, 762);
		ActionSender.sendInventoryInterface(player, 763);
		ActionSender.sendAMask(player, 0, 516, 762, 92, 40, 1278);
		ActionSender.sendAMask(player, 0, 27, 763, 0, 36, 1150);
		ActionSender.sendBlankClientScript(player, 1451);
		ActionSender.sendItems(player, 31, player.getInventory().getContainer(), false);
		ActionSender.sendItems(player, 95, bank, false);
		sendTabConfig();
	}

	public void addItem(int itemId, int amount, boolean b) {
		int player_amount = amount;
		int currentTab = (Integer) player.getAttribute("currentTab", 1);
		if(player_amount < amount){
			amount = player_amount;
		}
		Item item = new Item(itemId, amount);
		int index = bank.indexOf(item);
		if(index > -1) {
			Item item2 = bank.get(index);
			if(item2 != null) {
				if(item2.getId() == item.getId()) {
					bank.set(index, new Item(item.getId(), item2.getAmount() + amount));
				}
			}
		} else {
			int freeSlot;
			if(currentTab == 10) {
				freeSlot = bank.getFreeSlot();
			} else {
				freeSlot = tabStartSlot[currentTab] + getItemsInTab(currentTab);
			}
			if(item.getAmount() > 0) {
				if(currentTab != 10) {
					insert(bank.getFreeSlot(), freeSlot);
					increaseTabStartSlots(currentTab);
				}
				bank.set(freeSlot, new Item(item.getId(), amount));
			}
		}
		refresh();
	}

	public void addItem(int slot, int amount) {
		Item item = player.getInventory().get(slot);
		int player_amount = player.getInventory().getContainer().getNumberOf(item);
		int currentTab = (Integer) player.getAttribute("currentTab", 1);
		if(item == null) {
			return;
		}
		if(player_amount < amount){
			amount = player_amount;
		}
		if(item.getDefinition().isNoted() && !(new Item(item.getId() - 1).getDefinition().isNoted()) && new Item(item.getId() - 1).getDefinition().getName().equalsIgnoreCase(new Item(item.getId()).getDefinition().getName())) {
			item = new Item(item.getId() - 1, item.getAmount());
			player.getInventory().deleteItem(item.getId() + 1, amount, slot);
		} else {
			player.getInventory().deleteItem(item.getId(), amount, slot);
		}
		int index = bank.indexOf(item);
		if(index > -1) {
			Item item2 = bank.get(index);
			if(item2 != null) {
				if(item2.getId() == item.getId()) {
					bank.set(index, new Item(item.getId(), item2.getAmount() + amount));
				}
			}
		} else {
			int freeSlot;
			if(currentTab == 10) {
				freeSlot = bank.getFreeSlot();
			} else {
				freeSlot = tabStartSlot[currentTab] + getItemsInTab(currentTab);
			}
			if(item.getAmount() > 0) {
				if(currentTab != 10) {
					insert(bank.getFreeSlot(), freeSlot);
					increaseTabStartSlots(currentTab);
				}
				bank.set(freeSlot, new Item(item.getId(), amount));
			}
		}
		refresh();
	}

	public void addItemEquip(int slot, int amount) {
		Item item = player.getEquipment().get(slot);
		int player_amount = player.getEquipment().getEquipment().getNumberOf(item);
		int currentTab = (Integer) player.getAttribute("currentTab", 1);
		if(item == null) {
			return;
		}
		if(player_amount < amount){
			amount = player_amount;
		}
		if(item.getDefinition().isNoted() && !(new Item(item.getId() - 1).getDefinition().isNoted()) && new Item(item.getId() - 1).getDefinition().getName().equalsIgnoreCase(new Item(item.getId()).getDefinition().getName())) {
			item = new Item(item.getId() - 1, item.getAmount());
			player.getEquipment().deleteItem(item.getId() + 1, amount);
		} else {
			player.getEquipment().deleteItem(item.getId(), amount);
		}
		int index = bank.indexOf(item);
		if(index > -1) {
			Item item2 = bank.get(index);
			if(item2 != null) {
				if(item2.getId() == item.getId()) {
					bank.set(index, new Item(item.getId(), item2.getAmount() + amount));
				}
			}
		} else {
			int freeSlot;
			if(currentTab == 10) {
				freeSlot = bank.getFreeSlot();
			} else {
				freeSlot = tabStartSlot[currentTab] + getItemsInTab(currentTab);
			}
			if(item.getAmount() > 0) {
				if(currentTab != 10) {
					insert(bank.getFreeSlot(), freeSlot);
					increaseTabStartSlots(currentTab);
				}
				bank.set(freeSlot, new Item(item.getId(), amount));
			}
		}
		refresh();
	}

	public void refresh() {
		ActionSender.sendItems(player, 95, bank, false);
		sendTabConfig();
	}


	public void removeItem(int slot, int amount) {
		if (slot < 0 || slot > Bank.SIZE || amount <= 0) {
			return;
		}
		Item item = bank.get(slot);
		Item item2 = bank.get(slot);
		Item item3 = bank.get(slot);
		int tabId = getTabByItemSlot(slot);
		if (item == null) {
			return;
		}
		if (amount > item.getAmount()) {
			item = new Item(item.getId(), item.getAmount());
			item2 = new Item(item.getId() + 1, item.getAmount());
			item3 = new Item(item.getId(), item.getAmount());
			if (noting()) {
				if (item2.getDefinition().isNoted() && !(new Item(item2.getId() - 1).getDefinition().isNoted()) && new Item(item2.getId() - 1).getDefinition().getName().equalsIgnoreCase(new Item(item2.getId()).getDefinition().getName())) {
					item = new Item(item.getId() + 1, item.getAmount());
				} else {
					player.sendMessage("You cannot withdraw this item as a note.");
					item = new Item(item.getId(), item.getAmount());
				}
			}
		} else {
			item = new Item(item.getId(), amount);
			item2 = new Item(item.getId(), amount);
			item3 = new Item(item.getId(), amount);
			if (noting()) {
				item2 = new Item(item.getId() + 1, item.getAmount());
				if (item2.getDefinition().isNoted() && !(new Item(item2.getId() - 1).getDefinition().isNoted()) && new Item(item2.getId() - 1).getDefinition().getName().equalsIgnoreCase(new Item(item2.getId()).getDefinition().getName())) {
					item = new Item(item.getId() + 1, item.getAmount());
				} else {
					player.sendMessage("You cannot withdraw this item as a note.");
					item = new Item(item.getId(), item.getAmount());
					return;
				}
			}
		}
		if (amount > player.getInventory().getFreeSlots() && !item3.getDefinition().isStackable() && !noting()) {
			item = new Item(item.getId(), player.getInventory().getFreeSlots());
			item2 = new Item(item2.getId(), player.getInventory().getFreeSlots());
			item3 = new Item(item3.getId(), player.getInventory().getFreeSlots());
		}
		if (bank.contains(item3)) {
			if (player.getInventory().getFreeSlots() <= 0) {
				player.sendMessage("Not enough space in your inventory.");
			} else {
				if (noting() && !item.getDefinition().isNoted()) {
					player.getInventory().addItem(item.getId(), item.getAmount());
					bank.remove(item3);
				} else {
					player.getInventory().addItem(item.getId(), item.getAmount());
					bank.remove(item3);
				}
			}
		}
		if (get(slot) == null) {
			decreaseTabStartSlots(tabId);
		}
		bank.shift();
		refresh();
	}

	public boolean noting() {
		return player.getAttribute("noting") == Boolean.TRUE;
	}

	public void bankInv() {
		Item item = null;
		for(int slot = 0; slot < Inventory.SIZE; slot++) {
			item = player.getInventory().getContainer().get(slot);
			if(item == null) {
				continue;
			}
			if(player.getInventory().getContainer().getNumberOf(item) == 1) {
				addItem(slot, player.getInventory().getContainer().getNumberOf(item));
			} else {
				addItem(slot, player.getInventory().getContainer().getNumberOf(item));
			}
		}
		player.getInventory().getContainer().clear();
		refresh();
		player.getInventory().refresh();
	}

	public void bankEquip() {
		Item item = null;
		for(int slot = 0; slot < Equipment.SIZE; slot++) {
			item = player.getEquipment().getEquipment().get(slot);
			if(item == null) {
				continue;
			}
			if(player.getEquipment().getEquipment().getNumberOf(item) == 1) {
				addItemEquip(slot, player.getEquipment().getEquipment().getNumberOf(item));
			} else {
				addItemEquip(slot, player.getEquipment().getEquipment().getNumberOf(item));
			}
		}
		player.getEquipment().getEquipment().clear();
		refresh();
		player.getEquipment().refresh();
	}

	public void bankBob() {
		bank.addAll(player.getBob().getContainer());
		player.getBob().getContainer().clear();
		refresh();
		player.getBob().refresh();
	}

	public boolean contains(int item, int amount) {
		return bank.contains(new Item(item, amount));
	}

	public boolean contains(int item) {
		return bank.contains(new Item(item));
	}

	public Container getContainer() {
		return bank;
	}

	public Item get(int slot) {
		return bank.get(slot);
	}

	public void set(int slot, Item item) {
		bank.set(slot, item);
	}

	public void increaseTabStartSlots(int startId) {
		for(int i = startId+1; i < tabStartSlot.length; i++) {
			tabStartSlot[i]++;
		}
	}

	public void decreaseTabStartSlots(int startId) {
		if(startId == 10)
			return;
		for(int i = startId + 1; i < tabStartSlot.length; i++) {
			tabStartSlot[i]--;
		}
		if(getItemsInTab(startId) == 0) {
			collapseTab(startId);
		}
	}

	public void insert(int fromId, int toId) {
		Item temp = bank.getItems()[fromId];
		if(toId > fromId) {
			for(int i = fromId; i < toId; i++) {
				set(i, get(i+1));
			}
		} else if(fromId > toId) {
			for(int i = fromId; i > toId; i--) {
				set(i, get(i-1));
			}	
		}
		set(toId, temp);
	}

	public int getItemsInTab(int tabId) {
		return tabStartSlot[tabId+1] - tabStartSlot[tabId];
	}

	public int getTabByItemSlot(int itemSlot) {
		int tabId = 0;
		for(int i = 0; i < tabStartSlot.length; i++) {
			if(itemSlot >= tabStartSlot[i]) {
				tabId = i;
			}
		}
		return tabId;
	}

	public void collapseTab(int tabId) {
		int size = getItemsInTab(tabId);
		Item[] tempTabItems = new Item[size];
		for(int i = 0; i < size; i++) {
			tempTabItems[i] = get(tabStartSlot[tabId] + i);
			set(tabStartSlot[tabId] + i, null);
		}
		bank.shift();
		for(int i = tabId; i < tabStartSlot.length-1; i++) {
			tabStartSlot[i] = tabStartSlot[i+1] - size;
		}
		tabStartSlot[10] = tabStartSlot[10] - size;
		for(int i = 0; i < size; i++) {
			int slot = bank.getFreeSlot();
			set(slot, tempTabItems[i]);
		}
	}

	public void sendTabConfig() {
		int config = 0; 
		config += getItemsInTab(2);
		config += getItemsInTab(3) * 1024;
		config += getItemsInTab(4) * 1048576;
		ActionSender.sendConfig2(player, 1246, config);
		config = 0;
		config += getItemsInTab(5);
		config += getItemsInTab(6) * 1024;
		config += getItemsInTab(7) * 1048576;
		ActionSender.sendConfig2(player, 1247, config);
		int tab = (Integer) player.getAttribute("currentTab", 10);
		config = -2013265920;
		config += (134217728 * (tab == 10 ? 0 : tab - 1));
		config += getItemsInTab(8);
		config += getItemsInTab(9) * 1024;
		ActionSender.sendConfig2(player, 1248, config);
	}

	public int getArrayIndex(int tabId) {
		if(tabId == 61 || tabId == 73) {
			return 10;
		}
		int base = 59;
		for(int i = 2; i < 10; i++) {
			if(tabId == base) {
				return i;
			}
			base -= 2;
		}
		base = 74;
		for(int i = 2; i < 10; i++) {
			if(tabId == base) {
				return i;
			}
			base++;
		}
		//Should not happen
		return -1;
	}

	public int[] getTab() {
		return tabStartSlot;
	}

	public int getPin(int i){
		return bankPin[i];
	}

	public void setPin(int index, int value){
		bankPin[index] = value;
	}

	public boolean getPinSet(){
		return pinSet;
	}

	public void setPinSet(boolean value){
		pinSet = value;
	}

	public boolean getHasEnteredPin(){
		return hasEnteredPin;
	}

	public void setHasEnteredPin(boolean value){
		hasEnteredPin = value;
	}

}
