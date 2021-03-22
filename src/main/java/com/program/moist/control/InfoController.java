package com.program.moist.control;

import com.program.moist.entity.infoEntities.Category;
import com.program.moist.entity.infoEntities.Information;
import com.program.moist.entity.relations.FavInfo;
import com.program.moist.service.InfoService;
import com.program.moist.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public Result getInfoByKind(HttpServletRequest request) {
        Result result = new Result();

        String value = request.getParameter("cate_id");
        if (value == null || "".equals(value)) {
            result.setStatus(Status.WRONG_REQUEST);
            result.setDescription("need parameter cate_id");
            return result;
        }

        Integer cate_id = null;
        try {
            cate_id = Integer.parseInt(value);
        } catch (Exception e) {
            log.error("value is not num string", e);
            result.setStatus(Status.WRONG_REQUEST);
            result.setDescription("value is not num string");
            return result;
        }

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

    @RequestMapping("/browse/getInfoByArea")
    public Result getInfoByArea(HttpServletRequest request) {
        Result result = new Result();

        String hashKey = request.getParameter("area");
        if (hashKey == null || "".equals(hashKey)) {
            result.setStatus(Status.WRONG_REQUEST);
            result.setDescription("need parameter area");
            return result;
        }

        List<Information> list;
        if (RedisUtil.hasKey(TokenUtil.AREA, hashKey)) {
            list = JsonUtil.string2Obj((String) RedisUtil.getMapValue(TokenUtil.AREA, hashKey), List.class, Information.class);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("area", hashKey);
            list = infoService.getInfoByMap(params);
            RedisUtil.putMapValue(TokenUtil.AREA, hashKey, JsonUtil.obj2String(list));
        }

        result.setStatus(Status.SUCCESS);
        result.getResultMap().put(hashKey, list);
        return result;
    }

    @RequestMapping("/browse/getInfoByUserId")
    public Result getInfoByUserId(Integer userId) {
        Result result = new Result();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        List<Information> list = infoService.getInfoByMap(params);
        if (list == null) {
            result.setStatus(Status.WRONG_REQUEST);
            result.setDescription("userId maybe wrong");
        } else {
            result.setStatus(Status.SUCCESS);
            result.setDescription("success");
            result.getResultMap().put("info_list", list);
        }

        return result;
    }

    @RequestMapping("/addInfo")
    public Result addInfo(Information information) {
        Result result = new Result();
        System.out.println(information.toString());
        //infoService.addInfo(information);
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/updateInfo")
    public Result updateInfo(Information information) {
        Result result = new Result();
        infoService.updateInfo(information);
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/deleteInfo")
    public Result deleteInfo(Integer infoId) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("info_id", infoId);
        infoService.deleteInfoByMap(params);

        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/addUserFavInfo")
    public Result addUserFavInfo(FavInfo favInfo) {
        Result result = new Result();
        infoService.addFavInfo(favInfo);
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/getUserFavInfo")
    public Result getUserFavInfo(Integer userId) {
        Result result = new Result();

        List<Information> list = infoService.getUserFavInfo(userId);
        result.setStatus(Status.SUCCESS);
        result.getResultMap().put("fav_info", list);

        return result;
    }

    @RequestMapping("/deleteFavInfo")
    public Result deleteFavInfo(FavInfo favInfo) {
        Result result = new Result();

        infoService.deleteFavInfo(favInfo.getInfo_id(), favInfo.getUser_id());
        result.setStatus(Status.SUCCESS);
        return result;
    }

}