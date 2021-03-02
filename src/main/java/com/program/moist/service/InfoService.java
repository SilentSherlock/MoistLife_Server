package com.program.moist.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.moist.dao.infoEntities.InformationDao;
import com.program.moist.dao.relations.FavInfoDao;
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

    @Resource
    private InformationDao informationDao;
    @Resource
    private FavInfoDao favInfoDao;

    private static final String TAG = "InfoService-";
    public void addInfo(Information information) {
        String name = "addInfo-";
        log.info(TAG + name);
        int res =  informationDao.add(information);
        if (res == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + "success");
    }

    public void updateInfo(Information information) {
        String name = "updateInfo-";
        log.info(TAG + name);
        int result = informationDao.updateById(information);
        if (result == 0) log.info(TAG + name + "no such info");
        else log.info(TAG + name + "success");
    }

    public void deleteByMap(Map<String, Object> map) {
        String name = "delete by map-";
        log.info(TAG + name);
        int result =  informationDao.deleteByMap(map);
        if (result == 0) log.info(TAG + name + "no row effected");
        else log.info(TAG + name + "success");
    }

    public List<Information> getByMap(Map<String, Object> map) {
        String name = "get By map-";
        log.info(TAG + name);
        return informationDao.selectByMap(map);
    }

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
}