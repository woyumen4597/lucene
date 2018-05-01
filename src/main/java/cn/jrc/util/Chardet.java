package cn.jrc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/5/1 18:12
 */
public class Chardet {
    public enum CharType {
        Enter, //encounter %
        Code1, //encounter first char after %
        Code2, //encounter second char after %
    }

    public static boolean isUtf8(String code1, String code2, String code3) {
        if (code1.compareTo("E4") >= 0 && code1.compareTo("E9") <= 0 &&
                code2.compareTo("80") >= 0 && code2.compareTo("BF") <= 0 &&
                code3.compareTo("80") >= 0 && code3.compareTo("BF") <= 0) {
            return true;
        }
        return false;
    }

    public static boolean isGbk(String code1, String code2) {
        if (code1.compareTo("B0") >= 0 && code1.compareTo("F7") <= 0
                && code2.compareTo("A0") >= 0 && code2.compareTo("FF") <= 0) {
            return true;
        }
        return false;
    }

    public static String getEncodeByList(List<String> code) {
        if (code.size() >= 2 && code.size() % 2 == 1 && code.size() % 3 == 0) {
            return "utf8";
        } else if (code.size() >= 2 && code.size() % 2 == 0 && code.size() % 3 != 0) {
            return "gbk";
        } else if (code.size() % 6 == 0) {
            for (int m = 0; m < code.size(); m = m + 6) {
                if (!isUtf8(code.get(m), code.get(m + 1), code.get(m + 2)) &&
                        isGbk(code.get(m), code.get(m + 1)) &&
                        isGbk(code.get(m + 2), code.get(m + 3))) {
                    return "gbk";
                } else if (isUtf8(code.get(m), code.get(m + 1), code.get(m + 2)) && !isGbk(code.get(m), code.get(m + 1))) {
                    return "utf8";
                }
                if (!isUtf8(code.get(m + 3), code.get(m + 4), code.get(m + 5)) &&
                        isGbk(code.get(m + 2), code.get(m + 3)) &&
                        isGbk(code.get(m + 4), code.get(m + 5))) {
                    return "gbk";
                } else if (isUtf8(code.get(m + 3), code.get(m + 4), code.get(m + 5)) &&
                        !isGbk(code.get(m + 2), code.get(m + 3))) {
                    return "utf8";
                }
            }

        }
        return "utf8";
    }

    public static String getURLEncoding(String url) {
        List<String> codes = new ArrayList<>();
        CharType currentSate = null;
        char c1 = '\0';
        char c2 = '\0';
        for (int i = 0; i < url.length(); i++) {
            char currentChar = url.charAt(i);
            if (currentChar == '%') {
                if (currentSate == CharType.Code2) {
                    char[] s1 = {c1, c2};
                    codes.add(new String(s1));
                }
                currentSate = CharType.Enter;
            } else if (currentSate == CharType.Enter) {
                c1 = currentChar;
                currentSate = CharType.Code1;
            } else if (currentSate == CharType.Code1) {
                c2 = currentChar;
                currentSate = CharType.Code2;
            } else if (currentSate == CharType.Code2) {
                char[] s1 = {c1, c2};
                codes.add(new String(s1));
                currentSate = null;
                return getEncodeByList(codes);
            }
        }
        if (currentSate == CharType.Code2) {
            char[] s1 = {c1, c2};
            codes.add(new String(s1));
        }
        return getEncodeByList(codes);
    }

}
