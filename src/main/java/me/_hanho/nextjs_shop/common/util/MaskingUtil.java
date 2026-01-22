package me._hanho.nextjs_shop.common.util;

public class MaskingUtil {

    private MaskingUtil() {
        // util class
    }

    /**
     * 유저 이름 마스킹
     * 예) 홍길동 → 홍길*****
     */
    public static String maskUserIdName(String userName, int starCount) {
        if (userName == null || userName.isEmpty()) return userName;

        int prefixLen = Math.min(2, userName.length());
        return userName.substring(0, prefixLen) + repeatStar(starCount);
    }

    /**
     * 전화번호 마스킹
     * 예) 01012345678 → 010****5678
     *     010-1234-5678 → 010****5678
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.isEmpty()) return phone;

        // 숫자만 추출
        String digits = phone.replaceAll("\\D", "");
        if (digits.length() < 10) return phone;

        String front = digits.substring(0, 3);
        String back = digits.substring(digits.length() - 4);

        return front + "****" + back;
    }

    /**
     * 이메일 마스킹
     * 예) hoseong@gmail.com → ho****@gmail.com
     */
    public static String maskEmail(String email, int starCount) {
        if (email == null || email.isEmpty()) return email;

        int atIndex = email.indexOf("@");
        if (atIndex <= 1) return email;

        String prefix = email.substring(0, Math.min(2, atIndex));
        String domain = email.substring(atIndex);

        return prefix + repeatStar(starCount) + domain;
    }

    /**
     * 별(*) 반복 생성
     */
    private static String repeatStar(int count) {
        if (count <= 0) return "";
        return "*".repeat(count);
    }
}
