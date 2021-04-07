package com.program.moist.entity.relations;

import lombok.Data;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe user's following and followed
 */
@Data
public class Follow {
    private Integer fromUserId;
    private Integer toUserId;
}
