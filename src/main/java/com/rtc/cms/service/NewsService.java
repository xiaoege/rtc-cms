package com.rtc.cms.service;

import com.rtc.cms.vo.ResultData;

/**
 * @author ChenHang
 */
public interface NewsService {
    /**
     * 查询新闻列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResultData listNews(int pageNum, int pageSize);

    /**
     * 审核新闻
     * @param body
     * @return
     */
    ResultData examinateNews(String body);

    /**
     * 删除新闻
     * @param id
     * @return
     */
    ResultData removeNews(int id);

    /**
     * 查询单个新闻
     * @param id
     * @return
     */
    ResultData getNews(int id);
}
