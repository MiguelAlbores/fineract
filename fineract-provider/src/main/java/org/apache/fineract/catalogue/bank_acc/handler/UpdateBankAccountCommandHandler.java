package org.apache.fineract.catalogue.bank_acc.handler;

import org.apache.fineract.catalogue.bank_acc.service.BankAccountWritePlatformService;
import org.apache.fineract.commands.annotation.CommandType;
import org.apache.fineract.commands.handler.NewCommandSourceHandler;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommandType(entity = "BANK_ACCOUNT", action = "UPDATE")
public class UpdateBankAccountCommandHandler implements NewCommandSourceHandler {

    private final BankAccountWritePlatformService writePlatformService;

    @Autowired
    public UpdateBankAccountCommandHandler(final BankAccountWritePlatformService writePlatformService) {
        this.writePlatformService = writePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {
        return this.writePlatformService.updateBankAccount(command.entityId(), command.subentityId(), command);
    }
}
