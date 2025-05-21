package com.ryqg.jiaofu.business.common;

import cn.hutool.db.sql.Order;
import lombok.Data;

import java.io.Serializable;

@Data
public class Page implements Serializable {
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 页码，0表示第一页
     */
    private int pageNumber;
    /**
     * 每页结果数
     */
    private int pageSize;
    /**
     * 排序
     */
    private Order[] orders;
}
