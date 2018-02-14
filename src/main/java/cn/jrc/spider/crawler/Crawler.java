package cn.jrc.spider.crawler;


import cn.jrc.spider.LinkQueue;
import cn.jrc.spider.util.Downloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/22 14:33
 */
public abstract class Crawler implements Runnable{
    public Crawler(){

    }
    /**
     * init url seeds
     * @param seeds
     */
    private void initCrawlerWithSeeds(String[] seeds){
        for (int i = 0; i < seeds.length; i++) {
            LinkQueue.addUnvisitedUrl(seeds[i]);
        }
    }

    public void crawling(String[] seeds,String dirPath){
        initCrawlerWithSeeds(seeds);
        while(!LinkQueue.unVisitedUrlsEmpty()&&LinkQueue.getVisitedUrlNum()<=100){
            String url = (String) LinkQueue.unVisitedUrlDequeue();
            Downloader.download(url,dirPath);
            getUrls(url);
        }
    }


    public abstract void getUrls(String url);


}
