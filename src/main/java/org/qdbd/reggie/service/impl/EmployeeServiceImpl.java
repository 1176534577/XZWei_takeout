package org.qdbd.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qdbd.reggie.entity.Employee;
import org.qdbd.reggie.mapper.EmployeeMapper;
import org.qdbd.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
