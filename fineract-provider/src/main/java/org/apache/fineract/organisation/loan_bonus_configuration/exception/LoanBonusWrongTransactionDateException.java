package org.apache.fineract.organisation.loan_bonus_configuration.exception;

import org.apache.fineract.infrastructure.core.exception.PlatformDataIntegrityException;

public class LoanBonusWrongTransactionDateException extends PlatformDataIntegrityException {

    public LoanBonusWrongTransactionDateException() {
        super("error.msg.loan.bonus.transactiondate.invalid", "transaction date can't be before closedOn date for Loan", "transactionDate", null);
    }
}
