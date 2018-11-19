package org.apache.fineract.organisation.loan_bonus_configuration.domain;

import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "loan_bonus_collect_history")
public class LoanBonusCollectHistory extends AbstractPersistableCustom<Long> {
    @Column(name = "bonus_amount")
    private BigDecimal bonusAmount;
    @Column(name = "days_in_arrear")
    private Long daysInArrear;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "collected_date")
    private Date collectedDate;
    @Column(name = "is_collectable")
    private boolean isCollectable;
    @Column(name = "collected")
    private boolean collected;
    @Column(name = "canceled")
    private boolean canceled;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "canceled_date")
    private Date canceledDate;
    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;
    @Column(name = "comments")
    private String comments;
    @ManyToOne
    @JoinColumn(name = "loan_bonus_cycle_applied", nullable = false)
    private LoanBonusConfigurationCycle cycleApplied;

    public LoanBonusCollectHistory(BigDecimal bonusAmount, Long daysInArrear, Date collectedDate, boolean isCollectable, boolean collected, boolean canceled, Date canceledDate, Loan loan, String comments, LoanBonusConfigurationCycle cycleApplied) {
        this.bonusAmount = bonusAmount;
        this.daysInArrear = daysInArrear;
        this.collectedDate = collectedDate;
        this.isCollectable = isCollectable;
        this.collected = collected;
        this.canceled = canceled;
        this.canceledDate = canceledDate;
        this.loan = loan;
        this.comments = comments;
        this.cycleApplied = cycleApplied;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(BigDecimal bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Long getDaysInArrear() {
        return daysInArrear;
    }

    public void setDaysInArrear(Long daysInArrear) {
        this.daysInArrear = daysInArrear;
    }

    public Date getCollectedDate() {
        return collectedDate;
    }

    public void setCollectedDate(Date collectedDate) {
        this.collectedDate = collectedDate;
    }

    public boolean isCollectable() {
        return isCollectable;
    }

    public void setCollectable(boolean collectable) {
        isCollectable = collectable;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Date getCanceledDate() {
        return canceledDate;
    }

    public void setCanceledDate(Date canceledDate) {
        this.canceledDate = canceledDate;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LoanBonusConfigurationCycle getCycleApplied() {
        return cycleApplied;
    }

    public void setCycleApplied(LoanBonusConfigurationCycle cycleApplied) {
        this.cycleApplied = cycleApplied;
    }
}
