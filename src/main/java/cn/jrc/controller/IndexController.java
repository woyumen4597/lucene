package cn.jrc.controller;

import cn.jrc.domain.Page;
import cn.jrc.domain.Result;
import cn.jrc.service.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public String ss(ModelMap modelMap) {
        return "index";
    }

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
}
