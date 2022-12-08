-- 注意：该页面对应的前台目录为views/store.storePayment文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值


INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) 
VALUES ('2022120811055350190', NULL, '店铺消费记录', '/store.storePayment/storePaymentList', 'store.storePayment/StorePaymentList', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2022-12-08 11:05:19', NULL, NULL, 0);

-- 权限控制sql
-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022120811055350191', '2022120811055350190', '添加店铺消费记录', NULL, NULL, 0, NULL, NULL, 2, 'store.storePayment:store_payment:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-08 11:05:19', NULL, NULL, 0, 0, '1', 0);
-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022120811055350192', '2022120811055350190', '编辑店铺消费记录', NULL, NULL, 0, NULL, NULL, 2, 'store.storePayment:store_payment:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-08 11:05:19', NULL, NULL, 0, 0, '1', 0);
-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022120811055350193', '2022120811055350190', '删除店铺消费记录', NULL, NULL, 0, NULL, NULL, 2, 'store.storePayment:store_payment:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-08 11:05:19', NULL, NULL, 0, 0, '1', 0);
-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022120811055350194', '2022120811055350190', '批量删除店铺消费记录', NULL, NULL, 0, NULL, NULL, 2, 'store.storePayment:store_payment:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-08 11:05:19', NULL, NULL, 0, 0, '1', 0);
-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022120811055350195', '2022120811055350190', '导出excel_店铺消费记录', NULL, NULL, 0, NULL, NULL, 2, 'store.storePayment:store_payment:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-08 11:05:19', NULL, NULL, 0, 0, '1', 0);
-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022120811055350196', '2022120811055350190', '导入excel_店铺消费记录', NULL, NULL, 0, NULL, NULL, 2, 'store.storePayment:store_payment:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-08 11:05:19', NULL, NULL, 0, 0, '1', 0);