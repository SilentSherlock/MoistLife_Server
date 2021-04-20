package com.program.moist.control;

import com.program.moist.entity.infoEntities.Category;
import com.program.moist.entity.infoEntities.Information;
import com.program.moist.entity.relations.FavInfo;
import com.program.moist.service.FileService;
import com.program.moist.service.InfoService;
import com.program.moist.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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
    @Resource
    private FileService fileService;

    @RequestMapping("/browse/getDefaultInfo")
    public Result getDefaultInfo() {
        List<Information> list;
        if (RedisUtil.hasKey(TokenUtil.DEFAULT_INFO)) {
            list = JsonUtil.string2Obj(RedisUtil.getCommon(TokenUtil.DEFAULT_INFO), List.class, Information.class);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("area", "paris");
            list = infoService.getInfoByMap(params);
            RedisUtil.setCommon(TokenUtil.DEFAULT_INFO, JsonUtil.obj2String(list));
        }

        Result result = Result.createBySuccess();
        result.getResultMap().put(TokenUtil.DEFAULT_INFO, list);
        return result;
    }

    @RequestMapping("/browse/getAllCate")
    public Result getAllCate() {
        List<Category> categories;
        if (RedisUtil.hasKey(TokenUtil.CATEGORY)) {
            categories = JsonUtil.string2Obj(RedisUtil.getCommon(TokenUtil.CATEGORY), List.class, Category.class);
        } else {
            categories = infoService.getAllCategory();
            RedisUtil.setCommon(TokenUtil.CATEGORY, JsonUtil.obj2String(categories));
        }

        Result result = Result.createBySuccess();
        result.getResultMap().put(TokenUtil.CATEGORY, categories);
        return result;
    }

    @RequestMapping("/browse/getInfoByCate")
    public Result getInfoByKind(Integer cateId) {
        List<Information> list;
        String key = TokenUtil.CATEGORY + cateId;
        if (RedisUtil.hasKey(key)) {
            list = JsonUtil.string2Obj(RedisUtil.getCommon(key), List.class, ProcessHandle.Info.class);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("cate_id", cateId);
            list = infoService.getInfoByMap(params);
            RedisUtil.setCommon(key, JsonUtil.obj2String(list));
        }

        Result result = Result.createBySuccess();
        result.getResultMap().put(key, list);
        return result;
    }

    @RequestMapping("/browse/getInfoByArea")
    public Result getInfoByArea(String area) {

        if (area == null || "".equals(area)) {
            return Result.createByWrongRequest("need parameter area");
        }

        List<Information> list;
        if (RedisUtil.hasKey(TokenUtil.AREA, area)) {
            list = JsonUtil.string2Obj((String) RedisUtil.getMapValue(TokenUtil.AREA, area), List.class, Information.class);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("area", area);
            list = infoService.getInfoByMap(params);
            RedisUtil.putMapValue(TokenUtil.AREA, area, JsonUtil.obj2String(list));
        }

        Result result = Result.createBySuccess();
        result.getResultMap().put(area, list);
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
            result.getResultMap().put(TokenUtil.INFOS, list);
        }

        return result;
    }

    /**
     * 分页获取
     * @param index 当前页面,固定10条数据
     * @param name
     * @param value
     * @return
     */
    @RequestMapping("/getInfoByPage")
    public Result getInfoByPage(Integer index, String name, String value) {
        Result result = new Result();
        List<Information> cur = infoService.getInfoByPage(index, 10, name, value);

        result.getResultMap().put(TokenUtil.INFOS, cur);
        if (cur == null || cur.size() == 0) {
            result.setStatus(Status.DEFAULT);
            result.setDescription("数据为空");
        } else {
            result.setStatus(Status.SUCCESS);
        }

        return result;
    }
    @RequestMapping("/addInfo")
    public Result addInfo(Information information) {
        Result result = new Result();
        //System.out.println(information.toString());
        infoService.addInfo(information);
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

        infoService.deleteFavInfo(favInfo.getInfoId(), favInfo.getUserId());
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/uploadInfoImage")
    public Result uploadInfoImage(@RequestParam("infoImage")MultipartFile[] multipartFiles, Integer infoId, Integer userId) {
        Result result = new Result();
        String path = userId + "/infoImage/" + infoId + "/";
        List<String> paths = fileService.upload(multipartFiles, path);
        if (paths == null || paths.size() == 0) {
            result.setStatus(Status.FALSE);
        } else {
            result.setStatus(Status.SUCCESS);
        }
        result.getResultMap().put(TokenUtil.PATHS, paths);
        return result;
    }
}