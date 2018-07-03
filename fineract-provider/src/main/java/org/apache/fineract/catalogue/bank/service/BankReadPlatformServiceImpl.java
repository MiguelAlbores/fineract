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
package org.apache.fineract.catalogue.bank.service;

import org.apache.fineract.catalogue.bank.data.BankData;
import org.apache.fineract.catalogue.bank.data.BankOptionsData;
import org.apache.fineract.catalogue.bank.domain.BankStatus;
import org.apache.fineract.catalogue.bank.domain.BankType;
import org.apache.fineract.catalogue.bank.exception.BankNotFoundException;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankReadPlatformServiceImpl implements BankReadPlatformService{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BankReadPlatformServiceImpl(final RoutingDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<BankData> retrieveAllBanks() {
        BankMapper bm = new BankMapper();
        String sql = "select " + bm.schema();

        return this.jdbcTemplate.query(sql, bm);
    }

    @Override
    public BankData getBank(Long bankId) {
        try{
            BankMapper bm = new BankMapper();
            String sql = "select " + bm.schema() + " where cb.id = ?";

            return this.jdbcTemplate.queryForObject(sql, bm, new Object[] { bankId });
        } catch (final EmptyResultDataAccessException e) {
            throw new BankNotFoundException(bankId);
        }
    }

    @Override
    public List<EnumOptionData> getStatusOptions() {
        List<EnumOptionData> statusOptions = new ArrayList<>();
        for (final BankStatus status : BankStatus.values()) {
            final EnumOptionData optionData = new EnumOptionData(status.getValue(), status.getCode(),
                    status.toString());
            statusOptions.add(optionData);
        }
        return statusOptions;
    }

    @Override
    public List<EnumOptionData> getTypeOptions() {
        List<EnumOptionData> statusOptions = new ArrayList<>();
        for (final BankType type : BankType.values()) {
            final EnumOptionData optionData = new EnumOptionData(type.getValue(), type.getCode(),
                    type.toString());
            statusOptions.add(optionData);
        }
        return statusOptions;
    }

    @Override
    public BankOptionsData getOptions() {
        return new BankOptionsData(this.getStatusOptions(), this.getTypeOptions());
    }

    private static final class BankMapper implements RowMapper<BankData> {

        @Override
        public BankData mapRow(ResultSet rs, int rowNum) throws SQLException {
            String name = rs.getString("name");
            Long id = rs.getLong("id");
            Long bank_status_id = rs.getLong("bank_status_id");
            Long updated_by = rs.getLong("updated_by");
            Long created_by = rs.getLong("created_by");
            final DateTime created_at = JdbcSupport.getDateTime(rs, "created_at");
            final DateTime updated_at = JdbcSupport.getDateTime(rs, "updated_at");
            final String external_code = rs.getString("external_code");
            Long bank_type_id = rs.getLong("bank_type_id");
            BankType type = BankType.fromInt(bank_type_id);
            EnumOptionData typeData = new EnumOptionData(type.getValue(), type.getCode(), type.toString());
            BankStatus status = BankStatus.fromInt(bank_status_id);
            EnumOptionData statusData = new EnumOptionData(status.getValue(), status.getCode(), status.toString());
            return new BankData(id, name, created_at, updated_at, external_code, created_by, updated_by, typeData, statusData);
        }

        public String schema() {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    " cb.id as id, cb.name as name, cb.bank_status_id as bank_status_id, cb.bank_type_id as bank_type_id, " +
                    "cb.created_by, cb.updated_by, cb.created_at, cb.updated_at, cb.external_code");
            sb.append(" from catalogue_bank cb");
            return sb.toString();
        }
    }
}
