package io.linfeng.common.enums;

/**
 * 性别
 */
public enum GenderStatus {
    /**
     * 男
     */
    MAN(1),
    /**
     * 女
     */
    WOMEN(2),
    /**
     * 保密
     */
    UNKNOWN(0);

    private int value;

    GenderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}