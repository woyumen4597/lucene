package cn.jrc.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
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
    public void testSearch() throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("./indexDir")));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new IKAnalyzer();
        QueryParser parser = new QueryParser("title",analyzer);
        Query query = parser.parse("(server OR webpack)");
        TopDocs topDocs = searcher.search(query, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.getField("title"));
        }
    }

    @Test
    public void testIndexReader() throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("./indexDir")));
        //sum of docs
        System.out.println(reader.numDocs());
        //returns the number of documents that have at least one term for this field
        System.out.println(reader.getDocCount("answers"));
        //测试某个词在某字段中的频率
        System.out.println(reader.docFreq(new Term("tags", "javascript")));
        System.out.println(reader.maxDoc());
        Terms terms = reader.getTermVector(0, "description"); //矢量计算用来计算相似文档时使用
        System.out.println(reader.getSumDocFreq("answers"));
        //测试某个词在某字段中的频率
        System.out.println(reader.totalTermFreq(new Term("tags", "javascript")));
        Document document = reader.document(1);
        for (IndexableField field : document.getFields()) {
            System.out.println("name: "+field.name()+" type: "+field.fieldType()+" value: "+field.stringValue());
        }
    }
}
