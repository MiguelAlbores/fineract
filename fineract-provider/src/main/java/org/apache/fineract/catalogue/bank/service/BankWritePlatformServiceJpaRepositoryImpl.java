package org.apache.fineract.catalogue.bank.service;

import org.apache.fineract.accounting.glaccount.service.GLAccountWritePlatformServiceJpaRepositoryImpl;
import org.apache.fineract.catalogue.bank.domain.Bank;
import org.apache.fineract.catalogue.bank.domain.BankRepository;
import org.apache.fineract.catalogue.bank.exception.BankNotFoundException;
import org.apache.fineract.catalogue.bank.serialization.BankCommandFromApiJsonDeserializer;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.useradministration.domain.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class BankWritePlatformServiceJpaRepositoryImpl implements BankWritePlatformService{

    private final static Logger logger = LoggerFactory.getLogger(GLAccountWritePlatformServiceJpaRepositoryImpl.class);
    private final BankCommandFromApiJsonDeserializer fromApiJsonDeserializer;
    private final BankRepository bankRepository;
    private final PlatformSecurityContext context;

    @Autowired
    public BankWritePlatformServiceJpaRepositoryImpl(BankCommandFromApiJsonDeserializer fromApiJsonDeserializer,
                                                     BankRepository bankRepository,
                                                     PlatformSecurityContext context) {
        this.fromApiJsonDeserializer = fromApiJsonDeserializer;
        this.bankRepository = bankRepository;
        this.context = context;
    }

    @Transactional
    @Override
    public CommandProcessingResult createBank(JsonCommand command) {
        try {
            final AppUser currentUser = this.context.authenticatedUser();
            Bank bank = Bank.fromJson(command, currentUser);
            this.bankRepository.saveAndFlush(bank);

            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(bank.getId()).build();
        } catch (final DataIntegrityViolationException dve) {
            return CommandProcessingResult.empty();
        }
    }

    @Transactional
    @Override
    public CommandProcessingResult updateBank(Long bankId, JsonCommand command) {
        try {
            final AppUser currentUser = this.context.authenticatedUser();
            final Bank bankForUpdate = this.bankRepository.findOne(bankId);
            if(bankForUpdate == null) throw new BankNotFoundException(bankId);
            final Map<String, Object> changesOnly = bankForUpdate.update(command, currentUser);

            if(!changesOnly.isEmpty())
                this.bankRepository.saveAndFlush(bankForUpdate);

            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(bankForUpdate.getId()).with(changesOnly).build();
        } catch (final Exception dve) {
            return CommandProcessingResult.empty();
        }
    }

    @Transactional
    @Override
    public CommandProcessingResult deleteBank(Long bankId, JsonCommand command) {
        this.bankRepository.delete(bankId);

        return new CommandProcessingResultBuilder() //
                .withCommandId(command.commandId()) //
                .withEntityId(bankId) //
                .build();
    }
}
