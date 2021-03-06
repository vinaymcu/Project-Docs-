DROP TABLE IF EXISTS `SET_TOP_BOXES`;
CREATE TABLE SET_TOP_BOXES (EQUIPMENT_ID INT(11) AUTO_INCREMENT,SERIAL_NUMBER VARCHAR(20) NOT NULL,INTERNAL_IP_ADDRESS VARCHAR(20),EXTERNAL_IP_ADDRESS VARCHAR(20),MAC_ADDRESS VARCHAR(20) UNIQUE,HW_VERSION VARCHAR(20),SW_VERSION VARCHAR(50),UI_VERSION VARCHAR(50),ASSIGNEDTO_SUBSCRIBER_ID VARCHAR(11),LOCATION_ID INT(11), ASSIGNMENT_STATUS VARCHAR(10) NOT NULL,OVERRIDING_DEFAULT TINYINT(1),MAX_BANDWIDTH_UPDATE INT(3),TV_QUALITY_INTEREST VARCHAR(20),DATETIME_OF_ASSIGNMENT BIGINT,STB_NAME VARCHAR(30) NOT NULL,CAS_DEVICE_ID VARCHAR(30) NOT NULL,VMX_CLIENT_ID VARCHAR(30),SUPPORTED_MODES VARCHAR(40),CONNECTION_MODE VARCHAR(30),DRM_SYNC_STATUS TINYINT(1) NOT NULL,LAST_UPDATED_ON BIGINT NOT NULL,LAST_UPDATED_BY VARCHAR(30) NOT NULL,PRIMARY KEY (EQUIPMENT_ID)) ;
--
-- Table structure for table `STB_SERVICE_PORT_MAPPINGS`
--
DROP TABLE IF EXISTS `STB_SERVICE_PORT_MAPPINGS`;
CREATE TABLE STB_SERVICE_PORT_MAPPINGS (EQUIPMENT_ID INT(11),SERVICE_ID INT(50),INTERNAL_PORT INT(5) NOT NULL,EXTERNAL_PORT INT(5) NOT NULL,PRIMARY KEY (EQUIPMENT_ID, SERVICE_ID)) ;
--
-- Table structure for table `STB_PROPERTIES`
--
DROP TABLE IF EXISTS `STB_PROPERTIES`;
CREATE TABLE STB_PROPERTIES (EQUIPMENT_ID INT(11),PROPERTY_NAME VARCHAR(20),PROPERTY_VALUE VARCHAR(200),PRIMARY KEY (EQUIPMENT_ID, PROPERTY_NAME)) ;
--
-- Table structure for table `STB_UDF`
--
DROP TABLE IF EXISTS `STB_UDF`;
CREATE TABLE STB_UDF (EQUIPMENT_ID INT(11),UDF_NAME VARCHAR(30),UDF_VALUE VARCHAR(255),PRIMARY KEY (EQUIPMENT_ID, UDF_NAME));
--
-- Table structure for table `RESOURCES`
--
DROP TABLE IF EXISTS `RESOURCES`;
CREATE TABLE RESOURCES (ID INT(11) AUTO_INCREMENT,NAME VARCHAR(20),DESCRIPTION VARCHAR(50),UNIT VARCHAR(20),PRIMARY KEY (ID, NAME));
--
-- Table structure for table `STB_ASSIGNED_RESOURCES`
--
DROP TABLE IF EXISTS `STB_ASSIGNED_RESOURCES`;
CREATE TABLE STB_ASSIGNED_RESOURCES (EQUIPMENT_ID INT(11),RESOURCE_ID INT(11),AMOUNT VARCHAR(20),PRIMARY KEY (EQUIPMENT_ID, RESOURCE_ID));
--
-- Table structure for table `STB_RESOURCES_ALLOCATION`
--
DROP TABLE IF EXISTS `STB_RESOURCES_ALLOCATION`;
CREATE TABLE STB_RESOURCES_ALLOCATION (EQUIPMENT_ID INT(11),STB_PROFILE VARCHAR(20),PROFILE_BANDWIDTH INT(8),QOE_BANDWIDTH INT(8),LAST_UPDATED_ON BIGINT,PRIMARY KEY (EQUIPMENT_ID)) ;
--
-- Table structure for table `STB_MAX_BW_ALLOWED_PER_QUALITY`
--
DROP TABLE IF EXISTS `STB_MAX_BW_ALLOWED_PER_QUALITY`;
CREATE TABLE STB_MAX_BW_ALLOWED_PER_QUALITY (EQUIPMENT_ID INT(11),TV_QUALITY VARCHAR(5),MAX_BANDWIDTH INT(10),PRIMARY KEY (EQUIPMENT_ID, TV_QUALITY)) ;
--
-- Table structure for table `SUBSCRIBERS`
--
DROP TABLE IF EXISTS `SUBSCRIBERS`;
CREATE TABLE SUBSCRIBERS (SUBSCRIBER_ID VARCHAR(11),ACCOUNT_NUMBER VARCHAR(11) UNIQUE,NAME VARCHAR(75) NOT NULL,CITY VARCHAR(50),LOCATION_ID INT(11),TYPE VARCHAR(20),PHONE_NUMBER VARCHAR(25),SUBSCRIBER_BANDWIDTH_PROFILE VARCHAR(30) NOT NULL,MAX_BW_OVERRIDE INT(10),QOE_CONTROL_BANDWIDTH INT(11),RET_ENABLE BOOLEAN,RCC_ENABLE BOOLEAN,NETWORK_BUFFER_SIZE INT(11),FREE_BANDWIDTH INT(11),STATUS VARCHAR(10) NOT NULL,PRIMARY KEY (SUBSCRIBER_ID)) ;
--
-- Table structure for table `SUBSCRIBER_STB_LIMIT`
--
DROP TABLE IF EXISTS `SUBSCRIBER_STB_LIMIT`;
CREATE TABLE SUBSCRIBER_STB_LIMIT (SUBSCRIBER_ID VARCHAR(11),CONTENT_QUALITY VARCHAR(10),STB_LIMIT INT(2) NOT NULL,PRIMARY KEY (SUBSCRIBER_ID, CONTENT_QUALITY)) ;
--
-- Table structure for table `LOCATIONS`
--
DROP TABLE IF EXISTS `LOCATIONS`;
CREATE TABLE LOCATIONS (ID INT(11),PARENT_ID INT(11) NOT NULL,NAME VARCHAR(30) NOT NULL,TV_REGION_ID INT(11) NOT NULL,PRIMARY KEY (ID)) ;
--
-- Table structure for table `HARDWARE_VERSIONS`
--
DROP TABLE IF EXISTS `HARDWARE_VERSIONS`;
CREATE TABLE HARDWARE_VERSIONS (NAME VARCHAR(20),QOE_CAPABLE BOOLEAN NOT NULL,DISABLE_STB_AUTO_REGISTRATION BOOLEAN NOT NULL,PRIMARY KEY (NAME)); 
--
-- Table structure for table `HARDWARE_MTP_MAPPING`
--
DROP TABLE IF EXISTS `HARDWARE_MTP_MAPPING`;
CREATE TABLE HARDWARE_MTP_MAPPING (HARDWARE_NAME VARCHAR(20),MTP_ID VARCHAR(20),PRIMARY KEY (HARDWARE_NAME, MTP_ID)) ;
--
-- Table structure for table `LANGUAGES`
--
DROP TABLE IF EXISTS `LANGUAGES`;
CREATE TABLE LANGUAGES (LANGUAGE_TYPE VARCHAR(10),DISPLAY_NAME VARCHAR(30) NOT NULL,AVAILABLE_FOR_UI BOOLEAN NOT NULL,AVAILABLE_FOR_AUDIO BOOLEAN NOT NULL,PRIMARY KEY (LANGUAGE_TYPE)) ;
--
-- Table structure for table `DEFAULT_SERVICE_PORT_LINK`
--
DROP TABLE IF EXISTS `DEFAULT_SERVICE_PORT_LINK`;
CREATE TABLE DEFAULT_SERVICE_PORT_LINK (SERVICE_ID INT(11) NOT NULL AUTO_INCREMENT,SERVICE_NAME VARCHAR(50) UNIQUE,DEFAULT_INTERNAL_PORT INT(6) NOT NULL,DEFAULT_EXTERNAL_PORT INT(6) NOT NULL,PROTOCOL VARCHAR(10) NOT NULL,DEFAULT_SERVICE BOOLEAN NOT NULL,PRIMARY KEY (SERVICE_ID)) ;
--
-- Table structure for table `SERVICE_HARDWARE_MAPPING`
--
DROP TABLE IF EXISTS `SERVICE_HARDWARE_MAPPING`;
CREATE TABLE SERVICE_HARDWARE_MAPPING (SERVICE_ID INT(11) UNIQUE,HARDWARE_NAME VARCHAR(20),UNIQUE(SERVICE_ID, HARDWARE_NAME)) ;
--
-- Table structure for table `CONNECTION_MODES`
--
DROP TABLE IF EXISTS `CONNECTION_MODES`;
CREATE TABLE `CONNECTION_MODES` (`ID` int(11) NOT NULL AUTO_INCREMENT, `CONNECTION_MODE` varchar(50) NOT NULL, STATUS VARCHAR(10) NOT NULL,PRIMARY KEY (`ID`))  AUTO_INCREMENT=1 ;
--
-- Table structure for table `USER_DEFINED_FIELDS`
--
DROP TABLE IF EXISTS `USER_DEFINED_FIELDS`;
CREATE TABLE USER_DEFINED_FIELDS ( ID INT(11) AUTO_INCREMENT,NAME VARCHAR(30) NOT NULL UNIQUE,DESCRIPTION VARCHAR(256),DEFAULT_VALUE VARCHAR(256),ENUM_VALUES VARCHAR(1024),LAST_UPDATE_DATETIME BIGINT,LAST_UPDATE_USER_ID VARCHAR(11), PRIMARY KEY (ID)) ;
--
-- Table structure for table `UDF_RULES`
--
DROP TABLE IF EXISTS `UDF_RULES`;
CREATE TABLE UDF_RULES (  RULE_ID INT(11) AUTO_INCREMENT,  RULE_NAME VARCHAR(100) NOT NULL,  INPUT_PARAMETER BOOLEAN DEFAULT 0,  CLASS_NAME VARCHAR(150) NOT NULL,  PRIMARY KEY (RULE_ID)) ;
--
-- Table structure for table `UDF_RULES_MAPPING`
--
DROP TABLE IF EXISTS `UDF_RULES_MAPPING`;
CREATE TABLE `UDF_RULES_MAPPING` (  UDF_ID INT(11) NOT NULL,  RULE_ID INT(11) NOT NULL,  SETTING VARCHAR(500),  PRIMARY KEY (UDF_ID,RULE_ID)) ;
--
-- Table structure for table `QOE_GLOBAL_SETTINGS`
--
DROP TABLE IF EXISTS `QOE_GLOBAL_SETTINGS`;
CREATE TABLE QOE_GLOBAL_SETTINGS(ID INT(11) NOT NULL AUTO_INCREMENT,SUBS_CONFLICT_RESOLUTION_TIMEOUT INT(2) NOT NULL,QOE_RCC_ENABLE_DEFAULT BOOLEAN NOT NULL,QOE_RET_ENABLE_DEFAULT BOOLEAN NOT NULL,USE_GLOBAL_QOE_BANDWIDTH BOOLEAN NOT NULL,ALLOW_RCC_OVERSUBSCRIPTION  BOOLEAN NOT NULL,PRIMARY KEY (ID)) ;
--
-- Table structure for table `SET_TOP_BOXES_AUDIT`
--
DROP TABLE IF EXISTS `SET_TOP_BOXES_AUDIT`;
CREATE TABLE SET_TOP_BOXES_AUDIT(ACTION VARCHAR(10),EQUIPMENT_ID INT(11),SERIAL_NUMBER VARCHAR(20),INTERNAL_IP_ADDRESS VARCHAR(20),EXTERNAL_IP_ADDRESS VARCHAR(20),MAC_ADDRESS VARCHAR(20),HW_VERSION VARCHAR(20),SW_VERSION VARCHAR(50),UI_VERSION VARCHAR(50),ASSIGNEDTO_SUBSCRIBER_ID VARCHAR(11),LOCATION_ID INT(11), ASSIGNMENT_STATUS VARCHAR(10),OVERRIDING_DEFAULT TINYINT(1),MAX_BANDWIDTH_UPDATE INT(3),TV_QUALITY_INTEREST VARCHAR(20),STB_PROFILE VARCHAR(20),TIME_OF_ASSIGNMENT BIGINT,STB_NAME VARCHAR(30),CAS_DEVICE_ID VARCHAR(30),VMX_CLIENT_ID VARCHAR(30),SUPPORTED_MODES VARCHAR(40),CONNECTION_MODE VARCHAR(30),DRM_SYNC_STATUS TINYINT(1),LAST_UPDATED_ON BIGINT,LAST_UPDATED_BY VARCHAR(30)) ;
ALTER TABLE SET_TOP_BOXES ADD CONSTRAINT FK_STB_HARDWARE_HWVERSION FOREIGN KEY (HW_VERSION) REFERENCES HARDWARE_VERSIONS(NAME);
ALTER TABLE SET_TOP_BOXES ADD CONSTRAINT FK_STB_SUB_SUBID FOREIGN KEY (ASSIGNEDTO_SUBSCRIBER_ID) REFERENCES SUBSCRIBERS(SUBSCRIBER_ID);

ALTER TABLE STB_SERVICE_PORT_MAPPINGS ADD CONSTRAINT FK_STB_SPM_EQUIPMENT_ID FOREIGN KEY (EQUIPMENT_ID) REFERENCES SET_TOP_BOXES(EQUIPMENT_ID);
ALTER TABLE STB_SERVICE_PORT_MAPPINGS ADD CONSTRAINT FK_STB_SPM_SERVICE_ID FOREIGN KEY (SERVICE_ID) REFERENCES DEFAULT_SERVICE_PORT_LINK(SERVICE_ID);
ALTER TABLE STB_PROPERTIES ADD CONSTRAINT FK_STB_PROPERTIES_EQUIPMENT_ID FOREIGN KEY (EQUIPMENT_ID) REFERENCES SET_TOP_BOXES(EQUIPMENT_ID);
ALTER TABLE `STB_UDF`  ADD CONSTRAINT `STB_UDF_EQUIPMENT_ID`  FOREIGN KEY (`EQUIPMENT_ID`)  REFERENCES `SET_TOP_BOXES` (`EQUIPMENT_ID`);
ALTER TABLE `STB_UDF`  ADD CONSTRAINT `STB_UDF_UDF_NAME`  FOREIGN KEY (`UDF_NAME`)  REFERENCES `USER_DEFINED_FIELDS` (`NAME`);
ALTER TABLE STB_ASSIGNED_RESOURCES ADD CONSTRAINT FK_STB_ASS_RESOURCES_EQUIPMENT_ID FOREIGN KEY (EQUIPMENT_ID) REFERENCES SET_TOP_BOXES(EQUIPMENT_ID);
ALTER TABLE STB_ASSIGNED_RESOURCES ADD CONSTRAINT FK_STB_ASS_RESOURCES_RESOURCE_ID FOREIGN KEY (RESOURCE_ID) REFERENCES RESOURCES(ID);
ALTER TABLE STB_RESOURCES_ALLOCATION ADD CONSTRAINT FK_STB_RESOURCES_ALLOC_EQUIPMENT_ID FOREIGN KEY (EQUIPMENT_ID) REFERENCES SET_TOP_BOXES(EQUIPMENT_ID);
ALTER TABLE STB_MAX_BW_ALLOWED_PER_QUALITY ADD CONSTRAINT FK_STB_MAX_BW_EQUIPMENT_ID FOREIGN KEY (EQUIPMENT_ID) REFERENCES SET_TOP_BOXES(EQUIPMENT_ID);

ALTER TABLE SUBSCRIBER_STB_LIMIT ADD CONSTRAINT FK_SUBSCRIBER_STB_LIMIT_SUBSCRIBER_ID FOREIGN KEY (SUBSCRIBER_ID) REFERENCES SUBSCRIBERS(SUBSCRIBER_ID);
ALTER TABLE LOCATIONS ADD CONSTRAINT FK_LOCATIONS_ID FOREIGN KEY (PARENT_ID) REFERENCES LOCATIONS(ID);
ALTER TABLE `UDF_RULES_MAPPING`  ADD CONSTRAINT `UDF_RULES_MAPPING_UDF_ID`  FOREIGN KEY (UDF_ID)  REFERENCES USER_DEFINED_FIELDS (ID);
ALTER TABLE `UDF_RULES_MAPPING`  ADD CONSTRAINT `UDF_RULES_MAPPING_RULE_ID`  FOREIGN KEY (RULE_ID)  REFERENCES UDF_RULES (RULE_ID);
alter table SET_TOP_BOXES change HW_VERSION HARDWARE_VERSION varchar(20);
alter table SET_TOP_BOXES MODIFY CONNECTION_MODE INT(20);
INSERT INTO  UDF_RULES (RULE_NAME,INPUT_PARAMETER,CLASS_NAME) VALUES ('Required',0,'com.acn.avs.stb.util.udfrule.UDFRequiredRule');
INSERT INTO  UDF_RULES (RULE_NAME,INPUT_PARAMETER,CLASS_NAME) VALUES ('Length',1,'com.acn.avs.stb.util.udfrule.UDFLengthRule');
INSERT INTO  UDF_RULES (RULE_NAME,INPUT_PARAMETER,CLASS_NAME) VALUES ('Numeric',0,'com.acn.avs.stb.util.udfrule.UDFNumericRule');
INSERT INTO  UDF_RULES (RULE_NAME,INPUT_PARAMETER,CLASS_NAME) VALUES ('Format',1,'com.acn.avs.stb.util.udfrule.UDFFormatRule');
INSERT INTO  UDF_RULES (RULE_NAME,INPUT_PARAMETER,CLASS_NAME) VALUES ('Unique',1,'com.acn.avs.stb.util.udfrule.UDFUniqueRule');
INSERT INTO DEFAULT_SERVICE_PORT_LINK (SERVICE_ID, SERVICE_NAME, DEFAULT_INTERNAL_PORT, DEFAULT_EXTERNAL_PORT, PROTOCOL, DEFAULT_SERVICE) VALUES (1, 'STB Trigger Update',9001, 9001, 'TCP', true);
INSERT INTO DEFAULT_SERVICE_PORT_LINK (SERVICE_ID, SERVICE_NAME, DEFAULT_INTERNAL_PORT, DEFAULT_EXTERNAL_PORT, PROTOCOL, DEFAULT_SERVICE) VALUES (2, 'Secure Shell', 22, 22, 'TCP', true);
INSERT INTO DEFAULT_SERVICE_PORT_LINK (SERVICE_ID, SERVICE_NAME, DEFAULT_INTERNAL_PORT, DEFAULT_EXTERNAL_PORT, PROTOCOL, DEFAULT_SERVICE) VALUES (3, 'UnicastPushMessaging', 8002, 8002, 'UDP', true);
INSERT INTO CONNECTION_MODES (CONNECTION_MODE, STATUS) VALUES ('Bridged','Enabled');
INSERT INTO CONNECTION_MODES (CONNECTION_MODE, STATUS) VALUES ('Implicit NAT','Disabled');
INSERT INTO CONNECTION_MODES (CONNECTION_MODE, STATUS) VALUES ('UPnP NAPT','Disabled');
INSERT INTO QOE_GLOBAL_SETTINGS (ID, SUBS_CONFLICT_RESOLUTION_TIMEOUT, QOE_RCC_ENABLE_DEFAULT, QOE_RET_ENABLE_DEFAULT, USE_GLOBAL_QOE_BANDWIDTH, ALLOW_RCC_OVERSUBSCRIPTION) VALUES (1, 10, 0, 0, 0, 0);
 INSERT INTO `SUBSCRIBERS`(`SUBSCRIBER_ID`,`ACCOUNT_NUMBER`,`NAME`,`SUBSCRIBER_BANDWIDTH_PROFILE`,`STATUS`) VALUES (102365,102365,'abc','bw2000',0);
INSERT INTO `SUBSCRIBERS`(`SUBSCRIBER_ID`,`ACCOUNT_NUMBER`,`NAME`,`SUBSCRIBER_BANDWIDTH_PROFILE`,`STATUS`) VALUES (156987,156987,'abc','bw2000',0);
INSERT INTO `SUBSCRIBERS`(`SUBSCRIBER_ID`,`ACCOUNT_NUMBER`,`NAME`,`SUBSCRIBER_BANDWIDTH_PROFILE`,`STATUS`) VALUES (145896,145896,'abc','bw2000',0);
INSERT INTO `SUBSCRIBERS`(`SUBSCRIBER_ID`,`ACCOUNT_NUMBER`,`NAME`,`SUBSCRIBER_BANDWIDTH_PROFILE`,`STATUS`) VALUES (1234,1234,'abc','bw2000',0);

-- ------------------------------------------------------------
-- Data for the table `SET_TOP_BOXES`
-- ------------------------------------------------------------
insert  into `SET_TOP_BOXES`(`EQUIPMENT_ID`,`SERIAL_NUMBER`,`INTERNAL_IP_ADDRESS`,`EXTERNAL_IP_ADDRESS`,`MAC_ADDRESS`,`HARDWARE_VERSION`,`SW_VERSION`,`UI_VERSION`,`ASSIGNEDTO_SUBSCRIBER_ID`,`LOCATION_ID`,`ASSIGNMENT_STATUS`,`OVERRIDING_DEFAULT`,`MAX_BANDWIDTH_UPDATE`,`TV_QUALITY_INTEREST`,`DATETIME_OF_ASSIGNMENT`,`STB_NAME`,`CAS_DEVICE_ID`,`VMX_CLIENT_ID`,`SUPPORTED_MODES`,`CONNECTION_MODE`,`DRM_SYNC_STATUS`,`LAST_UPDATED_ON`,`LAST_UPDATED_BY`) values (1012,'1',NULL,'10.10.10.10','ASB47EF',NULL,NULL,NULL,102365,'24','1',NULL,NULL,NULL,NULL,'STB1','ASB47EF',NULL,1,2,0,123456,'Manoj');

insert  into `SET_TOP_BOXES`(`EQUIPMENT_ID`,`SERIAL_NUMBER`,`INTERNAL_IP_ADDRESS`,`EXTERNAL_IP_ADDRESS`,`MAC_ADDRESS`,`HARDWARE_VERSION`,`SW_VERSION`,`UI_VERSION`,`ASSIGNEDTO_SUBSCRIBER_ID`,`LOCATION_ID`,`ASSIGNMENT_STATUS`,`OVERRIDING_DEFAULT`,`MAX_BANDWIDTH_UPDATE`,`TV_QUALITY_INTEREST`,`DATETIME_OF_ASSIGNMENT`,`STB_NAME`,`CAS_DEVICE_ID`,`VMX_CLIENT_ID`,`SUPPORTED_MODES`,`CONNECTION_MODE`,`DRM_SYNC_STATUS`,`LAST_UPDATED_ON`,`LAST_UPDATED_BY`) values (1027,'1',NULL,'10.10.10.10','MK5567F',NULL,NULL,NULL,145896,'24','1',NULL,NULL,NULL,NULL,'STB1','MK5567F',NULL,1,1,0,123456,'Manoj');

insert  into `SET_TOP_BOXES`(`EQUIPMENT_ID`,`SERIAL_NUMBER`,`INTERNAL_IP_ADDRESS`,`EXTERNAL_IP_ADDRESS`,`MAC_ADDRESS`,`HARDWARE_VERSION`,`SW_VERSION`,`UI_VERSION`,`ASSIGNEDTO_SUBSCRIBER_ID`,`LOCATION_ID`,`ASSIGNMENT_STATUS`,`OVERRIDING_DEFAULT`,`MAX_BANDWIDTH_UPDATE`,`TV_QUALITY_INTEREST`,`DATETIME_OF_ASSIGNMENT`,`STB_NAME`,`CAS_DEVICE_ID`,`VMX_CLIENT_ID`,`SUPPORTED_MODES`,`CONNECTION_MODE`,`DRM_SYNC_STATUS`,`LAST_UPDATED_ON`,`LAST_UPDATED_BY`) values (1017,'1',NULL,'10.10.10.10','FB4567F',NULL,NULL,NULL,156987,'24','1',NULL,NULL,NULL,NULL,'STB1','FB4567F',NULL,1,1,0,123456,'Manoj');

insert  into `SET_TOP_BOXES`(`EQUIPMENT_ID`,`SERIAL_NUMBER`,`INTERNAL_IP_ADDRESS`,`EXTERNAL_IP_ADDRESS`,`MAC_ADDRESS`,`HARDWARE_VERSION`,`SW_VERSION`,`UI_VERSION`,`ASSIGNEDTO_SUBSCRIBER_ID`,`LOCATION_ID`,`ASSIGNMENT_STATUS`,`OVERRIDING_DEFAULT`,`MAX_BANDWIDTH_UPDATE`,`TV_QUALITY_INTEREST`,`DATETIME_OF_ASSIGNMENT`,`STB_NAME`,`CAS_DEVICE_ID`,`VMX_CLIENT_ID`,`SUPPORTED_MODES`,`CONNECTION_MODE`,`DRM_SYNC_STATUS`,`LAST_UPDATED_ON`,`LAST_UPDATED_BY`) values (1111,'1',NULL,'10.10.10.11','MAC1111',NULL,NULL,NULL,1234,'24','1',NULL,NULL,NULL,NULL,'STB1','MAC1111',NULL,1,3,0,123456,'Manoj');

insert  into `SET_TOP_BOXES`(`EQUIPMENT_ID`,`SERIAL_NUMBER`,`INTERNAL_IP_ADDRESS`,`EXTERNAL_IP_ADDRESS`,`MAC_ADDRESS`,`HARDWARE_VERSION`,`SW_VERSION`,`UI_VERSION`,`ASSIGNEDTO_SUBSCRIBER_ID`,`LOCATION_ID`,`ASSIGNMENT_STATUS`,`OVERRIDING_DEFAULT`,`MAX_BANDWIDTH_UPDATE`,`TV_QUALITY_INTEREST`,`DATETIME_OF_ASSIGNMENT`,`STB_NAME`,`CAS_DEVICE_ID`,`VMX_CLIENT_ID`,`SUPPORTED_MODES`,`CONNECTION_MODE`,`DRM_SYNC_STATUS`,`LAST_UPDATED_ON`,`LAST_UPDATED_BY`) values (1112,'1',NULL,'10.10.10.10','MAC1112',NULL,NULL,NULL,102365,'24','1',NULL,NULL,NULL,NULL,'STB1','MAC1112',NULL,1,2,0,123456,'Manoj');

insert  into `SET_TOP_BOXES`(`EQUIPMENT_ID`,`SERIAL_NUMBER`,`INTERNAL_IP_ADDRESS`,`EXTERNAL_IP_ADDRESS`,`MAC_ADDRESS`,`HARDWARE_VERSION`,`SW_VERSION`,`UI_VERSION`,`ASSIGNEDTO_SUBSCRIBER_ID`,`LOCATION_ID`,`ASSIGNMENT_STATUS`,`OVERRIDING_DEFAULT`,`MAX_BANDWIDTH_UPDATE`,`TV_QUALITY_INTEREST`,`DATETIME_OF_ASSIGNMENT`,`STB_NAME`,`CAS_DEVICE_ID`,`VMX_CLIENT_ID`,`SUPPORTED_MODES`,`CONNECTION_MODE`,`DRM_SYNC_STATUS`,`LAST_UPDATED_ON`,`LAST_UPDATED_BY`) values (1113,'1',NULL,'10.10.10.10','MAC1113',NULL,NULL,NULL,102365,'24','1',NULL,NULL,NULL,NULL,'STB1','MAC1113',NULL,1,2,0,123456,'Manoj');

insert  into `SET_TOP_BOXES`(`EQUIPMENT_ID`,`SERIAL_NUMBER`,`INTERNAL_IP_ADDRESS`,`EXTERNAL_IP_ADDRESS`,`MAC_ADDRESS`,`HARDWARE_VERSION`,`SW_VERSION`,`UI_VERSION`,`ASSIGNEDTO_SUBSCRIBER_ID`,`LOCATION_ID`,`ASSIGNMENT_STATUS`,`OVERRIDING_DEFAULT`,`MAX_BANDWIDTH_UPDATE`,`TV_QUALITY_INTEREST`,`DATETIME_OF_ASSIGNMENT`,`STB_NAME`,`CAS_DEVICE_ID`,`VMX_CLIENT_ID`,`SUPPORTED_MODES`,`CONNECTION_MODE`,`DRM_SYNC_STATUS`,`LAST_UPDATED_ON`,`LAST_UPDATED_BY`) values (1114,'1',NULL,'10.10.10.10','MAC1114',NULL,NULL,NULL,102365,'24','1',NULL,NULL,NULL,NULL,'STB1','MAC1114',NULL,1,2,0,123456,'Manoj');

-- ------------------------------------------------------------
-- Data for the table `STB_SERVICE_PORT_MAPPINGS`
-- ------------------------------------------------------------
insert  into `STB_SERVICE_PORT_MAPPINGS`(`EQUIPMENT_ID`,`INTERNAL_PORT`,`EXTERNAL_PORT`, `SERVICE_ID`) values (1012,8080,8090,1);

insert  into `STB_SERVICE_PORT_MAPPINGS`(`EQUIPMENT_ID`,`INTERNAL_PORT`,`EXTERNAL_PORT`, `SERVICE_ID`) values (1017,8080,8090, 2);

insert  into `STB_SERVICE_PORT_MAPPINGS`(`EQUIPMENT_ID`,`INTERNAL_PORT`,`EXTERNAL_PORT`, `SERVICE_ID`) values (1027,8080,8090, 3);

insert  into `STB_SERVICE_PORT_MAPPINGS`(`EQUIPMENT_ID`,`INTERNAL_PORT`,`EXTERNAL_PORT`, `SERVICE_ID`) values (1111,8080,8090,3);

insert  into `STB_SERVICE_PORT_MAPPINGS`(`EQUIPMENT_ID`,`INTERNAL_PORT`,`EXTERNAL_PORT`, `SERVICE_ID`) values (1112,8080,8090, 2);

-- ------------------------------------------------------------
-- Data for the table SERVICE_HARDWARE_MAPPING
-- ------------------------------------------------------------
INSERT INTO SERVICE_HARDWARE_MAPPING VALUES(1,'MOTOROLA');
INSERT INTO SERVICE_HARDWARE_MAPPING VALUES(2,'MOTOROLA');
INSERT INTO SERVICE_HARDWARE_MAPPING VALUES(3,'MOTOROLA');


insert into hardware_versions values('MOTOROLA', 1,0);

-- ------------------------------------------------------------
-- Table structure for table `TRIGGER_MESSAGE`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `TRIGGER_MESSAGE`;

CREATE TABLE `TRIGGER_MESSAGE` (  `ID` bigint(11) NOT NULL auto_increment COMMENT 'System generated identifier of trigger message.',  `MACADDRESS` varchar(20) NOT NULL COMMENT 'MACAddress of target STB',  `TRIGGER_TYPE` varchar(75) DEFAULT NULL COMMENT 'Trigger info for trigger message',  `TIMESTAMP` bigint(13) NOT NULL COMMENT 'Timestamp of triggering',  `RETRY_COUNT` smallint(11) NOT NULL COMMENT 'Number of retries done',  `ERROR_MESSAGE` varchar(1024) NOT NULL COMMENT 'Reason for failure of request',   `INSTANCE_ID` bigint(12) NOT NULL,  PRIMARY KEY (`ID`)) ;

-- ------------------------------------------------------------
-- Table structure for table `TRIGGER_MAPPING`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `TRIGGER_MAPPING`;

CREATE TABLE `TRIGGER_MAPPING` (  `TRIGGER_TYPE` varchar(30) NOT NULL COMMENT 'Trigger Type',  `SERVICE_NAME` varchar(30) NOT NULL,  PRIMARY KEY (`TRIGGER_TYPE`)) ;


-- ------------------------------------------------------------
-- Data for the table `TRIGGER_MAPPING`
-- ------------------------------------------------------------
INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES ('T_SUBSCRIBER_INFO','STB Trigger Update');

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES ('T_FAVORITES','STB Trigger Update');

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES ('T_CONTENT_RENTAL','STB Trigger Update');

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES ('T_NPVR','STB Trigger Update');

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES ('T_CONTACTS','STB Trigger Update');

INSERT INTO `TRIGGER_MAPPING` (`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES ('T_REMINDERS','STB Trigger Update');

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES ('T_BOOKMARK','STB Trigger Update');

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES ('S_MESSAGE_INFO','UnicastPushMessaging');

INSERT INTO `TRIGGER_MAPPING`(`TRIGGER_TYPE`,`SERVICE_NAME`) VALUES ('T_USER_PREFERENCES','STB Trigger Update');

DROP TABLE IF EXISTS `DB_VERSION`;
CREATE TABLE DB_VERSION (PRODUCT_VERSION VARCHAR(50) NOT NULL, MINOR_VERSION INT(3) NOT NULL, CREATOR VARCHAR(50), DATE_CREATED DATE, DATE_APPLIED DATE NOT NULL, PRIMARY KEY (PRODUCT_VERSION,MINOR_VERSION)    );

INSERT INTO DB_VERSION (PRODUCT_VERSION, MINOR_VERSION, CREATOR, DATE_CREATED, DATE_APPLIED)     VALUES ('AVS 6.1', 0, 'RSI', PARSEDATETIME(FORMATDATETIME('2016-12-22', 'yyyy-MM-dd'), 'yyyy-MM-dd'), CURRENT_TIMESTAMP());

INSERT INTO `TRIGGER_MESSAGE`(`MACADDRESS`,`TRIGGER_TYPE`,`TIMESTAMP`,`RETRY_COUNT`,`ERROR_MESSAGE`,`INSTANCE_ID`) VALUES ('AD68','S_MESSAGE_INFO', 123654,1,'abc',10219);