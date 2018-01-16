package cn.jrc.test;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/16 19:52
 * 对int型字段索引，只索引不存储，提供了一些静态工厂方法用于创建一般的查询，提供了不同于文本的数值类型存储方式，使用KD-trees索引
 */
public class TestIntPoint {
    public static void addIntPoint(Document document,String name,int value){
        Field field = new IntPoint(name,value);
        document.add(field);
        //要排序,必须添加一个同名的NumericDocValuesField
        field = new NumericDocValuesField(name,value);
        document.add(field);
        //要存储值,必须添加一个同名的StoredField
        field = new StoredField(name,value);
        document.add(field);
    }

    public static void main(String[] args) throws IOException {
        Document document = new Document();
        Directory directory =  new RAMDirectory();
        IndexWriter writer = new IndexWriter(directory,new IndexWriterConfig(new StandardAnalyzer()));
        addIntPoint(document,"intValue",10);
        writer.addDocument(document);

        document = new Document();
        addIntPoint(document, "intValue", 20);
        writer.addDocument(document);

        document = new Document();
        addIntPoint(document, "intValue", 30);
        writer.addDocument(document);

        writer.close();

        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
        // true代表从大到小
        SortField intValues = new SortField("intValue", SortField.Type.INT, true);
        TopFieldDocs search = searcher.search(new MatchAllDocsQuery(), 10, new Sort(intValues));
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println(searcher.doc(scoreDoc.doc));
        }
    }

}
