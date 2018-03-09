package cn.jrc.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
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
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);//增加Token表示的字符串属性
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.addAttribute(PositionIncrementAttribute.class);
        TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);
        tokenStream.reset();
        int position = 0;
        while(tokenStream.incrementToken()){
            CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
            System.out.println("输出结果: "+attribute.toString());
            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            System.out.println("start:"+startOffset+" end:"+endOffset);

            int increment = positionIncrementAttribute.getPositionIncrement();
            if(increment>0){
                position = position+increment;
            }
            System.out.println("第"+position+"个分词");
            System.out.println("类型:"+typeAttribute.type());
            System.out.println("------------");
        }
        tokenStream.close();
    }

    public static void main(String[] args) throws IOException, InvalidTokenOffsetsException {
        String chText = "金荣钏例如常见的StandardAnalyzer,作为Lucene中一款";
        IKAnalyzer analyzer = new IKAnalyzer(true);
        analyze(analyzer,chText);

    }
}
