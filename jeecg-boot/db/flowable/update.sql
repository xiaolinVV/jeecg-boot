INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1533795346018693121', '流程状态说明', 'act_status', '流程状态说明', 0, 'admin', '2022-06-06 20:58:02', NULL, NULL, 0);

INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795486402048002', '1533795346018693121', '待启动', '0', '', 1, 1, 'admin', '2022-06-06 20:58:35', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795641847148545', '1533795346018693121', '启动', '1', '', 2, 1, 'admin', '2022-06-06 20:59:12', 'admin', '2022-06-06 20:59:26');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795759518347266', '1533795346018693121', '撤回', '2', '', 3, 1, 'admin', '2022-06-06 20:59:40', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795810802102274', '1533795346018693121', '驳回', '3', '', 4, 1, 'admin', '2022-06-06 20:59:53', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795871774699522', '1533795346018693121', '审批中', '4', '', 5, 1, 'admin', '2022-06-06 21:00:07', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795927621857281', '1533795346018693121', '审批通过', '5', '', 6, 1, 'admin', '2022-06-06 21:00:20', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1533795981191507969', '1533795346018693121', '审批异常', '6', '', 7, 1, 'admin', '2022-06-06 21:00:33', NULL, NULL);