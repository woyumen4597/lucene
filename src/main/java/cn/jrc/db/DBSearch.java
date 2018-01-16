package cn.jrc.db;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/16 19:19
 */
public class DBSearch {
    private static int TOP_NUM = 100; //显示数
    public static final String INDEXPATH = "./indexDir";
    public static void searchData(String search) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEXPATH)));
        System.out.println("freq "+reader.getSumDocFreq("name"));
        IndexSearcher searcher = new IndexSearcher(reader);
        String fieldString = "author";
        QueryParser parser = new QueryParser(fieldString,new IKAnalyzer());
        Query query = parser.parse(search);
        TopDocs topDocs = searcher.search(query, 5);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i=0;i<scoreDocs.length;i++) {
            Document doc = searcher.doc(i);
            System.out.println(doc.getField("id").toString()+" "+doc.getField("name").toString()+" "+doc.getField("author").toString()+" "+
            doc.getField("stock"));
        }

    }

    public static void main(String[] args) throws IOException, ParseException {
        searchData("吴承恩");
    }
}
