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

CREATE TABLE `catalogue_bank` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `bank_type_id` SMALLINT(5) NOT NULL,
  `bank_status_id` SMALLINT(5) NOT NULL,
  `created_by` INT NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` INT NULL,
  `updated_at` TIMESTAMP,
  `external_code` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `bank_acc_account` (
  `id_bank` INT NOT NULL,
  `id_gl_account` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `external_code` VARCHAR(255) NULL,
  `acc_use_id` SMALLINT(5) NOT NULL,
  `bank_acc_status_id` SMALLINT(5) NOT NULL,
  `created_by` INT NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` INT NULL,
  `updated_at` TIMESTAMP,
  PRIMARY KEY (`id_bank`, `id_gl_account`),
  INDEX `fk_bank_acc_account_idx` (`id_gl_account` ASC),
  CONSTRAINT `fk_bank_acc_account_acc`
    FOREIGN KEY (`id_gl_account`)
    REFERENCES `acc_gl_account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bank_acc_account_bank`
    FOREIGN KEY (`id_bank`)
    REFERENCES `catalogue_bank` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

INSERT INTO `m_permission`
(
`grouping`,
`code`,
`entity_name`,
`action_name`,
`can_maker_checker`)
VALUES
('catalogue', 'CREATE_BANK', 'Banks', 'CREATE', '0'),
('catalogue', 'UPDATE_BANK', 'Banks', 'UPDATE', '0'),
('catalogue', 'DELETE_BANK', 'Banks', 'DELETE', '0'),
('catalogue', 'CREATE_BANK_ACCOUNT', 'BankAccounts', 'CREATE', '0'),
('catalogue', 'UPDATE_BANK_ACCOUNT', 'BankAccounts', 'UPDATE', '0'),
('catalogue', 'DELETE_BANK_ACCOUNT', 'BankAccounts', 'DELETE', '0');

INSERT INTO `r_enum_value`
(`enum_name`,
`enum_id`,
`enum_message_property`,
`enum_value`,
`enum_type`)
VALUES
('bank_type_id', 1, 'BANK', 'BANK', 0),
('bank_type_id', 2, 'CORRESPONDENT_AGENT', 'CORRESPONDENT_AGENT', 0),
('bank_type_id', 3, 'COMISSION_AGENT', 'COMISSION_AGENT', 0),
('bank_status_id', 1, 'ACTIVE', 'ACTIVE', 0),
('bank_status_id', 2, 'INACTIVE', 'INACTIVE', 0),
('acc_use_id', 1, 'CATCHMENT', 'CATCHMENT', 0),
('acc_use_id', 2, 'REPAYMENT', 'REPAYMENT', 0),
('acc_use_id', 3, 'DISBURSEMENT', 'DISBURSEMENT', 0),
('bank_acc_status_id', 1, 'ACTIVE', 'ACTIVE', 0),
('bank_acc_status_id', 2, 'INACTIVE', 'INACTIVE', 0);

