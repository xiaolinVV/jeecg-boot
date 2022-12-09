/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.183
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : 220.250.40.44:13308
 Source Schema         : shop-boot

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 10/12/2022 00:19:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for member_account_capital
-- ----------------------------
DROP TABLE IF EXISTS `member_account_capital`;
CREATE TABLE `member_account_capital` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `pay_type` varchar(2) DEFAULT NULL COMMENT '交易类型；0：订单交易；1：余额提现；2：订单退款；3：分销奖励；4：推荐开店奖励；5：礼包推广奖励；6：福利金推广奖励；7：兑换券推广奖励；8：称号分红；9：采购礼包直推奖励；10：采购礼包间推奖励，11：提现退款；12：积分挂卖；13：拼券付款；14：拼券失败退款；15：抢券付款；16：兑换券购买；做成数据字典member_deal_type',
  `go_and_come` varchar(1) DEFAULT NULL COMMENT '支付和收入；0：收入；1：支出',
  `amount` decimal(20,2) DEFAULT NULL COMMENT '交易金额',
  `order_no` varchar(50) DEFAULT NULL COMMENT '单号',
  `balance` decimal(20,2) DEFAULT NULL COMMENT '账户余额',
  PRIMARY KEY (`id`,`create_time`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE,
  KEY `index_del_flag` (`del_flag`) USING BTREE,
  KEY `index_go_and_come` (`go_and_come`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_attention_store
-- ----------------------------
DROP TABLE IF EXISTS `member_attention_store`;
CREATE TABLE `member_attention_store` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `sys_user_id` varchar(50) DEFAULT NULL COMMENT '角色为店铺的用户id',
  `attention_time` datetime DEFAULT NULL COMMENT '关注时间',
  `activities_type` varchar(50) DEFAULT NULL COMMENT '参与活动;对应数据字典字段的活动类型数据，为空代表没有参与活动，数据字典：activities_type',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_bank_card
-- ----------------------------
DROP TABLE IF EXISTS `member_bank_card`;
CREATE TABLE `member_bank_card` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `bank_name` varchar(40) DEFAULT NULL COMMENT '开户行名称',
  `bank_card` varchar(40) DEFAULT NULL COMMENT '银行卡号（支付宝账号）',
  `cardholder` varchar(20) DEFAULT NULL COMMENT '持卡人（真实姓名）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `car_type` varchar(1) DEFAULT NULL COMMENT '卡类型；0：银行卡；1：支付宝',
  `update_explain` varchar(200) DEFAULT NULL COMMENT '变更说明',
  `update_certificate` varchar(200) DEFAULT NULL COMMENT '变更凭证',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系人手机号',
  `identity_number` varchar(50) DEFAULT NULL COMMENT '身份证号',
  `citys` varchar(50) DEFAULT NULL COMMENT '所在城市',
  `citys_code` varchar(50) DEFAULT NULL COMMENT '所在城市编码',
  `opening_bank` varchar(100) DEFAULT NULL COMMENT '开户分支行',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员银行卡';

-- ----------------------------
-- Table structure for member_browsing_history
-- ----------------------------
DROP TABLE IF EXISTS `member_browsing_history`;
CREATE TABLE `member_browsing_history` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `good_type` varchar(1) DEFAULT NULL COMMENT '商品的类型;0:店铺商品；1：平台商品',
  `good_list_id` varchar(50) DEFAULT NULL COMMENT '平台商品id，可以为空',
  `good_store_list_id` varchar(50) DEFAULT NULL COMMENT '店铺商品id，可以为空',
  `browsing_time` datetime DEFAULT NULL COMMENT '浏览时间',
  `browsing_price` decimal(9,2) DEFAULT NULL COMMENT '浏览时价格',
  `quantity` decimal(6,0) DEFAULT NULL COMMENT '浏览次数',
  `prefecture_label` varchar(10) DEFAULT NULL COMMENT '专区标签',
  `marketing_prefecture_id` varchar(50) DEFAULT NULL COMMENT '专区id',
  `marketing_free_good_list_id` varchar(50) DEFAULT NULL COMMENT '免单活动id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE,
  KEY `index_good_list_id` (`good_list_id`) USING BTREE,
  KEY `index_good_store_list_id` (`good_store_list_id`) USING BTREE,
  KEY `index_marketing_free_good_list_id` (`marketing_free_good_list_id`) USING BTREE,
  KEY `index_marketing_prefecture_id` (`marketing_prefecture_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_business_designation
-- ----------------------------
DROP TABLE IF EXISTS `member_business_designation`;
CREATE TABLE `member_business_designation` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `marketing_business_designation_id` varchar(50) DEFAULT NULL COMMENT '创业称号id',
  `total_team` decimal(10,0) DEFAULT '1' COMMENT '团队总人数',
  `pushing_number` decimal(10,0) DEFAULT NULL COMMENT '直推人数',
  `t_member_id` varchar(50) DEFAULT NULL COMMENT '推广人id',
  `amount_share` decimal(20,2) DEFAULT NULL COMMENT '分红金额',
  `after_shots` decimal(10,0) DEFAULT '1' COMMENT '复投次数',
  `version` bigint(20) DEFAULT '0' COMMENT '乐观锁',
  `completion_times` decimal(3,0) DEFAULT NULL COMMENT '业绩完成次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='创业团队成员';

-- ----------------------------
-- Table structure for member_commission_subsidiary
-- ----------------------------
DROP TABLE IF EXISTS `member_commission_subsidiary`;
CREATE TABLE `member_commission_subsidiary` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `order_type` varchar(1) DEFAULT NULL COMMENT '订单类型；0：普通订单；1：礼包',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单或者礼包id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_designation
-- ----------------------------
DROP TABLE IF EXISTS `member_designation`;
CREATE TABLE `member_designation` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `name` varchar(50) DEFAULT NULL COMMENT '称号名称',
  `logo_addr` varchar(255) DEFAULT NULL COMMENT '图标',
  `direct_referrals` decimal(6,0) DEFAULT NULL COMMENT '直推人数',
  `total_recommend` decimal(6,0) DEFAULT NULL COMMENT '总推荐人数',
  `participation` decimal(6,2) DEFAULT NULL COMMENT '分红资金占比',
  `status` varchar(1) DEFAULT '1' COMMENT '状态：0：停用；1：启用；',
  `close_time` datetime DEFAULT NULL COMMENT '停用时间',
  `close_explain` varchar(200) DEFAULT NULL COMMENT '停用说明',
  `del_explain` varchar(200) DEFAULT NULL COMMENT '删除说明',
  `balance` decimal(20,2) DEFAULT NULL COMMENT '余额',
  `recorded_money` decimal(20,2) DEFAULT NULL COMMENT '累计入账资金',
  `enter_money` decimal(20,2) DEFAULT NULL COMMENT '累计出账资金',
  `sort` decimal(5,0) DEFAULT NULL COMMENT '级别，数字越小称号越低',
  `is_average` varchar(1) DEFAULT '1' COMMENT '是否均分；0：否；1：是',
  `low_level_dividends` varchar(1) DEFAULT '0' COMMENT '是否参与低级分红；0：否；1：是',
  `member_designations` varchar(500) DEFAULT NULL COMMENT '低级称号id；多用逗号隔开',
  `custom_remark` varchar(5000) DEFAULT NULL COMMENT '称号规则描述',
  `straight_push_id` varchar(50) DEFAULT NULL COMMENT '关联直推的下次称号id',
  `straight_push_prople` decimal(9,0) DEFAULT NULL COMMENT '关联直推人数',
  `total_teams` decimal(20,0) DEFAULT NULL COMMENT '团队人数',
  `is_open_money` varchar(1) DEFAULT '0' COMMENT '是否开启资金分配；0：否；1：是',
  `marketing_gift_bag_id` varchar(50) DEFAULT NULL COMMENT '礼包id',
  `gift_total_sales` decimal(9,2) DEFAULT NULL COMMENT '团队礼包销售总额',
  `member_designation_group_id` varchar(50) DEFAULT NULL COMMENT '称号团队id',
  `is_default` varchar(1) DEFAULT '0' COMMENT '默认称号: 0: 为否 1:为是',
  `is_buy_giftbag` varchar(1) DEFAULT '0' COMMENT '是否以购买礼包作为获得当前称号的获取条件：0为否；1为是；只有当前值为1时，礼包中设置赠送称选项中才能显示该称号并对其设置',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_marketing_gift_bag_id` (`marketing_gift_bag_id`) USING BTREE,
  KEY `index_member_designation_group_id` (`member_designation_group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员称号';

-- ----------------------------
-- Table structure for member_designation_count
-- ----------------------------
DROP TABLE IF EXISTS `member_designation_count`;
CREATE TABLE `member_designation_count` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `member_designation_id` varchar(50) DEFAULT NULL COMMENT '称号id',
  `total_members` decimal(15,0) DEFAULT NULL COMMENT '称号总人数',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_designation_id` (`member_designation_id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员团队和称号关系';

-- ----------------------------
-- Table structure for member_designation_group
-- ----------------------------
DROP TABLE IF EXISTS `member_designation_group`;
CREATE TABLE `member_designation_group` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `group_name` varchar(200) DEFAULT NULL COMMENT '团队名称',
  `member_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `total_members` decimal(15,0) DEFAULT NULL COMMENT '团队总人数',
  `store_manage_id` varchar(50) DEFAULT NULL COMMENT '店铺id',
  `del_explain` varchar(300) DEFAULT NULL COMMENT '删除说明',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_id` (`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员团队管理';

-- ----------------------------
-- Table structure for member_designation_member_list
-- ----------------------------
DROP TABLE IF EXISTS `member_designation_member_list`;
CREATE TABLE `member_designation_member_list` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `member_designation_id` varchar(50) DEFAULT NULL COMMENT '称号id',
  `old_t_manage_id` varchar(50) DEFAULT NULL COMMENT '原团队管理关系会员id',
  `t_manage_id` varchar(50) DEFAULT NULL COMMENT '团队管理关系会员id',
  `total_members` decimal(15,0) DEFAULT '1' COMMENT '团队网状总人数',
  `is_buy_gift` varchar(1) DEFAULT '0' COMMENT '是否购买礼包；0：否；1：是',
  `member_join_time` datetime DEFAULT NULL COMMENT '成员加入时间',
  `is_change` varchar(1) DEFAULT '0' COMMENT '是否变更；0：未变更；1:变更',
  `change_time` datetime DEFAULT NULL COMMENT '团队变更时间',
  `total_gift_sales` decimal(20,2) DEFAULT NULL COMMENT '团队礼包销售总额',
  `member_designation_group_id` varchar(50) DEFAULT NULL COMMENT '团队管理id',
  `buy_count` decimal(10,0) DEFAULT '1' COMMENT '购买次数',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_designation_group_id` (`member_designation_group_id`) USING BTREE,
  KEY `index_member_designation_id` (`member_designation_id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE,
  KEY `index_old_t_manage_id` (`old_t_manage_id`) USING BTREE,
  KEY `index_t_manage_id` (`t_manage_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_distribution_level
-- ----------------------------
DROP TABLE IF EXISTS `member_distribution_level`;
CREATE TABLE `member_distribution_level` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `direct` decimal(20,0) DEFAULT NULL COMMENT '直推人数',
  `team_number` decimal(20,0) DEFAULT '1' COMMENT '团队人数',
  `marketing_distribution_level_id` varchar(50) DEFAULT NULL COMMENT '分销等级id',
  `upgrade_direct` decimal(20,0) DEFAULT NULL COMMENT '分销升级直推',
  `upgrade_team_number` decimal(20,0) DEFAULT '1' COMMENT '分销升级团队',
  `version` bigint(20) DEFAULT '0' COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_marketing_distribution_level_id` (`marketing_distribution_level_id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员和分销级别关系';

-- ----------------------------
-- Table structure for member_distribution_record
-- ----------------------------
DROP TABLE IF EXISTS `member_distribution_record`;
CREATE TABLE `member_distribution_record` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_recharge_record_id` varchar(50) DEFAULT NULL COMMENT '会员余额明细',
  `good_picture` varchar(2000) DEFAULT NULL COMMENT '商品图片',
  `good_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `good_specification` varchar(50) DEFAULT NULL COMMENT '商品规格',
  `commission` decimal(9,2) DEFAULT NULL COMMENT '佣金',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_recharge_record_id` (`member_recharge_record_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_equities
-- ----------------------------
DROP TABLE IF EXISTS `member_equities`;
CREATE TABLE `member_equities` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  `equities` text COMMENT '会员权益',
  `cover_plan` varchar(255) DEFAULT NULL COMMENT '分享图',
  `posters` varchar(255) DEFAULT NULL COMMENT '海报图',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_give_welfare_payments
-- ----------------------------
DROP TABLE IF EXISTS `member_give_welfare_payments`;
CREATE TABLE `member_give_welfare_payments` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `give_welfare_payments` decimal(20,2) DEFAULT NULL COMMENT '可获赠数量',
  `version` bigint(20) DEFAULT '0' COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员福利金可获赠数量';

-- ----------------------------
-- Table structure for member_give_welfare_payments_record
-- ----------------------------
DROP TABLE IF EXISTS `member_give_welfare_payments_record`;
CREATE TABLE `member_give_welfare_payments_record` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_give_welfare_payments_id` varchar(50) DEFAULT NULL COMMENT '可获赠福利金id',
  `serial_number` varchar(50) DEFAULT NULL COMMENT '流水号',
  `trade_type` varchar(2) DEFAULT '0' COMMENT '交易类型',
  `go_and_come` varchar(1) DEFAULT '0' COMMENT '进出账；0：入账；1：出账',
  `welfare_payments` decimal(20,2) DEFAULT NULL COMMENT '交易金额',
  `total_welfare_payments` decimal(20,2) DEFAULT NULL COMMENT '总金额',
  `pay_time` datetime DEFAULT NULL COMMENT '交易时间',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '交易单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='福利金可获赠数量记录';

-- ----------------------------
-- Table structure for member_goods_collection
-- ----------------------------
DROP TABLE IF EXISTS `member_goods_collection`;
CREATE TABLE `member_goods_collection` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `good_type` varchar(1) DEFAULT NULL COMMENT '商品的类型;0:店铺商品；1：平台商品',
  `good_list_id` varchar(50) DEFAULT NULL COMMENT '平台商品id，可以为空',
  `good_store_list_id` varchar(50) DEFAULT NULL COMMENT '店铺商品id，可以为空',
  `collection_time` datetime DEFAULT NULL COMMENT '收藏时间',
  `collect_price` varchar(50) DEFAULT NULL COMMENT '收藏时价格',
  `prefecture_label` varchar(10) DEFAULT NULL COMMENT '专区标签',
  `marketing_prefecture_id` varchar(50) DEFAULT NULL COMMENT '专区id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE,
  KEY `index_good_list_id` (`good_list_id`) USING BTREE,
  KEY `index_good_store_list_id` (`good_store_list_id`) USING BTREE,
  KEY `index_marketing_prefecture_id` (`marketing_prefecture_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_grade
-- ----------------------------
DROP TABLE IF EXISTS `member_grade`;
CREATE TABLE `member_grade` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `grade_name` varchar(50) DEFAULT NULL COMMENT '等级名称',
  `grade_logo` varchar(200) DEFAULT NULL COMMENT '等级图标',
  `growth_value_small` decimal(15,2) DEFAULT NULL COMMENT '成长值最小',
  `growth_value_big` decimal(15,2) DEFAULT NULL COMMENT '成长值最大',
  `sort` decimal(9,0) DEFAULT NULL COMMENT '级别，数字越小等级越低',
  `discount` decimal(6,2) DEFAULT NULL COMMENT '折扣',
  `status` varchar(1) DEFAULT '1' COMMENT '状态；0：停用；1：启用',
  `close_time` datetime DEFAULT NULL COMMENT '停用时间',
  `close_explain` varchar(200) DEFAULT NULL COMMENT '停用说明',
  `del_explain` varchar(200) DEFAULT NULL COMMENT '删除说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员等级';

-- ----------------------------
-- Table structure for member_growth_record
-- ----------------------------
DROP TABLE IF EXISTS `member_growth_record`;
CREATE TABLE `member_growth_record` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '交易单号',
  `trade_type` varchar(1) DEFAULT NULL COMMENT '会员成长值来源字典growth_value_source：0：订单交易；1：店铺订单交易；2：礼包购买；3：兑换券购买；4：平台补发；5：购买采购礼包',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `growth_value` decimal(9,2) DEFAULT NULL COMMENT '成长值',
  `order_no` varchar(50) DEFAULT NULL COMMENT '流水号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_list
-- ----------------------------
DROP TABLE IF EXISTS `member_list`;
CREATE TABLE `member_list` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(4) DEFAULT NULL COMMENT '创建年',
  `month` int(2) DEFAULT NULL COMMENT '创建月',
  `day` int(2) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) CHARACTER SET utf8 DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `head_portrait` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '头像绝对地址',
  `phone` varchar(13) CHARACTER SET utf8 DEFAULT NULL COMMENT '手机号',
  `nick_name` varchar(100) DEFAULT NULL COMMENT '会员昵称',
  `sex` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '性别：0：未知；1：男；2：女；数据字典sex',
  `area_addr` varchar(150) CHARACTER SET utf8 DEFAULT NULL COMMENT '地区',
  `member_type` varchar(1) CHARACTER SET utf8 DEFAULT '0' COMMENT '会员类型：0：普通会员；1：vip会员；数据字典：member_type',
  `welfare_payments` decimal(20,2) DEFAULT NULL COMMENT '福利金',
  `balance` decimal(20,2) DEFAULT NULL COMMENT '余额',
  `is_open_store` varchar(1) CHARACTER SET utf8 DEFAULT NULL COMMENT '是否开店；0：否；1：是',
  `vip_time` datetime DEFAULT NULL COMMENT '加入vip的时间',
  `status` varchar(1) CHARACTER SET utf8 DEFAULT NULL COMMENT '状态：0：停用：1：启用',
  `stop_remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '停用说明',
  `openid` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '微信的openid',
  `session_key` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '微信使用',
  `sys_user_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '归属店铺id',
  `promoter_type` varchar(1) CHARACTER SET utf8 DEFAULT '2' COMMENT '推广人类型;0:店铺；1：会员；2：平台',
  `promoter` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '店铺或者会员id',
  `total_commission` decimal(20,2) DEFAULT NULL COMMENT '累计佣金',
  `have_withdrawal` decimal(20,2) DEFAULT NULL COMMENT '已提现',
  `account_frozen` decimal(20,2) DEFAULT NULL COMMENT '冻结金额',
  `share_times` decimal(4,0) DEFAULT '5' COMMENT '分享次数',
  `sys_smallcode_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '二维码表',
  `qrcode_addr` varchar(100) DEFAULT NULL COMMENT '用户二维码',
  `welfare_payments_frozen` decimal(20,2) DEFAULT NULL COMMENT '冻结福利金',
  `unusable_frozen` decimal(20,2) DEFAULT NULL COMMENT '不可用金额',
  `remark` varchar(150) DEFAULT NULL COMMENT '备注',
  `welfare_payments_unusable` decimal(20,2) DEFAULT NULL COMMENT '不可用福利金',
  `member_grade_id` varchar(50) DEFAULT NULL COMMENT '会员等级id',
  `growth_value` decimal(10,2) DEFAULT NULL COMMENT '成长值',
  `app_openid` varchar(50) DEFAULT NULL COMMENT 'app登录的openid',
  `union_id` varchar(50) DEFAULT NULL COMMENT '公共id',
  `transaction_password` varchar(100) DEFAULT NULL COMMENT '交易密码',
  `discount_time` decimal(5,0) DEFAULT NULL COMMENT '优惠次数',
  `integral` decimal(20,2) DEFAULT NULL COMMENT '积分',
  `sign_count` decimal(9,0) DEFAULT NULL COMMENT '连续签到次数',
  `fourth_integral` decimal(20,2) DEFAULT NULL COMMENT '第四积分',
  `account_number` varchar(50) DEFAULT NULL COMMENT '账号',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `promotion_code` varchar(10) DEFAULT NULL COMMENT '推广码',
  `version` bigint(20) DEFAULT '0' COMMENT '乐观锁',
  `mail` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `gz_openid` varchar(50) DEFAULT NULL COMMENT '微信公众号openid',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_promoter` (`promoter`) USING BTREE,
  KEY `index_sys_user_id` (`sys_user_id`) USING BTREE,
  KEY `index_member_grade_id` (`member_grade_id`) USING BTREE,
  KEY `index_openid` (`openid`) USING BTREE,
  KEY `index_phone` (`phone`) USING BTREE,
  KEY `index_union_id` (`union_id`) USING BTREE,
  KEY `index_account_number` (`account_number`) USING BTREE,
  KEY `index_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员列表';

-- ----------------------------
-- Table structure for member_marketing_distribution_level
-- ----------------------------
DROP TABLE IF EXISTS `member_marketing_distribution_level`;
CREATE TABLE `member_marketing_distribution_level` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(11) DEFAULT NULL COMMENT '创建年',
  `month` int(11) DEFAULT NULL COMMENT '创建月',
  `day` int(11) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `t_member_list_id` varchar(50) DEFAULT NULL COMMENT '会员推广的会员id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE,
  KEY `index_t_member_list_id` (`t_member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分销各个级别的人数统计';

-- ----------------------------
-- Table structure for member_recharge_record
-- ----------------------------
DROP TABLE IF EXISTS `member_recharge_record`;
CREATE TABLE `member_recharge_record` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(11) DEFAULT NULL COMMENT '创建年',
  `month` int(11) DEFAULT NULL COMMENT '创建月',
  `day` int(11) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `pay_type` varchar(2) DEFAULT NULL COMMENT '交易类型；0：订单交易；1：余额提现；2：订单退款；3：分销奖励；4：推荐开店奖励；5：礼包推广奖励；6：福利金推广奖励；7：兑换券推广奖励；8：称号分红；9：采购礼包直推奖励；10：采购礼包间推奖励，11：免单专区,12:回购；做成数据字典member_deal_type',
  `go_and_come` varchar(1) DEFAULT NULL COMMENT '支付和收入；0：收入；1：支出',
  `amount` decimal(20,2) DEFAULT NULL COMMENT '交易金额',
  `trade_status` varchar(2) DEFAULT NULL COMMENT '交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status',
  `order_no` varchar(50) DEFAULT NULL COMMENT '单号',
  `member_bank_card_id` varchar(50) DEFAULT NULL COMMENT '会员银行卡id',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作人',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `payment` varchar(1) DEFAULT NULL COMMENT '支付方式；0:微信支付；1：支付宝支付',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '交易单号',
  `trade_type` varchar(1) DEFAULT NULL COMMENT '交易类型；0：订单；1：礼包',
  `member_level` varchar(1) DEFAULT NULL COMMENT '成员级别；1：一级；2：二级',
  `member_withdraw_deposit_id` varchar(50) DEFAULT NULL COMMENT '会员提现审批id',
  `t_member_list_id` varchar(50) DEFAULT NULL COMMENT '推广会员id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_bank_card_id` (`member_bank_card_id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE,
  KEY `index_member_withdraw_deposit_id` (`member_withdraw_deposit_id`) USING BTREE,
  KEY `index_t_member_list_id` (`t_member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_search_history
-- ----------------------------
DROP TABLE IF EXISTS `member_search_history`;
CREATE TABLE `member_search_history` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(11) DEFAULT NULL COMMENT '创建年',
  `month` int(11) DEFAULT NULL COMMENT '创建月',
  `day` int(11) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `name` varchar(100) DEFAULT NULL COMMENT '搜索词名',
  `times` decimal(10,0) DEFAULT NULL COMMENT '搜索次数',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_shipping_address
-- ----------------------------
DROP TABLE IF EXISTS `member_shipping_address`;
CREATE TABLE `member_shipping_address` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(11) DEFAULT NULL COMMENT '创建年',
  `month` int(11) DEFAULT NULL COMMENT '创建月',
  `day` int(11) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `sys_area_id` varchar(50) DEFAULT NULL COMMENT '区域id',
  `area_explan` varchar(150) DEFAULT NULL COMMENT '城市描述',
  `area_address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `linkman` varchar(100) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(13) DEFAULT NULL COMMENT '手机号',
  `is_default` varchar(1) DEFAULT NULL COMMENT '是否默认；0：否；1：是',
  `house_number` varchar(30) DEFAULT NULL COMMENT '门牌号',
  `area_explan_ids` varchar(100) DEFAULT '' COMMENT '区域拼接',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `member_shopping_cart`;
CREATE TABLE `member_shopping_cart` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(11) DEFAULT NULL COMMENT '创建年',
  `month` int(11) DEFAULT NULL COMMENT '创建月',
  `day` int(11) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `good_type` varchar(1) DEFAULT NULL COMMENT '商品的类型;0:店铺商品；1：平台商品',
  `good_list_id` varchar(50) DEFAULT NULL COMMENT '平台商品id，可以为空',
  `good_store_list_id` varchar(50) DEFAULT NULL COMMENT '店铺商品id，可以为空',
  `add_time` datetime DEFAULT NULL COMMENT '加入时间',
  `add_price` decimal(9,2) DEFAULT NULL COMMENT '加入时价格',
  `good_store_specification_id` varchar(50) DEFAULT NULL COMMENT '店铺商品规格id',
  `good_specification_id` varchar(50) DEFAULT NULL COMMENT '商品规格id',
  `quantity` decimal(6,0) DEFAULT NULL COMMENT '商品数量',
  `is_view` varchar(1) DEFAULT NULL COMMENT '是否显示；0：不显示；1：显示',
  `marketing_prefecture_id` varchar(50) DEFAULT NULL COMMENT '平台专区id',
  `prefecture_label` varchar(10) DEFAULT NULL COMMENT '专区标签',
  `marketing_certificate_record_id` varchar(50) DEFAULT NULL COMMENT '兑换券记录id',
  `marketing_free_good_list_id` varchar(50) DEFAULT NULL COMMENT '免单活动id',
  `marketing_group_record_id` varchar(50) DEFAULT NULL COMMENT '拼团记录id',
  `sys_user_id` varchar(50) DEFAULT NULL COMMENT '店铺id',
  `marketing_store_gift_card_member_list_id` varchar(50) DEFAULT NULL COMMENT '礼品卡列表id',
  `marketing_rush_group_id` varchar(50) DEFAULT NULL COMMENT '组分id',
  `marketing_league_good_list_id` varchar(50) DEFAULT NULL COMMENT '加盟专区id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_good_list_id` (`good_list_id`) USING BTREE,
  KEY `index_good_specification_id` (`good_specification_id`) USING BTREE,
  KEY `index_good_store_list_id` (`good_store_list_id`) USING BTREE,
  KEY `index_good_store_specification_id` (`good_store_specification_id`) USING BTREE,
  KEY `index_marketing_certificate_record_id` (`marketing_certificate_record_id`) USING BTREE,
  KEY `index_marketing_free_good_list_id` (`marketing_free_good_list_id`) USING BTREE,
  KEY `index_marketing_group_record_id` (`marketing_group_record_id`) USING BTREE,
  KEY `index_marketing_prefecture_id` (`marketing_prefecture_id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE,
  KEY `index_marketing_store_gift_card_member_list_id` (`marketing_store_gift_card_member_list_id`) USING BTREE,
  KEY `index_sys_user_id` (`sys_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_third_integral
-- ----------------------------
DROP TABLE IF EXISTS `member_third_integral`;
CREATE TABLE `member_third_integral` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(11) DEFAULT NULL COMMENT '创建年',
  `month` int(11) DEFAULT NULL COMMENT '创建月',
  `day` int(11) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `integral` decimal(20,2) DEFAULT NULL COMMENT '积分数量',
  `marketing_group_integral_manage_id` varchar(50) DEFAULT NULL COMMENT '拼购管理id',
  `integral_group` varchar(100) DEFAULT NULL COMMENT '分组名称',
  `version` bigint(20) DEFAULT '0' COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_marketing_group_integral_manage_id` (`marketing_group_integral_manage_id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_welfare_payments
-- ----------------------------
DROP TABLE IF EXISTS `member_welfare_payments`;
CREATE TABLE `member_welfare_payments` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(11) DEFAULT NULL COMMENT '创建年',
  `month` int(11) DEFAULT NULL COMMENT '创建月',
  `day` int(11) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员列表id',
  `serial_number` varchar(50) DEFAULT NULL COMMENT '流水号',
  `bargain_payments` decimal(20,2) DEFAULT NULL COMMENT '交易福利金',
  `welfare_payments` decimal(20,2) DEFAULT NULL COMMENT '账户福利金',
  `we_type` varchar(1) DEFAULT NULL COMMENT '类型；0：支出；1：收入',
  `wp_explain` varchar(500) DEFAULT NULL COMMENT '说明',
  `go_and_come` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '来源或者去向',
  `bargain_time` datetime DEFAULT NULL COMMENT '交易时间',
  `operator` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '操作人',
  `is_platform` varchar(1) DEFAULT NULL COMMENT '0：店铺；1：平台；2：会员',
  `is_freeze` varchar(1) DEFAULT '0' COMMENT '是否冻结福利金，0:不是；1：是冻结；2：不可用',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '交易订单号',
  `trade_type` varchar(2) DEFAULT NULL COMMENT '会员福利金交易类型，字典member_welfare_deal_type；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；7：福利金付款；8：进店奖励；9：中奖拼团；10：拼团奖励；11：转入；12：转出；13：挂卖；14：转出失败退款；15：未成团退款；16：取消挂卖退款；17：订单取消；18：水滴兑换；19：消费奖励；20：订单退款；21：拼券付款；22：拼券失败退款；23：抢券付款',
  `trade_status` varchar(2) DEFAULT NULL COMMENT '交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status',
  `version` bigint(20) DEFAULT '0' COMMENT '乐观锁',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_withdraw_deposit
-- ----------------------------
DROP TABLE IF EXISTS `member_withdraw_deposit`;
CREATE TABLE `member_withdraw_deposit` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `year` int(11) DEFAULT NULL COMMENT '创建年',
  `month` int(11) DEFAULT NULL COMMENT '创建月',
  `day` int(11) DEFAULT NULL COMMENT '创建日',
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `member_list_id` varchar(50) DEFAULT NULL COMMENT '会员id',
  `order_no` varchar(50) DEFAULT NULL COMMENT '单号',
  `phone` varchar(13) DEFAULT NULL COMMENT '手机号',
  `money` decimal(20,2) DEFAULT NULL COMMENT '提现金额',
  `withdrawal_type` varchar(1) DEFAULT NULL COMMENT '提现类型；0：微信；1：支付宝;2:银行卡',
  `service_charge` decimal(20,2) DEFAULT NULL COMMENT '手续费',
  `amount` decimal(20,2) DEFAULT NULL COMMENT '实际金额',
  `time_application` datetime DEFAULT NULL COMMENT '申请时间',
  `status` varchar(1) DEFAULT NULL COMMENT '状态；0：待审核；1：待打款；2：已付款；3：无效',
  `pay_time` datetime DEFAULT NULL COMMENT '打款时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `close_explain` varchar(100) DEFAULT NULL COMMENT '无效原因',
  `bank_card` varchar(40) DEFAULT NULL COMMENT '银行卡号(支付宝账号)',
  `bank_name` varchar(40) DEFAULT NULL COMMENT '开户行名称',
  `cardholder` varchar(100) DEFAULT NULL COMMENT '持卡人姓名(真实姓名)',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `receipt_voucher` varchar(500) DEFAULT NULL COMMENT '收款凭证',
  `process_information` varchar(1000) DEFAULT NULL COMMENT '处理信息进度json',
  `opening_bank` varchar(100) DEFAULT NULL COMMENT '开户分支行',
  `identity_number` varchar(50) DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_member_list_id` (`member_list_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
