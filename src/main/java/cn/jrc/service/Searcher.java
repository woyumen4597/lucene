package cn.jrc.service;

import cn.jrc.crawler.Collector;
import cn.jrc.dao.TaskDao;
import cn.jrc.domain.Page;
import cn.jrc.domain.Result;
import cn.jrc.util.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.DateTools;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private Collector collector;
    private static String SEEDS = "./files/seeds.txt";

    @Autowired
    private TaskDao dao;



    /**
     * sort desc by date as default
     * false order by date desc
     *
     * @param desc:true represents desc by date,false represents asc by date
     */
    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    private IndexSearcher getIndexSearcher() throws IOException {
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
    private List<Result> search(String queryString, int begin, int limit) throws IOException, ParseException, InvalidTokenOffsetsException {
        List<Result> list = new ArrayList<>();
        TopDocs topDocs = getTopDocs(queryString);
        LOG.info("Find Place " + topDocs.totalHits);
        QueryScorer scorer = new QueryScorer(query);// query score
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);// get digest which score
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
                "<b><font color='red'>", "</font></b>");// formatter
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);// highlight
        int middle = (int) Math.min(begin + limit, topDocs.totalHits);
        for (int i = begin; i < middle; i++) {
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            Document doc = searcher.doc(scoreDoc.doc);
            String description = doc.get("description");
            Result result = new Result();
            TokenStream tokenStream = analyzer.tokenStream("description", new StringReader(description));
            description = highlighter.getBestFragment(tokenStream, description);
            if (description == null) {
                description = doc.get("description");
            }
            description = description.substring(1, Math.min(description.length(), 200)) + "...";
            result.setDescription(description);
            result.setUrl(doc.get("url"));
            try {
                Date date = DateTools.stringToDate(doc.get("date"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                String format = simpleDateFormat.format(date);
                result.setDate(format);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            result.setTitle(doc.get("title"));
            list.add(result);
        }
        reader.close();
        return list;
    }

    private TopDocs getTopDocs(String queryString) throws IOException, ParseException {
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
        SortField titleSort = new SortField("title", SortField.Type.SCORE, false);
        SortField descriptionSort = new SortField("description", SortField.Type.SCORE, false);
        SortField answersSort = new SortField("answers", SortField.Type.SCORE, false);
        SortField sortField = new SortField("date", SortField.Type.STRING, desc);
        Sort sort = new Sort(new SortField[]{titleSort, descriptionSort, answersSort, sortField});
        topDocs = searcher.search(query, MAX_NUM, sort);
        return topDocs;
    }

    private int getCount(String queryString) throws IOException, ParseException {
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
            totalPage = Math.min(10, totalPage);
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

    /**
     * error control(use database to modify urls which was indexed fail)
     */
    public void modify(){
        List<String> urls = dao.getUrlsByState(0);
        collector = new Collector(urls);
        collector.extractAndIndex(false);
    }


    /**
     * collect index
     */
    public void collect(){
        List<String> seeds = FileUtils.readFromFile(SEEDS);
        collector = new Collector(seeds);
        collector.collect();
    }


    /**
     * incremental update for index
     * @param date  update_time in this date
     */
    public void update(String date){
        List<String> urls = dao.getUrlsByDate(date);
        collector = new Collector(urls);
        collector.extractAndIndex(true);
    }
}
