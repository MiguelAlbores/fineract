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
package org.apache.fineract.catalogue.bank.domain;

import org.apache.fineract.catalogue.bank.api.BankJsonInputParams;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.useradministration.domain.AppUser;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "catalogue_bank")
public class Bank extends AbstractPersistableCustom<Long> {
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "bank_status_id")
    private Long bankStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    Date updatedAt;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = true)
    private AppUser updatedBy;

    @Column(name = "external_code", nullable = true, length = 255)
    private String externalCode;

    @Column(name = "bank_type_id")
    private Long bankType;

    public Bank(String name, Long bankStatus, AppUser createdBy, String externalCode, Long type) {
        this.name = name;
        this.bankStatus = bankStatus;
        this.createdBy = createdBy;
        this.externalCode = externalCode;
        this.createdAt = new Date();
        this.bankType = type;
    }

    public static Bank fromJson(final JsonCommand command, AppUser authUser) {
        final String name = command.stringValueOfParameterNamed(BankJsonInputParams.NAME.getValue());
        final Long bankStatusId = command.longValueOfParameterNamed(BankJsonInputParams.BANK_STATUS_ID.getValue());
        final String externalCode = command.stringValueOfParameterNamed(BankJsonInputParams.EXTERNAL_CODE.getValue());
        final Long type = command.longValueOfParameterNamed(BankJsonInputParams.TYPE.getValue());
        return new Bank(name, bankStatusId, authUser, externalCode, type);
    }

    public Map<String, Object> update(JsonCommand command, AppUser authUser) {
        this.updatedBy = authUser;
        this.updatedAt = new Date();
        final Map<String, Object> actualChanges = new LinkedHashMap<>(3);

        final String nameParamName = BankJsonInputParams.NAME.getValue();
        if (command.isChangeInStringParameterNamed(nameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(nameParamName);
            actualChanges.put(nameParamName, newValue);
            this.name = newValue;
        }

        final String externalCodeParamName = BankJsonInputParams.EXTERNAL_CODE.getValue();
        if (command.isChangeInStringParameterNamed(externalCodeParamName, this.externalCode)) {
            final String newValue = command.stringValueOfParameterNamed(externalCodeParamName);
            actualChanges.put(externalCodeParamName, newValue);
            this.externalCode = newValue;
        }

        final String bankStatusIdParamName = BankJsonInputParams.BANK_STATUS_ID.getValue();
        if (command.isChangeInLongParameterNamed(bankStatusIdParamName, this.bankStatus)) {
            final Long newValue = command.longValueOfParameterNamed(bankStatusIdParamName);
            actualChanges.put(bankStatusIdParamName, newValue);
            this.bankStatus = newValue;
        }

        final String typeParamName = BankJsonInputParams.TYPE.getValue();
        if (command.isChangeInLongParameterNamed(typeParamName, this.bankType)) {
            final Long newValue = command.longValueOfParameterNamed(typeParamName);
            actualChanges.put(typeParamName, newValue);
            this.bankType = newValue;
        }

        return actualChanges;
    }
}
