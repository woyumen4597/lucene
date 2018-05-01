package cn.jrc.util;

import cn.jrc.domain.PageInfo;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 18:58
 */
public class IndexUtils {
    private static String indexDir = "./indexDir";

    public static IndexSearcher getIndexSearcher() throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    public static void index(PageInfo pageInfo, String indexDir) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IKAnalyzer analyzer = new IKAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer;
        try {
            writer = new IndexWriter(directory, iwc);
            Document document = addDocument(pageInfo);
            writer.addDocument(document);
            writer.commit();
            writer.close();
        } catch (LockObtainFailedException e) {
            // do nothing this is common exception
        }

    }

    private static Document addDocument(PageInfo pageInfo) {
        Document document = new Document();
        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        document.add(new StringField("url", pageInfo.getUrl(), Field.Store.YES));
        document.add(new TextField("title", pageInfo.getTitle(), Field.Store.YES));
        String answers = GsonUtils.fromList2Json(pageInfo.getAnswers());
        document.add(new TextField("answers", answers, Field.Store.YES));
        String tags = GsonUtils.fromList2Json(pageInfo.getTags());
        document.add(new TextField("tags", tags, Field.Store.YES));
        document.add(new StringField("date", pageInfo.getDate().toString(), Field.Store.YES));
        document.add(new TextField("description", pageInfo.getDescription(), Field.Store.YES));
        return document;
    }

    public static void update(PageInfo pageInfo, String indexDir) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IKAnalyzer analyzer = new IKAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer;
        try {
            writer = new IndexWriter(directory, iwc);
            Term term = new Term("url", pageInfo.getUrl());
            if (!pageInfo.getAnswers().isEmpty()) {
                Document document = addDocument(pageInfo);
                writer.updateDocument(term, document);
                writer.commit();
                writer.close();
            }
        } catch (LockObtainFailedException e) {
            // do nothing this is common exception
        }
    }

    public static void delete(Term term) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IKAnalyzer analyzer = new IKAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer;
        writer = new IndexWriter(directory, iwc);
        writer.deleteDocuments(term);
        writer.commit();
        writer.close();
    }
}
