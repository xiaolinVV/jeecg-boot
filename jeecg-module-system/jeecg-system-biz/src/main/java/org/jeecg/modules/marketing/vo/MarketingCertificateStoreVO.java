package org.jeecg.modules.marketing.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MarketingCertificateStoreVO {

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
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
    /**店铺用户id*/
    @Excel(name = "店铺用户id", width = 15)
    @ApiModelProperty(value = "店铺用户id")
    private String sysUserId;
    /**logo图片地址*/
    @Excel(name = "logo图片地址", width = 15)
    @ApiModelProperty(value = "logo图片地址")
    private String logoAddr;
    /**门店名称*/
    @Excel(name = "门店名称", width = 15)
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    /**分店名称*/
    @Excel(name = "分店名称", width = 15)
    @ApiModelProperty(value = "分店名称")
    private String subStoreName;
    /**城市区域地址说明*/
    @Excel(name = "城市区域地址说明", width = 15)
    @ApiModelProperty(value = "城市区域地址说明")
    private String areaAddress;
    /**门店详细地址*/
    @Excel(name = "门店详细地址", width = 15)
    @ApiModelProperty(value = "门店详细地址")
    private String storeAddress;

    private Integer pageNo = 1;

    private Integer pageSize = 10;
}
