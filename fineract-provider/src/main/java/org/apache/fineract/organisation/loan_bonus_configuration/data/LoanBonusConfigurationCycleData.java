package org.apache.fineract.organisation.loan_bonus_configuration.data;

import org.joda.time.DateTime;

public class LoanBonusConfigurationCycleData {
    private final Long id;
    private final Long fromValue;
    private final Long toValue;
    private final Double percentValue;
    private final Long loanBonusConfigId;
    private final DateTime createdAt;
    private final DateTime updatedAt;
    private final Long createdBy;
    private final Long updatedBy;

    public LoanBonusConfigurationCycleData(Long id, Long fromValue, Long toValue, Double percentValue, Long loanBonusConfigId, DateTime createdAt, DateTime updatedAt, Long createdBy, Long updatedBy) {
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
