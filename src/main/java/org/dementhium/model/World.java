package org.dementhium.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dementhium.GameExecutor;
import org.dementhium.content.BanManager;
import org.dementhium.content.areas.AreaManager;
import org.dementhium.content.areas.CoordinateEvent;
import org.dementhium.content.clans.ClanManager;
import org.dementhium.content.skills.magic.MagicHandler;
import org.dementhium.event.Event;
import org.dementhium.event.Tickable;
import org.dementhium.event.impl.AutoSaveEvent;
import org.dementhium.event.impl.ClanSaveEvent;
import org.dementhium.event.impl.UpdateEvent;
import org.dementhium.identifiers.IdentifierManager;
import org.dementhium.io.PlayerLoader;
import org.dementhium.io.PlayerLoader.PlayerLoadResult;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.map.PathFinder;
import org.dementhium.model.map.Region;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.npc.NPCDropLoader;
import org.dementhium.model.npc.NPCLoader;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.ShopManager;
import org.dementhium.net.GameSession;
import org.dementhium.net.PacketManager;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.task.Task;
import org.dementhium.task.impl.SessionLoginTask;
import org.dementhium.util.Constants;
import org.dementhium.util.EntityList;
import org.dementhium.util.Misc;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 *
 * @author 'Mystic Flow
 * @author Khaled
 * @author `Discardedx2
 */
public final class World {

	private static final World INSTANCE = new World();
	
	public static World getWorld() {
		return INSTANCE;
	}

	private final GameExecutor executor = new GameExecutor();

	private EntityList<Player> players = new EntityList<Player>(Constants.MAX_AMT_OF_PLAYERS);
	private EntityList<Player> lobbyPlayers = new EntityList<Player>(Constants.MAX_AMT_OF_PLAYERS);
	private EntityList<NPC> npcs = new EntityList<NPC>(Constants.MAX_AMT_OF_NPCS);
	private ShopManager shopManager = new ShopManager();
	private NPCDropLoader npcDropLoader = new NPCDropLoader();
	private LinkedList<Tickable> tickablesToAdd = new LinkedList<Tickable>();
	private LinkedList<Tickable> tickables = new LinkedList<Tickable>();

	private final PacketManager packetManager = new PacketManager();
	private final PlayerLoader playerLoader = new PlayerLoader();

	private final GroundItemManager groundItemManager = new GroundItemManager();

	private ClanManager clanManager;
	private AreaManager areaManager;
	private BanManager banManager = new BanManager();

	private World() {
	}

	public void load() throws Exception {
		clanManager = new ClanManager();
		areaManager = new AreaManager();
		banManager.load();
		npcDropLoader.load();
		MagicHandler.load();
		ItemDefinition.init();
		WeaponInterface.init();
		NPCDefinition.init();
		NPCLoader.loadSpawns();
		IdentifierManager.registerIdentifiers();
		Region.preLoad();
		shopManager.loadShops();
		registerEvents();
	}

	public void registerEvents() {
		//events
		schedule(new UpdateEvent());
		schedule(new AutoSaveEvent());
		schedule(new ClanSaveEvent());
	}
	


	public void load(final GameSession connection, final PlayerDefinition definition) {
		if(connection != null && definition != null) {
			executor.submitWork(new Runnable() {
				public void run() {
					PlayerLoadResult result = playerLoader.load(connection, definition);

					int code = result.getReturnCode();
					if (code != 2) {
						connection.write(new MessageBuilder().writeByte(code).toMessage()).addListener(ChannelFutureListener.CLOSE);
					} else {
						connection.setPlayer(result.getPlayer());
						if (playerLoader.load(result.getPlayer())) {
							submit(new SessionLoginTask(result.getPlayer()));
						}
					}
				}
			});
		}
	}

	public void register(final Player player) {
		int code = 2;
		if(player.getConnection().isInLobby()) {
			if (!lobbyPlayers.add(player)) {
				code = 7;
			}
		} else {
			if (!players.add(player)) {
				code = 7;
			}
		}
		player.getConnection().write(new MessageBuilder().writeByte(code).toMessage());
		if (code == 2) {
			player.loadPlayer();
		}
	}

	public void unregister(final Player player) {
		if(player.getBob() != null) {
			player.getBob().dismiss();
		}
		clanManager.leaveClan(player);
		player.setOnline(false);
		if(player.getConnection().isInLobby()) {
			lobbyPlayers.remove(player);
		} else {
			players.remove(player);
		}
		executor.submitWork(new Runnable() {
			@Override
			public void run() {
				playerLoader.save(player);
			}
		});
		String name = Misc.formatPlayerNameForDisplay(player.getUsername());
		for(Player p : World.getWorld().getPlayers()) {
			if(p.getFriendManager().getFriends().contains(name)) {
				p.getFriendManager().updateFriend(name);
			}
		}
	}

	public boolean isOnList(String name) {
		name = Misc.formatPlayerNameForProtocol(name);
		for (Player p : players)
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equalsIgnoreCase(name))
				return true;
		for (Player p : lobbyPlayers)
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equalsIgnoreCase(name))
				return true;

		return false;
	}

	public boolean isInGame(String name) {
		name = Misc.formatPlayerNameForProtocol(name);
		for (Player p : players)
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equalsIgnoreCase(name))
				return true;
		/*for(Player pl : lobbyPlayers)
			if (Misc.formatPlayerNameForProtocol(pl.getUsername()).equalsIgnoreCase(name))
				return true;*/

		return false;
	}

	public boolean isInGame(String name, Player player) {
		name = Misc.formatPlayerNameForProtocol(name);
		for (Player p : players) {
			if(p == player) {
				continue;
			}
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equalsIgnoreCase(name)) {
				return true;
			}
		}
		/*for(Player pl : lobbyPlayers) {
			if(pl == player) {
				continue;
			}
			if (Misc.formatPlayerNameForProtocol(pl.getUsername()).equalsIgnoreCase(name)) {
				return true;
			}
		}*/

		return false;
	}

	public Player getPlayerInServer(String name) {
		name = Misc.formatPlayerNameForProtocol(name);
		for (Player p : players)
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equalsIgnoreCase(name))
				return p;
		for (Player p : lobbyPlayers)
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equalsIgnoreCase(name))
				return p;

		return null;
	}

	public Player getPlayerOutOfLobby(String name) {
		name = Misc.formatPlayerNameForProtocol(name);
		for(Player pl : lobbyPlayers) {
			if(pl.getUsername().equalsIgnoreCase(name)) {
				return pl;
			}
		}
		return null;
	}

	public EntityList<NPC> getNpcs() {
		return npcs;
	}

	public EntityList<Player> getPlayers() {
		return players;
	}

	public PacketManager getPacketManager() {
		return packetManager;
	}

	public PlayerLoader getPlayerLoader() {
		return playerLoader;
	}

	public ClanManager getClanManager() {
		return clanManager;
	}

	public EntityList<Player> getLobbyPlayers() {
		return lobbyPlayers;
	}

	public AreaManager getAreaManager() {
		return areaManager;
	}

	public ShopManager getShopManager() {
		return shopManager;
	}

	public GroundItemManager getGroundItemManager() {
		return groundItemManager;
	}
	
	public BanManager getBanManager() {
		return banManager;
	}


	public NPCDropLoader getNpcDropLoader() {
		return npcDropLoader;
	}


	public void submitCoordinateEvent(final Mob mob, final CoordinateEvent coordinateEvent) {
		submit(new Tickable(1) {

			private int attempts;

			@Override
			public void execute() {
				if(mob.getCoordinateEvent() == null || ++attempts >= 20) {
					stop();
					return;
				}
				if(!mob.getCoordinateEvent().equals(coordinateEvent)) {
					stop();
					return;
				}
				if(!mob.getWalkingQueue().isMoving() && mob.getCoordinateEvent().inArea()) {
					mob.getCoordinateEvent().execute();
					mob.stopCoordinateEvent();
					stop();
				}
			}
		});
	}

	public void submit(Event event) {
		executor.schedule(event);
	}

	public void schedule(Event event) {
		executor.scheduleAtFixedRate(event);
	}

	public void submit(Task task) {
		executor.submit(task);
	}

	public void submit(Tickable tickable) {
		tickablesToAdd.add(tickable);
	}

	public GameExecutor getExecutor() {
		return executor;
	}

	public void processTickables() {
		if (tickablesToAdd.size() > 0) {
			tickables.addAll(tickablesToAdd);
			tickablesToAdd = new LinkedList<Tickable>();
		}
		for (Iterator<Tickable> it = tickables.iterator(); it.hasNext();) {
			Tickable t = it.next();
			if (t.isRunning()) {
				t.run();
			} else {
				it.remove();
			}
		}
	}

	public boolean doPath(PathFinder pathFinder, Mob mob, int x, int y) {
		return doPath(pathFinder, mob, x, y, false);
	}

	public boolean doPath(final PathFinder pathFinder, final Mob mob, final int x, final int y, final boolean ignoreLastStep) {
		final Location destination = Location.create(x, y, mob.getLocation().getZ());
		Runnable r = new Runnable() {
			public void run() {
				Location base = mob.getLocation();
				int srcX = base.getLocalX();
				int srcY = base.getLocalY();
				int destX = destination.getLocalX(base);
				int destY = destination.getLocalY(base);
				List<Location> path = (List<Location>) pathFinder.findPath(mob.getLocation(), srcX, srcY, destX, destY, mob.getLocation().getZ(), 0, mob.getWalkingQueue().isRunning(), ignoreLastStep, true);
				if (path != null) {
					try {
						mob.getWalkingQueue().reset();
						for (Location step : path) {
							mob.addPoint(step.getX(), step.getY());
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				} 
			}
		};
		r.run();
		return true;
	}

}
