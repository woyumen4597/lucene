package cn.jrc.task;

import cn.jrc.crawler.CSDNCrawler;
import cn.jrc.crawler.Crawler;
import cn.jrc.crawler.SGFCrawler;
import cn.jrc.crawler.STOCrawler;
import cn.jrc.dao.TaskDao;
import cn.jrc.domain.Task;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingDeque;

import static cn.jrc.crawler.Collector.MAX_NUM;
import static cn.jrc.crawler.Collector.DONE;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/4/10 13:22
 */
public class DeriveTask implements Runnable {
    private String seed;
    private BlockingDeque queue;
    private TaskDao dao = new TaskDao();

    public DeriveTask(String seed, BlockingDeque queue) {
        this.seed = seed;
        this.queue = queue;
    }

    @Override
    public void run() {
        Crawler crawler = null;
        try {
            if (seed.startsWith("https://ask.csdn.net/")) {
                crawler = new CSDNCrawler(seed);
            } else if (seed.startsWith("https://segmentfault.com/questions/")) {
                crawler = new SGFCrawler(seed);
            } else if (seed.startsWith("https://stackoverflow.com/questions/")) {
                crawler = new STOCrawler(seed);
            }
        } catch (IOException e) {
            dao.update(seed, Task.FAILED);
            e.printStackTrace();
        }
        if (crawler != null) {
            Set<String> deriveLinks = crawler.deriveLinks();
            for (String link : deriveLinks) {
                if (queue.size() < MAX_NUM) {
                    try {
                        if (!queue.contains(link) && dao.insert(new Task(link, Task.READY))) {
                            queue.put(link);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    DONE = true;
                    break;
                }
            }
        }
    }
}
