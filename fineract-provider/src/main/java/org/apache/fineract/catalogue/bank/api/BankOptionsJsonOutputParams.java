package org.apache.fineract.catalogue.bank.api;

import java.util.HashSet;
import java.util.Set;

public enum BankOptionsJsonOutputParams {
    BANK_STATUS_OPTIONS("bankStatusOptions"),
    BANK_TYPE_OPTION("bankTypeOptions");
    private final String value;

    BankOptionsJsonOutputParams(final String value) {
        this.value = value;
    }

    private static final Set<String> values = new HashSet<>();
    static {
        for (final BankOptionsJsonOutputParams type : BankOptionsJsonOutputParams.values()) {
            values.add(type.value);
        }
    }

    public static Set<String> getAllValues() {
        return values;
    }

    @Override
    public String toString() {
        return name().toString().replaceAll("_", " ");
    }

    public String getValue() {
        return this.value;
    }
}
