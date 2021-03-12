package com.program.moist.control;

import com.program.moist.service.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Date: 2021/3/12
 * Author: SilentSherlock
 * Description: accept user's or admin's request of Info, deal with the request,
 * operate the DB or return data with JSON type
 */
@RestController
@RequestMapping(value = "/info")
@Slf4j
public class InfoController {

    @Resource
    private InfoService infoService;


}
