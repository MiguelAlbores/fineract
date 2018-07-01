package org.apache.fineract.catalogue.bank.api;

import java.util.HashSet;
import java.util.Set;

public enum BankJsonOutputParams {
    ID("id"), NAME("name"), EXTERNAL_CODE("externalCode"), TYPE("type"),
    BANK_STATUS_ID("status"), CREATED_AT("createdAt"), CREATED_BY("createdBy"),
    UPDATE_AT("updatedAt"), UPDATED_BY("updatedBy");

    private final String value;

    BankJsonOutputParams(final String value) {
        this.value = value;
    }

    private static final Set<String> values = new HashSet<>();
    static {
        for (final BankJsonOutputParams type : BankJsonOutputParams.values()) {
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
