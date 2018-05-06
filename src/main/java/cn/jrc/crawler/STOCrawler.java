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
 * StackOverflow Crawler
 *
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/25 15:55
 */
public class STOCrawler extends Crawler {


    public STOCrawler(String url) throws IOException {
        super(url);
    }


    @Override
    public boolean match(String link) {
        return Pattern.matches("https://stackoverflow.com/questions/[0-9]+.*", link)
                && !link.contains("?") && !link.contains("#");
    }

    @Override
    public PageInfo handle(Document document, String url) {
        PageInfo pageInfo = new PageInfo();
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
        Elements elements = document.select("div.answercell>div.post-text");
        for (Element element : elements) {
            answers.add(element.text());
        }
        pageInfo.setAnswers(answers);
        pageInfo.setUrl(url);
        pageInfo.setDate(new Date());
        return pageInfo;
    }

}
