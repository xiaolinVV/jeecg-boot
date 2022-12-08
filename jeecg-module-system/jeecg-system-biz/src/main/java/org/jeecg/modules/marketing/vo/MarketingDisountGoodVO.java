package org.jeecg.modules.marketing.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class MarketingDisountGoodVO {
    private static final long serialVersionUID = 1L;
    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;


    /**删除状态（0，正常，1已删除）*/
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic //mybatis-plus配置逻辑
    private String delFlag;
    /**
     * 店铺优惠券id
     */
    @Excel(name = "店铺优惠券id", width = 15)
    @ApiModelProperty(value = "店铺优惠券id")
    private String marketingDiscountId;
    /**
     * 店铺商品id
     */
    @Excel(name = "店铺商品id", width = 15)
    @ApiModelProperty(value = "店铺商品id")
    private String goodId;
    /**
     * 商品图片地址
     */
    private String mainPicture;
    /**
     * 商品名称
     */
    private String goodName;
    /**
     * 商品分类id
     */
    private String goodStoreTypeId;
    /**
     * 商品分类名称
     */
    private String goodStoreTypeName;
    /**
     * 市场价
     */
    private String marketPrice;
    /**
     * 销售价
     */
    private String price;
    /**
     * 成本价
     */
    private String costPrice;
    /***
     * vip价
     */
    private String vipPrice;
    /**
     * 库存
     */
    private String repertory;
    /**平台分类id*/
    private String goodTypeId;
    /**排序*/
    private java.math.BigDecimal sort;
    /**分类别名*/
    private String nickName;
    /**三级分类Id*/
    private String goodTypeIdSan;
    /**二级分类Id*/
    private String goodTypeIdTwo;
    /**一级分类Id*/
    private String goodTypeIdOne;
    /**三级分类名称*/
    private String goodTypeSanName;
    /**二级分类名称*/
    private String goodTypeTwoName;
    /**一级分类名称*/
    private String goodTypeOneName;
    /**字符串拼接分类名称*/
    private String goodTypeNames;

    private String goodStoreTypeIdTwo;
    private String goodStoreTypeIdOne;
    private String goodStoreTypeTwoName;
    private String goodStoreTypeOneName;
    private String goodStoreTypeNames;
}
