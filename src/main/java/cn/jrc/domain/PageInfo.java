package cn.jrc.domain;


import java.util.ArrayList;
import java.util.Date;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 16:24
 */
public class PageInfo {
    private String url;
    private String title;
    private String question;
    private ArrayList<String> answers;
    private ArrayList<String> tags;
    private Date date;
    private String description;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
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



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
