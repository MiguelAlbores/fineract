package org.apache.fineract.catalogue.bank.domain;

import java.util.HashMap;
import java.util.Map;

public enum BankType {
    BANK(1, "bankType.bank"),
    CORRESPONDENT_AGENT(2, "bankType.correspondentAgent"),
    COMMISSION_AGENT(3, "bankType.commissionAgent"),;
    private final long value;
    private final String code;

    BankType(long value, String code) {
        this.value = value;
        this.code = code;
    }

    public long getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    private static final Map<Long, BankType> intToEnumMap = new HashMap<>();

    static {
        for (final BankType type : BankType.values()) {
            intToEnumMap.put(type.value, type);
        }
    }

    public static BankType fromInt(final long i) {
        final BankType type = intToEnumMap.get(Long.valueOf(i));
        return type;
    }

    @Override
    public String toString() {
        return name().toString();
    }
}
