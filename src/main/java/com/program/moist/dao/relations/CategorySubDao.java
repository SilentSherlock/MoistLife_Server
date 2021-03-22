package com.program.moist.dao.relations;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.moist.entity.relations.CategorySub;

import java.util.List;

/**
 * Date: 2021/3/22
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface CategorySubDao extends BaseMapper<CategorySub> {
    List<CategorySub> getAll();
}
