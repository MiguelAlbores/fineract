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

import java.util.HashMap;
import java.util.Map;

public enum BankStatus {
    ACTIVE(1, "bankStatus.active"),
    INACTIVE(2, "bankStatus.inactive");

    private final long value;
    private final String code;

    BankStatus(long value, String code) {
        this.value = value;
        this.code = code;
    }

    public long getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    private static final Map<Long, BankStatus> intToEnumMap = new HashMap<>();

    static {
        for (final BankStatus type : BankStatus.values()) {
            intToEnumMap.put(type.value, type);
        }
    }

    public static BankStatus fromInt(final long i) {
        final BankStatus status = intToEnumMap.get(Long.valueOf(i));
        return status;
    }

    @Override
    public String toString() {
        return name().toString();
    }
}
