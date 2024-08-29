package org.dementhium.model.player;

import org.dementhium.model.Container;
import org.dementhium.model.Item;

/**
 * Saves trade items.
 * @author Armo
 *
 */
public class SaveTrade {
	
	public static final int SIZE = 28;

	private final Container saveItems = new Container(SIZE, false);
	
	private transient Player player;
	
	public SaveTrade(Player player) {
		this.player = player;
	}

	public Container getContainer() {
		return saveItems;
	}

	public void reset() {
		saveItems.reset();
	}

}
