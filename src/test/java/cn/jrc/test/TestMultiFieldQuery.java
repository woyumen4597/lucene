package cn.jrc.test;

import cn.jrc.domain.Book;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/17 11:10
 * MUST+MUST = AND,
 * SHOULD+MUST = MUST
 * SHOULD+SHOULD = OR
 * 
 */
public class TestMultiFieldQuery {
    public static final String INDEXPATH = "./indexDir";

    public static void main(String[] args) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEXPATH)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = MultiFieldQueryParser.parse(new String[]{"01334be", "吴承恩"}, new String[]{"id", "author"}, new BooleanClause.Occur[]{BooleanClause.Occur.SHOULD,
                BooleanClause.Occur.MUST}, new IKAnalyzer());
        TopDocs search = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = search.scoreDocs;
        System.out.println(scoreDocs.length);
        for (int i=0;i<scoreDocs.length;i++) {
            Document doc = searcher.doc(i);
            Book book = new Book();
            book.setId(doc.getField("id").toString());
            book.setName(doc.getField("name").toString());
            book.setAuthor(doc.getField("author").toString());
            book.setImage(doc.getField("image").toString());
            System.out.println(book);
        }
    }
}
