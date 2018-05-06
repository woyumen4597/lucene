package cn.jrc.test;

import cn.jrc.crawler.Collector;
import cn.jrc.dao.TaskDao;
import cn.jrc.util.FileUtils;
import org.junit.Test;
import java.util.List;


/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/4/5 15:18
 */
public class CollectorTest {
    @Test
    public void collect() {
        List<String> urls = FileUtils.readFromFile("./files/seeds.txt");
        Collector collector = new Collector(urls);
        collector.collect();
    }

    @Test
    public void taskdao(){
        TaskDao dao = new TaskDao();
        dao.update("https://stackoverflow.com/questions/950087/how-do-i-include-a-javascript-file-in-another-javascript-file?answertab=oldest#tab-top",1);
    }

    @Test
    public void test(){
        TaskDao dao = new TaskDao();
        List<String> urls = dao.getUrlsByState(0);
        Collector collector = new Collector();
        collector.extractAndIndex(urls,false);
    }
}
