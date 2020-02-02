package com.sys.readh.utils;

import java.util.Random;

/**
 * 验证码
 * @author sys
 */
public class CodeUtils {
    private static final char[] CHARS = {
            '7', '8', '9', 'E', 'F', 'G', 'H',
            'n', 'p', 'q', 'r', 's','2', '3', '4', '5', '6', 't', 'u',
            'v', 'w','V', 'W', 'X', 'Y', 'Z', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'J', 'K', 'L', 'M',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U',
    };
    public static String randomText() {
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }
}
