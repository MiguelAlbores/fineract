package org.apache.fineract.organisation.loan_bonus_configuration.service;

import org.apache.fineract.accounting.glaccount.domain.GLAccount;
import org.apache.fineract.accounting.glaccount.domain.GLAccountRepository;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.organisation.loan_bonus_configuration.api.LoanBonusConfigJsonInputParameters;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusConfiguration;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusConfigurationCycle;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusConfigurationRepository;
import org.apache.fineract.organisation.loan_bonus_configuration.exception.LoanBonusConfigurationNotFoundException;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoanBonusConfigurationWritePlatformServiceImpl  implements LoanBonusConfigurationWritePlatformService{

    private final LoanBonusConfigurationRepository loanBonusConfigurationRepository;
    private final GLAccountRepository glAccountRepository;
    private final PlatformSecurityContext context;

    @Autowired
    public LoanBonusConfigurationWritePlatformServiceImpl(LoanBonusConfigurationRepository loanBonusConfigurationRepository, GLAccountRepository glAccountRepository, PlatformSecurityContext context) {
        this.loanBonusConfigurationRepository = loanBonusConfigurationRepository;
        this.glAccountRepository = glAccountRepository;
        this.context = context;
    }

    @Override
    public CommandProcessingResult createLoanBonusConfig(JsonCommand command) {
        try {
            System.out.println(command.toString());
            final AppUser currentUser = this.context.authenticatedUser();
            LoanBonusConfiguration loanBonusConfiguration = LoanBonusConfiguration.fromJson(command, currentUser);
            if(loanBonusConfiguration.getCycles() != null)
            for(LoanBonusConfigurationCycle cycle : loanBonusConfiguration.getCycles()){
                cycle.setLoanBonusConfig(loanBonusConfiguration);
            }
            this.loanBonusConfigurationRepository.saveAndFlush(loanBonusConfiguration);

            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(loanBonusConfiguration.getId()).build();
        } catch (final DataIntegrityViolationException dve) {
            return CommandProcessingResult.empty();
        }
    }

    @Override
    public CommandProcessingResult updateLoanBonusConfig(Long id, JsonCommand command) {
        try {
            final AppUser currentUser = this.context.authenticatedUser();
            final LoanBonusConfiguration loanBonusConfiguration = this.loanBonusConfigurationRepository.findOne(id);
            if(loanBonusConfiguration == null) throw new LoanBonusConfigurationNotFoundException(id);

            final Map<String, Object> changesOnly = loanBonusConfiguration.update(command, currentUser);

            if(!changesOnly.isEmpty())
                this.loanBonusConfigurationRepository.saveAndFlush(loanBonusConfiguration);

            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(loanBonusConfiguration.getId()).with(changesOnly).build();
        } catch (final Exception dve) {
            return CommandProcessingResult.empty();
        }
    }
}
