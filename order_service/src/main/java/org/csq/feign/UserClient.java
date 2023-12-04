package org.csq.feign;

import org.csq.entity.Result;
import org.csq.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("userservice")
public interface UserClient {

    @GetMapping("/user/get/{userid}")
    Result<User> getUser(@PathVariable("userid") Integer userid,
                         @RequestHeader("token") String token);
}
