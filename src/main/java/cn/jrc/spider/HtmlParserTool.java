package cn.jrc.spider;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/22 14:59
 */
public class HtmlParserTool {
    public static Set<String> extractLinks(String url, LinkStringFilter filter) {
        Set<String> links = new HashSet<>();
        try {
            Parser parser = new Parser(url);
           // parser.setEncoding("utf8");
            NodeFilter frameFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    if (node.getText().startsWith("frame src=")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);
            for (int i = 0; i < list.size(); i++) {
                Node tag = list.elementAt(i);
                if (tag instanceof LinkTag) {
                    LinkTag link = (LinkTag) tag;
                    String linkUrl = link.getLink();
                    links.add(linkUrl);
//                    if (filter.accept(link)) {
//                        links.add(linkUrl);
//                    }
                } else {
                    String frame = tag.getText();
                    int start = frame.indexOf("src=");
                    frame = frame.substring(start);
                    int end = frame.indexOf(" ");
                    if (end == -1) {
                        end = frame.indexOf(">");
                    }
                    String frameUrl = frame.substring(5, end - 1);
                    LinkTag linkTag = new LinkTag();
                    linkTag.setLink(frameUrl);
                    links.add(frameUrl);
                    /*if (filter.accept(linkTag)) {
                        links.add(frameUrl);
                    }*/
                }

            }
        } catch (ParserException e) {
            e.printStackTrace();
        }
        return links;
    }
}
