package cn.jrc.spider.crawler;

import cn.jrc.domain.PageInfo;
import org.jsoup.nodes.Document;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 18:49
 */
public interface Handler {
    PageInfo handle(Document document,String url);
    void index(PageInfo pageInfo);
}
