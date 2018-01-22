package cn.jrc.spider;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/22 14:35
 */
public class PageRankComputeUrl implements ComputeUrl {
    @Override
    public boolean accept(String url, String pageContent) {
        return false;
    }
}
