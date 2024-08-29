package org.dementhium.mysql;

import java.sql.*;

import org.dementhium.model.player.Player;
import org.dementhium.util.Misc;

public class ReportLogs extends SQLConnection{

	public ReportLogs() {
		super();
	}

	public int createReport(Player player) {
		try {
			int random = Misc.random(999999999);
			Statement stmt = getConnection().createStatement();
			String theString = "No data - needs to be updated with the ::updatelog command in-game (Unique ID: "+random+")";
			ResultSet rs2 = runQuery("INSERT INTO `log` (`chatlogs`) VALUES ('" + theString + "')");
			ResultSet rs = runQuery("SELECT id FROM `log` WHERE `chatlogs`= '" + theString + "'");
			if(rs.next()) {
				int rowid = rs.getInt("id");
				String theString2 = "To see the logs of this report type ::updatelog "+rowid+" in-game.";
				runQuery("UPDATE `log` SET `chatlogs` = '"+theString2+"' WHERE `id` = '" + rowid + "'");
				player.sendMessage("Thank you for your report. Your report id # is '"+rowid+"'. Write this number down.");
				player.sendMessage("Submit a new thread in the report section and provide the report id. (::forums)");
				return rowid;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public boolean updateReport(Player player, String theLog, int reportId) {
		try {
			Statement stmt = getConnection().createStatement();
			String username = Misc.formatPlayerNameForDisplay(player.getUsername());
			String theString = "This report log was last updated by "+username+" using ::updatelog<br><br>";
			String finalized = "";
			finalized += theString;
			finalized += theLog;
			runQuery("SELECT id FROM `log` WHERE `id`= '" + reportId + "'`");
			runQuery("UPDATE log SET chatlogs = '" + finalized + "' WHERE id = '" + reportId + "'");
			player.sendMessage("Successfully updated report log "+reportId+".");
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

}