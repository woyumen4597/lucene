package cn.jrc.crawler;

import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.jrc.domain.PageInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CSDN Crawler
 *
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/23 16:05
 */
public class CSDNCrawler extends Crawler {

    public CSDNCrawler(String url) throws IOException {
        super(url);
    }


    @Override
    public boolean match(String link) {
        return Pattern.matches("https://ask.csdn.net/questions/[0-9]+", link);
    }


    @Override
    public PageInfo handle(Document document, String url) {
        PageInfo pageInfo = new PageInfo();
        String title = document.getElementsByTag("title").get(0).text();
        pageInfo.setTitle(title);
        pageInfo.setUrl(url);
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