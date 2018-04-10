package cn.jrc.task;

import cn.jrc.crawler.CSDNCrawler;
import cn.jrc.crawler.Crawler;
import cn.jrc.crawler.SGFCrawler;
import cn.jrc.crawler.STOCrawler;
import cn.jrc.dao.TaskDao;
import cn.jrc.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/4/5 15:11
 */
public class ExtractAndIndexTask implements Runnable {
    public static Logger LOG = LoggerFactory.getLogger(ExtractAndIndexTask.class);
    private String url;
    private CountDownLatch endGate;
    private Crawler crawler = null;
    private TaskDao dao = new TaskDao();

    public ExtractAndIndexTask(String url, CountDownLatch endGate) {
        this.url = url;
        this.endGate = endGate;
        try {
            if (url.startsWith("https://ask.csdn.net/")) {
                crawler = new CSDNCrawler(url);
            } else if (url.startsWith("https://segmentfault.com/q/")) {
                crawler = new SGFCrawler(url);
            } else if (url.startsWith("https://stackoverflow.com/questions/")) {
                crawler = new STOCrawler(url);
            }
        } catch (IOException e) {
            dao.update(url, Task.FAILED);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            if (crawler != null) {
                LOG.info("Start Extract Url: " + url);
                try {
                    crawler.visit();
                    dao.update(url, Task.EXTRACTED);
                    LOG.info("End Extract Url: " + url);
                } catch (IOException e) {
                    dao.update(url, Task.FAILED);
                    e.printStackTrace();
                }
            }
        } finally {
            endGate.countDown();
            LOG.info("Remaining: " + endGate.getCount());
        }
    }
}
