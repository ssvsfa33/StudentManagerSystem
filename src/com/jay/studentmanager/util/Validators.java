package com.jay.studentmanager.util;

/**
 * 格式校验工具，规则与原项目 text.java 完全一致：
 * 身份证号 18 位（前 17 位必须是数字）、手机号 11 位且不以 0 开头。
 * 只用到 for + if，没有新语法。
 */
public final class Validators {

    private Validators() {
    }

    /** 身份证号：长度 18 位，前 17 位必须是数字（末位允许是 X） */
    public static boolean isValidPersonId(String personid) {
        if (personid == null || personid.length() != 18) {
            return false;
        }
        for (int i = 0; i < 17; i++) {
            char c = personid.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /** 手机号：11 位数字，且不以 0 开头 */
    public static boolean isValidPhoneNumber(String phonenumber) {
        if (phonenumber == null || phonenumber.length() != 11) {
            return false;
        }
        if (phonenumber.charAt(0) == '0') {
            return false;
        }
        for (int i = 0; i < phonenumber.length(); i++) {
            char c = phonenumber.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
