package com.assess.util;

import com.fasterxml.jackson.databind.ser.Serializers;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {
    public static String getBase64String(String origin){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(origin.getBytes());
    }

    public static String getOriginString(String base64Str){
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            return new String(decoder.decode(base64Str.getBytes()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("base64反编译失败");
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(Base64Util.getBase64String("18888888888"));
        System.out.println(Base64Util.getOriginString("YWRtaW4="));
    }
}
