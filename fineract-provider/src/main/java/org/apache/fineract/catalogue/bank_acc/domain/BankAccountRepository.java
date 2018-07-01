package org.apache.fineract.catalogue.bank_acc.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BankAccountRepository extends JpaRepository<BankAccount, BankAccountPK>, JpaSpecificationExecutor<BankAccount> {
}
