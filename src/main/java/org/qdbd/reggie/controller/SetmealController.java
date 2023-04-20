package org.qdbd.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.qdbd.reggie.common.R;
import org.qdbd.reggie.dto.SetmealDto;
import org.qdbd.reggie.entity.Category;
import org.qdbd.reggie.entity.Setmeal;
import org.qdbd.reggie.entity.SetmealDish;
import org.qdbd.reggie.service.CategoryService;
import org.qdbd.reggie.service.SetmealDishService;
import org.qdbd.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);

        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        // 降序排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, queryWrapper);
        // 以为前端需求，所以使用SetmealDto
        Page<SetmealDto> setmealDtoPage = new Page<>();
        // 对象拷贝
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        // 拿到records
        List<Setmeal> setmeals = setmealPage.getRecords();
        List<SetmealDto> list = setmeals.stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝
            BeanUtils.copyProperties(setmeal, setmealDto);
            // 分类Id
            Long categoryId = setmeal.getCategoryId();
            // 根据分类Id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                // 分类名称
                String categoryName = category.getName();
                // 赋分类名称值
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        // 赋records值
        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);

    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }



}
