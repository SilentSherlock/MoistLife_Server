package com.program.moist.dao.infoEntities;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.moist.entity.infoEntities.Topic;

import java.util.List;

/**
 * Date: 2021/3/22
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface TopicDao extends BaseMapper<Topic> {
    List<Topic> getAll();
    List<Topic> getByIds(List<Integer> ids);
}
