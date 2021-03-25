package com.program.moist.dao.relations;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.moist.entity.infoEntities.Category;
import com.program.moist.entity.relations.TopicSub;

import java.util.List;

/**
 * Date: 2021/3/22
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface TopicSubDao extends BaseMapper<TopicSub> {
    List<TopicSub> getAll();
}
