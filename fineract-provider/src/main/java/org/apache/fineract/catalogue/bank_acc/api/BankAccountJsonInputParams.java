package org.apache.fineract.catalogue.bank_acc.api;

import java.util.HashSet;
import java.util.Set;

public enum BankAccountJsonInputParams {
    NAME("name"), EXTERNAL_CODE("externalCode"), BANK_ACCOUNT_STATUS_ID("bankAccountStatusId"),
    USE("bankUseId"), GL_ACCOUNT_ID("glAccountId");
    private final String value;

    private BankAccountJsonInputParams(final String value) {
        this.value = value;
    }

    private static final Set<String> values = new HashSet<>();
    static {
        for (final BankAccountJsonInputParams type : BankAccountJsonInputParams.values()) {
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
