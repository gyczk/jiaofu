package com.ryqg.jiaofu.business.common;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class BaseModel implements Serializable {

    private String id;

   private Date createTime;

   private Date updateTime;



    public void initNew() {
        if (StringUtils.isBlank(this.getId())) {
            this.setId(UUID.randomUUID().toString());
        }
    }

    public void reInit() {
        this.setId(UUID.randomUUID().toString());
    }

}
