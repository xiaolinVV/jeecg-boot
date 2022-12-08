package org.jeecg.modules.marketing.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGoodSpecification;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 专区商品
 * @Author: jeecg-boot
 * @Date: 2020-03-26
 * @Version: V1.0
 */
@Data
@TableName("marketing_prefecture_good")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "marketing_prefecture_good对象", description = "专区商品")
public class MarketingPrefectureGoodDTO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改人
     */
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**
     * 修改时间
     */
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**
     * 创建年
     */
    @Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
    private Integer year;
    /**
     * 创建月
     */
    @Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
    private Integer month;
    /**
     * 创建日
     */
    @Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
    private Integer day;
    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    private String delFlag;
    /**
     * 平台专区id
     */
    @Excel(name = "平台专区id", width = 15)
    @ApiModelProperty(value = "平台专区id")
    private String marketingPrefectureId;
    /**
     * 平台专区类型id
     */
    @Excel(name = "平台专区类型id", width = 15)
    @ApiModelProperty(value = "平台专区类型id")
    private String marketingPrefectureTypeId;
    /**
     * 商品列表id
     */
    @Excel(name = "商品列表id", width = 15)
    @ApiModelProperty(value = "商品列表id")
    private String goodListId;
    /**
     * 状态；0：停用；1：启用
     */
    @Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
    private String status;
    /**
     * 停用说明
     */
    @Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
    private String closeExplian;
    /**
     * 原商品是否可用；0：不可用；1：可用
     */
    @Excel(name = "原商品是否可用；0：不可用；1：可用", width = 15)
    @ApiModelProperty(value = "原商品是否可用；0：不可用；1：可用")
    private String srcStatus;
    /**
     * 专区价
     */
    @Excel(name = "专区价", width = 15)
    @ApiModelProperty(value = "专区价")
    private String prefecturePrice;
    /**
     * 福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣
     */
    @Excel(name = "福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣", width = 15)
    @ApiModelProperty(value = "福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣")
    private String isWelfare;
    /**
     * 福利金限额抵扣比例
     */
    @Excel(name = "福利金限额抵扣比例", width = 15)
    @ApiModelProperty(value = "福利金限额抵扣比例")
    private String welfareProportion;
    /**
     * 赠送福利金；0：不支持；1：支持
     */
    @Excel(name = "赠送福利金；0：不支持；1：支持", width = 15)
    @ApiModelProperty(value = "赠送福利金；0：不支持；1：支持")
    private String isGiveWelfare;
    /**
     * 赠送福利金比例
     */
    @Excel(name = "赠送福利金比例", width = 15)
    @ApiModelProperty(value = "赠送福利金比例")
    private String giveWelfareProportion;
    /**
     * 购买天数;-1：不限制；其他代表天数
     */
    @Excel(name = "购买天数;-1：不限制；其他代表天数", width = 15)
    @ApiModelProperty(value = "购买天数;-1：不限制；其他代表天数")
    private String buyProportionDay;
    /**
     * 可购买件数；-1：不限制；其他代表件数
     */
    @Excel(name = "可购买件数；-1：不限制；其他代表件数", width = 15)
    @ApiModelProperty(value = "可购买件数；-1：不限制；其他代表件数")
    private String buyProportionLetter;
    /**
     * 可购买件数；-1：不限制；其他代表件数
     */
    @Excel(name = "可购买件数；-1：不限制；其他代表件数", width = 15)
    @ApiModelProperty(value = "可购买件数；-1：不限制；其他代表件数")
    private BigDecimal smallPrefecturePrice;
    /**
     * vip会员免福利金；0：不免；1：免
     */
    @Excel(name = "vip会员免福利金；0：不免；1：免", width = 15)
    @ApiModelProperty(value = "vip会员免福利金；0：不免；1：免")
    private String isVipLower;

    private String isViewMarketPrice;
    /***********新增数据*************/
    //规格
    private List<MarketingPrefectureGoodSpecificationDTO> goodListSpecificationList;
    private List<MarketingPrefectureGoodSpecification> marketingPrefectureGoodSpecificationList;
    private String goodName;//商品名称
    private String mainPicture;//商品图片
    private String price;//销售价
    private String costPrice;//成本价
    private String marketPrice;//市场价
    private String repertory;//库存
    private String goodTypeIdThree;//三级分类id
    private String goodTypeIdTwo;//二级分类id
    private String goodTypeIdOne;//一级分类id
    private String goodTypeThreeName;//三级分类名称
    private String goodTypeTwoName;//二级分类名称
    private String goodTypeOneName;//一级分类名称
    private String goodTypeNames;//三级分类拼接
    private String realname;//供应商名称
    private String frameStatus;//上下架状态

    private String typeName;//专区分类名称
    private String goodStatus;//原来商品状态
    /**
     * 查询条件开始时间
     */
    private String createTime_begin;
    /**
     * 查询条件开始时间
     */
    private String createTime_end;

    /**
     * 折扣比例
     */
    private String discount;

    /**
     * 最大折扣比例
     */
    private String maxDiscount;
    /**
     * 最小折扣比例
     */
    private String minDiscount;


    /**
     * 购买人限制：会员类型：0普通会员；1：vip会员 字典：member_type
     */
    @Excel(name = "购买人限制：会员类型：0普通会员；1：vip会员 字典：member_type", width = 15, dicCode = "member_type")
    @ApiModelProperty(value = "购买人限制：会员类型：0普通会员；1：vip会员 字典：member_type")
    @Dict(dicCode = "member_type")
    private String buyerLimit;
    /**
     * 商品折扣
     */
    private String prefecturePriceProportion;

    private String marketingPrefectureRecommendId;

    private String typeId;

    private String typeOneId;

    private String typeTwoId;

    private BigDecimal sort;

    private String isIntegral;
}
