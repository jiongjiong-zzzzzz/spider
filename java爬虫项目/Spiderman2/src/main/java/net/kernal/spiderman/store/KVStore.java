package net.kernal.spiderman.store;

public interface KVStore {

    boolean contains(String group, String key);

    void put(String group, String key, byte[] value);

    byte[] get(String group, String key);

    void removeKeys(String group);

    void removeKey(String group, String key);

    void close();

}
