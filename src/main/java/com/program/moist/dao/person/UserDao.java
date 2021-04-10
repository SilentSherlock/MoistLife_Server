package com.program.moist.dao.person;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.moist.entity.person.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Date: 2021/3/1
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface UserDao extends BaseMapper<User> {
    List<User> getByIds(List<Integer> ids);

    Integer checkByPhone(String phone);
    Integer checkByEmail(String email);
    Integer checkByIN(String IN);

    User getUser(Integer userId);
    Integer updateColumnById(@Param("name") String name, @Param("value") Object value, @Param("id") Integer userId);
}
