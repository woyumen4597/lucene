package cn.jrc.spider;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.*;

import java.io.File;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/20 12:48
 */
public abstract class AbstractFrontier {
    private Environment env;
    private static  final String CLASS_CATALOG = "java_class_catalog";
    protected StoredClassCatalog javaCatalog;
    protected Database catalogdatabase;
    protected Database database;

    public AbstractFrontier(String homeDirectory) {
        System.out.println("Opening enviroment in: "+homeDirectory);
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(true);
        envConfig.setAllowCreate(true);
        env = new Environment(new File(homeDirectory),envConfig);
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setAllowCreate(true);
        //open
        catalogdatabase = env.openDatabase(null,CLASS_CATALOG,dbConfig);
        javaCatalog = new StoredClassCatalog(catalogdatabase);
        DatabaseConfig dbConfig0 = new DatabaseConfig();
        dbConfig0.setTransactional(true);
        dbConfig0.setAllowCreate(true);
        database = env.openDatabase(null,"URL",dbConfig);
    }

    public void close() throws DatabaseException{
        database.close();
        javaCatalog.close();
        env.close();
    }

    protected abstract void put(Object key,Object value);
    protected abstract Object get(Object key);
    protected abstract Object delete(Object key);
}
