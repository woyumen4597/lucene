package cn.jrc.domain;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 存储多个datum的数据结构
 *
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/18 17:22
 */
public class CrawlDatums implements Iterable<CrawlDatum> {
    protected LinkedList<CrawlDatum> dataList = new LinkedList<>();

    public CrawlDatums() {
    }

    public CrawlDatums(Iterable<String> links) {
        add(links);
    }

    private CrawlDatums add(Iterable<String> links) {
        for (String link : links) {
            add(link);
        }
        return this;
    }

    private CrawlDatums add(String link) {
        CrawlDatum datum = new CrawlDatum(link);
        return add(datum);
    }

    public CrawlDatums add(CrawlDatum datum) {
        dataList.add(datum);
        return this;
    }

    public CrawlDatum get(int index) {
        return dataList.get(index);
    }

    public int size() {
        return dataList.size();
    }

    public CrawlDatum remove(int index) {
        return dataList.remove(index);
    }

    public void clear() {
        dataList.clear();
    }

    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    @Override
    public String toString() {
        return dataList.toString();
    }


    @Override
    public Iterator<CrawlDatum> iterator() {
        return dataList.iterator();
    }
}
