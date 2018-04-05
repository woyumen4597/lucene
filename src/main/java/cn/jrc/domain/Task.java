package cn.jrc.domain;

/**
 * state:
 * 0 未抓
 * 1 已抽取
 * 2 抓取失败
 * Created by Lucas.Jin on 2018/3/29.
 */
public class Task {
    private String url;
    private int state;
    public static int READY = 0;
    public static int EXTRACTED = 1;
    public static int FAILED = 2;

    public Task(String url, int state) {
        this.url = url;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Task{" +
                "url='" + url + '\'' +
                ", state=" + state +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
