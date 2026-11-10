package com.jay.studentmanager.view;

import com.jay.studentmanager.controller.AuthController;
import com.jay.studentmanager.controller.StudentController;
import com.jay.studentmanager.util.CaptchaGenerator;

/**
 * 登录模块视图（MVC 的 V）：
 * 负责打印菜单、询问输入、显示结果；所有判断都交给 AuthController。
 * 对应原项目 text.java 的 loginui / logchoose / login / signup / changepassword。
 */
public class LoginView {

    // 菜单选项常量（沿用原项目的写法）
    private static final int LOGIN = 1;
    private static final int SIGN_UP = 2;
    private static final int FORGET_PASSWORD = 3;
    private static final int EXIT = 4;

    // 登录最多允许的尝试次数
    private static final int MAX_ATTEMPTS = 3;

    private final ConsoleUi ui;
    private final AuthController authController;
    private final StudentView studentView;

    public LoginView(ConsoleUi ui, AuthController authController, StudentController studentController) {
        this.ui = ui;
        this.authController = authController;
        this.studentView = new StudentView(ui, studentController);
    }

    /** 登录主菜单循环（对应原 loginui） */
    public void run() {
        int choice = 0;
        while (choice != EXIT) {
            ui.println("欢迎来到jay的学生管理系统");
            ui.println("请选择操作：1登录 2注册 3忘记密码 4退出");
            choice = ui.readInt("");
            handle(choice);
        }
    }

    private void handle(int choice) {
        switch (choice) {
            case LOGIN:
                doLogin();
                break;
            case SIGN_UP:
                doSignUp();
                break;
            case FORGET_PASSWORD:
                doForgetPassword();
                break;
            case EXIT:
                break;
            default:
                ui.println("您输入的选择有误，请重新输入");
        }
    }

    /** 登录（对应原 login）：最多 3 次机会，每次都重新输入密码和验证码 */
    private void doLogin() {
        ui.println("请输入用户名:");
        String username = ui.readToken("");
        if (!authController.usernameExists(username)) {
            ui.println("用户名不存在,请先注册");
            ui.println("登录失败");
            return;
        }

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            ui.println("请输入密码:");
            String password = ui.readToken("");
            String captcha = CaptchaGenerator.generate();
            ui.println("请输入验证码：" + captcha);
            String inputCaptcha = ui.readToken("");

            int result = authController.login(username, password, captcha, inputCaptcha);
            if (result == AuthController.LOGIN_SUCCESS) {
                ui.println("登录成功");
                studentView.run();  // 进入学生管理菜单，退出后回到登录菜单
                return;
            }
            if (result == AuthController.LOGIN_WRONG_PASSWORD) {
                int left = MAX_ATTEMPTS - attempt;
                if (left > 0) {
                    ui.println("密码错误,请重新输入，剩余次数" + left + "次");
                } else {
                    ui.println("密码错误,3次均失败");
                }
            } else if (result == AuthController.LOGIN_WRONG_CAPTCHA) {
                ui.println("验证码错误,请重新输入");
            }
        }
        ui.println("登录失败");
    }

    /** 注册（对应原 signup）：成功打印“注册成功”，失败打印原因 + “注册失败” */
    private void doSignUp() {
        ui.println("请输入用户名:");
        String username = ui.readToken("");
        ui.println("请输入密码:");
        String password = ui.readToken("");
        ui.println("请输入身份证号:");
        String personid = ui.readToken("");
        ui.println("请输入手机号:");
        String phonenumber = ui.readToken("");

        String error = authController.register(username, password, personid, phonenumber);
        if (error == null) {
            ui.println("注册成功");
        } else {
            ui.println(error);
            ui.println("注册失败");
        }
    }

    /** 忘记密码（对应原 changepassword）：先确认用户名，再用注册手机号验证 */
    private void doForgetPassword() {
        ui.println("请输入用户名:");
        String username = ui.readToken("");
        if (!authController.usernameExists(username)) {
            ui.println("密码修改失败");
            return;
        }
        ui.println("请输入注册时使用的手机号");
        String phonenumber = ui.readToken("");
        ui.println("请输入新密码:");
        String newPassword = ui.readToken("");

        String error = authController.changePassword(username, phonenumber, newPassword);
        if (error == null) {
            ui.println("密码修改成功");
        } else {
            ui.println(error);
            ui.println("密码修改失败");
        }
    }
}
