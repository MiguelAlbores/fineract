package org.apache.fineract.organisation.loan_bonus_configuration.service;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusCollectHistory;

public interface LoanBonusWriteService {
    CommandProcessingResult collectBonus(Long loanId, JsonCommand command);
    CommandProcessingResult cancelBonus(Long loanId, JsonCommand command);
}
