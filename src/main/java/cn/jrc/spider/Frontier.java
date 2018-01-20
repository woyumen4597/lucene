package cn.jrc.spider;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/20 12:46
 */
public interface Frontier {
    public CrawlUrl getNext() throws Exception;
    public boolean putUrl(CrawlUrl url) throws Exception;
    //public boolean visited(CrawUrl url);
}
