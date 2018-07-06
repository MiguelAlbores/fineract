package org.apache.fineract.organisation.loan_bonus_configuration.service;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;

public interface LoanBonusConfigurationWritePlatformService {
    CommandProcessingResult createLoanBonusConfig(JsonCommand command);

    CommandProcessingResult updateLoanBonusConfig(Long id, JsonCommand command);
}
