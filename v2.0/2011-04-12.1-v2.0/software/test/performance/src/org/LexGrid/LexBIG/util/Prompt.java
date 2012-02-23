package org.LexGrid.LexBIG.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Prompt {
    public static String prompt(String prompt) {
        try {
            System.out.print(prompt);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
            return br.readLine().trim();
        } catch (Exception e) {
            return "Prompt Error";
        }
    }

    public static String prompt(String prompt, String defaultValue) {
        String value = prompt(prompt + " [" + defaultValue + "]: ");
        if (value.length() > 0)
            return value;
        else
            return defaultValue;
    }

    public static boolean prompt(String prompt, boolean defaultValue) {
        while (true) {
            String reply = prompt(prompt, Boolean.toString(defaultValue));
            if (reply.length() <= 0)
                return defaultValue;
            else if (reply.equalsIgnoreCase("true"))
                return true;
            else if (reply.equalsIgnoreCase("false"))
                return false;
            else if (reply.equalsIgnoreCase("t"))
                return true;
            else if (reply.equalsIgnoreCase("f"))
                return false;
        }
    }
    
    public static int prompt(String prompt, int defaultValue) {
        while (true) {
            String reply = prompt(prompt, Integer.toString(defaultValue));
            try {
                return Integer.parseInt(reply);
            } catch (NumberFormatException e) {
                System.out.println("Try again: invalid integer value.");
            }
        }
    }
}
