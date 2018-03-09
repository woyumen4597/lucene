package cn.jrc.util;

import cn.jrc.lucene.search.TermInfo;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.PriorityQueue;

import java.io.IOException;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/3/9 19:59
 */
public class TermUtils {
    public static TermInfo[] getHighFreqTerms(IndexReader reader,int numTerms,String field) throws IOException {
        Terms terms = reader.getTermVector(0, field);
        TermsEnum termsEnum = terms.iterator();
        int minFreq = 0;
        BytesRef thisTerm = null;
        PriorityQueue<TermInfo> tiq = new PriorityQueue<TermInfo>(50) {
            @Override
            protected boolean lessThan(TermInfo a, TermInfo b) {
                return a.docFreq-b.docFreq>0;
            }
        };
        while((thisTerm=termsEnum.next())!=null){
            if(termsEnum.docFreq()>minFreq) {
                String termText = thisTerm.utf8ToString();
                tiq.insertWithOverflow(new TermInfo(termText, (int) termsEnum.totalTermFreq()));
                if(tiq.size()>=numTerms){
                    tiq.pop();
                    minFreq = ((TermInfo)tiq.pop()).docFreq;
                }
            }

        }
        TermInfo[] res = new TermInfo[tiq.size()];
        for (int i = 0; i < res.length; i++) {
            res[res.length-i-1] = tiq.pop();
        }
        return res;
    }
}
