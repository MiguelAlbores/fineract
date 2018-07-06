package org.apache.fineract.organisation.loan_bonus_configuration.data;

import org.apache.fineract.accounting.glaccount.data.GLAccountData;
import org.joda.time.DateTime;

import java.util.List;

public class LoanBonusConfigurationData {
    private final Long id;
    private final Long daysInArrear;
    private final Long daysToCollectBonus;
    private final GLAccountData glAccountToDebit;
    private final GLAccountData glAccountToCredit;
    private final List<LoanBonusConfigurationCycleData> cycles;
    private final DateTime createdAt;
    private final DateTime updatedAt;
    private final Long createdBy;
    private final Long updatedBy;

    public LoanBonusConfigurationData(Long id, Long daysInArrear, Long daysToCollectBonus, GLAccountData glAccountToDebit, GLAccountData glAccountToCredit, List<LoanBonusConfigurationCycleData> cycles, DateTime createdAt, DateTime updatedAt, Long createdBy, Long updatedBy) {
        this.id = id;
        this.daysInArrear = daysInArrear;
        this.daysToCollectBonus = daysToCollectBonus;
        this.glAccountToDebit = glAccountToDebit;
        this.glAccountToCredit = glAccountToCredit;
        this.cycles = cycles;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
