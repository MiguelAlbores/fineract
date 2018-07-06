--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

CREATE TABLE `loan_bonus_configuration` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `days_in_arrear` INT NULL,
  `days_to_collect_bonus` INT NULL,
  `gl_account_to_debit` BIGINT(20) NULL,
  `gl_account_to_credit` BIGINT(20) NULL,
  `createdby_id` BIGINT(20) NOT NULL,
  `created_date` DATETIME NOT NULL,
  `lastmodifiedby_id` BIGINT(20) NULL,
  `lastmodified_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_loan_bonus_account_debit_idx` (`gl_account_to_debit` ASC),
  INDEX `fk_loan_bonus_account_credit_idx` (`gl_account_to_credit` ASC),
  CONSTRAINT `fk_loan_bonus_account_debit`
    FOREIGN KEY (`gl_account_to_debit`)
    REFERENCES `acc_gl_account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_loan_bonus_account_credit`
    FOREIGN KEY (`gl_account_to_credit`)
    REFERENCES `acc_gl_account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `loan_bonus_configuration_cycles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `from_value` SMALLINT(3) NOT NULL,
  `to_value` SMALLINT(3) NOT NULL,
  `percent_value` DECIMAL(4,2) NOT NULL,
  `id_loan_bonus_configuration` INT NOT NULL,
  `createdby_id` BIGINT(20) NOT NULL,
  `created_date` DATETIME NOT NULL,
  `lastmodifiedby_id` BIGINT(20) NULL,
  `lastmodified_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_loan_bonus_configuration_id_config_idx` (`id_loan_bonus_configuration` ASC),
  CONSTRAINT `fk_loan_bonus_configuration_id_config`
    FOREIGN KEY (`id_loan_bonus_configuration`)
    REFERENCES `loan_bonus_configuration` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('loan_bonus_config', 'CREATE_LOAN_BONUS_CONFIG', 'LoanBonusConfiguration', 'CREATE', '0');
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('loan_bonus_config', 'UPDATE_LOAN_BONUS_CONFIG', 'LoanBonusConfiguration', 'UPDATE', '0');


