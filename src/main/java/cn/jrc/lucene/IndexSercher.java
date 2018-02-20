package cn.jrc.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;


/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/11 19:39
 */
public class IndexSercher {
    public static  void search(String indexDir,String s) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("文档",analyzer);
        parser.setDefaultOperator(QueryParser.Operator.AND); //查询语句的逻辑关系(分词)
//        Query query = parser.parse(s);
        Term term = new Term("文档","xia");
        //通配符
        //Query query = new WildcardQuery(term);
        //模糊查询
         Query query = new FuzzyQuery(term);
        long start = System.currentTimeMillis();
        TopDocs docs = searcher.search(query, 10);
        long end = System.currentTimeMillis();
        System.out.println("发现 "+docs.totalHits+"文档 (共花费 "+(end-start)+" milis "+s+"匹配");
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("路径")+" "+doc.getField("文档"));
        }
    }
}
