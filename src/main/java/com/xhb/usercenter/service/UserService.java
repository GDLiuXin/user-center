package com.xhb.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhb.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-03-11 18:53:33
 * 用户服务
 *
*/
public interface UserService  extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode 编号
     * @return 新用户id
     */

    long userResister(String userAccount,String userPassword ,String checkPassword,String planetCode);

    /**
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request 用户账号，用户密码
     * @return 脱敏的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser 用户脱敏
     * @return  用户脱敏信息
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request request
     * @return er
     */
    int userLogout(HttpServletRequest request);


}