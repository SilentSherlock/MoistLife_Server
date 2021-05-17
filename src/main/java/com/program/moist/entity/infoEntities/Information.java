package com.program.moist.entity.infoEntities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.program.moist.utils.TokenUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: the bean of Information, describe the main entity of the system, such as a local service, a second-hand merchandise etc.
 */
@Data
public class Information {

    @TableId(type = IdType.AUTO)
    private Integer infoId;
    private String infoTitle;
    private Float price;
    private Integer cateId;
    private String area;
    private String detail;
    private String infoPictures;
    private Integer userId;
    @DateTimeFormat(pattern = TokenUtil.DATE_FORMAT)
    private Date infoTime;
    @DateTimeFormat(pattern = TokenUtil.DATE_FORMAT)
    private Date updateTime;
    private Integer infoState;//0--正在寻找 1--已经结束
}
