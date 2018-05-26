package cn.jrc.controller;

import cn.jrc.domain.Page;
import cn.jrc.domain.Result;
import cn.jrc.service.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/5/5 17:52
 */
@Controller
public class IndexController {
    public static Logger LOG = LoggerFactory.getLogger(IndexController.class);
    private int limit = 10;//num of record per page
    @Autowired
    private Searcher searcher;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * return page.html with Page<result>
     * @param q queryString
     * @param pageNum
     * @param model
     * @return
     */
    @RequestMapping("/query")
    public String query(@RequestParam String q, @RequestParam(required = false) String pageNum, Model model) {
        LOG.info("Query: " + q);
        if (q == null || q.trim().length() == 0) {
            model.addAttribute("msg", "Please input your query");
            return "page";
        }
        if (pageNum == null || pageNum.length() == 0) {
            pageNum = "1";
        }
        Page<Result> page = searcher.getPage(q, Integer.parseInt(pageNum), limit);
        model.addAttribute("page", page);
        model.addAttribute("list", page.getList());
        return "page";
    }

    @RequestMapping("/page")
    public String page() {
        return "page";
    }


    @RequestMapping("/api/modify")
    public ResponseEntity modify(){
        searcher.modify();
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/api/collect")
    public ResponseEntity collect(){
        searcher.collect();
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/api/update")
    public ResponseEntity update(@RequestParam("date") String date){
        searcher.update(date);
        return new ResponseEntity(HttpStatus.OK);
    }
}
