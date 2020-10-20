package com.rtc.cms.dao.news;

import com.rtc.cms.entity.news.RtcNewsDetail;

public interface RtcNewsDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RtcNewsDetail record);

    int insertSelective(RtcNewsDetail record);

    RtcNewsDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RtcNewsDetail record);

    int updateByPrimaryKeyWithBLOBs(RtcNewsDetail record);

    int updateByPrimaryKey(RtcNewsDetail record);
}