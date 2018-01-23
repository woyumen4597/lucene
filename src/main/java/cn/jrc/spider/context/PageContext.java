package cn.jrc.spider.context;

import org.jsoup.nodes.Node;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/23 15:58
 */
public class PageContext {
    private StringBuffer textBuffer;
    private int number;
    private Node node;

    public StringBuffer getTextBuffer() {
        return textBuffer;
    }

    public void setTextBuffer(StringBuffer textBuffer) {
        this.textBuffer = textBuffer;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
