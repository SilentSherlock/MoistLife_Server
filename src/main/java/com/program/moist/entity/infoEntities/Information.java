package com.program.moist.entity.infoEntities;

import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: the bean of Information, describe the main entity of the system, such as a local service, a second-hand merchandise etc.
 */
@Data
public class Information {

    private Integer infoId;
    private String infoTitle;
    private Float price;
    private Integer cateId;
    private String area;
    private String detail;
    private String infoPictures;
    private Integer userId;
    private Date infoTime;
    private Date updateTime;
}
