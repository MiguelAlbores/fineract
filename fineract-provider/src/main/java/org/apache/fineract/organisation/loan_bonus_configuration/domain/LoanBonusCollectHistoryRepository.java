package org.apache.fineract.organisation.loan_bonus_configuration.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanBonusCollectHistoryRepository extends JpaRepository<LoanBonusCollectHistory, Long>, JpaSpecificationExecutor<LoanBonusCollectHistory> {
    LoanBonusCollectHistory findByLoan_Id(Long loanId);
}
