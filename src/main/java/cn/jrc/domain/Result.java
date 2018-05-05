package cn.jrc.domain;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/5/5 22:32
 */
public class Result {
    private String url;
    private String title;
    private String date;
    private String description;

    public Result() {
    }

    public Result(String url, String title, String date, String description) {
        this.url = url;
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Result{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
