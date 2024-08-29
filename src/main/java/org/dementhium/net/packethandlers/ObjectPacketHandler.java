package org.dementhium.net.packethandlers;

import org.dementhium.action.Action;
import org.dementhium.content.areas.CoordinateEvent;
import org.dementhium.content.misc.WildernessDitch;
import org.dementhium.content.skills.Woodcutting;
import org.dementhium.content.skills.Woodcutting.Tree;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.AStarPathFinder;
import org.dementhium.model.map.GameObjectDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.event.Tickable;
import org.dementhium.model.player.Bank;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public class ObjectPacketHandler extends PacketHandler {

	public static final int OPTION_1 = 19, OPTION_2 = 80, OPTION_3 = 16;

	@Override
	public void handlePacket(Player player, Message packet) {
		if(player.getAttribute("busy") == Boolean.TRUE) {
			return;
		}
		player.getActionManager().clearActions();
		switch(packet.getOpcode()) {
		case OPTION_1:
			handleOptionOne(player, packet);
			break;
		case OPTION_2:
			handleOptionTwo(player, packet);
			break;
		case OPTION_3:
			handleOptionThree(player, packet);
			break;
		}
	}

	private void handleOptionOne(final Player player, Message packet) {
		try {
			if((Integer) player.getAttribute("bankScreen", 1) == 1 || (Integer) player.getAttribute("bankScreen", 1) == 2){
				if((Integer) player.getAttribute("bankScreen", 1) == 1){
					player.getBank().setHasEnteredPin(false);
				}
				Bank.resetPinScreen(player);
				ActionSender.closeInter(player);
				ActionSender.closeInventoryInterface(player);
				
			}
			final int coordY = packet.readLEShortA();
			final int coordX = packet.readLEShortA();
			final int objectId = packet.readLEShort();
			int height = packet.readByteC();

			World.getWorld().doPath(new AStarPathFinder(), player, coordX, coordY);

			final GameObjectDefinition definition = GameObjectDefinition.forId(objectId);

			final Location location = Location.create(coordX, coordY, height);
		

			player.setCoordinateEvent(new CoordinateEvent(player, coordX, coordY, definition.getSizeX(), definition.getSizeY()) {
				@Override
				public void execute() {
					if(diagonal(player.getLocation(), coordX, coordY)) {
						if(!player.getWalkingQueue().isMoving()) {
							int moveX = player.getLocation().getX(), moveY = coordY;
							World.getWorld().doPath(new AStarPathFinder(), player, moveX, moveY);
							moveX = coordX;
							moveY = player.getLocation().getY();
							World.getWorld().doPath(new AStarPathFinder(), player, moveX, moveY);
						}
					}
					World.getWorld().submit(new Tickable(1) {
						public void execute() {
							player.getMask().setFacePosition(location);
							this.stop();
						}
					});
					if(objectId >= 1440 && objectId <= 1443) {
						player.registerAction(new Action(1) {
							@Override
							public void execute() {
								if(player.getLocation().getY() == 3520 || player.getLocation().getY() == 3523) {
									WildernessDitch.cross(player);
									stop();
								}
							}
						});
					} else if(objectId == 26972) {
						player.getBank().openBank();
					} else {
						if(definition != null) {
							Tree tree = Tree.forId(objectId);
							if(tree != null) {
								player.registerAction(new Woodcutting(tree));
								return;
							}
						}
					}
				}
			});
		} catch(Exception e) {
		}
	}

	private void handleOptionTwo(final Player player, Message packet) {
		try {
			if((Integer) player.getAttribute("bankScreen", 1) == 1 || (Integer) player.getAttribute("bankScreen", 1) == 2){
				if((Integer) player.getAttribute("bankScreen", 1) == 1){
					player.getBank().setHasEnteredPin(false);
				}
				Bank.resetPinScreen(player);
				ActionSender.closeInter(player);
				ActionSender.closeInventoryInterface(player);
				
			}
			final int objectId = packet.readLEShortA();
			int coordX = packet.readShort();
			int coordY = packet.readLEShortA();
			int height = packet.readByteC();
			GameObjectDefinition definition = GameObjectDefinition.forId(objectId);

			World.getWorld().doPath(new AStarPathFinder(), player, coordX, coordY);

			Location location = Location.create(coordX, coordY, height);

			player.getMask().setFacePosition(location);

			if(objectId == 26972) {
				player.setCoordinateEvent(new CoordinateEvent(player, coordX, coordY, definition.getSizeX(), definition.getSizeY()) {
				@Override
					public void execute() {
						player.getBank().openBank();
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void handleOptionThree(final Player player, Message packet) {
		try {
			if((Integer) player.getAttribute("bankScreen", 1) == 1 || (Integer) player.getAttribute("bankScreen", 1) == 2){
				if((Integer) player.getAttribute("bankScreen", 1) == 1){
				player.getBank().setHasEnteredPin(false);
				}
				Bank.resetPinScreen(player);
				ActionSender.closeInter(player);
				ActionSender.closeInventoryInterface(player);
				
			}
			int height = packet.readByte();
			int coordY = packet.readShortA();
			int coordX = packet.readShortA();
			final int objectId = packet.readShort();
			GameObjectDefinition definition = GameObjectDefinition.forId(objectId);

			World.getWorld().doPath(new AStarPathFinder(), player, coordX, coordY);

			Location location = Location.create(coordX, coordY, height);

			player.getMask().setFacePosition(location);

			if(objectId == 26972) {
				player.setCoordinateEvent(new CoordinateEvent(player, coordX, coordY, definition.getSizeX(), definition.getSizeY()) {
					@Override
					public void execute() {
						player.getBank().openBank();
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean diagonal(Location l, int x, int y) {
		int xDial = Math.abs(l.getX() - x);
		int yDial = Math.abs(l.getY() - y);
		return xDial == 1 && yDial == 1;
	}

}
