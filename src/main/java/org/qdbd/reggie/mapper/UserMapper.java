package org.qdbd.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.qdbd.reggie.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
