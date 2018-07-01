package org.apache.fineract.catalogue.bank_acc.domain;

import java.util.HashMap;
import java.util.Map;

public enum BankAccountStatus {
    ACTIVE(1, "bankAccountStatus.active"),
    INACTIVE(2, "bankAccountStatus.inactive");

    private final long value;
    private final String code;

    BankAccountStatus(long value, String code) {
        this.value = value;
        this.code = code;
    }

    public long getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    private static final Map<Long, BankAccountStatus> intToEnumMap = new HashMap<>();

    static {
        for (final BankAccountStatus type : BankAccountStatus.values()) {
            intToEnumMap.put(type.value, type);
        }
    }

    public static BankAccountStatus fromInt(final long i) {
        final BankAccountStatus status = intToEnumMap.get(Long.valueOf(i));
        return status;
    }

    @Override
    public String toString() {
        return name().toString();
    }
}
