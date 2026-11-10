package com.jay.studentmanager.model;

/**
 * 用户实体：用户名、密码、身份证号、手机号。
 * 手机号由原来的 long 改为 String：手机号是“编号”不是“数值”，
 * 用 String 才能正确表达“不以 0 开头”这条校验规则。
 */
public class User {

    private String username;
    private String password;
    private String personid;
    private String phonenumber;

    public User() {
    }

    public User(String username, String password, String personid, String phonenumber) {
        this.username = username;
        this.password = password;
        this.personid = personid;
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
