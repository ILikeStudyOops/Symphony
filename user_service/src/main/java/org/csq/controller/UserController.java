package org.csq.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.csq.entity.Result;
import org.csq.entity.User;
import org.csq.service.LoginServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户测试请求")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private LoginServcie loginServcie;

    @ApiOperation("获取用户信息")
    @GetMapping("get/{userid}")
    public Result<User> getUserById(@PathVariable("userid") Integer userid){
        User user = new User();
        user.setId(userid);
        user.setUsername("jack");
        user.setPassword("123456");
        return new Result<User>().back(200,"操作成功",true,user);
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        return loginServcie.login(user);
    }

    @PostMapping("/logout")
    public Result logout(){
        return loginServcie.logout();
    }
}
