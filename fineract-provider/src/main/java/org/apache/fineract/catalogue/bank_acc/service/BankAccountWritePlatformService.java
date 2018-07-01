package org.apache.fineract.catalogue.bank_acc.service;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;

public interface BankAccountWritePlatformService {

    CommandProcessingResult createBankAccount(Long bankId, JsonCommand command);

    CommandProcessingResult updateBankAccount(Long bankId, Long glAccountId, JsonCommand command);

    CommandProcessingResult deleteBankAccount(Long bankId, Long glAccountId, JsonCommand command);
}
