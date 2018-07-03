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

package org.apache.fineract.catalogue.bank_acc.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BankAccountPK implements Serializable {

    @Column(name = "id_bank")
    private Long idBank;
    @Column(name = "id_gl_account")
    private Long idBankAccount;

    public BankAccountPK() {
    }

    public BankAccountPK(Long idBank, Long idBankAccount) {
        this.idBank = idBank;
        this.idBankAccount = idBankAccount;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof BankAccountPK)) return false;
        BankAccountPK that = (BankAccountPK) obj;
        return Objects.equals(this.idBank, that.idBank) && Objects.equals(this.idBankAccount, that.idBankAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idBankAccount,this.idBank);
    }

    public Long getIdBankAccount() {
        return idBankAccount;
    }

    public void setIdBankAccount(Long idBankAccount) {
        this.idBankAccount = idBankAccount;
    }

    public void setIdBank(Long idBank) {
        this.idBank = idBank;
    }
}
