package org.dementhium.mysql;

import org.dementhium.model.player.Player;
import org.dementhium.util.Constants;

/**
 * vBulletin class
 * @author probaby Armo
 */

import java.sql.*;

public class vBulletin implements Runnable {
	
	
	private static Connection connection;
	private Statement statement = null;
	private static Thread thread = null;
	private static long lastConnection = System.currentTimeMillis();
	
	public enum Type {
		myBB,
		SMF,
		IPB,
		vBulletin,
		phpBB,
	}

	private String[] tableNames = new String[6];
	private void setTables() {
		if(forum == Type.myBB){
			tableNames = new String[] {"mybb_users","username","password","salt","usergroupid",};
		} else if(forum == Type.SMF){
			tableNames = new String[] {" smf_members","memberName","passwd","passwordSalt","ID_GROUP",};
		} else if(forum == Type.IPB){
			tableNames = new String[] {"members","members_display_name","members_pass_hash","members_pass_salt","member_group_id",};
		} else if(forum == Type.vBulletin){//vbulletin
			tableNames = new String[] {"user","username","password","salt","usergroupid","pmunread","username","displaygroupid","pmtotal",};
		} else if(forum == Type.phpBB){//phpBB
			tableNames = new String[] {"users","username","user_password","user_password","group_id",};
		}
	}
	
	public vBulletin(String url,String database,String username,String password,Type t) {
		this.hostAddress = "jdbc:mysql://"+url+"/"+database;
		this.username = username;
		this.password = password;
		this.forum = t;
		try {
			//connect();
			thread = new Thread(this);
			thread.start();
		} catch(Exception e){
			connection = null;
		}
	}

	public static void destroyConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private final String hostAddress;
	private final String username;
	private final String password;
	private final Type forum;
	
	private void connect(int attempts) {
		if(Constants.SQL_ENABLED == false) {
			return;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(Exception e2){
			System.out.println("Cannot find mySql Driver.");
			return;
		}
		try {
			connection = DriverManager.getConnection(hostAddress, username,password);
			statement = connection.createStatement();
				} catch(Exception e) {
			if(System.currentTimeMillis() - lastConnection > 10000 || attempts == 0) {
				destroyConnection();
				connect(0);
				lastConnection = System.currentTimeMillis();
			}
			connect(++attempts);
		}
	}
	private void ping() {
		try {
			ResultSet results = null;
			String query = "SELECT * FROM "+tableNames[0]+" WHERE "+tableNames[2]+" LIKE 'null312'";
			results = statement.executeQuery(query);
		} catch(Exception e){
			connection = null;
			connect(0);
		}
	}

        public void setRank(String playerName, int rank, int additional, Player player) {
                try {
		player.getDefinition().setWantTitle(true);
		player.getMask().setApperanceUpdate(true);
		player.forumGroup = rank;
		String title = "";
		if(rank == 10) {
			title = "Donator";
		} else if(rank == 29) {
			title = "Premium Donator";
		} else if(rank == 33) {
			title = "Super Donator";
		}
		query("UPDATE user SET usergroupid = " + rank + " WHERE username = '" + playerName + "'");
		if(getAdditionGroups(playerName).equalsIgnoreCase("") || getAdditionGroups(playerName).equalsIgnoreCase("0")) {
			query("UPDATE user SET membergroupids = '" + additional + "' WHERE username = '" + playerName + "'");
		} else {
			query("UPDATE user SET membergroupids = '" + additional + "," + getAdditionGroups(playerName) + "' WHERE username = '" + playerName + "'");
		}
		query("UPDATE user SET displaygroupid = " + rank + " WHERE username = '" + playerName + "'");
		query("UPDATE user SET usertitle = '" + title + "' WHERE username = '" + playerName + "'");
		player.addGroups = getAdditionGroups(playerName, true);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

	public String getAdditionGroups(String name) {
		try {
			ResultSet results = null;
			String query = "SELECT * FROM "+tableNames[0]+" WHERE "+tableNames[1]+" LIKE '"+name+"'";
			try {
				if(statement == null) {
					statement = connection.createStatement();
				}
			} catch(Exception e5){
				statement = null;
				connection = null;
				connect(0);
				statement = connection.createStatement();
			}
			results = statement.executeQuery(query);
			String additionalGroups = null;
			if(results.next()){
				additionalGroups = results.getString("membergroupids");
			}
			return additionalGroups;
		} catch(Exception e6){
			return "";
		}
//		return "";
	}

	public String[] getAdditionGroups(String name, boolean split) {
		try {
			ResultSet results = null;
			String query = "SELECT * FROM "+tableNames[0]+" WHERE "+tableNames[1]+" LIKE '"+name+"'";
			try {
				if(statement == null) {
					statement = connection.createStatement();
				}
			} catch(Exception e5){
				statement = null;
				connection = null;
				connect(0);
				statement = connection.createStatement();
			}
			results = statement.executeQuery(query);
			String[] additionalGroups = null;
			if(results.next()){
				additionalGroups = results.getString("membergroupids").split(",");
			}
			return additionalGroups;
		} catch(Exception e6){
			return new String[] {""};
		}
	//	return new String[] {""};
	}

	public String getRealUsername(String name) {
		try {
			ResultSet results = null;
			String query = "SELECT * FROM "+tableNames[0]+" WHERE "+tableNames[1]+" LIKE '"+name+"'";
			try {
				if(statement == null) {
					statement = connection.createStatement();
				}
			} catch(Exception e5){
				statement = null;
				connection = null;
				connect(0);
				statement = connection.createStatement();
			}
			results = statement.executeQuery(query);
			String username = null;
			if(results.next()){
				username = results.getString("username");
			}
			return username;
		} catch(Exception e6){
			return "";
		}
	}

        private ResultSet query(String s) throws SQLException {
                try {
                        if (s.toLowerCase().startsWith("select")) {
                                ResultSet rs = statement.executeQuery(s);
                                return rs;
                        } else {
                                statement.executeUpdate(s);
                        }
                        return null;
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }
	
	public void run() {
		boolean allowRun = true;
		while(allowRun){
			try {
				if(connection == null) {
					setTables();
					connect(0);
				} else {
					ping();
				}
				thread.sleep(10000);
			} catch(Exception e){
			}
		}
	}
	/**
	 * returns 2 integers, the return code and the usergroup of the player
	 */
	
	public int[] checkUser(String name,String password) {
		int i = 0;
		int[] returnCodes = {0,0,0,0,0,0};//return code for client, group id

		try{
			setTables();
			ResultSet results = null;
			String query = "SELECT * FROM "+tableNames[0]+" WHERE "+tableNames[1]+" LIKE '"+name+"'";
			try {
			if(statement == null)
				statement = connection.createStatement();
			} catch(Exception e5){
				statement = null;
				connection = null;
				connect(0);
				statement = connection.createStatement();
			}
			results = statement.executeQuery(query);
			if(results.next()){
				String pass = results.getString(tableNames[2]);
				String salt = results.getString(tableNames[3]);
				int group = results.getInt(tableNames[4]);
				int pmsUnread = results.getInt(tableNames[5]);
				String username = results.getString(tableNames[6]);
				int displayGroup = results.getInt(tableNames[7]);
				int pmsTotal = results.getInt(tableNames[8]);
				returnCodes[1] = group;
				returnCodes[2] = pmsUnread;
				returnCodes[4] = displayGroup;
				returnCodes[5] = pmsTotal;
				String pass2 = "";
				if(forum == Type.myBB){
					pass2 = MD5.MD5(MD5.MD5(salt)+MD5.MD5(password));
				} else if(forum == Type.vBulletin){
					pass2 = MD5.MD5(password);
					pass2 = MD5.MD5(pass2+salt);
				} else if(forum == Type.SMF){
					pass2 = MD5.SHA((name.toLowerCase())+password);
				} else if(forum == Type.phpBB){
					pass2 = MD5.MD5(password);
				} else if(forum == Type.IPB){
					pass2 = MD5.MD5(MD5.MD5(salt)+MD5.MD5(password));
				}
				if(pass.equalsIgnoreCase(pass2)){
					returnCodes[0] = 2;
					return returnCodes;
				} else {
					returnCodes[0] = 3;
					return returnCodes;
				}
			} else {
				//no user exists
				returnCodes[0] = 3;
				return returnCodes;
			}
		} catch(Exception e) {
			statement = null;
			returnCodes[0] = 8;
			e.printStackTrace();
			return returnCodes;
		}
	}
}