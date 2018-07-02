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

package org.apache.fineract.catalogue.bank_acc.api;

import org.apache.fineract.catalogue.bank_acc.data.BankAccountData;
import org.apache.fineract.catalogue.bank_acc.data.BankAccountOptionsData;
import org.apache.fineract.catalogue.bank_acc.service.BankAccountReadPlatformService;
import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/bank/{bankId}/accounts")
@Component
@Scope("singleton")
public class BankAccoutApiResource {

    private final BankAccountReadPlatformService readPlatformService;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final DefaultToApiJsonSerializer<BankAccountData> apiJsonSerializerService;
    private final DefaultToApiJsonSerializer<BankAccountOptionsData> apiOptionsJsonSerializerService;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

    @Autowired
    public BankAccoutApiResource(BankAccountReadPlatformService readPlatformService, ApiRequestParameterHelper apiRequestParameterHelper, DefaultToApiJsonSerializer<BankAccountData> apiJsonSerializerService, DefaultToApiJsonSerializer<BankAccountOptionsData> apiOptionsJsonSerializerService, PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
        this.readPlatformService = readPlatformService;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.apiJsonSerializerService = apiJsonSerializerService;
        this.apiOptionsJsonSerializerService = apiOptionsJsonSerializerService;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAllBankAccounts(@Context final UriInfo uriInfo, @PathParam("bankId") final Long bankId){
        List<BankAccountData> bank_accounts = this.readPlatformService.getAllAccountsByBank(bankId);
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.apiJsonSerializerService.serialize(settings, bank_accounts, BankAccountJsonOutputParams.getAllValues());

    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createBankAccount(final String jsonRequestBody, @PathParam("bankId") final Long bankId) {
        final CommandWrapper commandRequest = new CommandWrapperBuilder() //
                .createBankAccount(bankId)
                .withJson(jsonRequestBody) //
                .build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.apiJsonSerializerService.serialize(result);
    }

    @GET
    @Path("{bankAccountId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getBankAccount(@PathParam("bankAccountId") final Long bankAccountId, @PathParam("bankId") final Long bankId, @Context final UriInfo uriInfo) {
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());

        BankAccountData bank = this.readPlatformService.getAccount(bankAccountId, bankId);
        return this.apiJsonSerializerService.serialize(settings, bank, BankAccountJsonOutputParams.getAllValues());
    }

    @PUT
    @Path("{bankAccountId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateBankAccount(@PathParam("bankAccountId") final Long bankAccountId, @PathParam("bankId") final Long bankId, final String apiRequestBodyAsJson) {
        final CommandWrapper commandRequest = new CommandWrapperBuilder().updateBankAccount(bankId, bankAccountId).withJson(apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.apiJsonSerializerService.serialize(result);
    }

    @DELETE
    @Path("{bankAccountId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String deleteBankAccount(@PathParam("bankAccountId") final Long bankAccountId, @PathParam("bankId") final Long bankId, final String apiRequestBodyAsJson) {
        final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteBankAccount(bankId, bankAccountId).withJson(apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.apiJsonSerializerService.serialize(result);
    }

    @GET
    @Path("template")
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveNewAccountDetails(@Context final UriInfo uriInfo) {
        BankAccountOptionsData bankOptions = this.readPlatformService.getOptions();
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.apiOptionsJsonSerializerService.serialize(settings, bankOptions, BankAccountOptionsJsonOutputParams.getAllValues());
    }
}
