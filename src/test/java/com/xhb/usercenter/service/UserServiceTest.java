package com.xhb.usercenter.service;

import com.xhb.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务注册
 */
@SpringBootTest
public class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    public void testAddUser(){
        User user = new User();
        user.setUsername("xiehaobin");
        user.setUserAccount("123");
        user.setAvatarUrl("https://www.baidu.com/");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("123");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }
    @Test
    void userResister() {
        String userAccount ="xiehaobin";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userResister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "xi";
        result = userService.userResister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount ="haobin";
        userPassword = "123456";
        result = userService.userResister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount ="hao bin";
        userPassword = "12345678";
        result = userService.userResister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        checkPassword = "123456789";
        result = userService.userResister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount ="xiehaobin";
        userPassword = "12345678";
        result = userService.userResister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount ="xiehaobin";
        result = userService.userResister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertTrue(result < 0);

    }
}