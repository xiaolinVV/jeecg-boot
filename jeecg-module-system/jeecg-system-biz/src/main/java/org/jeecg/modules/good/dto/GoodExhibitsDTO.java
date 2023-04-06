package org.jeecg.modules.good.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class GoodExhibitsDTO {

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
    private java.util.Date createTime;
    /**修改人*/
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**修改时间*/
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
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
    /**编号*/
    @Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
    private String serialNumber;
    /**主图*/
    @Excel(name = "主图", width = 15)
    @ApiModelProperty(value = "主图")
    private String mainPicture;
    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String exhibitsName;
    /**店铺列表id*/
    @Excel(name = "店铺列表id", width = 15)
    @ApiModelProperty(value = "店铺列表id")
    private String storeManageId;
    /**描述*/
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String exhibitsDescribe;
    /**视频*/
    @Excel(name = "视频", width = 15)
    @ApiModelProperty(value = "视频")
    private String video;
    /**详情图*/
    @Excel(name = "详情图", width = 15)
    @ApiModelProperty(value = "详情图")
    private String detailsFigure;
    /**上下架；0：下架；1：上架*/
    @Excel(name = "上下架；0：下架；1：上架", width = 15)
    @ApiModelProperty(value = "上下架；0：下架；1：上架")
    private String frameStatus;
    /**排序*/
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.math.BigDecimal sort;
    /**状态；0：未通过；1：通过*/
    @Excel(name = "状态；0：未通过；1：通过", width = 15)
    @ApiModelProperty(value = "状态；0：未通过；1：通过")
    private String status;

    private String storeName;
}