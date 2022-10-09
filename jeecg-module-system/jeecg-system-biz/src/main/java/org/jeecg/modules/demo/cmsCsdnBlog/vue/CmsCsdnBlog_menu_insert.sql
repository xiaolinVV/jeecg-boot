-- 注意：该页面对应的前台目录为views/cmsCsdnBlog文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值


INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) 
VALUES ('2022100902468840590', NULL, 'csdn博客表', '/cmsCsdnBlog/cmsCsdnBlogList', 'cmsCsdnBlog/CmsCsdnBlogList', NULL, NULL, 0, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2022-10-09 14:46:59', NULL, NULL, 0);

-- 权限控制sql
-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022100902468850591', '2022100902468840590', '添加csdn博客表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.demo:cms_csdn_blog:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-10-09 14:46:59', NULL, NULL, 0, 0, '1', 0);
-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022100902468850592', '2022100902468840590', '编辑csdn博客表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.demo:cms_csdn_blog:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-10-09 14:46:59', NULL, NULL, 0, 0, '1', 0);
-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022100902468850593', '2022100902468840590', '删除csdn博客表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.demo:cms_csdn_blog:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-10-09 14:46:59', NULL, NULL, 0, 0, '1', 0);
-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022100902468850594', '2022100902468840590', '批量删除csdn博客表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.demo:cms_csdn_blog:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-10-09 14:46:59', NULL, NULL, 0, 0, '1', 0);
-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022100902468850595', '2022100902468840590', '导出excel_csdn博客表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.demo:cms_csdn_blog:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-10-09 14:46:59', NULL, NULL, 0, 0, '1', 0);
-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2022100902468850596', '2022100902468840590', '导入excel_csdn博客表', NULL, NULL, 0, NULL, NULL, 2, 'org.jeecg.modules.demo:cms_csdn_blog:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-10-09 14:46:59', NULL, NULL, 0, 0, '1', 0);