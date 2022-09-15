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

 Date: 15/09/2022 17:10:32
*/


-- ----------------------------
-- Table structure for FLW_CHANNEL_DEFINITION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[FLW_CHANNEL_DEFINITION]') AND type IN ('U'))
	DROP TABLE [dbo].[FLW_CHANNEL_DEFINITION]
GO

CREATE TABLE [dbo].[FLW_CHANNEL_DEFINITION] (
  [ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION_] int  NULL,
  [KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CATEGORY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOYMENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[FLW_CHANNEL_DEFINITION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of FLW_CHANNEL_DEFINITION
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for FLW_EV_DATABASECHANGELOG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[FLW_EV_DATABASECHANGELOG]') AND type IN ('U'))
	DROP TABLE [dbo].[FLW_EV_DATABASECHANGELOG]
GO

CREATE TABLE [dbo].[FLW_EV_DATABASECHANGELOG] (
  [ID] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [AUTHOR] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [FILENAME] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [DATEEXECUTED] datetime  NOT NULL,
  [ORDEREXECUTED] int  NOT NULL,
  [EXECTYPE] nvarchar(10) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [MD5SUM] nvarchar(35) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [COMMENTS] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TAG] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LIQUIBASE] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [CONTEXTS] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LABELS] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOYMENT_ID] nvarchar(10) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[FLW_EV_DATABASECHANGELOG] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of FLW_EV_DATABASECHANGELOG
-- ----------------------------
BEGIN TRANSACTION
GO

INSERT INTO [dbo].[FLW_EV_DATABASECHANGELOG] VALUES (N'1', N'flowable', N'org/flowable/eventregistry/db/liquibase/flowable-eventregistry-db-changelog.xml', N'2022-05-26 15:51:44.000', N'1', N'EXECUTED', N'8:1b0c48c9cf7945be799d868a2626d687', N'createTable tableName=FLW_EVENT_DEPLOYMENT; createTable tableName=FLW_EVENT_RESOURCE; createTable tableName=FLW_EVENT_DEFINITION; createIndex indexName=ACT_IDX_EVENT_DEF_UNIQ, tableName=FLW_EVENT_DEFINITION; createTable tableName=FLW_CHANNEL_DEFIN...', N'', NULL, N'3.8.9', NULL, NULL, N'3551494265')
GO

COMMIT
GO


-- ----------------------------
-- Table structure for FLW_EV_DATABASECHANGELOGLOCK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[FLW_EV_DATABASECHANGELOGLOCK]') AND type IN ('U'))
	DROP TABLE [dbo].[FLW_EV_DATABASECHANGELOGLOCK]
GO

CREATE TABLE [dbo].[FLW_EV_DATABASECHANGELOGLOCK] (
  [ID] int  NOT NULL,
  [LOCKED] varchar(1) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [LOCKGRANTED] datetime2(7)  NULL,
  [LOCKEDBY] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[FLW_EV_DATABASECHANGELOGLOCK] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of FLW_EV_DATABASECHANGELOGLOCK
-- ----------------------------
BEGIN TRANSACTION
GO

INSERT INTO [dbo].[FLW_EV_DATABASECHANGELOGLOCK] VALUES (N'1', N'0', NULL, NULL)
GO

COMMIT
GO


-- ----------------------------
-- Table structure for FLW_EVENT_DEFINITION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[FLW_EVENT_DEFINITION]') AND type IN ('U'))
	DROP TABLE [dbo].[FLW_EVENT_DEFINITION]
GO

CREATE TABLE [dbo].[FLW_EVENT_DEFINITION] (
  [ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION_] int  NULL,
  [KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CATEGORY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOYMENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[FLW_EVENT_DEFINITION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of FLW_EVENT_DEFINITION
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for FLW_EVENT_DEPLOYMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[FLW_EVENT_DEPLOYMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[FLW_EVENT_DEPLOYMENT]
GO

CREATE TABLE [dbo].[FLW_EVENT_DEPLOYMENT] (
  [ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CATEGORY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOY_TIME_] datetime2(7)  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_DEPLOYMENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[FLW_EVENT_DEPLOYMENT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of FLW_EVENT_DEPLOYMENT
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for FLW_EVENT_RESOURCE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[FLW_EVENT_RESOURCE]') AND type IN ('U'))
	DROP TABLE [dbo].[FLW_EVENT_RESOURCE]
GO

CREATE TABLE [dbo].[FLW_EVENT_RESOURCE] (
  [ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOYMENT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_BYTES_] varbinary(max)  NULL
)
GO

ALTER TABLE [dbo].[FLW_EVENT_RESOURCE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of FLW_EVENT_RESOURCE
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for FLW_RU_BATCH
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[FLW_RU_BATCH]') AND type IN ('U'))
	DROP TABLE [dbo].[FLW_RU_BATCH]
GO

CREATE TABLE [dbo].[FLW_RU_BATCH] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [TYPE_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [SEARCH_KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEARCH_KEY2_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime2(7)  NOT NULL,
  [COMPLETE_TIME_] datetime2(7)  NULL,
  [STATUS_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [BATCH_DOC_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[FLW_RU_BATCH] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of FLW_RU_BATCH
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Table structure for FLW_RU_BATCH_PART
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[FLW_RU_BATCH_PART]') AND type IN ('U'))
	DROP TABLE [dbo].[FLW_RU_BATCH_PART]
GO

CREATE TABLE [dbo].[FLW_RU_BATCH_PART] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] int  NULL,
  [BATCH_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [SCOPE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_SCOPE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_TYPE_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEARCH_KEY_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEARCH_KEY2_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime2(7)  NOT NULL,
  [COMPLETE_TIME_] datetime2(7)  NULL,
  [STATUS_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESULT_DOC_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[FLW_RU_BATCH_PART] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of FLW_RU_BATCH_PART
-- ----------------------------
BEGIN TRANSACTION
GO

COMMIT
GO


-- ----------------------------
-- Indexes structure for table FLW_CHANNEL_DEFINITION
-- ----------------------------
CREATE UNIQUE NONCLUSTERED INDEX [ACT_IDX_CHANNEL_DEF_UNIQ]
ON [dbo].[FLW_CHANNEL_DEFINITION] (
  [KEY_] ASC,
  [VERSION_] ASC,
  [TENANT_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table FLW_CHANNEL_DEFINITION
-- ----------------------------
ALTER TABLE [dbo].[FLW_CHANNEL_DEFINITION] ADD CONSTRAINT [PK__FLW_CHAN__C4971C0F69C6B1F5] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table FLW_EV_DATABASECHANGELOGLOCK
-- ----------------------------
ALTER TABLE [dbo].[FLW_EV_DATABASECHANGELOGLOCK] ADD CONSTRAINT [PK__FLW_EV_D__3214EC276F7F8B4B] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table FLW_EVENT_DEFINITION
-- ----------------------------
CREATE UNIQUE NONCLUSTERED INDEX [ACT_IDX_EVENT_DEF_UNIQ]
ON [dbo].[FLW_EVENT_DEFINITION] (
  [KEY_] ASC,
  [VERSION_] ASC,
  [TENANT_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table FLW_EVENT_DEFINITION
-- ----------------------------
ALTER TABLE [dbo].[FLW_EVENT_DEFINITION] ADD CONSTRAINT [PK__FLW_EVEN__C4971C0F640DD89F] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table FLW_EVENT_DEPLOYMENT
-- ----------------------------
ALTER TABLE [dbo].[FLW_EVENT_DEPLOYMENT] ADD CONSTRAINT [PK__FLW_EVEN__C4971C0F66EA454A] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table FLW_EVENT_RESOURCE
-- ----------------------------
ALTER TABLE [dbo].[FLW_EVENT_RESOURCE] ADD CONSTRAINT [PK__FLW_EVEN__C4971C0F6CA31EA0] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table FLW_RU_BATCH
-- ----------------------------
ALTER TABLE [dbo].[FLW_RU_BATCH] ADD CONSTRAINT [PK__FLW_RU_B__C4971C0F725BF7F6] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Indexes structure for table FLW_RU_BATCH_PART
-- ----------------------------
CREATE NONCLUSTERED INDEX [FLW_IDX_BATCH_PART]
ON [dbo].[FLW_RU_BATCH_PART] (
  [BATCH_ID_] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table FLW_RU_BATCH_PART
-- ----------------------------
ALTER TABLE [dbo].[FLW_RU_BATCH_PART] ADD CONSTRAINT [PK__FLW_RU_B__C4971C0F7814D14C] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO

