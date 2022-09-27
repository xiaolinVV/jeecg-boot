DROP TABLE IF EXIST `form_design`;
CREATE TABLE `form_design` (
                               `id` varchar(32) COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
                               `code` varchar(50) COLLATE utf8_general_ci NOT NULL COMMENT '表单编码',
                               `name` varchar(50) COLLATE utf8_general_ci NOT NULL COMMENT '表单名称',
                               `type` int(2) NOT NULL DEFAULT '0' COMMENT '表单类型。0：简单表单；1：复杂表单；',
                               `theme` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '表单主题。不配置默认为表单名称',
                               `design_data` text COLLATE utf8_general_ci DEFAULT NULL COMMENT '表单设计数据。',
                               `js_code` text COLLATE utf8_general_ci DEFAULT NULL COMMENT '表单js代码。仅当复杂表单才有',
                               `del_flag` int(1) DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
                               `create_by` varchar(32) COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_by` varchar(32) COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`) /*T![clustered_index] NONCLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='表单设计表';

INSERT INTO `jeecg-boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1561274666920157186', '1561274491820548098', '表单设计器', '/formDesign/FormDesignList', 'formDesign/FormDesignList', NULL, NULL, 1, NULL, '1', NULL, 0, NULL, 1, 1, 0, 0, 0, NULL, 'admin', '2022-08-21 16:51:02', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `jeecg-boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1561274491820548098', NULL, '表单设计', '/formDesign', 'layouts/RouteView', NULL, NULL, 0, NULL, '1', 6.2, 0, 'gold', 1, 0, 0, 0, 0, NULL, 'admin', '2022-08-21 16:50:21', NULL, NULL, 0, 0, '1', 0);

INSERT INTO `jeecg-boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1561275229086916609', 'f6817f48af4fb3af11b9e8bf182f618b', '1561274491820548098', NULL, '2022-08-21 16:53:16', '0:0:0:0:0:0:0:1');
INSERT INTO `jeecg-boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1561275229107888129', 'f6817f48af4fb3af11b9e8bf182f618b', '1561274666920157186', NULL, '2022-08-21 16:53:16', '0:0:0:0:0:0:0:1');