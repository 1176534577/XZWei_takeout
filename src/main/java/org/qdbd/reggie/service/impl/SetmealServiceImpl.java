package org.qdbd.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qdbd.reggie.common.CustomExpection;
import org.qdbd.reggie.dto.SetmealDto;
import org.qdbd.reggie.entity.Dish;
import org.qdbd.reggie.entity.Setmeal;
import org.qdbd.reggie.entity.SetmealDish;
import org.qdbd.reggie.mapper.DishMapper;
import org.qdbd.reggie.mapper.SetmealMapper;
import org.qdbd.reggie.service.DishService;
import org.qdbd.reggie.service.SetmealDishService;
import org.qdbd.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐和套餐包含的菜品
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐基本信息
        this.save(setmealDto);
        // 保存套餐菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        // 浅拷贝
        setmealDishes.stream().peek(setmealDish -> setmealDish.setSetmealId(setmealDto.getId())).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐setmeal，同时删除和套餐大关联菜品数据setmealDish
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 判断是否停售
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        // 不能删除，抛出业务异常
        if (count > 0) {
            throw new CustomExpection("套餐正在售卖中，不能删除");
        }

        // 可以删除，先删除套餐setmeal中数据
        this.removeByIds(ids);
        // 再删除setmeal_dish中的对应数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper1);

    }
}
