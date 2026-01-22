package me._hanho.nextjs_shop.util;

import java.security.SecureRandom;

public final class CouponCodeGenerator {

    // 헷갈리는 문자(0,O,1,I,L) 제외: 입력 실수 방지
    private static final char[] ALPHABET = "ABCDEFGHJKMNPQRSTUVWXYZ23456789".toCharArray();
    private static final SecureRandom RND = new SecureRandom();

    private CouponCodeGenerator() {}

    /**
     * 예: "K7Q9-2HXM-8P3D" (4-4-4 형태)
     */
    public static String generate(int totalLength, int groupSize) {
        if (totalLength <= 0) throw new IllegalArgumentException("totalLength must be > 0");
        if (groupSize <= 0) throw new IllegalArgumentException("groupSize must be > 0");

        StringBuilder raw = new StringBuilder(totalLength);
        for (int i = 0; i < totalLength; i++) {
            raw.append(ALPHABET[RND.nextInt(ALPHABET.length)]);
        }

        // 하이픈 그룹핑
        StringBuilder out = new StringBuilder(totalLength + totalLength / groupSize);
        for (int i = 0; i < raw.length(); i++) {
            if (i > 0 && i % groupSize == 0) out.append('-');
            out.append(raw.charAt(i));
        }
        return out.toString();
    }

    // 편의 메소드: 12자리, 4글자 단위
    public static String generateDefault() {
        return generate(12, 4);
    }
}
