package com.program.moist.entity.infoEntities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.program.moist.utils.TokenUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = TokenUtil.DATE_FORMAT)
    private Date comTime;
    private Integer parentComId;
    private Integer comKind;
}
