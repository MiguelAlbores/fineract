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

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.data.jpa.domain.AbstractAuditable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "loan_bonus_configuration_cycles")
public class LoanBonusConfigurationCycle extends AbstractPersistableCustom<Long> {
    @Column(name = "from_value")
    private Long fromValue;

    @Column(name = "to_value")
    private Long toValue;

    @Column(name = "percent_value")
    private Double percentValue;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="id_loan_bonus_configuration")
    private LoanBonusConfiguration loanBonusConfig;

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

    public LoanBonusConfigurationCycle(Long fromValue, Long toValue, Double percentValue, AppUser createdBy) {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.percentValue = percentValue;
        this.createdAt = new Date();
        this.createdBy = createdBy;
    }

    @Override
    protected void setId(Long id) {
        super.setId(id);
    }

    public Long getFromValue() {
        return fromValue;
    }

    public void setFromValue(Long fromValue) {
        this.fromValue = fromValue;
    }

    public Long getToValue() {
        return toValue;
    }

    public void setToValue(Long toValue) {
        this.toValue = toValue;
    }

    public Double getPercentValue() {
        return percentValue;
    }

    public void setPercentValue(Double percentValue) {
        this.percentValue = percentValue;
    }

    public LoanBonusConfiguration getLoanBonusConfig() {
        return loanBonusConfig;
    }

    public void setLoanBonusConfig(LoanBonusConfiguration loanBonusConfig) {
        this.loanBonusConfig = loanBonusConfig;
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
}
