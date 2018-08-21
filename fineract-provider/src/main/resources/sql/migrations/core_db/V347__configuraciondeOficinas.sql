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

ALTER TABLE `m_office` 
ADD COLUMN `office_type_id` SMALLINT(5) NOT NULL AFTER `opening_date`,
ADD COLUMN `address` VARCHAR(1000) NULL AFTER `office_type_id`,
ADD COLUMN `colony` VARCHAR(100) NULL AFTER `address`,
ADD COLUMN `postal_code` VARCHAR(100) NULL AFTER `colony`,
ADD COLUMN `phone` VARCHAR(100) NULL AFTER `postal_code`,
ADD COLUMN `municipality` VARCHAR(255) NULL AFTER `phone`,
ADD COLUMN `state` VARCHAR(255) NULL AFTER `municipality`,
ADD COLUMN `une` TEXT NULL AFTER `state`;

INSERT INTO `r_enum_value` (`enum_name`, `enum_id`, `enum_message_property`, `enum_value`, `enum_type`) VALUES ('office_type_id', '1', 'Root', 'Root', '0');
INSERT INTO `r_enum_value` (`enum_name`, `enum_id`, `enum_message_property`, `enum_value`, `enum_type`) VALUES ('office_type_id', '2', 'Group', 'Group', '0');
INSERT INTO `r_enum_value` (`enum_name`, `enum_id`, `enum_message_property`, `enum_value`, `enum_type`) VALUES ('office_type_id', '3', 'Office', 'Office', '0');

