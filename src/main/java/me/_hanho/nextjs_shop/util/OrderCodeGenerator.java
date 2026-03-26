package me._hanho.nextjs_shop.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class OrderCodeGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 예시 결과:
     * PAY-20260326152345-482731
     */
    public static String generatePayCode() {
        String dateTime = LocalDateTime.now().format(FORMATTER);
        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000); // 6자리
        return "PAY-" + dateTime + "-" + randomNumber;
    }
}