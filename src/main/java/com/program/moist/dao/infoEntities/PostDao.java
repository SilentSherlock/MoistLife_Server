package com.program.moist.dao.infoEntities;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.moist.entity.infoEntities.Post;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.List;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface PostDao extends BaseMapper<Post> {
    List<Post> getByIds(List<Integer> ids);
    IPage<Post> getByPage(Page<Post> page, @Param("name") String name, @Param("value") Object value);
}
