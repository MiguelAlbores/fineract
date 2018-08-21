package org.apache.fineract.organisation.office.exception;

import org.apache.fineract.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class OfficeTypeHierarchyException extends AbstractPlatformDomainRuleException {


    public OfficeTypeHierarchyException(String message) {
        super("error.msg.office.wrong.hierarchy", message);
    }
}
