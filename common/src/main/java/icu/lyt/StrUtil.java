package icu.lyt;

public class StrUtil {

    public static String removeWhiteSpaceAndComment(String command) {
        return command.trim().split("//")[0].replaceAll(" ", "");
    }


}
