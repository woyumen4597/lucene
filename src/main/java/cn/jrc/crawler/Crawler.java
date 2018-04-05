package cn.jrc.spider.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.Proxys;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.jrc.domain.PageInfo;
import cn.jrc.util.IPUtils;
import cn.jrc.util.IndexUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/18 22:48
 */
public abstract class Crawler extends BreadthCrawler {
    public static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    Proxys proxys = new Proxys();


    public Crawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        proxys.addEmpty(); //add myself
    }

    @Override
    public Page getResponse(CrawlDatum crawlDatum) throws Exception {
        HttpRequest request = new HttpRequest(crawlDatum);
        request.setProxy(proxys.nextRandom());
        return request.responsePage();
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        String url = page.url();
        if (match(page, next)) {
            PageInfo pageInfo = handle(page.doc(), url);
            index(pageInfo);
        }
    }

    /**
     * 判断url是否符合要求
     *
     * @param page
     * @param next
     * @return
     */
    public abstract boolean match(Page page, CrawlDatums next);

    /**
     * 处理页面返回PageInfo对象
     *
     * @param document
     * @param url
     * @return pageInfo
     */
    public abstract PageInfo handle(Document document, String url) throws NullPointerException;

    private void index(PageInfo pageInfo) {
        try {
            LOG.info("Index Start: " + pageInfo.toString());
            IndexUtils.index(pageInfo, "./indexDir");
            LOG.info("Index End: " + pageInfo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
