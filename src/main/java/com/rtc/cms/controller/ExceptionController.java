package com.rtc.cms.controller;

import com.rtc.cms.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenHang
 */
@ApiIgnore
@RestControllerAdvice
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(Exception e) {
        e.printStackTrace();
        String exceptionInfo = CommonUtils.getExceptionInfo(e);

        logger.debug(exceptionInfo);
        logger.info(exceptionInfo);

        Map map = new HashMap();
        map.put("data", null);
        map.put("message", "请求失败");
        map.put("code", 500);

        if (e instanceof MaxUploadSizeExceededException) {
            map.put("message", "文件大小超出限制");
        }
        return map;
    }


}
