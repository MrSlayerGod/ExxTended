package org.dementhium.model;

import org.dementhium.model.definition.ItemDefinition;

/**
 * Represents a single item.
 * <p/>
 * Immutable.
 *
 * @author Graham
 */
public class Item {
	
	private int id;
	private int amount;
	private ItemDefinition definition;

	public Item() {
		this(0, 0);
	}

	public Item(int id) {
		this.id = id;
		this.amount = 1;
	}

	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
		if (this.amount <= 0) {
			this.amount = 1;
		}
	}

	public Item(int id, int amount, boolean amt0) {
		this.id = id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
	}

	public Item(Item item) {
		this.id = item.getId();
		this.amount = item.getAmount();
		if (this.amount <= 0) {
			this.amount = 1;
		}
	}

	public ItemDefinition getDefinition() {
		if(definition == null) {
			definition = ItemDefinition.forId(id);
		}
		return definition;
	}

	public int getAmount() {
		return amount;
	}

	public int getId() {
		return id;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override public String toString() { return "Item(" + id + ", " + amount + ")"; }
}
