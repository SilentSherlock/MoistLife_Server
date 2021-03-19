package com.program.moist.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.moist.dao.infoEntities.CategoryDao;
import com.program.moist.dao.infoEntities.InformationDao;
import com.program.moist.dao.relations.FavInfoDao;
import com.program.moist.entity.infoEntities.Category;
import com.program.moist.entity.infoEntities.Information;
import com.program.moist.entity.relations.FavInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2021/3/2
 * Author: SilentSherlock
 * Description: supply information operations
 */
@Service
@Slf4j
public class InfoService {

    //region class params
    @Resource
    private InformationDao informationDao;
    @Resource
    private FavInfoDao favInfoDao;
    @Resource
    private CategoryDao categoryDao;

    private static final String TAG = "InfoService-";
    //endregion

    //region info crud
    /**
     * add a information
     * */
    public void addInfo(Information information) {
        String name = "addInfo-";
        log.info(TAG + name);
        int res =  informationDao.insert(information);
        if (res == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + res + " insert");
    }

    /**
     * update a information by information id
     * */
    public void updateInfo(Information information) {
        String name = "updateInfo-";
        log.info(TAG + name);
        int result = informationDao.updateById(information);
        if (result == 0) log.info(TAG + name + "no such info");
        else log.info(TAG + name + result + " rows updated");
    }

    /**
     * delete information by params, for example, key = "info_id", value = "2",
     * or key = "user_id", value = "5", etc.
     * */
    public void deleteInfoByMap(Map<String, Object> map) {
        String name = "delete by map-";
        log.info(TAG + name);
        int result =  informationDao.deleteByMap(map);
        if (result == 0) log.info(TAG + name + "no row effected");
        else log.info(TAG + name + result + " rows effected");
    }

    /**
     * get information by params, selector could define the key and its value,
     * it will used in SQL statements conditions as ${key} = #{value}
     * */
    public List<Information> getInfoByMap(Map<String, Object> map) {
        String name = "get By map-";
        log.info(TAG + name);
        return informationDao.selectByMap(map);
    }
    //endregion

    //region favInfo crud

    /**
     * user add a fav info
     * */
    public void addFavInfo(FavInfo favInfo) {
        String name = "addFavInfo-";
        log.info(TAG + name);
        int result = favInfoDao.insert(favInfo);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + result + " rows insert");
    }

    /**
     * delete a favInfo by map, in fact, the key should be info_id
     * */
    public void deleteFavInfo(Integer infoId, Integer userId) {
        String name = "deleteFavInfo-";
        log.info(TAG + name);

        Map<String, Object> params = new HashMap<>();
        params.put("info_id", infoId);
        params.put("user_id", userId);
        int result = favInfoDao.deleteByMap(params);
        if (result == 0) log.info(TAG + name + "no row effected");
        else log.info(TAG + name + result + " rows effected");
    }
    //endregion

    //region mixed methods
    /**
     * get user's favorite Infos
     * */
    public List<Information> getUserFavInfo(Integer userId) {
        String name = "get user fav info-";
        log.info(TAG + name);

        Map<String, Object> param = new HashMap<>();
        param.put("user_id", userId);
        List<FavInfo> favInfos = favInfoDao.selectByMap(param);

        List<Integer> ids = new LinkedList<>();
        for (FavInfo cur :
                favInfos) {
            ids.add(cur.getInfo_id());
        }

        return informationDao.getByIds(ids);
    }
    //endregion

    //region category crud

    /**
     *
     * @param category
     */
    public void addCategory(Category category) {
        int result = categoryDao.insert(category);
        if (result == 0) {
            log.warn(TAG + "add category failed");
        }
    }

    /**
     *
     * @return
     */
    public List<Category> getAllCategory() {
        try {
            return categoryDao.getAll();
        } catch (Exception e) {
            log.error(TAG + "get all category failed", e);
            return null;
        }
    }

    /**
     *
     * @param params
     * @return
     */
    public List<Category> getCateByMap(Map<String, Object> params) {
        try {
            return categoryDao.selectByMap(params);
        } catch (Exception e) {
            log.error(TAG + "get Category by map wrong", e);
            return null;
        }
    }

    /**
     *
     * @param category
     */
    public void updateCategory(Category category) {
        try {
            int result = categoryDao.updateById(category);
            if (result == 0) log.warn(TAG + "update category, no rows effected");
        } catch (Exception e) {
            log.error(TAG + "update category failed", e);
        }
    }
    //endregion
}
