package cn.jrc.test;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/17 10:48
 */
public class TestIkDic {
    public static void main(String[] args) throws IOException {
        String s = "金荣钏关于停止词的应用问题,我们可以找到一些有关的资料进行系统的学习";
        StringReader reader = new StringReader(s);
        IKAnalyzer analyzer = new IKAnalyzer();
        TokenStream stream = analyzer.tokenStream("field", reader);
        stream.reset();
        boolean hasnext = stream.incrementToken();
        while(hasnext){
            CharTermAttribute attribute = stream.getAttribute(CharTermAttribute.class);
            System.out.println(attribute.toString());
            hasnext = stream.incrementToken();
        }

    }
}
