package cn.jrc.test;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/16 20:04
 * 只存储不共享，例如标题类字段，如果需要共享并排序，推荐使用SortedDocValuesField
 */
public class TestBinaryDocValuesField {
    public static void addBinaryDocValuesField(Document document,String name,String value){
        Field field = new BinaryDocValuesField(name,new BytesRef(value));
        document.add(field);
        //如果需要存储,加此句
        field = new StoredField(name,value);
        document.add(field);
    }

    public static void main(String[] args) throws IOException {

        Directory directory = new RAMDirectory();
        IndexWriter writer = new IndexWriter(directory,new IndexWriterConfig(new StandardAnalyzer()));
        Document document = new Document();
        addBinaryDocValuesField(document,"binaryValue","1234");
        writer.addDocument(document);

        document = new Document();
        addBinaryDocValuesField(document,"binaryValue","2345");
        writer.addDocument(document);

        document = new Document();
        addBinaryDocValuesField(document,"binaryValue","12345");
        writer.addDocument(document);

        writer.close();
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
        SortField binaryValue = new SortField("binaryValue", SortField.Type.STRING_VAL, true);
        TopFieldDocs search = searcher.search(new MatchAllDocsQuery(), 10, new Sort(binaryValue));
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println(searcher.doc(scoreDoc.doc));
        }
                


    }
}
