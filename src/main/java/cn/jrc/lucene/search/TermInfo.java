package cn.jrc.lucene.search;


/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/3/9 19:58
 */
public class TermInfo {
    public String term;
    public int docFreq;

    public TermInfo(String term, int docFreq) {
        this.term = term;
        this.docFreq = docFreq;
    }
}