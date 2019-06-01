package com.assess.util;

import java.util.Date;
import java.util.Random;

public class RandomUtil {
    public static String get32Random() {
        int passLength = 18;
        StringBuffer buffer = null;
        StringBuffer sb = new StringBuffer();
        sb.append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());

        buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        sb.append(buffer.charAt(r.nextInt(buffer.length() - 10)));
        passLength -= 1;

        int range = buffer.length();
        for (int i = 0; i < passLength; ++i) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }

    public static String getTimeAndCountRandom(int count){
        if (count <= 0){
            return null;
        }
        int passLength = count;
        StringBuffer buffer = null;
        StringBuffer sb = new StringBuffer();
        sb.append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());

        buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        sb.append(buffer.charAt(r.nextInt(buffer.length() - 10)));
        passLength -= 1;

        int range = buffer.length();
        for (int i = 0; i < passLength; ++i) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String a = "18233189798";
        String b = a.substring(a.length()-6);
        System.out.println(b);
    }
}
