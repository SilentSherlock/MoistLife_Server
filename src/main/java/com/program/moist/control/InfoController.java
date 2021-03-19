package com.program.moist.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.program.moist.entity.infoEntities.Category;
import com.program.moist.entity.infoEntities.Information;
import com.program.moist.entity.person.Admin;
import com.program.moist.service.InfoService;
import com.program.moist.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/browse/getDefaultInfo")
    @ResponseBody
    public Result getDefaultInfo() {
        Result result = new Result();
        result.setStatus(Status.SUCCESS);

        List<Information> list;
        if (RedisUtil.hasKey(TokenUtil.DEFAULT_INFO)) {
            list = JsonUtil.string2Obj(RedisUtil.getCommon(TokenUtil.LOGIN_TOKEN), List.class, Information.class);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("area", "北京");
            list = infoService.getInfoByMap(params);
            RedisUtil.setCommon(TokenUtil.DEFAULT_INFO, JsonUtil.obj2String(list));
        }

        result.getResultMap().put(TokenUtil.DEFAULT_INFO, list);
        return result;
    }

    @RequestMapping("/browse/getAllCate")
    @ResponseBody
    public Result getAllCate() {
        Result result = new Result();
        result.setStatus(Status.SUCCESS);

        List<Category> categories;
        if (RedisUtil.hasKey(TokenUtil.CATEGORY)) {
            categories = JsonUtil.string2Obj(RedisUtil.getCommon(TokenUtil.CATEGORY), List.class, Category.class);
        } else {
            categories = infoService.getAllCategory();
            RedisUtil.setCommon(TokenUtil.CATEGORY, JsonUtil.obj2String(categories));
        }

        result.getResultMap().put(TokenUtil.CATEGORY, categories);
        return result;
    }

    @RequestMapping("/browse/getInfoByCate")
    @ResponseBody
    public Result getInfoByKind(HttpServletRequest request) {
        Result result = new Result();
        result.setStatus(Status.SUCCESS);

        Integer cate_id = Integer.parseInt(request.getParameter("cate_id"));
        List<Information> list;
        String key = TokenUtil.CATEGORY + cate_id;
        if (RedisUtil.hasKey(key)) {
            list = JsonUtil.string2Obj(RedisUtil.getCommon(key), List.class, ProcessHandle.Info.class);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("cate_id", cate_id);
            list = infoService.getInfoByMap(params);

            RedisUtil.setCommon(key, JsonUtil.obj2String(list));
        }

        result.getResultMap().put(key, list);
        return result;
    }


}
