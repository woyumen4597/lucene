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
import java.util.regex.Pattern;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 16:29
 */
public class PageTest {
    @Test
    public void CSDN() throws IOException {
        Document document = Jsoup.parse(new File("./files/csdn.html"), "utf-8");
        PageInfo  pageInfo = new PageInfo();
        String title = document.getElementsByTag("title").get(0).text();
        pageInfo.setTitle(title);
        String uri = document.baseUri();
        pageInfo.setUrl(uri);
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
    }

    @Test
    public void SGF() throws IOException {
        PageInfo pageInfo = new PageInfo();
        Document document = Jsoup.parse(new File("./files/sgf.html"), "utf-8");
        String title = document.select("h1#questionTitle>a").text();
        pageInfo.setTitle(title);
        Elements elements = document.select("a.tag");
        ArrayList<String> tags = new ArrayList<>();
        for (Element element : elements) {
            tags.add(element.text());
        }
        pageInfo.setTags(tags);
        pageInfo.setUrl(document.baseUri());
        String description = document.select("div.question").text();
        pageInfo.setDescription(description);
        ArrayList<String> answers = new ArrayList<>();
        Elements elements1 = document.select("div.answer");
        for (Element element : elements1) {
            answers.add(element.text());
        }
        pageInfo.setAnswers(answers);
        pageInfo.setDate(new Date());
    }

    @Test
    public void STO() throws IOException {
        PageInfo pageInfo = new PageInfo();
        Document document = Jsoup.parse(new File("./files/sto.html"), "utf-8");
        String title = document.select("div#question-header>h1>a.question-hyperlink").text();
        pageInfo.setTitle(title);
        String description = document.select("div.post-text").get(0).text();
        pageInfo.setDescription(description);
        Elements select = document.select("div.post-taglist>a");
        ArrayList<String> tags = new ArrayList<>();
        for (Element element : select) {
            tags.add(element.text());
        }
        pageInfo.setTags(tags);
        ArrayList<String> answers = new ArrayList<>();
        Elements elements = document.select("td.answercell>div.post-text");
        for (Element element : elements) {
            answers.add(element.text());
        }
        pageInfo.setAnswers(answers);
        pageInfo.setUrl(document.baseUri());
        pageInfo.setDate(new Date());
    }

    @Test
    public void testUrlMatch(){
        String url = "https://stackoverflow.com/questions/14994391";
        String urlRegex = "https://stackoverflow.com/questions/[0-9]*";
        boolean matches = Pattern.matches(urlRegex, url);
        System.out.println(matches);
    }
}
