package org.dementhium.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dementhium.io.XMLHandler;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 *
 */
public class BanManager {
	
	private List<String> bannedUsers;
	
	public void load() {
		try {
			bannedUsers = XMLHandler.fromXML("data/xml/banned_users.xml");
		} catch (IOException e) {
			bannedUsers = new ArrayList<String>();
			e.printStackTrace();
		}
	}
	
	public boolean contains(String name) {
		return bannedUsers.contains(name);
	}
	
	public String getBannedUser(String name) {
		for (String s : bannedUsers) {
			if (s.equals(name)) {
				return s;
			}
		}
		throw new IllegalArgumentException("Banned user [ "+name+" ] does not exist");
	}
}
