package org.apache.fineract.organisation.loan_bonus_configuration.service;

import org.apache.fineract.accounting.glaccount.domain.GLAccount;
import org.apache.fineract.accounting.glaccount.domain.GLAccountRepository;
import org.apache.fineract.accounting.journalentry.domain.JournalEntry;
import org.apache.fineract.accounting.journalentry.domain.JournalEntryRepository;
import org.apache.fineract.accounting.journalentry.domain.JournalEntryType;
import org.apache.fineract.accounting.producttoaccountmapping.domain.PortfolioProductType;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusCollectHistory;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusCollectHistoryRepository;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusConfiguration;
import org.apache.fineract.organisation.loan_bonus_configuration.domain.LoanBonusConfigurationRepository;
import org.apache.fineract.organisation.loan_bonus_configuration.exception.LoanBonusWrongCollectDateException;
import org.apache.fineract.organisation.loan_bonus_configuration.exception.LoanBonusWrongTransactionDateException;
import org.apache.fineract.organisation.monetary.domain.Money;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransaction;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransactionRepository;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.apache.fineract.accounting.journalentry.service.AccountingProcessorHelper.LOAN_TRANSACTION_IDENTIFIER;

@Service
public class LoanBonusWriteServiceImpl implements LoanBonusWriteService{
    private final LoanBonusCollectHistoryRepository loanBonusCollectHistoryRepository;
    private final LoanBonusConfigurationRepository loanBonusConfigurationRepository;
    private final GLAccountRepository glAccountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final LoanTransactionRepository loanTransactionRepository;

    @Autowired
    public LoanBonusWriteServiceImpl(LoanBonusCollectHistoryRepository loanBonusCollectHistoryRepository, LoanBonusConfigurationRepository loanBonusConfigurationRepository, GLAccountRepository glAccountRepository, JournalEntryRepository journalEntryRepository, LoanTransactionRepository loanTransactionRepository) {
        this.loanBonusCollectHistoryRepository = loanBonusCollectHistoryRepository;
        this.loanBonusConfigurationRepository = loanBonusConfigurationRepository;
        this.glAccountRepository = glAccountRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.loanTransactionRepository = loanTransactionRepository;
    }

    @Override
    public CommandProcessingResult collectBonus(Long loanId, JsonCommand command) {
        LoanBonusCollectHistory loanBonusCollectHistory = this.loanBonusCollectHistoryRepository.findByLoan_Id(loanId);
        if(loanBonusCollectHistory != null) {
            if(!loanBonusCollectHistory.getLoan().isClosed())
                return CommandProcessingResult.empty();

            LocalDate transactionDate = command.localDateValueOfParameterNamed("transactionDate");
            if(transactionDate.toDate().before(loanBonusCollectHistory.getLoan().getClosedOnDate())){
                throw new LoanBonusWrongTransactionDateException();
            }

            LoanBonusConfiguration loanBonusConfiguration = loanBonusConfigurationRepository.getLoanBonusConfigurationByLoanProductId(loanBonusCollectHistory.getLoan().productId());

            Integer daysFromClosedToCollect = Days.daysBetween(new LocalDate(loanBonusCollectHistory.getLoan().getClosedOnDate()),transactionDate).getDays();
            if(daysFromClosedToCollect > loanBonusConfiguration.getDaysToCollectBonus()){
                throw new LoanBonusWrongCollectDateException();
            }


            LoanTransaction bonusTransaction = LoanTransaction.accrueLoanBonus(loanBonusCollectHistory.getLoan().getOffice(), loanBonusCollectHistory.getLoan(),
                    Money.of(loanBonusCollectHistory.getLoan().getCurrency(), loanBonusCollectHistory.getBonusAmount()), new Date(), null);
            this.loanTransactionRepository.saveAndFlush(bonusTransaction);

            GLAccount accountToCredit = this.glAccountRepository.findOne(loanBonusConfiguration.getGlAccountToCredit());
            GLAccount accountToDebit = this.glAccountRepository.findOne(loanBonusConfiguration.getGlAccountToDebit());

            String modifiedTransactionId = LOAN_TRANSACTION_IDENTIFIER + bonusTransaction.getId().toString();
            final JournalEntry journalEntry = JournalEntry.createNew(loanBonusCollectHistory.getLoan().getOffice(), null, accountToCredit, loanBonusCollectHistory.getLoan().getCurrencyCode(), modifiedTransactionId,
                    false, new Date(), JournalEntryType.DEBIT, loanBonusCollectHistory.getBonusAmount(), null, PortfolioProductType.LOAN.getValue(), loanId, null,
                    bonusTransaction, null, null, null);
            this.journalEntryRepository.saveAndFlush(journalEntry);
            final JournalEntry journalEntry2 = JournalEntry.createNew(loanBonusCollectHistory.getLoan().getOffice(), null, accountToDebit
                    , loanBonusCollectHistory.getLoan().getCurrencyCode(), modifiedTransactionId,
                    false, new Date(), JournalEntryType.DEBIT, loanBonusCollectHistory.getBonusAmount(), null, PortfolioProductType.LOAN.getValue(), loanId, null,
                    bonusTransaction, null, null, null);
            this.journalEntryRepository.saveAndFlush(journalEntry2);

            loanBonusCollectHistory.setCollected(true);
            loanBonusCollectHistory.setCollectedDate(new Date());
            this.loanBonusCollectHistoryRepository.saveAndFlush(loanBonusCollectHistory);
            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(loanBonusCollectHistory.getId()).withTransactionId(bonusTransaction.getId().toString()).build();
        }
        return CommandProcessingResult.empty();
    }

    @Override
    public CommandProcessingResult cancelBonus(Long loanId, JsonCommand command) {
        LoanBonusCollectHistory loanBonusCollectHistory = this.loanBonusCollectHistoryRepository.findByLoan_Id(loanId);
        if(loanBonusCollectHistory != null) {
            if(!loanBonusCollectHistory.getLoan().isClosed())
                return CommandProcessingResult.empty();
            LoanBonusConfiguration loanBonusConfiguration = loanBonusConfigurationRepository.getLoanBonusConfigurationByLoanProductId(loanBonusCollectHistory.getLoan().productId());
            LoanTransaction bonusTransaction = LoanTransaction.accrueLoanBonus(loanBonusCollectHistory.getLoan().getOffice(), loanBonusCollectHistory.getLoan(),
                    Money.of(loanBonusCollectHistory.getLoan().getCurrency(), loanBonusCollectHistory.getBonusAmount()), new Date(), null);
            this.loanTransactionRepository.saveAndFlush(bonusTransaction);

            GLAccount accountToCredit = this.glAccountRepository.findOne(loanBonusConfiguration.getGlAccountToCredit());

            String modifiedTransactionId = LOAN_TRANSACTION_IDENTIFIER + bonusTransaction.getId().toString();
            final JournalEntry journalEntry = JournalEntry.createNew(loanBonusCollectHistory.getLoan().getOffice(), null, accountToCredit, loanBonusCollectHistory.getLoan().getCurrencyCode(), modifiedTransactionId,
                    false, new Date(), JournalEntryType.DEBIT, loanBonusCollectHistory.getBonusAmount(), null, PortfolioProductType.LOAN.getValue(), loanId, null,
                    bonusTransaction, null, null, null);
            this.journalEntryRepository.saveAndFlush(journalEntry);

            loanBonusCollectHistory.setCanceled(true);
            loanBonusCollectHistory.setCanceledDate(new Date());
            this.loanBonusCollectHistoryRepository.saveAndFlush(loanBonusCollectHistory);
            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(loanBonusCollectHistory.getId()).withTransactionId(bonusTransaction.getId().toString()).build();
        }
        return CommandProcessingResult.empty();
    }
}
