package com.jay.studentmanager.service;

import com.jay.studentmanager.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生数据服务（Model 层的业务部分）：内存存储，负责增删改查。
 * 成败一律用 boolean 返回：true 成功，false 失败。
 * 修复了原项目“失败返回 null、调用方却照常加入列表”的缺陷，本层不会产生 null 元素。
 */
public class StudentService {

    private final List<Student> students = new ArrayList<>();

    /** 添加学生，学号必须唯一；成功返回 true，学号重复返回 false */
    public boolean add(Student student) {
        if (student == null) {
            return false;
        }
        if (findById(student.getId()) != null) {
            return false;
        }
        students.add(student);
        return true;
    }

    /** 按学号删除；成功返回 true，学号不存在返回 false */
    public boolean delete(String id) {
        Student student = findById(id);
        if (student == null) {
            return false;
        }
        students.remove(student);
        return true;
    }

    /** 按学号修改姓名 / 年龄 / 地址（就地更新，不存在原版“先删后加”中途丢数据的问题） */
    public boolean update(String id, String name, int age, String address) {
        Student student = findById(id);
        if (student == null) {
            return false;
        }
        student.setName(name);
        student.setAge(age);
        student.setAddress(address);
        return true;
    }

    /** 按学号查找，找不到返回 null */
    public Student findById(String id) {
        if (id == null) {
            return null;
        }
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if (id.equals(student.getId())) {
                return student;
            }
        }
        return null;
    }

    public boolean exists(String id) {
        return findById(id) != null;
    }

    /** 返回全部学生（一份拷贝，外部增删不影响服务内部数据） */
    public List<Student> listAll() {
        return new ArrayList<>(students);
    }

    public int count() {
        return students.size();
    }
}
