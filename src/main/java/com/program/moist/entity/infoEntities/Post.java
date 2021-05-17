package com.program.moist.entity.infoEntities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.program.moist.utils.TokenUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    private Integer postId;
    private String postTitle;
    private Integer userId;
    private Integer topicId;
    private String detail;
    private String location;
    @DateTimeFormat(pattern = TokenUtil.DATE_FORMAT)
    private Date postTime;
    private String postPictures;
    private Integer postState;
}
