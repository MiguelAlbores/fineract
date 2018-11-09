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

CREATE TABLE IF NOT EXISTS `loan_product_tax_component` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `loan_product_id` BIGINT(20) NOT NULL,
  `tax_component_id` BIGINT(20) NOT NULL,
  `percentage` decimal(19,6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_tax_component_UNIQUE` (`loan_product_id`, `tax_component_id`),
  CONSTRAINT `fk_product_tax_component_loan_product` FOREIGN KEY (`loan_product_id`) REFERENCES `m_product_loan` (`id`),
  CONSTRAINT `fk_product_tax_component_tax_component` FOREIGN KEY (`tax_component_id`) REFERENCES `m_tax_component` (`id`)
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `loan_tax` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `loan_id` BIGINT(20) NOT NULL,
  `product_tax_component_id` BIGINT(20) NOT NULL,
  `amount` decimal(19,6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `loan_tax_component_UNIQUE` (`loan_id`, `product_tax_component_id`),
  CONSTRAINT `fk_loan_tax_loan` FOREIGN KEY (`loan_id`) REFERENCES `m_loan` (`id`),
  CONSTRAINT `fk_loan_tax_loan_product_tax_component` FOREIGN KEY (`product_tax_component_id`) REFERENCES `loan_product_tax_component` (`id`)
)ENGINE = InnoDB;

ALTER TABLE `m_loan_repayment_schedule` 
ADD COLUMN `tax_amount` DECIMAL(19,6) NULL;

ALTER TABLE `m_loan` 
ADD COLUMN `tax_on_interest` DECIMAL(19,6) NULL;
ALTER TABLE `m_loan_repayment_schedule` 
ADD COLUMN `tax_completed_derived` DECIMAL(19,6) NULL;

ALTER TABLE `m_loan_transaction` 
ADD COLUMN `tax_on_interest_portion_derived` DECIMAL(19,6) NULL;
