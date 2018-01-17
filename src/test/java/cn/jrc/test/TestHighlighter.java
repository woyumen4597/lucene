package cn.jrc.test;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/17 14:50
 */
public class TestHighlighter {
    public static void main(String[] args) throws IOException, InvalidTokenOffsetsException {
        String text = "高亮显示技术已经实现了将评分最高的片段呈现给了用户的功能";
        TermQuery query = new TermQuery(new Term("query", "技术"));
        QueryScorer scorer = new QueryScorer(query);
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        Highlighter highlighter = new Highlighter(formatter, scorer);
        TokenStream tokenStream = new IKAnalyzer().tokenStream("field", new StringReader(text));
        System.out.println(highlighter.getBestFragment(tokenStream,text));
    }
}
