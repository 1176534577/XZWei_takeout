package org.qdbd.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qdbd.reggie.entity.OrderDetail;
import org.qdbd.reggie.mapper.OrderDetailMapper;
import org.qdbd.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
