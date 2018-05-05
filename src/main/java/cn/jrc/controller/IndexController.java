package cn.jrc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/5/5 17:52
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String ss(ModelMap modelMap) {
        modelMap.addAttribute("message","ssss");
        return "index";
    }

    @RequestMapping("/query")
    public String query(@RequestParam String q,ModelMap modelMap){
        System.out.println(q);
        return "list";
    }
}
