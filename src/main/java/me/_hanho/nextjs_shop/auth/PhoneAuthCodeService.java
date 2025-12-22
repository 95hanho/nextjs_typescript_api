package me._hanho.nextjs_shop.auth;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class PhoneAuthCodeService {

    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 6자리 숫자 인증번호 생성 (000000 ~ 999999)
     */
    public String generate6DigitCode() {
        int n = secureRandom.nextInt(1_000_000); // 0 ~ 999,999
        return String.format("%06d", n);         // 6자리 zero-padding
    }
}