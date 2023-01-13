package org.csq.controller;

import org.csq.entity.Order;
import org.csq.entity.Result;
import org.csq.entity.User;
import org.csq.feign.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {

    private UserClient userClient;

    @Autowired
    public OrderController(UserClient userClient){
        this.userClient = userClient;
    }

    @GetMapping("get/{orderid}")
    public Result<Order> getOrder(@PathVariable("orderid") Integer orderid){
        Result<User> user = userClient.getUser(123);
        Order order = new Order();
        order.setId(orderid);
        order.setPrice(789.0);
        order.setUser(user.getData());
        return new Result<Order>().back(200,"操作成功",true,order);
    }
}
