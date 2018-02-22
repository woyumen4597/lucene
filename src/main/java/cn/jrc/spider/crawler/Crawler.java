package cn.jrc.spider.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.jrc.domain.PageInfo;
import cn.jrc.util.IndexUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/18 22:48
 */
public class Crawler extends BreadthCrawler implements Handler{
    public static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    public Crawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        this.addSeed("http://ask.csdn.net/");
        this.addRegex("http://ask.csdn.net/.*");
        this.addRegex("-.*#.*");
        this.addRegex("-.*\\?.*");
        this.setThreads(50);
        this.setResumable(true);
        getConf().setExecuteInterval(1000);

    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        String url = page.url();
        if(page.matchUrl("http://ask.csdn.net/questions/.*")){
            PageInfo pageInfo = handle(page.doc(), url);
            index(pageInfo);
        }
    }


    public static void main(String[] args) throws Exception {
        Crawler crawler = new Crawler("db",true);
        crawler.start(4);
    }

    @Override
    public PageInfo handle(Document document, String url) {
        PageInfo  pageInfo = new PageInfo();
        String title = document.getElementsByTag("title").get(0).text();
        pageInfo.setTitle(title);
        pageInfo.setUrl(url);
        pageInfo.setQuestion(title);
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

    @Override
    public void index(PageInfo pageInfo) {
        try {
            LOG.info("Index Start: "+pageInfo.toString());
            IndexUtils.index(pageInfo,"./indexDir");
            LOG.info("Index End: "+pageInfo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
