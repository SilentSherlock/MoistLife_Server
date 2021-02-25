package com.program.moist.entity.relations;

import lombok.Data;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe user's thumb up posts
 */
@Data
public class ThumbUpPost {

    private int post_id;
    private int user_id;
}
