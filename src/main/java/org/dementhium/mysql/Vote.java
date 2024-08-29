package org.dementhium.mysql;

import java.sql.*;

import org.dementhium.model.player.Player;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.util.Constants;

public class Vote extends SQLConnection {

	public Vote() {
		super();
	}


	public boolean hasVotedOnIP(String playerName, Player player) {
		try {
			Statement statement = getConnection().createStatement();
			ResultSet results = getQuery("SELECT * FROM Votes WHERE ip = '" + player.getIp() + "'");
			while(results.next()) {
				String ip = results.getString("ip");
				if(ip.equalsIgnoreCase(player.getIp()))
				{
					return true;
				}

			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean hasVoted(String playerName, Player player) {
		if(hasVotedOnIP(playerName, player)) {
			return true;
		}
		try {
			ResultSet results = getQuery("SELECT * FROM Votes WHERE username = '" + playerName + "'");
			while(results.next()) {
				int recieved = results.getInt("recieved");
				if(recieved == 0 || recieved == 1)
				{
					return true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkVote(String playerName, Player player) {
		try {
			if(!checkVotes(playerName)) {
				return false;
			}
			if(player.getInventory().getFreeSlots() <= 0) {
				player.sendMessage("You cannot recieve your reward until you free up a slot and relog!");
				return false;
			}
			int itemid = 6529;
			int amount = 50;
			int tokkulsGot = amount;
			if(Constants.DOUBLE_BONUS_WEEKEND) {
				tokkulsGot = amount * Constants.BONUS_AMOUNT;
				player.sendMessage("Bonus weekend! Heres double the tokkul.");
			}
			player.sendMessage("Thank you for voting! Take these Shop Tokkuls as a reward!");
			if(player.getRights() >= 1) {
				tokkulsGot += 25;
				player.sendMessage("You're a staff member so you got 25 extra tokens!");
			} else if(player.getGroup().equalsIgnoreCase("Donator")) {
				tokkulsGot += 25;
				player.sendMessage("You're a donator so you got 25 extra tokens!");
			} else if(player.getGroup().equalsIgnoreCase("Premium")) {
				tokkulsGot += 50;
				player.sendMessage("You're a premium donator so you got 50 extra tokens!");
			} else if(player.getGroup().equalsIgnoreCase("Super")) {
				tokkulsGot += 75;
				player.sendMessage("You're a super donator so you got 75 extra tokens!");
			}
			amount = tokkulsGot;
			ItemDefinition def = ItemDefinition.forId(itemid);
			updateQuery("UPDATE Votes SET recieved = 1 WHERE username = '" + playerName + "'");
			player.getInventory().addItem(itemid, amount);
			player.sendMessage("You receive "+amount+" x "+def.getName()+".");
			player.voted = true;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public  boolean checkVotes(String playerName) {
		try {
			ResultSet results = getQuery("SELECT * FROM Votes WHERE username = '" + playerName + "'");
			while(results.next()) {
				int recieved = results.getInt("recieved");
				if(recieved == 0)
				{
					return true;
				}

			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean voteGiven(String playerName, Player player) {
		try {
			if(player.getInventory().getFreeSlots() <= 0) {
				player.sendMessage("You cannot recieve your reward until you free up a slot!");
				return false;
			}
			updateQuery("UPDATE Votes SET recieved = 1 WHERE username = '" + playerName + "'");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}