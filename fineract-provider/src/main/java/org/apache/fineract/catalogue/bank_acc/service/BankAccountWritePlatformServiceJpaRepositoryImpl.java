package org.apache.fineract.catalogue.bank_acc.service;

import org.apache.fineract.catalogue.bank.serialization.BankCommandFromApiJsonDeserializer;
import org.apache.fineract.catalogue.bank_acc.domain.BankAccount;
import org.apache.fineract.catalogue.bank_acc.domain.BankAccountPK;
import org.apache.fineract.catalogue.bank_acc.domain.BankAccountRepository;
import org.apache.fineract.catalogue.bank_acc.exception.BankAccountNotFoundException;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BankAccountWritePlatformServiceJpaRepositoryImpl implements BankAccountWritePlatformService{
    private final BankCommandFromApiJsonDeserializer fromApiJsonDeserializer;
    private final BankAccountRepository bankAccountRepository;
    private final PlatformSecurityContext context;

    @Autowired
    public BankAccountWritePlatformServiceJpaRepositoryImpl(BankCommandFromApiJsonDeserializer fromApiJsonDeserializer, BankAccountRepository bankAccountRepository, PlatformSecurityContext context) {
        this.fromApiJsonDeserializer = fromApiJsonDeserializer;
        this.bankAccountRepository = bankAccountRepository;
        this.context = context;
    }

    @Override
    public CommandProcessingResult createBankAccount(Long bankId, JsonCommand command) {
        try {
            final AppUser currentUser = this.context.authenticatedUser();
            BankAccount bankAccount = BankAccount.fromJson(command, currentUser);
            this.bankAccountRepository.saveAndFlush(bankAccount);

            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(bankAccount.getId().getIdBankAccount()).build();
        } catch (final DataIntegrityViolationException dve) {
            return CommandProcessingResult.empty();
        }
    }

    @Override
    public CommandProcessingResult updateBankAccount(Long bankId, Long glAccountId, JsonCommand command) {
        try {
            final AppUser currentUser = this.context.authenticatedUser();
            BankAccountPK idBankAccount = new BankAccountPK(bankId, glAccountId);
            final BankAccount bankAccountForUpdate = this.bankAccountRepository.findOne(idBankAccount);
            if(bankAccountForUpdate == null) throw new BankAccountNotFoundException(bankId, glAccountId);
            final Map<String, Object> changesOnly = bankAccountForUpdate.update(command, currentUser);

            if(!changesOnly.isEmpty())
                this.bankAccountRepository.saveAndFlush(bankAccountForUpdate);

            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(bankAccountForUpdate.getId().getIdBankAccount()).with(changesOnly).build();
        } catch (final Exception dve) {
            return CommandProcessingResult.empty();
        }
    }

    @Override
    public CommandProcessingResult deleteBankAccount(Long bankId, Long glAccountId, JsonCommand command) {
        BankAccountPK idBankAccount = new BankAccountPK(bankId, glAccountId);
        this.bankAccountRepository.delete(idBankAccount);

        return new CommandProcessingResultBuilder() //
                .withCommandId(command.commandId()) //
                .withEntityId(bankId) //
                .build();
    }
}
