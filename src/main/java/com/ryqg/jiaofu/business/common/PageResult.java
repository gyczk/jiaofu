package com.ryqg.jiaofu.business.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    private long pageNo;

    private long pageSize;

    private long total;
    
    private long totalPage;

    private List<T> data ;

    public long getTotalPage() {
        if (pageSize == 0) {
            return 0L;
        }
        return total % pageSize == 0 ? (total / pageSize) : (total / pageSize + 1);
    }
}
