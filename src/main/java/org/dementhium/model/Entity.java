package org.dementhium.model;

import org.dementhium.model.GroundItemManager.GroundItem;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author 'Mystic Flow <Steven@rune-server.org>
 *
 */
public abstract class Entity {

	protected Location location;

	public void setLocation(Location location) {
		if(this.location != null) {
			this.location.remove(this);
		}
		if(location != null) {
			location.add(this);
			this.location = location;
		}
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void destroy() {
		if(location != null) {
			location.remove(this);
		}
	}

	public boolean isPlayer() { 
		return false;
	}
	public boolean isNPC() { 
		return false;
	}
	public boolean isGroundItem() {
		return false;
	}
	public GroundItem getGroundItem() {
		return null;
	}
	public Player getPlayer() { 
		return null; 
	}
	public NPC getNpc() { 
		return null; 
	}

}
