package org.apache.fineract.organisation.loan_bonus_configuration.data;

import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusConfigurationCycle;
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
