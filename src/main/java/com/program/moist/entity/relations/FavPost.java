package com.program.moist.entity.relations;

import lombok.Data;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe user's favorite posts
 */
@Data
public class FavPost {

    private Integer postId;
    private Integer userId;
}
