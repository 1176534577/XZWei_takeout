package org.qdbd.reggie.dto;


import lombok.Data;
import org.qdbd.reggie.entity.Setmeal;
import org.qdbd.reggie.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
