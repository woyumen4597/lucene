package cn.jrc.spider.crawler;

import cn.jrc.spider.LinkQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * stack overflow
 *
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/7 20:27
 */
public class STOCrawler extends Crawler implements Runnable {
    @Override
    public void getUrls(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            Elements elements = document.select("a[href]");
            for (Element element : elements) {
                String link = element.attr("abs:href");
                if (link.trim().contains("/questions")) {
                    LinkQueue.addUnvisitedUrl(link);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        Crawler crawler = new STOCrawler();
        crawler.crawling(new String[]{"https://stackoverflow.com/questions/"}, "./files");
    }
}
