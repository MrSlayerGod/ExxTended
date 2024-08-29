package org.dementhium.model.npc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.dementhium.model.Location;
import org.dementhium.model.World;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class NPCLoader {

	public static void loadSpawns() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		System.out.println("Loading default npc spawns...");
		int size = 0;
		BufferedReader reader = new BufferedReader(new FileReader("data/npcs/npcspawns.txt"));
		String string;
		while((string = reader.readLine()) != null) {
			if(string.startsWith("//")) {
				continue;
			}
			String[] spawn = string.split(" ");
			int id = Integer.parseInt(spawn[0]), x = Integer.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer.parseInt(spawn[3]), faceDir = Integer.parseInt(spawn[4]);
			boolean doesWalk = Boolean.parseBoolean(spawn[5]);
			NPC npc = null;
			if(spawn.length == 6) {
				npc = new NPC(id);
			} else if(spawn.length == 7) {
				try {
				npc = (NPC) Class.forName("org.dementhium.model.npc.impl." + spawn[6]).getConstructor(int.class).newInstance(id);
				} catch (ClassNotFoundException e) {
					npc = (NPC) Class.forName("org.dementhium.model.npc.impl.godwars." + spawn[6]).getConstructor(int.class).newInstance(id);
				}
			}
			if(npc != null) {
				npc.setLocation(Location.create(x, y, z));
				npc.setOriginalLocation(Location.create(x, y, z));
				npc.setDoesWalk(doesWalk);
				npc.setFaceDir(faceDir);
				npc.EntityLoad();
				World.getWorld().getNpcs().add(npc);
				size++;
			}
		}
		System.out.println("Loaded " + size + " default npc spawns.");
		reader.close();
		reader = null;
	}

}
