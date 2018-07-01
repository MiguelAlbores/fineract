package org.apache.fineract.catalogue.bank.domain;

import java.util.HashMap;
import java.util.Map;

public enum BankStatus {
    ACTIVE(1, "bankStatus.active"),
    INACTIVE(2, "bankStatus.inactive");

    private final long value;
    private final String code;

    BankStatus(long value, String code) {
        this.value = value;
        this.code = code;
    }

    public long getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    private static final Map<Long, BankStatus> intToEnumMap = new HashMap<>();

    static {
        for (final BankStatus type : BankStatus.values()) {
            intToEnumMap.put(type.value, type);
        }
    }

    public static BankStatus fromInt(final long i) {
        final BankStatus status = intToEnumMap.get(Long.valueOf(i));
        return status;
    }

    @Override
    public String toString() {
        return name().toString();
    }
}
