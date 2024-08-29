package org.dementhium.model.player;

import java.util.HashMap;

public class ShopManager {

	private HashMap<Integer, Shop> shops = new HashMap<Integer, Shop>();

	public void loadShops() {
		shops.put(1, new Shop(1, false, new int[] {14484, 11694, 18349, 18351, 18353, 18355, 18357, 18359, 12093, 15332, 20135, 20139, 20143, 13887, 13893, 13896, 13884, 13890, 20072, 15486, 19784, 20159, 20163, 20167, 13738, 13740, 13742}));
		shops.put(2, new Shop(2, false, new int[] {16403, 17039, 16425, 17361, 16711, 17259, 16689, 16293, 16359}));
		System.out.println("Loaded Shops");
	}

	public void openShop(Player player, int id) {
		player.setAttribute("shopId", id);
		shops.get(id).open(player);
		shops.get(id).addPlayer(player);
	}

	public Shop getShop(int id) {
		return shops.get(id);
	}

}