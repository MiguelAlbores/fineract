package org.apache.fineract.catalogue.bank_acc.exception;

import org.apache.fineract.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

public class BankAccountNotFoundException extends AbstractPlatformResourceNotFoundException {
    public BankAccountNotFoundException(final Long idGlAccount, final Long idBank) {
        super("error.msg.bank.account.id.invalid", "Bank account with identifier bank:" + idBank + " and account: "+ idGlAccount +" does not exist", idGlAccount,idBank);
    }
}
