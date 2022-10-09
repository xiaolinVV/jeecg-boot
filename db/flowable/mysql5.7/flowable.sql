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
                                    `process_definition_key` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程定义key 一个key会有多个版本的id',
                                    `process_definition_id` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程定义id 一个流程定义唯一',
                                    `process_instance_id` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程业务实例id 一个流程业务唯一，本表中也唯一',
                                    `title` varchar(500) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程业务简要描述',
                                    `data_id` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务表id，理论唯一',
                                    `service_impl_name` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务类名，用来获取spring容器里的服务对象',
                                    `proposer` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '申请人',
                                    `bpm_status` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程状态说明，有：启动  撤回  驳回  审批中  审批通过  审批异常',
                                    `task_id` varchar(1000) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前的节点定义上的Id,',
                                    `task_name` varchar(1000) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前的节点',
                                    `task_name_id` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前的节点实例上的Id',
                                    `todo_users` varchar(1000) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前的节点可以处理的用户名',
                                    `done_users` varchar(1000) COLLATE utf8_general_ci DEFAULT NULL COMMENT '处理过的人',
                                    `priority` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前任务节点的优先级 流程定义的时候所填',
                                    `jimu_report_id` varchar(64) COLLATE utf8_general_ci DEFAULT NULL COMMENT '积木报表ID, 可查看当前审批单挂载的单据报表页面',
                                    `pc_form_url` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT 'PC表单组件地址',
                                    `proposer_dept_id` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '申请人部门ID',
                                    `proposer_dept_name` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '申请人部门名称',
                                    `proposer_name` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '申请人名称',
                                    PRIMARY KEY (`id`) /*T![clustered_index] NONCLUSTERED */,
                                    UNIQUE KEY `dataid` (`data_id`) COMMENT '业务数据Id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='流程业务扩展表';

-- 流程状态
INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1533795346018693121', '流程状态说明', 'act_status', '流程状态说明', 0, 'admin', '2022-06-06 20:58:02', NULL, NULL, 0);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795486402048002', '1533795346018693121', '待启动', '0', '', 1, 1, 'admin', '2022-06-06 20:58:35', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795641847148545', '1533795346018693121', '启动', '1', '', 2, 1, 'admin', '2022-06-06 20:59:12', 'admin', '2022-06-06 20:59:26');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795759518347266', '1533795346018693121', '撤回', '2', '', 3, 1, 'admin', '2022-06-06 20:59:40', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795810802102274', '1533795346018693121', '驳回', '3', '', 4, 1, 'admin', '2022-06-06 20:59:53', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795871774699522', '1533795346018693121', '审批中', '4', '', 5, 1, 'admin', '2022-06-06 21:00:07', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795927621857281', '1533795346018693121', '审批通过', '5', '', 6, 1, 'admin', '2022-06-06 21:00:20', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795981191507969', '1533795346018693121', '审批异常', '6', '', 7, 1, 'admin', '2022-06-06 21:00:33', NULL, NULL);

-- 字典
INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1569944741751529474', '流程是否退回', 'actIsReturn', '', 0, 'admin', '2022-09-14 15:02:49', NULL, NULL, 0);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1569944780246851585', '1569944741751529474', '审批通过', '0', '默认审批通过，不退回', 1, 1, 'admin', '2022-09-14 15:02:58', 'admin', '2022-09-14 15:03:14');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1569944924614795266', '1569944741751529474', '审批退回', '1', '', 2, 1, 'admin', '2022-09-14 15:03:33', NULL, NULL);

INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1569946201646145538', '流程配置-表单类型', 'ext_flow_form_type', '', 0, 'admin', '2022-09-14 15:08:37', NULL, NULL, 0);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1569946295590166529', '1569946201646145538', '自定义开发', '3', '', 3, 1, 'admin', '2022-09-14 15:09:00', NULL, NULL);


-- 流程菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1534453758712836097', '1534453116317429762', '我发起流程', '/flowable/myProcess/MyProcessList', 'flowable/myProcess/MyProcessList', NULL, NULL, 1, NULL, '1', 3, 0, NULL, 1, 1, 0, 0, 0, NULL, 'admin', '2022-06-08 16:34:20', 'admin', '2022-06-08 17:13:39', 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1529774634329157633', '1529772027745382402', '流程样例', '/flowable/test_demo/TestDemoList', 'flowable/test_demo/TestDemoList', NULL, NULL, 1, NULL, '1', 2, 0, NULL, 1, 1, 0, 0, 0, NULL, 'admin', '2022-05-26 18:41:09', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1534463621773864962', '1534453116317429762', '已办任务', '/flowable/finishedList/MyFinishedList', 'flowable/finishedList/MyFinishedList', NULL, NULL, 1, NULL, '1', 2, 0, NULL, 1, 1, 0, 0, 0, NULL, 'admin', '2022-06-08 17:13:31', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1529772403697627137', '1529772027745382402', '流程设计', '/flowable/modeler/modelerDesign', 'flowable/modeler/modelerDesign', NULL, NULL, 1, NULL, '1', 1, 0, NULL, 1, 1, 0, 0, 0, NULL, 'admin', '2022-05-26 18:32:17', 'admin', '2022-06-05 18:29:45', 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1534462289604214786', '1534453116317429762', '待办任务', '/flowable/todoList/MyTodoList', 'flowable/todoList/MyTodoList', NULL, NULL, 1, NULL, '1', 1, 0, NULL, 1, 1, 0, 0, 0, NULL, 'admin', '2022-06-08 17:08:13', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1534473146522349569', '1534472925054709762', '请假申请', '/demo/tbLeaveApplyList/TbLeaveApplyList', 'demo/tbLeaveApplyList/TbLeaveApplyList', NULL, NULL, 1, NULL, '1', 1, 0, NULL, 1, 1, 0, 0, 0, NULL, 'admin', '2022-06-08 17:51:22', 'admin', '2022-06-08 17:54:54', 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1529772027745382402', '', '流程设计', '/flowable/design', 'layouts/RouteView', NULL, NULL, 0, NULL, '1', 6.1, 0, 'cluster', 1, 0, 0, 0, 0, NULL, 'admin', '2022-05-26 18:30:48', 'admin', '2022-06-05 18:29:36', 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1534453116317429762', '', '个人办公', '/flowable', 'layouts/RouteView', NULL, NULL, 0, NULL, '1', 0.1, 0, 'share-alt', 1, 0, 0, 0, 0, NULL, 'admin', '2022-06-08 16:31:46', 'admin', '2022-06-08 16:32:07', 0, 0, '1', 0);

-- 流程菜单默认授权给 admin
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1534453853265031170', 'f6817f48af4fb3af11b9e8bf182f618b', '1534453758712836097', NULL, '2022-06-08 16:34:42', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1534462598825082881', 'f6817f48af4fb3af11b9e8bf182f618b', '1534462289604214786', NULL, '2022-06-08 17:09:27', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1529772484307955713', 'f6817f48af4fb3af11b9e8bf182f618b', '1529772403697627137', NULL, '2022-05-26 18:32:37', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1529774723948851201', 'f6817f48af4fb3af11b9e8bf182f618b', '1529774634329157633', NULL, '2022-05-26 18:41:31', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1534463722533629953', 'f6817f48af4fb3af11b9e8bf182f618b', '1534463621773864962', NULL, '2022-06-08 17:13:55', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1534453853252448258', 'f6817f48af4fb3af11b9e8bf182f618b', '1534453116317429762', NULL, '2022-06-08 16:34:42', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1529772484274401282', 'f6817f48af4fb3af11b9e8bf182f618b', '1529772027745382402', NULL, '2022-05-26 18:32:37', '0:0:0:0:0:0:0:1');

DROP TABLE IF EXISTS `flow_my_business_config`;
CREATE TABLE `flow_my_business_config` (
                                           `id` varchar(36) NOT NULL,
                                           `form_type` varchar(32) DEFAULT '3' COMMENT '表单类型',
                                           `table_name` varchar(64) DEFAULT NULL COMMENT '表名/自定义表单 CODE',
                                           `code` varchar(64) DEFAULT NULL COMMENT '唯一编码',
                                           `bpm_status_column_name` varchar(32) DEFAULT NULL COMMENT '流程状态列名',
                                           `title_expression` varchar(64) DEFAULT NULL COMMENT '标题表达式',
                                           `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                                           `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新日期',
                                           `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
                                           `process_definition_key` varchar(32) NOT NULL COMMENT '流程定义key 一个key会有多个版本的id',
                                           `jimu_report_id` varchar(64) DEFAULT NULL COMMENT '积木报表',
                                           `pc_form_url` varchar(64) DEFAULT NULL COMMENT 'PC表单组件地址',
                                           PRIMARY KEY (`id`) /*T![clustered_index] NONCLUSTERED */,
                                           UNIQUE KEY `unique_table_name` (`table_name`),
                                           UNIQUE KEY `unique_code` (`code`),
                                           KEY `index_processDefinitionKey` (`process_definition_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of flow_my_business
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;