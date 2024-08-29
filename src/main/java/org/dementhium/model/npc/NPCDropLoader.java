package org.dementhium.model.npc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author Steve
 *
 */
public class NPCDropLoader {

    RandomAccessFile dropFile;
    private Map<Integer, ArrayList<Drop>> dropMap = new HashMap<Integer, ArrayList<Drop>>();

    public NPCDropLoader() {
        try {
            dropFile = new RandomAccessFile("data/npcs/drops.bin", "r");
        } catch (FileNotFoundException e) {
            System.out.println("No drop file!");
        }
    }

    public void load() {
        System.out.println("Loading drops...");
        try {
            FileChannel channel = dropFile.getChannel();
            if (channel.size() > 0) {
                ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
                ArrayList<Drop> drops = null;
                int dropSize = buffer.getShort();
                for (int i = 0; i < dropSize; i++) {
                    int npcId = buffer.getShort();
                    short dropAmt = buffer.getShort();
                    drops = new ArrayList<Drop>(dropAmt);
                    for (int x = 0; x < dropAmt; x++) {
                        drops.add(new Drop(buffer.getShort(), buffer.getDouble(), buffer.getInt()));
                    }
                    dropMap.put(npcId, drops);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded " + dropMap.size() + " drops");
    }

    public Map<Integer, ArrayList<Drop>> getDropMap() {
        return dropMap;
    }

    public static class Drop {

        public static Drop create(int itemId, int rate, int amount) {
            return new Drop((short) itemId, rate, amount);
        }
        
        private short itemId;
        private double rate;
        private int amount;

        public Drop(short itemId, double rate, int amount) {
            this.itemId = itemId;
            this.rate = rate;
            this.amount = amount;
        }

        public short getItemId() {
            return itemId;
        }

        public double getRate() {
            return rate;
        }

        public void setItemId(short itemId) {
            this.itemId = itemId;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
