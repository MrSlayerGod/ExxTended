package org.dementhium.model.definition;

/**
 * 
 * @author 'Mystic Flow
 *
 */
public class PlayerDefinition {

	private String username;
	private String password;
	private int rights;
	private int title;
	private boolean isDonator;
	private String titleName;
	private boolean hasTitle;
	private boolean wantTitle = true;
	
	public PlayerDefinition(String user, String pass) {
		this.username = user;
		this.password = pass;
	}

	public String getName() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRights(int rights) {
		this.rights = rights;
	}
	
	public int getRights() {
		return rights;
	}

	public void setTitle(String titleName) {
		this.titleName = titleName;
	}

	public boolean wantTitle() {
		if(!hasTitle() && !isDonator()) {
			return false;
		}
		return wantTitle;
	}

	public void setWantTitle(boolean b) {
		this.wantTitle = b;
	}

	public int getTitle() {
		return title;
	}

	public String getTitleName() {
		return titleName;
	}

	public boolean hasTitle() {
		return hasTitle;
	}

	public void setHasTitle(boolean b) {
		this.hasTitle = b;
	}

	public void setDonator(boolean b) {
		this.isDonator = b;
	}

	public boolean isDonator() {
		return isDonator;
	}

}