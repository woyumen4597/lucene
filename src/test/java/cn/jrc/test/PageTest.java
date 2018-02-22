package cn.jrc.test;

import cn.jrc.domain.PageInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 16:29
 */
public class PageTest {
    public static PageInfo getPageInfo() throws IOException {
        Document document = Jsoup.parse(new File("./files/1.html"), "utf-8");
        PageInfo  pageInfo = new PageInfo();
        String title = document.getElementsByTag("title").get(0).text();
        pageInfo.setTitle(title);
        String uri = document.baseUri();
        pageInfo.setUrl(uri);
        pageInfo.setQuestion(title);
        Elements elements = document.select("div.tags > a");
        ArrayList<String> tags = new ArrayList<>();
        for (Element element : elements) {
            tags.add(element.text());
        }
        pageInfo.setTags(tags);
        ArrayList<String> answers = new ArrayList<>();
        Elements elements1 = document.select("div.answer_list>div>div>p");
        for (Element element : elements1) {
            answers.add(element.text());
        }
        pageInfo.setAnswers(answers);
        pageInfo.setDate(new Date());
        String description = document.getElementsByTag("dd").get(0).text();
        pageInfo.setDescription(description);
        return pageInfo;
    }
}
