package org.dementhium.util;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;

import java.util.StringTokenizer;
import java.text.*;


public class Misc {

	public static String formatInteger(int num) {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		return df.format((int)num)+"";
	}

	public static String formatIP(String ip) {
		int spot = ip.indexOf(":");
		String replace = ip.substring(spot);
		ip = ip.replaceAll("/", "");
		ip = ip.replaceAll(replace, "");
		return ip;
	}

	public static int percentage(double d) {
		return (int) (d * 100);
	}	

	public static int IPAddressToNumber(String ipAddress) {
		StringTokenizer st = new StringTokenizer(ipAddress, ".");
		int[] ip = new int[4];
		int i = 0;
		while (st.hasMoreTokens())
			ip[i++] = Integer.parseInt(st.nextToken());
		return ((ip[0] << 24) | (ip[1] << 16) | (ip[2] << 8) | (ip[3]));
	}

	public static int random(int random) {
		return (int) (Math.random() * (random + 1));
	}

	public static int random(double random) {
		return (int) (Math.random() * (random + 1));
	}

	/**
	 * Format a player's name for use in the protocol.
	 * 
	 * @return
	 */
	public static String formatPlayerNameForProtocol(String name) {
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}

	public static String formatPlayerNameForDisplay(String name) {
		Player player = World.getWorld().getPlayerInServer(name);
		if(player == null) {
			name = name.replaceAll("_", " ");
			name = name.toLowerCase();
			StringBuilder newName = new StringBuilder();
			boolean wasSpace = true;
			for (int i = 0; i < name.length(); i++) {
				if (wasSpace) {
					newName.append(("" + name.charAt(i)).toUpperCase());
					wasSpace = false;
				} else {
					newName.append(name.charAt(i));
				}
				if (name.charAt(i) == ' ') {
					wasSpace = true;
				}
			}
			return newName.toString();
		}
		return player.realUsername;
	}

	public static String longToString(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
			return null;
		if (l % 37L == 0L)
			return null;
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static long stringToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	public static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e',
		'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
		's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9' };

	public static int packGJString2(int position, byte[] buffer, String string) {
		int length = string.length();
		int offset = position;
		for (int i_6_ = 0; length > i_6_; i_6_++) {
			int character = string.charAt(i_6_);
			if (character > 127) {
				if (character > 2047) {
					buffer[offset++] = (byte) ((character | 919275) >> 12);
					buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
					buffer[offset++] = (byte) (128 | (character & 63));
				} else {
					buffer[offset++] = (byte) ((character | 12309) >> 6);
					buffer[offset++] = (byte) (128 | (character & 63));
				}
			} else
				buffer[offset++] = (byte) character;
		}
		return offset - position;
	}

	public static final byte[] DIRECTION_DELTA_X = new byte[] {-1, 0, 1, -1, 1, -1, 0, 1};
	public static final byte[] DIRECTION_DELTA_Y = new byte[] {-1, -1, -1, 0, 0, 1, 1, 1};
	public static final byte[] NPC_DIRECTION_DELTA_Y = new byte[] {-1, 0, 1, -1, 1, -1, 0, 1};
	public static final byte[] NPC_DIRECTION_DELTA_X = new byte[] {-1, -1, -1, 0, 0, 1, 1, 1};


	public static int getFacingDirection(int coordX, int coordY, int faceX, int faceY) {
		if (faceX > coordX) {
			if (faceY > coordY)
				return 10240;
			else if (faceY < coordY)
				return 14336;
			else
				return 12288;
		} else if (faceX >= coordX) {
			if (coordY < faceY)
				return 8192;
			else if (faceY < coordY)
				return 0;
		} else if (faceY <= coordY) {
			if (faceY < coordY)
				return 2048;
			else
				return 4096;
		} else
			return 6144;
		return -1;
	}


	/**
	 * Walk dirs
	 * 0	-	South-West
	 * 1	-	South	
	 * 2	-	South-East
	 * 3	-	West
	 * 4	-	East
	 * 5	-	North-West
	 * 6	-	North
	 * 7	-	North-East
	 */
	public static int walkingDirection(int dx, int dy) {
		if(dx < 0 && dy < 0) {
			return 0;
		}
		if(dx == 0 && dy < 0) {
			return 1;
		}
		if(dx > 0 && dy < 0) {
			return 2;
		}
		if(dx < 0 && dy == 0) {
			return 3;
		}
		if(dx > 0 && dy == 0) {
			return 4;
		}
		if(dx < 0 && dy > 0) {
			return 5;
		}
		if(dx == 0 && dy > 0) {
			return 6;
		}
		if(dx > 0 && dy > 0) {
			return 7;
		}
		return -1;
	}


	public static int runningDirection(int dx, int dy) {
		if(dx == -2 && dy == -2)
			return 0;
		if(dx == -1 && dy == -2)
			return 1;
		if(dx == 0 && dy == -2)
			return 2;
		if(dx == 1 && dy == -2)
			return 3;
		if(dx == 2 && dy == -2)
			return 4;
		if(dx == -2 && dy == -1)
			return 5;
		if(dx == 2 && dy == -1)
			return 6;
		if(dx == -2 && dy == 0)
			return 7;
		if(dx == 2 && dy == 0)
			return 8;
		if(dx == -2 && dy == 1)
			return 9;
		if(dx == 2 && dy == 1)
			return 10;
		if(dx == -2 && dy == 2)
			return 11;
		if(dx == -1 && dy == 2)
			return 12;
		if(dx == 0 && dy == 2)
			return 13;
		if(dx == 1 && dy == 2)
			return 14;
		if(dx == 2 && dy == 2)
			return 15;


		return -1;
	}

	public static int getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = coordX1 - coordX2;
		int deltaY = coordY1 - coordY2;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	public static String UncapitaloptimizeText(String text) {
		char buf[] = text.toCharArray();
		return new String(buf, 0, buf.length);
	}

	public static String optimizeText(String text) {
		char buf[] = text.toLowerCase().toCharArray();
		boolean endMarker = true;
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;
				endMarker = false;
			}
			if (c == '.' || c == '!' || c == '?') {
				endMarker = true;
			}
		}
		return new String(buf, 0, buf.length);
	}

	public static String upper(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}


	public static String checkString(String l) {
		char s[] = l.toCharArray();
		int count = 0;
		for (int i = 0; i < s.length; i++) {
			char c = s[i];
			if(c == '_') {
				s[i] = ' ';
				c = s[i];
			}
			if(count == 0) {
				s[i] -= 0x20;
				count = 1;
			}
			if (c == '.' || c == '!' || c == '?' || c == ' ') {
				s[i+1] -= 0x20;
			}
		}
		return new String(s, 0, s.length);
	}

	public static int pathfindingDirToClientDir(int pathDir) {
		switch(pathDir) {
		case 0x1:
			return 4;
		case 0x2:
			return 3;
		case 0x4:
			return 1;
		case 0x8:
			return 6;
		case 0x6:
			return 0;
		case 0x9:
			return 7;//2
		case 0xA:
			return 5;
		case 0x5:
			return 2; //7
		default:
			return -1;
		}
	}
}
