package org.qdbd.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qdbd.reggie.common.CustomExpection;
import org.qdbd.reggie.entity.Category;
import org.qdbd.reggie.entity.Dish;
import org.qdbd.reggie.entity.Employee;
import org.qdbd.reggie.entity.Setmeal;
import org.qdbd.reggie.mapper.CategoryMapper;
import org.qdbd.reggie.mapper.EmployeeMapper;
import org.qdbd.reggie.service.CategoryService;
import org.qdbd.reggie.service.DishService;
import org.qdbd.reggie.service.EmployeeService;
import org.qdbd.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除前判断是否关联
     * @param id
     * @return
     */
    @Override
    public void removeById(Long id) {
        // 判断分类是否关联菜品，如果关联，抛出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1>0){
            throw new CustomExpection("当前分类关联菜品，不能删除");
        }
        // 判断分类是否关联套餐，如果关联，抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomExpection("当前分类关联套餐，不能删除");
        }
        // 正常删除分类
        super.removeById(id);
    }
}
