package org.apache.fineract.catalogue.bank.data;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;

import java.util.List;

public class BankOptionsData {
    List<EnumOptionData> bankStatusOptions;
    List<EnumOptionData> bankTypeOptions;

    public BankOptionsData(List<EnumOptionData> bankStatusOptions, List<EnumOptionData> bankTypeOptions) {
        this.bankStatusOptions = bankStatusOptions;
        this.bankTypeOptions = bankTypeOptions;
    }
}
