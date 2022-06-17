/*
 Navicat Premium Data Transfer

 Source Server         : TiDB_Blockchain
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 220.250.40.44:14000
 Source Schema         : flowable

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 26/05/2022 16:03:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flow_my_business
-- ----------------------------
DROP TABLE IF EXISTS `flow_my_business`;
CREATE TABLE `flow_my_business` (
  `id` varchar(50) COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `process_definition_key` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程定义key 一个key会有多个版本的id',
  `process_definition_id` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程定义id 一个流程定义唯一',
  `process_instance_id` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程业务实例id 一个流程业务唯一，本表中也唯一',
  `title` varchar(500) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程业务简要描述',
  `data_id` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务表id，理论唯一',
  `service_impl_name` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务类名，用来获取spring容器里的服务对象',
  `proposer` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '申请人',
  `act_status` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程状态说明，有：启动  撤回  驳回  审批中  审批通过  审批异常',
  `task_id` varchar(1000) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前的节点定义上的Id,',
  `task_name` varchar(1000) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前的节点',
  `task_name_id` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前的节点实例上的Id',
  `todo_users` varchar(1000) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前的节点可以处理的用户名',
  `done_users` varchar(1000) COLLATE utf8_general_ci DEFAULT NULL COMMENT '处理过的人',
  `priority` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前任务节点的优先级 流程定义的时候所填',
  PRIMARY KEY (`id`) /*T![clustered_index] NONCLUSTERED */,
  UNIQUE KEY `dataid` (`data_id`) COMMENT '业务数据Id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='流程业务扩展表';

-- ----------------------------
-- Records of flow_my_business
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
