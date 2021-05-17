package com.program.moist.entity.infoEntities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.program.moist.utils.TokenUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = TokenUtil.DATE_FORMAT)
    private Date createTime;
    @DateTimeFormat(pattern = TokenUtil.DATE_FORMAT)
    private Date updateTime;
}
