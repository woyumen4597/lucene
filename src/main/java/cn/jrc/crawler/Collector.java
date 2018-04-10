package cn.jrc.crawler;

import cn.jrc.dao.TaskDao;
import cn.jrc.domain.Task;
import cn.jrc.task.ExtractAndIndexTask;
import cn.jrc.util.CollectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/4/5 14:08
 */
public class Collector {
    public static Logger LOG = LoggerFactory.getLogger(Collector.class);
    private static int MAX_NUM = 100;
    private static LinkedBlockingDeque<String> urlQueue = new LinkedBlockingDeque<>(MAX_NUM);
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static boolean DONE = false;
    private List<String> seeds;
    private TaskDao dao = new TaskDao();

    public Collector() {
    }

    public Collector(List<String> seeds) {
        this.seeds = seeds;
        CollectorUtils.convertListToQueue(seeds, urlQueue);
    }

    public void collect() {
        if (seeds == null) {
            throw new NullPointerException("seeds is null");
        }
        deriveLinks();
        extractAndIndex();
    }


    private void deriveLinks() {
        while (!DONE) {
            String seed = null;
            try {
                seed = urlQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOG.info("Start derive link!url = " + seed);
            deriveLink(seed);
            LOG.info("End derive link! url = " + seed);
            LOG.info("Remaining: " + (urlQueue.size()));
        }
    }

    private void deriveLink(String seed) {
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
                if (urlQueue.size() < MAX_NUM && !urlQueue.isEmpty()) {
                    try {
                        if (!urlQueue.contains(link) && dao.insert(new Task(link, Task.READY))) {
                            urlQueue.put(link);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    DONE = true;
                }
            }
        }

    }

    private void extractAndIndex() {
        CountDownLatch endGate = new CountDownLatch(urlQueue.size());
        LOG.info("Count: " + urlQueue.size());
        while (!urlQueue.isEmpty()) {
            try {
                String url = urlQueue.take();
                executorService.submit(new ExtractAndIndexTask(url, endGate));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("结束");
    }

    public void extractAndIndex(List<String> urls) {
        CollectorUtils.convertListToQueue(urls, urlQueue);
        extractAndIndex();
    }

    public List<String> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<String> seeds) {
        this.seeds = seeds;
    }
}
