初始化项目时依次执行如下数据库初始化脚本：

1. db/jeecgboot-mysql-5.7.sql
2. db/权限表切换VUE2.sql
3. 如果需要初始化 flowable 流程引擎，执行 db/flowable/mysql5.7/flowable-mysql5.7.sql、db/flowable/mysql5.7/flowable.sql


//脚本中可能有缺漏如下SQL，待补充
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1546736974756032513', '1455101470794850305', '选择代码生成路径', NULL, NULL, 0, NULL, NULL, 2, 'online:codeGenerate:projectPath', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-07-12 14:03:26', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1699374269152100354', '1455101470794850305', '同步数据库', NULL, NULL, 0, NULL, NULL, 2, 'online:form:syncDb', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-09-06 18:49:33', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1699374509749960705', '1455101470794850305', '查询数据库表名', NULL, NULL, 0, NULL, NULL, 2, 'online:form:queryTables', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-09-06 18:50:31', NULL, NULL, 0, 0, '1', 0);