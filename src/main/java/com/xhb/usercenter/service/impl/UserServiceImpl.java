package com.xhb.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhb.usercenter.common.ErrorCode;
import com.xhb.usercenter.exception.BusinessException;
import com.xhb.usercenter.mapper.UserMapper;
import com.xhb.usercenter.model.domain.User;
import com.xhb.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xhb.usercenter.contant.UserConstant.USER_LOGIN_STATE;


/**
* 用户服务实现类
*
* @author Administrator
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    /*
    * 加盐
    * */
    private static final String SALT = "XHB";


    @Override
    public long userResister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        // 1.校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号小于四位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码过短");
        }
        if (planetCode.length() > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编号过长");
        }
        // 账户不能包含特殊字符
        String  validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：’“”。，、？ ]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号包含特殊字符");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不相同");        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        // 编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode",planetCode);
         count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编号重复");
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex(( SALT + userPassword).getBytes());
        // 3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean savaResult = this.save(user);
        if (!savaResult){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码错误");
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号错误");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        }
        // 账户不能包含特殊字符
        String  validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：’“”。，、？ ]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号包含特殊字符");
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex(( SALT + userPassword).getBytes());

        // 3.插入数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"您输入的账号不存在");
        }
        // 3.用户脱敏
        User safetyuser = getSafetyUser(user);
        // 4.记录用户的登陆态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return safetyuser;
    }

    /**
     *用户脱敏
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            return null;
        }
        User safetyuser = new User();
        safetyuser.setId(originUser.getId());
        safetyuser.setUsername(originUser.getUsername());
        safetyuser.setUserAccount(originUser.getUserAccount());
        safetyuser.setAvatarUrl(originUser.getAvatarUrl());
        safetyuser.setGender(originUser.getGender());
        safetyuser.setPhone(originUser.getPhone());
        safetyuser.setEmail(originUser.getEmail());
        safetyuser.setPlanetCode(originUser.getPlanetCode());
        safetyuser.setUserRole(originUser.getUserRole());
        safetyuser.setUserStatus(originUser.getUserStatus());
        safetyuser.setCreateTime(originUser.getCreateTime());
        return safetyuser;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


}




