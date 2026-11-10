package com.jay.studentmanager;

import com.jay.studentmanager.controller.AuthController;
import com.jay.studentmanager.controller.StudentController;
import com.jay.studentmanager.model.Student;
import com.jay.studentmanager.service.StudentService;
import com.jay.studentmanager.service.UserService;
import com.jay.studentmanager.util.CaptchaGenerator;
import com.jay.studentmanager.util.Validators;

/**
 * 服务层 / 控制器层冒烟测试（不涉及菜单交互）。
 * 全部用返回值做判断，没有 try。
 * 运行：java -cp out com.jay.studentmanager.SmokeTest
 * 全部通过退出码为 0，有失败则为 1。
 */
public class SmokeTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testValidators();
        testCaptcha();
        testUserService();
        testAuthController();
        testStudentController();

        System.out.println();
        System.out.println("passed=" + passed + ", failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---------- 格式校验 ----------
    private static void testValidators() {
        check("合法身份证通过", Validators.isValidPersonId("110101199901011234"));
        check("末位是X也允许", Validators.isValidPersonId("11010119990101123X"));
        check("18位但中间有字母拒绝", !Validators.isValidPersonId("110101199A01011234"));
        check("17位身份证拒绝", !Validators.isValidPersonId("1101011999010123"));

        check("合法手机号通过", Validators.isValidPhoneNumber("13800138000"));
        check("10位手机号拒绝", !Validators.isValidPhoneNumber("1380013800"));
        check("12位手机号拒绝", !Validators.isValidPhoneNumber("138001380001"));
        check("0开头手机号拒绝", !Validators.isValidPhoneNumber("01234567890"));
    }

    // ---------- 验证码 ----------
    private static void testCaptcha() {
        String captcha = CaptchaGenerator.generate();
        check("验证码长度为5", captcha.length() == 5);

        int digitCount = 0;
        for (int i = 0; i < captcha.length(); i++) {
            char c = captcha.charAt(i);
            if (c >= '0' && c <= '9') {
                digitCount++;
            }
        }
        check("验证码恰好含1个数字", digitCount == 1);
    }

    // ---------- 用户注册 / 改密 ----------
    private static void testUserService() {
        UserService userService = new UserService();

        check("首次注册成功", userService.register("jay", "123456", "110101199901011234", "13800138000") == null);
        check("注册后只有1条记录（修复原版双写bug）", userService.userCount() == 1);

        check("重复用户名被拒绝",
                "用户名已存在".equals(userService.register("jay", "654321", "110101199901011235", "13800138001")));
        check("注册失败不会产生新记录", userService.userCount() == 1);

        // 校验顺序与原版一致：身份证 → 手机号 → 用户名
        check("身份证错误优先提示",
                "身份证号码格式错误".equals(userService.register("jay", "x", "bad-id", "13800138000")));
        check("手机号错误其次提示",
                "手机号格式错误".equals(userService.register("newbie", "x", "110101199901011234", "01234567890")));

        check("用户名存在查询", userService.usernameExists("jay"));
        check("未注册用户名查询", !userService.usernameExists("nobody"));

        check("原密码校验通过", userService.checkPassword("jay", "123456"));
        check("错误密码校验不通过", !userService.checkPassword("jay", "wrong"));

        check("改密成功", userService.changePassword("jay", "13800138000", "newpwd") == null);
        check("改密后旧密码失效", !userService.checkPassword("jay", "123456"));
        check("改密后新密码生效", userService.checkPassword("jay", "newpwd"));
        check("改密手机号错误提示", "手机号错误".equals(userService.changePassword("jay", "13911112222", "x")));
        check("改密用户名不存在提示",
                "该用户名不存在".equals(userService.changePassword("nobody", "13800138000", "x")));
    }

    // ---------- 登录 ----------
    private static void testAuthController() {
        AuthController auth = new AuthController(new UserService());
        auth.register("jay", "123456", "110101199901011234", "13800138000");

        check("登录成功", auth.login("jay", "123456", "aB3d", "Ab3D") == AuthController.LOGIN_SUCCESS);
        check("验证码忽略大小写", auth.login("jay", "123456", "aB3d", "AB3D") == AuthController.LOGIN_SUCCESS);
        check("密码错误", auth.login("jay", "wrong", "aB3d", "aB3d") == AuthController.LOGIN_WRONG_PASSWORD);
        check("验证码错误", auth.login("jay", "123456", "aB3d", "zz") == AuthController.LOGIN_WRONG_CAPTCHA);
        check("用户名不存在", auth.login("nobody", "1", "a", "a") == AuthController.LOGIN_USER_NOT_FOUND);
    }

    // ---------- 学生增删改查 ----------
    private static void testStudentController() {
        StudentController controller = new StudentController(new StudentService());

        check("添加成功", controller.add("001", "张三", 20, "北京"));
        check("添加后1条", controller.listAll().size() == 1);

        check("重复学号被拒绝", !controller.add("001", "李四", 21, "上海"));
        check("被拒绝后仍是1条（修复原版null入表bug）", controller.listAll().size() == 1);

        boolean hasNull = false;
        for (int i = 0; i < controller.listAll().size(); i++) {
            if (controller.listAll().get(i) == null) {
                hasNull = true;
            }
        }
        check("列表里没有null元素", !hasNull);

        check("学号存在查询", controller.exists("001"));
        check("学号不存在查询", !controller.exists("999"));
        check("删除不存在的学号返回false", !controller.delete("999"));

        check("修改成功", controller.update("001", "张三丰", 25, "武当"));
        Student first = controller.listAll().get(0);
        check("修改后姓名年龄正确", "张三丰".equals(first.getName()) && first.getAge() == 25);
        check("修改后学号不变", "001".equals(first.getId()));
        check("修改不存在的学号返回false", !controller.update("999", "x", 1, "y"));

        check("删除成功", controller.delete("001"));
        check("删除后列表为空", controller.listAll().size() == 0);
    }

    private static void check(String name, boolean ok) {
        if (ok) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }
}
