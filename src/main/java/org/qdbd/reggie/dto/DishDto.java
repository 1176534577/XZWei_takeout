package org.qdbd.reggie.dto;


import lombok.Data;
import org.qdbd.reggie.entity.Dish;
import org.qdbd.reggie.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
