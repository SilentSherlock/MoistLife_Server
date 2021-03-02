package com.program.moist.dao.infoEntities;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.moist.entity.infoEntities.Information;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface InformationDao extends BaseMapper<Information> {

    Information getById(Integer infoId);
    List<Information> getByKind(String kind);
    List<Information> getByArea(String area);
    List<Information> getByUserId(Integer userId);
    int add(Information information);
    int delete(Integer infoId);//for admin
    int deleteByUserId(@Param("info_id") Integer infoId, @Param("user_id") Integer userId);//for user
    int update(Information information);
    List<Information> getByIds(List<Integer> ids);
}
