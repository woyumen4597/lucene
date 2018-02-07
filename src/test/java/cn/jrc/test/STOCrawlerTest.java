package cn.jrc.test;

import cn.jrc.spider.crawler.Crawler;
import cn.jrc.spider.crawler.STOCrawler;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/7 20:29
 */
public class STOCrawlerTest {
    public static void main(String[] args) {
        Crawler crawler = new STOCrawler();
        new Thread(crawler).start();
    }
}
