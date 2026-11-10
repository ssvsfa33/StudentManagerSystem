package com.jay.studentmanager.controller;

import com.jay.studentmanager.model.Student;
import com.jay.studentmanager.service.StudentService;

import java.util.List;

/**
 * 学生管理控制器（MVC 的 C）：
 * 把视图传来的原始输入组装成学生对象，转交给服务层处理，成败用 boolean 往返。
 */
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    public boolean exists(String id) {
        return studentService.exists(id);
    }

    /** 添加学生：成功 true，学号重复 false */
    public boolean add(String id, String name, int age, String address) {
        Student student = new Student(id, name, age, address);
        return studentService.add(student);
    }

    /** 删除学生：成功 true，学号不存在 false */
    public boolean delete(String id) {
        return studentService.delete(id);
    }

    /** 修改学生：成功 true，学号不存在 false */
    public boolean update(String id, String name, int age, String address) {
        return studentService.update(id, name, age, address);
    }

    /** 查询全部学生 */
    public List<Student> listAll() {
        return studentService.listAll();
    }
}
