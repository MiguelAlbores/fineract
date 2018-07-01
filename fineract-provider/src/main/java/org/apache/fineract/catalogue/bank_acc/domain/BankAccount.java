package org.apache.fineract.catalogue.bank_acc.domain;

import org.apache.fineract.catalogue.bank_acc.api.BankAccountJsonInputParams;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.useradministration.domain.AppUser;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "bank_acc_account")
public class BankAccount {
    @EmbeddedId
    private BankAccountPK id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "external_code", nullable = true, length = 255)
    private String externalCode;

    @Column(name = "bank_acc_status_id")
    private Long bankAccountStatus;

    @Column(name = "acc_use_id")
    private Long use;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = true)
    private AppUser updatedBy;

    public BankAccount(BankAccountPK id, String name, String externalCode, Long bankAccountStatus, Long use, AppUser createdBy) {
        this.id = id;
        this.name = name;
        this.externalCode = externalCode;
        this.bankAccountStatus = bankAccountStatus;
        this.use = use;
        this.createdBy = createdBy;
    }

    public static BankAccount fromJson(JsonCommand command, AppUser currentUser) {
        final String name = command.stringValueOfParameterNamed(BankAccountJsonInputParams.NAME.getValue());
        final Long bankId = command.entityId();
        final Long glAccountId = command.longValueOfParameterNamed(BankAccountJsonInputParams.GL_ACCOUNT_ID.getValue());
        BankAccountPK pk = new BankAccountPK(bankId, glAccountId);
        final String externalCode = command.stringValueOfParameterNamed(BankAccountJsonInputParams.EXTERNAL_CODE.getValue());
        final Long use = command.longValueOfParameterNamed(BankAccountJsonInputParams.USE.getValue());
        final Long status = command.longValueOfParameterNamed(BankAccountJsonInputParams.BANK_ACCOUNT_STATUS_ID.getValue());
        return new BankAccount(pk, name, externalCode, status, use, currentUser);
    }

    public BankAccountPK getId() {
        return id;
    }

    public Map<String, Object> update(JsonCommand command, AppUser currentUser) {
        this.updatedBy = currentUser;
        final Map<String, Object> actualChanges = new LinkedHashMap<>(3);

        final String nameParamName = BankAccountJsonInputParams.NAME.getValue();
        if (command.isChangeInStringParameterNamed(nameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(nameParamName);
            actualChanges.put(nameParamName, newValue);
            this.name = newValue;
        }

        final String externalCodeParamName = BankAccountJsonInputParams.EXTERNAL_CODE.getValue();
        if (command.isChangeInStringParameterNamed(externalCodeParamName, this.externalCode)) {
            final String newValue = command.stringValueOfParameterNamed(externalCodeParamName);
            actualChanges.put(externalCodeParamName, newValue);
            this.externalCode = newValue;
        }

        final String bankStatusIdParamName = BankAccountJsonInputParams.BANK_ACCOUNT_STATUS_ID.getValue();
        if (command.isChangeInLongParameterNamed(bankStatusIdParamName, this.bankAccountStatus)) {
            final Long newValue = command.longValueOfParameterNamed(bankStatusIdParamName);
            actualChanges.put(bankStatusIdParamName, newValue);
            this.bankAccountStatus = newValue;
        }

        final String useParamName = BankAccountJsonInputParams.USE.getValue();
        if (command.isChangeInLongParameterNamed(useParamName, this.use)) {
            final Long newValue = command.longValueOfParameterNamed(useParamName);
            actualChanges.put(useParamName, newValue);
            this.use = newValue;
        }

        return actualChanges;
    }
}
