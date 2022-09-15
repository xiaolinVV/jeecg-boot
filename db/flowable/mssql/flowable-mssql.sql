/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.196
 Source Server Type    : SQL Server
 Source Server Version : 10501600
 Source Host           : 192.168.1.196:1433
 Source Catalog        : health-center
 Source Schema         : dbo

 Target Server Type    : SQL Server
 Target Server Version : 10501600
 File Encoding         : 65001

 Date: 15/09/2022 17:09:49
*/


-- ----------------------------
-- Table structure for ACT_EVT_LOG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_EVT_LOG]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_EVT_LOG]
GO

CREATE TABLE [dbo].[ACT_EVT_LOG] (
  [LOG_NR_] bigint  NOT NULL,
  [TYPE_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TIME_STAMP_] datetime  NOT NULL,
  [USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_] varbinary(max)  NULL,
  [LOCK_OWNER_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOCK_TIME_] datetime  NULL,
  [IS_PROCESSED_] tinyint  NULL
)
GO

ALTER TABLE [dbo].[ACT_EVT_LOG] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_EVT_LOG
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_GE_BYTEARRAY
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_GE_BYTEARRAY]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_GE_BYTEARRAY]
GO

CREATE TABLE [dbo].[ACT_GE_BYTEARRAY] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOYMENT_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [BYTES_] varbinary(max)  NULL,
  [GENERATED_] tinyint  NULL
)
GO

ALTER TABLE [dbo].[ACT_GE_BYTEARRAY] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_GE_BYTEARRAY
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_GE_PROPERTY
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_GE_PROPERTY]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_GE_PROPERTY]
GO

CREATE TABLE [dbo].[ACT_GE_PROPERTY] (
  [NAME_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VALUE_] nvarchar(300) COLLATE Chinese_PRC_CI_AS  NULL,
  [REV_] int  NULL
)
GO

ALTER TABLE [dbo].[ACT_GE_PROPERTY] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_GE_PROPERTY
-- ----------------------------
BEGIN TRANSACTION
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'batch.schema.version', N'6.5.0.6', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'cfg.execution-related-entities-count', N'true', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'cfg.task-related-entities-count', N'true', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'common.schema.version', N'6.5.0.6', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'entitylink.schema.version', N'6.5.0.6', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'eventsubscription.schema.version', N'6.5.0.6', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'identitylink.schema.version', N'6.5.0.6', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'job.schema.version', N'6.5.0.6', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'next.dbid', N'1', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'schema.history', N'create(6.5.0.6)', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'schema.version', N'6.5.0.6', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'task.schema.version', N'6.5.0.6', N'1')
GO

INSERT INTO [dbo].[ACT_GE_PROPERTY] VALUES (N'variable.schema.version', N'6.5.0.6', N'1')
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_ACTINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_ACTINST]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_ACTINST]
GO

CREATE TABLE [dbo].[ACT_HI_ACTINST] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CALL_PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACT_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACT_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ASSIGNEE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_TIME_] datetime  NOT NULL,
  [END_TIME_] datetime  NULL,
  [DURATION_] bigint  NULL,
  [DELETE_REASON_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_ACTINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_ACTINST
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_ATTACHMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_ATTACHMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_ATTACHMENT]
GO

CREATE TABLE [dbo].[ACT_HI_ATTACHMENT] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [URL_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [CONTENT_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TIME_] datetime  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_ATTACHMENT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_ATTACHMENT
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_COMMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_COMMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_COMMENT]
GO

CREATE TABLE [dbo].[ACT_HI_COMMENT] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TIME_] datetime  NOT NULL,
  [USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACTION_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [MESSAGE_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_MSG_] varbinary(max)  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_COMMENT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_COMMENT
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_DETAIL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_DETAIL]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_DETAIL]
GO

CREATE TABLE [dbo].[ACT_HI_DETAIL] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACT_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VAR_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REV_] int  NULL,
  [TIME_] datetime  NOT NULL,
  [BYTEARRAY_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DOUBLE_] float(53)  NULL,
  [LONG_] bigint  NULL,
  [TEXT_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TEXT2_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_DETAIL] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_DETAIL
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_ENTITYLINK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_ENTITYLINK]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_ENTITYLINK]
GO

CREATE TABLE [dbo].[ACT_HI_ENTITYLINK] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [LINK_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REF_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REF_SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REF_SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HIERARCHY_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_ENTITYLINK] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_ENTITYLINK
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_IDENTITYLINK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_IDENTITYLINK]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_IDENTITYLINK]
GO

CREATE TABLE [dbo].[ACT_HI_IDENTITYLINK] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [GROUP_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_IDENTITYLINK] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_IDENTITYLINK
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_PROCINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_PROCINST]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_PROCINST]
GO

CREATE TABLE [dbo].[ACT_HI_PROCINST] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BUSINESS_KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [START_TIME_] datetime  NOT NULL,
  [END_TIME_] datetime  NULL,
  [DURATION_] bigint  NULL,
  [START_USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [END_ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUPER_PROCESS_INSTANCE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DELETE_REASON_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CALLBACK_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CALLBACK_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REFERENCE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REFERENCE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_PROCINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_PROCINST
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_TASKINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_TASKINST]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_TASKINST]
GO

CREATE TABLE [dbo].[ACT_HI_TASKINST] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_DEF_KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROPAGATED_STAGE_INST_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ASSIGNEE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_TIME_] datetime  NOT NULL,
  [CLAIM_TIME_] datetime  NULL,
  [END_TIME_] datetime  NULL,
  [DURATION_] bigint  NULL,
  [DELETE_REASON_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRIORITY_] int  NULL,
  [DUE_DATE_] datetime  NULL,
  [FORM_KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CATEGORY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_UPDATED_TIME_] datetime  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_TASKINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_TASKINST
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_TSK_LOG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_TSK_LOG]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_TSK_LOG]
GO

CREATE TABLE [dbo].[ACT_HI_TSK_LOG] (
  [ID_] bigint  NOT NULL,
  [TYPE_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [TIME_STAMP_] datetime  NOT NULL,
  [USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_TSK_LOG] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_TSK_LOG
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_HI_VARINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_VARINST]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_VARINST]
GO

CREATE TABLE [dbo].[ACT_HI_VARINST] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VAR_TYPE_] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [BYTEARRAY_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DOUBLE_] float(53)  NULL,
  [LONG_] bigint  NULL,
  [TEXT_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TEXT2_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [LAST_UPDATED_TIME_] datetime  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_VARINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_HI_VARINST
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_ID_BYTEARRAY
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_ID_BYTEARRAY]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_ID_BYTEARRAY]
GO

CREATE TABLE [dbo].[ACT_ID_BYTEARRAY] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [BYTES_] varbinary(max)  NULL
)
GO

ALTER TABLE [dbo].[ACT_ID_BYTEARRAY] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_ID_BYTEARRAY
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_ID_GROUP
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_ID_GROUP]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_ID_GROUP]
GO

CREATE TABLE [dbo].[ACT_ID_GROUP] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_ID_GROUP] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_ID_GROUP
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_ID_INFO
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_ID_INFO]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_ID_INFO]
GO

CREATE TABLE [dbo].[ACT_ID_INFO] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [USER_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [VALUE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PASSWORD_] varbinary(max)  NULL,
  [PARENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_ID_INFO] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_ID_INFO
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_ID_MEMBERSHIP
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_ID_MEMBERSHIP]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_ID_MEMBERSHIP]
GO

CREATE TABLE [dbo].[ACT_ID_MEMBERSHIP] (
  [USER_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [GROUP_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[ACT_ID_MEMBERSHIP] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_ID_MEMBERSHIP
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_ID_PRIV
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_ID_PRIV]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_ID_PRIV]
GO

CREATE TABLE [dbo].[ACT_ID_PRIV] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[ACT_ID_PRIV] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_ID_PRIV
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_ID_PRIV_MAPPING
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_ID_PRIV_MAPPING]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_ID_PRIV_MAPPING]
GO

CREATE TABLE [dbo].[ACT_ID_PRIV_MAPPING] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PRIV_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [GROUP_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_ID_PRIV_MAPPING] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_ID_PRIV_MAPPING
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_ID_PROPERTY
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_ID_PROPERTY]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_ID_PROPERTY]
GO

CREATE TABLE [dbo].[ACT_ID_PROPERTY] (
  [NAME_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VALUE_] nvarchar(300) COLLATE Chinese_PRC_CI_AS  NULL,
  [REV_] int  NULL
)
GO

ALTER TABLE [dbo].[ACT_ID_PROPERTY] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_ID_PROPERTY
-- ----------------------------
BEGIN TRANSACTION
GO

INSERT INTO [dbo].[ACT_ID_PROPERTY] VALUES (N'schema.version', N'6.5.0.6', N'1')
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_ID_TOKEN
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_ID_TOKEN]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_ID_TOKEN]
GO

CREATE TABLE [dbo].[ACT_ID_TOKEN] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [TOKEN_VALUE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TOKEN_DATE_] datetime  NULL,
  [IP_ADDRESS_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [USER_AGENT_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TOKEN_DATA_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_ID_TOKEN] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_ID_TOKEN
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_ID_USER
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_ID_USER]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_ID_USER]
GO

CREATE TABLE [dbo].[ACT_ID_USER] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [FIRST_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DISPLAY_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [EMAIL_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PWD_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PICTURE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_ID_USER] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_ID_USER
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_PROCDEF_INFO
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_PROCDEF_INFO]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_PROCDEF_INFO]
GO

CREATE TABLE [dbo].[ACT_PROCDEF_INFO] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [INFO_JSON_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_PROCDEF_INFO] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_PROCDEF_INFO
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RE_DEPLOYMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RE_DEPLOYMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RE_DEPLOYMENT]
GO

CREATE TABLE [dbo].[ACT_RE_DEPLOYMENT] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CATEGORY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOY_TIME_] datetime  NULL,
  [DERIVED_FROM_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DERIVED_FROM_ROOT_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_DEPLOYMENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ENGINE_VERSION_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RE_DEPLOYMENT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RE_DEPLOYMENT
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RE_MODEL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RE_MODEL]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RE_MODEL]
GO

CREATE TABLE [dbo].[ACT_RE_MODEL] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CATEGORY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [LAST_UPDATE_TIME_] datetime  NULL,
  [VERSION_] int  NULL,
  [META_INFO_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOYMENT_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EDITOR_SOURCE_VALUE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EDITOR_SOURCE_EXTRA_VALUE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RE_MODEL] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RE_MODEL
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RE_PROCDEF
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RE_PROCDEF]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RE_PROCDEF]
GO

CREATE TABLE [dbo].[ACT_RE_PROCDEF] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [CATEGORY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VERSION_] int  NOT NULL,
  [DEPLOYMENT_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_NAME_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DGRM_RESOURCE_NAME_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [HAS_START_FORM_KEY_] tinyint  NULL,
  [HAS_GRAPHICAL_NOTATION_] tinyint  NULL,
  [SUSPENSION_STATE_] int  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ENGINE_VERSION_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DERIVED_FROM_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DERIVED_FROM_ROOT_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DERIVED_VERSION_] int  NOT NULL
)
GO

ALTER TABLE [dbo].[ACT_RE_PROCDEF] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RE_PROCDEF
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_ACTINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_ACTINST]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_ACTINST]
GO

CREATE TABLE [dbo].[ACT_RU_ACTINST] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CALL_PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACT_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACT_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ASSIGNEE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_TIME_] datetime  NOT NULL,
  [END_TIME_] datetime  NULL,
  [DURATION_] bigint  NULL,
  [DELETE_REASON_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_ACTINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_ACTINST
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_DEADLETTER_JOB
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_DEADLETTER_JOB]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_DEADLETTER_JOB]
GO

CREATE TABLE [dbo].[ACT_RU_DEADLETTER_JOB] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EXCLUSIVE_] tinyint  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROCESS_INSTANCE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ELEMENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ELEMENT_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCEPTION_STACK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCEPTION_MSG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DUEDATE_] datetime  NULL,
  [REPEAT_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_CFG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [CUSTOM_VALUES_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_DEADLETTER_JOB] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_DEADLETTER_JOB
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_ENTITYLINK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_ENTITYLINK]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_ENTITYLINK]
GO

CREATE TABLE [dbo].[ACT_RU_ENTITYLINK] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [CREATE_TIME_] datetime  NULL,
  [LINK_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REF_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REF_SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REF_SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HIERARCHY_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_ENTITYLINK] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_ENTITYLINK
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_EVENT_SUBSCR
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_EVENT_SUBSCR]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_EVENT_SUBSCR]
GO

CREATE TABLE [dbo].[ACT_RU_EVENT_SUBSCR] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [EVENT_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EVENT_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACTIVITY_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CONFIGURATION_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_] datetime  NOT NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_EVENT_SUBSCR] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_EVENT_SUBSCR
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_EXECUTION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_EXECUTION]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_EXECUTION]
GO

CREATE TABLE [dbo].[ACT_RU_EXECUTION] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [BUSINESS_KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUPER_EXEC_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROOT_PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_ACTIVE_] tinyint  NULL,
  [IS_CONCURRENT_] tinyint  NULL,
  [IS_SCOPE_] tinyint  NULL,
  [IS_EVENT_SCOPE_] tinyint  NULL,
  [IS_MI_ROOT_] tinyint  NULL,
  [SUSPENSION_STATE_] int  NULL,
  [CACHED_ENT_STATE_] int  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_TIME_] datetime  NULL,
  [START_USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOCK_TIME_] datetime  NULL,
  [IS_COUNT_ENABLED_] tinyint  NULL,
  [EVT_SUBSCR_COUNT_] int  NULL,
  [TASK_COUNT_] int  NULL,
  [JOB_COUNT_] int  NULL,
  [TIMER_JOB_COUNT_] int  NULL,
  [SUSP_JOB_COUNT_] int  NULL,
  [DEADLETTER_JOB_COUNT_] int  NULL,
  [VAR_COUNT_] int  NULL,
  [ID_LINK_COUNT_] int  NULL,
  [CALLBACK_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CALLBACK_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REFERENCE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [REFERENCE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROPAGATED_STAGE_INST_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_EXECUTION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_EXECUTION
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_HISTORY_JOB
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_HISTORY_JOB]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_HISTORY_JOB]
GO

CREATE TABLE [dbo].[ACT_RU_HISTORY_JOB] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [LOCK_EXP_TIME_] datetime  NULL,
  [LOCK_OWNER_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [RETRIES_] int  NULL,
  [EXCEPTION_STACK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCEPTION_MSG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_CFG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [CUSTOM_VALUES_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ADV_HANDLER_CFG_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_HISTORY_JOB] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_HISTORY_JOB
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_IDENTITYLINK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_IDENTITYLINK]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_IDENTITYLINK]
GO

CREATE TABLE [dbo].[ACT_RU_IDENTITYLINK] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [GROUP_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_IDENTITYLINK] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_IDENTITYLINK
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_JOB
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_JOB]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_JOB]
GO

CREATE TABLE [dbo].[ACT_RU_JOB] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [LOCK_EXP_TIME_] datetime  NULL,
  [LOCK_OWNER_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCLUSIVE_] tinyint  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROCESS_INSTANCE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ELEMENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ELEMENT_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [RETRIES_] int  NULL,
  [EXCEPTION_STACK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCEPTION_MSG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DUEDATE_] datetime  NULL,
  [REPEAT_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_CFG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [CUSTOM_VALUES_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_JOB] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_JOB
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_SUSPENDED_JOB
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_SUSPENDED_JOB]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_SUSPENDED_JOB]
GO

CREATE TABLE [dbo].[ACT_RU_SUSPENDED_JOB] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EXCLUSIVE_] tinyint  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROCESS_INSTANCE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ELEMENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ELEMENT_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [RETRIES_] int  NULL,
  [EXCEPTION_STACK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCEPTION_MSG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DUEDATE_] datetime  NULL,
  [REPEAT_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_CFG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [CUSTOM_VALUES_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_SUSPENDED_JOB] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_SUSPENDED_JOB
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_TASK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_TASK]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_TASK]
GO

CREATE TABLE [dbo].[ACT_RU_TASK] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROPAGATED_STAGE_INST_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_DEF_KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ASSIGNEE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DELEGATION_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRIORITY_] int  NULL,
  [CREATE_TIME_] datetime  NULL,
  [DUE_DATE_] datetime  NULL,
  [CATEGORY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUSPENSION_STATE_] int  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [FORM_KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CLAIM_TIME_] datetime  NULL,
  [IS_COUNT_ENABLED_] tinyint  NULL,
  [VAR_COUNT_] int  NULL,
  [ID_LINK_COUNT_] int  NULL,
  [SUB_TASK_COUNT_] int  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_TASK] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_TASK
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_TIMER_JOB
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_TIMER_JOB]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_TIMER_JOB]
GO

CREATE TABLE [dbo].[ACT_RU_TIMER_JOB] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [LOCK_EXP_TIME_] datetime  NULL,
  [LOCK_OWNER_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCLUSIVE_] tinyint  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROCESS_INSTANCE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ELEMENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ELEMENT_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_DEFINITION_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [RETRIES_] int  NULL,
  [EXCEPTION_STACK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCEPTION_MSG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DUEDATE_] datetime  NULL,
  [REPEAT_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_CFG_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [CUSTOM_VALUES_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_TIMER_JOB] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_TIMER_JOB
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for ACT_RU_VARIABLE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_VARIABLE]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_VARIABLE]
GO

CREATE TABLE [dbo].[ACT_RU_VARIABLE] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [BYTEARRAY_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DOUBLE_] float(53)  NULL,
  [LONG_] bigint  NULL,
  [TEXT_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TEXT2_] nvarchar(4000) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_VARIABLE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of ACT_RU_VARIABLE
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Primary Key structure for table ACT_EVT_LOG
-- ----------------------------
ALTER TABLE [dbo].[ACT_EVT_LOG] ADD CONSTRAINT [PK__ACT_EVT___DE8852D87D0E9093] PRIMARY KEY CLUSTERED ([LOG_NR_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_GE_BYTEARRAY
-- ----------------------------
ALTER TABLE [dbo].[ACT_GE_BYTEARRAY] ADD CONSTRAINT [PK__ACT_GE_B__C4971C0F7FEAFD3E] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_GE_PROPERTY
-- ----------------------------
ALTER TABLE [dbo].[ACT_GE_PROPERTY] ADD CONSTRAINT [PK__ACT_GE_P__A7BE44DE3631FF56] PRIMARY KEY CLUSTERED ([NAME_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_HI_ACTINST
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_HI_ACT_INST_START]
ON [dbo].[ACT_HI_ACTINST] (
  [START_TIME_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_ACT_INST_END]
ON [dbo].[ACT_HI_ACTINST] (
  [END_TIME_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_ACT_INST_PROCINST]
ON [dbo].[ACT_HI_ACTINST] (
  [PROC_INST_ID_] ASC,
  [ACT_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_ACT_INST_EXEC]
ON [dbo].[ACT_HI_ACTINST] (
  [EXECUTION_ID_] ASC,
  [ACT_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_ACTINST
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_ACTINST] ADD CONSTRAINT [PK__ACT_HI_A__C4971C0F753864A1] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_ATTACHMENT
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_ATTACHMENT] ADD CONSTRAINT [PK__ACT_HI_A__C4971C0F02C769E9] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_COMMENT
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_COMMENT] ADD CONSTRAINT [PK__ACT_HI_C__C4971C0F05A3D694] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_HI_DETAIL
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_HI_DETAIL_PROC_INST]
ON [dbo].[ACT_HI_DETAIL] (
  [PROC_INST_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_DETAIL_ACT_INST]
ON [dbo].[ACT_HI_DETAIL] (
  [ACT_INST_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_DETAIL_TIME]
ON [dbo].[ACT_HI_DETAIL] (
  [TIME_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_DETAIL_NAME]
ON [dbo].[ACT_HI_DETAIL] (
  [NAME_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_DETAIL_TASK_ID]
ON [dbo].[ACT_HI_DETAIL] (
  [TASK_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_DETAIL
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_DETAIL] ADD CONSTRAINT [PK__ACT_HI_D__C4971C0F0880433F] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_HI_ENTITYLINK
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_HI_ENT_LNK_SCOPE]
ON [dbo].[ACT_HI_ENTITYLINK] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC,
  [LINK_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_ENT_LNK_SCOPE_DEF]
ON [dbo].[ACT_HI_ENTITYLINK] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC,
  [LINK_TYPE_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_ENTITYLINK
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_ENTITYLINK] ADD CONSTRAINT [PK__ACT_HI_E__C4971C0F0E391C95] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_HI_IDENTITYLINK
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_HI_IDENT_LNK_USER]
ON [dbo].[ACT_HI_IDENTITYLINK] (
  [USER_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_IDENT_LNK_SCOPE]
ON [dbo].[ACT_HI_IDENTITYLINK] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_IDENT_LNK_SUB_SCOPE]
ON [dbo].[ACT_HI_IDENTITYLINK] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_IDENT_LNK_SCOPE_DEF]
ON [dbo].[ACT_HI_IDENTITYLINK] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_IDENT_LNK_TASK]
ON [dbo].[ACT_HI_IDENTITYLINK] (
  [TASK_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_IDENT_LNK_PROCINST]
ON [dbo].[ACT_HI_IDENTITYLINK] (
  [PROC_INST_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_IDENTITYLINK
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_IDENTITYLINK] ADD CONSTRAINT [PK__ACT_HI_I__C4971C0F0B5CAFEA] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_HI_PROCINST
-- ----------------------------
CREATE UNIQUE NONCLUSTERED INDEX [proc_inst_id_]
ON [dbo].[ACT_HI_PROCINST] (
  [PROC_INST_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_PRO_INST_END]
ON [dbo].[ACT_HI_PROCINST] (
  [END_TIME_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_PRO_I_BUSKEY]
ON [dbo].[ACT_HI_PROCINST] (
  [BUSINESS_KEY_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_PROCINST
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_PROCINST] ADD CONSTRAINT [PK__ACT_HI_P__C4971C0F11158940] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_HI_TASKINST
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_HI_TASK_SCOPE]
ON [dbo].[ACT_HI_TASKINST] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_TASK_SUB_SCOPE]
ON [dbo].[ACT_HI_TASKINST] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_TASK_SCOPE_DEF]
ON [dbo].[ACT_HI_TASKINST] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_TASK_INST_PROCINST]
ON [dbo].[ACT_HI_TASKINST] (
  [PROC_INST_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_TASKINST
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_TASKINST] ADD CONSTRAINT [PK__ACT_HI_T__C4971C0F13F1F5EB] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_TSK_LOG
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_TSK_LOG] ADD CONSTRAINT [PK__ACT_HI_T__C4971C0F16CE6296] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_HI_VARINST
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_HI_PROCVAR_NAME_TYPE]
ON [dbo].[ACT_HI_VARINST] (
  [NAME_] ASC,
  [VAR_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_VAR_SCOPE_ID_TYPE]
ON [dbo].[ACT_HI_VARINST] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_VAR_SUB_ID_TYPE]
ON [dbo].[ACT_HI_VARINST] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_PROCVAR_PROC_INST]
ON [dbo].[ACT_HI_VARINST] (
  [PROC_INST_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_PROCVAR_TASK_ID]
ON [dbo].[ACT_HI_VARINST] (
  [TASK_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_HI_PROCVAR_EXE]
ON [dbo].[ACT_HI_VARINST] (
  [EXECUTION_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_VARINST
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_VARINST] ADD CONSTRAINT [PK__ACT_HI_V__C4971C0F19AACF41] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_ID_BYTEARRAY
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_BYTEARRAY] ADD CONSTRAINT [PK__ACT_ID_B__C4971C0F1C873BEC] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_ID_GROUP
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_GROUP] ADD CONSTRAINT [PK__ACT_ID_G__C4971C0F1F63A897] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_ID_INFO
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_INFO] ADD CONSTRAINT [PK__ACT_ID_I__C4971C0F251C81ED] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_ID_MEMBERSHIP
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_MEMBERSHIP] ADD CONSTRAINT [PK__ACT_ID_M__C2371B0F22401542] PRIMARY KEY CLUSTERED ([USER_ID_], [GROUP_ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_ID_PRIV
-- ----------------------------
CREATE UNIQUE NONCLUSTERED INDEX [ACT_UNIQ_PRIV_NAME]
ON [dbo].[ACT_ID_PRIV] (
  [NAME_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_ID_PRIV
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_PRIV] ADD CONSTRAINT [PK__ACT_ID_P__C4971C0F27F8EE98] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_ID_PRIV_MAPPING
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_PRIV_USER]
ON [dbo].[ACT_ID_PRIV_MAPPING] (
  [USER_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_PRIV_GROUP]
ON [dbo].[ACT_ID_PRIV_MAPPING] (
  [GROUP_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_ID_PRIV_MAPPING
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_PRIV_MAPPING] ADD CONSTRAINT [PK__ACT_ID_P__C4971C0F2AD55B43] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_ID_PROPERTY
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_PROPERTY] ADD CONSTRAINT [PK__ACT_ID_P__A7BE44DE2DB1C7EE] PRIMARY KEY CLUSTERED ([NAME_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_ID_TOKEN
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_TOKEN] ADD CONSTRAINT [PK__ACT_ID_T__C4971C0F308E3499] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_ID_USER
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_USER] ADD CONSTRAINT [PK__ACT_ID_U__C4971C0F36470DEF] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_PROCDEF_INFO
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_INFO_PROCDEF]
ON [dbo].[ACT_PROCDEF_INFO] (
  [PROC_DEF_ID_] ASC
)
GO

CREATE UNIQUE NONCLUSTERED INDEX [ACT_UNIQ_INFO_PROCDEF]
ON [dbo].[ACT_PROCDEF_INFO] (
  [PROC_DEF_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_PROCDEF_INFO
-- ----------------------------
ALTER TABLE [dbo].[ACT_PROCDEF_INFO] ADD CONSTRAINT [PK__ACT_PROC__C4971C0F336AA144] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RE_DEPLOYMENT
-- ----------------------------
ALTER TABLE [dbo].[ACT_RE_DEPLOYMENT] ADD CONSTRAINT [PK__ACT_RE_D__C4971C0F39237A9A] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RE_MODEL
-- ----------------------------
ALTER TABLE [dbo].[ACT_RE_MODEL] ADD CONSTRAINT [PK__ACT_RE_M__C4971C0F3BFFE745] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RE_PROCDEF
-- ----------------------------
CREATE UNIQUE NONCLUSTERED INDEX [ACT_UNIQ_PROCDEF]
ON [dbo].[ACT_RE_PROCDEF] (
  [KEY_] ASC,
  [VERSION_] ASC,
  [DERIVED_VERSION_] ASC,
  [TENANT_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RE_PROCDEF
-- ----------------------------
ALTER TABLE [dbo].[ACT_RE_PROCDEF] ADD CONSTRAINT [PK__ACT_RE_P__C4971C0F44952D46] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_ACTINST
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_RU_ACTI_START]
ON [dbo].[ACT_RU_ACTINST] (
  [START_TIME_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_RU_ACTI_END]
ON [dbo].[ACT_RU_ACTINST] (
  [END_TIME_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_RU_ACTI_PROC]
ON [dbo].[ACT_RU_ACTINST] (
  [PROC_INST_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_RU_ACTI_PROC_ACT]
ON [dbo].[ACT_RU_ACTINST] (
  [PROC_INST_ID_] ASC,
  [ACT_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_RU_ACTI_EXEC]
ON [dbo].[ACT_RU_ACTINST] (
  [EXECUTION_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_RU_ACTI_EXEC_ACT]
ON [dbo].[ACT_RU_ACTINST] (
  [EXECUTION_ID_] ASC,
  [ACT_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_ACTINST
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_ACTINST] ADD CONSTRAINT [PK__ACT_RU_A__C4971C0F3EDC53F0] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_DEADLETTER_JOB
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_DEADLETTER_JOB_EXCEPTION_STACK_ID]
ON [dbo].[ACT_RU_DEADLETTER_JOB] (
  [EXCEPTION_STACK_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_DEADLETTER_JOB_CUSTOM_VALUES_ID]
ON [dbo].[ACT_RU_DEADLETTER_JOB] (
  [CUSTOM_VALUES_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_DJOB_SCOPE]
ON [dbo].[ACT_RU_DEADLETTER_JOB] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_DJOB_SUB_SCOPE]
ON [dbo].[ACT_RU_DEADLETTER_JOB] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_DJOB_SCOPE_DEF]
ON [dbo].[ACT_RU_DEADLETTER_JOB] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_DEADLETTER_JOB
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_DEADLETTER_JOB] ADD CONSTRAINT [PK__ACT_RU_D__C4971C0F41B8C09B] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_ENTITYLINK
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_ENT_LNK_SCOPE]
ON [dbo].[ACT_RU_ENTITYLINK] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC,
  [LINK_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_ENT_LNK_SCOPE_DEF]
ON [dbo].[ACT_RU_ENTITYLINK] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC,
  [LINK_TYPE_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_ENTITYLINK
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_ENTITYLINK] ADD CONSTRAINT [PK__ACT_RU_E__C4971C0F477199F1] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_EVENT_SUBSCR
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_EVENT_SUBSCR_CONFIG_]
ON [dbo].[ACT_RU_EVENT_SUBSCR] (
  [CONFIGURATION_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_EVENT_SUBSCR
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_EVENT_SUBSCR] ADD CONSTRAINT [PK__ACT_RU_E__C4971C0F4A4E069C] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_EXECUTION
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_EXEC_BUSKEY]
ON [dbo].[ACT_RU_EXECUTION] (
  [BUSINESS_KEY_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDC_EXEC_ROOT]
ON [dbo].[ACT_RU_EXECUTION] (
  [ROOT_PROC_INST_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_EXECUTION
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_EXECUTION] ADD CONSTRAINT [PK__ACT_RU_E__C4971C0F4D2A7347] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_HISTORY_JOB
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_HISTORY_JOB] ADD CONSTRAINT [PK__ACT_RU_H__C4971C0F5006DFF2] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_IDENTITYLINK
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_IDENT_LNK_USER]
ON [dbo].[ACT_RU_IDENTITYLINK] (
  [USER_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_IDENT_LNK_GROUP]
ON [dbo].[ACT_RU_IDENTITYLINK] (
  [GROUP_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_IDENT_LNK_SCOPE]
ON [dbo].[ACT_RU_IDENTITYLINK] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_IDENT_LNK_SUB_SCOPE]
ON [dbo].[ACT_RU_IDENTITYLINK] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_IDENT_LNK_SCOPE_DEF]
ON [dbo].[ACT_RU_IDENTITYLINK] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_ATHRZ_PROCEDEF]
ON [dbo].[ACT_RU_IDENTITYLINK] (
  [PROC_DEF_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_IDENTITYLINK
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_IDENTITYLINK] ADD CONSTRAINT [PK__ACT_RU_I__C4971C0F52E34C9D] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_JOB
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_JOB_EXCEPTION_STACK_ID]
ON [dbo].[ACT_RU_JOB] (
  [EXCEPTION_STACK_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_JOB_CUSTOM_VALUES_ID]
ON [dbo].[ACT_RU_JOB] (
  [CUSTOM_VALUES_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_JOB_SCOPE]
ON [dbo].[ACT_RU_JOB] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_JOB_SUB_SCOPE]
ON [dbo].[ACT_RU_JOB] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_JOB_SCOPE_DEF]
ON [dbo].[ACT_RU_JOB] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_JOB
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_JOB] ADD CONSTRAINT [PK__ACT_RU_J__C4971C0F5B78929E] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_SUSPENDED_JOB
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_SUSPENDED_JOB_EXCEPTION_STACK_ID]
ON [dbo].[ACT_RU_SUSPENDED_JOB] (
  [EXCEPTION_STACK_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_SUSPENDED_JOB_CUSTOM_VALUES_ID]
ON [dbo].[ACT_RU_SUSPENDED_JOB] (
  [CUSTOM_VALUES_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_SJOB_SCOPE]
ON [dbo].[ACT_RU_SUSPENDED_JOB] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_SJOB_SUB_SCOPE]
ON [dbo].[ACT_RU_SUSPENDED_JOB] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_SJOB_SCOPE_DEF]
ON [dbo].[ACT_RU_SUSPENDED_JOB] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_SUSPENDED_JOB
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_SUSPENDED_JOB] ADD CONSTRAINT [PK__ACT_RU_S__C4971C0F55BFB948] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_TASK
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_TASK_CREATE]
ON [dbo].[ACT_RU_TASK] (
  [CREATE_TIME_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_TASK_SCOPE]
ON [dbo].[ACT_RU_TASK] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_TASK_SUB_SCOPE]
ON [dbo].[ACT_RU_TASK] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_TASK_SCOPE_DEF]
ON [dbo].[ACT_RU_TASK] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_TASK
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_TASK] ADD CONSTRAINT [PK__ACT_RU_T__C4971C0F589C25F3] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_TIMER_JOB
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_TIMER_JOB_EXCEPTION_STACK_ID]
ON [dbo].[ACT_RU_TIMER_JOB] (
  [EXCEPTION_STACK_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_TIMER_JOB_CUSTOM_VALUES_ID]
ON [dbo].[ACT_RU_TIMER_JOB] (
  [CUSTOM_VALUES_ID_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_TJOB_SCOPE]
ON [dbo].[ACT_RU_TIMER_JOB] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_TJOB_SUB_SCOPE]
ON [dbo].[ACT_RU_TIMER_JOB] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_TJOB_SCOPE_DEF]
ON [dbo].[ACT_RU_TIMER_JOB] (
  [SCOPE_DEFINITION_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_TIMER_JOB
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_TIMER_JOB] ADD CONSTRAINT [PK__ACT_RU_T__C4971C0F61316BF4] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table ACT_RU_VARIABLE
-- ----------------------------
CREATE NONCLUSTERED INDEX [ACT_IDX_RU_VAR_SCOPE_ID_TYPE]
ON [dbo].[ACT_RU_VARIABLE] (
  [SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_RU_VAR_SUB_ID_TYPE]
ON [dbo].[ACT_RU_VARIABLE] (
  [SUB_SCOPE_ID_] ASC,
  [SCOPE_TYPE_] ASC
)
GO

CREATE NONCLUSTERED INDEX [ACT_IDX_VARIABLE_TASK_ID]
ON [dbo].[ACT_RU_VARIABLE] (
  [TASK_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_VARIABLE
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_VARIABLE] ADD CONSTRAINT [PK__ACT_RU_V__C4971C0F5E54FF49] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Foreign Keys structure for table ACT_ID_PRIV_MAPPING
-- ----------------------------
ALTER TABLE [dbo].[ACT_ID_PRIV_MAPPING] ADD CONSTRAINT [ACT_FK_PRIV_MAPPING] FOREIGN KEY ([PRIV_ID_]) REFERENCES [dbo].[ACT_ID_PRIV] ([ID_]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

