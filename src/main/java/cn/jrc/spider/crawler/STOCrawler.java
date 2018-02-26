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
 * StackOverflow Crawler
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/25 15:55
 */
public class STOCrawler extends Crawler{
    public STOCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        this.addSeed("https://stackoverflow.com/questions/");
        this.addRegex("https://stackoverflow.com/questions.*");
        this.addRegex("-.*#.*");
        this.setThreads(20);
        this.getConf().setExecuteInterval(2000);
        this.setResumable(true);
    }

    @Override
    public boolean match(Page page, CrawlDatums next) {
        return page.matchUrl("https://stackoverflow.com/questions/[0-9]+.*");
    }

    @Override
    public PageInfo handle(Document document, String url) {
        PageInfo pageInfo = new PageInfo();
        String title = document.select("div#question-header>h1>a.question-hyperlink").text();
        pageInfo.setTitle(title);
        String description = document.select("div.post-text").get(0).text();
        pageInfo.setDescription(description);
        Elements select = document.select("div.post-taglist>a");
        ArrayList<String> tags = new ArrayList<>();
        for (Element element : select) {
            tags.add(element.text());
        }
        pageInfo.setTags(tags);
        ArrayList<String> answers = new ArrayList<>();
        Elements elements = document.select("td.answercell>div.post-text");
        for (Element element : elements) {
            answers.add(element.text());
        }
        pageInfo.setAnswers(answers);
        pageInfo.setUrl(url);
        pageInfo.setDate(new Date());
        return pageInfo;
    }

    public static void main(String[] args) throws Exception {
        Crawler crawler = new STOCrawler("db",true);
        crawler.start(4);
    }
}
