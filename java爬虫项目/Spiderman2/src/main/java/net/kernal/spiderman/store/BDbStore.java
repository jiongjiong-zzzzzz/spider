package net.kernal.spiderman.store;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BDbStore implements KVStore {

    private Environment env;
    private Map<String, Database> dbs;

    public BDbStore(File file, String... groups) {
    	File[] files = file.listFiles(f -> f.isFile());
    	if (files != null) {
	        Arrays.asList(files)
	        	.parallelStream()
	        	.forEach(f -> {
	        		f.delete();
	        	});
    	}
        
        dbs = new HashMap<>(groups.length);
        // Open the environment. Create it if it does not already exist.
        EnvironmentConfig envCfg = new EnvironmentConfig();
        envCfg.setAllowCreate(true);
        env = new Environment(file, envCfg);
        
        // Open the database. Create it if it does not already exist.
        DatabaseConfig dbCfg = new DatabaseConfig();
        dbCfg.setAllowCreate(true);
        for (String group : groups) {
            Database db = env.openDatabase(null, group, dbCfg);
            dbs.put(group, db);
        }
    }

    public boolean contains(String group, String key) {
        return get(group, key) != null;
    }

    public void put(String group, String key, byte[] value) {
        this.dbs.get(group).put(null, new DatabaseEntry(key.getBytes()), new DatabaseEntry(value));
    }

    public byte[] get(String group, String key) {
        DatabaseEntry value = new DatabaseEntry();
        OperationStatus stat = this.dbs.get(group).get(null, new DatabaseEntry(key.getBytes()), value, LockMode.DEFAULT);
        return stat == OperationStatus.SUCCESS ? value.getData() : null;
    }

    public void close() {
        this.dbs.values().forEach(db -> db.close());
        this.env.close();
    }

    public void removeKeys(String group) {
        Database db = dbs.get(group);
        db.close();
        dbs.remove(group);
        env.removeDatabase(null, group);
        DatabaseConfig dbCfg = new DatabaseConfig();
        dbCfg.setAllowCreate(true);
        Database newDb = env.openDatabase(null, group, dbCfg);
        dbs.put(group, newDb);
    }

    public void removeKey(String group, String key) {
        this.dbs.get(group).delete(null, new DatabaseEntry(key.getBytes()));
    }

}
