package com.src.srcapi.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StringUtils {
    public static char[] stringToChar(String s1) {
        char[] charArr = new char[s1.length()];

        for (int i = 0; i < s1.length(); i++) {
            charArr[i] = s1.charAt(i);
        }

        return charArr;
    }

    public static String getStringByInputStream(InputStream inputStream){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] b = new byte[10240];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, n);
            }
        } catch (Exception e) {
            try {
                inputStream.close();
                outputStream.close();
            } catch (Exception ignored) {
            }
        }
        return outputStream.toString();
    }
}
