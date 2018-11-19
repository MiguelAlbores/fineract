package org.apache.fineract.organisation.loan_bonus_configuration.service;

import org.apache.fineract.organisation.loan_bonus_configuration.data.LoanBonusData;

public interface LoanBonusReadService {
    LoanBonusData getLoanBonusDataFromLoan(Long loanId);
}
