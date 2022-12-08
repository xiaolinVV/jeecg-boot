package org.jeecg.modules.marketing.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MarketingChannelDTO {

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**渠道名称*/
    @Excel(name = "渠道名称", width = 15)
    @ApiModelProperty(value = "渠道名称")
    private String name;
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
    /**状态；0：停用；1：启用*/
    @Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
    private String status;
    /**
     * 停用说明
     */
    @Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
    private String statusExplain;
    /**
     * 渠道英文名
     */
    @Excel(name = "渠道英文名", width = 15)
    @ApiModelProperty(value = "渠道英文名")
    private String englishName;

    /**
     *发行量
     */
    private String total ;
    /**
     *领券量
     */
    private String discountGet ;
    /**
     *状态
     */
    private String statusName ;

}