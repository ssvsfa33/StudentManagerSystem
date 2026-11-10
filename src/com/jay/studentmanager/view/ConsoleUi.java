package com.jay.studentmanager.view;

import java.util.Scanner;

/**
 * 控制台输入输出封装（视图层的公共零件）：
 * 1. 整个程序只有这一个 Scanner（原项目在 8 个地方各 new 了一个）；
 * 2. 输入不是数字时不再直接崩溃，而是提示后重新输入（全程没有用 try）。
 */
public class ConsoleUi {

    private final Scanner scanner = new Scanner(System.in);

    public void println(String message) {
        System.out.println(message);
    }

    /** 读一个单词（相当于原来的 sc.next()）；prompt 传空字符串时只读取、不打印提示 */
    public String readToken(String prompt) {
        if (!prompt.isEmpty()) {
            System.out.println(prompt);
        }
        return scanner.next();
    }

    /**
     * 读一个非负小整数（菜单选项、学生年龄都用它）。
     * 原项目 sc.nextInt() 遇到字母会直接抛异常闪退，
     * 现在先检查是不是“1~9 位纯数字”，不是就提示后重新输入。
     */
    public int readInt(String prompt) {
        String token = readToken(prompt);
        while (!isSmallNumber(token)) {
            System.out.println("输入有误，请输入数字");
            token = readToken("");
        }
        return Integer.parseInt(token);
    }

    /**
     * 判断是否为 1~9 位纯数字。
     * 限制 9 位是因为 int 最大 2147483647（10 位），9 位以内 parseInt 一定不会溢出，
     * 所以后面的 Integer.parseInt 是安全的，不需要 try。
     */
    private boolean isSmallNumber(String token) {
        if (token == null || token.length() == 0 || token.length() > 9) {
            return false;
        }
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
