package org.apache.fineract.catalogue.bank_acc.domain;

import java.util.HashMap;
import java.util.Map;

public enum BankAccountUse {
    CATCHMENT(1, "bankAccountUse.catchment"),
    REPAYMENT(2, "bankAccountUse.repayment"),
    DISBURSEMENT(3, "bankAccountUse.disbursement");

    private final long value;
    private final String code;

    BankAccountUse(long value, String code) {
        this.value = value;
        this.code = code;
    }

    public long getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    private static final Map<Long, BankAccountUse> intToEnumMap = new HashMap<>();

    static {
        for (final BankAccountUse type : BankAccountUse.values()) {
            intToEnumMap.put(type.value, type);
        }
    }

    public static BankAccountUse fromInt(final long i) {
        final BankAccountUse status = intToEnumMap.get(Long.valueOf(i));
        return status;
    }

    @Override
    public String toString() {
        return name().toString();
    }
}
