package project.personalproject.global.util;

public class ValidationUtil {

    // TODO : Util 테스트 주도 개발
    public static boolean isEmail(String email) {
        return email != null && email.contains("@");
    }
}
