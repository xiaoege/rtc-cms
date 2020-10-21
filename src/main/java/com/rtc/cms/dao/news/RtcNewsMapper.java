package com.rtc.cms.dao.news;

import com.rtc.cms.dto.NewsOperation;
import com.rtc.cms.dto.RtcNewsDTO;
import com.rtc.cms.entity.news.RtcNews;
import com.rtc.cms.vo.NewsDetailVO;

import java.util.List;

public interface RtcNewsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RtcNews record);

    int insertSelective(RtcNews record);

    RtcNews selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RtcNewsDTO rtcNewsDTO);

    int updateByPrimaryKeyWithBLOBs(RtcNews record);

    int updateByPrimaryKey(RtcNews record);

    int selectTest(int id);

    List<RtcNews> selectNewsList();

    int examinateNews(NewsOperation newsOperation);

    int deleteNews(int id);

    NewsDetailVO selectNews(int id);

    String selectUuid(int id);

    int deleteNewsDetail(String uuid);
}