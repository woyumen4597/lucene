package cn.jrc.crawler;

import cn.jrc.domain.PageInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * SegmentFault Crawler
 *
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/23 16:37
 */
public class SGFCrawler extends Crawler {


    public SGFCrawler(String url) throws IOException {
        super(url);
    }


    @Override
    public boolean match(String link) {
        return Pattern.matches("https://segmentfault.com/q/[0-9]*",link);
    }

    @Override
    public PageInfo handle(Document document, String url) {
        PageInfo pageInfo = new PageInfo();
        String title = document.select("h1#questionTitle>a").text();
        Elements elements = document.select("a.tag");
        ArrayList<String> tags = new ArrayList<>();
        for (Element element : elements) {
            tags.add(element.text());
        }
        String description = document.select("div.question").text();
        ArrayList<String> answers = new ArrayList<>();
        Elements elements1 = document.select("div.answer");
        for (Element element : elements1) {
            answers.add(element.text());
        }
        pageInfo.setTitle(title);
        pageInfo.setTags(tags);
        pageInfo.setUrl(url);
        pageInfo.setDescription(description);
        pageInfo.setAnswers(answers);
        pageInfo.setDate(new Date());
        return pageInfo;
    }

}
