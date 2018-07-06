package org.apache.fineract.organisation.loan_bonus_configuration.service;

import org.apache.fineract.accounting.glaccount.service.GLAccountReadPlatformService;
import org.apache.fineract.accounting.journalentry.data.JournalEntryAssociationParametersData;
import org.apache.fineract.organisation.loan_bonus_configuration.data.LoanBonusConfigurationCycleData;
import org.apache.fineract.organisation.loan_bonus_configuration.data.LoanBonusConfigurationData;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusConfiguration;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusConfigurationCycle;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusConfigurationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanBonusConfigurationReadPlatformServiceImpl implements LoanBonusConfigurationReadPlatformService{

    private final LoanBonusConfigurationRepository loanBonusConfigurationRepository;
    private final GLAccountReadPlatformService glAccountReadPlatformService;

    @Autowired
    public LoanBonusConfigurationReadPlatformServiceImpl(LoanBonusConfigurationRepository loanBonusConfigurationRepository, GLAccountReadPlatformService glAccountReadPlatformService) {
        this.loanBonusConfigurationRepository = loanBonusConfigurationRepository;
        this.glAccountReadPlatformService = glAccountReadPlatformService;
    }

    @Override
    public LoanBonusConfigurationData getConfiguration() {
        List<LoanBonusConfiguration> configs = this.loanBonusConfigurationRepository.getConfiguration(new PageRequest(0,1)).getContent();
        if(configs != null && !configs.isEmpty() && configs.size() == 1)
            return convertToData(configs.get(0));
        return null;
    }

    private LoanBonusConfigurationData convertToData(LoanBonusConfiguration loanBonusConfiguration) {
        List<LoanBonusConfigurationCycleData> cycles = new ArrayList<>();
        if(loanBonusConfiguration.getCycles() != null && !loanBonusConfiguration.getCycles().isEmpty()){
            for(LoanBonusConfigurationCycle cycle : loanBonusConfiguration.getCycles()){
                cycles.add(new LoanBonusConfigurationCycleData( cycle.getId(),
                        cycle.getFromValue(), cycle.getToValue(), cycle.getPercentValue(),
                        cycle.getLoanBonusConfig().getId(),
                        new DateTime(cycle.getCreatedAt()),
                        new DateTime(cycle.getUpdatedAt()),
                        cycle.getCreatedBy().getId(),
                        cycle.getUpdatedBy() != null ? cycle.getUpdatedBy().getId():null
                ));
            }
        }

        LoanBonusConfigurationData data = new LoanBonusConfigurationData( loanBonusConfiguration.getId(),
                loanBonusConfiguration.getDaysInArrear(),
                loanBonusConfiguration.getDaysToCollectBonus(),
                glAccountReadPlatformService.retrieveGLAccountById(loanBonusConfiguration.getGlAccountToDebit().longValue(), new JournalEntryAssociationParametersData()),
                glAccountReadPlatformService.retrieveGLAccountById(loanBonusConfiguration.getGlAccountToCredit().longValue(), new JournalEntryAssociationParametersData()),
                cycles,
                new DateTime(loanBonusConfiguration.getCreatedAt()),
                new DateTime(loanBonusConfiguration.getUpdatedAt()),
                loanBonusConfiguration.getCreatedBy().getId(),
                loanBonusConfiguration.getUpdatedBy() != null ? loanBonusConfiguration.getUpdatedBy().getId():null
                );

        return data;
    }
}
