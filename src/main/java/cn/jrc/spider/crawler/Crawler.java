package cn.jrc.spider.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/18 22:48
 */
public class Crawler extends BreadthCrawler{

    public Crawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        this.addSeed("http://ask.csdn.net/");
        this.addRegex("http://ask.csdn.net/questions/.*");
        this.setThreads(50);
        getConf().setTopN(100);
    }

    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
        String url = page.url();
        if(page.matchUrl("http://ask.csdn.net/questions/.*")){
            String title = page.select("dt").first().text();
            String content = page.selectText("dd>p");
            System.out.println("title: "+title);
            System.out.println("content: "+content);
        }
    }

    public static void main(String[] args) throws Exception {
        Crawler crawler = new Crawler("crawl",true);
        crawler.start(4);
    }
}
