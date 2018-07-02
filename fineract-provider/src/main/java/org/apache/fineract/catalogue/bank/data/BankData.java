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
package org.apache.fineract.catalogue.bank.data;


import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.joda.time.DateTime;

public class BankData {
    private final Long id;
    private final String name;
    private final DateTime createdAt;
    private final DateTime updatedAt;
    private final String externalCode;
    private final Long createdBy;
    private final Long updatedBy;
    private final EnumOptionData type;
    private final EnumOptionData status;

    public BankData(Long id, String name, DateTime createdAt, DateTime updatedAt, String externalCode, Long createdBy, Long updatedBy, EnumOptionData type, EnumOptionData status) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.externalCode = externalCode;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.type = type;
        this.status = status;
    }
}
