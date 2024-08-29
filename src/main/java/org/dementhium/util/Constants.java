package org.dementhium.util;

import org.dementhium.mysql.*;

public class Constants {

	public final static int ALLOWED_LAG = 6000;
	
	public final static int REVISION = 614;
	
	public static final int SPEC_BAR_FULL = 1000;
	public static final int SPEC_BAR_HALF = 500;

	public static final boolean ANTI_FARM = false;
	public static final boolean ANTI_MULE = false;
	public static final boolean ADMIN_TRADE_ENABLED = false;
	public static boolean VOTE_ENABLED = false;
	public static final boolean SQL_ENABLED = false;
	
	public final static byte DISCONNECT = -1;
	public final static byte GET_CONNECTION_ID = 0;
	public final static byte LOGIN_START = 1;
	public final static byte LOGIN_CYPTION = 2;
	public final static byte CHECK_ACC_NAME = 3;
	public final static byte CHECK_ACC_COUNTRY = 4;
	public final static byte UPDATE_SERVER = 6;
	
	/* Bonus Weekend */
	public static boolean DOUBLE_BONUS_WEEKEND = false;
	public static int BONUS_AMOUNT = 1;
	
	public final static byte MAKE_ACC = 5;
	public final static byte REMOVE_ID = 100;
	public static final byte LOGIN_OK = 2;
	public static final byte INVALID_PASSWORD = 3;
	public static final byte BANNED = 4;
	public static final byte ALREADY_ONLINE = 5;
	public static final byte WORLD_FULL = 7;
	public static final byte TRY_AGAIN = 11;

	public static final short MAX_AMT_OF_PLAYERS = 2048;
	public static final short MAX_AMT_OF_NPCS = 2048;

	public static final byte LOBBY_PM_CHAT_MESSAGE = 0;
	public static final byte LOBBY_CLAN_CHAT_MESSAGE = 11;
	public static final byte COMMANDS_MESSAGE = 99;
	
	public static final boolean TESTING_LOCALHOST = true;

	public static final vBulletin vb = new vBulletin("","","","", vBulletin.Type.vBulletin);

	private static DatabaseConnection database = null;
	private static Vote voteDatabase = null;
	private static Highscores hiDatabase = null;
	private static HighscoresPK hiPkDatabase = null;
	private static PlayersOnline playersOnline = null;
	private static ReportLogs reportLogs = null;

	public static final byte[] PACKET_SIZES = new byte[256];


	static {
		for (int i = 0; i < 256; i++) {
			PACKET_SIZES[i] = -3;
		}
		PACKET_SIZES[0] = 8;
		PACKET_SIZES[1] = -1;
		PACKET_SIZES[2] = -1;
		PACKET_SIZES[3] = -1;
		PACKET_SIZES[4] = 1;
		PACKET_SIZES[5] = 4;
		PACKET_SIZES[6] = 0;
		PACKET_SIZES[7] = 6;
		PACKET_SIZES[8] = -1;
		PACKET_SIZES[9] = 7;
		PACKET_SIZES[10] = 16;
		PACKET_SIZES[11] = 3;
		PACKET_SIZES[12] = -1;
		PACKET_SIZES[13] = 8;
		PACKET_SIZES[14] = 8;
		PACKET_SIZES[15] = 3;
		PACKET_SIZES[16] = 7;
		PACKET_SIZES[17] = 4;
		PACKET_SIZES[18] = 2;
		PACKET_SIZES[19] = 7;
		PACKET_SIZES[20] = 8;
		PACKET_SIZES[21] = 3;
		PACKET_SIZES[22] = 2;
		PACKET_SIZES[23] = 15;
		PACKET_SIZES[24] = 8;
		PACKET_SIZES[25] = -1;
		PACKET_SIZES[26] = 3;
		PACKET_SIZES[27] = -1;
		PACKET_SIZES[28] = 11;
		PACKET_SIZES[29] = 7;
		PACKET_SIZES[30] = 0;
		PACKET_SIZES[31] = 2;
		PACKET_SIZES[32] = 2;
		PACKET_SIZES[33] = 7;
		PACKET_SIZES[34] = 6;
		PACKET_SIZES[35] = 4;
		PACKET_SIZES[36] = 3;
		PACKET_SIZES[37] = -1;
		PACKET_SIZES[38] = 15;
		PACKET_SIZES[39] = 0;
		PACKET_SIZES[40] = 8;
		PACKET_SIZES[41] = 4;
		PACKET_SIZES[42] = 3;
		PACKET_SIZES[43] = 7;
		PACKET_SIZES[44] = 4;
		PACKET_SIZES[45] = 2;
		PACKET_SIZES[46] = -1;
		PACKET_SIZES[47] = -1;
		PACKET_SIZES[48] = 8;
		PACKET_SIZES[49] = 4;
		PACKET_SIZES[50] = 3;
		PACKET_SIZES[51] = -1;
		PACKET_SIZES[52] = 8;
		PACKET_SIZES[53] = -1;
		PACKET_SIZES[54] = -1;
		PACKET_SIZES[55] = 8;
		PACKET_SIZES[56] = 7;
		PACKET_SIZES[57] = 11;
		PACKET_SIZES[58] = 12;
		PACKET_SIZES[59] = 3;
		PACKET_SIZES[60] = -1;
		PACKET_SIZES[61] = 4;
		PACKET_SIZES[62] = -1;
		PACKET_SIZES[63] = 3;
		PACKET_SIZES[64] = 7;
		PACKET_SIZES[65] = 3;
		PACKET_SIZES[66] = 3;
		PACKET_SIZES[67] = -1;
		PACKET_SIZES[68] = 2;
		PACKET_SIZES[69] = -1;
		PACKET_SIZES[70] = 3;
		PACKET_SIZES[71] = -1;
		PACKET_SIZES[72] = -1;
		PACKET_SIZES[73] = 6;
		PACKET_SIZES[74] = 3;
		PACKET_SIZES[75] = -1;
		PACKET_SIZES[76] = 3;
		PACKET_SIZES[77] = -1;
		PACKET_SIZES[78] = 7;
		PACKET_SIZES[79] = 8;
		PACKET_SIZES[80] = 7;
		PACKET_SIZES[81] = -1;
		PACKET_SIZES[82] = 16;
		PACKET_SIZES[83] = 1;
	}

	public static DatabaseConnection getDatabase() {
		if(database == null) {
			database = new DatabaseConnection();
		}
		return database;
	}

	public static Vote getVoteDatabase() {
		if(voteDatabase == null) {
			voteDatabase = new Vote();
		}
		return voteDatabase;
	}

	public static Highscores getHiscores() {
		if(hiDatabase == null) {
			hiDatabase = new Highscores();
		}
		return hiDatabase;
	}

	public static HighscoresPK getHiscoresPK() {
		if(hiPkDatabase == null) {
			hiPkDatabase = new HighscoresPK();
		}
		return hiPkDatabase;
	}

	public static PlayersOnline getPlayersOnline() {
		if(playersOnline == null) {
			playersOnline = new PlayersOnline();
		}
		return playersOnline;
	}

	public static ReportLogs getLogDatabase() {
		if(reportLogs == null) {
			reportLogs = new ReportLogs();
		}
		return reportLogs;
	}
}
