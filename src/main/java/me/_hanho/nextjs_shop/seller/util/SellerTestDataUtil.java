package me._hanho.nextjs_shop.seller.util;

public final class SellerTestDataUtil {

    private static final Integer TEST_SELLER_NO = 12;
    private static final String TEST_PREFIX = "[TEST] ";

    private SellerTestDataUtil() {
    }

    public static String addTestPrefixForTestSeller(String value, Integer sellerNo) {
        if (value == null || value.isBlank()) {
            return value;
        }

        if (!TEST_SELLER_NO.equals(sellerNo)) {
            return value;
        }

        if (value.startsWith(TEST_PREFIX)) {
            return value;
        }

        return TEST_PREFIX + value;
    }

}
