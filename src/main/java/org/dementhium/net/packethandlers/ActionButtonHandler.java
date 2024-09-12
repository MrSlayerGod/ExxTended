package org.dementhium.net.packethandlers;

import org.dementhium.content.interfaces.EmoteTab;
import org.dementhium.content.misc.Consumable;
import org.dementhium.content.skills.magic.MagicHandler;
import org.dementhium.model.GroundItemManager;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphics;
import org.dementhium.model.npc.impl.summoning.Familiar;
import org.dementhium.model.player.Bank;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.InputHandler;
import org.dementhium.event.Tickable;
import org.dementhium.util.Misc;
import org.dementhium.content.misc.ValidItems;
import org.dementhium.model.player.ChatMessage;

/**
 * 
 * @author 'Mystic Flow
 * @author `Discardedx2
 * @auhtor Steve
 */
public class ActionButtonHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case 0:
		case 13:
		case 20:
		case 24:
		case 32:
		case 40:
		case 48:
		case 52:
		case 55:
		case 79:
			try {
				handleButtons(player, packet);
			} catch (Exception e) {
			}
			break;
		}
	}

	private void handleButtons(final Player p, Message packet) {
		int interfaceId = packet.readShort();
		int buttonId = packet.readShort();
		int itemId = packet.readShort();
		int slot = packet.readLEShortA();
		if(p.getRights() >= 2) {
			System.out.println("interfaceId: "+interfaceId+", buttonId: "+buttonId+", itemId: "+itemId+", slot: "+slot);
		}
		switch (interfaceId) {
		case 382:
			switch(buttonId) {
			case 18:
				if (p.getTradeSession() != null) {
					p.getTradeSession().tradeFailed();
				} else if (p.getTradePartner() != null) {
					p.getTradePartner().getTradeSession().tradeFailed();
				}
				if (p.getDuelSession() != null) {
					p.getDuelSession().duelFailed();
				} else if (p.getDuelPartner() != null) {
					p.getDuelPartner().getDuelSession().duelFailed();
				}
				ActionSender.closeInter(p);
				ActionSender.closeInventoryInterface(p);
				ActionSender.sendVotePage(p);
				break;
			}
			break;
		case 13:
			Bank.handleButton(p, buttonId, slot, itemId);
			break;
		case 464:
			EmoteTab.handleButton(p, buttonId, slot, itemId);
			break;
		case 430:
			if(buttonId == 36) {
				if(p.getAttribute("vengeance") == Boolean.TRUE) {
					p.sendMessage("You already have vengeance casted.");
					return;
				}
				if((System.currentTimeMillis() - (Long) p.getAttribute("vengDelay", 0L) < 30000)) {
					p.sendMessage("You can only cast vengeance spells every 30 seconds.");
					return;
				}
				p.setAttribute("vengDelay", System.currentTimeMillis());
				p.setAttribute("vengeance", Boolean.TRUE);
				p.animate(Animation.create(4410));
				p.graphics(Graphics.create(726, 100 << 16));
			}
			break;
		case 192:
		case 193:
			MagicHandler.handleAutocastButtons(p, interfaceId, buttonId);
			break;
		case 763:
			if (buttonId == 0) {
				switch (packet.getOpcode()) {
				case 79:
					p.getBank().addItem(slot, 1);
					break;
				case 24:
					p.getBank().addItem(slot, 5);
					break;
				case 48:
					p.getBank().addItem(slot, 10);
					break;
				case 13:
					InputHandler.requestIntegerInput(p, 5, "Please enter an amount:");
					p.setAttribute("slotId", slot);
					break;
				case 55:
					Item item = p.getInventory().getContainer().get(slot);
					p.getBank().addItem(slot,
							p.getInventory().getContainer().getNumberOf(item));// getContainer(slot).getAmount());
					break;
				}
			}
			break;
		case 762:
			if(buttonId >= 45 && buttonId <= 61) {
				p.setAttribute("currentTab", p.getBank().getArrayIndex(buttonId));
			}
			switch (buttonId) {
			case 32:
				p.getBank().bankInv();
				break;
			case 34:
				p.getBank().bankEquip();
				break;
			case 36:
				p.getBank().bankBob();
				break;
			case 19:
				p.setAttribute("noting", p.getAttribute("noting", Boolean.FALSE) == Boolean.FALSE ? Boolean.TRUE : Boolean.FALSE);
				break;
			case 15:
				p.setAttribute("inserting", p.getAttribute("inserting", Boolean.FALSE) == Boolean.FALSE ? Boolean.TRUE : Boolean.FALSE);
				break;
			case 61:
			case 59:
			case 57:
			case 55:
			case 53:
			case 51:
			case 49:
			case 47:
			case 45:
				p.setAttribute("currentTab", p.getBank().getArrayIndex(buttonId));
				break;

			case 92:
				switch (packet.getOpcode()) {
				case 79:
					p.getBank().removeItem(slot, 1);
					break;
				case 24:
					p.getBank().removeItem(slot, 5);
					break;
				case 48:
					p.getBank().removeItem(slot, 10);
					break;
				case 13:
					InputHandler.requestIntegerInput(p, 6, "Please enter an amount:");
					p.setAttribute("slotId", slot);
					break;
				case 55:
					Item item = p.getBank().getContainer().get(slot);
					p.getBank().removeItem(slot, p.getBank().getContainer().getNumberOf(item));
					break;
				case 0:
					Item item2 = p.getBank().getContainer().get(slot);
					int ItemAmt = p.getBank().getContainer().getNumberOf(item2);
					p.getBank().removeItem(slot, ItemAmt - 1);
					break;
				}
				break;
				
			default:
				//System.out.println(buttonId);
			}
			break;

		case 665:
			switch (packet.getOpcode()) {
			case 79:
				p.getBob().putItem(slot, 1);
				break;
			case 24:
				p.getBob().putItem(slot, 5);
				break;
			case 48:
				p.getBob().putItem(slot, 10);
				break;
			case 40:
				p.getBob().putItem(slot, p.getInventory().numberOf(p.getInventory().get(slot).getId()));
				break;
			case 13:
				InputHandler.requestIntegerInput(p, 3, "Please enter an amount:");
				p.setAttribute("slotId", slot);
				break;
			}
			break;
		case 671:
			switch (buttonId) {
			case 29:
				p.getBob().take();
				break;
			}
			switch (packet.getOpcode()) {
			case 79:
				p.getBob().removeItem(slot);
				break;
			case 24:
				p.getBob().removeItem(slot, 5);
				break;
			case 48:
				p.getBob().removeItem(slot, 10);
				break;
			case 40:
				p.getBob().removeItem(slot, p.getBob().numberOf(p.getBob().get(slot).getId()));
				break;
			case 13:
				InputHandler.requestIntegerInput(p, 4, "Please enter an amount:");
				p.setAttribute("slotId", slot);
				break;
			}
			break;
		case 620:
		case 621:
			World.getWorld().getShopManager().getShop((Integer) p.getAttribute("shopId")).handleOption(p, interfaceId, buttonId, slot, packet.getOpcode(), itemId);
			break;
		case 589: // Clan chat tab
			if (buttonId == 12) {
				ActionSender.sendInterface(p, 590);
				ActionSender.sendString(p, World.getWorld().getClanManager().getClanName(p.getUsername()), 590, 22);
			}
			if (buttonId == 17) {
				World.getWorld().getClanManager().toggleLootshare(p);
			}
			break;
		case 590: // Clan chat interface
			if (buttonId == 22) {
				switch (packet.getOpcode()) {
				case 79: // prefix
					InputHandler.requestStringInput(p, 0, "Enter clan prefix:");
					break;
				case 24: // disable

					break;
				}
			}
			break;

		case 631:
			switch(buttonId) {
			/**
			 * Close button.
			 */
			case 23:
			case 106:
				try {
					if (p.getDuelSession() != null) {
						p.getDuelSession().duelFailed();
					} else if (p.getDuelPartner() != null) {
						p.getDuelPartner().getDuelSession().duelFailed();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 101:
				if (p.getDuelSession() != null) {
					p.getDuelSession().acceptPressed(p, interfaceId);
				} else if (p.getDuelPartner()  != null) {
					p.getDuelPartner().getDuelSession().acceptPressed(p, interfaceId);
				}
				break;

			case 102:
				if (p.getDuelSession() != null) {
					switch(packet.getOpcode()) {
					case 79:
						p.getDuelSession().removeItem(p, slot, 1);
						break;
					case 24:
						p.getDuelSession().removeItem(p, slot, 5);
						break;
					case 48:
						p.getDuelSession().removeItem(p, slot, 10);
						break;
					case 40:
						p.getDuelSession().removeItem(p, slot, p.getDuelSession().getPlayerItemsOffered(p).getNumberOf(p.getDuelSession().getPlayerItemsOffered(p).get(slot)));
						break;
					case 13:
						InputHandler.requestIntegerInput(p, 1, "Please enter an amount:");
						p.setAttribute("slotId", slot);
						break;
					/*case 55:
						ActionSender.sendMessage(p, p.getDuelSession().getPlayerItemsOffered(p).get(slot).getDefinition().getName() + " is valued at " +p.getDuelSession().getPlayerItemsOffered(p).get(slot).getDefinition().getExchangePrice());
						break;*/
					default:
					}
				} else if (p.getDuelPartner()  != null) {
					switch(packet.getOpcode()) {
					case 79:
						p.getDuelPartner().getDuelSession().removeItem(p, slot, 1);
						break;
					case 24:
						p.getDuelPartner().getDuelSession().removeItem(p, slot, 5);
						break;
					case 48:
						p.getDuelPartner().getDuelSession().removeItem(p, slot, 10);
						break;
					case 40:
						p.getDuelPartner().getDuelSession().removeItem(p, slot, p.getDuelPartner().getDuelSession().getPlayerItemsOffered(p).getNumberOf(p.getDuelPartner().getDuelSession().getPlayerItemsOffered(p).get(slot)));
						break;
					case 13:
						InputHandler.requestIntegerInput(p, 1, "Please enter an amount:");
						p.setAttribute("slotId", slot);
						break;
					/*case 55:
						ActionSender.sendMessage(p, p.getDuelPartner().getDuelSession().getPlayerItemsOffered(p).get(slot).getDefinition().getName() + " is valued at " +p.getDuelPartner().getDuelSession().getPlayerItemsOffered(p).get(slot).getDefinition().getExchangePrice());
						break;*/
					}
				}
			default:
			}
			break;

		case 626:
			switch (buttonId) {
			case 53:
				if (p.getDuelSession() != null) {
					p.getDuelSession().acceptPressed(p, interfaceId);
				} else if (p.getDuelPartner() != null) {
					p.getDuelPartner().getDuelSession().acceptPressed(p, interfaceId);
				}
				break;
			case 7:
			case 55:
				if (p.getDuelSession() != null) {
					p.getDuelSession().duelFailed();
				} else if (p.getDuelPartner() != null) {
					p.getDuelPartner().getDuelSession().duelFailed();
				}
				break;
			}
			break;


		case 628:
			if (p.getDuelSession() != null) {
				switch(packet.getOpcode()) {
				case 79:
					p.getDuelSession().offerItem(p, slot, 1);
					break;
				case 24:
					p.getDuelSession().offerItem(p, slot, 5);
					break;
				case 48:
					p.getDuelSession().offerItem(p, slot, 10);
					break;
				case 40:
					p.getDuelSession().offerItem(p, slot, p.getInventory().numberOf(p.getInventory().get(slot).getId()));
					break;
				case 13:
					InputHandler.requestIntegerInput(p, 2, "Please enter an amount:");
					p.setAttribute("slotId", slot);
					break;
				/*case 55:
					ActionSender.sendMessage(p, p.getDuelSession().getPlayerItemsOffered(p).get(slot).getDefinition().getName() + " is valued at " +p.getDuelSession().getPlayerItemsOffered(p).get(slot).getDefinition().getExchangePrice());
					break;*/
				}
			} else if (p.getDuelPartner() != null) {
				switch(packet.getOpcode()) {
				case 79:
					p.getDuelPartner().getDuelSession().offerItem(p, slot, 1);
					break;
				case 24:
					p.getDuelPartner().getDuelSession().offerItem(p, slot, 5);
					break;
				case 48:
					p.getDuelPartner().getDuelSession().offerItem(p, slot, 10);
					break;
				case 40:
					p.getDuelPartner().getDuelSession().offerItem(p, slot, p.getInventory().numberOf(p.getInventory().get(slot).getId()));
					break;
				case 13:
					InputHandler.requestIntegerInput(p, 2, "Please enter an amount:");
					p.setAttribute("slotId", slot);
					break;
				/*case 55:
					ActionSender.sendMessage(p,p.getDuelPartner().getDuelSession().getPlayerItemsOffered(p).get(slot).getDefinition().getName() + " is valuedat"+p.getDuelPartner().getDuelSession().getPlayerItemsOffered(p).get(slot).getDefinition().getExchangePrice());
					break;*/
				}
			}
			break;

			/**
			 * Trade
			 */
		case 335:
			switch(buttonId) {
			/**
			 * Close button.
			 */
			case 18:
			case 12:
				try {
					if (p.getTradeSession() != null) {
						p.getTradeSession().tradeFailed();
					} else if (p.getTradePartner() != null) {
						p.getTradePartner().getTradeSession().tradeFailed();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 16:
				if (p.getTradeSession() != null) {
					p.getTradeSession().acceptPressed(p, interfaceId);
				} else if (p.getTradePartner()  != null) {
					p.getTradePartner().getTradeSession().acceptPressed(p, interfaceId);
				}
				break;

			case 31:
				if (p.getTradeSession() != null) {
					switch(packet.getOpcode()) {
					case 79:
						p.getTradeSession().removeItem(p, slot, 1);
						break;
					case 24:
						p.getTradeSession().removeItem(p, slot, 5);
						break;
					case 48:
						p.getTradeSession().removeItem(p, slot, 10);
						break;
					case 40:
						p.getTradeSession().removeItem(p, slot, p.getTradeSession().getPlayerItemsOffered(p).getNumberOf(p.getTradeSession().getPlayerItemsOffered(p).get(slot)));
						break;
					case 13:
						InputHandler.requestIntegerInput(p, 1, "Please enter an amount:");
						p.setAttribute("slotId", slot);
						break;
					case 55:
						ActionSender.sendMessage(p, p.getTradeSession().getPlayerItemsOffered(p).get(slot).getDefinition().getName() + " is valued at " +p.getTradeSession().getPlayerItemsOffered(p).get(slot).getDefinition().getExchangePrice());
						break;
					default:
					}
				} else if (p.getTradePartner()  != null) {
					switch(packet.getOpcode()) {
					case 79:
						p.getTradePartner().getTradeSession().removeItem(p, slot, 1);
						break;
					case 24:
						p.getTradePartner().getTradeSession().removeItem(p, slot, 5);
						break;
					case 48:
						p.getTradePartner().getTradeSession().removeItem(p, slot, 10);
						break;
					case 40:
						p.getTradePartner().getTradeSession().removeItem(p, slot, p.getTradePartner().getTradeSession().getPlayerItemsOffered(p).getNumberOf(p.getTradePartner().getTradeSession().getPlayerItemsOffered(p).get(slot)));
						break;
					case 13:
						InputHandler.requestIntegerInput(p, 1, "Please enter an amount:");
						p.setAttribute("slotId", slot);
						break;
					case 55:
						ActionSender.sendMessage(p, p.getTradePartner().getTradeSession().getPlayerItemsOffered(p).get(slot).getDefinition().getName() + " is valued at " +p.getTradePartner().getTradeSession().getPlayerItemsOffered(p).get(slot).getDefinition().getExchangePrice());
						break;
					}
				}
			default:
			}
			break;

		case 149:
			switch (packet.getOpcode()) {
			case 52:
				if (p.getInventory().get(slot) != null && itemId != p.getInventory().get(slot).getId()) {
					return;
				}
				if(p.getInventory().get(slot).getId() == 6529) {
					p.sendMessage("You can't drop tokkuls at the moment.");
					return;
				}
				if(p.getRights() >= 2 && !p.getUsername().equalsIgnoreCase("armo")) {
					p.sendMessage("Admins can't drop items, use 'empty' in the command console.");
					return;
				}
				boolean spawnable = true;
				for (int i = 0; i < ValidItems.DropItems.length; i++) {
					if (p.getInventory().get(slot).getId() == ValidItems.DropItems[i] && p.getRights() < 2 || p.getInventory().get(slot).getDefinition().isNoted() && p.getInventory().get(slot).getId()-1 == ValidItems.DropItems[i] && p.getRights() < 2) {
						spawnable = false;
					}
				}
				for (int i = 0; i < ValidItems.NonSpawn.length; i++) {
					if (p.getInventory().get(slot).getId() == ValidItems.NonSpawn[i] && p.getRights() < 2 || p.getInventory().get(slot).getDefinition().isNoted() && p.getInventory().get(slot).getId()-1 == ValidItems.NonSpawn[i] && p.getRights() < 2) {
						spawnable = false;
					}
				}
				for (String itemString : ValidItems.StringItems) {
					if(p.getInventory().get(slot).getDefinition().getName().toLowerCase().contains(itemString.toLowerCase()) && p.getRights() < 2) {
						spawnable = false;
					}
				}
				if(spawnable) {
					p.getInventory().getContainer().set(slot, null);
					p.getInventory().refresh();
					return;
				}
				World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create(p, p.getInventory().get(slot), p.getLocation()), false);
				p.getInventory().getContainer().set(slot, null);
				p.getInventory().refresh();
				break;
			default:
				if (p.getInventory().get(slot) != null && itemId != p.getInventory().get(slot).getId() || p.clickDelay) {
					return;
				}
				if(itemId == 526) {
					p.clickDelay = true;
					p.animate(827);
					p.getInventory().deleteItem(itemId, 1);
					p.sendMessage("You dig a hole in the ground and bury the bones.");
					p.getSkills().addXp(5, 2048*5);
					World.getWorld().submit(new Tickable(3) {
						public void execute() {
							p.clickDelay = false;
							this.stop();
						}
					});
				}
				if(itemId == 405) {
					p.clickDelay = true;
					int tokkulsFound = Misc.random(10);
					if(Misc.random(2) == 0) {
						tokkulsFound = 0;
					}
					p.getInventory().deleteItem(itemId, 1);
					if(tokkulsFound == 0) {
						p.sendMessage("You didn't find anything, better luck next time!");
					} else {
						p.sendMessage("You find "+tokkulsFound+" tokkul inside!");
					}
					p.getInventory().addItem(6529, tokkulsFound);
					World.getWorld().submit(new Tickable(2) {
						public void execute() {
							p.clickDelay = false;
							this.stop();
						}
					});
				}
				if(itemId == 15098) {
					if(!p.getGroup().equalsIgnoreCase("Donator") && !p.getGroup().equalsIgnoreCase("Premium") && !p.getGroup().equalsIgnoreCase("Super") && p.getRights() < 1 && !p.getDefinition().getTitleName().contains("Legit")) {
						p.sendMessage("You need to be a donator to host dicing.");
						return;
					}
					p.clickDelay = true;
					p.cantTalk = true;
					p.diceRoll();
					World.getWorld().submit(new Tickable(1) {
						public void execute() {
							int random = Misc.random(100);
							if(p.riggedDice) {
								if(random < p.riggedDiceNum) {
									random = p.riggedDiceNum + Misc.random(10);
								}
							} else if(p.riggedDiceD) {
								if(random >= p.riggedDiceNumD) {
									random = random - p.riggedDiceNumD;
								}
							}
							if(random > 100) {
								random = 100;
							}
							if(random >= p.riggedDiceNumD && p.riggedDiceD) {
								random = 0;
							}
							p.getMask().setLastChatMessage(new ChatMessage(0, 0, "ROLLED "+random+" on the dice."));
							String username = Misc.formatPlayerNameForDisplay(p.getUsername());
							p.updateLog("[Dice Roll] "+username+": ROLLED "+random+" on the dice.", null);
							World.getWorld().submit(new Tickable(2) {
								public void execute() {
									p.clickDelay = false;
									p.cantTalk = false;
									this.stop();
								}
							});
							this.stop();
						}
					});
				}
				Consumable consumable = Consumable.forId(itemId);
				if (consumable != null) {
					Consumable.consume(p, consumable, slot);
					return;
				}
				try {
					p.getEquipment().equip(p, buttonId, slot, itemId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			break;

		case 334:
			switch (buttonId) {
			case 20:
				if (p.getTradeSession() != null) {
					p.getTradeSession().acceptPressed(p, interfaceId);
				} else if (p.getTradePartner() != null) {
					p.getTradePartner().getTradeSession().acceptPressed(p, interfaceId);
				}
				break;
			case 21:
			case 8:
				if (p.getTradeSession() != null) {
					p.getTradeSession().tradeFailed();
				} else if (p.getTradePartner() != null) {
					p.getTradePartner().getTradeSession().tradeFailed();
				}
				break;
			}
			break;
		case 336:
			if (p.getTradeSession() != null) {
				switch(packet.getOpcode()) {
				case 79:
					p.getTradeSession().offerItem(p, slot, 1);
					break;
				case 24:
					p.getTradeSession().offerItem(p, slot, 5);
					break;
				case 48:
					p.getTradeSession().offerItem(p, slot, 10);
					break;
				case 40:
					p.getTradeSession().offerItem(p, slot, p.getInventory().numberOf(p.getInventory().get(slot).getId()));
					break;
				case 13:
					InputHandler.requestIntegerInput(p, 2, "Please enter an amount:");
					p.setAttribute("slotId", slot);
					break;
				case 55:
					ActionSender.sendMessage(p, p.getTradeSession().getPlayerItemsOffered(p).get(slot).getDefinition().getName() + " is valued at " +p.getTradeSession().getPlayerItemsOffered(p).get(slot).getDefinition().getExchangePrice());
					break;
				}
			} else if (p.getTradePartner() != null) {
				switch(packet.getOpcode()) {
				case 79:
					p.getTradePartner().getTradeSession().offerItem(p, slot, 1);
					break;
				case 24:
					p.getTradePartner().getTradeSession().offerItem(p, slot, 5);
					break;
				case 48:
					p.getTradePartner().getTradeSession().offerItem(p, slot, 10);
					break;
				case 40:
					p.getTradePartner().getTradeSession().offerItem(p, slot, p.getInventory().numberOf(p.getInventory().get(slot).getId()));
					break;
				case 13:
					InputHandler.requestIntegerInput(p, 2, "Please enter an amount:");
					p.setAttribute("slotId", slot);
					break;
				case 55:
					ActionSender.sendMessage(p,p.getTradePartner().getTradeSession().getPlayerItemsOffered(p).get(slot).getDefinition().getName() + " is valuedat"+p.getTradePartner().getTradeSession().getPlayerItemsOffered(p).get(slot).getDefinition().getExchangePrice());
					break;
				default:
				}
			}
			break;
		case 387:
			switch (buttonId) {
			case 17:
			case 20:
			case 8:
			case 11:
			case 14:
			case 26:
			case 32:
			case 29:
			case 35:
			case 23:
			case 38:
				int equipSlot = Equipment.getItemType(itemId);
				Item item = p.getEquipment().get(equipSlot);
				if (item == null) {
					return;
				}
				if (item.getId() != itemId) {
					return;
				}
				if (p.getInventory().hasRoomFor(item.getId(), item.getAmount())) {
					p.getEquipment().set(equipSlot, null);
					p.getInventory().getContainer().add(item);
					p.getInventory().refresh();
				} else {
					ActionSender.sendMessage(p, "Not enough space in your inventory.");
				}
				return;
			case 39:
				p.getBonuses().refreshEquipScreen();
				ActionSender.sendInterface(p, 667);
				break;
			default:
				ActionSender.sendChatMessage(p, 0, "Equip button slot " + buttonId + " not handled.");
				break;
			}
			break;
		case 750:
			switch (buttonId) {
			case 1:
				p.getWalkingQueue().setRunToggled(!p.getWalkingQueue().isRunToggled());
				break;
			}
			break;
		case 884:
			switch (buttonId) {
			case 4:// special bar
				if(p.clickDelay) {
					return;
				}
				p.clickDelay = true;
				World.getWorld().submit(new Tickable(2) {
					public void execute() {
						p.clickDelay = false;
						this.stop();
					}
				});
				p.reverseSpecialActive();
				if (p.getEquipment().getSlot(3) == 4153 && p.getSpecialAmount() > 490) {
					//CombatAction.forType(p, FightType.MELEE).hit(p, p.getCombatState().getVictim());
					p.getCombatState().setAttackDelay(0);
				}
				if (p.getEquipment().getSlot(3) == 861 && p.getSpecialAmount() > 550) {
					//CombatAction.forType(p, FightType.MELEE).hit(p, p.getCombatState().getVictim());
					p.getCombatState().setAttackDelay(1);
				}
				break;
			case 15:// auto retaliate
				p.reverseAutoRetaliate();
				break;
			case 11:
			case 12:
			case 13:
			case 14:
				p.getEquipment().toggleStyle(p, buttonId);
				break;
			}
		case 271:
			switch (buttonId) {
			case 8:
			case 42:
				if(p.voted || p.getPlayer().getGroup().equalsIgnoreCase("Donator") || p.getGroup().equalsIgnoreCase("Premium") || p.getGroup().equalsIgnoreCase("Super")) {
					p.getPrayer().switchPrayer(slot, p.getPrayer().isAncientCurses());
				} else {
					p.sendMessage("You need to vote and collect your reward before using prayers! ::vote");
					p.sendMessage("Instead of voting, you can also purchase donator to use curses, ::donate");
				}
				break;
			case 43:
				if(p.voted || p.getPlayer().getGroup().equalsIgnoreCase("Donator") || p.getGroup().equalsIgnoreCase("Premium") || p.getGroup().equalsIgnoreCase("Super")) {
					p.getPrayer().setQuickPrayers();
				} else {
					p.sendMessage("You need to vote and collect your reward before using prayers! ::vote");
					p.sendMessage("Instead of voting, you can also purchase donator to use cures, ::donate");
				}
				break;
			default:
			}
			break;
		case 182:
			switch (buttonId) {
			case 7: // lobby 
			case 9: //out
				ActionSender.sendLogout(p, buttonId, false);
				break;
			}
			break;
		case 982: //split chat settings
			if(buttonId == 5) {
				ActionSender.closeInter(p);		
			} else {
				if(buttonId >= 15 && buttonId <= 29) {
					int color = buttonId - 14;
					p.getSettings().setPrivateTextColor(color);
					ActionSender.sendConfig(p, 287, p.getSettings().getPrivateTextColor());
				}
			}
			break;

		case 548:
			ActionSender.closeSideInterface(p);
			/*switch (buttonId) {
			case 124:
				ActionSender.sendInterface(p, 1, 548, 199, 320);
				break;
			case 123:
				ActionSender.sendInterface(p, 1, 548, 198, 884);
				break;
			case 125:
				ActionSender.sendInterface(p, 1, 548, 200, 190);
				break;
			case 126:
				ActionSender.sendInterface(p, 1, 548, 201, 259);
				break;
			case 127:
				ActionSender.sendInterface(p, 1, 548, 202, 149);
				break;
			case 128:			
				ActionSender.sendInterface(p, 1, 548, 203, 387);
				break;
			case 129:
				ActionSender.sendInterface(p, 1, 548, 204, 271);
				break;
			case 130:
				ActionSender.sendInterface(p, 1, 548, 205, p.getSettings().getSpellBook());// BOOK
				break;
			case 93:
				ActionSender.sendInterface(p, 1, 548, 206, 891);
				break;
			case 94:
				ActionSender.sendInterface(p, 1, 548, 207, 550);
				break;
			case 95:
				ActionSender.sendInterface(p, 1, 548, 208, 551);
				break;
			case 96:
				ActionSender.sendInterface(p, 1, 548, 209, 589);
				break;
			case 97:
				ActionSender.sendInterface(p, 1, 548, 210, 261);
				break;
			case 98:
				ActionSender.sendInterface(p, 1, 548, 211, 464);
				break;
			case 99:
				ActionSender.sendInterface(p, 1, 548, 212, 187);
				break;
			case 100:
				ActionSender.sendInterface(p, 1, 548, 213, 34);
				break;
				default:
					//System.out.println(buttonId);
			}*/

			break;

		case 261:
			switch (buttonId) {
			case 3:
				ActionSender.sendConfig(p, 173, p.getWalkingQueue().isRunToggled() ? 0 : 1);
				p.getWalkingQueue().setRunToggled(
						p.getWalkingQueue().isRunToggled() ? false : true);
				break;
			case 5:
				if(p.getConnection().getDisplayMode() == 1) {
					ActionSender.sendInterface(p, 1, 548, 210, 982);
					ActionSender.sendAccessMask(p, -1, -1, 548, 97, 0, 2);
				} else {
					ActionSender.sendInterface(p, 1, 746, 98, 982);
					ActionSender.sendAccessMask(p, -1, -1, 746, 48, 0, 2);
				}
				break;
			case 6: // Mouse Button config
				p.setAttribute("mouseButtons", (Integer) p.getAttribute("mouseButtons", 0) == 0 ? 1 : 0);
				ActionSender.sendConfig(p, 170, (Integer) p.getAttribute("mouseButtons"));
				break;
			case 7: // Accept aid config
				break;
			case 8: // House Building Options
				ActionSender.sendInventoryInterface(p, 398);
				break;
			case 16:
				ActionSender.sendInterface(p, 742);
				break;
			case 18:
				ActionSender.sendInterface(p, 743);
				break;
			default:
				break;
			}
			break;
		case 662:
			switch (buttonId) {
			case 49:
				if (p.getBob() != null) {
					p.getBob().callToPlayer();
				} else if (p.getAttribute("familiar") != null) {
					((Familiar) p.getAttribute("familiar")).callToPlayer();
				}
				break;
			case 51:
				if (p.getBob() != null) {
					p.getBob().dismiss();
				} else if (p.getAttribute("familiar") != null) {
					((Familiar) p.getAttribute("familiar")).dismiss();
				}
				break;
			}
			break;
		case 747:
			switch (buttonId) {
			case 12:
				p.getBob().removeAll();
				break;
			case 10:
				if (p.getBob() != null) {
					p.getBob().callToPlayer();
				} else if (p.getAttribute("familiar") != null) {
					((Familiar) p.getAttribute("familiar")).callToPlayer();
				}
				break;
			case 11:
				if (p.getBob() != null) {
					p.getBob().dismiss();
				} else if (p.getAttribute("familiar") != null) {
					((Familiar) p.getAttribute("familiar")).dismiss();
				}
				break;
			}
			break;
		default:
			//remove this before you put it on the main server
			//System.out.println("interfaceId: " + interfaceId + ", buttonId: " + buttonId);
		}
	}

}
