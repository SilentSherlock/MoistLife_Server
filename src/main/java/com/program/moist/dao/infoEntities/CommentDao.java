package com.program.moist.dao.infoEntities;

import com.program.moist.entity.infoEntities.Comment;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface CommentDao {

    Comment getByComId(int comId);

}
