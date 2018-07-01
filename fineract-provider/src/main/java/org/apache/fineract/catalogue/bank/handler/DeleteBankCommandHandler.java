package org.apache.fineract.catalogue.bank.handler;

import org.apache.fineract.catalogue.bank.service.BankWritePlatformService;
import org.apache.fineract.commands.annotation.CommandType;
import org.apache.fineract.commands.handler.NewCommandSourceHandler;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommandType(entity = "BANK", action = "DELETE")
public class DeleteBankCommandHandler implements NewCommandSourceHandler {

    private final BankWritePlatformService writePlatformService;

    @Autowired
    public DeleteBankCommandHandler(final BankWritePlatformService writePlatformService) {
        this.writePlatformService = writePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.writePlatformService.deleteBank(command.entityId(), command);
    }
}
