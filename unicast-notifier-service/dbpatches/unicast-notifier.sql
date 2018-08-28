/*
*********************************************************************
SQL Community v12.2.4 (64 bit)
-- ------------------------------------------------------------
Server version	5.6.18-enterprise-commercial-advanced

*********************************************************************
*/

-- ------------------------------------------------------------
-- Database: `unicastdb`
-- ------------------------------------------------------------

DROP SCHEMA IF EXISTS `unicastdb`;
CREATE SCHEMA `unicastdb`;
USE `unicastdb`;

-- ------------------------------------------------------------
-- Table structure for table `TRIGGER_MESSAGE`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `TRIGGER_MESSAGE`;

CREATE TABLE `TRIGGER_MESSAGE` (
  `ID` bigint(11) NOT NULL auto_increment COMMENT 'System generated identifier of trigger message.',
  `MACADDRESS` varchar(20) NOT NULL COMMENT 'MACAddress of target STB',
  `TRIGGER_TYPE` varchar(75) DEFAULT NULL COMMENT 'Trigger info for trigger message',
  `TIMESTAMP` bigint(13) NOT NULL COMMENT 'Timestamp of triggering',
  `RETRY_COUNT` smallint(11) NOT NULL COMMENT 'Number of retries done',
  `ERROR_MESSAGE` varchar(1024) NOT NULL COMMENT 'Reason for failure of request',
   `INSTANCE_ID` bigint(12) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ------------------------------------------------------------
-- Table structure for table `TRIGGER_MAPPING`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `TRIGGER_MAPPING`;

CREATE TABLE `TRIGGER_MAPPING` (
  `TRIGGER_TYPE` varchar(30) NOT NULL COMMENT 'Trigger Type',
  `SERVICE_NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`TRIGGER_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ------------------------------------------------------------
-- Data for the table `TRIGGER_MAPPING`
-- ------------------------------------------------------------
INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES 
("T_SUBSCRIBER_INFO","STB Trigger Update");

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES 
("T_FAVORITES","STB Trigger Update");

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES 
("T_CONTENT_RENTAL","STB Trigger Update");

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES 
("T_NPVR","STB Trigger Update");

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES 
("T_CONTACTS","STB Trigger Update");

INSERT INTO `TRIGGER_MAPPING` (`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES 
("T_REMINDERS","STB Trigger Update");

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES 
("T_BOOKMARK","STB Trigger Update");

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES 
("S_MESSAGE_INFO","UnicastPushMessaging");

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES 
("T_USER_PREFERENCES","STB Trigger Update");


CREATE TABLE DB_VERSION (
    PRODUCT_VERSION VARCHAR(50) NOT NULL,
                MINOR_VERSION INT(3) NOT NULL,
                CREATOR VARCHAR(50),
                DATE_CREATED DATE,
                DATE_APPLIED DATE NOT NULL,
                PRIMARY KEY (PRODUCT_VERSION,MINOR_VERSION)
    );

INSERT INTO DB_VERSION (PRODUCT_VERSION, MINOR_VERSION, CREATOR, DATE_CREATED, DATE_APPLIED)
     VALUES ('AVS 6.1', 0, 'RSI', STR_TO_DATE('2016-12-12', '%Y-%m-%d'), CURRENT_TIMESTAMP());