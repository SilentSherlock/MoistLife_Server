package com.program.moist.dao.infoEntities;

import com.program.moist.entity.infoEntities.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe the class features
 */
public interface CommentDao {

    Comment getByComId(Integer comId);
    List<Comment> getByPostId(Integer postId);
    List<Comment> getByParentId(Integer parentComId);
    List<Comment> getAll();
    int add(Comment comment);
    int deleteByPostId(Integer postId);
    int updateComKindByComId(@Param("com_id") Integer comId, @Param("com_kind") Integer comKind);
}
