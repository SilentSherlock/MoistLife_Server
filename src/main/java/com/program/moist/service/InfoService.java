package com.program.moist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.moist.dao.infoEntities.CategoryDao;
import com.program.moist.dao.infoEntities.InformationDao;
import com.program.moist.dao.relations.CategorySubDao;
import com.program.moist.dao.relations.FavInfoDao;
import com.program.moist.entity.infoEntities.Category;
import com.program.moist.entity.infoEntities.Information;
import com.program.moist.entity.relations.CategorySub;
import com.program.moist.entity.relations.FavInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    private CategorySubDao categorySubDao;
    @Resource
    private CategoryDao categoryDao;

    private static final String TAG = "InfoService-";
    //endregion

    //region categorySub crud

    /**
     *
     * @param categorySub
     */
    public void addCategorySub(CategorySub categorySub) {
        String name = "addCategorySub-";
        log.info(TAG + name);
        int res = categorySubDao.insert(categorySub);
        if (res == 0) log.info(TAG + name + " failed");
        else log.info(TAG + name + res + " inserted");
    }

    /**
     *
     * @param params
     */
    public void deleteCategorySubByMap(Map<String, Object> params) {
        String name = "deleteCategorySub-";
        log.info(TAG + name);
        int res = categorySubDao.deleteByMap(params);
        if (res == 0) log.info(TAG + name + " failed");
        else log.info(TAG + name + res + " deleted");
    }

    //endregion

    //region info crud

    /**
     * 分页查询信息
     * @param index 查询页码
     * @param size 一页信息数量
     * @param name 查询列名
     * @param value 查询列值
     * @return
     */
    public List<Information> getInfoByPage(Integer index, Integer size, String name, String value) {
        Page<Information> page = new Page<>(index, size);
        String method = "getInfoByPage-";
        log.info(TAG + method);
        IPage<Information> result;
        if (name.endsWith("id")) {
            result = informationDao.getByPage(page, name, Integer.parseInt(value));
        } else result = informationDao.getByPage(page, name, value);
        if (result == null) log.warn(TAG + method + "get nothing");
        return result.getRecords();
    }
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
            ids.add(cur.getInfoId());
        }

        return informationDao.getByIds(ids);
    }

    /**
     * 获得订阅数前k的category
     * @param k 默认取前十,category少于10取全部
     * @return
     */
    public List<Category> getTopKCategory(Integer k) {
        if (k == null) k = 10;
        List<CategorySub> list = categorySubDao.getAll();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (CategorySub categorySub : list) {
            Integer count = map.getOrDefault(categorySub.getCateId(), 0);
            map.put(categorySub.getCateId(), ++count);
        }

        Set<Map.Entry<Integer, Integer>> sets = map.entrySet();
        PriorityQueue<Map.Entry<Integer, Integer>> queue = new PriorityQueue<>(sets.size(), Comparator.comparingInt(Map.Entry::getValue));
        queue.addAll(sets);

        List<Category> result = new LinkedList<>();
        k = Math.min(queue.size(), k);
        for (int i = 0; i < k; i++) {
            Map<String, Object> params = new HashMap<>();
            params.put("cate_id", Objects.requireNonNull(queue.poll()).getKey());
            result.addAll(categoryDao.selectByMap(params));
        }

        return result;
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
    public List<Category> getTopCategory() {
        try {
            return categoryDao.getTopCate();
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
