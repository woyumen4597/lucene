package cn.jrc.test;

import cn.jrc.util.Downloader;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 16:29
 */
public class PageTest {
    @Test
    public void testHeader() throws IOException {
        Document document = Jsoup.parse(new File("./files/1.html"), "utf-8");
        Element head = document.head();
        String contenttype = head.attr("ContentType");
        System.out.println(contenttype);

    }
}
