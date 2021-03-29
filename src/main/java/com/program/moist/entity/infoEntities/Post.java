package com.program.moist.entity.infoEntities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: the bean of post, describe the post in the community
 */
@Data
@TableName(value = "post")
public class Post {

    @TableId(type = IdType.AUTO)
    private Integer post_id;
    private String post_title;
    private Integer user_id;
    private Integer topic_id;
    private String detail;
    private String location;
    private Date post_time;
    private String post_pictures;
    private Integer post_state;
}
