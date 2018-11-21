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

import org.joda.time.LocalDate;

import java.math.BigDecimal;

public class LoanBonusData {
	private final boolean isCollectable;
	private final boolean collected;
	private final LocalDate collectedDate;
	private final LocalDate canceledDate;
	private final boolean canceled;
	private final Long daysInArrear;
	private final Long daysInArrearAllowed;
	private final Long daysToCollectBonus;
	private final Integer loanCounter;
	private final BigDecimal expectedBonusAmount;
	private final String reason;
	private final LoanBonusConfigurationCycleData loanBonusCycleApplied;

	public LoanBonusData(boolean isCollectable, boolean collected, LocalDate collectedDate, LocalDate canceledDate, boolean canceled, Long daysInArrear, Long daysInArrearAllowed, Long daysToCollectBonus, Integer loanCounter, BigDecimal expectedBonusAmount, String reason, LoanBonusConfigurationCycleData loanBonusCycleApplied) {
		this.isCollectable = isCollectable;
		this.collected = collected;
		this.collectedDate = collectedDate;
		this.canceledDate = canceledDate;
		this.canceled = canceled;
		this.daysInArrear = daysInArrear;
		this.daysInArrearAllowed = daysInArrearAllowed;
		this.daysToCollectBonus = daysToCollectBonus;
		this.loanCounter = loanCounter;
		this.expectedBonusAmount = expectedBonusAmount;
		this.reason = reason;
		this.loanBonusCycleApplied = loanBonusCycleApplied;
	}
}
