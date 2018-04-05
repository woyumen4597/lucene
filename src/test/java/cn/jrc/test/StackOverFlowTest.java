package cn.jrc.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/27 16:45
 */
public class StackOverFlowTest {
    public static void main(String[] args) throws IOException {
        String url = "https://stackoverflow.com/questions/";
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("a[href]");
        for (Element element : elements) {
            String link  = element.attr("abs:href");
            if(link.trim().contains("/questions")){
                System.out.println(link);
            }
        }

    }
}
