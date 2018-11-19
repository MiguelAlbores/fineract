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

package org.apache.fineract.organisation.loan_bonus_configuration.data;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class LoanBonusConfigurationCycleData {
    private final Long id;
    private final Long fromValue;
    private final Long toValue;
    private final BigDecimal percentValue;
    private final Long loanBonusConfigId;
    private final DateTime createdAt;
    private final DateTime updatedAt;
    private final Long createdBy;
    private final Long updatedBy;

    public LoanBonusConfigurationCycleData(Long id, Long fromValue, Long toValue, BigDecimal percentValue, Long loanBonusConfigId, DateTime createdAt, DateTime updatedAt, Long createdBy, Long updatedBy) {
        this.id = id;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.percentValue = percentValue;
        this.loanBonusConfigId = loanBonusConfigId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

}
