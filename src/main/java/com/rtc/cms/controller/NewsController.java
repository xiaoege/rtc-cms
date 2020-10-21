package com.rtc.cms.controller;

import com.rtc.cms.service.NewsService;
import com.rtc.cms.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author ChenHang
 */
@Controller
@RequestMapping("news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("initNews")
    public String initNews() {
        return "news";
    }

    @GetMapping("toNewsDetail")
    public String operationNews(@RequestParam(name = "id") int id) {
        return "newsDetail";
    }
    @GetMapping("toNewsEdit")
    public String toNewsEdit(@RequestParam(name = "id") int id) {
        return "newsEdit";
    }

    /**
     * 查询新闻列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @GetMapping("listNews")
    public ResultData listNews(@RequestParam(name = "page", required = false, defaultValue = "1") int pageNum,
                               @RequestParam(name = "limit", required = false, defaultValue = "20") int pageSize) {
        return newsService.listNews(pageNum, pageSize);
    }

    /**
     * 查询单个新闻
     *
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("getNews")
    public ResultData getNews(@RequestParam(required = false) int id) {
        return newsService.getNews(id);
    }

    /**
     * 审核新闻
     *
     * @param body
     * @return
     */
    @ResponseBody
    @PostMapping("examinateNews")
    public ResultData examinateNews(@RequestBody String body) {
        return newsService.examinateNews(body);
    }

    /**
     * 删除新闻
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("removeNews")
    public ResultData removeNews(@RequestParam int id) {
        return newsService.removeNews(id);
    }

    /**
     * 修改新闻
     * @param body
     * @return
     */
    @ResponseBody
    @PostMapping("modifyNews")
    public ResultData modifyNews(@RequestBody String body) {
        return newsService.modifyNews(body);
    }
}
