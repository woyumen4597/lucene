package cn.jrc.spider.rmdup;

import org.htmlparser.Node;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * similarity between pages
 *
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/23 19:36
 */
public class PageSim {
    public static double getPageSim(String urlStr1, String urlStr2) throws IOException, ParserException {
        ArrayList<Node> pageNodes1 = new ArrayList<>();
        URL url = new URL(urlStr1);
        Node node;
        Lexer lexer = new Lexer(url.openConnection());
        lexer.setNodeFactory(new PrototypicalNodeFactory());
        while (null != (node = lexer.nextNode())) {
            pageNodes1.add(node);
        }

        URL url2 = new URL(urlStr2);
        ArrayList<Node> pageNodes2 = new ArrayList<>();
        lexer = new Lexer(url2.openConnection());
        lexer.setNodeFactory(new PrototypicalNodeFactory());
        while (null != (node = lexer.nextNode())) {
            pageNodes2.add(node);
        }
        double distance = PageDistance.LCS(pageNodes1.toArray(), pageNodes2.toArray());
        return (2.0*distance)/((double)pageNodes1.size()+(double)pageNodes2.size());
    }

    public static void main(String[] args) throws IOException, ParserException {
        double sim = getPageSim("http://www.baidu.com", "http://www.baidu.com");
        System.out.println(sim);
    }
}
