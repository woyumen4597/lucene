package cn.jrc.service;

import cn.jrc.domain.Page;
import cn.jrc.domain.Result;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/4/10 14:50
 */
@Service
public class Searcher {
    private static Logger LOG = LoggerFactory.getLogger(Searcher.class);
    private IndexReader reader;
    private IndexSearcher searcher;
    private Query query;
    private Analyzer analyzer;
    private boolean desc = true;
    private int MAX_NUM = 100;

    /**
     * sort desc by date as default
     * false 代表按时间倒序
     *
     * @param desc:true represents desc by date,false represents asc by date
     */
    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public IndexSearcher getIndexSearcher() throws IOException {
        reader = DirectoryReader.open(FSDirectory.open(Paths.get("./indexDir")));
        searcher = new IndexSearcher(reader);
        return searcher;
    }

    /**
     * @param queryString
     * @param begin
     * @param limit
     * @return list of result
     * @throws IOException
     * @throws ParseException
     * @throws InvalidTokenOffsetsException
     */
    public List<Result> search(String queryString, int begin, int limit) throws IOException, ParseException, InvalidTokenOffsetsException {
        List<Result> list = new ArrayList<>();
        TopDocs topDocs = getTopDocs(queryString);
        LOG.info("Find Place " + topDocs.totalHits);
        QueryScorer scorer = new QueryScorer(query);// 查询得分
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);// 得到得分的片段，就是得到一段包含所查询的关键字的摘要
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
                "<b><font color='red'>", "</font></b>");// 对查询的数据格式化；无参构造器的默认是将关键字加粗
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);// 根据得分和格式化
        highlighter.setTextFragmenter(fragmenter);// 设置成高亮
        // 遍历topdocs查询结果,使用middle变量解决查询越界错误
        int middle = (int) Math.min(begin + limit, topDocs.totalHits);
        for (int i = begin; i < middle; i++) {
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            Document doc = searcher.doc(scoreDoc.doc);
            String description = doc.get("description");
            Result result = new Result();
            if (description != null) {
                TokenStream tokenStream = analyzer.tokenStream("description", new StringReader(description));
                // TokenStream将查询出来的搞成片段，得到的是整个内容
                description = highlighter.getBestFragment(tokenStream, description);
                result.setDescription(description);
                result.setUrl(doc.get("url"));
                result.setDate(doc.get("date"));
                result.setTitle(doc.get("title"));
            }
            list.add(result);
        }
        reader.close();
        return list;
    }

    public TopDocs getTopDocs(String queryString) throws IOException, ParseException {
        TopDocs topDocs = null;
        searcher = getIndexSearcher();
        analyzer = new IKAnalyzer();
        Query titleQuery = null, descQuery = null, answerQuery = null;
        //query for title
        QueryParser parser = new QueryParser("title", analyzer);
        titleQuery = parser.parse(queryString);
        //query for description
        parser = new QueryParser("description", analyzer);
        descQuery = parser.parse(queryString);
        LOG.info("Searching for: " + descQuery.toString("title"));
        //query for answers
        parser = new QueryParser("answers", analyzer);
        answerQuery = parser.parse(queryString);
        BooleanQuery booleanQuery = new BooleanQuery.Builder().add(titleQuery, BooleanClause.Occur.SHOULD)
                .add(descQuery, BooleanClause.Occur.SHOULD)
                .add(answerQuery, BooleanClause.Occur.SHOULD).build();
        query = booleanQuery.rewrite(reader);
        //sort
        SortField sortField = new SortField("date", SortField.Type.STRING, desc);
        Sort sort = new Sort(new SortField[]{sortField});
        topDocs = searcher.search(query, MAX_NUM, sort);
        return topDocs;
    }

    public int getCount(String queryString) throws IOException, ParseException {
        return (int) getTopDocs(queryString).totalHits;
    }


    public Page<Result> getPage(String queryString, int pageNum, int limit) {
        Page<Result> page = new Page<>();
        page.setPageNum(pageNum);
        page.setLimit(limit);
        try {
            int totalCount = getCount(queryString);
            page.setTotalCount(totalCount);
            int totalPage = (int) Math.ceil(totalCount * 1.0 / limit);
            page.setTotalPage(totalPage);
            int begin = (pageNum - 1) * limit;
            List<Result> list = search(queryString, begin, limit);
            page.setList(list);
            return page;
        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return null;
    }

}
