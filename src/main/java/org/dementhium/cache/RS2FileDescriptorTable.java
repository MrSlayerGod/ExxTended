package org.dementhium.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lazaro, Defyboy
 */
public class RS2FileDescriptorTable {
	
    public static int hashName(String s) {
        int r = 0;
        for (int i = 0; i < s.length(); i++) {
            r = s.charAt(i) + ((r << 5) - r);
        }
        return r;
    }

    private Map<Integer, RS2FileDescriptor> descriptorMap = null;
    private RS2FileDescriptor[] entries;
    private boolean file512BitChecksums;
    private CacheFile infoFile;
    private int revision;
    private boolean titled;
    private int version;

    public RS2FileDescriptorTable(CacheFile infoFile) throws IOException {
        this.infoFile = infoFile;
        infoFile.decompress(null);
        ByteBuffer buffer = infoFile.getData();
        version = buffer.get() & 0xff;
        if (version == 6) {
            revision = buffer.getInt();
        } else if (version != 5) {
            throw new IOException("Invalid descriptor version : " + version);
        }
        int flags = buffer.get() & 0xff;
        titled = (flags & 0x1) != 0;
        file512BitChecksums = (flags & 0x2) != 0;
        int count = buffer.getShort() & 0xffff;
        int[] spacing = new int[count];
        int entryCount = 0;
        for (int i = 0; i < count; i++) {
            spacing[i] = entryCount += buffer.getShort() & 0xffff;
        }
        entryCount++;
        entries = new RS2FileDescriptor[entryCount];
        for (int i = 0; i < entryCount; i++) {
            entries[i] = new RS2FileDescriptor(i);
        }
        if (titled) {
            descriptorMap = new HashMap<Integer, RS2FileDescriptor>();
            for (int i = 0; i < count; i++) {
                RS2FileDescriptor entry = entries[spacing[i]];
                entry.setNameHash(buffer.getInt());
                descriptorMap.put(entry.getNameHash(), entry);
            }
        }
        for (int i = 0; i < count; i++) {
            entries[spacing[i]].setExists(true);
            entries[spacing[i]].setCRC(buffer.getInt());
        }
        if (file512BitChecksums) {
            for (int i = 0; i < count; i++) {
                byte[] checksum = new byte[64];
                buffer.get(checksum);
                entries[spacing[i]].setChecksum512Bit(checksum);
            }
        }
        for (int i = 0; i < count; i++) {
            entries[spacing[i]].setRevision(buffer.getInt());
        }
        for (int i = 0; i < count; i++) {
            RS2FileDescriptor entry = entries[spacing[i]];
            RS2FileDescriptor.SubRS2FileDescriptor[] subEntries = new RS2FileDescriptor.SubRS2FileDescriptor[buffer.getShort() & 0xffff];
            for (int j = 0; j < subEntries.length; j++) {
                subEntries[j] = new RS2FileDescriptor.SubRS2FileDescriptor();
            }
            entry.setSubFiles(subEntries);
        }
        for (int i = 0; i < count; i++) {
            RS2FileDescriptor entry = entries[spacing[i]];
            for (int j = 0; j < entry.getSubFiles().length; j++) {
                entry.getSubFiles()[j].setOffset(buffer.getShort() & 0xffff);
            }
        }
        if (titled) {
            for (int i = 0; i < count; i++) {
                RS2FileDescriptor entry = entries[spacing[i]];
                for (int j = 0; j < entry.getSubFiles().length; j++) {
                    entries[spacing[i]].getSubFiles()[j].setNameHash(buffer.getInt());
                }
            }
        }
    }

    public Map<Integer, RS2FileDescriptor> getDescriptorMap() {
        return descriptorMap;
    }

    public RS2FileDescriptor[] getEntries() {
        return entries;
    }

    public CacheFile getFile() {
        return infoFile;
    }

    public int getRevision() {
        return revision;
    }

    public int getVersion() {
        return version;
    }

    public boolean isTitled() {
        return titled;
    }
}
