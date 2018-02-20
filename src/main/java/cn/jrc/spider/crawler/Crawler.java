package cn.jrc.spider.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.jrc.domain.PageInfo;
import cn.jrc.util.Downloader;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/18 22:48
 */
public class Crawler extends BreadthCrawler{

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
            String title = page.select("dt").first().text();
            String description = page.selectText("dd>p");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTitle(title);
            System.out.println("title: "+title);
            Downloader.download(page.content(),"./files/",url);

        }
    }

    public static void main(String[] args) throws Exception {
        Crawler crawler = new Crawler("db",true);
        crawler.start(4);
    }
}
