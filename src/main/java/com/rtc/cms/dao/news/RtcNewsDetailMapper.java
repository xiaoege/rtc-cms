package com.rtc.cms.dao.news;

import com.rtc.cms.entity.news.RtcNewsDetail;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface RtcNewsDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RtcNewsDetail record);

    int insertSelective(RtcNewsDetail record);

    RtcNewsDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RtcNewsDetail record);

    int updateByPrimaryKeyWithBLOBs(RtcNewsDetail record);

    int updateByPrimaryKey(RtcNewsDetail record);

    int insertNewsContent(@Param("newsId")String newsId, @Param("contentList") ArrayList<String> contentList);
}