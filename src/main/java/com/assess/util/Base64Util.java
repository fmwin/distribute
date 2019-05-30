package com.assess.util;

import java.util.Base64;

public class Base64Util {
    public static String getBase64String(String origin){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(origin.getBytes());
    }

    public static void main(String[] args) {
        System.out.println(Base64Util.getBase64String("18888888888"));
    }
}
