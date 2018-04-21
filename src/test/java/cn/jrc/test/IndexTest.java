package cn.jrc.test;

import cn.jrc.crawler.Crawler;
import cn.jrc.crawler.STOCrawler;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Assert;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 19:35
 */
public class IndexTest {
    public IndexSearcher getIndexSearcher() throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("./indexDir")));
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    public void search(Query query) throws IOException {
        IndexSearcher searcher = getIndexSearcher();
        TopDocs topDocs = searcher.search(query, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.getField("title"));
        }
    }

    @Test
    public void testSearch() throws IOException, ParseException {
        IndexSearcher searcher = getIndexSearcher();
        Analyzer analyzer = new IKAnalyzer();
        QueryParser parser = new QueryParser("title", analyzer);
        Query query = parser.parse("(server OR webpack)");
        TopDocs topDocs = searcher.search(query, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.getField("title"));
        }
    }


    @Test
    public void testTermQuery() throws IOException {
        Term term = new Term("title", "java");
        Query query = new TermQuery(term);
        search(query);
    }

    @Test
    public void testMatchNoDocsQuery() throws IOException {
        Query query = new MatchNoDocsQuery();
        search(query);
    }

    @Test
    public void testTermRangeQuery() throws IOException {
        //搜索起始字母范围从a到z的title
        Query query = new TermRangeQuery("title", new BytesRef("a"), new BytesRef("f"), true, true);
        search(query);
    }

    @Test
    public void testQueryParser() throws IOException, ParseException {
        //使用WhitespaceAnalyzer分析器不会忽略大小写，也就是说大小写敏感
        QueryParser parser = new QueryParser("title", new WhitespaceAnalyzer());
        Query query = parser.parse("+visual +studio");
        search(query);

        //有点需要注意，在QueryParser解析通配符表达式的时候，一定要用引号包起来，然后作为字符串传递给parse函数
        query = new QueryParser("field", new StandardAnalyzer()).parse("\"This is some phrase*\"");
        Assert.assertEquals("analyzed", "\"? ? some phrase\"", query.toString("field"));
        System.out.println("---------------------");
        //使用QueryParser解析"~"，~代表编辑距离，~后面参数的取值在0-2之间，默认值是2，不要使用浮点数
        query = parser.parse("jav~2");
        search(query);
    }

    @Test
    public void testBooleanQuery() throws IOException {
        Query termQuery1 = new TermQuery(new Term("title", "java"));
        Query termQuery2 = new TermQuery(new Term("title", "c++"));
        //测试or查询
        BooleanQuery query = new BooleanQuery.Builder().add(termQuery1, BooleanClause.Occur.SHOULD)
                .add(termQuery2, BooleanClause.Occur.SHOULD).build();
        search(query);
        System.out.println("-------------------------");
        query = new BooleanQuery.Builder().add(termQuery2, BooleanClause.Occur.MUST)
                .add(new TermQuery(new Term("title", "class")), BooleanClause.Occur.MUST).build();
        search(query);

    }

    @Test
    public void testPhraseQuery() throws IOException {
        //设置两个短语之间的跨度为2，也就是说has和bridges之间的短语小于等于均可检索到
        PhraseQuery query = new PhraseQuery.Builder().setSlop(2)
                .add(new Term("title", "class"))
                .add(new Term("title", "c++")).build();
        search(query);
    }

    @Test
    public void testMatchAllDocQuery() throws IOException {
        Query query = new MatchAllDocsQuery();
        search(query);
    }


    @Test
    public void testFuzzyQuery() throws IOException {
        Query query = new FuzzyQuery(new Term("title", "jav"));
        search(query);
    }

    @Test
    public void testWildQuery() throws IOException {
        Query query = new WildcardQuery(new Term("title", "*ava"));
        search(query);
    }

    @Test
    public void testPrefixQuery() throws IOException {
        Query query = new PrefixQuery(new Term("title", "ja"));
        search(query);
    }

    @Test
    public void testMultiPhraseQuery() throws IOException {
        Term[] terms = new Term[]{new Term("title", "java"), new Term("title", "brace")};
        Term term2 = new Term("title", "efficiency");
        //多个add之间认为是OR操作，即(has lots)和bridges之间的slop不大于3，不计算标点
        MultiPhraseQuery multiPhraseQuery = new MultiPhraseQuery.Builder().add(terms).add(term2).setSlop(3).build();
        search(multiPhraseQuery);
    }


    @Test
    public void updateIndex() throws IOException {
        String url = "https://stackoverflow.com/questions/987142/make-gitignore-ignore-everything-except-a-few-files";
        Crawler crawler = new STOCrawler(url);
        crawler.visit();

    }
}
