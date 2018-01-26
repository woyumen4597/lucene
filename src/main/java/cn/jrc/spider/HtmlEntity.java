package cn.jrc.spider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/26 16:31
 */
public class HtmlEntity {
    private String path;
    private String content;

    //外链(本页面所连接的其他页面)
    private List<String> outLinks = new ArrayList<>();

    //内链
    private List<String> inLinks = new ArrayList<>();

    private double pr;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getOutLinks() {
        return outLinks;
    }

    public void setOutLinks(List<String> outLinks) {
        this.outLinks = outLinks;
    }

    public List<String> getInLinks() {
        return inLinks;
    }

    public void setInLinks(List<String> inLinks) {
        this.inLinks = inLinks;
    }

    public double getPr() {
        return pr;
    }

    public void setPr(double pr) {
        this.pr = pr;
    }
}
