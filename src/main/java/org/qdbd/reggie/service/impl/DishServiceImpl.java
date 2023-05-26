package org.qdbd.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qdbd.reggie.dto.DishDto;
import org.qdbd.reggie.entity.Dish;
import org.qdbd.reggie.entity.DishFlavor;
import org.qdbd.reggie.mapper.DishMapper;
import org.qdbd.reggie.service.DishFlavorService;
import org.qdbd.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 在 @Transactional 注解中如果不配置rollbackFor属性,那么事务只会在遇到RuntimeException的时候才会回滚，加上 rollbackFor=Exception.class,可以让事务在遇到非运行时异常时也回滚。
     * @param dishDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithFlavor(DishDto dishDto) {
        // 保存基本信息
        this.save(dishDto);
        Long dishId = dishDto.getId();
        // 保存菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        /* flavors = flavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishId);
            return dishFlavor;
        }).collect(Collectors.toList()); */

        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据Id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdwithFlavor(Long id) {

        // 查询菜品信息
        Dish dish = this.getById(id);
        // 条件构造器
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        queryWrapper.eq(DishFlavor::getDishId, id);
        // 查找口味信息
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        // 拷贝信息
        BeanUtils.copyProperties(dish, dishDto);
        // 单独赋口味值
        dishDto.setFlavors(dishFlavors);
        // 返回
        return dishDto;
    }

    /**
     * 在 @Transactional 注解中如果不配置rollbackFor属性,那么事务只会在遇到RuntimeException的时候才会回滚，加上 rollbackFor=Exception.class,可以让事务在遇到非运行时异常时也回滚。
     * @param dishDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithFlavor(DishDto dishDto) {
        // 更新菜品
        this.updateById(dishDto);
        // 清理口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 更新口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 使用流
        flavors = flavors.stream().map(flavor -> {
            flavor.setDishId(dishDto.getId());
            return flavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }
}
