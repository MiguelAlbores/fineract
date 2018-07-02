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

package org.apache.fineract.catalogue.bank_acc.data;

import org.apache.fineract.accounting.glaccount.data.GLAccountData;
import org.apache.fineract.catalogue.bank.data.BankData;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.joda.time.DateTime;

public class BankAccountData {
    private final Long idBank;
    private final GLAccountData glAccount;
    private final String name;
    private final String externalCode;
    private final DateTime createdAt;
    private final DateTime updatedAt;
    private final Long createdBy;
    private final Long updatedBy;
    private final EnumOptionData accUse;
    private final EnumOptionData status;

    public BankAccountData(Long idBank, GLAccountData glAccount, String name, String externalCode, DateTime createdAt, DateTime updatedAt, Long createdBy, Long updatedBy, EnumOptionData accUse, EnumOptionData status) {
        this.idBank = idBank;
        this.glAccount = glAccount;
        this.name = name;
        this.externalCode = externalCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.accUse = accUse;
        this.status = status;
    }
}

