package org.qdbd.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qdbd.reggie.dto.DishDto;
import org.qdbd.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品及其对应的菜品口味
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据Id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    DishDto getByIdwithFlavor(Long id);


    void updateWithFlavor(DishDto dishDto);
}
