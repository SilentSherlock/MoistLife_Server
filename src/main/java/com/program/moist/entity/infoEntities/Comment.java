package com.program.moist.entity.infoEntities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe user's comment
 */
@Data
public class Comment {

    @TableId(type = IdType.AUTO)
    private Integer com_id;
    private Integer from_user_id;
    private Integer to_user_id;
    private Integer post_id;
    private String content;
    private Date com_time;
    private Integer parent_com_id;
    private Integer com_kind;//default 0, deleted 1
}
