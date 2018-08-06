/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.fineract.catalogue.bank_acc.service;

import org.apache.fineract.accounting.glaccount.data.GLAccountData;
import org.apache.fineract.accounting.glaccount.domain.GLAccountRepository;
import org.apache.fineract.accounting.glaccount.service.GLAccountReadPlatformService;
import org.apache.fineract.accounting.journalentry.data.JournalEntryAssociationParametersData;
import org.apache.fineract.catalogue.bank.domain.BankRepository;
import org.apache.fineract.catalogue.bank_acc.data.BankAccountData;
import org.apache.fineract.catalogue.bank_acc.data.BankAccountOptionsData;
import org.apache.fineract.catalogue.bank_acc.domain.BankAccountStatus;
import org.apache.fineract.catalogue.bank_acc.domain.BankAccountUse;
import org.apache.fineract.catalogue.bank_acc.exception.BankAccountNotFoundException;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BankAccountReadPlatformServiceImpl implements BankAccountReadPlatformService{

    private final JdbcTemplate jdbcTemplate;
    private final BankRepository bankRepository;
    private final GLAccountReadPlatformService glAccountReadPlatformService;

    @Autowired
    public BankAccountReadPlatformServiceImpl(final RoutingDataSource dataSource, BankRepository bankRepository, GLAccountReadPlatformService glAccountReadPlatformService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.bankRepository = bankRepository;
        this.glAccountReadPlatformService = glAccountReadPlatformService;
    }

    @Override
    public List<BankAccountData> getAllAccountsByBank(Long bankId) {
        BankAccountExtractor bm = new BankAccountExtractor(this.glAccountReadPlatformService);
        String sql = "select " + bm.schema() + " where ba.id_bank = ?";

        return this.jdbcTemplate.query(sql, bm, new Object[] { bankId });
    }

    @Override
    public List<BankAccountData> getAllAccounts(Integer usage) {
        BankAccountExtractor bm = new BankAccountExtractor(this.glAccountReadPlatformService);
        String sql = "select " + bm.schema() + " where ba.acc_use_id = ?";

        return this.jdbcTemplate.query(sql, bm, new Object[] { usage });
    }

    @Override
    public BankAccountData getAccount(Long idGlAccount, Long bankId) {
        try{
            BankAccountMapper bm = new BankAccountMapper(this.glAccountReadPlatformService);
            String sql = "select " + bm.schema() + " where ba.id_gl_account=? and ba.id_bank = ?";

            return this.jdbcTemplate.queryForObject(sql, bm, new Object[] { idGlAccount, bankId });
        } catch (final EmptyResultDataAccessException e) {
            throw new BankAccountNotFoundException(idGlAccount, bankId);
        }
    }

    @Override
    public List<EnumOptionData> getStatusOptions() {
        List<EnumOptionData> statusOptions = new ArrayList<>();
        for (final BankAccountStatus status : BankAccountStatus.values()) {
            final EnumOptionData optionData = new EnumOptionData(status.getValue(), status.getCode(),
                    status.toString());
            statusOptions.add(optionData);
        }
        return statusOptions;
    }

    @Override
    public List<EnumOptionData> getUseOptions() {
        List<EnumOptionData> statusOptions = new ArrayList<>();
        for (final BankAccountUse use : BankAccountUse.values()) {
            final EnumOptionData optionData = new EnumOptionData(use.getValue(), use.getCode(),
                    use.toString());
            statusOptions.add(optionData);
        }
        return statusOptions;
    }

    @Override
    public BankAccountOptionsData getOptions() {
        return new BankAccountOptionsData(this.getStatusOptions(), this.getUseOptions());
    }

    private static final class BankAccountMapper implements RowMapper<BankAccountData> {
        private final GLAccountReadPlatformService glAccountReadPlatformService;

        public BankAccountMapper(GLAccountReadPlatformService glAccountReadPlatformService) {
            this.glAccountReadPlatformService = glAccountReadPlatformService;
        }

        public String schema() {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    " ba.id_bank, ba.name, ba.bank_acc_status_id, ba.id_gl_account, ba.acc_use_id," +
                            "ba.created_by, ba.updated_by, ba.created_at, ba.updated_at, ba.external_code");
            sb.append(" from bank_acc_account ba");
            return sb.toString();
        }

        @Override
        public BankAccountData mapRow(ResultSet rs, int rowNum) throws SQLException {
            String name = rs.getString("name");
            Long acc_use_id = rs.getLong("acc_use_id");
            BankAccountUse accountUse = BankAccountUse.fromInt(acc_use_id);
            EnumOptionData useData = new EnumOptionData(accountUse.getValue(), accountUse.getCode(), accountUse.toString());
            Long bankid = rs.getLong("id_bank");
            Long glAccountId = rs.getLong("id_gl_account");
            GLAccountData glAccountData;
            glAccountData = glAccountReadPlatformService.retrieveGLAccountById(glAccountId, new JournalEntryAssociationParametersData());
            Integer bank_acc_status_id = rs.getInt("bank_acc_status_id");
            BankAccountStatus accountStatus = BankAccountStatus.fromInt(bank_acc_status_id);
            EnumOptionData statusData = new EnumOptionData(accountStatus.getValue(), accountStatus.getCode(), accountStatus.toString());
            Long updated_by = rs.getLong("updated_by");
            Long created_by = rs.getLong("created_by");
            final DateTime created_at = JdbcSupport.getDateTime(rs, "created_at");
            final DateTime updated_at = JdbcSupport.getDateTime(rs, "updated_at");
            final String external_code = rs.getString("external_code");
            return new BankAccountData(bankid, glAccountData, name, external_code,
                    created_at, updated_at, created_by, updated_by, useData, statusData);
        }
    }

    private static final class BankAccountExtractor implements ResultSetExtractor<List<BankAccountData>> {
        private final GLAccountReadPlatformService glAccountReadPlatformService;

        public BankAccountExtractor(GLAccountReadPlatformService glAccountReadPlatformService) {
            this.glAccountReadPlatformService = glAccountReadPlatformService;
        }

        public String schema() {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    " ba.id_bank, ba.name, ba.bank_acc_status_id, ba.id_gl_account, ba.acc_use_id," +
                            "ba.created_by, ba.updated_by, ba.created_at, ba.updated_at, ba.external_code");
            sb.append(" from bank_acc_account ba");
            return sb.toString();
        }

        @Override
        public List<BankAccountData> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<BankAccountData> accounts = new ArrayList<>();
            Map<Long, GLAccountData> accountsMap = new HashMap<>();
            while (rs.next()) {
                String name = rs.getString("name");
                Long acc_use_id = rs.getLong("acc_use_id");
                BankAccountUse accountUse = BankAccountUse.fromInt(acc_use_id);
                EnumOptionData useData = new EnumOptionData(accountUse.getValue(), accountUse.getCode(), accountUse.toString());
                Long bankid = rs.getLong("id_bank");
                Long glAccountId = rs.getLong("id_gl_account");
                GLAccountData glAccountData;
                if (accountsMap.containsKey(glAccountId))
                    glAccountData = accountsMap.get(glAccountId);
                else {
                    glAccountData = glAccountReadPlatformService.retrieveGLAccountById(glAccountId, new JournalEntryAssociationParametersData());
                    accountsMap.put(glAccountId, glAccountData);
                }
                Integer bank_acc_status_id = rs.getInt("bank_acc_status_id");
                BankAccountStatus accountStatus = BankAccountStatus.fromInt(bank_acc_status_id);
                EnumOptionData statusData = new EnumOptionData(accountStatus.getValue(), accountStatus.getCode(), accountStatus.toString());
                Long updated_by = rs.getLong("updated_by");
                Long created_by = rs.getLong("created_by");
                final DateTime created_at = JdbcSupport.getDateTime(rs, "created_at");
                final DateTime updated_at = JdbcSupport.getDateTime(rs, "updated_at");
                final String external_code = rs.getString("external_code");
                BankAccountData bankAccountData = new BankAccountData(bankid, glAccountData, name, external_code,
                        created_at, updated_at, created_by, updated_by, useData, statusData);
                accounts.add(bankAccountData);
            }
            return accounts;
        }
    }
}
