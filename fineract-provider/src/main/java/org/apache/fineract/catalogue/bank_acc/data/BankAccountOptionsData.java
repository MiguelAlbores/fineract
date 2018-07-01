package org.apache.fineract.catalogue.bank_acc.data;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;

import java.util.List;

public class BankAccountOptionsData {
    List<EnumOptionData> bankAccountStatusOptions;
    List<EnumOptionData> bankAccountUseOptions;

    public BankAccountOptionsData(List<EnumOptionData> bankAccountStatusOptions, List<EnumOptionData> bankAccountUseOptions) {
        this.bankAccountStatusOptions = bankAccountStatusOptions;
        this.bankAccountUseOptions = bankAccountUseOptions;
    }
}
