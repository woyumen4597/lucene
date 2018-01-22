package cn.jrc.spider;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/22 14:32
 */
public interface ComputeUrl {
    public boolean accept(String url,String pageContent);
}
