package com.program.moist.entity.infoEntities;

import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/3/18
 * Author: SilentSherlock
 * Description: describe the information kind
 */
@Data
public class Category {
    private Integer cate_id;
    private Integer parent_cate_id;//父类别的id，父类别id为0，说明是一级类别
    private String cate_name;
    private Date create_time;
    private Date update_time;
}
