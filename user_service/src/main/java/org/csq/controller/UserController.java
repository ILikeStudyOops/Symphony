package org.csq.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.csq.entity.Result;
import org.csq.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户测试请求")
@RestController
@RequestMapping("user")
public class UserController {

    @ApiOperation("获取用户信息")
    @GetMapping("get/{userid}")
    public Result<User> getUserById(@PathVariable("userid") Integer userid){
        User user = new User();
        user.setId(userid);
        user.setUsername("jack");
        user.setPassword("123456");
        return new Result<User>().back(200,"操作成功",true,user);
    }
}
