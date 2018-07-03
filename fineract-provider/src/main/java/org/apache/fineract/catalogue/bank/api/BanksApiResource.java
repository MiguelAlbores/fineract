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
package org.apache.fineract.catalogue.bank.api;

import org.apache.fineract.catalogue.bank.data.BankData;
import org.apache.fineract.catalogue.bank.data.BankOptionsData;
import org.apache.fineract.catalogue.bank.service.BankReadPlatformService;
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

@Path("/banks")
@Component
@Scope("singleton")
public class BanksApiResource {

    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final BankReadPlatformService bankReadPlatformService;
    private final DefaultToApiJsonSerializer<BankData> apiJsonSerializerService;
    private final DefaultToApiJsonSerializer<BankOptionsData> optionsapiJsonSerializerService;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;


    @Autowired
    public BanksApiResource(ApiRequestParameterHelper apiRequestParameterHelper, BankReadPlatformService bankReadPlatformService, DefaultToApiJsonSerializer<BankData> apiJsonSerializerService, DefaultToApiJsonSerializer<BankOptionsData> optionsapiJsonSerializerService, PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.bankReadPlatformService = bankReadPlatformService;
        this.apiJsonSerializerService = apiJsonSerializerService;
        this.optionsapiJsonSerializerService = optionsapiJsonSerializerService;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAllBanks(@Context final UriInfo uriInfo){
        List<BankData> banks = this.bankReadPlatformService.retrieveAllBanks();
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.apiJsonSerializerService.serialize(settings, banks, BankJsonOutputParams.getAllValues());

    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createBank(final String jsonRequestBody) {
        final CommandWrapper commandRequest = new CommandWrapperBuilder() //
                .createBank()
                .withJson(jsonRequestBody) //
                .build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.apiJsonSerializerService.serialize(result);
    }

    @GET
    @Path("{bankId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getBank(@PathParam("bankId") final Long bankId, @Context final UriInfo uriInfo) {
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());

        BankData bank = this.bankReadPlatformService.getBank(bankId);
        return this.apiJsonSerializerService.serialize(settings, bank, BankJsonOutputParams.getAllValues());
    }

    @PUT
    @Path("{bankId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateBank(@PathParam("bankId") final Long bankId, final String apiRequestBodyAsJson) {
        final CommandWrapper commandRequest = new CommandWrapperBuilder().updateBank(bankId).withJson(apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.apiJsonSerializerService.serialize(result);
    }

    @DELETE
    @Path("{bankId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String deleteBank(@PathParam("bankId") final Long bankId, final String apiRequestBodyAsJson) {
        final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteBank(bankId).withJson(apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.apiJsonSerializerService.serialize(result);
    }

    @GET
    @Path("template")
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveNewAccountDetails(@Context final UriInfo uriInfo) {
        BankOptionsData bankOptions = this.bankReadPlatformService.getOptions();
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.optionsapiJsonSerializerService.serialize(settings, bankOptions, BankOptionsJsonOutputParams.getAllValues());
    }
}
