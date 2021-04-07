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
    private Integer comId;
    private Integer fromUserId;
    private Integer toUserId;
    private Integer postId;
    private String content;
    private Date comTime;
    private Integer parentComId;
    private Integer comKind;
}
