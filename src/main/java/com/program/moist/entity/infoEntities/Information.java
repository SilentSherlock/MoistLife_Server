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

    private int info_id;
    private String info_title;
    private float price;
    private String kind;
    private String area;
    private String detail;
    private String info_pictures;
    private int user_id;
    private Date info_time;
}
