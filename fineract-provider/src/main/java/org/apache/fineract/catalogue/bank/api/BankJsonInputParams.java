package org.apache.fineract.catalogue.bank.api;

import java.util.HashSet;
import java.util.Set;

public enum BankJsonInputParams {
    NAME("name"), EXTERNAL_CODE("externalCode"), BANK_STATUS_ID("bankStatusId"),
    TYPE("bankTypeId");
    private final String value;

    private BankJsonInputParams(final String value) {
        this.value = value;
    }

    private static final Set<String> values = new HashSet<>();
    static {
        for (final BankJsonInputParams type : BankJsonInputParams.values()) {
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
