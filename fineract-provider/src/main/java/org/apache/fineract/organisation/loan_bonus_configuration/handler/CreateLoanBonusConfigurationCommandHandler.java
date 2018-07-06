package org.apache.fineract.organisation.loan_bonus_configuration.handler;

import org.apache.fineract.commands.annotation.CommandType;
import org.apache.fineract.commands.handler.NewCommandSourceHandler;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.organisation.loan_bonus_configuration.service.LoanBonusConfigurationWritePlatformServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommandType(entity = "LOAN_BONUS_CONFIG", action = "CREATE")
public class CreateLoanBonusConfigurationCommandHandler implements NewCommandSourceHandler {

    private final LoanBonusConfigurationWritePlatformServiceImpl writePlatformService;

    @Autowired
    public CreateLoanBonusConfigurationCommandHandler(final LoanBonusConfigurationWritePlatformServiceImpl writePlatformService) {
        this.writePlatformService = writePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.writePlatformService.createLoanBonusConfig(command);
    }
}
