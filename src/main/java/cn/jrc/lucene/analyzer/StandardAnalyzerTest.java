package cn.jrc.lucene.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.search.highlight.*;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/15 13:33
 */
public class StandardAnalyzerTest {
    public static void analyze(Analyzer analyzer,String text) throws IOException, InvalidTokenOffsetsException {
        System.out.println("分词器: "+analyzer.getClass());
        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
        tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while(tokenStream.incrementToken()){
            CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
            System.out.println(attribute.toString());
        }
    }

    public static void main(String[] args) throws IOException, InvalidTokenOffsetsException {
        String chText = "金荣钏例如常见的StandardAnalyzer,作为Lucene中一款";
        IKAnalyzer analyzer = new IKAnalyzer(true);
        analyze(analyzer,chText);

    }
}
