package org.apache.fineract.organisation.loan_bonus_configuration.handler;

import org.apache.fineract.commands.annotation.CommandType;
import org.apache.fineract.commands.handler.NewCommandSourceHandler;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.organisation.loan_bonus_configuration.service.LoanBonusWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommandType(entity = "LOAN_BONUS", action = "CANCEL")
public class CancelLoanBonusCommandHandler implements NewCommandSourceHandler {
    private final LoanBonusWriteService loanBonusWriteService;

    @Autowired
    public CancelLoanBonusCommandHandler(LoanBonusWriteService loanBonusWriteService) {
        this.loanBonusWriteService = loanBonusWriteService;
    }

    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {
        return this.loanBonusWriteService.cancelBonus(command.getLoanId(), command);
    }
}
