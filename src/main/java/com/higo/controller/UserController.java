package com.higo.controller;

import cn.hutool.json.JSONObject;
import com.higo.common.ApiRestResponse;
import com.higo.common.Constant;
import com.higo.exception.ImoocMallException;
import com.higo.exception.ImoocMallExceptionEnum;
import com.higo.model.pojo.User;
import com.higo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Intellij IDEA.
 *
 * @author ACwin YANG
 * @date 2022/05/25
 */
/**
 * 描述：     用户控制器
 */
@Controller
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

//    @GetMapping("/test")
//    @ResponseBody
//    public User personalPage() {
//        return userService.getUser(currentUser.getId());
//    }


    /**
     * 注册
     */
    //@requestMapping
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestBody JSONObject json) throws ImoocMallException {
        String username = json.getStr("username");
        String password = json.getStr("password");
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8位
        if (password.length() < 8) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(username, password);
        return ApiRestResponse.success();
    }
    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login( @RequestBody JSONObject json, HttpSession session)

            throws ImoocMallException {
        String username = json.getStr("username");
        String password = json.getStr("password");

        System.out.println(json);
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        //保存用户信息时，不保存密码
        user.setPassword(null);
        //设置session 保存Id值
        session.setAttribute(Constant.HIGO_USER, user);

        User currentUser = (User) session.getAttribute(Constant.HIGO_USER);
        System.out.println("after login"+currentUser.getId());

        return ApiRestResponse.success(user);
    }
    /**
     * 显示用户信息
     */
    @GetMapping("/user")
    @ResponseBody
    public ApiRestResponse userInfo(HttpSession session) {
        User currentUser = (User) session.getAttribute(Constant.HIGO_USER);
        System.out.println("Current UserId"+currentUser.getId());
        if (currentUser == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        User user = userService.getUser(currentUser.getId());
        return ApiRestResponse.success(user);
    }



    /**
     * 更新个性签名
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestBody  JSONObject json)
            throws ImoocMallException {
        User currentUser = (User) session.getAttribute(Constant.HIGO_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        String signature = json.getStr("personalizedSignature");
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }


    /**
     * 登出，清除session
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.HIGO_USER);
        System.out.println("exit");
        return ApiRestResponse.success();
    }
    /**
     * 管理员登录接口
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestBody JSONObject json, HttpSession session)

            throws ImoocMallException {
        String username = json.getStr("username");
        String password = json.getStr("password");
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        //校验是否是管理员
        if (userService.checkAdminRole(user)) {
            //是管理员，执行操作
            //保存用户信息时，不保存密码
            user.setPassword(null);
            session.setAttribute(Constant.HIGO_USER, user);
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }
    }
}
