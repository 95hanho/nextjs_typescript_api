package me._hanho.nextjs_shop.buy;

@lombok.Data
public class ExistingHold {
    private int productOptionId;
    private int holdId;
    private int count;
    public static final ExistingHold ZERO = new ExistingHold();
}