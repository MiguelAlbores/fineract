package org.apache.fineract.catalogue.bank.command;

public class BankCommand {
    private final Long id;
    private final String name;
    private final String external_code;
    private final String registry_status_id;

    public BankCommand(Long id, String name, String external_code, String registry_status_id) {
        this.id = id;
        this.name = name;
        this.external_code = external_code;
        this.registry_status_id = registry_status_id;
    }
}
