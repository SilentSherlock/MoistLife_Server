package com.program.moist.entity.infoEntities;

import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe user's comment
 */
@Data
public class Comment {

    private int com_id;
    private int from_user_id;
    private int to_user_id;
    private int post_id;
    private String content;
    private Date com_time;
    private int parent_com_id;
}
