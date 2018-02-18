package cn.jrc.db;


import cn.jrc.domain.CrawlDatum;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/26 13:40
 */
public class BerkeleyDBUtil {
   public static DatabaseConfig defaultDBConfig;
   static{
        defaultDBConfig = createDefaultDBConfig();
   }

   public static DatabaseConfig createDefaultDBConfig(){
       DatabaseConfig config = new DatabaseConfig();
       config.setAllowCreate(true);
       return config;
   }


   public static void writeDatum(Database database, CrawlDatum datum){
       
   }
}
