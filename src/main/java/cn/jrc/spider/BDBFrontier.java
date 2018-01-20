package cn.jrc.spider;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;

import java.util.Map;
import java.util.Set;

/**
 * The type Bdb frontier.
 *
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018 /1/20 12:57
 */
public class BDBFrontier extends AbstractFrontier implements Frontier {
    private StoredMap pendingUrisDB = null;

    /**
     * Instantiates a new Bdb frontier.
     *
     * @param homeDirectory the home directory
     */
    public BDBFrontier(String homeDirectory) {
        super(homeDirectory);
        EntryBinding keyBinding = new SerialBinding(javaCatalog, String.class);
        EntryBinding valueBinding = new SerialBinding(javaCatalog, CrawlUrl.class);
        pendingUrisDB = new StoredMap(database, keyBinding, valueBinding, true);
    }

    @Override
    protected void put(Object key, Object value) {
        pendingUrisDB.put(key, value);
    }

    @Override
    protected Object get(Object key) {
        return pendingUrisDB.get(key);
    }

    @Override
    protected Object delete(Object key) {
        return pendingUrisDB.remove(key);
    }

    @Override
    public CrawlUrl getNext() throws Exception {
        CrawlUrl result = null;
        if (!pendingUrisDB.isEmpty()) {
            Set entrys = pendingUrisDB.entrySet();
            System.out.println(entrys);
            Map.Entry<String, CrawlUrl> entry = (Map.Entry<String, CrawlUrl>) pendingUrisDB.entrySet().iterator().next();
            result = entry.getValue();
            delete(entry.getKey());
        }
        return result;
    }

    @Override
    public boolean putUrl(CrawlUrl url) throws Exception {
        put(url.getOriUrl(), url);
        return true;
    }

    //calc value for url through md5 algorithm
    private String calcuteUrl(String url) {
        return url;
    }

    public static void main(String[] args) throws Exception {
        BDBFrontier bdbFrontier = new BDBFrontier("./db");
        CrawlUrl url = new CrawlUrl();
        url.setOriUrl("http://www.163.com");
        bdbFrontier.putUrl(url);
        System.out.println(((CrawlUrl) bdbFrontier.getNext()).getOriUrl());
        bdbFrontier.close();
    }
}
