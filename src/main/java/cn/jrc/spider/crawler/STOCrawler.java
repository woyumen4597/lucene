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
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/23 16:37
 */
public class STOCrawler extends Crawler {
    public STOCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        this.addSeed("https://segmentfault.com/questions/");
        this.addRegex("https://segmentfault.com/q/.*");
        this.addRegex("-.*#.*");
        this.addRegex("-.*\\?.*"); //不要匹配带有?的url
        this.setThreads(50);
        this.setResumable(true);
        this.getConf().setExecuteInterval(1000);
    }

    @Override
    public boolean match(Page page, CrawlDatums next) {
        return page.matchUrl("https://segmentfault.com/q/.*");
    }

    @Override
    public PageInfo handle(Document document, String url) {
        PageInfo pageInfo = new PageInfo();
        String title = document.select("h1#questionTitle>a").text();
        pageInfo.setTitle(title);
        Elements elements = document.select("a.tag");
        ArrayList<String> tags = new ArrayList<>();
        for (Element element : elements) {
            tags.add(element.text());
        }
        pageInfo.setTags(tags);
        pageInfo.setUrl(url);
        String description = document.select("div.question").text();
        pageInfo.setDescription(description);
        ArrayList<String> answers = new ArrayList<>();
        Elements elements1 = document.select("div.answer");
        for (Element element : elements1) {
            answers.add(element.text());
        }
        pageInfo.setAnswers(answers);
        pageInfo.setDate(new Date());
        return pageInfo;
    }

    public static void main(String[] args) throws Exception {
        STOCrawler crawler = new STOCrawler("db",true);
        crawler.start(4);
    }
}
