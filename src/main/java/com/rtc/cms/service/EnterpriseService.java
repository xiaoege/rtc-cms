package com.rtc.cms.service;

import com.rtc.cms.vo.ResultData;

/**
 * @author ChenHang
 */
public interface EnterpriseService {
    /**
     * 企业列表查询
     * @param name
     * @param nation
     * @param area
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResultData listEnterprise(String name, String nation, String area, String startDate, String endDate, int pageNum, int pageSize) throws Exception;

    ResultData removeEnterprise(String id, String esId, String nation, String eType);

    /**
     * 根据地区找到mysql里对应的表
     * @param eType 地区
     * @return mysql里对应的表
     */
    String getTable(String eType);

    /**
     * 批量删除公司
     * @param ca
     * @return
     */
    ResultData removeEnterpriseList(String ca);
}
