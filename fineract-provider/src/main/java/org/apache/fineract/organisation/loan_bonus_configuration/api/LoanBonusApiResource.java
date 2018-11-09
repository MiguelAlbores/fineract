package org.apache.fineract.organisation.loan_bonus_configuration.api;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.exception.UnrecognizedQueryParamException;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.organisation.loan_bonus_configuration.data.LoanBonusData;
import org.apache.fineract.organisation.loan_bonus_configuration.service.LoanBonusReadService;
import org.apache.fineract.organisation.loan_bonus_configuration.service.LoanBonusWriteService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/loan/{loanId}/bonus")
@Component
@Scope("singleton")
public class LoanBonusApiResource {
    private final DefaultToApiJsonSerializer<LoanBonusData> apiJsonSerializerService;
    private final LoanBonusReadService loanBonusReadService;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

    @Autowired
    public LoanBonusApiResource(DefaultToApiJsonSerializer<LoanBonusData> apiJsonSerializerService,
                                LoanBonusReadService loanBonusReadService,
                                PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
        this.apiJsonSerializerService = apiJsonSerializerService;
        this.loanBonusReadService = loanBonusReadService;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    }

    private boolean is(final String commandParam, final String commandValue) {
        return StringUtils.isNotBlank(commandParam) && commandParam.trim().equalsIgnoreCase(commandValue);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveBonusDataForLoan(@PathParam("loanId") final Long loanId){
        return this.apiJsonSerializerService.serialize(this.loanBonusReadService.getLoanBonusDataFromLoan(loanId));
    }

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public String collectBonusForLoan(@PathParam("loanId") final Long loanId,
                                      @QueryParam("command") final String commandParam,
                                      final String apiRequestBodyAsJson){
        final CommandWrapperBuilder builder = new CommandWrapperBuilder().withJson(apiRequestBodyAsJson);

        CommandProcessingResult result = null;
        if (is(commandParam, "collect")) {
            final CommandWrapper commandRequest = builder.loanBonusCollect(loanId).build();
            result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        } else if (is(commandParam, "cancel")) {
            final CommandWrapper commandRequest = builder.loanBonusCancel(loanId).build();
            result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        }

        if (result == null) { throw new UnrecognizedQueryParamException("command", commandParam); }

        return this.apiJsonSerializerService.serialize(result);
    }

}
