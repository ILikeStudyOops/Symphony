package org.csq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.csq.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}