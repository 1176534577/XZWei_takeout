package org.qdbd.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.qdbd.reggie.entity.Dish;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}