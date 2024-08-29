package org.dementhium.net.packethandlers;

import org.dementhium.model.player.Bank;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.event.Tickable;
import org.dementhium.model.World;

public class WalkingHandler extends PacketHandler {

	public static final int WALK_ONE = 60;
	public static final int WALK_TWO = 71;

	@Override
	public void handlePacket(Player p, Message in) {
		switch(in.getOpcode()) {
		case WALK_ONE:
		case WALK_TWO:
			if(!p.cantTalk) {
				walkTwo(p, in);
			}
			break;
		}
	}

	public void walkOne(Player p, Message in) {
		if(p.isDead()) {
			return;
		}
		if (p.getCombatState().isFrozen() && in.getOpcode() != 71) {
			ActionSender.sendMessage(p, "A magical force stops you from moving.");
			return;
		}
		int steps = (in.getLength() - 5) / 2;
		if(steps > 25)
			return;
		int firstY = in.readShort() - (p.getLocation().getRegionY() - 6) * 8;
		int firstX = in.readShort() - (p.getLocation().getRegionX() - 6) * 8;
		boolean runSteps = in.readByteC() == 1;
		//System.out.println(firstX+","+firstY);
		if((Integer) p.getAttribute("bankScreen", 1) == 1 || (Integer) p.getAttribute("bankScreen", 1) == 2){
			if((Integer) p.getAttribute("bankScreen", 1) == 1){
				p.getBank().setHasEnteredPin(false);
			}
			Bank.resetPinScreen(p);
			//ActionSender.sendLoginMasks(p);
		}
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
		p.getActionManager().clearActions();
		p.getMask().setFacePosition(null);
		p.getMask().setInteractingEntity(null);
		p.getCombatState().setVictim(null);
		p.resetTurnTo();
		p.getWalkingQueue().reset();
		//if(!noZone(firstX, firstY, p.getLocation().getRegionX(), p.getLocation().getRegionY())) {
			p.getWalkingQueue().setIsRunning(runSteps);
			p.getWalkingQueue().addToWalkingQueue(firstX, firstY);
			for(int i = 0; i < steps; i++)  {
				int localX = in.readByte() + firstX;
				int localY = in.readByte() + firstY;
				p.getWalkingQueue().addToWalkingQueue(localX, localY);
			}
		//}
	}

	public void walkTwo(final Player p, Message in) {
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
		if(p.isDead()) {
			return;
		}
		if (p.getCombatState().isFrozen() && in.getOpcode() != 71) {
			ActionSender.sendMessage(p, "A magical force stops you from moving.");
			return;
		}
		int size = in.getLength();
		if(in.getOpcode() == 71) {
			//size -= 14;
		}
		int steps = (size - 7) / 2;
		int firstY = in.readShort() - (p.getLocation().getRegionY() - 6) * 8;
		boolean runSteps = in.readByteC() == 1;
		int firstX = in.readShort() - (p.getLocation().getRegionX() - 6) * 8;

		int index = in.readShort();
		if(index != p.getIndex()) {
			return;
		}

		if((Integer) p.getAttribute("bankScreen", 1) == 1 || (Integer) p.getAttribute("bankScreen", 1) == 2){
			if((Integer) p.getAttribute("bankScreen", 1) == 1){
				p.getBank().setHasEnteredPin(false);
			}
			Bank.resetPinScreen(p);
			ActionSender.closeInter(p);
			ActionSender.closeInventoryInterface(p);
			ActionSender.sendLoginMasks(p);
		}
		p.getActionManager().clearActions();
		p.getMask().setFacePosition(null);
		p.getMask().setInteractingEntity(null);
		p.getCombatState().setVictim(null);
		p.dontRetaliate = true;
		World.getWorld().submit(new Tickable(5) {
			public void execute() {
				p.dontRetaliate = false;
				this.stop();
			}
		});
		p.resetTurnTo();
		p.getWalkingQueue().reset();
		//if(!noZone(firstX, firstY, p.getLocation().getRegionX(), p.getLocation().getRegionY())) {
			p.getWalkingQueue().setIsRunning(runSteps);
			p.getWalkingQueue().addToWalkingQueue(firstX, firstY);
			for(int i = 0; i < steps; i++)  {
				int localX = in.readByte() + firstX;
				int localY = in.readByte() + firstY;
				p.getWalkingQueue().addToWalkingQueue(localX, localY);
			}
		//}
	}

	/*public boolean noZone(int lX, int lY, int rX, int rY) {
		if(rX == 340 && rY == 436) {
			if(lY == 55 || lY == 56) {
				if(lX >= 48 && lX <= 55 && (lY == 47 || lY == 56)) {
					return true;
				}
			}
		}
		return false;
	}*/

}
