package cn.jrc.spider;


import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.filters.LinkStringFilter;

import java.util.Set;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/22 14:33
 */
public class MyCrawer {
    private ComputeUrl computeUrl = null;
    public MyCrawer(){
        computeUrl = new PageRankComputeUrl();
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
        LinkStringFilter filter = new LinkStringFilter("(http://.+?\\.163\\.com/.+)\"");
        initCrawlerWithSeeds(seeds);
        while(!LinkQueue.unVisitedUrlsEmpty()&&LinkQueue.getVisitedUrlNum()<=1000){
            String visitUrl = (String) LinkQueue.unVisitedUrlDequeue();
            if (visitUrl == null) {
                continue;
            }
            DownloadFile downloader = new DownloadFile();
            String content = downloader.downloadFile(visitUrl);
            if(computeUrl.accept(visitUrl,content)){
                continue;
            }
            LinkQueue.addVisitedUrl(visitUrl);
            Set<String> links = HtmlParserTool.extractLinks(visitUrl, filter);
            for (String link : links) {
                LinkQueue.addUnvisitedUrl(link);
            }
        }
    }

    public static void main(String[] args) {
        MyCrawer crawer = new MyCrawer();
        crawer.crawling(new String[]{"http://news.163.com"});
    }

}
