package org.qdbd.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qdbd.reggie.entity.Category;
import org.qdbd.reggie.entity.Employee;

import java.io.Serializable;

public interface CategoryService extends IService<Category> {
    void removeById(Long id);

}
