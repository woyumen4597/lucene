package cn.jrc.domain;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * data structure for crawler
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/18 17:12
 */
public class CrawlDatum implements Serializable{
    private String url = null;

    public CrawlDatum(){}

    public CrawlDatum(String url) {
        this.url = url;
    }
    // remove duplicate key
    private String key = null;

    public String url(){
        return url;
    }

    // fluent api
    public CrawlDatum url(String url) {
        this.url = url;
        return this;
    }

    public String key(){
        if(key==null){
            return url;
        }else{
            return key;
        }
    }

    public CrawlDatum key(String key) {
        this.key = key;
        return this;
    }

    public String briefInfo(){
        return String.format("CrawlDatum: %s (URL: %s)",key(),url());
    }

    /**
     *
     * 判断当前Page的URL是否与输入正则匹配
     * @param urlRegex
     * @return
     */
    public boolean matchUrl(String urlRegex) {
        return Pattern.matches(urlRegex, url());
    }
}
