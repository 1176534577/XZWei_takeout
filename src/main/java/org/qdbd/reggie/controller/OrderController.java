package org.qdbd.reggie.controller;

import org.qdbd.reggie.common.R;
import org.qdbd.reggie.entity.Orders;
import org.qdbd.reggie.service.AddressBookService;
import org.qdbd.reggie.service.OrderDetailService;
import org.qdbd.reggie.service.OrderService;
import org.qdbd.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {

        ordersService.submit(orders);
        return R.success("下单成功");

    }


}
