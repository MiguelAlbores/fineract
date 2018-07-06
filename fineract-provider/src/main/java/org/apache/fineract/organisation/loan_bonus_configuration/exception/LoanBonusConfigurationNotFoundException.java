package org.apache.fineract.organisation.loan_bonus_configuration.exception;

import org.apache.fineract.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

public class LoanBonusConfigurationNotFoundException extends AbstractPlatformResourceNotFoundException {
    public LoanBonusConfigurationNotFoundException(final Long id) {
        super("error.msg.loan.bonus.config.id.invalid", "Loan bonus configuration with identifier: " + id + " does not exist", id);
    }
}
