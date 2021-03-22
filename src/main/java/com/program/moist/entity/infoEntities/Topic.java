package com.program.moist.entity.infoEntities;

import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/3/22
 * Author: SilentSherlock
 * Description: describe post' topic, as a container
 */
@Data
public class Topic {
    private Integer topic_id;
    private Integer parent_topic_id;
    private String topic_name;
    private Date create_time;
    private Date update_time;
}
