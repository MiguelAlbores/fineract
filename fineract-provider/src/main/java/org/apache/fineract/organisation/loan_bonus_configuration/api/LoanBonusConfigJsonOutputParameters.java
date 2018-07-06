package org.apache.fineract.organisation.loan_bonus_configuration.api;

import java.util.HashSet;
import java.util.Set;

public enum LoanBonusConfigJsonOutputParameters {
    DAYS_IN_ARREAR("daysInArrear"), DAYS_TO_COLLECT_BONUS("daysToCollectBonus"),
    GL_ACCOUNT_TO_DEBIT("glAccountToDebit"), GL_ACCOUNT_TO_CREDIT("glAccountToCredit"),
    CYCLES_SETTINGS("cycles"), LOAN_CYCLE_FROM_VALUE("fromValue"),
    LOAN_CYCLE_TO_VALUE("toValue"),LOAN_CYCLE_PERCENT_VALUE("percentValue"),
    UPDATED_BY("updatedBy");
    private final String value;

    LoanBonusConfigJsonOutputParameters(final String value) {
        this.value = value;
    }

    private static final Set<String> values = new HashSet<>();
    static {
        for (final LoanBonusConfigJsonOutputParameters type : LoanBonusConfigJsonOutputParameters.values()) {
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