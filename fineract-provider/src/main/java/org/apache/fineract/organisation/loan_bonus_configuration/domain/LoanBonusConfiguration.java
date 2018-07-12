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

package org.apache.fineract.organisation.loan_bonus_configuration.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.fineract.accounting.glaccount.domain.GLAccount;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.organisation.loan_bonus_configuration.api.LoanBonusConfigJsonInputParameters;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProduct;
import org.apache.fineract.useradministration.domain.AppUser;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "loan_bonus_configuration")
public class LoanBonusConfiguration extends AbstractPersistableCustom<Long> {
    @Column(name = "days_in_arrear")
    private Long daysInArrear;

    @Column(name = "days_to_collect_bonus")
    private Long daysToCollectBonus;

    @JoinColumn(name = "gl_account_to_debit")
    private Long glAccountToDebit;

    @Column(name = "gl_account_to_credit")
    private Long glAccountToCredit;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true, mappedBy="loanBonusConfig", fetch = FetchType.EAGER)
    private List<LoanBonusConfigurationCycle> cycles = new ArrayList<LoanBonusConfigurationCycle>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastmodified_date")
    Date updatedAt;

    @ManyToOne(optional = true)
    @JoinColumn(name = "createdby_id", nullable = false)
    private AppUser createdBy;

    @ManyToOne(optional = true)
    @JoinColumn(name = "lastmodifiedby_id", nullable = true)
    private AppUser updatedBy;

    @Column(name = "loan_product_id")
    private Long loanProductId;

    public LoanBonusConfiguration(Long daysInArrear, Long daysToCollectBonus, Long glAccountToDebit, Long glAccountToCredit, List<LoanBonusConfigurationCycle> cycles, AppUser createdBy, Long loanProductId) {
        this.daysInArrear = daysInArrear;
        this.daysToCollectBonus = daysToCollectBonus;
        this.glAccountToDebit = glAccountToDebit;
        this.glAccountToCredit = glAccountToCredit;
        this.cycles = cycles;
        this.createdAt = new Date();
        this.createdBy = createdBy;
        this.loanProductId = loanProductId;
    }

    public static LoanBonusConfiguration fromJson(JsonCommand command, AppUser authUser) {
        final Long loanProductId = command.longValueOfParameterNamed(LoanBonusConfigJsonInputParameters.LOAN_PRODUCT_ID.getValue());
        final Long daysInArrear = command.longValueOfParameterNamed(LoanBonusConfigJsonInputParameters.DAYS_IN_ARREAR.getValue());
        final Long daysToCollectBonus = command.longValueOfParameterNamed(LoanBonusConfigJsonInputParameters.DAYS_TO_COLLECT_BONUS.getValue());
        final Long glAccountDebit = command.longValueOfParameterNamed(LoanBonusConfigJsonInputParameters.GL_ACCOUNT_TO_DEBIT.getValue());
        final Long glAccountCredit = command.longValueOfParameterNamed(LoanBonusConfigJsonInputParameters.GL_ACCOUNT_TO_CREDIT.getValue());
        System.out.println(glAccountCredit);
        JsonArray jsonArray = command.arrayOfParameterNamed(LoanBonusConfigJsonInputParameters.CYCLES_SETTINGS.getValue());
        List<LoanBonusConfigurationCycle> cycles = new ArrayList<LoanBonusConfigurationCycle>();
        if(jsonArray != null) {
            for (JsonElement pa : jsonArray) {
                JsonObject jsonObject = pa.getAsJsonObject();
                final Long fromValue = jsonObject.get(LoanBonusConfigJsonInputParameters.LOAN_CYCLE_FROM_VALUE.getValue()).getAsLong();
                final Long toValue = jsonObject.get(LoanBonusConfigJsonInputParameters.LOAN_CYCLE_TO_VALUE.getValue()).getAsLong();
                final Double percentValue = jsonObject.get(LoanBonusConfigJsonInputParameters.LOAN_CYCLE_PERCENT_VALUE.getValue()).getAsDouble();
                cycles.add(new LoanBonusConfigurationCycle(fromValue, toValue, percentValue, authUser));
            }
        }
        return new LoanBonusConfiguration(daysInArrear, daysToCollectBonus, glAccountDebit, glAccountCredit, cycles, authUser, loanProductId);
    }

    public Map<String, Object> update(JsonCommand command, AppUser updatedBy) {
        this.updatedBy = updatedBy;
        this.updatedAt = new Date();
        final Map<String, Object> actualChanges = new LinkedHashMap<>(6);

        final String daysInArrearParamName = LoanBonusConfigJsonInputParameters.DAYS_IN_ARREAR.getValue();
        if (command.isChangeInLongParameterNamed(daysInArrearParamName, this.daysInArrear)) {
            final Long newValue = command.longValueOfParameterNamed(daysInArrearParamName);
            actualChanges.put(daysInArrearParamName, newValue);
            this.daysInArrear = newValue;
        }

        final String loanProductIdParamName = LoanBonusConfigJsonInputParameters.LOAN_PRODUCT_ID.getValue();
        if (command.isChangeInLongParameterNamed(loanProductIdParamName, this.loanProductId)) {
            final Long newValue = command.longValueOfParameterNamed(loanProductIdParamName);
            actualChanges.put(loanProductIdParamName, newValue);
            this.loanProductId = newValue;
        }

        final String daysToCollectBonusParamName = LoanBonusConfigJsonInputParameters.DAYS_TO_COLLECT_BONUS.getValue();
        if (command.isChangeInLongParameterNamed(daysToCollectBonusParamName, this.daysToCollectBonus)) {
            final Long newValue = command.longValueOfParameterNamed(daysToCollectBonusParamName);
            actualChanges.put(daysToCollectBonusParamName, newValue);
            this.daysToCollectBonus = newValue;
        }

        final String glAccountToDebitParamName = LoanBonusConfigJsonInputParameters.GL_ACCOUNT_TO_DEBIT.getValue();
        if (command.isChangeInLongParameterNamed(glAccountToDebitParamName, this.glAccountToDebit)) {
            final Long newValue = command.longValueOfParameterNamed(glAccountToDebitParamName);
            actualChanges.put(glAccountToDebitParamName, newValue);
            this.glAccountToDebit = newValue;
        }

        final String glAccountToCreditParamName = LoanBonusConfigJsonInputParameters.GL_ACCOUNT_TO_CREDIT.getValue();
        if (command.isChangeInLongParameterNamed(glAccountToCreditParamName, this.glAccountToCredit)) {
            final Long newValue = command.longValueOfParameterNamed(glAccountToCreditParamName);
            actualChanges.put(glAccountToCreditParamName, newValue);
            this.glAccountToCredit = newValue;
        }

        JsonArray jsonArray = command.arrayOfParameterNamed(LoanBonusConfigJsonInputParameters.CYCLES_SETTINGS.getValue());
        if(jsonArray != null){
            List<LoanBonusConfigurationCycle> cycles = new ArrayList<LoanBonusConfigurationCycle>();
            for(JsonElement pa : jsonArray){
                JsonObject jsonObject = pa.getAsJsonObject();
                Long id = null;
                if(jsonObject.has("id")){
                    id = jsonObject.get("id").getAsLong();
                }
                final Long fromValue = jsonObject.get(LoanBonusConfigJsonInputParameters.LOAN_CYCLE_FROM_VALUE.getValue()).getAsLong();
                final Long toValue = jsonObject.get(LoanBonusConfigJsonInputParameters.LOAN_CYCLE_TO_VALUE.getValue()).getAsLong();
                final Double percentValue = jsonObject.get(LoanBonusConfigJsonInputParameters.LOAN_CYCLE_PERCENT_VALUE.getValue()).getAsDouble();
                LoanBonusConfigurationCycle cycle = new LoanBonusConfigurationCycle(fromValue, toValue, percentValue, updatedBy);
                cycle.setId(id);
                cycle.setLoanBonusConfig(this);
                cycles.add(cycle);
            }
            actualChanges.put("cycles", cycles.size());
            this.cycles = cycles;
        }
        return actualChanges;
    }

    public void setGlAccountToCredit(Long glAccountToCredit) {
        this.glAccountToCredit = glAccountToCredit;
    }

    public void setGlAccountToDebit(Long glAccountToDebit) {
        this.glAccountToDebit = glAccountToDebit;
    }

    public Long getDaysInArrear() {
        return daysInArrear;
    }

    public void setDaysInArrear(Long daysInArrear) {
        this.daysInArrear = daysInArrear;
    }

    public Long getDaysToCollectBonus() {
        return daysToCollectBonus;
    }

    public void setDaysToCollectBonus(Long daysToCollectBonus) {
        this.daysToCollectBonus = daysToCollectBonus;
    }

    public Long getGlAccountToDebit() {
        return glAccountToDebit;
    }

    public Long getGlAccountToCredit() {
        return glAccountToCredit;
    }

    public List<LoanBonusConfigurationCycle> getCycles() {
        return cycles;
    }

    public void setCycles(List<LoanBonusConfigurationCycle> cycles) {
        this.cycles = cycles;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public AppUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AppUser updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getLoanProductId() {
        return loanProductId;
    }
}
