package org.jeecg.modules.member.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MemberGoodsCollectionDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**主键ID*/
    @Excel(name = "主键ID", width = 15)
    private String id;
    /**删除状态（0，正常，1已删除）*/
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    private String delFlag;
    /**收藏时间*/
    @Excel(name = "收藏时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date collectionTime;
    /**头像绝对地址*/
    @Excel(name = "头像绝对地址", width = 15)
    private String headPortrait;
    /**手机号*/
    @Excel(name = "手机号", width = 15)
    private String phone;
    /**会员昵称*/
    @Excel(name = "会员昵称", width = 15)
    private String nickName;
    /**商品名称*/
    @Excel(name = "商品名称", width = 15)
    private String goodName;
    /**商品销售价格*/
    @Excel(name = "商品销售价格", width = 15)
    private String price;
    /**降价金额*/
    @Excel(name = "降价金额", width = 15)
    private String depreciate;
    /**优惠券*/
    @Excel(name = "优惠券", width = 15)
    private String discountCoupon;

    /**上下架；0：下架；1：上架*/
    @Excel(name = "上下架；0：下架；1：上架", width = 15)
    private String frameStatus;
    /**参与活动;对应数据字典字段的活动类型数据，为空代表没有参与活动*/
    @Excel(name = "参与活动;对应数据字典字段的活动类型数据，为空代表没有参与活动", width = 15,dicCode = "activities_type")
    @Dict(dicCode = "activities_type")
    private String activitiesType;
    /**收藏时金额*/
    @Excel(name = "收藏时金额", width = 15)
    private String collectPrice;
    /**收藏时金额*/
    @Excel(name = "当前价格", width = 15)
    private String currentPricing;
    /**查询条件开始时间*/
    @Excel(name = "查询条件开始时间", width = 15)
    private String createTime_begin;
    /**查询条件开始时间*/
    @Excel(name = "查询条件结束时间", width = 15)
    private String createTime_end;
}
