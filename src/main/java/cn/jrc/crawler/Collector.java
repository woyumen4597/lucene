package cn.jrc.crawler;

import cn.jrc.task.DeriveTask;
import cn.jrc.task.ExtractAndIndexTask;
import cn.jrc.util.CollectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Collector Framework
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/4/5 14:08
 */
public class Collector {
    public static Logger LOG = LoggerFactory.getLogger(Collector.class);
    public volatile static int MAX_NUM = 1000;
    private static LinkedBlockingDeque<String> urlQueue = new LinkedBlockingDeque<>(MAX_NUM);
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    public static boolean DONE = false;
    private List<String> seeds;

    public Collector(List<String> seeds) {
        this.seeds = seeds;
        CollectorUtils.convertListToQueue(seeds, urlQueue);
    }

    public void collect() {
        if (seeds == null) {
            throw new NullPointerException("seeds is null");
        }
        deriveLinks();
        extractAndIndex(false);
    }


    /**
     * derive more urls with seeds
     */
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
        executorService.submit(new DeriveTask(seed, urlQueue));
    }

    public void extractAndIndex(boolean update) {
        CountDownLatch endGate = new CountDownLatch(urlQueue.size());
        LOG.info("Count: " + urlQueue.size());
        while (!urlQueue.isEmpty()) {
            try {
                String url = urlQueue.take();
                executorService.submit(new ExtractAndIndexTask(url, endGate, update));
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
        LOG.info("Collect Ends");
    }



}
