package cn.jrc.db;

import cn.jrc.domain.Book;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/16 19:19
 */
public class DBSearch {
    private static int TOP_NUM = 100; //显示数
    public static final String INDEXPATH = "./indexDir";
    public static List<Book> searchData(String search) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEXPATH)));
        IndexSearcher searcher = new IndexSearcher(reader);
        String fieldString = "author";
        QueryParser parser = new QueryParser(fieldString,new IKAnalyzer());
        Query query = parser.parse(search);
        TopDocs topDocs = searcher.search(query, 5);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<Book> books = new ArrayList<>();
        for (int i=0;i<scoreDocs.length;i++) {
            Document doc = searcher.doc(i);
            Book book = new Book();
            book.setId(doc.getField("id").toString());
            book.setName(doc.getField("name").toString());
            book.setAuthor(doc.getField("author").toString());
            book.setImage(doc.getField("image").toString());
            books.add(book);
        }
        return books;

    }

    public static void main(String[] args) throws IOException, ParseException {
        searchData("吴承恩");
    }
}
