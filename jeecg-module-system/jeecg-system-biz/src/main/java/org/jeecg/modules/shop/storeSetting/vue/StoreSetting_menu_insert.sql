-- 注意：该页面对应的前台目录为views/storeSetting文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值


INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) 
VALUES ('2022092911263970390', NULL, '店铺设置', '/storeSetting/storeSettingList', 'storeSetting/StoreSettingList', NULL, NULL, 0, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2022-09-29 11:26:39', NULL, NULL, 0);

-- 权限控制sql
-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022092911263970391', '2022092911263970390', '添加店铺设置', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.shop:store_setting:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-09-29 11:26:39', NULL, NULL, 0, 0, '1', 0);
-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022092911263970392', '2022092911263970390', '编辑店铺设置', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.shop:store_setting:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-09-29 11:26:39', NULL, NULL, 0, 0, '1', 0);
-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022092911263970393', '2022092911263970390', '删除店铺设置', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.shop:store_setting:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-09-29 11:26:39', NULL, NULL, 0, 0, '1', 0);
-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022092911263970394', '2022092911263970390', '批量删除店铺设置', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.shop:store_setting:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-09-29 11:26:39', NULL, NULL, 0, 0, '1', 0);
-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022092911263970395', '2022092911263970390', '导出excel_店铺设置', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.shop:store_setting:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-09-29 11:26:39', NULL, NULL, 0, 0, '1', 0);
-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022092911263970396', '2022092911263970390', '导入excel_店铺设置', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.shop:store_setting:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-09-29 11:26:39', NULL, NULL, 0, 0, '1', 0);