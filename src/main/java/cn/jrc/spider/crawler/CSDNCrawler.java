package cn.jrc.spider.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.jrc.domain.PageInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;

/**
 * CSDN Crawler
 *
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/23 16:05
 */
public class CSDNCrawler extends Crawler {
    public CSDNCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        this.addSeed("http://ask.csdn.net/");
        this.addSeed("http://ask.csdn.net/questions?type=resolved");
        this.addRegex("http://ask.csdn.net/.*");
        this.addRegex("-.*#.*");
       // this.addRegex("-.*\\?.*");
        this.setThreads(20);
        this.setResumable(true);
        getConf().setExecuteInterval(2000);
    }

    @Override
    public boolean match(Page page, CrawlDatums next) {
        return page.matchUrl("http://ask.csdn.net/questions/[0-9]+*");
    }


    @Override
    public PageInfo handle(Document document, String url){
        PageInfo pageInfo = new PageInfo();
        String title = document.getElementsByTag("title").get(0).text();
        pageInfo.setTitle(title);
        pageInfo.setUrl(url);
        Elements elements = document.select("div.tags > a");
        ArrayList<String> tags = new ArrayList<>();
        for (Element element : elements) {
            tags.add(element.text());
        }
        pageInfo.setTags(tags);
        ArrayList<String> answers = new ArrayList<>();
        Elements elements1 = document.select("div.answer_list>div>div>p");
        for (Element element : elements1) {
            answers.add(element.text());
        }
        pageInfo.setAnswers(answers);
        pageInfo.setDate(new Date());
        String description = document.getElementsByTag("dd").get(0).text();
        pageInfo.setDescription(description);
        return pageInfo;
    }

    public static void main(String[] args) throws Exception {
        Crawler crawler = new CSDNCrawler("db", true);
        crawler.start(4);
    }
}
