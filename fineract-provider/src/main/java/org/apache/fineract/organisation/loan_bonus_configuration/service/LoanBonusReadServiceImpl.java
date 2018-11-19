package org.apache.fineract.organisation.loan_bonus_configuration.service;

import org.apache.fineract.accounting.journalentry.domain.JournalEntry;
import org.apache.fineract.accounting.journalentry.domain.JournalEntryRepository;
import org.apache.fineract.infrastructure.core.service.ThreadLocalContextUtil;
import org.apache.fineract.infrastructure.jobs.annotation.CronTarget;
import org.apache.fineract.infrastructure.jobs.exception.JobExecutionException;
import org.apache.fineract.infrastructure.jobs.service.JobName;
import org.apache.fineract.organisation.loan_bonus_configuration.data.LoanBonusConfigurationData;
import org.apache.fineract.organisation.loan_bonus_configuration.data.LoanBonusData;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.*;
import org.apache.fineract.portfolio.loanaccount.domain.*;
import org.apache.fineract.scheduledjobs.service.ScheduledJobRunnerServiceImpl;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class LoanBonusReadServiceImpl implements LoanBonusReadService{
    private final static Logger logger = LoggerFactory.getLogger(ScheduledJobRunnerServiceImpl.class);
    private final LoanRepositoryWrapper loanRepository;
    private final LoanBonusConfigurationRepository loanBonusConfigurationRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final LoanBonusCollectHistoryRepository loanBonusCollectHistoryRepository;

    @Autowired
    public LoanBonusReadServiceImpl(LoanRepositoryWrapper loanRepository,
                                    LoanBonusConfigurationRepository loanBonusConfigurationRepository,
                                    JournalEntryRepository journalEntryRepository,
                                    LoanBonusCollectHistoryRepository loanBonusCollectHistoryRepository) {
        this.loanRepository = loanRepository;
        this.loanBonusConfigurationRepository = loanBonusConfigurationRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.loanBonusCollectHistoryRepository = loanBonusCollectHistoryRepository;
    }

    @Override
    public LoanBonusData getLoanBonusDataFromLoan(Long loanId) {
        LoanBonusCollectHistory loanBonusCollectHistory = this.loanBonusCollectHistoryRepository.findByLoan_Id(loanId);
        if(loanBonusCollectHistory != null) {
            LoanBonusConfiguration loanBonusConfiguration = loanBonusConfigurationRepository.getLoanBonusConfigurationByLoanProductId(loanBonusCollectHistory.getLoan().productId());

            return new LoanBonusData(loanBonusCollectHistory.isCollectable(),
                    loanBonusCollectHistory.isCollected(),
                    new LocalDate(loanBonusCollectHistory.getCollectedDate()),
                    new LocalDate(loanBonusCollectHistory.getCanceledDate()),
                    loanBonusCollectHistory.isCanceled(),
                    loanBonusCollectHistory.getDaysInArrear(),
                    loanBonusConfiguration.getDaysInArrear(),
                    loanBonusConfiguration.getDaysToCollectBonus(),
                    loanBonusCollectHistory.getLoan().getCurrentLoanCounter(),
                    loanBonusCollectHistory.getBonusAmount(),
                    loanBonusCollectHistory.getComments(),
                    loanBonusCollectHistory.getCycleApplied().toData());
        }

        return null;
    }

    @CronTarget(jobName = JobName.UPDATE_BONUS_FOR_LOANS)
    public void updateBonusForLoans() throws JobExecutionException {
        logger.debug("Running update bonus job...");
        List<Loan> loans = loanRepository.getAllLoansForUpdateBonus();
        for(Loan loan :loans){
            LoanBonusCollectHistory loanBonusCollectHistory = loanBonusCollectHistoryRepository.findByLoan_Id(loan.getId());
            if(loanBonusCollectHistory != null && (loanBonusCollectHistory.isCanceled() || loanBonusCollectHistory.isCollected()))
                continue;
            Integer loanCounter = loan.getCurrentLoanCounter();
            if(loanCounter == null )
                continue;
            LoanBonusConfigurationCycle cycleForLoan = null;
            LoanBonusConfiguration loanBonusConfiguration = loanBonusConfigurationRepository.getLoanBonusConfigurationByLoanProductId(loan.productId());
            if(loanBonusConfiguration == null)
                continue;
            for(LoanBonusConfigurationCycle cycle : loanBonusConfiguration.getCycles()){
                if(cycle.isInCycle(loanCounter))
                    cycleForLoan = cycle;
            }
            Long daysInArrearAllowed = loanBonusConfiguration.getDaysInArrear();
            Long daysToCollectBonus = loanBonusConfiguration.getDaysToCollectBonus();

            //calculate days in arrear
            Long daysinArrear = 0L;
            for (LoanRepaymentScheduleInstallment installment : loan.getRepaymentScheduleInstallments()){
                if(installment.getDueDate().toDate().after(LocalDate.now().toDate()))
                    break;
                if(installment.getObligationsMetOnDate() == null){
                    LocalDate now = LocalDate.now();
                    daysinArrear += Days.daysBetween(installment.getDueDate(), now).getDays();
                    break;
                } else {
                    daysinArrear += installment.daysInArrear();
                }
            }

            List<JournalEntry> journalEntryList = this.journalEntryRepository.findAllByLoanTransaction_TypeOfAndLoanTransaction_LoanAndLoanTransactionReversed(LoanTransactionType.BONUS_PAY.getValue(), loan, false);
            BigDecimal expectedBonus = BigDecimal.ZERO;
            for(JournalEntry journalEntry : journalEntryList){
                expectedBonus = expectedBonus.add(journalEntry.getAmount());
            }

            boolean isCollectable = true;
            String reason = null;
            if (daysinArrear > daysInArrearAllowed) {
                isCollectable = false;
                reason = "warning.loan.bonus.exceedDaysOnArrear";
            }

            if(loan.isClosed()){
                LocalDate now = LocalDate.now();
                Integer actualDaysToCollect = Days.daysBetween(new LocalDate(loan.getClosedOnDate()), now).getDays();
                if(actualDaysToCollect > daysToCollectBonus) {
                    isCollectable = false;
                    reason = "warning.loan.bonus.exceedLimitDaysToCollectBonus";
                }
            }

            if(cycleForLoan == null){
                isCollectable = false;
                reason = "warning.loan.bonus.noCycleSetForLoanCount";
            }

            if(loanBonusCollectHistory == null) {
                loanBonusCollectHistory =
                        new LoanBonusCollectHistory(expectedBonus,
                                daysinArrear, null, isCollectable,
                                false, false, null, loan,
                                reason, cycleForLoan);
            } else {
                loanBonusCollectHistory.setBonusAmount(expectedBonus);
                loanBonusCollectHistory.setCollectable(isCollectable);
                loanBonusCollectHistory.setComments(reason);
                loanBonusCollectHistory.setDaysInArrear(daysinArrear);
                loanBonusCollectHistory.setCycleApplied(cycleForLoan);
            }
            this.loanBonusCollectHistoryRepository.save(loanBonusCollectHistory);
        }
        logger.debug("Update bonus job finished...");
    }
}
