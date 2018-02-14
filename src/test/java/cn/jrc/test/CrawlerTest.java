package cn.jrc.test;

import cn.jrc.spider.crawler.CSDNCrawler;
import cn.jrc.spider.crawler.Crawler;
import cn.jrc.spider.crawler.SGFCrawler;
import cn.jrc.spider.crawler.STOCrawler;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/7 20:29
 */
public class CrawlerTest {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3,6,5, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
        Crawler sto = new STOCrawler();
        Crawler sgf = new SGFCrawler();
        Crawler csdn = new CSDNCrawler();
        executor.execute(sto);
        executor.execute(sgf);
        executor.execute(csdn);
    }
}
