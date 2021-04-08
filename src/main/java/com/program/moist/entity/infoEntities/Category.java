package com.program.moist.entity.infoEntities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/3/18
 * Author: SilentSherlock
 * Description: describe the information kind
 */
@Data
public class Category {
    @TableId(type = IdType.AUTO)
    private Integer cateId;
    private Integer parentCateId;//父类别的id，父类别id为0，说明是一级类别
    private String cateName;
    private Date createTime;
    private Date updateTime;
}
