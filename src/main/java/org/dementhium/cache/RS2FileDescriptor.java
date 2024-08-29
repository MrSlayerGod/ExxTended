package org.dementhium.cache;

/**
 * @author Lazaro, Defyboy
 */
public class RS2FileDescriptor {
	
    public static class SubRS2FileDescriptor {
        private int nameHash = 0;
        private int offset = 0;

        public int getNameHash() {
            return nameHash;
        }

        public int getOffset() {
            return offset;
        }

        public void setNameHash(int nameHash) {
            this.nameHash = nameHash;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }
    
    private byte[] checksum512Bit = null;
    private int crc = 0;
    private boolean exists = false;
    private int id;
    private int nameHash = 0;
    private int revision = 0;
    private SubRS2FileDescriptor[] subFiles = null;

    public RS2FileDescriptor(int id) {
        this.id = id;
    }

    public boolean exists() {
        return exists;
    }

    public byte[] getChecksum512Bit() {
        return checksum512Bit;
    }

    public int getCRC() {
        return crc;
    }

    public int getId() {
        return id;
    }

    public int getNameHash() {
        return nameHash;
    }

    public int getRevision() {
        return revision;
    }

    public SubRS2FileDescriptor[] getSubFiles() {
        return subFiles;
    }

    public void setChecksum512Bit(byte[] checksum512Bit) {
        this.checksum512Bit = checksum512Bit;
    }

    public void setCRC(int crc) {
        this.crc = crc;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public void setNameHash(int nameHash) {
        this.nameHash = nameHash;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public void setSubFiles(SubRS2FileDescriptor[] subFiles) {
        this.subFiles = subFiles;
    }
}
