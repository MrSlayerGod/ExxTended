package org.dementhium.model.player;

import org.dementhium.model.Location;

public class RegionData {

	private boolean didTeleport;
	private boolean DidMapRegionChange;
	private boolean NeedReload;
	private Location lastMapRegion;
	private Location lastLocation;
	private Player player;

	public RegionData(Player entity) {
		this.player = entity;
		this.setLastLocation(Location.create(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ()));
	}

	public void teleport(int coordX, int coordY, int height) {
		try {
		player.getWalkingQueue().reset();
		Location futurelocation = Location.create(coordX, coordY, height);
		if (lastMapRegion.getRegionX() - futurelocation.getRegionX() >= 4 || lastMapRegion.getRegionX() - futurelocation.getRegionX() <= -4) {
			this.setDidMapRegionChange(true);
		} else if (lastMapRegion.getRegionY() - futurelocation.getRegionY() >= 4 || lastMapRegion.getRegionY() - futurelocation.getRegionY() <= -4) {
			this.setDidMapRegionChange(true);
		}
		this.lastLocation = Location.create(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		player.setLocation(Location.create(coordX, coordY, height));
		this.setDidTeleport(true);
		} catch(Exception e) {
		}
	}


	public void reset() {
		if(player.getWalkingQueue().isDidTele())
			this.setDidTeleport(false);
		this.setDidMapRegionChange(false);
		this.setNeedReload(false);
	}

	public void setDidMapRegionChange(boolean didMapRegionChange) {
		DidMapRegionChange = didMapRegionChange;
	}
	public boolean isDidMapRegionChange() {
		return DidMapRegionChange;
	}

	public void setLastMapRegion(Location lastMapRegion) {
		this.lastMapRegion = lastMapRegion;
	}
	public Location getLastMapRegion() {
		if(lastMapRegion == null) {
			lastMapRegion = player.getLocation();
		}
		return lastMapRegion;
	}

	public void setDidTeleport(boolean didTeleport) {
		this.didTeleport = didTeleport;
	}

	public boolean isDidTeleport() {
		return didTeleport;
	}

	public void setNeedReload(boolean needReload) {
		NeedReload = needReload;
	}

	public boolean isNeedReload() {
		return NeedReload;
	}

	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}

	public Location getLastLocation() {
		return lastLocation;
	}

	public void teleport(Location pos) {
		teleport(pos.getX(), pos.getY(), pos.getZ());
	}

}
