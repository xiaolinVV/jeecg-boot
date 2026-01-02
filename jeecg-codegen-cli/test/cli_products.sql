CREATE TABLE `cli_products` (
  `id` varchar(32) NOT NULL COMMENT '主键ID',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态 0=未删除 1=已删除',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT (now()) not null COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',

  `product_code` varchar(64) NOT NULL COMMENT '产品编码',
  `product_name` varchar(120) NOT NULL COMMENT '产品名称',
  `product_status` varchar(1) DEFAULT NULL COMMENT '产品状态：0=停用；1=启用。字典：cli_product_status',
  `product_type` varchar(2) DEFAULT NULL COMMENT '产品类型：01=标准；02=定制。字典：cli_product_type',
  `owner_user_id` varchar(32) DEFAULT NULL COMMENT '负责人用户ID(关联 sys_user 表)',
  `dept_id` varchar(32) DEFAULT NULL COMMENT '所属部门ID(关联 sys_depart 表)',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '单价',
  `sale_date` date DEFAULT NULL COMMENT '上架日期',
  `is_hot` tinyint(1) DEFAULT 0 COMMENT '是否热卖 0=否 1=是',
  `manual_file` varchar(255) DEFAULT NULL COMMENT '说明书文件',
  `preview_image` varchar(255) DEFAULT NULL COMMENT '预览图片',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sort_order` int DEFAULT 0 COMMENT '排序',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cli_products_code` (`product_code`),
  KEY `idx_cli_products_status` (`product_status`),
  KEY `idx_cli_products_type` (`product_type`),
  KEY `idx_cli_products_owner_user_id` (`owner_user_id`),
  KEY `idx_cli_products_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CLI产品测试表';
