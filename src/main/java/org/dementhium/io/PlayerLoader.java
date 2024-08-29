package org.dementhium.io;

import java.nio.ByteBuffer;

import org.dementhium.model.World;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.net.GameSession;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;


/**
 * Class that handles player loading
 * @author 'Mystic Flow
 */
public final class PlayerLoader extends FileUtilities {

	public static final String DIRECTORY = "data/games/";
	public static final String EXTENSION = ".bin";

	public class PlayerLoadResult {

		private final Player player;
		private final int returnCode;

		private PlayerLoadResult(Player player, int returnCode) {
			this.returnCode = returnCode;
			this.player = player;
		}

		public Player getPlayer() {
			return player;
		}

		public int getReturnCode() {
			return returnCode;
		}

	}

	public PlayerLoadResult load(GameSession connection, PlayerDefinition def) {
		Player player = null;
		LoginCode code = LoginCode.LoginSuccess;

		if(def.getName() == null || def.getPassword() == null) {
			code = LoginCode.InvalidPassword;
		}

		Player lobbyPlayer = World.getWorld().getPlayerOutOfLobby(def.getName());

		if (World.getWorld().isOnList(def.getName()) && lobbyPlayer == null) {
			code = LoginCode.AlreadyLoggedIn;
		}
		if (World.getWorld().getBanManager().contains(def.getName())) {
			code = LoginCode.SuspendedAccount;
		}
		if(lobbyPlayer == null) {
			if(exists(DIRECTORY + def.getName() + EXTENSION)) {
				boolean password = def.getPassword().equals(getPassword(new Player(connection, def)));
				if(code == LoginCode.LoginSuccess && !password) {
					code = LoginCode.InvalidPassword;
				}
			}
		}

		if(code == LoginCode.LoginSuccess) {
			player = new Player(connection, def);
		}

		if(player != null) {
			if(!exists(DIRECTORY + def.getName() + EXTENSION)) {
				save(player);
			}
		}
		return new PlayerLoadResult(player, code.code);
	}

	public boolean load(Player player) {
		try {
			ByteBuffer data = fileBuffer(DIRECTORY + player.getUsername() + EXTENSION);
			player.load(data);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public boolean save(Player player) {
		try {
			ChannelBuffer saveBuffer = ChannelBuffers.dynamicBuffer();
			player.save(saveBuffer);
			writeBufferToFile(DIRECTORY + player.getUsername() + EXTENSION, saveBuffer.toByteBuffer());
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getPassword(Player player) {
		try {
			ByteBuffer data = fileBuffer(DIRECTORY + player.getUsername() + EXTENSION);
			return player.load(data);
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
