package org.jeecg.modules.member.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MemberAttentionStoreVo implements Serializable{
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
    /**门店详细地址*/
    @Excel(name = "门店详细地址", width = 15)
    @ApiModelProperty(value = "门店详细地址")
    private String storeAddress;
    /**综合评分*/
    @Excel(name = "综合评分", width = 15)
    @ApiModelProperty(value = "综合评分")
    private java.math.BigDecimal comprehensiveEvaluation;
    /**状态：0:停用；1：启用*/
    @Excel(name = "状态：0:停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态：0:停用；1：启用")
    private String status;
    /**关注时间*/
    @Excel(name = "关注时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "关注时间")
    private Date attentionTime;
    /**
     * 参与活动
     */
    @Excel(name = "参与活动;对应数据字典字段的活动类型数据，为空代表没有参与活动", width = 15,dicCode = "activities_type")
    @ApiModelProperty(value = "参与活动;对应数据字典字段的活动类型数据，为空代表没有参与活动")
    @Dict(dicCode = "activities_type")
    private String activitiesType;

    /**查询条件开始时间*/
    @Excel(name = "查询条件开始时间", width = 15)
    private String createTime_begin;
    /**查询条件开始时间*/
    @Excel(name = "查询条件结束时间", width = 15)
    private String createTime_end;
    @ExcelCollection(name = "会员列表")
    private List<MemberList>memberListList;
}
