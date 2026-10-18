package javax.microedition.rms;

import dr.web.Host;
import java.util.ArrayList;
import java.util.HashMap;

/** MIDP record stores, persisted to the browser's localStorage (one key per store). */
public class RecordStore {
    private static final String PREFIX = "diamond-rush.rms.";
    private static final HashMap<String, RecordStore> OPEN = new HashMap<>();
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    private final String name;
    /** Index = record id - 1; deleted records are null. */
    private final ArrayList<byte[]> records = new ArrayList<>();
    private int openCount;
    private int version;
    private long lastModified;

    private RecordStore(String name) {
        this.name = name;
    }

    public static RecordStore openRecordStore(String name, boolean createIfNecessary)
            throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {
        if (name == null || name.isEmpty() || name.length() > 32) {
            throw new IllegalArgumentException();
        }
        RecordStore store = OPEN.get(name);
        if (store == null) {
            String saved = Host.storeGet(PREFIX + name);
            if (saved == null && !createIfNecessary) {
                throw new RecordStoreNotFoundException(name);
            }
            store = new RecordStore(name);
            if (saved != null) {
                store.load(saved);
            } else {
                store.save();
            }
            OPEN.put(name, store);
        }
        store.openCount++;
        return store;
    }

    public static RecordStore openRecordStore(String name, boolean createIfNecessary, int authmode, boolean writable)
            throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {
        return openRecordStore(name, createIfNecessary);
    }

    public static void deleteRecordStore(String name) throws RecordStoreException, RecordStoreNotFoundException {
        RecordStore open = OPEN.get(name);
        if (open != null && open.openCount > 0) {
            throw new RecordStoreException("Record store is open");
        }
        if (Host.storeGet(PREFIX + name) == null) {
            throw new RecordStoreNotFoundException(name);
        }
        OPEN.remove(name);
        Host.storeRemove(PREFIX + name);
    }

    public static String[] listRecordStores() {
        String keys = Host.storeKeys(PREFIX);
        if (keys.isEmpty()) {
            return null;
        }
        String[] names = keys.split("\n");
        for (int i = 0; i < names.length; i++) {
            names[i] = names[i].substring(PREFIX.length());
        }
        return names;
    }

    public void closeRecordStore() throws RecordStoreNotOpenException, RecordStoreException {
        checkOpen();
        if (--openCount == 0) {
            OPEN.remove(name);
        }
    }

    public String getName() throws RecordStoreNotOpenException {
        checkOpen();
        return name;
    }

    public int getVersion() throws RecordStoreNotOpenException {
        checkOpen();
        return version;
    }

    public long getLastModified() throws RecordStoreNotOpenException {
        checkOpen();
        return lastModified;
    }

    public int getNumRecords() throws RecordStoreNotOpenException {
        checkOpen();
        int count = 0;
        for (byte[] record : records) {
            if (record != null) {
                count++;
            }
        }
        return count;
    }

    public int getSize() throws RecordStoreNotOpenException {
        checkOpen();
        int size = 0;
        for (byte[] record : records) {
            if (record != null) {
                size += record.length;
            }
        }
        return size;
    }

    public int getSizeAvailable() throws RecordStoreNotOpenException {
        checkOpen();
        return 512 * 1024;
    }

    public int getNextRecordID() throws RecordStoreNotOpenException, RecordStoreException {
        checkOpen();
        return records.size() + 1;
    }

    public int addRecord(byte[] data, int offset, int numBytes)
            throws RecordStoreNotOpenException, RecordStoreException, RecordStoreFullException {
        checkOpen();
        records.add(copy(data, offset, numBytes));
        changed();
        return records.size();
    }

    public void setRecord(int recordId, byte[] newData, int offset, int numBytes)
            throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException,
            RecordStoreFullException {
        checkOpen();
        record(recordId);
        records.set(recordId - 1, copy(newData, offset, numBytes));
        changed();
    }

    public void deleteRecord(int recordId)
            throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        checkOpen();
        record(recordId);
        records.set(recordId - 1, null);
        changed();
    }

    public int getRecordSize(int recordId)
            throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        checkOpen();
        return record(recordId).length;
    }

    public byte[] getRecord(int recordId)
            throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        checkOpen();
        byte[] record = record(recordId);
        return record.length == 0 ? null : copy(record, 0, record.length);
    }

    public int getRecord(int recordId, byte[] buffer, int offset)
            throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        checkOpen();
        byte[] record = record(recordId);
        System.arraycopy(record, 0, buffer, offset, record.length);
        return record.length;
    }

    private byte[] record(int recordId) throws InvalidRecordIDException {
        if (recordId < 1 || recordId > records.size() || records.get(recordId - 1) == null) {
            throw new InvalidRecordIDException("Invalid record id " + recordId);
        }
        return records.get(recordId - 1);
    }

    private void checkOpen() throws RecordStoreNotOpenException {
        if (openCount <= 0) {
            throw new RecordStoreNotOpenException(name);
        }
    }

    private static byte[] copy(byte[] data, int offset, int length) {
        byte[] result = new byte[data == null ? 0 : length];
        if (data != null) {
            System.arraycopy(data, offset, result, 0, length);
        }
        return result;
    }

    private void changed() {
        version++;
        lastModified = System.currentTimeMillis();
        save();
    }

    /** Format: version, then one entry per record id (hex bytes, or "-" when deleted), comma separated. */
    private void save() {
        StringBuilder sb = new StringBuilder();
        sb.append(version);
        for (byte[] record : records) {
            sb.append(',');
            if (record == null) {
                sb.append('-');
            } else {
                for (byte b : record) {
                    sb.append(HEX[b >> 4 & 15]).append(HEX[b & 15]);
                }
            }
        }
        Host.storeSet(PREFIX + name, sb.toString());
    }

    private void load(String saved) {
        String[] parts = saved.split(",", -1);
        version = Integer.parseInt(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.equals("-")) {
                records.add(null);
                continue;
            }
            byte[] record = new byte[part.length() / 2];
            for (int j = 0; j < record.length; j++) {
                record[j] = (byte) Integer.parseInt(part.substring(j * 2, j * 2 + 2), 16);
            }
            records.add(record);
        }
    }
}
