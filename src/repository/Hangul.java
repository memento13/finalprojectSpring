package repository;

import java.io.UnsupportedEncodingException;

public class Hangul {
    public static String hangul(String input)  {
        try {
            byte ptext[] = input.getBytes("ISO-8859-1");
            input = new String(ptext, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return input;
    }
}
