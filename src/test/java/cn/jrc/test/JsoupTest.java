package cn.jrc.test;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;

import java.io.File;
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
            System.out.printf(" * a: <%s>  (%s)",link.attr("abs:href"),title);
        }
       // System.out.println(doc.getElementById("js_top_news"));
    }

    @Test
    public void testRemoveComments() throws IOException {
        File file = new File("./files/test1.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://www.baidu.com");
        removeComments(doc);
        System.out.println(doc.html());
    }

    public static void removeComments(Node node) throws IOException {
        for (int i = 0; i < node.childNodeSize(); i++) {
            Node child = node.childNode(i);
            if(child.nodeName().equals("#comment")){
                child.remove();
            }else{
                removeComments(child);
            }
        }
    }

    @Test
    public void testNodeVisitor() throws IOException {
        File file = new File("./files/test1.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://www.baidu.com");
        NodeTraversor.traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                System.out.println("head");
            }

            @Override
            public void tail(Node node, int depth) {
                System.out.println("tail");
            }
        },doc);
    }


}
