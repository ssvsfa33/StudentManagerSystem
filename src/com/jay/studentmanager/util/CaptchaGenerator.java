package com.jay.studentmanager.util;

import java.util.Random;

/**
 * 登录验证码生成：4 个随机字母 + 1 个数字，再打乱位置。
 * 算法与原项目 text.java 的 Verify() 相同，只是整理了写法、去掉了字符串拼接。
 */
public final class CaptchaGenerator {

    private static final Random RANDOM = new Random();

    private CaptchaGenerator() {
    }

    public static String generate() {
        // 52 个字母：小写 a-z + 大写 A-Z
        char[] letters = new char[52];
        for (int i = 0; i < 26; i++) {
            letters[i] = (char) ('a' + i);
            letters[i + 26] = (char) ('A' + i);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(letters[RANDOM.nextInt(letters.length)]);
        }
        sb.append(RANDOM.nextInt(10));

        // 把末尾的数字与前面某一位交换，让它不固定在最后
        char[] chars = sb.toString().toCharArray();
        int index = RANDOM.nextInt(chars.length - 1);
        char temp = chars[4];
        chars[4] = chars[index];
        chars[index] = temp;
        return new String(chars);
    }
}
