-- 注意：该页面对应的前台目录为views/formDesign文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值


INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) 
VALUES ('2022082104467260480', NULL, '表单设计表', '/formDesign/formDesignList', 'formDesign/FormDesignList', NULL, NULL, 0, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2022-08-21 16:46:48', NULL, NULL, 0);

-- 权限控制sql
-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022082104467260481', '2022082104467260480', '添加表单设计表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.system:form_design:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-08-21 16:46:48', NULL, NULL, 0, 0, '1', 0);
-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022082104467260482', '2022082104467260480', '编辑表单设计表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.system:form_design:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-08-21 16:46:48', NULL, NULL, 0, 0, '1', 0);
-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022082104467260483', '2022082104467260480', '删除表单设计表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.system:form_design:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-08-21 16:46:48', NULL, NULL, 0, 0, '1', 0);
-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022082104467260484', '2022082104467260480', '批量删除表单设计表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.system:form_design:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-08-21 16:46:48', NULL, NULL, 0, 0, '1', 0);
-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022082104467260485', '2022082104467260480', '导出excel_表单设计表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.system:form_design:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-08-21 16:46:48', NULL, NULL, 0, 0, '1', 0);
-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022082104467260486', '2022082104467260480', '导入excel_表单设计表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.system:form_design:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-08-21 16:46:48', NULL, NULL, 0, 0, '1', 0);