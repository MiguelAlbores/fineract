package org.apache.fineract.catalogue.bank.domain;

import org.springframework.beans.factory.annotation.Autowired;

public class BankRepositoryWrapper {
    private final BankRepository repository;

    @Autowired
    public BankRepositoryWrapper(final BankRepository repository) {
        this.repository = repository;
    }
}
