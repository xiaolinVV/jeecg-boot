package org.jeecg.modules.good.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class SearchTermsVO {

    private  String search;        //搜索框查询条件
    private Integer isPlatform;  //平台 1,店铺 0
    private  Integer pattern;     //排序条件
    private  String goodTypeId;  //一级分类id
    private  String goodTypeIdTwo;  //二级分类id
    private  String goodTypeIdThree;  //三级分类id
    private  BigDecimal minPrice;   //价格区间 小价格
    private  BigDecimal maxPrice; //价格区间 最大价格
    private  Integer marketingDiscountGoodCount; //优惠券个数
    private  String sourceType;//来源状态: 1:供应链自营产品 2:京东商品 3.供应链普通商品
    private  String marketingPrefectureId;  //专区id
    private  String marketingPrefectureTypeId;  //专区分类
    private  String  isWelfare;//福利金抵扣 0：不支持；1：支持
    private  String isVipLower;//会员直降  0：不支持；1：支持
    private  String  sysUserId;//店铺uid

 }
