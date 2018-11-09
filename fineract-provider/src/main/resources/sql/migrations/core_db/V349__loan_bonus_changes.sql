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

INSERT INTO `job` (`name`, `display_name`, `cron_expression`, `task_priority`, `create_time`, `job_key`, `is_active`, `currently_running`, `updates_allowed`, `scheduler_group`, `is_misfired`) VALUES ('Update bonus for Loans', 'Update bonus for Loans', '0 0 0 1/1 * ? *', '5', now(), 'Update bonus for Loans', '1', '0', '1', '0', '0');
CREATE TABLE `loan_bonus_collect_history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `bonus_amount` DECIMAL(19,6) NOT NULL,
  `is_collectable` TINYINT(1) NOT NULL DEFAULT 0,
  `collected` TINYINT(1) NOT NULL DEFAULT 0,
  `canceled` TINYINT(1) NOT NULL DEFAULT 0,
  `days_in_arrear` SMALLINT(5) NULL DEFAULT 0,
  `collected_date` DATE NULL,
  `canceled_date` DATE NULL,
  `loan_id` BIGINT(20) NOT NULL,
  `comments` VARCHAR(255) NULL,
  `loan_bonus_cycle_applied` INT(11) NULL,
  PRIMARY KEY (`id`));

INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('loan_bonus', 'COLLECT_LOAN_BONUS', 'LoanBonusCollectHistory', 'UPDATE', '0');
INSERT INTO .`m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('loan_bonus', 'CANCEL_LOAN_BONUS', 'LoanBonusCollectHistory', 'UPDATE', '0');

