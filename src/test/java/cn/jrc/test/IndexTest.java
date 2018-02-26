package cn.jrc.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
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

    @Test
    public void testSearch() throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("./indexDir")));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new IKAnalyzer();
        QueryParser parser = new QueryParser("title",analyzer);
        Term term = new Term("title","代码");
        Query query = new FuzzyQuery(term);
        TopDocs topDocs = searcher.search(query, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.getField("title"));
        }
    }
}
