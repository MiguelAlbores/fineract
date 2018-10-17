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

package org.apache.fineract.portfolio.loanaccount.data;
import org.apache.fineract.portfolio.loanproduct.data.LoanProductTaxComponentData;

import java.math.BigDecimal;

public class LoanTaxData {
    private Long loanId;
    private BigDecimal amount;
    private LoanProductTaxComponentData loanProductTaxComponent;

    public LoanTaxData(Long loanId, BigDecimal amount, LoanProductTaxComponentData loanProductTaxComponent) {
        this.loanId = loanId;
        this.amount = amount;
        this.loanProductTaxComponent = loanProductTaxComponent;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LoanProductTaxComponentData getLoanProductTaxComponent() {
        return loanProductTaxComponent;
    }

    public void setLoanProductTaxComponent(LoanProductTaxComponentData loanProductTaxComponent) {
        this.loanProductTaxComponent = loanProductTaxComponent;
    }
}

