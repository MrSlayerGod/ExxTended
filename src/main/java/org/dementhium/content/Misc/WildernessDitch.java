package org.dementhium.content.misc;

import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class WildernessDitch {
	
	private static final Animation CROSS_ANIMATION = Animation.create(6703);
	
	public static void cross(Player player) {
		player.setAttribute("busy", Boolean.TRUE);
		
		boolean north = player.getLocation().getY() > 3520;
		
		int y = north ? -3 : 3;
		int dir = north ? 2 : 0;
		//Animation.create(6132)
		player.forceMovement(CROSS_ANIMATION, new int[] {player.getLocation().getX(), player.getLocation().getY() + y, 33, 60, dir, 2}, 1, true);
	}
	
}
