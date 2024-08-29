package org.dementhium.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dementhium.model.player.Player;
import org.dementhium.util.Constants;

public class DatabaseConnection extends SQLConnection{

	public DatabaseConnection() {
		super();
	}

	private DatabaseConnection getDatabase() {
		return Constants.getDatabase();
	}

	public boolean checkPurchase(String playerName) {
		try {
			playerName = playerName.replaceAll(" ", "_");
			ResultSet results = getDatabase().getQuery("SELECT * FROM `donation` WHERE `username` = '" + playerName + "'");
			while(results.next()) {
					return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteFrom(String playerName, int product) {
		try {
			playerName = playerName.replaceAll(" ", "_");
			ResultSet results = getDatabase().getQuery("SELECT * FROM `donation` WHERE `username` = '" + playerName + "' AND `productid` = '" + product + "'");
			while(results.next()) {
				int index = results.getInt("index");
				getDatabase().newQuery("DELETE FROM `donation` WHERE `index` = '" + index + "' AND `username` = '" + playerName + "'");
				closeConnection();
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void getOrderQuote(Player player, String playerName) {
		try {
			playerName = playerName.replaceAll(" ", "_");
			ResultSet results = getDatabase().getQuery("SELECT * FROM `donation` WHERE `username` = '" + playerName + "'");
			while(results.next()) {
				int prod = results.getInt("productid");
				int price = results.getInt("price");
				player.managePurchaseItems(prod, price);
				closeConnection();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}