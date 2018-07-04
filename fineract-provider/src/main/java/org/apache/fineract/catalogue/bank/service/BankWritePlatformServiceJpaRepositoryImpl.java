/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.fineract.catalogue.bank.service;

import org.apache.fineract.accounting.glaccount.service.GLAccountWritePlatformServiceJpaRepositoryImpl;
import org.apache.fineract.catalogue.bank.domain.Bank;
import org.apache.fineract.catalogue.bank.domain.BankRepository;
import org.apache.fineract.catalogue.bank.exception.BankNotFoundException;
import org.apache.fineract.catalogue.bank.serialization.BankCommandFromApiJsonDeserializer;
import org.apache.fineract.catalogue.bank_acc.domain.BankAccountRepository;
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
    private final BankAccountRepository bankAccountRepository;
    private final PlatformSecurityContext context;

    @Autowired
    public BankWritePlatformServiceJpaRepositoryImpl(BankCommandFromApiJsonDeserializer fromApiJsonDeserializer, BankRepository bankRepository, BankAccountRepository bankAccountRepository, PlatformSecurityContext context) {
        this.fromApiJsonDeserializer = fromApiJsonDeserializer;
        this.bankRepository = bankRepository;
        this.bankAccountRepository = bankAccountRepository;
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
        this.bankAccountRepository.deleteByBankId(bankId);
        this.bankRepository.delete(bankId);

        return new CommandProcessingResultBuilder() //
                .withCommandId(command.commandId()) //
                .withEntityId(bankId) //
                .build();
    }
}
