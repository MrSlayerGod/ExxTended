package org.dementhium.model.definition;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dementhium.io.XMLHandler;

/**
 * Price Definition class
 * @author 1024 - Armo
 *
 */
public class PriceDefinition {
	
	private static Map<Integer, PriceDefinition> definitions = null;
	@SuppressWarnings("unchecked")
	public static void load() throws IOException {
		System.out.println("Loading price definitions...");
		List<PriceDefinition> defs = XMLHandler.fromXML("data/item/priceDefinitions.xml");
		definitions = new HashMap<Integer, PriceDefinition>();
		for(PriceDefinition def : defs) {
			if(def != null) {
				definitions.put(def.getId(), def);
			}
		}
		System.out.println("Loaded " + definitions.size() + " price definitions.");
	}
	public static PriceDefinition forId(int id) {
		if(id <= -1) {
			id = 0;
		}
		return definitions.get(id);
	}
	
		private int minPrice;
		private int normPrice;
		private int maxPrice;
		private int pkpPrice;
		private int guildPrice;
		private int votePrice;
		
		public int getMinimumPrice() {
			return minPrice;
		}
		
		public int getNormalPrice() {
			return normPrice;
		}

		public int getMaximumPrice() {
			return maxPrice;
		}

		public int getPkPtsPrice() {
			return pkpPrice;
		}

		public int getGuildTokensPrice() {
			return guildPrice;
		}

		public int getVotePrice() {
			return votePrice;
		}
	private int id;
	public int getId() {
		return id;
	}

}
