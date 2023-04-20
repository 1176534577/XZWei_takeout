package org.qdbd.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qdbd.reggie.entity.Dish;
import org.qdbd.reggie.entity.DishFlavor;
import org.qdbd.reggie.mapper.DishFlavorMapper;
import org.qdbd.reggie.mapper.DishMapper;
import org.qdbd.reggie.service.DishFlavorService;
import org.qdbd.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
