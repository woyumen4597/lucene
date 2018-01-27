package cn.jrc.spider;


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
public class MyCrawer {
    public MyCrawer(){

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

    public void crawling(String[] seeds){
        initCrawlerWithSeeds(seeds);
        while(!LinkQueue.unVisitedUrlsEmpty()&&LinkQueue.getVisitedUrlNum()<=100){
            String url = (String) LinkQueue.unVisitedUrlDequeue();
            Downloader.download(url);
            getUrls(url);
        }
    }

    public static void main(String[] args) {
        MyCrawer crawer = new MyCrawer();
        crawer.crawling(new String[]{"https://stackoverflow.com/questions/"});
    }

    public void getUrls(String url){
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = document.select("a[href]");
        for (Element element : elements) {
            String link  = element.attr("abs:href");
            if(link.trim().contains("/questions")){
                LinkQueue.addUnvisitedUrl(link);
            }
        }
    }

}
