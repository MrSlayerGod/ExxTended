package org.dementhium.model;

import org.dementhium.event.Tickable;
import org.dementhium.model.map.Region;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

public class ProjectileManager {

	public static void sendGlobalProjectile(final int projectileId, final Mob shooter, final Mob target, final int startHeight, final int endHeight, final int speed) {
		for (final Player p : Region.getLocalPlayers(shooter.getLocation(), 12)) {
			if (p == null)
				continue;
			if (p.getLocation().withinDistance(shooter.getLocation()) || p.getLocation().withinDistance(target.getLocation()))
				ActionSender.sendProjectile(p, projectileId, target, shooter.getLocation(), target.getLocation(), startHeight, endHeight, speed);
		}
	}

	public static void sendGlobalProjectile(final int projectileId, final Mob shooter, final Mob target, final Location loc, final int startHeight, final int endHeight, final int speed) {
		for (final Player p : Region.getLocalPlayers(shooter.getLocation(), 12)) {
			if (p == null)
				continue;
			if (p.getLocation().withinDistance(shooter.getLocation()) || p.getLocation().withinDistance(loc))
				ActionSender.sendProjectile(p, projectileId, target, shooter.getLocation(), loc, startHeight, endHeight, speed);
		}
	}

	public static void sendDelayedProjectile(final Mob from, final Mob to, final int projectile, boolean instant) {
		World.getWorld().submit(new Tickable(1) {
			public void execute() {
				this.stop();
				sendGlobalProjectile(projectile, from, to, 160, 140, 45);
			}
		});
	}

	public static void sendGlobalProjectile(final Mob from, final Mob to, final int projectile) {
		sendGlobalProjectile(projectile, from, to, 160, 140, 45);
	}

	public static void sendGlobalProjectile(final Mob from, final Mob to, final int projectile, final int startHeight, final int endHeight) {
		sendGlobalProjectile(projectile, from, to, startHeight, endHeight, 45);
	}

	public static void sendGlobalProjectile(final Mob from, final Mob to, final int projectile, final int startHeight, final int endHeight, int speed) {
		sendGlobalProjectile(projectile, from, to, startHeight, endHeight, speed);
	}
	
	public static void sendDelayedProjectile(final Mob from, final Mob to, final int projectile, final int startHeight, final int endHeight, boolean instant) {
		World.getWorld().submit(new Tickable(1) {
			public void execute() {
				this.stop();
				sendGlobalProjectile(projectile, from, to, startHeight, endHeight, 45);
			}
		});
	}

	public static int getTicks(Location l1, Location l2) {
		return 2;
		//return (int) Math.round(l1.getDistance(l2)) > 4 ? 3 : 2;
	}

}
