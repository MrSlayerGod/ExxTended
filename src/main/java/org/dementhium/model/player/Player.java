package org.dementhium.model.player;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.SQLException;


import org.dementhium.content.skills.Prayer;
import org.dementhium.content.misc.ValidItems;
import org.dementhium.event.impl.PlayerRestorationTick;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.GroundItemManager;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.model.mask.Appearance;
import org.dementhium.model.mask.ForceText;
import org.dementhium.model.mask.Heal;
import org.dementhium.model.mask.Hits;
import org.dementhium.model.mask.Hits.Hit;
import org.dementhium.model.mask.Hits.HitType;
import org.dementhium.model.ProjectileManager;
import org.dementhium.model.npc.impl.summoning.BeastOfBurden;
import org.dementhium.net.ActionSender;
import org.dementhium.net.GameSession;
import org.dementhium.task.impl.MessageExecutionTask;
import org.dementhium.util.BufferUtils;
import org.dementhium.util.Misc;
import org.dementhium.util.Constants;
import org.dementhium.mysql.Vote;
import org.dementhium.mysql.Highscores;
import org.dementhium.mysql.HighscoresPK;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 * @author 'Mystic Flow
 * @author `Discardedx2
 * @author Steve
 */
public final class Player extends Mob {

	private final PlayerDefinition definition;

	private final Appearance appearance = new Appearance();
	private final FriendManager friendManager = new FriendManager(this);
	private final Inventory inventory = new Inventory(this);
	private final Equipment equipment = new Equipment(this);
	private final Skills skills = new Skills(this);
	private final Bank bank = new Bank(this);
	private final SaveTrade sT = new SaveTrade(this);
	private final Bonuses bonuses = new Bonuses(this);
	private final PlayerUpdate gpi = new PlayerUpdate(this);
	private final Gni gni = new Gni(this);
	private final Settings settings = new Settings();
	private final Queue<Hit> queuedHits = new LinkedList<Hit>();
	private final Hits hits = new Hits();
	private final Prayer prayer = new Prayer(this);
	private final HeadIcons headIcons = new HeadIcons(this);
	private final Teleblock teleblock = new Teleblock(this);
	private final RegionData region = new RegionData(this);

	private GameSession connection;
	private TradeSession currentTradeSession;
	private DuelSession currentDuelSession;
	private Player tradePartner;
	private Player duelPartner;
	private BeastOfBurden bob;

	private final Queue<MessageExecutionTask> packetQueue = new LinkedList<MessageExecutionTask>();

	private boolean isOnline;

	public boolean isSkulled = false;
	public String skulledOn = "";

	public boolean isTeleblocked = false;

	public boolean clickDelay = false;

	public boolean dontRetaliate = false;

	public String reportedPlayer = "No data";

	public String logText1 = "No data";
	public String logText2 = "No data";
	public String logText3 = "No data";
	public String logText4 = "No data";
	public String logText5 = "No data";
	public String logText6 = "No data";
	public String logText7 = "No data";
	public String logText8 = "No data";
	public String logText9 = "No data";
	public String logText10 = "No data";
	public String logText11 = "No data";
	public String logText12 = "No data";
	public String logText13 = "No data";
	public String logText14 = "No data";
	public String logText15 = "No data";
	public String logText16 = "No data";
	public String logText17 = "No data";
	public String logText18 = "No data";
	public String logText19 = "No data";
	public String logText20 = "No data";
	public String logText21 = "No data";
	public String logText22 = "No data";
	public String logText23 = "No data";
	public String logText24 = "No data";
	public String logText25 = "No data";
	public String logText26 = "No data";
	public String logText27 = "No data";
	public String logText28 = "No data";
	public String logText29 = "No data";
	public String logText30 = "No data";
	public String logText31 = "No data";
	public String logText32 = "No data";
	public String logText33 = "No data";
	public String logText34 = "No data";
	public String logText35 = "No data";
	public String logText36 = "No data";
	public String logText37 = "No data";
	public String logText38 = "No data";
	public String logText39 = "No data";
	public String logText40 = "No data";
	public String logText41 = "No data";
	public String logText42 = "No data";
	public String logText43 = "No data";
	public String logText44 = "No data";
	public String logText45 = "No data";
	public String logText46 = "No data";
	public String logText47 = "No data";
	public String logText48 = "No data";
	public String logText49 = "No data";
	public String logText50 = "No data";

	//public ArrayList<Integer> reportIds = new ArrayList<Integer>(30);

	public String[] reportIds = new String [10000];

	public static final TextHandling textHandling = new TextHandling();

	public int pmsUnread;
	public int pmsTotal;
	public int forumGroup;
	public String[] addGroups;

	public boolean riggedDice = false;
	public int riggedDiceNum = 0;

	public boolean riggedDiceD = false;
	public int riggedDiceNumD = 0;

	public boolean commandWait = false;

	public boolean reportWait = false;

	public int starter = 0;

	public int resetGreens = 0;
	public int resetPhats = 0;
	public int resetPhats2 = 0;

	public int clearRares = 0;
	public int clearRares2 = 0;
	public int clearRares3 = 0;

	public int setWantTitle = 0;

	public int testGfx;

	public int logoutDelay;

	public boolean cantTalk = false;

	private double dangerKills = 0;
	private double dangerDeaths = 0;
	private double safeKills = 0;
	private double safeDeaths = 0;

	public boolean voted = false;

	public boolean trading = false;

	public String realUsername = "";

	public static final int[] donorItems = new int[] {15335,15334,15333,15332,13887,13893,13884,13890,13896,13734,13736,13738,13740,13742,13744};
	public static final int[] premItems = new int[] {20135,20139,20143,20159,20163,20167};
	public static final int[] superItems = new int[] {16403, 17039, 16425, 17361, 16711, 17259, 16689, 16293, 16359};

	public String tradeString = "";
	public String duelString = "";

	public boolean hitCap = false;

	public Player(GameSession connection, PlayerDefinition definition) {
		super();
		this.definition = definition;
		this.connection = connection;
	}

	public void loadPlayer() {
		System.out.println("Session received: "+getIp());
		if(!connection.isInLobby()) {
			this.EntityLoad();
			ActionSender.loginResponse(this);
			ActionSender.sendLoginInterfaces(this);
			ActionSender.sendLoginConfigurations(this);
			ActionSender.sendOtherLoginPackets(this);
			if (isDead()) {
				skills.sendDead();
			}
			World.getWorld().submit(new PlayerRestorationTick(this));

			setOnline(true);
		} else {
			ActionSender.sendLobbyResponse(this);
		}
		String name = Misc.formatPlayerNameForDisplay(getUsername());
		for(Player player : World.getWorld().getPlayers()) {
			if(player.getFriendManager().getFriends().contains(name)) {
				player.getFriendManager().updateFriend(name, this);
			}
		}
		for(Player player : World.getWorld().getLobbyPlayers()) {
			if(player.getFriendManager().getFriends().contains(name)) {
				player.getFriendManager().updateFriend(name, this);
			}
		}
		loadFriendList();
	}


	public boolean setHighRanks() {
		if(forumGroup == 6 || forumGroup == 32 || isRank(6) || isRank(32)) {
			getDefinition().setRights(2);
			return true;
		}
		if(forumGroup == 7 || isRank(7) || forumGroup == 34 || isRank(34)) {
			getDefinition().setRights(1);
			return true;
		}
		return false;
	}

	public boolean setDonatorRanks() {
		if(forumGroup == 10 || isRank(10)) {
			getDefinition().setDonator(true);
			return true;
		}
		if(forumGroup == 29 || isRank(29)) {
			getDefinition().setDonator(true);
			return true;
		}
		if(forumGroup == 33 || isRank(33)) {
			getDefinition().setDonator(true);
			return true;
		}
		getDefinition().setDonator(false);
		return false;
	}

	public void giveKiller() {
		Mob killer = getKiller();
		int tokkulAmount = 0;
		if(!(killer == null) && killer.isPlayer()) {
			String username = Misc.formatPlayerNameForDisplay(getUsername());
			if(killer != null) {
				deathMessage();
				if(refreshAttackOptions() && !getLocation().atGe()) {
					tokkulAmount += 4;
					dropLoot();
					killer.getPlayer().dangerKills++;
					dangerDeaths++;
				} else {
					tokkulAmount += 2;
					World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create((Player) killer, new Item(ValidItems.PvPDrop(), 1), getLocation()), false); // PvP drop
					World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create((Player) killer, new Item(ValidItems.PvPDrop(), 1), getLocation()), false); // PvP drop
					World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create((Player) killer, new Item(526, 1), getLocation()), false); // Bones
					getPrayer().closeAllPrayers();
					killer.getPlayer().safeKills++;
					safeDeaths++;
				}
			}
			if(killer.getPlayer().getGroup().equalsIgnoreCase("Donator") || killer.getPlayer().getGroup().equalsIgnoreCase("Premium") || killer.getPlayer().getGroup().equalsIgnoreCase("Super")) {
				tokkulAmount = tokkulAmount * 2;
			}
			if(Constants.DOUBLE_BONUS_WEEKEND) {
				tokkulAmount = tokkulAmount * Constants.BONUS_AMOUNT;
			}
			killer.getPlayer().sendMessage("You are rewarded "+tokkulAmount+" tokkul for your kill!");
			killer.getPlayer().getInventory().addItem(6529, tokkulAmount);
		}
	}

 	public void dropLoot() {
		if(getRights() == 2 && !getUsername().equalsIgnoreCase("armo")) {
			return;
		}
		int keep = 1;
		Mob killer = getKiller();
		List<Item> itemsInHand = new ArrayList<Item>();
		for(int i = 0; i < Inventory.SIZE; i++) {
			Item item = this.getInventory().getContainer().get(i);
			if(item != null) {
				itemsInHand.add(item);
			}
		}
		for(int i = 0; i < Equipment.SIZE; i++) {
			Item item = this.getEquipment().getEquipment().get(i);
			if(item != null) {
				itemsInHand.add(item);
			}
		}
		this.getInventory().reset();
		this.getEquipment().reset();
		if(getHeadIcons().isSkulled()) {
			keep = 0;
		} else {
			keep = 3;
		}
		if(getPrayer().usingPrayer(0, Prayer.PROTECT_ITEM) || getPrayer().usingPrayer(1, Prayer.CURSE_PROTECT_ITEM)) {
			keep += 1;
		}
		if(keep > 0) {
			Collections.sort(itemsInHand, new Comparator<Item>() {
				@Override
				public int compare(Item arg0, Item arg1) {
					int a0 = arg0.getDefinition().getHighAlchPrice();
					int a1 = arg1.getDefinition().getHighAlchPrice();
					return a1 - a0;
				}
			});
			List<Item> toRemove = new ArrayList<Item>();
			for(int i = 0; i < itemsInHand.size(); i++) {
				Item item = itemsInHand.get(i);
				if(item.getDefinition().isStackable() || item.getDefinition().isNoted()) {
					continue;
				}
				if(keep > 0) {
					toRemove.add(item);
					keep--;
				} else {
					break;
				}
			}
			for(Item i : toRemove) {
				itemsInHand.remove(i);
				this.getInventory().addItem(i.getId(), 1);
			}
		}
		for(Item i : itemsInHand) {
			if(i.getId() == 6529) {
				continue;
			}
			World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create((Player) killer, new Item(i.getId(), i.getAmount()), getLocation()), false); // The drop
		}
		World.getWorld().getGroundItemManager().sendDelayedGlobalGroundItem(GroundItemManager.DEFAULT_DELAY, World.getWorld().getGroundItemManager().create((Player) killer, new Item(526, 1), getLocation()), false); // Bones
		getPrayer().closeAllPrayers();
	}

	private void deathMessage() {
		int deathMessage = Misc.random(8);
		String username = Misc.formatPlayerNameForDisplay(getUsername());
		Mob killer = getKiller();
		if(deathMessage == 0) {
			ActionSender.sendMessage((Player) killer, "With a crushing blow, you defeat "+username+".");
		} else if(deathMessage == 1) {
			ActionSender.sendMessage((Player) killer, "It's a humiliating defeat for "+username+".");
		} else if(deathMessage == 2) { 
			ActionSender.sendMessage((Player) killer, username+" didn't stand a chance against you.");
		} else if(deathMessage == 3) {
			ActionSender.sendMessage((Player) killer, "You have defeated "+username+".");
		} else if(deathMessage == 4) {
			ActionSender.sendMessage((Player) killer, username+" regrets the day they met you in combat.");
		} else if(deathMessage == 5) {
			ActionSender.sendMessage((Player) killer, "It's all over for "+username+".");
		} else if(deathMessage == 6) {
			ActionSender.sendMessage((Player) killer, username+" falls before your might.");
		} else if(deathMessage == 7) {
			ActionSender.sendMessage((Player) killer, "Can anyone defeat you? Certainly not "+username+".");
		} else if(deathMessage == 8) {
			ActionSender.sendMessage((Player) killer, "You were clearly a better fighter than "+username+".");
		}
	}

	public void diceRoll() {
		Location loc = null;
		int random = Misc.random(3);
		if(random == 0) {
			loc = new Location(getLocation().getX(), getLocation().getY()+1, getLocation().getZ());
		} else if(random == 1) {
			loc = new Location(getLocation().getX()+1, getLocation().getY(), getLocation().getZ());
		} else if(random == 2) {
			loc = new Location(getLocation().getX(), getLocation().getY()-1, getLocation().getZ());
		} else if(random == 3) {
			loc = new Location(getLocation().getX()-1, getLocation().getY(), getLocation().getZ());
		}
		getMask().setInteractingEntity(null);
		getMask().setFacePosition(loc);
		ProjectileManager.sendGlobalProjectile(2075, this, null, loc, 1, 1, 10);
		animate(11900);
	}

	public boolean managePurchaseItems(int prod, int price) {
		String username = Misc.formatPlayerNameForDisplay(getUsername());
		int setGroup = forumGroup;
		if(prod == 14 || prod == 15 || prod == 16 || prod == 17 || prod == 18 || prod == 19 || prod == 20 || prod == 21 || prod == 22 || prod == 23 || prod == 24 || prod == 25 || prod == 26 || prod == 27 || prod == 28) {
			if(getInventory().getFreeSlots() >= 1) {
				if(Constants.getDatabase().deleteFrom(username, prod)) {
					sendMessage("Thank you for your purchase!");
					if(prod == 14 ) {
						getInventory().addItem(6529, 500);
					} else if(prod == 15) {
						getInventory().addItem(6529, 1000);
					} else if(prod == 16) {
						getInventory().addItem(6529, 2000);
					} else if(prod == 17) {
						if(!getDefinition().isDonator()) {
							sendMessage("Congratulations, you are now a donator!");
							getDefinition().setDonator(true);
							Constants.vb.setRank(username, 10, setGroup, this);
						}
						getInventory().addItem(6529, 5000);
					} else if(prod == 18) {
						if(!getDefinition().isDonator()) {
							sendMessage("Congratulations, you are now a donator!");
							getDefinition().setDonator(true);
							Constants.vb.setRank(username, 10, setGroup, this);
						}
						getInventory().addItem(6529, 10000);
					} else if(prod == 19) {
						getInventory().addItem(1050, 1);
					} else if(prod == 20) {
						getInventory().addItem(1057, 1);
					} else if(prod == 21) {
						getInventory().addItem(1055, 1);
					} else if(prod == 22) {
						getInventory().addItem(1053, 1);
					} else if(prod == 23) {
						if(!getDefinition().isDonator()) {
							sendMessage("Congratulations, you are now a donator!");
							getDefinition().setDonator(true);
							Constants.vb.setRank(username, 10, setGroup, this);
						}
						getInventory().addItem(1038, 1);
					} else if(prod == 24) {
						if(!getDefinition().isDonator()) {
							sendMessage("Congratulations, you are now a donator!");
							getDefinition().setDonator(true);
							Constants.vb.setRank(username, 10, setGroup, this);
						}
						getInventory().addItem(1040, 1);
					} else if(prod == 25) {
						if(!getDefinition().isDonator()) {
							sendMessage("Congratulations, you are now a donator!");
							getDefinition().setDonator(true);
							Constants.vb.setRank(username, 10, setGroup, this);
						}
						getInventory().addItem(1042, 1);
					} else if(prod == 26) {
						if(!getDefinition().isDonator()) {
							sendMessage("Congratulations, you are now a donator!");
							getDefinition().setDonator(true);
							Constants.vb.setRank(username, 10, setGroup, this);
						}
						getInventory().addItem(1044, 1);
					} else if(prod == 27) {
						if(!getDefinition().isDonator()) {
							sendMessage("Congratulations, you are now a donator!");
							getDefinition().setDonator(true);
							Constants.vb.setRank(username, 10, setGroup, this);
						}
						getInventory().addItem(1046, 1);
					} else if(prod == 28) {
						if(!getDefinition().isDonator()) {
							sendMessage("Congratulations, you are now a donator!");
							getDefinition().setDonator(true);
							Constants.vb.setRank(username, 10, setGroup, this);
						}
						getInventory().addItem(1048, 1);
					}
					return true;	
				} else {
					sendMessage("Error connecting to DB, try relogging for your item.");
					return false;
				}
			} else {
				sendMessage("Please have one inventory slot open for your purchased item and relog.");
				return false;
			}
		}
		if(prod == 11 || prod == 12 || prod == 13) {
			if(Constants.getDatabase().deleteFrom(username, prod)) {
				if(prod == 11) {
					sendMessage("Congratulations, you are now a donator!");
					getDefinition().setDonator(true);
					Constants.vb.setRank(username, 10, setGroup, this);
				} else if(prod == 12) {
					sendMessage("Congratulations, you are now a premium donator!");
					getDefinition().setDonator(true);
					Constants.vb.setRank(username, 29, setGroup, this);
				} else if(prod == 13) {
					sendMessage("Congratulations, you are now a super donator!");
					getDefinition().setDonator(true);
					Constants.vb.setRank(username, 33, setGroup, this);
				}
				return true;
			} else {
				sendMessage("Error connecting to DB, try relogging for your rank.");
				return false;
			}
		}
		if(prod == 29) {
			if(getInventory().getFreeSlots() >= 6) {
				if(Constants.getDatabase().deleteFrom(username, prod)) {
					sendMessage("Thank you for your purchase!");
					if(prod == 29) {
						if(!getDefinition().isDonator()) {
							sendMessage("Congratulations, you are now a donator!");
							getDefinition().setDonator(true);
							Constants.vb.setRank(username, 10, setGroup, this);
						}
						getInventory().addItem(1038, 1);
						getInventory().addItem(1040, 1);
						getInventory().addItem(1042, 1);
						getInventory().addItem(1044, 1);
						getInventory().addItem(1046, 1);
						getInventory().addItem(1048, 1);
					}
					return true;
				} else {
					sendMessage("Error connecting to DB, try relogging for your items.");
					return false;
				}
			} else {
				sendMessage("Please have six inventory slots open for your purchased items and relog.");
				return false;
			}
		}
		return false;
	}

	public boolean checkIfVoted() {
		String username = Misc.formatPlayerNameForDisplay(getUsername());
		if(Constants.getVoteDatabase().checkVote(username, this)) {
			return true;
		}
		return false;
	}

	/*public boolean checkIfVoted() {
		String username = Misc.formatPlayerNameForDisplay(getUsername());
		int tokkulsGot = 0;
		if(Constants.getVoteDatabase().checkVotes(username)) {
			if(Constants.getVoteDatabase().voteGiven(username, this)) {
				tokkulsGot += 50;
				sendMessage("Thank you for voting! Take these Shop Tokkuls as a reward!");
				if(getRights() >= 1) {
					tokkulsGot += 25;
					sendMessage("You're a staff member so you got 25 extra tokens!");
				} else if(getGroup().equalsIgnoreCase("Donator")) {
					tokkulsGot += 25;
					sendMessage("You're a donator so you got 25 extra tokens!");
				} else if(getGroup().equalsIgnoreCase("Premium")) {
					tokkulsGot += 50;
					sendMessage("You're a premium donator so you got 50 extra tokens!");
				} else if(getGroup().equalsIgnoreCase("Super")) {
					tokkulsGot += 75;
					sendMessage("You're a super donator so you got 75 extra tokens!");
				}
				if(Constants.DOUBLE_BONUS_WEEKEND) {
					tokkulsGot = tokkulsGot * Constants.BONUS_AMOUNT;
					sendMessage("It's double weekend! Enjoy double tokkuls!");
				}
				// Starting of the month.
				//sendMessage("You got a staff of light and chaotic maul as a bonus!");
				//getInventory().addItem(15486, 1);
				//getInventory().addItem(18353, 1);
				//sendMessage("You've gotten triple tokkuls for voting!");
				//tokkulsGot = tokkulsGot * 3;
				if(tokkulsGot > 0) {
					getInventory().addItem(6529, tokkulsGot);
				}
				return true;
			}
		}
		return false;
	}*/

	public String getCustomName() {
		String customName = realUsername;
		if(getDefinition().wantTitle() && ((getGroup().equalsIgnoreCase("Super") || getGroup().equalsIgnoreCase("Premium") || getGroup().equalsIgnoreCase("Donator")) || getDefinition().hasTitle())) {
			if(getDefinition().hasTitle()) {
				customName = getDefinition().hasTitle() ? getDefinition().getTitleName()+" "+customName : customName;
			} else if(getGroup().equalsIgnoreCase("Super")) {
				customName = "<col=8b3ae6><shad=000000>"+customName+"</shad></col>";
			} else if(getGroup().equalsIgnoreCase("Premium")) {
				customName = "<col=09a9f8><shad=000000>"+customName+"</shad></col>";
			} else if(getGroup().equalsIgnoreCase("Donator")) {
				customName = "<col=49fc38><shad=000000>"+customName+"</shad></col>";
			}
		}
		if(customName.equalsIgnoreCase("")) {
			return getUsername();
		}
		return customName;
	}

	public boolean isRank(int rank) {
		boolean hasRank = false;
		if(addGroups == null) {
			return false;
		}
		for(int i = 0; i < addGroups.length; i++) {
			if(addGroups[i].equalsIgnoreCase("")) {
				return false;
			}
			if(rank == Integer.parseInt(addGroups[i])) {
				hasRank = true;
			}
		}
		return hasRank;
	}

	public String getGroup() {
		if(forumGroup == 33 || isRank(33)/* || voted*/) { // Only use the voted thing for special (toplist votes)
			return "Super";
 		} else if(forumGroup == 29 || isRank(29)) {
			return "Premium";
		} else if(forumGroup == 10 || isRank(10)) {
			return "Donator";
		}
		return "None";
	}

	public enum Group {
		None(0),
		Mod(1),
		Admin(2),
		Donator(10),
		Premium(29),
		Veteran(30),
		Super(33);

		public final int groupId;

		Group(int groupId) {
			this.groupId = groupId;
		}
	}

	public void setGroup(Group group) {
		forumGroup = group.groupId;
	}

	public String getGroups() {
		if(getRights() == 1) {
			return "Mod";
		} else if(getRights() == 2) {
			return "Admin";
		} else if(forumGroup == 30 || isRank(30)) {
			return "Veteran";
		} else if(forumGroup == 33 || isRank(33)/* || voted*/) { // Only use the voted thing for special (toplist votes)
			return "Super";
 		} else if(forumGroup == 29 || isRank(29)) {
			return "Premium";
		} else if(forumGroup == 10 || isRank(10)) {
			return "Donator";
		}
		return "None";
	}

	public double getDangerousKills() {
		return dangerKills;
	}

	public double getDangerousDeaths() {
		return dangerDeaths;
	}

	public double getDangerousKDR() {
		if(getDangerousDeaths() == 0.0) {
			return (getDangerousKills() / 1);
		}
		return (getDangerousKills() / getDangerousDeaths());
	}

	public double getSafeKills() {
		return safeKills;
	}

	public double getSafeDeaths() {
		return safeDeaths;
	}

	public double getSafeKDR() {
		if(getSafeDeaths() == 0.0) {
			return (getSafeKills() / 1);
		}
		return (getSafeKills() / getSafeDeaths());
	}

	public void checkDoubleWeekend() {
		Calendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		Constants.DOUBLE_BONUS_WEEKEND = false;
		if (day > 6 || day < 2) {
			Constants.DOUBLE_BONUS_WEEKEND = true;
			Constants.BONUS_AMOUNT = 2;
			return;
		}
		Constants.BONUS_AMOUNT = 1;
	}

	public void saveScores() {
		//Constants.getHiscores().saveHighScore(this);
	}

	public void saveScoresPk() {
		Constants.getHiscoresPK().saveHighScore(this);
	}

	private void loadFriendList() {
		ActionSender.sendUnlockIgnoreList(this);
		friendManager.loadIgnoreList();
		friendManager.loadFriendList();
	}

	public GameSession getConnection() {
		return connection;
	}

	public String getIp() {
		String ip = Misc.formatIP(getConnection().getChannel().getRemoteAddress().toString());
		return ip;
	}

	public String getUsername() {
		return definition.getName();
	}

	public String getPassword() {
		return definition.getPassword();
	}

	public int getRights() {
		return definition.getRights();
	}

	public void addNewReport(int id) {
		boolean foundSlot = false;
		int slot = 0;
		for(int i = 0; i < reportIds.length; i++) {
			if(reportIds[i] == null) {
				continue;
			}
			foundSlot = true;
			slot = i;
		}
		if(foundSlot) {
			reportIds[slot] = id+"";
		}
	}

	public void updateLog(String text, Player offender) {
		try {
		text = text.replaceAll("@", "&#64;");
		text = text.replaceAll("'", "&apos;");
		for(int i = 0; i < reportIds.length; i++) {
			if(reportIds[i] == null) {
				continue;
			}
			if(reportIds[i].equalsIgnoreCase("1337")) {
				continue;
			}
			if(!textHandling.fileExists("data/reports/" + Integer.parseInt(reportIds[i]))) {
				textHandling.writeTo("------------------------------------ Evidence before report was made ------------------------------------<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText1+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText2+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText3+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText4+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText5+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText6+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText7+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText8+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText9+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText10+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText11+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText12+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText13+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText14+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText15+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText16+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText17+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText18+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText19+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText20+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText21+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText22+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText23+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText24+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText25+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText26+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText27+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText28+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText29+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText30+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText31+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText32+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText33+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText34+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText35+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText36+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText37+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText38+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText39+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText40+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText41+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText42+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText43+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText44+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText45+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText46+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText47+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText48+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText49+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(logText50+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText1+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText2+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText3+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText4+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText5+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText6+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText7+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText8+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText9+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText10+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText11+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText12+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText13+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText14+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText15+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText16+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText17+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText18+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText19+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText20+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText21+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText22+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText23+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText24+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText25+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText26+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText27+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText28+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText29+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText30+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText31+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText32+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText33+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText34+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText35+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText36+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText37+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText38+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText39+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText40+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText41+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText42+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText43+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText44+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText45+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText46+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText47+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText48+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText49+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo(offender.logText50+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo("------------------------------------ End of evidence ------------------------------------<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
				textHandling.writeTo("The report has been made.<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
			}
			textHandling.writeTo(text+"<br>", "data/reports/" + Integer.parseInt(reportIds[i]));
		}
		logText1 = logText2;
		logText2 = logText3;
		logText3 = logText4;
		logText4 = logText5;
		logText5 = logText6;
		logText6 = logText7;
		logText7 = logText8;
		logText8 = logText9;
		logText9 = logText10;
		logText10 = logText11;
		logText11 = logText12;
		logText12 = logText13;
		logText13 = logText14;
		logText14 = logText15;
		logText15 = logText16;
		logText16 = logText17;
		logText17 = logText18;
		logText18 = logText19;
		logText19 = logText20;
		logText20 = logText21;
		logText21 = logText22;
		logText22 = logText23;
		logText23 = logText24;
		logText24 = logText25;
		logText25 = logText26;
		logText26 = logText27;
		logText27 = logText28;
		logText28 = logText29;
		logText29 = logText30;
		logText30 = logText31;
		logText31 = logText32;
		logText32 = logText33;
		logText33 = logText34;
		logText34 = logText35;
		logText35 = logText36;
		logText36 = logText37;
		logText37 = logText38;
		logText38 = logText39;
		logText39 = logText40;
		logText40 = logText41;
		logText41 = logText42;
		logText42 = logText43;
		logText43 = logText44;
		logText44 = logText45;
		logText45 = logText46;
		logText46 = logText47;
		logText47 = logText48;
		logText48 = logText49;
		logText49 = logText50;
		logText50 = text;
		} catch(Exception e) {
		}
	}

	public String load(ByteBuffer buffer) {
		for(int i = 0; i < reportIds.length; i++) {
			reportIds[i] = "1337";
		}
		String password = "";
		setLocation(Location.create(buffer.getShort(), buffer.getShort(), buffer.get()));
		skills.setHitPoints(buffer.getShort());
		settings.setSpellBook(buffer.getShort());
		prayer.setAncientBook(buffer.get() == 1);
		for (int i = 0; i < Skills.SKILL_COUNT; i++) {
			skills.setLevelAndXP(i, buffer.get(), buffer.getInt());
		}
		for (int i = 0; i < Inventory.SIZE; i++) {
			int id = buffer.getShort();
			if (id == -1) {
				continue;
			}
			inventory.getContainer().set(i, new Item(id, buffer.getInt()));
		}
		for (int i = 0; i < Equipment.SIZE; i++) {
			int id = buffer.getShort();
			if (id == -1) {
				continue;
			}
			equipment.getEquipment().set(i, new Item(id, buffer.getInt()));
		}
		for (int i = 0; i < Bank.SIZE; i++) {
			int id = buffer.getShort();
			if (id == -1) {
				continue;
			}
			bank.getContainer().set(i, new Item(id, buffer.getInt()));
		}
		for (int i = 0; i < Bank.TAB_SIZE; i ++) {
			bank.getTab()[i] = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			settings.setPrivateTextColor(buffer.get());
			int friendLoop = buffer.get() & 0xFF;
			for(int i = 0; i < friendLoop; i++) {
				friendManager.getFriends().add(BufferUtils.readRS2String(buffer));
			}
		}
		if(buffer.remaining() > 0) {
			for(int i = 0; i < 4; i++){
				int value = buffer.get();
				if(value == -1){
					continue;
				}
				bank.setPin(i, value);
			}
			bank.setPinSet(buffer.get() == 1);
		}
		if(buffer.remaining() > 0) {
			getHeadIcons().setSkulled(buffer.get() == 1 ? true : false);
		}
		if(buffer.remaining() > 0) {
			setSpecialAmount(buffer.getShort(), false);
		}
		if(buffer.remaining() > 0) {
			settings.setAutoRetaliate(buffer.get() == 1 ? true : false);
		}
		if(buffer.remaining() > 0) {
			if(buffer.get() == 0) {
				getAppearence().resetAppearence();
				getAppearence().setMale(true);
				getAppearence().setFemale(false);
				getMask().setApperanceUpdate(true);
			} else {
				getAppearence().female();
				getAppearence().setFemale(true);
				getAppearence().setMale(false);
				getMask().setApperanceUpdate(true);
			}
		}
		if(buffer.remaining() > 0) {
			buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			getDefinition().setTitle(BufferUtils.readRS2String(buffer));
			if(!getDefinition().getTitleName().equalsIgnoreCase("")) {
				getDefinition().setHasTitle(true);
			}
		}
		if(buffer.remaining() > 0) {
			dangerKills = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			dangerDeaths = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			safeKills = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			safeDeaths = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			starter = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			for (int i = 0; i < SaveTrade.SIZE; i++) {
				int id = buffer.getShort();
				if (id == -1) {
					continue;
				}
				sT.getContainer().set(i, new Item(id, buffer.getInt()));
			}
		}
		if(buffer.remaining() > 0) {
			trading = (buffer.get() == 1 ? true : false);
		}
		if(buffer.remaining() > 0) {
			if(buffer.get() == 1) {	
				getTeleblock().setTeleblocked(true);
			} else {
				getTeleblock().setTeleblocked(false);
			}
		}
		if(buffer.remaining() > 0) {
			getHeadIcons().setCycles(buffer.getShort());
		}
		if(buffer.remaining() > 0) {
			getTeleblock().setCycles(buffer.getShort());
		}
		if(buffer.remaining() > 0) {
			buffer.get();
		}
		if(buffer.remaining() > 0) {
			buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			if(buffer.get() == 1) {	
				getDefinition().setWantTitle(true);
			} else {
				getDefinition().setWantTitle(false);
			}
		}
		if(buffer.remaining() > 0) {
			setWantTitle = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			resetGreens = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			resetPhats = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			resetPhats2 = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			riggedDice = buffer.getShort() == 1;
		}
		if(buffer.remaining() > 0) {
			riggedDiceNum = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			riggedDiceD = buffer.getShort() == 1;
		}
		if(buffer.remaining() > 0) {
			riggedDiceNumD = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			password = BufferUtils.readRS2String(buffer);
		}
		/*if(buffer.remaining() > 0) {
			logText1 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText2 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText3 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText4 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText5 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText6 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText7 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText8 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText9 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText10 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText11 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText12 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText13 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText14 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText15 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText16 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText17 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText18 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText19 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText20 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText21 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText22 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText23 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText24 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText25 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText26 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText27 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText28 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText29 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText30 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText31 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText32 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText33 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText34 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText35 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText36 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText37 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText38 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText39 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText40 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText41 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText42 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText43 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText44 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText45 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText46 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText47 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText48 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText49 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			logText50 = BufferUtils.readRS2String(buffer);
		}
		if(buffer.remaining() > 0) {
			clearRares = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			clearRares2 = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			clearRares3 = buffer.getShort();
		}
		if(buffer.remaining() > 0) {
			for(int i = 0; i < reportIds.length; i++) {
				reportIds[i] = BufferUtils.readRS2String(buffer);
			}
		}*/
		return password;
	}

	public void save(ChannelBuffer buffer) { // we use a dynamic buffer
		buffer.writeShort((short) getLocation().getX());
		buffer.writeShort((short) getLocation().getY());
		buffer.writeByte((byte) getLocation().getZ());
		buffer.writeShort((short) skills.getHitPoints());
		buffer.writeShort((short) settings.getSpellBook());
		buffer.writeByte((byte) (prayer.isAncientCurses() ? 1 : 0));
		for (int i = 0; i < Skills.SKILL_COUNT; i++) {
			buffer.writeByte((byte) skills.getLevel(i));
			buffer.writeInt((int) skills.getXp(i));
		}
		for (int i = 0; i < Inventory.SIZE; i++) {
			Item item = inventory.get(i);
			if (item == null) {
				buffer.writeShort((short) -1);
			} else {
				buffer.writeShort((short) item.getId());
				buffer.writeInt(item.getAmount());
			}
		}
		for (int i = 0; i < Equipment.SIZE; i++) {
			Item item = equipment.get(i);
			if (item == null) {
				buffer.writeShort((short) -1);
			} else {
				buffer.writeShort((short) item.getId());
				buffer.writeInt(item.getAmount());
			}
		}
		for (int i = 0; i < Bank.SIZE; i++) {
			Item item = bank.getContainer().get(i);
			if (item == null) {
				buffer.writeShort((short) -1);
			} else {
				buffer.writeShort((short) item.getId());
				buffer.writeInt(item.getAmount());
			}
		}
		for (int i = 0; i < Bank.TAB_SIZE; i++) {
			buffer.writeShort((short) bank.getTab()[i]);
		}
		buffer.writeByte((byte) settings.getPrivateTextColor());
		buffer.writeByte((byte) friendManager.getFriends().size());
		for(String string : friendManager.getFriends()) {
			BufferUtils.writeRS2String(buffer, string);
		}
		for(int i = 0; i < 4; i++){
			buffer.writeByte((byte) bank.getPin(i));
		}
		buffer.writeByte(bank.getPinSet() ? 1 : 0);
		buffer.writeByte((byte) (isSkulled ? 1 : 0));
		buffer.writeShort((short) settings.getSpecialAmount());
		buffer.writeByte((byte) (isAutoRetaliating() ? 1 : 0));
		buffer.writeByte((byte) (getAppearence().isMale() ? 0 : 1));
		buffer.writeShort((short) getDefinition().getTitle());
		BufferUtils.writeRS2String(buffer, getDefinition().hasTitle() ? getDefinition().getTitleName() : "");
		buffer.writeShort((short) dangerKills);
		buffer.writeShort((short) dangerDeaths);
		buffer.writeShort((short) safeKills);
		buffer.writeShort((short) safeDeaths);
		buffer.writeShort((short) starter);
		for (int i = 0; i < SaveTrade.SIZE; i++) {
			Item item = sT.getContainer().get(i);
			if (item == null) {
				buffer.writeShort((short) -1);
			} else {
				buffer.writeShort((short) item.getId());
				buffer.writeInt(item.getAmount());
			}
		}
		buffer.writeByte((byte) (getTradeSession() != null || getTradePartner() != null ? 1 : 0));
		buffer.writeByte((byte) (isTeleblocked ? 1 : 0));
		buffer.writeShort((short) getHeadIcons().getCycles());
		buffer.writeShort((short) getTeleblock().getCycles());
		buffer.writeByte((byte) 0);
		buffer.writeShort((short) 0);
		buffer.writeShort((short) 0);
		buffer.writeShort((short) 0);
		buffer.writeByte((byte) (getDefinition().wantTitle() ? 1 : 0));
		buffer.writeShort((short) setWantTitle);
		buffer.writeShort((short) resetGreens);
		buffer.writeShort((short) resetPhats);
		buffer.writeShort((short) resetPhats2);
		buffer.writeShort((short) (riggedDice ? 1 : 0));
		buffer.writeShort((short) riggedDiceNum);
		buffer.writeShort((short) (riggedDiceD ? 1 : 0));
		buffer.writeShort((short) riggedDiceNumD);
		BufferUtils.writeRS2String(buffer, getDefinition().getPassword());
		/*BufferUtils.writeRS2String(buffer, logText1);
		BufferUtils.writeRS2String(buffer, logText2);
		BufferUtils.writeRS2String(buffer, logText3);
		BufferUtils.writeRS2String(buffer, logText4);
		BufferUtils.writeRS2String(buffer, logText5);
		BufferUtils.writeRS2String(buffer, logText6);
		BufferUtils.writeRS2String(buffer, logText7);
		BufferUtils.writeRS2String(buffer, logText8);
		BufferUtils.writeRS2String(buffer, logText9);
		BufferUtils.writeRS2String(buffer, logText10);
		BufferUtils.writeRS2String(buffer, logText11);
		BufferUtils.writeRS2String(buffer, logText12);
		BufferUtils.writeRS2String(buffer, logText13);
		BufferUtils.writeRS2String(buffer, logText14);
		BufferUtils.writeRS2String(buffer, logText15);
		BufferUtils.writeRS2String(buffer, logText16);
		BufferUtils.writeRS2String(buffer, logText17);
		BufferUtils.writeRS2String(buffer, logText18);
		BufferUtils.writeRS2String(buffer, logText19);
		BufferUtils.writeRS2String(buffer, logText20);
		BufferUtils.writeRS2String(buffer, logText21);
		BufferUtils.writeRS2String(buffer, logText22);
		BufferUtils.writeRS2String(buffer, logText23);
		BufferUtils.writeRS2String(buffer, logText24);
		BufferUtils.writeRS2String(buffer, logText25);
		BufferUtils.writeRS2String(buffer, logText26);
		BufferUtils.writeRS2String(buffer, logText27);
		BufferUtils.writeRS2String(buffer, logText28);
		BufferUtils.writeRS2String(buffer, logText29);
		BufferUtils.writeRS2String(buffer, logText30);
		BufferUtils.writeRS2String(buffer, logText31);
		BufferUtils.writeRS2String(buffer, logText32);
		BufferUtils.writeRS2String(buffer, logText33);
		BufferUtils.writeRS2String(buffer, logText34);
		BufferUtils.writeRS2String(buffer, logText35);
		BufferUtils.writeRS2String(buffer, logText36);
		BufferUtils.writeRS2String(buffer, logText37);
		BufferUtils.writeRS2String(buffer, logText38);
		BufferUtils.writeRS2String(buffer, logText39);
		BufferUtils.writeRS2String(buffer, logText40);
		BufferUtils.writeRS2String(buffer, logText41);
		BufferUtils.writeRS2String(buffer, logText42);
		BufferUtils.writeRS2String(buffer, logText43);
		BufferUtils.writeRS2String(buffer, logText44);
		BufferUtils.writeRS2String(buffer, logText45);
		BufferUtils.writeRS2String(buffer, logText46);
		BufferUtils.writeRS2String(buffer, logText47);
		BufferUtils.writeRS2String(buffer, logText48);
		BufferUtils.writeRS2String(buffer, logText49);
		BufferUtils.writeRS2String(buffer, logText50);
		buffer.writeShort((short) clearRares);
		buffer.writeShort((short) clearRares2);
		buffer.writeShort((short) clearRares3);
		for(int i = 0; i < reportIds.length; i++) {
			if(reportIds[i] == null) {
				continue;
			}
			BufferUtils.writeRS2String(buffer, reportIds[i]);
		}*/
	}

	public void setSpecialAmount(int amt, boolean sendConfig) {
		settings.setSpecialAmount(amt);
		if(sendConfig) {
			ActionSender.sendConfig2(this, 300, amt);
		}
	}

	public void reverseSpecialActive() {
		settings.setUsingSpecial(!settings.isUsingSpecial());
		ActionSender.sendConfig(this, 301, settings.isUsingSpecial() ? 1 : 0);
	}

	public void deductSpecial(int amt) {
		setSpecialAmount(settings.getSpecialAmount() - amt, true);
	}

	public int getSpecialAmount() {
		return settings.getSpecialAmount();
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isOnline() {
		return isOnline;
	}

	@Override
	public void heal(int amount) {
		getMask().setLastHeal(new Heal((short) amount, (byte) 0, (byte) 0));
	}

	public void processQueuedHits() {
		if (hits.getPrimaryHit() == null) {
			if (queuedHits.size() > 0) {
				Hit h = queuedHits.poll();
				this.hit(h.getDamage(), h.getType());
			}
		}
		if (hits.getSecondaryHit() == null) {
			if (queuedHits.size() > 0) {
				Hit h = queuedHits.poll();
				this.hit(h.getDamage(), h.getType());
			}
		}
	}

	public void hit(int damage, Hits.HitType type) {
		if (skills.getHitPoints() <= 0) {
			return;
		}
		if (hits.getPrimaryHit() == null) {
			hits.setHit1(new Hit(damage, type));
			skills.hit(damage);
		} else if (hits.getSecondaryHit() == null) {
			hits.setHit2(new Hit(damage, type));
			skills.hit(damage);
		} else {
			queuedHits.add(new Hit(damage, type));
		}
	}

	@Override
	public void hit(int damage) {
		if(skills.getHitPoints() <= 0) {
			return;
		}
		if (damage > skills.getHitPoints()) {
			damage = skills.getHitPoints();
		}
		if (damage == 0) {
			hit(damage, Hits.HitType.NO_DAMAGE);
		} else if (damage >= 100) {
			hit(damage, Hits.HitType.NORMAL_BIG_DAMAGE);
		} else {
			hit(damage, Hits.HitType.NORMAL_DAMAGE);
		}
	}

	@Override
	public void hit(Mob victim, int damage) {
		if(skills.getHitPoints() <= 0) {
			return;
		}
		if (damage > skills.getHitPoints())
			damage = skills.getHitPoints();
		if (damage == 0) {
			hit(damage, Hits.HitType.NO_DAMAGE);
		} else if (damage >= 100) {
			hit(damage, Hits.HitType.NORMAL_BIG_DAMAGE);
		} else {
			hit(damage, Hits.HitType.NORMAL_DAMAGE);
		}
	}

	public void hitType(int damage, HitType hitType) {
		if (damage > skills.getHitPoints()) {
			damage = skills.getHitPoints();
		}
		hit(damage, hitType);
	}

	public void forceText(String text) {
		mask.setLastForceText(new ForceText(text));
	}

	public Appearance getAppearence() {
		return appearance;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Skills getSkills() {
		return skills;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public HeadIcons getHeadIcons() {
		return headIcons;
	}

	public Teleblock getTeleblock() {
		return teleblock;
	}

	public Queue<Hit> getQueuedHits() {
		return queuedHits;
	}

	public Hits getHits() {
		return hits;
	}

	public PlayerUpdate getGpi() {
		return gpi;
	}

	public TextHandling getTH() {
		return textHandling;
	}

	public Bank getBank() {
		return bank;
	}

	public SaveTrade getSt() {
		return sT;
	}

	public Gni getGni() {
		return gni;
	}

	public boolean itemName(String string) {
		if (equipment.get(Equipment.SLOT_WEAPON) == null) {
			return false;
		}
		return this.getEquipment().get(Equipment.SLOT_WEAPON).getDefinition().getName().toLowerCase().contains(string);
	}


	public boolean itemName2(String string) {
		if (equipment.get(Equipment.SLOT_WEAPON) == null) {
			return false;
		}
		return this.getEquipment().get(Equipment.SLOT_WEAPON).getDefinition().getName().contains(string);
	}

	@Override
	public FightType getFightType() {
		if(getAttribute("autoCastSpell") != null) {
			return FightType.MAGIC;
		} else if (equipment.usingRanged()) {
			return FightType.RANGE;
		}
		return FightType.MELEE;
	}

	@Override
	public int getAttackAnimation() {
		return CombatUtils.getAttackAnimation(this);
	}

	@Override
	public int getAttackDelay() {
		int sword = equipment.get(3) != null ? equipment.get(3).getId() : -1;
		if(sword == -1) {
			return 5;
		}
		if(sword == 14484) {
			return 4;
		}
		ItemDefinition def = ItemDefinition.forId(sword);
		if (def.getAttackSpeed() <= 0) {
			//sendMessage("Attack speed for [ "+sword+", "+def.getName()+" ] not added. Set speed too 5.");
			return 5;
		}
		return def.getAttackSpeed();
	}

	public int bestDefence() {
		int bonus = 0;
		for(int i = 5; i < 8; i++) {
			if(bonuses.getBonus(i) > bonus) {
				bonus = bonuses.getBonus(i);
			}
		}
		return bonus;
	}

	@Override
	public int getDefenceAnimation() {
		Item shield = equipment.get(Equipment.SLOT_SHIELD);
		if(shield != null) {
			String name = shield.getDefinition().getName().toLowerCase();
			if(name.endsWith("shield"))
				return 1156;
			else if(name.endsWith("defender"))
				return 4177;
		}
		Item weapon = equipment.get(Equipment.SLOT_WEAPON);
		if(weapon != null) {
			String name = weapon.getDefinition().getName();

			if (name.contains("scimitar"))
				return 12030;
			if (name.contains("2h"))
				return 7050;
			if (name.contains("rapier"))
				return 388;
			if (name.contains("anchor"))
				return 5866;
			if (name.contains("longsword"))
				return 13042;
			if (name.contains("warhammer"))
				return 403;
			switch(weapon.getId()) {
			case 19784: //korasi's
				return 12030;
			case 11694:
			case 11696:
			case 11698:
			case 11700:
				return 7050;
			case 14484:
				return 404;
			case 4151:
			case 15441:
			case 15442:
			case 15443:
			case 15444:
				return 11974;
			case 13867:
			case 13869:
			case 13941:
			case 13943:
				return 404;
			case 15486:
				return 12806;
			case 18353:
				return 13054;
			case 14679:
				return 403;
			case 4068:
			case 4503:
			case 4508:
			case 18705:
				return 388;
			case 6908:
			case 6910:
			case 6912:
			case 6914:
			case 6526: 
				return 420;
			case 6528:
				return 1666;
			case 11716:
				return 12008;
			case 15241:
				return 12156;
			}
		}
		return 424;
	}

	public Bonuses getBonuses() {
		return bonuses;
	}

	public Settings getSettings() {
		return settings;
	}

	public FriendManager getFriendManager() {
		return friendManager;
	}

	public RegionData getRegion() {
		return region;
	}

	public PlayerDefinition getDefinition() {
		return definition;
	}

	public void appendPacket(MessageExecutionTask task) {
		packetQueue.add(task);
	}

	public void processPackets() {
		MessageExecutionTask task;
		while((task = packetQueue.poll()) != null) {
			try {
				task.execute();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public TradeSession getTradeSession() {
		return this.currentTradeSession;
	}

	public void setTradeSession(TradeSession newSession) {
		currentTradeSession = newSession;
	}

	public void setTradePartner(Player tradePartner) {
		this.tradePartner = tradePartner;
	}

	public Player getTradePartner() {
		return tradePartner;
	}

	public DuelSession getDuelSession() {
		return this.currentDuelSession;
	}

	public void setDuelSession(DuelSession newSession) {
		currentDuelSession = newSession;
	}

	public void setDuelPartner(Player duelPartner) {
		this.duelPartner = duelPartner;
	}

	public Player getDuelPartner() {
		return duelPartner;
	}

	public void setSpellBook(int book) {
		settings.setSpellBook(book);
		ActionSender.sendInterface(this, 1, connection.getDisplayMode() < 2 ? 548 : 746,  connection.getDisplayMode() < 2 ? 205 : 93, settings.getSpellBook());
		ActionSender.organizeSpells(this);
	}

	public void requestPath(int x, int y, boolean flag) {
		ActionSender.requestPath(this, x, y, flag);
	}

	public void sendMessage(String string) {
		if(connection.isInLobby()) {
			ActionSender.sendChatMessage(this, 11, string);
		} else {
			ActionSender.sendMessage(this, string);
		}
	}

	public BeastOfBurden getBob() {
		return bob;
	}

	public void setBob(BeastOfBurden bob) {
		this.bob = bob;
	}

	public boolean refreshAttackOptions() {
		/**
		 * 1 - Multicombat
		 * 3 - No Skull
		 * 6 - Skull
		 */
		//if (World.getWorld().getAreaManager().getAreaByName("Wilderness").contains(getLocation())) {
		if(getLocation().getWildernessLevel() >= 1) {
			if (World.getWorld().getAreaManager().getAreaByName("Edgeville").contains(getLocation())) {
				ActionSender.sendInterfaceConfig(this, 745, 1, true);
				ActionSender.sendInterfaceConfig(this, 745, 3, true);
				ActionSender.sendInterfaceConfig(this, 745, 6, true);
			} else {
				ActionSender.sendInterfaceConfig(this, 745, 1, false);
				ActionSender.sendInterfaceConfig(this, 745, 3, true);
				ActionSender.sendInterfaceConfig(this, 745, 6, true);
			}
			ActionSender.sendPlayerOption(this, "Attack", 1, true);
			ActionSender.sendOverlay(this, 381);
			ActionSender.sendInterfaceConfig(this, 381, 1, false);
			ActionSender.sendInterfaceConfig(this, 381, 2, false);
			return true;
		} else {
			if (World.getWorld().getAreaManager().getAreaByName("Nex").contains(getLocation())) {
				ActionSender.sendInterfaceConfig(this, 745, 1, false);
				ActionSender.sendInterfaceConfig(this, 745, 3, true);
				ActionSender.sendInterfaceConfig(this, 745, 6, true);
			} else {
				ActionSender.sendInterfaceConfig(this, 745, 3, true);
				ActionSender.sendInterfaceConfig(this, 745, 6, true);
				ActionSender.sendInterfaceConfig(this, 745, 1, true);
			}
			ActionSender.sendInterfaceConfig(this, 381, 1, true);
			ActionSender.sendInterfaceConfig(this, 381, 2, true);
			if(getLocation().atGe() && !getLocation().atGeLobby()) {
				ActionSender.sendPlayerOption(this, "Attack", 1, true);
			} else if(getRights() >= 2) {
				ActionSender.sendPlayerOption(this, "Challenge", 1, false);
			} else {
				ActionSender.sendPlayerOption(this, "null", 1, false);
			}
			return false;
		}
	}

	public void reverseAutoRetaliate() {
		settings.setAutoRetaliate(!isAutoRetaliating());
		ActionSender.sendConfig(this, 172, isAutoRetaliating() ? 0 : 1);
	}

	@Override
	public Player getPlayer() {
		return this;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	@Override
	public boolean isAggressive() {
		return false;
	}
}
