package cn.jrc.spider.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.File;
import java.io.IOException;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/27 14:17
 */
public class NodeUtil {

    public static int getNumLinks(final Node iNode){
        int links = 0;
        for (int i = 0; i < iNode.childNodeSize(); i++) {
            Node node = iNode.childNode(i);
            if(node !=null) {
                links += getNumLinks(node);
            }
        }
        if(isLink(iNode)){
            links++;
        }
        return links;
    }

    private static boolean isLink(Node iNode) {
        return iNode.toString().trim().contains("href");
    }

    public static void main(String[] args) throws IOException {
        Document document = Jsoup.parse(new File("./files/test1.html"), "UTF-8", "http://www.baidu.com");
        Element ss = document.getElementById("ss");
        System.out.println(getNumLinks(ss));
    }
}
