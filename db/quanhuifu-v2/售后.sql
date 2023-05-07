create table order_refund_list
(
    id                                         varchar(36)                 not null comment '主键ID'
        primary key,
    create_by                                  varchar(36)    default ''   null comment '创建人',
    create_time                                datetime                    null comment '创建时间',
    update_by                                  varchar(36)    default ''   null comment '修改人',
    update_time                                datetime                    null comment '修改时间',
    year                                       int            default -1   null comment '创建年',
    month                                      int            default -1   null comment '创建月',
    day                                        int            default -1   null comment '创建日',
    del_flag                                   varchar(1)     default '0'  null comment '删除状态（0，正常，1已删除）',
    order_sub_list_id                          varchar(36)    default ''   null comment '子订单id（供应商/包裹）',
    order_list_id                              varchar(50)    default ''   null comment '订单id（店铺订单/平台订单）',
    order_no                                   varchar(50)    default ''   null comment '订单号（店铺订单/平台订单）',
    order_type                                 varchar(11)    default ''   null comment '订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；',
    order_good_record_id                       varchar(50)    default ''   null comment '订单商品记录id',
    member_id                                  varchar(50)    default ''   null comment '会员id',
    sys_user_id                                varchar(50)    default ''   null comment '店铺用户id/平台供应商用户id',
    refund_type                                varchar(1)     default ''   null comment '退款类型 0=仅退款 1=退货退款 2=换货 关联字典：refund_type',
    refund_reason                              varchar(1)     default ''   null comment '退款原因,关联字典：order_store_refund_reason',
    refund_explain                             varchar(500)   default ''   null comment '后台商家拒绝退款理由',
    remarks                                    varchar(500)   default ''   null comment '售后申请说明',
    status                                     varchar(1)     default '0'  null comment '售后状态 0=待处理 1=待买家退回 2=换货中（等待店铺确认收货） 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成。 关联字典：order_refund_status',
    good_main_picture                          varchar(1000)  default ''   null comment '订单商品主图相对地址（以json的形式存储多张）',
    good_list_id                               varchar(50)    default ''   null comment '订单商品id（只做对象映射）',
    good_specification_id                      varchar(50)    default ''   null comment '订单商品规格id（只做对象映射）',
    good_name                                  varchar(100)   default ''   null comment '商品名称',
    good_specification                         varchar(500)   default ''   null comment '规格名称，按照顺序逗号隔开',
    refund_certificate                         varchar(1000)  default ''   null comment '申请退款凭证图片，按照顺序逗号隔开',
    refund_price                               decimal(20, 2) default 0.00 null comment '申请退款金额',
    refund_amount                              decimal(20, 2) default 0.00 null comment '申请退款数量',
    is_platform                                varchar(1)     default ''   null comment '0=店铺  1=平台',
    good_record_total                          decimal(20, 2) default 0.00 null comment '订单商品总计金额（小计）',
    good_record_actual_payment                 decimal(20, 2) default 0.00 null comment '订单商品实付款',
    good_record_coupon                         decimal(20, 2) default 0.00 null comment '订单商品优惠卷优惠金额',
    good_record_gift_card_coupon               decimal(20, 2) default 0.00 null comment '订单商品礼品卡优惠金额',
    good_record_marketing_discount_coupon_id   varchar(500)   default ''   null comment '订单商品优惠券记录id,多个用逗号分隔',
    good_record_total_coupon                   decimal(20, 2) default 0.00 null comment '订单商品总优惠金额',
    good_record_amount                         decimal(20, 2) default 0.00 null comment '订单商品购买数量',
    good_unit_price                            decimal(9, 2)  default 0.00 null comment '订单商品销售单价',
    close_explain                              varchar(1)     default ''   null comment '售后单关闭原因，关联字典 refund_close_explain',
    refused_explain                            varchar(500)   default ''   null comment '商家拒绝原因',
    merchant_consignee_name                    varchar(50)    default ''   null comment '退货/换货商家邮寄信息：商家收件人姓名',
    merchant_consignee_address                 varchar(500)   default ''   null comment '退货/换货商家邮寄信息：商家收件地址',
    merchant_consignee_phone                   varchar(50)    default ''   null comment '退货/换货商家邮寄信息：商家收件手机号',
    merchant_consignee_province_id             varchar(50)    default ''   null comment '退货/换货商家邮寄信息：商家收件地址：省',
    merchant_consignee_city_id                 varchar(50)    default ''   null comment '退货/换货商家邮寄信息：商家收件地址：市',
    merchant_consignee_area_id                 varchar(50)    default ''   null comment '退货/换货商家邮寄信息：商家收件地址：区',
    buyer_logistics_company                    varchar(20)    default ''   null comment '买家寄回物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；',
    buyer_tracking_number                      varchar(100)   default ''   null comment '买家寄回快递单号',
    buyer_logistics_tracking                   json                        null comment '买家寄回物流跟踪信息的json保存（每次查询的时候更新）',
    exchange_good_specification_id             varchar(500)   default ''   null comment '买家换货商品规格id，逗号分隔',
    exchange_good_specification                varchar(500)   default ''   null comment '买家换货规格名称，按照顺序逗号隔开',
    exchange_member_shipping_address           json                        null comment '买家换货：买家收货地址json',
    merchant_logistics_company                 varchar(20)    default ''   null comment '换货时卖家寄回：商家物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；',
    merchant_tracking_number                   varchar(100)   default ''   null comment '换货时卖家寄回：商家快递单号',
    merchant_logistics_tracking                json                        null comment '换货时卖家寄回：商家物流跟踪信息的json保存（每次查询的时候更新）',
    refund_json                                json                        null comment '退款json返回日志',
    actual_refund_balance                      decimal(20, 2) default 0.00 null comment '实际退款余额',
    actual_refund_price                        decimal(20, 2) default 0.00 null comment '实际退款现金（汇付微信）',
    actual_refund_gift_card_balance            decimal(20, 2) default 0.00 null comment '实际退还礼品卡金额',
    actual_refund_discount_welfare_payments    decimal(20, 2) default 0.00 null comment '实际退还抵扣的福利金（专区商品）',
    welfare_payments_price                     decimal(20, 2) default 0.00 null comment '订单商品抵扣福利金价值（专区商品）',
    welfare_payments                           decimal(20, 2) default 0.00 null comment '订单商品抵扣福利金（专区商品）',
    actual_refund_marketing_discount_coupon_id varchar(500)   default ''   null comment '实际退款优惠券记录id'
)
    comment 'order_refund_list' charset = utf8mb4;

ALTER TABLE `shop-boot`.`order_store_good_record` MODIFY COLUMN `customary_dues` decimal(9, 2) NULL DEFAULT 0.00 COMMENT '应付款（支付前标准金额）' AFTER `total`;

ALTER TABLE `shop-boot`.`order_store_good_record` MODIFY COLUMN `actual_payment` decimal(9, 2) NULL DEFAULT 0.00 COMMENT '实付款（支付后标准金额）' AFTER `customary_dues`;

ALTER TABLE `shop-boot`.`order_store_good_record` ADD COLUMN `marketing_discount_coupon_id` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '优惠券记录id,多个用逗号分隔' AFTER `actual_payment`;

ALTER TABLE `shop-boot`.`order_store_good_record` ADD COLUMN `coupon` decimal(9, 2) NULL DEFAULT 0.00 COMMENT '优惠券优惠金额' AFTER `weight`;

ALTER TABLE `shop-boot`.`order_store_good_record` ADD COLUMN `gift_card_coupon` decimal(9, 2) NULL DEFAULT 0.00 COMMENT '礼品卡优惠金额' AFTER `coupon`;

ALTER TABLE `shop-boot`.`order_store_good_record` ADD COLUMN `total_coupon` decimal(9, 2) NULL DEFAULT 0.00 COMMENT '优惠总金额' AFTER `gift_card_coupon`;


# select *
# from sys_dict
# where dict_code in ('order_refund_status', 'refund_close_explain', 'refund_type', 'order_refund_reason');
#
# select * from sys_dict_item where dict_id in (select sys_dict.id
#                                               from sys_dict
#                                               where dict_code in ('order_refund_status', 'refund_close_explain', 'refund_type', 'order_refund_reason'));

INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type) VALUES ('1645366805847371778', '订单退款原因', 'order_refund_reason', '店铺订单退款原因', 0, 'admin', '2023-04-10 18:03:11', 'jeecg', '2023-04-28 10:23:14', 0000000000);
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type) VALUES ('1650687055926046721', '售后单状态', 'order_refund_status', '售后单状态', 0, 'jeecg', '2023-04-25 10:23:57', null, null, 0000000000);
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type) VALUES ('1650686209347727361', '售后单关闭原因', 'refund_close_explain', '售后单关闭原因', 0, 'jeecg', '2023-04-25 10:20:35', 'jeecg', '2023-04-25 10:20:45', 0000000000);
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type) VALUES ('1645385512262856706', '订单售后方式', 'refund_type', '订单售后方式', 0, 'admin', '2023-04-10 19:17:31', null, null, 0000000000);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('d2679f20d78711ed9324000c29eb6d2d', '1645366805847371778', '商品无货', '6', '买家关闭原因', 1, 1, 'admin', '2019-11-16 16:12:41', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('d27ebcd4d78711ed9324000c29eb6d2d', '1645366805847371778', '订单不能按预计时间送达', '1', '买家关闭原因', 1, 1, 'admin', '2019-11-16 16:09:22', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('d28b6211d78711ed9324000c29eb6d2d', '1645366805847371778', '重复下单/误下单', '3', '买家关闭原因', 1, 1, 'admin', '2019-11-16 16:11:42', 'admin', '2020-01-07 23:23:46');
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('d29b884fd78711ed9324000c29eb6d2d', '1645366805847371778', '其他渠道价格更低', '4', '买家关闭原因', 1, 1, 'admin', '2019-11-16 16:12:05', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('d2b0ab38d78711ed9324000c29eb6d2d', '1645366805847371778', '不想买了', '5', '买家关闭原因', 1, 1, 'admin', '2019-11-16 16:12:25', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('d2c302e4d78711ed9324000c29eb6d2d', '1645366805847371778', '操作有误（商品、地址等选错）', '2', '买家关闭原因', 1, 1, 'admin', '2019-11-16 16:09:41', 'admin', '2019-11-16 16:11:29');
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('d2d007d9d78711ed9324000c29eb6d2d', '1645366805847371778', '其他原因', '7', '买家关闭原因', 1, 1, 'admin', '2019-11-16 16:12:56', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650687151774281730', '1650687055926046721', '待处理', '0', '待处理', 1, 1, 'jeecg', '2023-04-25 10:24:20', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650687212621049858', '1650687055926046721', '待买家退回', '1', '待买家退回', 2, 1, 'jeecg', '2023-04-25 10:24:34', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650687359413301249', '1650687055926046721', '换货中（等待店铺确认收货）', '2', '换货中（等待店铺确认收货）', 3, 1, 'jeecg', '2023-04-25 10:25:09', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650687412248948737', '1650687055926046721', '退款中', '3', '退款中', 4, 1, 'jeecg', '2023-04-25 10:25:22', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650687474790215681', '1650687055926046721', '退款成功', '4', '退款成功', 5, 1, 'jeecg', '2023-04-25 10:25:37', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650687533405614082', '1650687055926046721', '已拒绝', '5', '已拒绝', 6, 1, 'jeecg', '2023-04-25 10:25:51', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650687591232483330', '1650687055926046721', '退款关闭', '6', '退款关闭', 7, 1, 'jeecg', '2023-04-25 10:26:05', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650687645410308098', '1650687055926046721', '换货关闭', '7', '换货关闭', 8, 1, 'jeecg', '2023-04-25 10:26:18', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650687703157485570', '1650687055926046721', '换货完成', '8', '换货完成', 9, 1, 'jeecg', '2023-04-25 10:26:31', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1650686319104274433', '1650686209347727361', '买家撤销', '0', '买家撤销', 1, 1, 'jeecg', '2023-04-25 10:21:01', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1655067154414809090', '1650686209347727361', '退换货超时自动关闭', '1', '退换货超时自动关闭', 2, 1, 'jeecg', '2023-05-07 12:28:54', 'jeecg', '2023-05-07 12:29:08');
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1645385678025945089', '1645385512262856706', '仅退款', '0', '', 1, 1, 'admin', '2023-04-10 19:18:10', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1645385739380224001', '1645385512262856706', '退货退款', '1', '', 2, 1, 'admin', '2023-04-10 19:18:25', null, null);
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1645385820267376641', '1645385512262856706', '换货', '2', '', 3, 1, 'admin', '2023-04-10 19:18:44', null, null);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1651521417630441474', '74547a7681b0592591bd8234560b66c2', 'common_refund_return_timeout', '167', '售后单退货超时时间7天（167小时）', 1, 1, 'jeecg', '2023-04-27 17:39:24', null, null);

