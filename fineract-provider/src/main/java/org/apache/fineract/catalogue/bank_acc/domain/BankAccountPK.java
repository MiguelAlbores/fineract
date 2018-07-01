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
