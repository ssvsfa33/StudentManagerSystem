package com.jay.studentmanager.controller;

import com.jay.studentmanager.service.UserService;

/**
 * 登录模块控制器（MVC 的 C）：
 * 视图负责问用户要数据，控制器负责调用服务做判断，再把结果交给视图显示。
 * 登录结果用 int 常量表示（和原项目 LOGIN=1 这种写法一致），没有用到异常。
 */
public class AuthController {

    /** 登录结果常量 */
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_USER_NOT_FOUND = 1;
    public static final int LOGIN_WRONG_PASSWORD = 2;
    public static final int LOGIN_WRONG_CAPTCHA = 3;

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    public boolean usernameExists(String username) {
        return userService.usernameExists(username);
    }

    /** 注册：返回 null 表示成功，否则返回要显示的错误信息 */
    public String register(String username, String password, String personid, String phonenumber) {
        return userService.register(username, password, personid, phonenumber);
    }

    /** 登录：依次检查 用户名 → 密码 → 验证码（验证码忽略大小写，与原项目一致） */
    public int login(String username, String password, String expectedCaptcha, String inputCaptcha) {
        if (!userService.usernameExists(username)) {
            return LOGIN_USER_NOT_FOUND;
        }
        if (!userService.checkPassword(username, password)) {
            return LOGIN_WRONG_PASSWORD;
        }
        if (!expectedCaptcha.equalsIgnoreCase(inputCaptcha)) {
            return LOGIN_WRONG_CAPTCHA;
        }
        return LOGIN_SUCCESS;
    }

    /** 修改密码：返回 null 表示成功，否则返回要显示的错误信息 */
    public String changePassword(String username, String phonenumber, String newPassword) {
        return userService.changePassword(username, phonenumber, newPassword);
    }
}
