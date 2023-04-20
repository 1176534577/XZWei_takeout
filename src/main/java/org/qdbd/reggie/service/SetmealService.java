package org.qdbd.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qdbd.reggie.dto.SetmealDto;
import org.qdbd.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐和套餐包含的菜品
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐setmeal，同时删除和套餐大关联菜品数据setmealDish
     * @param ids
     */
    void removeWithDish(List<Long> ids);
}
