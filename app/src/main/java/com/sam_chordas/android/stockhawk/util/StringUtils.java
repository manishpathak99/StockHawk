package com.sam_chordas.android.stockhawk.util;

import java.util.Random;

public final class StringUtils {

    private static int space = 1;

    private StringUtils() {
    }

    public static boolean isEmpty(String str) {
        if ("".equals(str) || "null".equals(str) || str == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks for all empty string inside passed string(array).
     *
     * @param strValues
     *            string array to be checked
     * @return true if all of the string is null or empty, false otherwise.
     */
    public static boolean isAllNullOrEmpty(String... strValues) {
       return containsNullOrEmpty(strValues);
    }

    public static boolean notEmpty(String str) {
        if ("".equals(str) || "null".equals(str) || str == null) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isDigit(String str) {
        int j, i;
        int flag = 0;
        if (isEmpty(str))
            return false;
        if (notEmpty(str)) {
            for (i = 0; i < str.length(); i++) {
                j = str.charAt(i);
                if ((j >= 65 && j <= 90) || (j >= 97 && j <= 122)) {
                    flag = 1;
                    break;
                }

            }
        }
        return flag == 1 ? false : true;
    }

    public static String cutString(String str, int length) {
        String result = str;
        if (str.length() > length) {
            result = str.substring(0, length) + "...";
        }
        return result;
    }

    public static String appendRandomSpace(String string) {
        Random random = new Random();
        int randomNum = random.nextInt(50);
        for (int index = 0; index < randomNum; index++)
            string = string + index;
        return string;
    }

    public static String inBetweenSpace(String string) {
        if (string.contains(" ")) {
            if (space == 1) {
                int t = string.indexOf(" ");
                string = string.substring(0, t + 1) + " " + string.substring(t + 1);
                space = 0;
            } else {
                space = 1;
            }
        }
        return string;
    }

    public static String appendInBetweenRandomSpace(String string) {
        String spaces = " ";
        Random random = new Random();
        int randomNum = random.nextInt(10);
        for (int index = 0; index < randomNum; index++)
            spaces = spaces + " ";
        if (string.contains(" ")) {
            int t = string.indexOf(" ");
            string = string.substring(0, t) + spaces + string.substring(t + 1);
        }
        return string;
    }

    /**
     * Checks for empty string inside passed string(array).
     *
     * @param strValues string array to be checked
     * @return true if any of the string is null or empty, false otherwise.
     */
    public static boolean containsNullOrEmpty(String... strValues) {
        boolean isEmpty = false;
        for (String strValue : strValues) {
            if (strValue == null || "null".equals(strValue) || strValue.trim().length() == 0) {
                isEmpty = true;
                break;
            }
        }
        return isEmpty;
    }

    /**
     * @param booleanString
     * @return if 0,null, "" or NULL return false otherwise true.
     */
    public static Boolean getBooleanServer (String booleanString) {
        if("0".equals(booleanString) || isEmpty(booleanString)){
            return false;
        }
        else {
            return true;
        }
    }


}
