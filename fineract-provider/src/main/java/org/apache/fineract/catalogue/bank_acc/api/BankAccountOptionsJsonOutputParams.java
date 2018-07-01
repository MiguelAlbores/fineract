package org.apache.fineract.catalogue.bank_acc.api;

import java.util.HashSet;
import java.util.Set;

public enum BankAccountOptionsJsonOutputParams {
    BANK_STATUS_OPTIONS("bankAccountStatusOptions"),
    BANK_TYPE_OPTION("bankAccountUseOptions");
    private final String value;

    BankAccountOptionsJsonOutputParams(final String value) {
        this.value = value;
    }

    private static final Set<String> values = new HashSet<>();
    static {
        for (final BankAccountOptionsJsonOutputParams type : BankAccountOptionsJsonOutputParams.values()) {
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