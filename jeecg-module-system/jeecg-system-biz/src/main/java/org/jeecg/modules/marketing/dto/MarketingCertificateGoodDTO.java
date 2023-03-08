package org.jeecg.modules.marketing.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MarketingCertificateGoodDTO {
    /**主键ID*/
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**创建人*/
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**修改人*/
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**修改时间*/
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**创建年*/
    @Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
    private Integer year;
    /**创建月*/
    @Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
    private Integer month;
    /**创建日*/
    @Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
    private Integer day;
    /**删除状态（0，正常，1已删除）*/
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    private String delFlag;
    /**兑换券id*/
    @Excel(name = "兑换券id", width = 15)
    @ApiModelProperty(value = "兑换券id")
    private String marketingCertificateId;
    /**平台商品id*/
    @Excel(name = "平台商品id", width = 15)
    @ApiModelProperty(value = "平台商品id")
    private String goodListId;
    /**商品主图相对地址（以json的形式存储多张）*/
    private String mainPicture;
    /**商品名称*/
    private String goodName;
    private String goodTypeIdThree;
    private String goodTypeIdTwo;
    private String goodTypeIdOne;
    private String goodTypeThreeName;
    private String goodTypeTwoName;
    private String goodTypeOneName;
    private String goodTypeNames; //三级分类拼接名称 ：分类 - 分类 - 分类
    /**
     * 分类名称
     */
    private String typeName;
    /**商品市场价*/
    private String marketPrice;
    /**商品销售价格*/
    private String price;
    /**商品成本价*/
    private String costPrice;
    /**商品供货价*/
    private String supplyPrice;
    /**商品会员价，按照利润比例，根据数据字段设置的比例自动填入*/
    private String vipPrice;
    /**库存*/
    private BigDecimal repertory;

    private String realname;//供应商真实名称
    /**
     * 供应商
     */
    private String username;
    /**0:店铺；1：平台*/
    @Excel(name = "0:店铺；1：平台", width = 15)
    @ApiModelProperty(value = "0:店铺；1：平台")
    private String isPlatform;
    /**可选月份，0：代表全部，其他的代表相应的月*/
    @Excel(name = "可选月份，0：代表全部，其他的代表相应的月", width = 15)
    @ApiModelProperty(value = "可选月份，0：代表全部，其他的代表相应的月")
    private String canMonth;
    /**商品规格id*/
    @Excel(name = "商品规格id", width = 15)
    @ApiModelProperty(value = "商品规格id")
    private String goodSpecificationId;
    /**商品数量*/
    @Excel(name = "商品数量", width = 15)
    @ApiModelProperty(value = "商品数量")
    private BigDecimal quantity;

    /**
     * 规格
     */
    private String specifications;
}
