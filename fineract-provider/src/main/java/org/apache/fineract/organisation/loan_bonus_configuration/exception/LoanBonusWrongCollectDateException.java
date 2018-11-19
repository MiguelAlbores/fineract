package org.apache.fineract.organisation.loan_bonus_configuration.exception;

import org.apache.fineract.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class LoanBonusWrongCollectDateException extends AbstractPlatformDomainRuleException {
    public LoanBonusWrongCollectDateException() {
        super("error.msg.loan.bonus.transactiondate.exceeddaysallowed", "Exceeds days allowed to collect the bonus", null);
    }
}
