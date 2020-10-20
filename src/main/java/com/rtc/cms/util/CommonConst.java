package com.rtc.cms.util;

/**
 * @author ChenHang
 */
public class CommonConst {
    /**
     * 在用
     */
    public static final Integer STATUS_USED = 1;

    /**
     * 弃用
     */
    public static final Integer STATUS_NOT_USED = 0;

    /**
     * 评论-未审核
     */
    public static final Integer STATUS_COMMENT_NOT_EXAMINED = 0;

    /**
     * 评论-审核通过
     */
    public static final Integer STATUS_COMMENT_EXAMINED = 1;

    /**
     * 评论-审核不通过
     */
    public static final Integer STATUS_COMMENT_DENY = 2;
}
