package cn.jrc.lucene.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.builders.RangeQueryBuilder;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/4/10 14:50
 */
public class Searcher {
    private IndexReader reader;
    private IndexSearcher searcher;

    public IndexSearcher getIndexSearcher() throws IOException {
        reader = DirectoryReader.open(FSDirectory.open(Paths.get("./indexDir")));
        searcher = new IndexSearcher(reader);
        return searcher;
    }

    /**
     * sort desc by date as default
     *
     * @param queryString
     * @throws ParseException
     * @throws InvalidTokenOffsetsException
     * @throws IOException
     */
    public void search(String queryString) throws ParseException, InvalidTokenOffsetsException, IOException {
        search(queryString, true);
    }

    /**
     * @param queryString
     * @param desc:true   represents desc by date,false represents asc by date
     * @throws IOException
     * @throws ParseException
     * @throws InvalidTokenOffsetsException
     */
    public void search(String queryString, boolean desc) throws IOException, ParseException, InvalidTokenOffsetsException {
        IndexSearcher searcher = getIndexSearcher();
        Analyzer analyzer = new IKAnalyzer();
        Query titleQuery = null, descQuery = null, answerQuery = null, query = null;
        //query for title
        QueryParser parser = new QueryParser("title", analyzer);
        titleQuery = parser.parse(queryString);
        //query for description
        parser = new QueryParser("description", analyzer);
        descQuery = parser.parse(queryString);
        System.out.println("Searching for: " + descQuery.toString("title"));
        //query for answers
        parser = new QueryParser("answers", analyzer);
        answerQuery = parser.parse(queryString);
        BooleanQuery booleanQuery = new BooleanQuery.Builder().add(titleQuery, BooleanClause.Occur.SHOULD)
                .add(descQuery, BooleanClause.Occur.SHOULD)
                .add(answerQuery, BooleanClause.Occur.SHOULD).build();
        query = booleanQuery.rewrite(reader);

        TopDocs topDocs = null;
        //sort
        SortField sortField = new SortField("date", SortField.Type.STRING, desc);
        Sort sort = new Sort(new SortField[]{sortField});
        topDocs = searcher.search(query, 10, sort);
        System.out.println("Find Place " + topDocs.totalHits);
        QueryScorer scorer = new QueryScorer(query);// 查询得分
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);// 得到得分的片段，就是得到一段包含所查询的关键字的摘要
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
                "<b><font color='red'>", "</font></b>");// 对查询的数据格式化；无参构造器的默认是将关键字加粗
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);// 根据得分和格式化
        highlighter.setTextFragmenter(fragmenter);// 设置成高亮
        String result;
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String description = doc.get("description");
            if (description != null) {
                TokenStream tokenStream = analyzer.tokenStream("description", new StringReader(description));
                // TokenStream将查询出来的搞成片段，得到的是整个内容
                result = highlighter.getBestFragment(tokenStream, description);
                if (result == null) {
                    System.out.println("desc: " + doc.get("title") + " date: " + doc.get("date"));
                } else {
                    System.out.println("desc: " + result);
                }
            }
        }
        reader.close();
    }

    public static void main(String[] args) throws IOException, InvalidTokenOffsetsException, ParseException {
        Searcher searcher = new Searcher();
        searcher.search("java C++", true);
    }

}
