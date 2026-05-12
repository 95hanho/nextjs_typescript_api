package me._hanho.nextjs_shop.util;

public class IpUtils {
    
    public static String extractClientIp(String forwardedFor) {
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return "unknown";
    }
}
