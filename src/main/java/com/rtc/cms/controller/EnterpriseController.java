package com.rtc.cms.controller;

import com.rtc.cms.service.EnterpriseService;
import com.rtc.cms.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author ChenHang
 */
@Controller
@RequestMapping("enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @GetMapping("/initEnterprise")
    public String initEnterprise() {
        return "enterprise/enterprise";
    }

    /**
     * 企业查询-列表
     *
     * @param name
     * @param nation
     * @param area
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    @GetMapping("listEnterprise")
    @ResponseBody
    public ResultData listEnterprise(@RequestParam(name = "name", required = false, defaultValue = "") String name,
                                     @RequestParam(name = "nation", required = false, defaultValue = "") String nation,
                                     @RequestParam(name = "area", required = false, defaultValue = "") String area,
                                     @RequestParam(name = "start_date", required = false, defaultValue = "") String startDate,
                                     @RequestParam(name = "end_date", required = false, defaultValue = "") String endDate,
                                     @RequestParam(name = "page", required = false, defaultValue = "1") int pageNum,
                                     @RequestParam(name = "limit", required = false, defaultValue = "20") int pageSize) throws Exception {
        return enterpriseService.listEnterprise(name, nation, area, startDate, endDate, pageNum, pageSize);
    }

    @PostMapping("removeEnterprise")
    @ResponseBody
    public ResultData removeEnterprise(@RequestParam(name = "id") String id,
                                       @RequestParam(name = "esId") String esId,
                                       @RequestParam(name = "nation") String nation,
                                       @RequestParam(name = "eType") String eType) {
        return enterpriseService.removeEnterprise(id, esId, nation, eType);
    }

    @PostMapping("removeEnterpriseList")
    @ResponseBody
    public ResultData removeEnterpriseList(@RequestBody String ca) {
        return enterpriseService.removeEnterpriseList(ca);
    }


}
