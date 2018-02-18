package cn.jrc.spider.crawler;

import cn.jrc.spider.LinkQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * segment fault
 *
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/14 16:39
 */
public class SGFCrawler extends Crawler {
    @Override
    public void getUrls(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            Elements elements = document.select("a[href]");
            for (Element element : elements) {
                String link = element.attr("abs:href");
                if (link.trim().contains("/q")) {
                    LinkQueue.addUnvisitedUrl(link);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        Crawler crawler = new SGFCrawler();
        crawler.crawling(new String[]{"https://segmentfault.com/questions/"}, "./files");
    }
}
