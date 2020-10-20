package com.rtc.cms.dto;

/**
 * @author ChenHang
 */
public class NewsOperation {
    private Integer[] id;
    private String operation;

    public Integer[] getId() {
        return id;
    }

    public void setId(Integer[] id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
