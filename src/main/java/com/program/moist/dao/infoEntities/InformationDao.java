package com.program.moist.dao.infoEntities;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    int delete(Integer infoId);//for admin
    int deleteByUserId(@Param("info_id") Integer infoId, @Param("user_id") Integer userId);//for user
    int getStatusCountByCateId(@Param("cateId") Integer cateId, @Param("infoState") Integer infoState);
    List<Information> getByIds(List<Integer> ids);
    IPage<Information> getByPage(Page<Information> page, @Param("name") String columnName, @Param("value") String columnValue);
    IPage<Information> getByPage(Page<Information> page, @Param("name") String columnName, @Param("value") Integer columnValue);
    List<Information> getAll();
}
