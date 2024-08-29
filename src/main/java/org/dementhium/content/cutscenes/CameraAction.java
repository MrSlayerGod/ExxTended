package org.dementhium.content.cutscenes;

import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 *
 * @author Steve
 *
 */
public class CameraAction extends CutsceneAction {

	private final int x, y, pitch, yaw, speed, height;

	public CameraAction(Player p, int delay, int x, int y, int pitch, int yaw, int speed, int height) {
		super(p, delay);
		this.x = x;
		this.y = y;
		this.pitch = pitch;
		this.yaw = yaw;
		this.speed = speed;
		this.height = height;
	}

	@Override
	public void execute() {
		ActionSender.moveCamera(getPlayer(), x, y, pitch, yaw, height, speed);
	}

}
