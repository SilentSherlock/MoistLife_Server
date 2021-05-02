package com.program.moist.dao.infoEntities;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.moist.entity.infoEntities.Category;

import java.util.List;

/**
 * Date: 2021/3/18
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface CategoryDao extends BaseMapper<Category> {
    List<Category> getAll();
    List<Category> getTopCate();
}
