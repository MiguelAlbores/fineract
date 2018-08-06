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
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/banks/accounts")
@Component
@Scope("singleton")
public class BankAccountsApiResources {

    private final BankAccountReadPlatformService readPlatformService;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final DefaultToApiJsonSerializer<BankAccountData> apiJsonSerializerService;
    private final DefaultToApiJsonSerializer<BankAccountOptionsData> apiOptionsJsonSerializerService;

    @Autowired
    public BankAccountsApiResources(BankAccountReadPlatformService readPlatformService, ApiRequestParameterHelper apiRequestParameterHelper, DefaultToApiJsonSerializer<BankAccountData> apiJsonSerializerService, DefaultToApiJsonSerializer<BankAccountOptionsData> apiOptionsJsonSerializerService) {
        this.readPlatformService = readPlatformService;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.apiJsonSerializerService = apiJsonSerializerService;
        this.apiOptionsJsonSerializerService = apiOptionsJsonSerializerService;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAllBankAccounts(@Context final UriInfo uriInfo, @QueryParam("usage") final Integer usage){
        List<BankAccountData> bank_accounts = this.readPlatformService.getAllAccounts(usage);
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.apiJsonSerializerService.serialize(settings, bank_accounts, BankAccountJsonOutputParams.getAllValues());

    }
}
