package org.jeecg.modules.member.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MemberShippingAddressDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**主键ID*/
    @Excel(name = "主键ID", width = 15)
    private String id;
    /**头像绝对地址*/
    @Excel(name = "头像绝对地址", width = 15)
    @ApiModelProperty(value = "头像绝对地址")
    private String headPortrait;
    /**手机号*/
    @Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**微信昵称*/
    @Excel(name = "微信昵称", width = 15)
    @ApiModelProperty(value = "微信昵称")
    private String nickName;
    /**联系人*/
    @Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private String linkman;
    /**手机号*/
    @Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private String iPhone;
    /**城市描述*/
    @Excel(name = "城市描述", width = 15)
    @ApiModelProperty(value = "城市描述")
    private String areaExplan;
    /**详细地址*/
    @Excel(name = "详细地址", width = 15)
    @ApiModelProperty(value = "详细地址")
    private String areaAddress;
    /**是否默认；0：否；1：是*/
    @Excel(name = "是否默认；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否默认；0：否；1：是")
    private String isDefault;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @Excel(name = "门牌号", width = 15)
    @ApiModelProperty(value = "门牌号")
    private String houseNumber;
    /**删除状态（0，正常，1已删除）*/
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    private String delFlag;
    /**查询条件开始时间*/
    @Excel(name = "查询条件开始时间", width = 15)
    private String createTime_begin;
    /**查询条件开始时间*/
    @Excel(name = "查询条件结束时间", width = 15)
    private String createTime_end;
}
