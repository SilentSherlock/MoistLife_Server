package com.program.moist.entity.infoEntities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/3/22
 * Author: SilentSherlock
 * Description: describe post' topic, as a container
 */
@Data
public class Topic {

    @TableId(type = IdType.AUTO)
    private Integer topicId;
    private Integer parentTopicId;
    private String topicName;
    private Date createTime;
    private Date updateTime;
}
