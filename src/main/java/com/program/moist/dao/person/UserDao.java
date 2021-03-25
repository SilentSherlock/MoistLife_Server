package com.program.moist.dao.person;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.moist.entity.person.User;

import java.util.List;

/**
 * Date: 2021/3/1
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface UserDao extends BaseMapper<User> {
    List<User> getByIds(List<Integer> ids);
}
