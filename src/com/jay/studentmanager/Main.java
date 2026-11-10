package com.jay.studentmanager;

import com.jay.studentmanager.controller.AuthController;
import com.jay.studentmanager.controller.StudentController;
import com.jay.studentmanager.service.StudentService;
import com.jay.studentmanager.service.UserService;
import com.jay.studentmanager.view.ConsoleUi;
import com.jay.studentmanager.view.LoginView;

/**
 * 程序入口（原 text.java 的 main 搬到了这里）：
 * 按 MVC 顺序组装——服务 Model → 控制器 Controller → 视图 View，然后启动登录菜单。
 */
public class Main {

    public static void main(String[] args) {
        // M：内存数据 + 业务逻辑
        UserService userService = new UserService();
        StudentService studentService = new StudentService();

        // C：夹在视图和服务之间做转发
        AuthController authController = new AuthController(userService);
        StudentController studentController = new StudentController(studentService);

        // V：只负责提问和显示
        ConsoleUi ui = new ConsoleUi();
        LoginView loginView = new LoginView(ui, authController, studentController);

        loginView.run();
    }
}
