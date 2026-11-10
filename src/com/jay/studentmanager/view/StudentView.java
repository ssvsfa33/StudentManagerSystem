package com.jay.studentmanager.view;

import com.jay.studentmanager.controller.StudentController;
import com.jay.studentmanager.model.Student;

import java.util.List;

/**
 * 学生管理菜单视图（MVC 的 V）：
 * 负责打印菜单、询问输入、显示结果；所有判断都交给 StudentController。
 * 对应原项目 text.java 的 chooseui / choose / addstudent / delStudent / Modify / text。
 */
public class StudentView {

    // 菜单选项常量（沿用原项目的写法）
    private static final int ADD_STUDENT = 1;
    private static final int DELETE_STUDENT = 2;
    private static final int UPDATE_STUDENT = 3;
    private static final int SEARCH_STUDENT = 4;
    private static final int EXIT = 5;

    private final ConsoleUi ui;
    private final StudentController controller;

    public StudentView(ConsoleUi ui, StudentController controller) {
        this.ui = ui;
        this.controller = controller;
    }

    /** 学生管理主菜单循环（对应原 chooseui） */
    public void run() {
        int choice = 0;
        while (choice != EXIT) {
            ui.println("-------------欢迎来到Jay的学生管理系统-------------");
            ui.println("1: 添加学生");
            ui.println("2: 删除学生");
            ui.println("3: 修改学生");
            ui.println("4: 查询学生");
            ui.println("5: 退出");
            ui.println("请输入您的选择:");
            choice = ui.readInt("");
            handle(choice);
        }
    }

    private void handle(int choice) {
        switch (choice) {
            case ADD_STUDENT:
                doAdd();
                break;
            case DELETE_STUDENT:
                doDelete();
                break;
            case UPDATE_STUDENT:
                doUpdate();
                break;
            case SEARCH_STUDENT:
                doList();
                break;
            case EXIT:
                break;
            default:
                ui.println("您输入的选择有误，请重新输入");
        }
    }

    /** 添加（对应原 addstudent）：先问学号，重复立刻提示，后面的信息不再白问 */
    private void doAdd() {
        ui.println("请输入学生ID:");
        String id = ui.readToken("");
        if (controller.exists(id)) {
            ui.println("该学号已存在");
            return;
        }
        ui.println("请输入学生姓名:");
        String name = ui.readToken("");
        ui.println("请输入学生年龄:");
        int age = ui.readInt("");
        ui.println("请输入学生地址:");
        String address = ui.readToken("");

        boolean success = controller.add(id, name, age, address);
        if (success) {
            ui.println("添加成功");
        } else {
            ui.println("该学号已存在");
        }
    }

    /** 删除（对应原 delStudent） */
    private void doDelete() {
        ui.println("请输入你要删除的学生的学号");
        String id = ui.readToken("");

        boolean success = controller.delete(id);
        if (success) {
            ui.println("删除成功");
        } else {
            ui.println("该学号不存在");
        }
    }

    /**
     * 修改（对应原 Modify）：
     * 学号不存在直接提示（原版会把 null 加进列表，下次查询就崩）；
     * 学号存在则重新输入姓名 / 年龄 / 地址，就地更新（原版是先删后加，中途失败会丢数据）。
     */
    private void doUpdate() {
        ui.println("请输入你要修改的学生的学号");
        String id = ui.readToken("");
        if (!controller.exists(id)) {
            ui.println("该学号不存在");
            return;
        }
        ui.println("请输入学生姓名:");
        String name = ui.readToken("");
        ui.println("请输入学生年龄:");
        int age = ui.readInt("");
        ui.println("请输入学生地址:");
        String address = ui.readToken("");

        boolean success = controller.update(id, name, age, address);
        if (success) {
            ui.println("修改成功");
        } else {
            ui.println("该学号不存在");
        }
    }

    /** 查询（对应原 text 方法）：列出全部学生 */
    private void doList() {
        List<Student> students = controller.listAll();
        if (students.size() == 0) {
            ui.println("当前没有学生信息");
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            ui.println(s.getId() + " " + s.getName() + " " + s.getAge() + " " + s.getAddress());
        }
    }
}
