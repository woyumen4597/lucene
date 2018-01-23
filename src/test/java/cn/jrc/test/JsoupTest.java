package cn.jrc.test;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/23 15:25
 */
public class JsoupTest {
    public static void main(String[] args) throws IOException {
        String url = "http://news.163.com";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        InputStream content = response.getEntity().getContent();
        Document doc = Jsoup.parse(content,"gb2312","http://news.163.com");
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String title = link.text();
            System.out.println(title);
        }
    }
}
