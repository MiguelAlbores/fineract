package org.apache.fineract.catalogue.bank_acc.service;

import org.apache.fineract.catalogue.bank_acc.data.BankAccountData;
import org.apache.fineract.catalogue.bank_acc.data.BankAccountOptionsData;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;

import java.util.List;

public interface BankAccountReadPlatformService {
    List<BankAccountData> getAllAccountsByBank(Long bankId);
    BankAccountData getAccount(Long idGlAccount, Long bankId);
    List<EnumOptionData> getStatusOptions();
    List<EnumOptionData> getUseOptions();
    BankAccountOptionsData getOptions();
}