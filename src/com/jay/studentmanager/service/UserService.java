package com.jay.studentmanager.service;

import com.jay.studentmanager.model.User;
import com.jay.studentmanager.util.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据服务（Model 层的业务部分）：注册 / 登录校验 / 修改密码。
 * 注册和改密返回 String：null 表示成功，非 null 是要显示给用户的错误原因
 * （和原项目 signup() 返回 null 表示失败是同一个思路，只是把原因也带出来了）。
 * 修复了原项目“signup 内部 add 一次、调用方又 add 一次”的注册双写缺陷。
 */
public class UserService {

    private final List<User> users = new ArrayList<>();

    /**
     * 注册。校验顺序与原项目一致：身份证号 → 手机号 → 用户名是否已存在。
     * 返回 null 表示注册成功；否则返回错误信息（如“用户名已存在”）。
     */
    public String register(String username, String password, String personid, String phonenumber) {
        if (!Validators.isValidPersonId(personid)) {
            return "身份证号码格式错误";
        }
        if (!Validators.isValidPhoneNumber(phonenumber)) {
            return "手机号格式错误";
        }
        if (usernameExists(username)) {
            return "用户名已存在";
        }
        users.add(new User(username, password, personid, phonenumber));
        return null;
    }

    public boolean usernameExists(String username) {
        return findUser(username) != null;
    }

    /** 校验用户名和密码是否匹配（用户名不存在或密码不对都返回 false） */
    public boolean checkPassword(String username, String password) {
        User user = findUser(username);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    /**
     * 修改密码：先用注册时的手机号验证身份，再改。
     * 返回 null 表示成功；否则返回错误信息（如“手机号错误”）。
     */
    public String changePassword(String username, String phonenumber, String newPassword) {
        User user = findUser(username);
        if (user == null) {
            return "该用户名不存在";
        }
        if (!user.getPhonenumber().equals(phonenumber)) {
            return "手机号错误";
        }
        user.setPassword(newPassword);
        return null;
    }

    public int userCount() {
        return users.size();
    }

    private User findUser(String username) {
        if (username == null) {
            return null;
        }
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }
}
